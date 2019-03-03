package com.example.khahani.asa.ret;


import com.example.khahani.asa.model.capacities.CapacitiesResponse;
import com.example.khahani.asa.model.cities.CitiesResponse;
import com.example.khahani.asa.model.hotels.HotelsResponse;
import com.example.khahani.asa.model.hotels_date.HotelsDateResponse;
import com.example.khahani.asa.model.loginuser.LoginUserResponse;
import com.example.khahani.asa.model.reserve15min.Reserve15MinResponse;
import com.example.khahani.asa.model.reserve5min.Reserve5MinResponse;
import com.example.khahani.asa.model.roomkinds.RoomkindsResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

//dynamic response
//https://stackoverflow.com/questions/24279245/how-to-handle-dynamic-json-in-retrofit

public interface ApiService {

    public static final String ASA_URL = "https://api.asacrs.com/asacrs/api/";

    @GET("cities")
    Call<CitiesResponse> getCities(
            @Query("access_key_id") String access_key_id,
            @Query("signature") String signature,
            @Query("version") String version,
            @Query("client_time_stamp") String client_time_stamp,
            @Query("from_time_stamp") String from_time_stamp,
            @Query("id_city") String id_city,
            @Query("id_hotel") String id_hotel);

    @GET("hotels")
    Call<HotelsResponse> getHotels(
            @Query("access_key_id") String access_key_id,
            @Query("signature") String signature,
            @Query("version") String version,
            @Query("client_time_stamp") String client_time_stamp,
            @Query("from_time_stamp") String from_time_stamp,
            @Query("id_city") String id_city,
            @Query("id_hotel") String id_hotel);

    @GET("roomkinds")
    Call<RoomkindsResponse> getRoomkinds(
            @Query("access_key_id") String access_key_id,
            @Query("signature") String signature,
            @Query("version") String version,
            @Query("client_time_stamp") String client_time_stamp,
            @Query("from_time_stamp") String from_time_stamp,
            @Query("id_hotel") String id_hotel);

    @GET("capacities")
    Call<CapacitiesResponse> getCapacities(
            @Query("access_key_id") String access_key_id,
            @Query("signature") String signature,
            @Query("version") String version,
            @Query("client_time_stamp") String client_time_stamp,
            @Query("id_city") String id_city,
            @Query("id_hotel") String id_hotel,
            @Query("id_roomkind") String id_roomkind,
            @Query("from_date") String from_date,
            @Query("to_date") String to_date,
            @Query("from_time_stamp") String from_time_stamp);

    @GET("hotels-date")
    Call<HotelsDateResponse> getHotelsDate(
            @Query("access_key_id") String access_key_id,
            @Query("signature") String signature,
            @Query("version") String version,
            @Query("client_time_stamp") String client_time_stamp,
            @Query("from_time_stamp") String from_time_stamp,
            @Query("id_city") String id_city,
            @Query("id_hotel") String id_hotel,
            @Query("from_date") String from_date,
            @Query("to_date") String to_date);


    @POST("reserve")
    Call<Reserve5MinResponse> postReserve5Min(
            @Query("access_key_id") String access_key_id,
            @Query("signature") String signature,
            @Query("version") String version,
            @Query("client_time_stamp") String client_time_stamp,
            @Query("id_hotel") String id_hotel,
            @Query("from_date") String from_date,
            @Query("to_date") String to_date,
            @QueryMap() Map<String, String> room_detail);

    @POST("reserve")
    Call<JSONObject> postReserve5Min2(
            @Query("access_key_id") String access_key_id,
            @Query("signature") String signature,
            @Query("version") String version,
            @Query("client_time_stamp") String client_time_stamp,
            @Query("id_hotel") String id_hotel,
            @Query("from_date") String from_date,
            @Query("to_date") String to_date,
            @QueryMap() Map<String, String> room_detail);


    @PUT("reserve/{id_reserve_asa}")
    Call<Reserve15MinResponse> putReserve15Min(
            @Path("id_reserve_asa") String id_reserve_asa,
            @Query("access_key_id") String access_key_id,
            @Query("signature") String signature,
            @Query("version") String version,
            @Query("client_time_stamp") String client_time_stamp,
            @Query("id_reserve_hotel") String id_reserve_hotel,
            @QueryMap() Map<String, String> reserve_detail);



    @GET("loginuser")
    Call<LoginUserResponse> getLoginUser(@Query("access_key_id") String access_key_id,
                                         @Query("signature") String signature,
                                         @Query("version") String version,
                                         @Query("client_time_stamp") String client_time_stamp,
                                         @Query("username") String username,
                                         @Query("password") String password);

}
