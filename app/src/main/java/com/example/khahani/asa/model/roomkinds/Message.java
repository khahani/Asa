package com.example.khahani.asa.model.roomkinds;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    public String id;
    @SerializedName("id_hotel")
    public String id_hotel;
    @SerializedName("iranian_board_rate")
    public String iranian_board_rate;
    @SerializedName("foreign_board_rate")
    public String foreign_board_rate;
    @SerializedName("persian_hotel_label")
    public String persian_hotel_label;
    @SerializedName("foreign_hotel_label")
    public String foreign_hotel_label;
    @SerializedName("room_kind_bed")
    public String room_kind_bed;
    @SerializedName("room_kind_internet_name")
    public String room_kind_internet_name;
    @SerializedName("extra_bed")
    public String extra_bed;
    @SerializedName("min_order")
    public String min_order;
    @SerializedName("max_order")
    public String max_order;
    @SerializedName("status")
    public String status;
    @SerializedName("record_timestamp")
    public String record_timestamp;


}
