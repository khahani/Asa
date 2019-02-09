package com.example.khahani.asa.fragment;

import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khahani.asa.R;
import com.example.khahani.asa.fragment.HotelFragment.OnListFragmentInteractionListener;
import com.example.khahani.asa.model.hotels.Message;

import java.util.List;


public class HotelRecyclerViewAdapter extends RecyclerView.Adapter<HotelRecyclerViewAdapter.ViewHolder> {

    private final List<Message> mValues;
    private final OnListFragmentInteractionListener mListener;

    public HotelRecyclerViewAdapter(List<Message> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hotel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).persian_name);

        if (Integer.parseInt(mValues.get(position).star) > 0) {
            holder.mRatingBar.setNumStars(Integer.parseInt(mValues.get(position).star));
            holder.mRatingBar.setRating(Float.parseFloat(mValues.get(position).star));
        }else{
            holder.mRatingBar.setVisibility(View.INVISIBLE);
        }


        holder.mView.setOnClickListener(view -> {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final AppCompatRatingBar mRatingBar;
        public Message mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mRatingBar = (AppCompatRatingBar) view.findViewById(R.id.ratingBar_star);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
