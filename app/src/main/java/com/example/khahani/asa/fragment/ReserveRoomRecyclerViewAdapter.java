package com.example.khahani.asa.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.khahani.asa.R;
import com.example.khahani.asa.fragment.ReserveRoomFragment.OnListFragmentInteractionListener;
import com.example.khahani.asa.model.capacities.Message;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReserveRoomRecyclerViewAdapter extends RecyclerView.Adapter<ReserveRoomRecyclerViewAdapter.ViewHolder> {


    private final OnListFragmentInteractionListener mListener;
    private final List<com.example.khahani.asa.model.roomkinds.Message> mRoomkinds;
    private final List<ReserveRoomViewModel> viewModels;
    private Context mContext;

    public ReserveRoomRecyclerViewAdapter(List<com.example.khahani.asa.model.roomkinds.Message> mRoomkinds,
                                          List<ReserveRoomViewModel> viewModels,
                                          OnListFragmentInteractionListener listener) {
        this.mRoomkinds = mRoomkinds;
        this.viewModels = viewModels;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reserveroom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mReserveRoomViewModel = viewModels.get(position);

        for (int i = 0; i < mRoomkinds.size(); i++) {
            if (holder.mReserveRoomViewModel.room_kind_id.equals(mRoomkinds.get(i).id)) {
                holder.mRoomkind = mRoomkinds.get(i);
                break;
            }
        }


        holder.mTitleRoomCapcity.setText(mContext.getResources().getString(R.string.titleRoomCapacity, holder.mRoomkind.room_kind_bed));

        holder.mTitleRoomExtraCapcity.setText(mContext.getResources()
                .getString(R.string.titleRoomExtraCapacity, holder.mRoomkind.extra_bed));

        holder.mTextViewTitleRoomkindName.setText(holder.mRoomkind.persian_hotel_label);


        if (Integer.parseInt(holder.mReserveRoomViewModel.lunch_dinner_person.get(0)) <= 0){
            holder.mRadioButtonFullBoard.setEnabled(false);
        }

        if (Integer.parseInt(holder.mReserveRoomViewModel.iranian_daily_board_rate_TSI.get(0)) <= 0){
            holder.mRadioButtonBreakfast.setEnabled(false);
        }

        stepFoodTypeAndChanges(holder);

        holder.mSpinnerRoomsCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.mReserveRoomViewModel.selectedRoomsCount = position;
                holder.mReserveRoomViewModel.selectedAdultsCount = 0;
                holder.mReserveRoomViewModel.selectedChildsCount = 0;
                updateSpinnersAdults(holder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.mSpinnerAdultsCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.mReserveRoomViewModel.selectedAdultsCount= position;
                holder.mReserveRoomViewModel.selectedChildsCount = 0;
                updateSpinnerChilds(holder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.mSpinnerChildsCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.mReserveRoomViewModel.selectedChildsCount = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updateSpinnerRooms(holder);
        updateSpinnersAdults(holder);
        updateSpinnerChilds(holder);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mCapacity);
                }
            }
        });

        holder.mRadioGroupFood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                stepFoodTypeAndChanges(holder);

            }
        });
    }

    private void stepFoodTypeAndChanges(ViewHolder holder) {

        holder.mReserveRoomViewModel.selectedFoodType = ReserveRoomViewModel.FOODTYPE_NONE;

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        holder.mTextViewTitleFixedBoardRateWithOff.setVisibility(View.VISIBLE);
        holder.mTextViewTitleFixedExtraBoardRate.setVisibility(View.VISIBLE);

        if (holder.mRadioButtonBreakfast.isChecked()) {

            holder.mReserveRoomViewModel.selectedFoodType = ReserveRoomViewModel.FOODTYPE_BREAKFAST;

            String iranian_daily_board_rate_TSI = formatter.format(
                    Integer.parseInt(holder.mReserveRoomViewModel.iranian_daily_board_rate_TSI.get(0)));
            String iranian_daily_board_rate_with_off = formatter.format(
                    Integer.parseInt(holder.mReserveRoomViewModel.iranian_daily_board_rate_TSI.get(0)) * 80 / 100);
            String iranian_extra_bed_rate_TSI = formatter.format(
                    Integer.parseInt(holder.mReserveRoomViewModel.iranian_extra_bed_rate_TSI.get(0)));
            String iranian_child_rate_TSI = formatter.format(
                    Integer.parseInt(holder.mReserveRoomViewModel.iranian_child_rate_TSI.get(0)));


            holder.mTextViewTitleFixedBoardRate.setText(mContext.getResources()
                    .getString(R.string.titleFixedBoardRate, iranian_daily_board_rate_TSI));

            holder.mTextViewTitleFixedBoardRate
                    .setPaintFlags(holder.mTextViewTitleFixedBoardRate.getPaintFlags() |
                            Paint.STRIKE_THRU_TEXT_FLAG);

            holder.mTextViewTitleFixedBoardRateWithOff.setText(mContext.getResources()
                    .getString(R.string.titleFixedBoardRate, iranian_daily_board_rate_with_off));

            holder.mTextViewTitleFixedExtraBoardRate.setText(mContext.getResources()
                    .getString(R.string.titleFixedExtraBoardRate, iranian_extra_bed_rate_TSI));

            holder.mTextViewTitleFixedChildBoardRate.setText(mContext.getResources()
                    .getString(R.string.titleFixedChildBoardRate, iranian_child_rate_TSI));

        }

        if (holder.mRadioButtonFullBoard.isChecked()){

            holder.mReserveRoomViewModel.selectedFoodType =
                    ReserveRoomViewModel.FOODTYPE_BREAKFAST_LUNCH_DINNER;

            String lunch_dinner_person = formatter.format(
                    Integer.parseInt(holder.mReserveRoomViewModel.lunch_dinner_person.get(0)));
            String lunch_dinner_person_child = formatter.format(
                    Integer.parseInt(holder.mReserveRoomViewModel.lunch_dinner_person.get(0)) / 2);


            holder.mTextViewTitleFixedBoardRate.setText(mContext.getResources()
                    .getString(R.string.titleFixedBoardRate, lunch_dinner_person));

            holder.mTextViewTitleFixedBoardRate
                    .setPaintFlags(holder.mTextViewTitleFixedBoardRate.getPaintFlags() &
                            (~ Paint.STRIKE_THRU_TEXT_FLAG));

            holder.mTextViewTitleFixedBoardRateWithOff.setVisibility(View.GONE);
            holder.mTextViewTitleFixedExtraBoardRate.setVisibility(View.GONE);

            holder.mTextViewTitleFixedChildBoardRate.setText(mContext.getResources()
                    .getString(R.string.titleFixedChildBoardRate, lunch_dinner_person_child));


        }
    }

    private void calcPrice(){
        if(mListener != null){
            mListener.onCalcPrice(viewModels);
        }
    }

    private void updateSpinnerRooms(ViewHolder holder){
        /*      Spinner Rooms start  */

        List<String> spinnerRoomsData = new ArrayList<>();
        for (int i = 0;
             i <= Integer.parseInt(holder.mReserveRoomViewModel.room_availibility);
             i++) {
            spinnerRoomsData.add(Integer.toString(i));
        }

        ArrayAdapter<String> roomsAdapter =
                new ArrayAdapter<>(mContext,
                        android.R.layout.simple_list_item_1, spinnerRoomsData);

        roomsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mSpinnerRoomsCount.setAdapter(roomsAdapter);
        /*      Spinner Rooms end  */
    }

    private void updateSpinnerChilds(ViewHolder holder) {

        int selectedRoomCount = holder.mReserveRoomViewModel.selectedRoomsCount;
        int selectedAdultCount = holder.mReserveRoomViewModel.selectedAdultsCount;

        int maxRoomCapacity = Integer.parseInt(holder.mRoomkind.extra_bed) +
                Integer.parseInt(holder.mRoomkind.room_kind_bed);

        /*      Spinner Childs Start    */

        List<String> spinnerChildsData = new ArrayList<>();

        if (selectedAdultCount <= 0){
            spinnerChildsData.add("0");
        }else {
            for (int i = 0;
                 i <= (maxRoomCapacity * selectedRoomCount) - selectedAdultCount;
                 i++) {
                spinnerChildsData.add(Integer.toString(i));
            }
        }

        ArrayAdapter<String> childsAdapter =
                new ArrayAdapter<>(mContext,
                        android.R.layout.simple_list_item_1, spinnerChildsData);

        childsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mSpinnerChildsCount.setAdapter(childsAdapter);

        /*      Spinner Childs End    */


    }

    private void updateSpinnersAdults(ViewHolder holder) {

        int selectedRoomCount = holder.mReserveRoomViewModel.selectedRoomsCount;

        int maxRoomCapacity = Integer.parseInt(holder.mRoomkind.extra_bed) +
                Integer.parseInt(holder.mRoomkind.room_kind_bed);

        /*      Spinner Adults Start    */

        List<String> spinnerAdultsData = new ArrayList<>();
        for (int i = 0;
             i <= maxRoomCapacity * selectedRoomCount;
             i++) {
            spinnerAdultsData.add(Integer.toString(i));
        }

        ArrayAdapter<String> adultsAdapter =
                new ArrayAdapter<>(mContext,
                        android.R.layout.simple_list_item_1, spinnerAdultsData);

        adultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mSpinnerAdultsCount.setAdapter(adultsAdapter);

        /*      Spinner Adults End    */
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public List<ReserveRoomViewModel> getModel() {
        return viewModels;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleRoomCapcity;
        public final TextView mTitleRoomExtraCapcity;
        public final TextView mTextViewTitleRoomkindName;
        public final TextView mTextViewTitleFixedBoardRate;
        public final TextView mTextViewTitleFixedExtraBoardRate;
        public final TextView mTextViewTitleFixedChildBoardRate;
        public final TextView mTextViewTitleFixedBoardRateWithOff;
        public final Spinner mSpinnerRoomsCount;
        public final Spinner mSpinnerAdultsCount;
        public final Spinner mSpinnerChildsCount;

        public final RadioGroup mRadioGroupFood;
        public final RadioButton mRadioButtonBreakfast;
        public final RadioButton mRadioButtonFullBoard;


        //    public final TextView mGuestRate;
        public Message mCapacity;
        public ReserveRoomViewModel mReserveRoomViewModel;
        public com.example.khahani.asa.model.roomkinds.Message mRoomkind;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleRoomCapcity = view.findViewById(R.id.textViewTitleRoomCapacity);
            mTitleRoomExtraCapcity = view.findViewById(R.id.textViewTitleRoomExtraCapacity);
            mTextViewTitleRoomkindName = view.findViewById(R.id.textViewTitleRoomkindName);
            mTextViewTitleFixedBoardRate = view.findViewById(R.id.textViewFixedBoardRate);
            mTextViewTitleFixedBoardRateWithOff = view.findViewById(R.id.textViewFixedBoardRateWithOff);
            mTextViewTitleFixedExtraBoardRate = view.findViewById(R.id.textViewFixedExtraBoardRate);
            mTextViewTitleFixedChildBoardRate = view.findViewById(R.id.textViewFixedChildBoardRate);
            mSpinnerRoomsCount = view.findViewById(R.id.spinnerRoomsCount);
            mSpinnerAdultsCount = view.findViewById(R.id.spinnerAdultsCount);
            mSpinnerChildsCount = view.findViewById(R.id.spinnerChildsCount);

            mRadioGroupFood = view.findViewById(R.id.radioGroupFood);
            mRadioButtonBreakfast = view.findViewById(R.id.radioButtonBreakfastRoom);
            mRadioButtonFullBoard = view.findViewById(R.id.radioButtonFullBoardRoom);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
