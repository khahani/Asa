package com.example.khahani.asa.fragment;

import com.example.khahani.asa.model.capacities.CapacitiesResponse;
import com.example.khahani.asa.model.capacities.Message;

import java.util.ArrayList;
import java.util.List;

public class ReserveRoomViewModel {
    public String hotel_id;
    public String room_kind_id;
    public String room_kind_name;
    public String room_availibility;
    public List<String> iranian_date;
    public List<String> iranian_daily_board_rate_TSI;
    public List<String> iranian_extra_bed_rate_TSI;
    public List<String> iranian_child_rate_TSI;
    public int selectedRoomsCount;
    public int selectedAdultsCount;
    public int selectedChildsCount;

    public ReserveRoomViewModel() {
        iranian_date = new ArrayList<>();
        iranian_daily_board_rate_TSI = new ArrayList<>();
        iranian_extra_bed_rate_TSI = new ArrayList<>();
        iranian_child_rate_TSI = new ArrayList<>();
    }

    public static List<ReserveRoomViewModel> fromCapacities(List<Message> capacities) {
        ArrayList<ReserveRoomViewModel> data = new ArrayList<>();

        for (int i = 0; i < capacities.size(); i++) {

            Message current = capacities.get(i);

            if (Integer.parseInt(current.room_availibility) <= 0)
                continue;

            ReserveRoomViewModel model;

            boolean exists = false;
            for (int j = 0; j < data.size(); j++) {

                if (capacities.get(i).room_kind_id.equals(
                        data.get(j).room_kind_id)) { // exists

                    model = data.get(j);
                    model.iranian_date.add(current.iranian_date);
                    model.iranian_daily_board_rate_TSI.add(current.iranian_daily_board_rate_TSI);
                    model.iranian_child_rate_TSI.add(current.iranian_child_rate_TSI);
                    model.iranian_extra_bed_rate_TSI.add(current.iranian_extra_bed_rate_TSI);

                    exists = true;
                    break;
                }
            }

            if (!exists) {
                model = new ReserveRoomViewModel();
                model.hotel_id = current.hotel_id;
                model.room_availibility =
                        Integer.parseInt(current.room_availibility) > 3 ?
                                "3" : current.room_availibility;

                model.room_kind_id = current.room_kind_id;
                model.room_kind_name = current.room_kind_name;
                model.iranian_date.add(current.iranian_date);
                model.iranian_daily_board_rate_TSI.add(current.iranian_daily_board_rate_TSI);
                model.iranian_child_rate_TSI.add(current.iranian_child_rate_TSI);
                model.iranian_extra_bed_rate_TSI.add(current.iranian_extra_bed_rate_TSI);

                data.add(model);
            }
        }
        return data;
    }
}
