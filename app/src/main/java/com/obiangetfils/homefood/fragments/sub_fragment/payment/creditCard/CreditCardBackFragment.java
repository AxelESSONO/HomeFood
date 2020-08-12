package com.obiangetfils.homefood.fragments.sub_fragment.payment.creditCard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.obiangetfils.homefood.R;

public class CreditCardBackFragment extends Fragment {

    private EditText tv_cvv;

    public CreditCardBackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit_card_back, container, false);

        tv_cvv = (EditText) view.findViewById(R.id.tv_cvv);

        tv_cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = 0;
                int inputlength = tv_cvv.getText().toString().length();

                if (count <= inputlength && inputlength == 3){
                    //|| inputlength == 9 || inputlength == 14

                    tv_cvv.setText(tv_cvv.getText().toString() + " ");

                    int pos = tv_cvv.getText().length();
                    tv_cvv.setSelection(pos);

                } else if (count >= inputlength && (inputlength == 3)) {
                    tv_cvv.setText(tv_cvv.getText().toString().substring(0, tv_cvv.getText().toString().length() - 1));
                    int pos = tv_cvv.getText().length();
                    tv_cvv.setSelection(pos);
                }
                count = tv_cvv.getText().toString().length();
            }
        });

        return view;
    }
}