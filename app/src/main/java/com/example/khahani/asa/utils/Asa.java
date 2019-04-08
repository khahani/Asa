package com.example.khahani.asa.utils;


import android.util.Log;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntUnaryOperator;
import com.example.khahani.asa.model.reserve15min.ReserveDetail;
import com.example.khahani.asa.model.reserve5min.RoomDetail;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

//import org.apache.commons.codec.binary.Base64;

public class Asa {

    //https://www.jokecamp.com/blog/examples-of-creating-base64-hashes-using-hmac-sha256-in-different-languages/#php
    public static String getSigniture(String accessKeyId, String... signParams) {
        try {
            String secret = accessKeyId;
            String message = "";

            for (int i = 0; i < signParams.length; i++) {
                message += signParams[i];

                if (i != signParams.length - 1) {
                    if (i % 2 == 0) {
                        message += "=";
                    } else {
                        message += "&";
                    }
                }
            }

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String signiture = Base64.encode(sha256_HMAC.doFinal(message.getBytes()));
            signiture = URLEncoder.encode(signiture, "utf-8");
            Log.d("Asa", "Signiture is : " + signiture);

            return signiture;
        } catch (Exception e) {
            System.out.println("Error");
        }

        return null;
    }


    public static String getMiladiDate(String persianDate) {

        String[] splited = persianDate.split("/");
        splited[1] = Integer.toString(Integer.parseInt(splited[1]) - 1);
        persianDate = splited[0] + "/" + splited[1] + "/" + splited[2];

        PersianCalendar calendar = new PersianCalendar();
        calendar.parse(persianDate);
        Date date = calendar.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(date);

      /*  String[] splited = persianDate.split("/");


        ULocale locale = new ULocale("fa_IR@calendar=persian");

        Calendar calendar = Calendar.getInstance(locale);
        Calendar calendar2 = Calendar.getInstance();

        calendar.set(Integer.parseInt(splited[0]),Integer.parseInt(splited[1]) - 1,Integer.parseInt(splited[2]));

        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, locale);

        return df.format(calendar);*/

    }

    public static String getToDate(String from_date, String numberNights) {

        String[] splitedDate = from_date.split("/");

        int originMonth = Integer.parseInt(splitedDate[1]);

        if (originMonth == 1) {
            from_date = splitedDate[0] + "/" + splitedDate[1] + "/" + splitedDate[2];
        } else {
            splitedDate[1] = Integer.toString(
                    Integer.parseInt(splitedDate[1]) - 1
            );
            from_date = splitedDate[0] + "/" + splitedDate[1] + "/" + splitedDate[2];
        }

        PersianCalendar calendar = new PersianCalendar();
        calendar.parse(from_date);
        calendar.addPersianDate(PersianCalendar.DAY_OF_MONTH,
                Integer.parseInt(numberNights));
        int day = calendar.getPersianDay();
        int month = calendar.getPersianMonth();
        int year = calendar.getPersianYear();

        if (originMonth == 1) {
            return year + "/" + month + "/" + day;
        } else {
            return year + "/" + (month + 1) + "/" + day;
        }
    }

