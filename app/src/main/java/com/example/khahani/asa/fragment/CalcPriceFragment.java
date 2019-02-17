package com.example.khahani.asa.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.khahani.asa.R;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalcPriceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalcPriceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalcPriceFragment extends Fragment {


    private static final String ARG_CALC_ROOMS = "calcRooms";
    private static final String ARG_CALC_CHILDS = "calcChilds";
    private static final String ARG_CALC_ADULTS = "calcAdults";
    private static final String ARG_CALC_EXTRA_BEDS = "calcExtraBeds";
    private static final String ARG_CALC_START_DATE = "calcStartDate";
    private static final String ARG_CALC_END_DATE = "calcEndDate";
    private static final String ARG_CALC_NIGHT_NUMBERS = "calcNightNumbers";
    private static final String ARG_CALC_DISCOUNT = "calcDiscount";
    private static final String ARG_CALC_TOTALPRICE = "calcTotalPrice";
    private static final String ARG_CALC_MUST_PAY = "calcMustPay";

    String calcRooms;
    String calcAdults;
    String calcChilds;
    String calcExtraBeds;
    String calcStartDate;
    String calcEndDate;
    String calcNightNumbers;
    String calcDiscount;
    String calcTotalPrice;
    String calcMustPay;


    private OnFragmentInteractionListener mListener;

    public CalcPriceFragment() {
        // Required empty public constructor
    }


    public static CalcPriceFragment newInstance(String calcRooms,
                                                String calcChilds,
                                                String calcAdults,
                                                String calcExtraBeds,
                                                String calcStartDate,
                                                String calcEndDate,
                                                String calcNightNumbers,
                                                String calcDiscount,
                                                String calcTotalPrice,
                                                String calcMustPay) {

        CalcPriceFragment fragment = new CalcPriceFragment();

        Bundle args = new Bundle();
        args.putString(ARG_CALC_ADULTS, calcAdults);
        args.putString(ARG_CALC_ROOMS, calcRooms);
        args.putString(ARG_CALC_CHILDS, calcChilds);
        args.putString(ARG_CALC_ADULTS, calcAdults);
        args.putString(ARG_CALC_EXTRA_BEDS, calcExtraBeds);
        args.putString(ARG_CALC_START_DATE, calcStartDate);
        args.putString(ARG_CALC_END_DATE, calcEndDate);
        args.putString(ARG_CALC_NIGHT_NUMBERS, calcNightNumbers);
        args.putString(ARG_CALC_DISCOUNT, calcDiscount);
        args.putString(ARG_CALC_TOTALPRICE, calcTotalPrice);
        args.putString(ARG_CALC_MUST_PAY, calcMustPay);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            calcRooms = getArguments().getString(ARG_CALC_ROOMS);
            calcChilds = getArguments().getString(ARG_CALC_CHILDS);
            calcAdults = getArguments().getString(ARG_CALC_ADULTS);
            calcExtraBeds = getArguments().getString(ARG_CALC_EXTRA_BEDS);
            calcStartDate = getArguments().getString(ARG_CALC_START_DATE);
            calcEndDate = getArguments().getString(ARG_CALC_END_DATE);
            calcNightNumbers = getArguments().getString(ARG_CALC_NIGHT_NUMBERS);
            calcDiscount = getArguments().getString(ARG_CALC_DISCOUNT);
            calcTotalPrice = getArguments().getString(ARG_CALC_TOTALPRICE);
            calcMustPay = getArguments().getString(ARG_CALC_MUST_PAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calc_price, container, false);

        Button closeButton = view.findViewById(R.id.buttonClose);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onCalcPriceFragmentInteraction(null);
                }
            }
        });

        TextView textViewCalcRooms = view.findViewById(R.id.textViewCalcRooms);
        TextView textViewCalcAdults = view.findViewById(R.id.textViewCalcAdults);
        TextView textViewCalcChilds = view.findViewById(R.id.textViewCalcChilds);
        TextView textViewCalcExtraBeds = view.findViewById(R.id.textViewCalcExtraBeds);
        TextView textViewCalcNightNumbers = view.findViewById(R.id.textViewCalcNightNumbers);
        TextView textViewCalcStartDate = view.findViewById(R.id.textViewCalcStartDate);
        TextView textViewCalcDiscount = view.findViewById(R.id.textViewCalcDiscount);
        TextView textViewCalcEndDate = view.findViewById(R.id.textViewCalcEndDate);
        TextView textViewCalcTotalPrice = view.findViewById(R.id.textViewCalcTotalPrice);
        TextView textViewCalcMustPay = view.findViewById(R.id.textViewCalcMustPay);

        DecimalFormat frm = new DecimalFormat("#,###,###");
        calcDiscount = frm.format(Integer.parseInt(calcDiscount));
        calcMustPay = frm.format(Integer.parseInt(calcMustPay));
        calcTotalPrice = frm.format(Integer.parseInt(calcTotalPrice));

        textViewCalcRooms.setText(getContext().getResources().getString(R.string.textViewCalcRooms, calcRooms));
        textViewCalcAdults.setText(getContext().getResources().getString(R.string.textViewCalcAdults, calcAdults));
        textViewCalcChilds.setText(getContext().getResources().getString(R.string.textViewCalcChilds, calcChilds));
        textViewCalcExtraBeds.setText(getContext().getResources().getString(R.string.textViewCalcExtraBeds, calcExtraBeds));
        textViewCalcNightNumbers.setText(getContext().getResources().getString(R.string.textViewCalcNightNumbers, calcNightNumbers));
        textViewCalcStartDate.setText(getContext().getResources().getString(R.string.textViewCalcStartDate, calcStartDate));
        textViewCalcDiscount.setText(getContext().getResources().getString(R.string.textViewCalcDiscount, calcDiscount));
        textViewCalcEndDate.setText(getContext().getResources().getString(R.string.textViewCalcEndDate, calcEndDate));
        textViewCalcTotalPrice.setText(getContext().getResources().getString(R.string.textViewCalcTotalPrice, calcTotalPrice));
        textViewCalcMustPay.setText(getContext().getResources().getString(R.string.textViewCalcMustPay, calcMustPay));


        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCalcPriceFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onCalcPriceFragmentInteraction(Uri uri);
    }
}
