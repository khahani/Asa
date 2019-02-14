package com.example.khahani.asa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.example.khahani.asa.AsaActivity;
import com.example.khahani.asa.R;
import com.example.khahani.asa.fragment.CityFragment;
import com.example.khahani.asa.fragment.HotelFragment;
import com.example.khahani.asa.fragment.Step1Fragment;
import com.example.khahani.asa.model.cities.CitiesResponse;
import com.example.khahani.asa.model.cities.Message;
import com.example.khahani.asa.model.hotels_date.HotelsDateResponse;
import com.example.khahani.asa.ret.AsaService;
import com.example.khahani.asa.utils.Asa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AsaActivity
        implements Step1Fragment.OnFragmentInteractionListener,
        CityFragment.OnListFragmentInteractionListener,
        HotelFragment.OnListFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String id_city;
    private String from_date;
    private String night_numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = findViewById(R.id.loading);

        init();

        step0();

    }


    /*      Step2 Start      */

    private void step2() {

        hotelFragment = HotelFragment.newInstance(1);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.
                setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.fragmentContainer, hotelFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        loading.setVisibility(View.VISIBLE);

        AsaService.getHotelsDate(id_city, "0", from_date,
                Asa.getToDate(from_date, night_numbers), callbackHotelsDate);

    }

    private Callback<HotelsDateResponse> callbackHotelsDate = new Callback<HotelsDateResponse>() {
        @Override
        public void onResponse(Call<HotelsDateResponse> call, Response<HotelsDateResponse> response) {
            Log.e(TAG, "onResponse: " + response.body().toJson());

            loading.setVisibility(View.INVISIBLE);

            hotelFragment.updateHotels(response.body().message);
        }

        @Override
        public void onFailure(Call<HotelsDateResponse> call, Throwable t) {
            Log.e(TAG, "onFailure: error", t);

            loading.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onListFragmentInteraction(com.example.khahani.asa.model.hotels_date.Message hotel) {
        Intent intent = new Intent(MainActivity.this, ReserveActivity.class);
        intent.putExtra("hotel_persian_name", hotel.persian_name);
        intent.putExtra("id_hotel", hotel.id);
        intent.putExtra("id_city", id_city);
        intent.putExtra("from_date", from_date);
        intent.putExtra("night_numbers", night_numbers);
        startActivity(intent);
    }

    /*      Step2 End      */



    /*       Step1 begin       */

    private void step1() {

        step1Fragment = new Step1Fragment();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.
                setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.fragmentContainer, step1Fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        onDateSetListener = (datePickerDialog, year, monthOfYear, dayOfMonth) -> {
            String selectedDate = "" + year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            Log.d(TAG, selectedDate);
            step1Fragment.updateEditTextFromDate(selectedDate);
        };
        datePickerDialog.setOnDateSetListener(onDateSetListener);

    }

    @Override
    public void pickFromDate(View view) {
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void nextStep(String fromDate, int numberNights) {
        from_date = fromDate;
        night_numbers = Integer.toString(numberNights);

        step2();
    }

    /*       Step1 end        */


    /*        Step0 Start     */
    private void step0() {

        cityFragment = CityFragment.newInstance(2);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.
                setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.add(R.id.fragmentContainer, cityFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }



    @Override
    public void onListFragmentInteraction(Message item) {
        id_city = item.code;

        step1();
    }

    @Override
    public void onLoadBegins() {

        loading.setVisibility(View.VISIBLE);


    }

    @Override
    public void onLoadCompleted() {
        loading.setVisibility(View.INVISIBLE);

    }


    /*         Step0  End      */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