    public static String roomDetailToUrl(List<RoomDetail> roomDetails) {
        if (roomDetails == null || roomDetails.size() <= 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        //room_detail[0][id_roomkind]=1&room_detail[0][number]=1
        // &room_detail[0][adult]=1&room_detail[0][child][0]=1&room_detail[0][child][1]=1

        for (int i = 0; i < roomDetails.size(); i++) {

            builder.append("room_detail[" + i + "][id_roomkind]=" + roomDetails.get(i).id_roomkind);
            builder.append("&");

            builder.append("room_detail[" + i + "][number]=" + roomDetails.get(i).number);
            builder.append("&");

            builder.append("room_detail[" + i + "][adult]=" + roomDetails.get(i).adult);

            if (roomDetails.get(i).child.size() > 0) {

                builder.append("&");

                for (int j = 0; j < roomDetails.get(i).child.size(); j++) {

                    builder.append("room_detail[" + i + "][child][" + j + "]=" + roomDetails.get(i).child.get(j));

                    if (j != roomDetails.get(i).child.size() - 1)

                        builder.append("&");

                }

            }

            if (i != roomDetails.size() - 1) {
                builder.append("&");
            }

        }

        return builder.toString();
    }

    public static Map<String, String> roomDetailToMap(List<RoomDetail> roomDetails) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        if (roomDetails == null || roomDetails.size() <= 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        //room_detail[0][id_roomkind]=1&room_detail[0][number]=1
        // &room_detail[0][adult]=1&room_detail[0][child][0]=1&room_detail[0][child][1]=1

        for (int i = 0; i < roomDetails.size(); i++) {

            map.put("room_detail[" + i + "][id_roomkind]", roomDetails.get(i).id_roomkind);

            map.put("room_detail[" + i + "][number]", roomDetails.get(i).number);

            map.put("room_detail[" + i + "][adult]", roomDetails.get(i).adult);

            if (roomDetails.get(i).child.size() > 0) {

                for (int j = 0; j < roomDetails.get(i).child.size(); j++) {

                    map.put("room_detail[" + i + "][child][" + j + "]", roomDetails.get(i).child.get(j));

                }

            }
//            else{
//                map.put("room_detail[" + i + "][child][0]", "0");
//            }

        }


        return map;
    }

    public static String reserveDetailToUrl(List<ReserveDetail> reserveDetails) {
        if (reserveDetails == null || reserveDetails.size() <= 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        //reserve_detail[last_name]=تست&reserve_detail[first_name]=تست&
        // reserve_detail[adress]=مشهد - تست&reserve_detail[source]=5897&
        // reserve_detail[telephone]=0513-8786543&reserve_detail[melli_code]=8126736124&
        // reserve_detail[transfer]=0&reserve_detail[travel_with]=1&
        // reserve_detail[nation]=0&reserve_detail[message]=ندارد&
        // reserve_detail[mobile]=09123456789&reserve_detail[flight_number]=زاگرس ۲۲&
        // reserve_detail[flight_time]=11:15&reserve_detail[clerk]=نام تست

        for (int i = 0; i < reserveDetails.size(); i++) {

            builder.append("reserve_detail[last_name]=" + reserveDetails.get(i).last_name);
            builder.append("&");

            builder.append("reserve_detail[first_name]=" + reserveDetails.get(i).first_name);
            builder.append("&");

            builder.append("reserve_detail[adress]=" + reserveDetails.get(i).adress);
            builder.append("&");

            builder.append("reserve_detail[source]=" + reserveDetails.get(i).source);
            builder.append("&");

            builder.append("reserve_detail[telephone]=" + reserveDetails.get(i).telephone);
            builder.append("&");

            builder.append("reserve_detail[melli_code]=" + reserveDetails.get(i).melli_code);
            builder.append("&");

            builder.append("reserve_detail[transfer]=" + reserveDetails.get(i).transfer);
            builder.append("&");

            builder.append("reserve_detail[travel_with]=" + reserveDetails.get(i).travel_with);
            builder.append("&");

            builder.append("reserve_detail[nation]=" + reserveDetails.get(i).nation);
            builder.append("&");

            builder.append("reserve_detail[message]=" + reserveDetails.get(i).message);
            builder.append("&");

            builder.append("reserve_detail[mobile]=" + reserveDetails.get(i).mobile);
            builder.append("&");

            builder.append("reserve_detail[flight_number]=" + reserveDetails.get(i).flight_number);
            builder.append("&");

            builder.append("reserve_detail[flight_time]=" + reserveDetails.get(i).flight_time);
            builder.append("&");

            builder.append("reserve_detail[clerk]=" + reserveDetails.get(i).clerk);

        }

        return builder.toString();
    }

    public static Map<String, String> reserveDetailToMap(List<ReserveDetail> reserveDetails) {

        if (reserveDetails == null || reserveDetails.size() <= 0) {
            return null;
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //reserve_detail[last_name]=تست&reserve_detail[first_name]=تست&
        // reserve_detail[adress]=مشهد - تست&reserve_detail[source]=5897&
        // reserve_detail[telephone]=0513-8786543&reserve_detail[melli_code]=8126736124&
        // reserve_detail[transfer]=0&reserve_detail[travel_with]=1&
        // reserve_detail[nation]=0&reserve_detail[message]=ندارد&
        // reserve_detail[mobile]=09123456789&reserve_detail[flight_number]=زاگرس ۲۲&
        // reserve_detail[flight_time]=11:15&reserve_detail[clerk]=نام تست

        for (int i = 0; i < reserveDetails.size(); i++) {

            map.put("reserve_detail[last_name]", reserveDetails.get(i).last_name);
            map.put("reserve_detail[first_name]", reserveDetails.get(i).first_name);
            map.put("reserve_detail[adress]", reserveDetails.get(i).adress);
            map.put("reserve_detail[source]", reserveDetails.get(i).source);
            map.put("reserve_detail[telephone]", reserveDetails.get(i).telephone);
            map.put("reserve_detail[melli_code]", reserveDetails.get(i).melli_code);
            map.put("reserve_detail[transfer]", reserveDetails.get(i).transfer);
            map.put("reserve_detail[travel_with]", reserveDetails.get(i).travel_with);
            map.put("reserve_detail[nation]", reserveDetails.get(i).nation);
            map.put("reserve_detail[message]", reserveDetails.get(i).message);
            map.put("reserve_detail[mobile]", reserveDetails.get(i).mobile);
            map.put("reserve_detail[flight_number]", reserveDetails.get(i).flight_number);
            map.put("reserve_detail[flight_time]", reserveDetails.get(i).flight_time);
            map.put("reserve_detail[clerk]", reserveDetails.get(i).clerk);

        }

        return map;
    }

    public static boolean isValidIranianNationalCode(String codemelli) {
        if (!codemelli.matches("^\\d{10}$"))
            return false;

        int check = Integer.parseInt(codemelli.substring(9, 10));

        int sum = IntStream.range(0, 9)
                .map((IntUnaryOperator) x ->
                        Integer.parseInt(codemelli.substring(x, x + 1)) * (10 - x))
                .sum() % 11;

        return (sum < 2 && check == sum) || (sum >= 2 && check + sum == 11);
    }

    public static String getMD5(String text) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(text.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
