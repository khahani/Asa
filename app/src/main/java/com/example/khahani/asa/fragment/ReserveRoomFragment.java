package com.example.khahani.asa.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khahani.asa.R;
import com.example.khahani.asa.model.capacities.CapacitiesResponse;
import com.example.khahani.asa.model.capacities.Message;
import com.example.khahani.asa.model.roomkinds.RoomkindsResponse;
import com.example.khahani.asa.ret.AsaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ReserveRoomFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_ID_HOTEL = "id_hotel";
    private static final String ARG_ID_CITY = "id_city";
    private static final String ARG_FROM_DATE = "from_date";
    private static final String ARG_NIGHT_NUMBERS = "night_numbers";
    private static final String TAG = ReserveRoomFragment.class.getSimpleName();
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private List<com.example.khahani.asa.model.roomkinds.Message> mRoomkinds;
    private ReserveRoomRecyclerViewAdapter mReserveRoomRecyclerViewAdapter;
    private String id_hotel;
    private String id_city;
    private String from_date;
    private String night_numbers;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReserveRoomFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ReserveRoomFragment newInstance(
            int columnCount,String id_city,
            String id_hotel, String from_date, String night_numbers) {

        ReserveRoomFragment fragment = new ReserveRoomFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_ID_HOTEL, id_hotel);
        args.putString(ARG_ID_CITY, id_city);
        args.putString(ARG_FROM_DATE, from_date);
        args.putString(ARG_NIGHT_NUMBERS, night_numbers);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            id_hotel = getArguments().getString(ARG_ID_HOTEL);
            id_city = getArguments().getString(ARG_ID_CITY);
            from_date = getArguments().getString(ARG_FROM_DATE);
            night_numbers = getArguments().getString(ARG_NIGHT_NUMBERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserveroom_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
             recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }

    public void updateCapacities(List<Message> capacities,
                                 List<com.example.khahani.asa.model.roomkinds.Message> roomkinds){

        List<ReserveRoomViewModel> viewModel = ReserveRoomViewModel.fromCapacities(capacities);

        mReserveRoomRecyclerViewAdapter = new ReserveRoomRecyclerViewAdapter(roomkinds, viewModel, mListener);

        recyclerView.setAdapter(mReserveRoomRecyclerViewAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        step0();

    }

    public List<com.example.khahani.asa.model.roomkinds.Message> getRoomkinds(){
        return mRoomkinds;
    }



    /*          Step 0 Start    */
    private void step0() {
        if (mListener != null){
            mListener.onLoadBegins();
        }

        AsaService.getRoomkind(id_hotel, callbackRoomkinds);
    }

    private Callback<RoomkindsResponse> callbackRoomkinds = new Callback<RoomkindsResponse>() {
        @Override
        public void onResponse(Call<RoomkindsResponse> call, Response<RoomkindsResponse> response) {
            if (mListener != null){
                mListener.onLoadCompleted();
            }

            mRoomkinds = response.body().message;

            step1();

        }

        @Override
        public void onFailure(Call<RoomkindsResponse> call, Throwable t) {
            if (mListener != null){
                mListener.onLoadCompleted();
            }

        }
    };

    /*          Step 0 End      */

    /*          Step1 Start     */

    private void step1() {


        if (mListener != null){
            mListener.onLoadBegins();
        }

        AsaService.getCapacities(id_city,
                id_hotel,
                from_date,
                night_numbers,
                callbackCapacities);


    }

    private Callback<CapacitiesResponse> callbackCapacities = new Callback<CapacitiesResponse>() {
        @Override
        public void onResponse(Call<CapacitiesResponse> call, Response<CapacitiesResponse> response) {
            Log.d(TAG, "onResponse: " + response.body().toString());
            if (mListener != null){
                mListener.onLoadCompleted();
            }


            updateCapacities(response.body().message, mRoomkinds);
        }

        @Override
        public void onFailure(Call<CapacitiesResponse> call, Throwable t) {
            if (mListener != null){
                mListener.onLoadCompleted();
            }

            Log.d(TAG, "onFailure: " + t.getMessage(), t);
        }
    };






    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public List<ReserveRoomViewModel> getModel() {
        return mReserveRoomRecyclerViewAdapter.getModel();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Message item);
        void onCalcPrice(List<ReserveRoomViewModel> model);

        void onLoadBegins();
        void onLoadCompleted();
    }
}
