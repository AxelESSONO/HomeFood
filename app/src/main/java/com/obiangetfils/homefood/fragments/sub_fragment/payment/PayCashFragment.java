package com.obiangetfils.homefood.fragments.sub_fragment.payment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obiangetfils.homefood.R;

public class PayCashFragment extends Fragment {

    public PayCashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_cash, container, false);
        return view;
    }
}