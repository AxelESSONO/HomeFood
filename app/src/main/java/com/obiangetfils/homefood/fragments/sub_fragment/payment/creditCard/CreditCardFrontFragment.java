package com.obiangetfils.homefood.fragments.sub_fragment.payment.creditCard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obiangetfils.homefood.R;

public class CreditCardFrontFragment extends Fragment {


    public CreditCardFrontFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit_card_front, container, false);
        return view;
    }
}