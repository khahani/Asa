package com.example.khahani.asa.ret;

import com.example.khahani.asa.model.capacities.CapacitiesResponse;
import com.example.khahani.asa.model.capacities.Message;
import com.example.khahani.asa.model.capacities.MessageDeserializer;
import com.example.khahani.asa.model.cities.CitiesResponse;
import com.example.khahani.asa.model.hotels.HotelsResponse;
import com.example.khahani.asa.model.hotels_date.HotelsDateResponse;
import com.example.khahani.asa.model.roomkinds.RoomkindsResponse;
import com.example.khahani.asa.utils.Asa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsaService {

    public static void getCapacities(String id_city,
                                     String id_hotel,
                                     String from_date,
                                     String night_numbers,
                                     Callback<CapacitiesResponse> callbackCapacities) {

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

        String to_date = Asa.getToDate(from_date, night_numbers);
        from_date = Asa.getMiladiDate(from_date);
        to_date = Asa.getMiladiDate(to_date);
        String id_roomkind = "0";

        String[] params = {"id_city", id_city,
                "id_hotel", id_hotel,
                "id_roomkind", id_roomkind,
                "from_date", from_date,
//                "to_date",Asa.getToDate(from_date, night_numbers),
                "to_date", to_date,
                "from_time_stamp", "0000-00-00+00%3A00%3A00"};

        service.getCapacities("demo",
                Asa.getSigniture("demo", params),
                "6",
                "0000-00-00+00%3A00%3A00",
                id_city,
                id_hotel,
                id_roomkind,
                from_date,
//                Asa.getToDate(from_date, night_numbers)
                to_date,
                "0000-00-00+00%3A00%3A00"
        ).enqueue(callbackCapacities);
    }

    public static void getHotels(String id_city, String id_hotel, Callback<HotelsResponse> callbackHotels) {
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
                "id_city", id_city, "id_hotel", id_hotel};

        service.getHotels("demo",
                Asa.getSigniture("demo", params),
                "6",
                "0000-00-00+00%3A00%3A00",
                "0000-00-00+00%3A00%3A00",
                id_city,
                "0").enqueue(callbackHotels);
    }

    public static void getCities(String id_city, String id_hotel, Callback<CitiesResponse> callbackCities) {
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
                .registerTypeAdapter(com.example.khahani.asa.model.cities.Message.class, new com.example.khahani.asa.model.cities.MessageDeserializer("message"))
                .disableHtmlEscaping()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiService.ASA_URL)
                .client(client)
                .build();

        ApiService service = retrofit.create(ApiService.class);


        String[] params = {"from_time_stamp", "0000-00-00+00%3A00%3A00",
                "id_city", id_city, "id_hotel", id_hotel};

        service.getCities("demo",
                Asa.getSigniture("demo", params),
                "6",
                "0000-00-00+00%3A00%3A00",
                "0000-00-00+00%3A00%3A00",
                id_city,
                id_hotel).enqueue(callbackCities);
    }

    public static void getRoomkind(String id_hotel, Callback<RoomkindsResponse> callbackCities) {
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
                .registerTypeAdapter(com.example.khahani.asa.model.roomkinds.Message.class,
                        new com.example.khahani.asa.model.roomkinds.MessageDeserializer("message"))
                .disableHtmlEscaping()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiService.ASA_URL)
                .client(client)
                .build();

        ApiService service = retrofit.create(ApiService.class);


        String[] params = {"from_time_stamp", "0000-00-00+00%3A00%3A00",
                "id_hotel", id_hotel};

        service.getRoomkinds("demo",
                Asa.getSigniture("demo", params),
                "6",
                "0000-00-00+00%3A00%3A00",
                "0000-00-00+00%3A00%3A00",
                id_hotel).enqueue(callbackCities);
    }

    public static void getHotelsDate(String id_city, String id_hotel,
                                     String from_date, String to_date,
                                     Callback<HotelsDateResponse> callbackHotelsDate) {
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
                .registerTypeAdapter(com.example.khahani.asa.model.roomkinds.Message.class,
                        new com.example.khahani.asa.model.roomkinds.MessageDeserializer("message"))
                .disableHtmlEscaping()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiService.ASA_URL)
                .client(client)
                .build();

        ApiService service = retrofit.create(ApiService.class);


        String[] params = new String[0];
        try {
            params = new String[]{"from_time_stamp", "0000-00-00+00%3A00%3A00",
                    "id_city", id_city,
                    "id_hotel", id_hotel,
                    "from_date", URLEncoder.encode(from_date, "utf-8"),
                    "to_date", URLEncoder.encode(to_date, "utf-8")};

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        service.getHotelsDate("demo",
                Asa.getSigniture("demo", params),
                "6",
                "0000-00-00+00%3A00%3A00",
                "0000-00-00+00%3A00%3A00",
                id_city,
                id_hotel,
                from_date,
                to_date).enqueue(callbackHotelsDate);
    }
}
