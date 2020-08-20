package com.obiangetfils.homefood.fragments.sub_fragment.payment.creditCard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

        return view;
    }

}