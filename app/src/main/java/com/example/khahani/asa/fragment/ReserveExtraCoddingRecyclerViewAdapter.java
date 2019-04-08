package com.example.khahani.asa.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.khahani.asa.R;
import com.example.khahani.asa.fragment.ReserveExtraCoddingFragment.OnReserveExtraListFragmentInteractionListener;
import com.example.khahani.asa.model.reserve_extra_codding.Message;

import java.util.List;

public class ReserveExtraCoddingRecyclerViewAdapter extends RecyclerView.Adapter<ReserveExtraCoddingRecyclerViewAdapter.ViewHolder> {

    private final List<Message> mValues;
    private final OnReserveExtraListFragmentInteractionListener mListener;

    public ReserveExtraCoddingRecyclerViewAdapter(List<Message> items, OnReserveExtraListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reserve_extra_codding, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIranianName.setText(mValues.get(position).Extra_Codding_GuestRate);
        holder.mGuestRate.setText(mValues.get(position).Extra_Codding_IranianName);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onReserveExtraListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIranianName;
        public final TextView mGuestRate;
        public final Spinner mExtraCount;

        public Message mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIranianName = (TextView) view.findViewById(R.id.textviewIranianName);
            mGuestRate = (TextView) view.findViewById(R.id.textviewGuestRate);
            mExtraCount = view.findViewById(R.id.spinnerExtraCount);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mGuestRate.getText() + "'";
        }
    }
}
