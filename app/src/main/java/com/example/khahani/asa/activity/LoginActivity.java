package com.example.khahani.asa.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.khahani.asa.R;
import com.example.khahani.asa.data.UserLoginPreferencecs;
import com.example.khahani.asa.databinding.ActivityLoginBinding;
import com.example.khahani.asa.model.loginuser.LoginUserResponse;
import com.example.khahani.asa.ret.AsaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mBinding;

    private Callback<LoginUserResponse> callbackLoginUser = new Callback<LoginUserResponse>() {
        @Override
        public void onResponse(Call<LoginUserResponse> call, Response<LoginUserResponse> response) {

            mBinding.loading.setVisibility(View.INVISIBLE);

            if (response.body().type.equals("success")) {

                UserLoginPreferencecs.write(LoginActivity.this,
                        response.body().message.panel_username,
                        response.body().message.persian_name,
                        response.body().message.panel_reserve_extra,
                        sharedPreferenceChangeListener);

            } else {
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                        .setMessage("نام کاربری یا رمز عبور اشتباه است")
                        .setTitle("پیام سیستمی")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .create();

                dialog.show();

            }
        }

        @Override
        public void onFailure(Call<LoginUserResponse> call, Throwable t) {
            mBinding.loading.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Network Failed", Toast.LENGTH_SHORT).show();
        }
    };


    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener =
            (sharedPreferences, key) -> {

                if (key.equals(UserLoginPreferencecs.PREF_PANEL_RESERVE_EXTRA)) {

                    if (sharedPreferences.contains(key)) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    }

                }

            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.login.setOnClickListener(loginClick);
    }

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mBinding.username.getText().toString().equals("") ||
                    mBinding.password.getText().toString().equals("")) {

                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                        .setMessage("لطفا اطلاعات را به درستی وارد کنید")
                        .setTitle("پیام سیستمی")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .create();

                dialog.show();

                return;

            }

            String username = mBinding.username.getText().toString();
            String password = mBinding.password.getText().toString();

            mBinding.loading.setVisibility(View.VISIBLE);

            AsaService.getLoginUser(username, password, callbackLoginUser);

        }
    };
}
