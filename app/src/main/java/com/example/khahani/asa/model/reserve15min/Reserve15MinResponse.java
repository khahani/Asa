package com.example.khahani.asa.model.reserve15min;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Reserve15MinResponse {
    @SerializedName("no")
    public String no;
    @SerializedName("type")
    public String type;
    @SerializedName("asa_internet_log")
    public int asa_internet_log;
    @SerializedName("time_stamp")
    public String time_stamp;
    @SerializedName("message")
    public Message message;

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s",
                no,
                type,
                asa_internet_log,
                time_stamp,
                message.toString());
    }

    public String toJson(){
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(MessageDeserializer.class, new MessageDeserializer("message"));
        Gson gson = builder.create();
        return gson.toJson(this);
    }

    public static Reserve15MinResponse fromJson(String json){
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(MessageDeserializer.class, new MessageDeserializer("message"));
        Gson gson = builder.create();
        return gson.fromJson(json, Reserve15MinResponse.class);
    }
}
