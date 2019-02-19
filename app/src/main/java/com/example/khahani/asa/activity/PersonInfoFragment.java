package com.example.khahani.asa.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.khahani.asa.R;
import com.example.khahani.asa.utils.Asa;


public class PersonInfoFragment extends Fragment {
    EditText name;
    EditText family;
    EditText codemelli;
    EditText phone;
//    EditText city;
//    EditText address;

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
        phone.setImeOptions(EditorInfo.IME_ACTION_DONE);
        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // If triggered by an enter key, this is the event; otherwise, this is null.
                if (event != null) {
                    // if shift key is down, then we want to insert the '\n' char in the TextView;
                    // otherwise, the default action is to send the message.
                    if (!event.isShiftPressed()) {

                        if (mListener!= null){
                            mListener.onPersonFragmentInteraction();
                        }

                        return true;
                    }
                    return false;
                }

                if (mListener!= null){
                    mListener.onPersonFragmentInteraction();
                }

                return true;
            }
        });
        //city = view.findViewById(R.id.editTextCity);
        //address = view.findViewById(R.id.editTextAddres);

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

        if (!Asa.isValidIranianNationalCode(codemelli.getText().toString())){
            return false;
        }

        return true;
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
