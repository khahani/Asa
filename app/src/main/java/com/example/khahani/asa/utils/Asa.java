package com.example.khahani.asa.utils;

import android.util.Log;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String getMiladiDate(String persianDate){
        PersianCalendar calendar = new PersianCalendar();
        calendar.parse(persianDate);
        Date date = calendar.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(date);
    }

    public static String getToDate(String from_date, String numberNights) {
        PersianCalendar calendar = new PersianCalendar();
        calendar.parse(from_date);
        calendar.addPersianDate(PersianCalendar.DAY_OF_MONTH,
                Integer.parseInt(numberNights));
        int day = calendar.getPersianDay();
        int month = calendar.getPersianMonth();
        int year = calendar.getPersianYear();
        return year + "/" + month + "/" + day;
    }
}
