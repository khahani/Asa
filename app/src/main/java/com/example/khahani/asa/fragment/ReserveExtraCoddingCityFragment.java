package com.example.khahani.asa.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khahani.asa.R;
import com.example.khahani.asa.model.reserve_extra_codding_city.Message;

import java.util.List;

public class ReserveExtraCoddingCityFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnReserveExtraListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;

    public ReserveExtraCoddingCityFragment() {
    }

    public static ReserveExtraCoddingCityFragment newInstance(int columnCount) {
        ReserveExtraCoddingCityFragment fragment = new ReserveExtraCoddingCityFragment();
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
        View view = inflater.inflate(R.layout.fragment_reserve_extra_codding_city_list, container, false);

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

    public void updateReserveExtra(List<Message> reserveExtras){
        recyclerView.setAdapter(new ReserveExtraCoddingCityRecyclerViewAdapter(reserveExtras, mListener));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReserveExtraListFragmentInteractionListener) {
            mListener = (OnReserveExtraListFragmentInteractionListener) context;
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

    public interface OnReserveExtraListFragmentInteractionListener {

        void onReserveExtraListFragmentInteraction(Message item);
    }
}
