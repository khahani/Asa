package com.example.khahani.asa.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.khahani.asa.AsaActivity;
import com.example.khahani.asa.R;
import com.example.khahani.asa.fragment.CityFragment;
import com.example.khahani.asa.fragment.HotelFragment;
import com.example.khahani.asa.fragment.Step1Fragment;
import com.example.khahani.asa.model.cities.CitiesResponse;
import com.example.khahani.asa.model.cities.Message;
import com.example.khahani.asa.model.cities.MessageDeserializer;
import com.example.khahani.asa.model.hotels.HotelsResponse;
import com.example.khahani.asa.ret.ApiService;
import com.example.khahani.asa.utils.Asa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AsaActivity
        implements Step1Fragment.OnFragmentInteractionListener,
        CityFragment.OnListFragmentInteractionListener,
HotelFragment.OnListFragmentInteractionListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressBar loading;

    private String id_city;
    private String from_date;
    private String night_numbers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = findViewById(R.id.loading);

        init();


        // testDatePicker();

        step0();

        //step1();


    }




    /*      Step2 Start      */

    private void step2() {

        hotelFragment = HotelFragment.newInstance(1);

        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.
                setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.fragmentContainer, hotelFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        loading.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    String string = request.url().toString();
                    string = string.replace("%25", "%");
                    string = string.replace("%2B", "+");
                    Request newRequest = new Request.Builder()
                            .url(string)
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(com.example.khahani.asa.model.hotels.Message.class,
                        new com.example.khahani.asa.model.hotels.MessageDeserializer("message"))
                .disableHtmlEscaping()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiService.ASA_URL)
                .client(client)
                .build();

        ApiService service = retrofit.create(ApiService.class);

        String[] params = {"from_time_stamp", "0000-00-00+00%3A00%3A00",
                "id_city", id_city, "id_hotel", "0"};

        service.getHotels("demo",
//                "p8qTL8mUhxJBadRK81%2BjDVy%2FJOL96XyDGbde9OaY658%3D",
                Asa.getSigniture("demo", params),
                "6",
                "0000-00-00+00%3A00%3A00",
                "0000-00-00+00%3A00%3A00",
                id_city,
                "0").enqueue(callbackHotels);

    }

    Callback<HotelsResponse> callbackHotels = new Callback<HotelsResponse>() {
        @Override
        public void onResponse(Call<HotelsResponse> call, Response<HotelsResponse> response) {
            Log.e(TAG, "onResponse: " + response.body().toJson());

            loading.setVisibility(View.INVISIBLE);

            hotelFragment.updateHotels(response.body().message);

        }

        @Override
        public void onFailure(Call<HotelsResponse> call, Throwable t) {
            Log.e(TAG, "onFailure: error", t);

            loading.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onListFragmentInteraction(com.example.khahani.asa.model.hotels.Message item) {

    }

    /*      Step2 End      */



    /*       Step1 begin       */

    private void step1() {

        step1Fragment = new Step1Fragment();

        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.
                setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.fragmentContainer, step1Fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        onDateSetListener = (datePickerDialog, year, monthOfYear, dayOfMonth) -> {
            String selectedDate = "" + year + "/" + monthOfYear + "/" + dayOfMonth;
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
        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.
                setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.fragmentContainer, cityFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    String string = request.url().toString();
                    string = string.replace("%25", "%");
                    string = string.replace("%2B", "+");
                    Request newRequest = new Request.Builder()
                            .url(string)
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Message.class, new MessageDeserializer("message"))
                .disableHtmlEscaping()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiService.ASA_URL)
                .client(client)
                .build();

        ApiService service = retrofit.create(ApiService.class);

        loading.setVisibility(View.VISIBLE);

        String[] params = {"from_time_stamp", "0000-00-00+00%3A00%3A00",
        "id_city", "5201", "id_hotel", "0"};

        service.getCities("demo",
                Asa.getSigniture("demo", params),
                "6",
                "0000-00-00+00%3A00%3A00",
                "0000-00-00+00%3A00%3A00",
                "5201",
                "0").enqueue(callbackCities);
    }

    Callback<CitiesResponse> callbackCities = new Callback<CitiesResponse>() {
        @Override
        public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
            Log.e(TAG, "onResponse: " + response.body().toJson());

            loading.setVisibility(View.INVISIBLE);

            cityFragment.updateCities(response.body().message);

        }

        @Override
        public void onFailure(Call<CitiesResponse> call, Throwable t) {
            Log.e(TAG, "onFailure: error", t);

            loading.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onListFragmentInteraction(Message item) {
        id_city = item.code;

        step1();
    }

    /*         Step0  End      */
}
