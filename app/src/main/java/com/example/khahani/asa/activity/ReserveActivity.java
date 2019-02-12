package com.example.khahani.asa.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.khahani.asa.AsaActivity;
import com.example.khahani.asa.R;
import com.example.khahani.asa.fragment.CalcPriceFragment;
import com.example.khahani.asa.fragment.ReserveRoomFragment;
import com.example.khahani.asa.fragment.ReserveRoomViewModel;
import com.example.khahani.asa.model.capacities.CapacitiesResponse;
import com.example.khahani.asa.model.roomkinds.Message;
import com.example.khahani.asa.model.roomkinds.RoomkindsResponse;
import com.example.khahani.asa.ret.AsaService;
import com.example.khahani.asa.utils.Asa;
import com.example.khahani.asa.utils.ExpandOrCollapseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveActivity extends AsaActivity
        implements ReserveRoomFragment.OnListFragmentInteractionListener,
        CalcPriceFragment.OnFragmentInteractionListener {

    private String TAG = ReserveActivity.class.getSimpleName();
    private BottomNavigationView navigation;

    private RoomkindsResponse mRoomkindsResponse;

    private String id_hotel;
    private String id_city;
    private String from_date;
    private String night_numbers;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_reserver_room:
                    invalidateOptionsMenu();
                    return true;
                case R.id.navigation_personal_info:
                    invalidateOptionsMenu();
                    mTextMessage.setText(R.string.title_personal_info);
                    return true;
                case R.id.navigation_review_and_payment:
                    invalidateOptionsMenu();
                    mTextMessage.setText(R.string.title_review_and_payment);
                    return true;
            }
            return false;
        }
    };

    /*          Step 0 Start    */
    private void step0() {
        loading.setVisibility(View.VISIBLE);
        AsaService.getRoomkind(id_hotel, callbackRoomkinds);
    }

    private Callback<RoomkindsResponse> callbackRoomkinds = new Callback<RoomkindsResponse>() {
        @Override
        public void onResponse(Call<RoomkindsResponse> call, Response<RoomkindsResponse> response) {
            loading.setVisibility(View.INVISIBLE);
            mRoomkindsResponse = response.body();

            step1();

        }

        @Override
        public void onFailure(Call<RoomkindsResponse> call, Throwable t) {
            loading.setVisibility(View.INVISIBLE);
        }
    };

    /*          Step 0 End      */

    /*          Step1 Start     */

    private void step1() {

        reserveRoomFragment = ReserveRoomFragment.newInstance(1);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.
                setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.fragmentContainer, reserveRoomFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        loading.setVisibility(View.VISIBLE);

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
            loading.setVisibility(View.INVISIBLE);

            reserveRoomFragment.updateCapacities(response.body().message, mRoomkindsResponse.message);
        }

        @Override
        public void onFailure(Call<CapacitiesResponse> call, Throwable t) {
            loading.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onFailure: " + t.getMessage(), t);
        }
    };

    @Override
    public void onListFragmentInteraction(com.example.khahani.asa.model.capacities.Message item) {

    }

    @Override
    public void onCalcPrice(List<ReserveRoomViewModel> models) {

    }

    /*          Step1   End    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        loading = findViewById(R.id.loading);
        init();


        Intent intent = getIntent();
        if (intent.hasExtra("hotel_persian_name")) {

            String activityTitle =
                    getString(R.string.title_activity_reserve,
                            intent.getStringExtra("hotel_persian_name"));
            setTitle(activityTitle);

            id_hotel = intent.getStringExtra("id_hotel");
            id_city = intent.getStringExtra("id_city");
            from_date = intent.getStringExtra("from_date");
            night_numbers = intent.getStringExtra("night_numbers");
        }

        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        step0();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reserve, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Build.VERSION.SDK_INT > 11){
            invalidateOptionsMenu();
            if (navigation.getSelectedItemId() == R.id.navigation_reserver_room) {
                menu.findItem(R.id.menu_calc_price).setVisible(true);
            }else{
                menu.findItem(R.id.menu_calc_price).setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_calc_price:
                List<ReserveRoomViewModel> reserveRoomViewModels =
                        reserveRoomFragment.getModel();

                int calcRooms = 0;
                int calcChilds = 0;
                int calcAdults = 0;
                int calcExtraBeds = 0;
                String calcStartDate = from_date;
                String calcEndDate = Asa.getToDate(from_date, night_numbers);
                int calcDiscount = 0;
                int calcTotalPrice = 0;


                for (int i = 0; i < reserveRoomViewModels.size(); i++) {
                    ReserveRoomViewModel model = reserveRoomViewModels.get(i);
                    if (model.selectedRoomsCount <= 0)
                        continue;

                    Message roomkind = null;
                    for (Message roomkindItem : mRoomkindsResponse.message) {
                        if (roomkindItem.id.equals(model.room_kind_id)) {
                            roomkind = roomkindItem;
                            break;
                        }
                    }

                    int totalPerson = model.selectedAdultsCount + model.selectedChildsCount;
                    int totalBedCapacity =
                            model.selectedRoomsCount * Integer.parseInt(roomkind.room_kind_bed);
                    int totalExtraCapacity =
                            model.selectedRoomsCount * Integer.parseInt(roomkind.extra_bed);

                    //   arrange: fill rooms capacity then add extra beds

                    int remainPersonForExtraBeds = totalPerson - totalBedCapacity;

                    if (remainPersonForExtraBeds < 0)
                        remainPersonForExtraBeds = 0;

                    calcExtraBeds += remainPersonForExtraBeds;

                    int remainChildForExtraBeds = 0;
                    int remainAdultForExtraBeds = 0;

                    if (remainPersonForExtraBeds > 0) { // fill extra child

                        if (model.selectedChildsCount > 0) {

                            while (remainPersonForExtraBeds > 0) {

                                remainChildForExtraBeds++;
                                remainPersonForExtraBeds--;

                                if (remainChildForExtraBeds == model.selectedChildsCount)
                                    break;
                            }

                        }
                    }

                    remainAdultForExtraBeds = remainPersonForExtraBeds;

                    // calc price

                    int totalPrice = 0;

                    for (int j = 0; j < Integer.parseInt(night_numbers); j++) {

                        int roomPrice = model.selectedRoomsCount *
                                Integer.parseInt(model.iranian_daily_board_rate.get(j));

                        int extraChildPrice = remainChildForExtraBeds *
                                Integer.parseInt(model.iranian_child_rate.get(j));

                        int extraAdultPrice = remainAdultForExtraBeds *
                                Integer.parseInt(model.iranian_extra_bed_rate.get(j));

                        totalPrice += roomPrice + extraChildPrice + extraAdultPrice;

                    }

                    calcRooms += model.selectedRoomsCount;
                    calcAdults += model.selectedAdultsCount;
                    calcChilds += model.selectedChildsCount;
                    calcTotalPrice += totalPrice;
                    calcDiscount += totalPrice * 20 / 100;

                }

                Log.d(TAG, "onOptionsItemSelected: Rooms: " + calcRooms);
                Log.d(TAG, "onOptionsItemSelected: Adults: " + calcAdults);
                Log.d(TAG, "onOptionsItemSelected: Childs: " + calcChilds);
                Log.d(TAG, "onOptionsItemSelected: ExtraBeds: " + calcExtraBeds);
                Log.d(TAG, "onOptionsItemSelected: Start Date: " + calcStartDate);
                Log.d(TAG, "onOptionsItemSelected: End Date: " + calcEndDate);
                Log.d(TAG, "onOptionsItemSelected: Discount: " + calcDiscount);
                Log.d(TAG, "onOptionsItemSelected: Total Price: " + calcTotalPrice);
                Log.d(TAG, "onOptionsItemSelected: Must Pay: " + (calcTotalPrice - calcDiscount));

                calcPriceFragment = CalcPriceFragment.newInstance(Integer.toString(calcRooms),
                        Integer.toString(calcChilds),
                        Integer.toString(calcAdults),
                        Integer.toString(calcExtraBeds),
                        calcStartDate,
                        calcEndDate,
                        night_numbers,
                        Integer.toString(calcDiscount),
                        Integer.toString(calcTotalPrice),
                        Integer.toString(calcTotalPrice - calcDiscount));


                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentCalcPriceContainer, calcPriceFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                FrameLayout calcContainer = findViewById(R.id.fragmentCalcPriceContainer);
                ExpandOrCollapseView.expand(calcContainer);

                FrameLayout container = findViewById(R.id.fragmentContainer);
                container.setVisibility(View.GONE);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

        FrameLayout calcContainer = findViewById(R.id.fragmentCalcPriceContainer);
        ExpandOrCollapseView.collapse(calcContainer);

        FrameLayout container = findViewById(R.id.fragmentContainer);
        container.setVisibility(View.VISIBLE);

    }
}
