package com.example.khahani.asa.ret;

import android.util.Log;

import com.example.khahani.asa.model.capacities.CapacitiesResponse;
import com.example.khahani.asa.model.capacities.Message;
import com.example.khahani.asa.model.capacities.MessageDeserializer;
import com.example.khahani.asa.model.cities.CitiesResponse;
import com.example.khahani.asa.model.hotels.HotelsResponse;
import com.example.khahani.asa.model.hotels_date.HotelsDateResponse;
import com.example.khahani.asa.model.loginuser.LoginUserResponse;
import com.example.khahani.asa.model.reserve15min.Reserve15MinResponse;
import com.example.khahani.asa.model.reserve15min.ReserveDetail;
import com.example.khahani.asa.model.reserve5min.Reserve5MinResponse;
import com.example.khahani.asa.model.reserve5min.RoomDetail;
import com.example.khahani.asa.model.reserve_extra_codding.ReserveExtraCoddingResponse;
import com.example.khahani.asa.model.reserve_extra_codding_city.ReserveExtraCoddingCityResponse;
import com.example.khahani.asa.model.roomkinds.RoomkindsResponse;
import com.example.khahani.asa.utils.Asa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsaService {

    private static final String VERSION = "8";

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

                    Log.d("TAG", "getCapacities: " + string);

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
        String id_roomkind = "0";

        String[] params = new String[0];
        try {
            params = new String[]{"id_city", id_city,
                    "id_hotel", id_hotel,
                    "id_roomkind", id_roomkind,
                    "from_date", URLEncoder.encode(from_date, "utf-8"),
                    "to_date", URLEncoder.encode(to_date, "utf-8"),
                    "from_time_stamp", "0000-00-00+00%3A00%3A00"};
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        service.getCapacities("demo",
                Asa.getSigniture("demo", params),
                VERSION,
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

    public static void getCities(String id_city, String id_hotel, Callback<CitiesResponse> callbackCities) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    String string = request.url().toString();
                    string = string.replace("%25", "%");
                    string = string.replace("%2B", "+");

                    Log.d("TAG", "getCities: " + string);

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
                VERSION,
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

                    Log.d("TAG", "getRoomkind: " + string);

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
                VERSION,
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
//                    string = string.replace("%2B", "+");

                    string = string.replace("0000-00-00%2B00%3A00%3A00", "0000-00-00+00%3A00%3A00");

                    Log.d("TAG", "getHotelsDate: " + string);
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
                VERSION,
                "0000-00-00+00%3A00%3A00",
                "0000-00-00+00%3A00%3A00",
                id_city,
                id_hotel,
                from_date,
                to_date).enqueue(callbackHotelsDate);
    }

    public static void postReserve5Min(String id_hotel,
                                       String from_date, String to_date,
                                       List<RoomDetail> roomDetails,
                                       Callback<Reserve5MinResponse> callbackReserve5Min) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(chain -> {
                    Request request = chain.request();

                    String string = request.url().toString();
                    string = string.replace("%25", "%");

                    string = string.replace("0000-00-00%2B00%3A00%3A00", "0000-00-00+00%3A00%3A00");

                    Log.d("TAG", "postReserve5Min: " + string);

                    Request newRequest = new Request.Builder()
                            .url(string)
                            .post(request.body())
                            .build();

                    return chain.proceed(newRequest);
                })
                .addInterceptor(logging)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(com.example.khahani.asa.model.reserve5min.Message.class,
                        new com.example.khahani.asa.model.reserve5min.MessageDeserializer("message"))
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

            String roomDetailParams = URLEncoder.encode(Asa.roomDetailToUrl(roomDetails), "utf-8");
            roomDetailParams = roomDetailParams.replace("%3D", "=");
            roomDetailParams = roomDetailParams.replace("%26", "&");

            params = new String[]{
                    "id_hotel", id_hotel,
                    "from_date", URLEncoder.encode(from_date, "utf-8"),
                    "to_date", URLEncoder.encode(to_date, "utf-8"),
                    roomDetailParams
            };

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        service.postReserve5Min("demo",
                Asa.getSigniture("demo", params),
                VERSION,
                "0000-00-00+00%3A00%3A00",
                id_hotel,
                from_date,
                to_date,
                Asa.roomDetailToMap(roomDetails)
        ).enqueue(callbackReserve5Min);
    }

    public static void putReserve15Min(String id_reserve_asa,
                                       String id_reserve_hotel,
                                       List<ReserveDetail> reserveDetails,
                                       Callback<Reserve15MinResponse> callbackReserve15Min) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(chain -> {
                    Request request = chain.request();

                    String string = request.url().toString();
                    string = string.replace("%25", "%");

                    string = string.replace("0000-00-00%2B00%3A00%3A00", "0000-00-00+00%3A00%3A00");

                    Log.d("TAG", "putReserve15Min: " + string);

                    Request newRequest = new Request.Builder()
                            .url(string)
                            .put(request.body())
                            .build();

                    return chain.proceed(newRequest);
                })
                .addInterceptor(logging)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(com.example.khahani.asa.model.reserve15min.Message.class,
                        new com.example.khahani.asa.model.reserve15min.MessageDeserializer("message"))
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

            String reserveDetailParam = URLEncoder.encode(Asa.reserveDetailToUrl(reserveDetails), "utf-8");
            reserveDetailParam = reserveDetailParam.replace("%3D", "=");
            reserveDetailParam = reserveDetailParam.replace("%26", "&");

            params = new String[]{
                    "id_reserve_hotel", id_reserve_hotel,
                    reserveDetailParam
            };

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        service.putReserve15Min(id_reserve_asa,
                "demo",
                Asa.getSigniture("demo", params),
                VERSION,
                "0000-00-00+00%3A00%3A00",
                id_reserve_hotel,
                Asa.reserveDetailToMap(reserveDetails)
        ).enqueue(callbackReserve15Min);
    }

    public static void getLoginUser(String username,
                                    String password,
                                    Callback<LoginUserResponse> callbackLoginUser) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    String string = request.url().toString();
                    string = string.replace("%25", "%");

                    string = string.replace("0000-00-00%2B00%3A00%3A00", "0000-00-00+00%3A00%3A00");

                    Log.d("TAG", "getLoginUser: " + string);

                    Request newRequest = new Request.Builder()
                            .url(string)
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(com.example.khahani.asa.model.loginuser.Message.class,
                        new com.example.khahani.asa.model.loginuser.MessageDeserializer("message"))
                .disableHtmlEscaping()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiService.ASA_URL)
                .client(client)
                .build();

        ApiService service = retrofit.create(ApiService.class);

        String md5password = Asa.getMD5(password);

        String[] params = {"username", username,
                "password", md5password};

        service.getLoginUser("demo",
                Asa.getSigniture("demo", params),
                VERSION,
                "0000-00-00+00%3A00%3A00",
                username,
                md5password
        ).enqueue(callbackLoginUser);
    }

    public static void getReserveExtraCodding(String from_date,
                                    String to_date,
                                    String id_hotel,
                                    Callback<ReserveExtraCoddingResponse> callbackReserveExtraCoddingResponse) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    String string = request.url().toString();
                    string = string.replace("%25", "%");

                    string = string.replace("0000-00-00%2B00%3A00%3A00", "0000-00-00+00%3A00%3A00");

                    Log.d("TAG", "getReserveExtraCodding: " + string);

                    Request newRequest = new Request.Builder()
                            .url(string)
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(com.example.khahani.asa.model.reserve_extra_codding.Message.class,
                        new com.example.khahani.asa.model.reserve_extra_codding.MessageDeserializer("message"))
                .disableHtmlEscaping()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiService.ASA_URL)
                .client(client)
                .build();

        String[] params = new String[0];
        try {
            params = new String[]{
                    "from_date", URLEncoder.encode(from_date, "utf-8"),
                    "to_date", URLEncoder.encode(to_date, "utf-8"),
                    "id_hotel", id_hotel
                    };

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ApiService service = retrofit.create(ApiService.class);

        try {

            service.getReserveExtraCodding("demo",
                    Asa.getSigniture("demo", params),
                    VERSION,
                    "0000-00-00+00%3A00%3A00",
                    from_date,
                    URLEncoder.encode(to_date, "utf-8"),
                    URLEncoder.encode(id_hotel, "utf-8")
            ).enqueue(callbackReserveExtraCoddingResponse);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void getReserveExtraCoddingCity(String from_date,
                                              String to_date,
                                              String id_city,
                                              Callback<ReserveExtraCoddingCityResponse> callbackReserveExtraCoddingCityResponse) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    String string = request.url().toString();
                    string = string.replace("%25", "%");

                    string = string.replace("0000-00-00%2B00%3A00%3A00", "0000-00-00+00%3A00%3A00");

                    Log.d("TAG", "getReserveExtraCoddingCity: " + string);

                    Request newRequest = new Request.Builder()
                            .url(string)
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(com.example.khahani.asa.model.reserve_extra_codding_city.Message.class,
                        new com.example.khahani.asa.model.reserve_extra_codding_city.MessageDeserializer("message"))
                .disableHtmlEscaping()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiService.ASA_URL)
                .client(client)
                .build();

        String[] params = new String[0];
        try {
            params = new String[]{
                    "from_date", URLEncoder.encode(from_date, "utf-8"),
                    "to_date", URLEncoder.encode(to_date, "utf-8"),
                    "id_city", id_city
            };

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ApiService service = retrofit.create(ApiService.class);

        try {

            service.getReserveExtraCoddingCity("demo",
                    Asa.getSigniture("demo", params),
                    VERSION,
                    "0000-00-00+00%3A00%3A00",
                    from_date,
                    URLEncoder.encode(to_date, "utf-8"),
                    URLEncoder.encode(id_city, "utf-8")
            ).enqueue(callbackReserveExtraCoddingCityResponse);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
