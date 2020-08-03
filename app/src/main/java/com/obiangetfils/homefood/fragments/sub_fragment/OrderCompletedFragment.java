package com.obiangetfils.homefood.fragments.sub_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.controller.HomeActivity;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;


public class OrderCompletedFragment extends Fragment{

    private Button homeFood, orderButton;

    public OrderCompletedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_completed, container, false);

        homeFood = (Button) view.findViewById(R.id.home_food);
        orderButton = (Button) view.findViewById(R.id.orderButton);

        homeFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getContext().startActivity(new Intent(getContext(), HomeActivity.class));
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getContext().startActivity(new Intent(getContext(), OrderActivity.class));
            }
        });

        return view;
    }
}