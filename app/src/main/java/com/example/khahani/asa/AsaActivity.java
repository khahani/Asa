package com.example.khahani.asa;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.khahani.asa.activity.PersonInfoFragment;
import com.example.khahani.asa.fragment.CalcPriceFragment;
import com.example.khahani.asa.fragment.CityFragment;
import com.example.khahani.asa.fragment.HotelFragment;
import com.example.khahani.asa.fragment.ReserveRoomFragment;
import com.example.khahani.asa.fragment.ReviewFragment;
import com.example.khahani.asa.fragment.Step1Fragment;
import com.example.khahani.asa.fragment.Step1FragmentViewModel;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.lang.reflect.Type;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AsaActivity extends AppCompatActivity {

    protected ProgressBar loading;

    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;

    protected PersianCalendar persianCalendar;
    protected DatePickerDialog datePickerDialog;
    protected DatePickerDialog.OnDateSetListener onDateSetListener;
    protected DialogInterface.OnCancelListener onDateCancelListener;
    protected DialogInterface.OnDismissListener onDateDismissListener;


    protected Step1Fragment step1Fragment;
    protected CityFragment cityFragment;
    protected HotelFragment hotelFragment;


    protected ReserveRoomFragment reserveRoomFragment;
    protected CalcPriceFragment calcPriceFragment;
    protected PersonInfoFragment personInfoFragment;
    protected ReviewFragment reviewFragment;

    protected void init() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        persianCalendar = new PersianCalendar();
        datePickerDialog = DatePickerDialog.newInstance(
                onDateSetListener,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );

        datePickerDialog.setOnCancelListener(onDateCancelListener);
        datePickerDialog.setOnDismissListener(onDateDismissListener);

        loading.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void networkFailed(Context context, Class<?> cls){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("خطا در برقراری اتصال با سرور")
                .setTitle("بررسی اتصالات")
                .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        restartApplication(context, cls);
                    }
                }).create();

        dialog.show();
    }

    private void restartApplication(Context context, Class<?> cls){
        Intent mStartActivity = new Intent(context, cls);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}
