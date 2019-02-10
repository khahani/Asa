package com.example.khahani.asa.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khahani.asa.R;
import com.example.khahani.asa.fragment.ReserveRoomFragment.OnListFragmentInteractionListener;
import com.example.khahani.asa.fragment.dummy.DummyContent.DummyItem;
import com.example.khahani.asa.model.capacities.Message;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ReserveRoomRecyclerViewAdapter extends RecyclerView.Adapter<ReserveRoomRecyclerViewAdapter.ViewHolder> {

    private final List<Message> mCapacities;
    private final OnListFragmentInteractionListener mListener;
    private final List<com.example.khahani.asa.model.roomkinds.Message> mRoomkinds;
    private Context mContext;

    public ReserveRoomRecyclerViewAdapter(List<Message> capacities, List<com.example.khahani.asa.model.roomkinds.Message> mRoomkinds, OnListFragmentInteractionListener listener) {
        mCapacities = capacities;
        this.mRoomkinds = mRoomkinds;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reserveroom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mCapacity = mCapacities.get(position);

        for (int i = 0; i < mRoomkinds.size(); i++) {
            if (mCapacities.get(position).room_kind_id.equals(mRoomkinds.get(i).id)) {
                holder.mRoomkind = mRoomkinds.get(i);
                break;
            }
        }

        holder.mTitleRoomCapcity.setText(mContext.getResources().getString(R.string.titleRoomCapacity, holder.mRoomkind.room_kind_bed));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mCapacity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCapacities.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleRoomCapcity;
//        public final TextView mContentView;
        public Message mCapacity;
        public com.example.khahani.asa.model.roomkinds.Message mRoomkind;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleRoomCapcity = (TextView) view.findViewById(R.id.textViewTitleRoomCapacity);
//            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '"  + "'";
        }
    }
}
