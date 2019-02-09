package com.example.khahani.asa.model.hotels;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("id")
    public String id;
    @SerializedName("id_area")
    public String id_area;
    @SerializedName("city_name")
    public String city_name;
    @SerializedName("persian_name")
    public String persian_name;
    @SerializedName("foreign_name")
    public String foreign_name;
    @SerializedName("hotel_kind")
    public String hotel_kind;
    @SerializedName("short_desc")
    public String short_desc;
    @SerializedName("star")
    public String star;
    @SerializedName("address")
    public String address;
    @SerializedName("room_number")
    public String room_number;
    @SerializedName("status")
    public String status;
    @SerializedName("order_priority")
    public String order_priority;
    @SerializedName("record_timestamp")
    public String record_timestamp;


}
