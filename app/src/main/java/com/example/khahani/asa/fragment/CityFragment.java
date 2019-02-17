package com.example.khahani.asa.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khahani.asa.R;
import com.example.khahani.asa.model.cities.CitiesResponse;
import com.example.khahani.asa.model.cities.Message;
import com.example.khahani.asa.ret.AsaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CityFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = CityFragment.class.getSimpleName();

    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;

    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CityFragment() {
    }

    @SuppressWarnings("unused")
    public static CityFragment newInstance(int columnCount) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }

    public void updateCities(List<Message> messages){
        recyclerView.setAdapter(new CityRecyclerViewAdapter(messages, mListener));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Message item);
        void onLoadBegins();
        void onLoadCompleted();
        void onNetworkFailed();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mListener != null){
            mListener.onLoadBegins();
        }

        AsaService.getCities("5201", "0", callbackCities);

    }

    Callback<CitiesResponse> callbackCities = new Callback<CitiesResponse>() {
        @Override
        public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
            Log.e(TAG, "onResponse: " + response.body().toJson());

            if (mListener != null){
                mListener.onLoadCompleted();
            }

            updateCities(response.body().message);


        }

        @Override
        public void onFailure(Call<CitiesResponse> call, Throwable t) {
            Log.e(TAG, "onFailure: error", t);

            if (mListener != null){
                mListener.onLoadCompleted();
                mListener.onNetworkFailed();
            }

        }
    };


}
