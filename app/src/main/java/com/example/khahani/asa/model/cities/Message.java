package com.example.khahani.asa.model.cities;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("code")
    public String code;
    @SerializedName("persian_name")
    public String persian_name;
    @SerializedName("foreign_name")
    public String foreign_name;
    @SerializedName("short_desc")
    public String short_desc;
    @SerializedName("record_timestamp")
    public String record_timestamp;

}
