package com.example.khahani.asa.model.reserve5min;

import java.util.ArrayList;
import java.util.List;

public class RoomDetail {

    public String id_roomkind;
    public String number;
    public String adult;
    public List<String> child;

    public RoomDetail() {
        child = new ArrayList<>();
    }
}
