package com.example.khahani.asa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.khahani.asa.AsaActivity;
import com.example.khahani.asa.R;
import com.example.khahani.asa.fragment.CityFragment;
import com.example.khahani.asa.fragment.ReserveRoomFragment;
import com.example.khahani.asa.model.capacities.CapacitiesResponse;
import com.example.khahani.asa.model.capacities.Message;
import com.example.khahani.asa.model.capacities.MessageDeserializer;
import com.example.khahani.asa.ret.ApiService;
import com.example.khahani.asa.ret.AsaService;
import com.example.khahani.asa.utils.Asa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReserveActivity extends AsaActivity
implements ReserveRoomFragment.OnListFragmentInteractionListener{

    private String TAG = ReserveActivity.class.getSimpleName();

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
                    step0();
                    return true;
                case R.id.navigation_personal_info:
                    mTextMessage.setText(R.string.title_personal_info);
                    return true;
                case R.id.navigation_review_and_payment:
                    mTextMessage.setText(R.string.title_review_and_payment);
                    return true;
            }
            return false;
        }
    };



    private void step0() {

        reserveRoomFragment = ReserveRoomFragment.newInstance(1);
        fragmentTransaction =getSupportFragmentManager().beginTransaction();
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

            reserveRoomFragment.updateReserveRoom(response.body().message);
        }

        @Override
        public void onFailure(Call<CapacitiesResponse> call, Throwable t) {
            loading.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onFailure: " + t.getMessage(), t);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        loading = findViewById(R.id.loading);
        init();


        Intent intent = getIntent();
        if (intent.hasExtra("hotel_persian_name")){

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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onListFragmentInteraction(com.example.khahani.asa.model.capacities.Message item) {

    }
}
