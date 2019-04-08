package com.example.khahani.asa.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.khahani.asa.R;
import com.example.khahani.asa.activity.ReserveExtraCoddingCityActivity;
import com.example.khahani.asa.databinding.FragmentStep1Binding;
import com.example.khahani.asa.utils.Asa;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.Format;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Step1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Step1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step1Fragment extends Fragment {

    private static final String TAG = Step1Fragment.class.getSimpleName();

    FragmentStep1Binding mBinding;

    AdapterView.OnItemSelectedListener mOnItemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int numberNights = position + 1;
                    Log.d(TAG, "Night number: " + numberNights);

                    updateToDate(numberNights);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    private View.OnClickListener mOnClickListenerButtonExtra = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.extraCoddingCity(
                    mBinding.editTextFromDate.getText().toString(),
                    mBinding.spinnerNumberNights.getSelectedItemPosition() + 1);
        }
    };

    private void updateToDate(int numberNights) {
        if (!mBinding.editTextFromDate.getText().toString().equals("")) {
//            PersianCalendar calendar = new PersianCalendar();
//            calendar.parse(mBinding.editTextFromDate.getText().toString());
//            calendar.addPersianDate(PersianCalendar.DAY_OF_MONTH, numberNights);
//            int day = calendar.getPersianDay();
//            int month = calendar.getPersianMonth();
//            int year = calendar.getPersianYear();
//            mBinding.editTextToDate.setText(year + "/" + month + "/" + day);
            mBinding.editTextToDate.setText(
                    Asa.getToDate(mBinding.editTextFromDate.getText().toString(),
                            Integer.toString(numberNights)));
        }
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View.OnClickListener mOnClickListenerFromDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.pickFromDate(v);
            }
        }
    };

    private View.OnClickListener mOnClickListenerButtonNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.nextStep(
                        mBinding.editTextFromDate.getText().toString(),
                        mBinding.spinnerNumberNights.getSelectedItemPosition() + 1
                );
            }
        }
    };


    public Step1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step1Fragment.
     */
    public static Step1Fragment newInstance(String param1, String param2) {
        Step1Fragment fragment = new Step1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step1, container, false);
        mBinding.linearLayoutRoot.requestFocus(); // for disabling focus on editText

        /* setup spinner */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.number_nights, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerNumberNights.setAdapter(adapter);
        mBinding.spinnerNumberNights.setOnItemSelectedListener(mOnItemSelectedListener);

        PersianCalendar now = new PersianCalendar();
        mBinding.editTextFromDate.setText(now.getPersianYear() + "/" + (now.getPersianMonth() + 1) + "/" + now.getPersianDay());

        mBinding.editTextFromDate.setOnClickListener(mOnClickListenerFromDate);

        mBinding.buttonNext.setOnClickListener(mOnClickListenerButtonNext);

        mBinding.buttonExtra.setOnClickListener(mOnClickListenerButtonExtra);

        return mBinding.getRoot();
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

    public void updateEditTextFromDate(String text) {
        mBinding.editTextFromDate.setText(text);
        updateToDate(mBinding.spinnerNumberNights.getSelectedItemPosition() + 1);
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
        void pickFromDate(View view);

        void nextStep(String fromDate, int numberNights);

        void extraCoddingCity(String fromDate, int numberNights);
    }
}
