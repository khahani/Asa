package com.example.khahani.asa.model.loginuser;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    public String id;
    @SerializedName("panel_username")
    public String panel_username;
    @SerializedName("persian_name")
    public String persian_name;
    @SerializedName("foreign_name")
    public String foreign_name;
    @SerializedName("credit")
    public String credit;
    @SerializedName("balance_duration")
    public String balance_duration;
    @SerializedName("panel_reserve_extra")
    public String panel_reserve_extra;

}
