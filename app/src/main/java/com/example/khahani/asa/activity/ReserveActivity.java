package com.example.khahani.asa.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.khahani.asa.AsaActivity;
import com.example.khahani.asa.R;
import com.example.khahani.asa.data.UserLoginPreferencecs;
import com.example.khahani.asa.fragment.CalcPriceFragment;
import com.example.khahani.asa.fragment.PersonInfoFragment;
import com.example.khahani.asa.fragment.ReserveExtraCoddingFragment;
import com.example.khahani.asa.fragment.ReserveRoomFragment;
import com.example.khahani.asa.fragment.ReserveRoomViewModel;
import com.example.khahani.asa.fragment.ReviewFragment;
import com.example.khahani.asa.model.reserve15min.Reserve15MinResponse;
import com.example.khahani.asa.model.reserve15min.ReserveDetail;
import com.example.khahani.asa.model.reserve5min.Reserve5MinResponse;
import com.example.khahani.asa.model.reserve5min.RoomDetail;
import com.example.khahani.asa.model.reserve_extra_codding.ReserveExtraCoddingResponse;
import com.example.khahani.asa.model.roomkinds.Message;
import com.example.khahani.asa.ret.AsaService;
import com.example.khahani.asa.utils.Asa;
import com.example.khahani.asa.utils.ExpandOrCollapseView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveActivity extends AsaActivity
        implements ReserveRoomFragment.OnListFragmentInteractionListener,
        CalcPriceFragment.OnFragmentInteractionListener,
        PersonInfoFragment.OnFragmentInteractionListener,
        ReviewFragment.OnFragmentInteractionListener,
        ReserveExtraCoddingFragment.OnReserveExtraListFragmentInteractionListener {

    private String TAG = ReserveActivity.class.getSimpleName();

    private BottomNavigationView navigation;

    private String id_hotel;
    private String id_city;
    private String from_date;
    private String night_numbers;

    private List<Message> mRoomkinds;
    private List<com.example.khahani.asa.model.capacities.Message> mCapacities;
    private List<ReserveRoomViewModel> selectedRoomDetails;
    private int mCalcRooms;
    private int mCalcChilds;
    private int mCalcAdults;
    private int mCalcExtraBeds;
    private String mCalcStartDate;
    private String mCalcEndDate;
    private int mCalcDiscount;
    private int mCalcTotalPrice;
    private int mCalcMustPay;

    private Reserve5MinResponse mReserve5MinResponse;
    private ReserveExtraCoddingResponse mReserveExtraCodding;
    private Reserve15MinResponse mReserve15MinResponse;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (navigation.getSelectedItemId() == item.getItemId())
                return true;

            switch (item.getItemId()) {
                case R.id.navigation_reserver_room:
                    invalidateOptionsMenu();

                    resetCalcPriceDetails();

                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, reserveRoomFragment);
                    fragmentTransaction.commit();

                    return true;
                case R.id.navigation_personal_info:
                    invalidateOptionsMenu();

                    stepCalcPrice();

                    if (mCalcRooms <= 0 || mCalcAdults <= 0) {

                        AlertDialog dialog = new AlertDialog.Builder(ReserveActivity.this)
                                .setMessage("ابتدا اطلاعات رزرو را تعیین کنید.")
                                .setTitle("پیام سیستمی")
                                .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();

                        dialog.show();

                        return false;

                    }

                    stepReserve5Min();

                    return true;
                case R.id.navigation_review_and_payment:
                    invalidateOptionsMenu();
                    if (shouldBeginStepReserve15Min()) {
                        stepReserve15Min();
                        return true;
                    } else {
                        return false;
                    }
                case R.id.navigation_reserve_extra_codding:
                    invalidateOptionsMenu();

                    stepCalcPrice();

                    if (mCalcRooms <= 0 || mCalcAdults <= 0) {

                        AlertDialog dialog = new AlertDialog.Builder(ReserveActivity.this)
                                .setMessage("ابتدا اطلاعات رزرو را تعیین کنید.")
                                .setTitle("پیام سیستمی")
                                .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();

                        dialog.show();

                        return false;

                    }

                    stepReserveExtraCodding();

                    return true;

            }
            return false;
        }
    };
    private Callback<ReserveExtraCoddingResponse> callbackReserveExtraCodding = new Callback<ReserveExtraCoddingResponse>() {
        @Override
        public void onResponse(Call<ReserveExtraCoddingResponse> call, Response<ReserveExtraCoddingResponse> response) {
            Log.d(TAG, "onResponse: " + response.body());

            loading.setVisibility(View.INVISIBLE);

            mReserveExtraCodding = response.body();

            if (reserveExtraCoddingFragment == null) {
                reserveExtraCoddingFragment= ReserveExtraCoddingFragment.newInstance(1);
            }

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, reserveExtraCoddingFragment);
            fragmentTransaction.commit();

            getSupportFragmentManager().executePendingTransactions();

            reserveExtraCoddingFragment.updateReserveExtra(mReserveExtraCodding.message);

        }

        @Override
        public void onFailure(Call<ReserveExtraCoddingResponse> call, Throwable t) {
            loading.setVisibility(View.INVISIBLE);
            Toast.makeText(ReserveActivity.this, "Network Failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void stepReserveExtraCodding() {

       loading.setVisibility(View.VISIBLE);

        AsaService.getReserveExtraCodding(from_date,
                Asa.getToDate(from_date, night_numbers),
                "697",
                callbackReserveExtraCodding);

    }


    private boolean shouldBeginStepReserve15Min() {
        if (mReserve5MinResponse == null) {
            Log.e(TAG, "stepReserve15Min: Error: Response5Min is null", new Throwable());
            return false;
        }

        if (personInfoFragment.isValid()) {
            return true;
        } else {
            AlertDialog dialog = new AlertDialog.Builder(ReserveActivity.this)
                    .setTitle("تکمیل اطلاعات")
                    .setMessage("اطلاعات فرم بدرستی وارد نشده است")
                    .setPositiveButton("اصلاح اطلاعات", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
            return false;
        }
    }

    private Callback<Reserve5MinResponse> callbackReserve5Min = new Callback<Reserve5MinResponse>() {
        @Override
        public void onResponse(Call<Reserve5MinResponse> call, Response<Reserve5MinResponse> response) {
            Log.d(TAG, "onResponse: " + response.body());

            loading.setVisibility(View.INVISIBLE);

            mReserve5MinResponse = response.body();

            if (personInfoFragment == null) {
                personInfoFragment = PersonInfoFragment.newInstance();
            }

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, personInfoFragment);
            fragmentTransaction.commit();

        }

        @Override
        public void onFailure(Call<Reserve5MinResponse> call, Throwable t) {
            Log.d(TAG, "onFailure: reserve 5 min", t);
            Toast.makeText(ReserveActivity.this, "Network Failed", Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.INVISIBLE);
            navigation.setSelectedItemId(R.id.navigation_reserver_room);
        }
    };

    private void stepReserve5Min() {

        List<RoomDetail> roomDetails = new ArrayList<>();

        for (int i = 0; i < selectedRoomDetails.size(); i++) {

            if (selectedRoomDetails.get(i).selectedRoomsCount <= 0)
                continue;

            RoomDetail roomDetail = new RoomDetail();
            roomDetail.id_roomkind = selectedRoomDetails.get(i).room_kind_id;
            roomDetail.number =
                    Integer.toString(selectedRoomDetails.get(i).selectedRoomsCount);
            roomDetail.adult =
                    Integer.toString(selectedRoomDetails.get(i).selectedAdultsCount);

            for (int j = 0; j < selectedRoomDetails.get(i).selectedChildsCount; j++) {
                roomDetail.child.add("1");
            }
            roomDetails.add(roomDetail);
        }

        loading.setVisibility(View.VISIBLE);

        AsaService.postReserve5Min(id_hotel, mCalcStartDate, mCalcEndDate, roomDetails, callbackReserve5Min);

    }


    @Override
    public void onListFragmentInteraction(com.example.khahani.asa.model.capacities.Message item) {

    }

    /*          Step1   End    */

    /*          Step2  CalcPrice Start  */

    @Override
    public void onCalcPrice(List<ReserveRoomViewModel> models) {

    }

    @Override
    public void onLoadBegins() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadCompleted() {
        loading.setVisibility(View.INVISIBLE);
    }

    private void resetCalcPriceDetails() {
        mCalcRooms = 0;
        mCalcAdults = 0;
        mCalcChilds = 0;
        mCalcExtraBeds = 0;
        mCalcStartDate = from_date;
        mCalcEndDate = Asa.getToDate(from_date, night_numbers);
        mCalcDiscount = 0;
        mCalcTotalPrice = 0;
        mCalcMustPay = 0;
    }

    private void stepCalcPrice() {

        resetCalcPriceDetails();

        selectedRoomDetails = reserveRoomFragment.getModel();

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
            for (Message roomkindItem : reserveRoomFragment.getRoomkinds()) {
                if (roomkindItem.id.equals(model.room_kind_id)) {
                    roomkind = roomkindItem;
                    break;
                }
            }

            if (model.selectedFoodType == ReserveRoomViewModel.FOODTYPE_BREAKFAST) {

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
                            Integer.parseInt(model.iranian_daily_board_rate_TSI.get(j));

                    int extraChildPrice = remainChildForExtraBeds *
                            Integer.parseInt(model.iranian_child_rate_TSI.get(j));

                    int extraAdultPrice = remainAdultForExtraBeds *
                            Integer.parseInt(model.iranian_extra_bed_rate_TSI.get(j));

                    totalPrice += roomPrice + extraChildPrice + extraAdultPrice;

                }

                calcTotalPrice += totalPrice;
                calcDiscount += totalPrice * 20 / 100;
            }

            if (model.selectedFoodType == ReserveRoomViewModel.FOODTYPE_BREAKFAST_LUNCH_DINNER) {

                //calc price for fullboard

                int totalPrice = 0;

                for (int j = 0; j < Integer.parseInt(night_numbers); j++) {

                    if (model.selectedAdultsCount <= 1) {
                        totalPrice += 2 *
                                Integer.parseInt(model.lunch_dinner_person.get(j));
                    } else {
                        totalPrice += model.selectedAdultsCount *
                                Integer.parseInt(model.lunch_dinner_person.get(j));
                    }

                    totalPrice += model.selectedChildsCount *
                            Integer.parseInt(model.lunch_dinner_person.get(j)) / 2;

                }

                calcTotalPrice += totalPrice;
            }

            calcRooms += model.selectedRoomsCount;
            calcAdults += model.selectedAdultsCount;
            calcChilds += model.selectedChildsCount;

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

        mCalcRooms = calcRooms;
        mCalcAdults = calcAdults;
        mCalcChilds = calcChilds;
        mCalcExtraBeds = calcExtraBeds;
        mCalcStartDate = calcStartDate;
        mCalcEndDate = calcEndDate;
        mCalcDiscount = calcDiscount;
        mCalcTotalPrice = calcTotalPrice;
        mCalcMustPay = calcTotalPrice - calcDiscount;

    }

    private void stepShowCalcPrice(int calcRooms, int calcChilds, int calcAdults,
                                   int calcExtraBeds, String calcStartDate, String calcEndDate, int calcDiscount,
                                   int calcTotalPrice) {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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
    }

    /**
     * hide calcPriceFragment
     *
     * @param uri
     */
    @Override
    public void onCalcPriceFragmentInteraction(Uri uri) {

        FrameLayout calcContainer = findViewById(R.id.fragmentCalcPriceContainer);
        ExpandOrCollapseView.collapse(calcContainer);

        FrameLayout container = findViewById(R.id.fragmentContainer);
        container.setVisibility(View.VISIBLE);

    }

    /*          Step2  CalcPrice End  */

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

        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        if (UserLoginPreferencecs.read(this, UserLoginPreferencecs.PREF_PANEL_RESERVE_EXTRA).equals("1")){

            navigation.getMenu().add(Menu.NONE, R.id.navigation_reserve_extra_codding, 2 , "خدمات اضافی").setIcon(R.drawable.ic_reserve_extra);

        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        reserveRoomFragment = ReserveRoomFragment.newInstance(
                1, id_city, id_hotel, from_date, night_numbers);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, reserveRoomFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reserve, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Build.VERSION.SDK_INT > 11) {
            invalidateOptionsMenu();
            if (navigation.getSelectedItemId() == R.id.navigation_reserver_room) {
                menu.findItem(R.id.menu_calc_price).setVisible(true);
                menu.findItem(R.id.menu_save_personal_info).setVisible(false);
            } else if (navigation.getSelectedItemId() == R.id.navigation_personal_info) {
                menu.findItem(R.id.menu_calc_price).setVisible(true);
                menu.findItem(R.id.menu_save_personal_info).setVisible(false);
            } else if (navigation.getSelectedItemId() == R.id.navigation_review_and_payment) {
                menu.findItem(R.id.menu_calc_price).setVisible(true);
                menu.findItem(R.id.menu_save_personal_info).setVisible(false);
            }else if(navigation.getSelectedItemId() == R.id.navigation_reserve_extra_codding){
                menu.findItem(R.id.menu_calc_price).setVisible(true);
                menu.findItem(R.id.menu_save_personal_info).setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_calc_price:
                stepCalcPrice();
                stepShowCalcPrice(mCalcRooms, mCalcChilds, mCalcAdults, mCalcExtraBeds,
                        mCalcStartDate, mCalcEndDate, mCalcDiscount, mCalcTotalPrice);
                break;
            case R.id.menu_save_personal_info:

//                stepReserve15Min();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Callback<Reserve15MinResponse> callbackReserve15Min = new Callback<Reserve15MinResponse>() {
        @Override
        public void onResponse(Call<Reserve15MinResponse> call, Response<Reserve15MinResponse> response) {
            Log.d(TAG, "onResponse Reserve15Min: " + response.body());

            mReserve15MinResponse = response.body();
            loading.setVisibility(View.INVISIBLE);

//            if (reviewFragment == null) {
//                reviewFragment = reviewFragment.newInstance();
//            }
//
//            fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragmentContainer, reviewFragment);
//            fragmentTransaction.commit();

            Intent intent = new Intent(ReserveActivity.this, PaymentActivity.class);

            intent.putExtra("amount", Integer.toString(mCalcMustPay));
            //Hotel Info
            intent.putExtra("hotel_id", selectedRoomDetails.get(0).hotel_id);
            intent.putExtra("hotel_persian_name", getIntent().getStringExtra("hotel_persian_name"));
            intent.putExtra("hotelAddress", getIntent().getStringExtra("hotelAddress"));
            intent.putExtra("hotelPhone", "051335----4");
            //QRCODE Info

            //Reserve Info

            intent.putExtra("fromDate", from_date);
            intent.putExtra("nightNumbers", night_numbers);

            intent.putExtra("reserveName", personInfoFragment.name.getText().toString());
            intent.putExtra("reserveFamily", personInfoFragment.family.getText().toString());
            intent.putExtra("reservePhone", personInfoFragment.phone.getText().toString());
            intent.putExtra("reserveCodeMelli", personInfoFragment.codemelli.getText().toString());

            intent.putExtra("id_reserve_asa", mReserve15MinResponse.message.id_reserve_asa);
            intent.putExtra("id_reserve_hotel", mReserve15MinResponse.message.id_reserve_hotel);

            int roomCount = 0;

            for (int i = 0; i < selectedRoomDetails.size(); i++) {
                if (selectedRoomDetails.get(i).selectedRoomsCount > 0) {
                    intent.putExtra("room_kind_name_" + roomCount, selectedRoomDetails.get(i).room_kind_name);
                    intent.putExtra("foodType_" + roomCount, "breakfast / fullboard");
                    intent.putExtra("selectedRoomsCount_" + roomCount, selectedRoomDetails.get(i).selectedRoomsCount);
                    intent.putExtra("selectedAdultsCount_" + roomCount, selectedRoomDetails.get(i).selectedAdultsCount);
                    intent.putExtra("selectedChildsCount_" + roomCount, selectedRoomDetails.get(i).selectedChildsCount);
                    roomCount++;
                }
            }
            intent.putExtra("roomCount", roomCount);


            startActivity(intent);


        }

        @Override
        public void onFailure(Call<Reserve15MinResponse> call, Throwable t) {
            Log.d(TAG, "onFailure Reserve15Min: ", t);
            loading.setVisibility(View.INVISIBLE);
        }
    };


    private void stepReserve15Min() {


        List<ReserveDetail> reserveDetails = new ArrayList<>();

        ReserveDetail reserveDetail = new ReserveDetail();
        reserveDetail.last_name = personInfoFragment.family.getText().toString();
        reserveDetail.first_name = personInfoFragment.name.getText().toString();
        reserveDetail.melli_code = personInfoFragment.codemelli.getText().toString();
        reserveDetail.adress = "";// + personInfoFragment.address.getText().toString();
        reserveDetail.mobile = personInfoFragment.phone.getText().toString();
        reserveDetail.source = "";//personInfoFragment.city.getText().toString();
        reserveDetail.transfer = "";
        reserveDetail.travel_with = "";
        reserveDetail.nation = "";
        reserveDetail.message = "ندارد";
        reserveDetail.flight_number = "";
        reserveDetail.flight_time = "";
        reserveDetail.clerk = "0";
        reserveDetail.telephone = "";

        reserveDetails.add(reserveDetail);

        loading.setVisibility(View.VISIBLE);

        AsaService.putReserve15Min(
                Integer.toString(mReserve5MinResponse.message.id_reserve_asa),
                Integer.toString(mReserve5MinResponse.message.id_reserve_hotel),
                reserveDetails,
                callbackReserve15Min);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            //super.onBackPressed();
            finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onPersonFragmentInteraction() {

        if (shouldBeginStepReserve15Min()) {
            stepReserve15Min();
        }

    }


    @Override
    public void onReviewFragmentInteraction(Uri uri) {

    }

    @Override
    public void onReserveExtraListFragmentInteraction(com.example.khahani.asa.model.reserve_extra_codding.Message item) {

    }
}
