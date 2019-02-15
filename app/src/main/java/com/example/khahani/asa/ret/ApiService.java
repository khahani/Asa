package com.example.khahani.asa.ret;


import com.example.khahani.asa.model.capacities.CapacitiesResponse;
import com.example.khahani.asa.model.cities.CitiesResponse;
import com.example.khahani.asa.model.hotels.HotelsResponse;
import com.example.khahani.asa.model.hotels_date.HotelsDateResponse;
import com.example.khahani.asa.model.reserve5min.Reserve5MinRespose;
import com.example.khahani.asa.model.roomkinds.RoomkindsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Call<Reserve5MinRespose> postReserve5Min(
            @Query("access_key_id") String access_key_id,
            @Query("signature") String signature,
            @Query("version") String version,
            @Query("client_time_stamp") String client_time_stamp,
            @Query("id_hotel") String id_hotel,
            @Query("from_date") String from_date,
            @Query("to_date") String to_date,
            @QueryMap() Map<String, String> room_detail);


}
