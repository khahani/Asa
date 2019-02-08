package com.example.khahani.asa.activity;

import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.khahani.asa.AsaActivity;
import com.example.khahani.asa.R;
import com.example.khahani.asa.fragment.Step1Fragment;
import com.example.khahani.asa.model.cities.CitiesResponse;
import com.example.khahani.asa.model.cities.Message;
import com.example.khahani.asa.model.cities.MessageDeserializer;
import com.example.khahani.asa.ret.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AsaActivity implements Step1Fragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


        // testDatePicker();

//        testService();

        step1();

    }

    private void step1() {

        step1Fragment = new Step1Fragment();
        fragmentTransaction.add(R.id.fragmentContainer, step1Fragment);
        fragmentTransaction.commit();

        onDateSetListener = (datePickerDialog, year, monthOfYear, dayOfMonth) -> {
            String selectedDate = "" + year + "/" + monthOfYear + "/" + dayOfMonth;
            Log.d(TAG, selectedDate);
            step1Fragment.updateEditTextFromDate(selectedDate);
        };
        datePickerDialog.setOnDateSetListener(onDateSetListener);

    }

    private void testService() {
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

        service.getCities("demo",
                "p8qTL8mUhxJBadRK81%2BjDVy%2FJOL96XyDGbde9OaY658%3D",
                "6",
                "0000-00-00+00%3A00%3A00",
                "0000-00-00+00%3A00%3A00",
                "5201",
                "0").enqueue(new Callback<CitiesResponse>() {
            @Override
            public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
                Log.e(TAG, "onResponse: " + response.body().toJson());
            }

            @Override
            public void onFailure(Call<CitiesResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: error", t);
            }
        });
    }


    @Override
    public void pickFromDate(View view) {
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }
}
