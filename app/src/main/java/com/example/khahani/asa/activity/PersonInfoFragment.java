package com.example.khahani.asa.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.khahani.asa.R;


public class PersonInfoFragment extends Fragment {
    EditText name;
    EditText family;
    EditText codemelli;
    EditText phone;
    EditText city;
    EditText address;

    private OnFragmentInteractionListener mListener;

    public PersonInfoFragment() {
        // Required empty public constructor
    }

    public static PersonInfoFragment newInstance() {
        PersonInfoFragment fragment = new PersonInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_person_info, container, false);

        name = view.findViewById(R.id.editTextName);
        family = view.findViewById(R.id.editTextFamily);
        codemelli = view.findViewById(R.id.editTextCodeMelli);
        phone = view.findViewById(R.id.editTextPhone);
        city = view.findViewById(R.id.editTextCity);
        address = view.findViewById(R.id.editTextAddres);

        return view;
    }

    public boolean isValid(){
        if (name.getText().toString().trim().equals("")){
            return false;
        }
        if(family.getText().toString().trim().equals("")){
            return false;
        }if(codemelli.getText().toString().trim().equals("")){
            return false;
        }

        return true;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPersonFragmentInteraction();
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

    public interface OnFragmentInteractionListener {
        void onPersonFragmentInteraction();
    }
}
