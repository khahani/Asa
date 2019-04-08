package com.example.khahani.asa.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.khahani.asa.AsaActivity;
import com.example.khahani.asa.R;
import com.example.khahani.asa.databinding.ActivityReserveExtraCoddingCityBinding;
import com.example.khahani.asa.fragment.ReserveExtraCoddingCityFragment;
import com.example.khahani.asa.fragment.ReserveExtraCoddingFragment;
import com.example.khahani.asa.model.reserve_extra_codding_city.Message;
import com.example.khahani.asa.model.reserve_extra_codding_city.ReserveExtraCoddingCityResponse;
import com.example.khahani.asa.ret.AsaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveExtraCoddingCityActivity extends AsaActivity
implements ReserveExtraCoddingCityFragment.OnReserveExtraListFragmentInteractionListener{

    private ActivityReserveExtraCoddingCityBinding mBinding;

    private Callback<ReserveExtraCoddingCityResponse> callbackReserveExtraCoddingCity = new Callback<ReserveExtraCoddingCityResponse>() {
        @Override
        public void onResponse(Call<ReserveExtraCoddingCityResponse> call, Response<ReserveExtraCoddingCityResponse> response) {
            mBinding.loading.setVisibility(View.INVISIBLE);

            reserveExtraCoddingCityFragment = ReserveExtraCoddingCityFragment.newInstance(1);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, reserveExtraCoddingCityFragment)
                    .commit();

            getSupportFragmentManager().executePendingTransactions();

            reserveExtraCoddingCityFragment.updateReserveExtra(response.body().message);
        }

        @Override
        public void onFailure(Call<ReserveExtraCoddingCityResponse> call, Throwable t) {
            mBinding.loading.setVisibility(View.INVISIBLE);
            Toast.makeText(ReserveExtraCoddingCityActivity.this, "Network Failed", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reserve_extra_codding_city);

        if (!getIntent().hasExtra("from_date")) {
            Toast.makeText(this, "wrong data", Toast.LENGTH_SHORT).show();
            finish();
        }

        String from_date = getIntent().getStringExtra("from_date");
        String to_date = getIntent().getStringExtra("to_date");
        String id_city = getIntent().getStringExtra("id_city");

        // TODO: 4/8/2019 Must Removed, just for testing 
        id_city = "5898";

        mBinding.loading.setVisibility(View.VISIBLE);
        AsaService.getReserveExtraCoddingCity(from_date, to_date, id_city, callbackReserveExtraCoddingCity);

    }

    @Override
    public void onReserveExtraListFragmentInteraction(Message item) {

    }
}
