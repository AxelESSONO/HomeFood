package com.obiangetfils.homefood.fragments.sub_fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.obiangetfils.homefood.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class PaymentMethodFragment extends Fragment implements View.OnClickListener{

    // 1 - Declare our interface that will be implemented by any container activity
    public interface OnButtonPaymentClickedListener {
        public void onButtonPaymentClicked(View view);
    }

    //2 - Declare callback
    private OnButtonPaymentClickedListener mCallback;
    private Button prev, next;

    public PaymentMethodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payement_method, container, false);

        prev = (Button) view.findViewById(R.id.prev_button);
        next = (Button) view.findViewById(R.id.next_button);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().onBackPressed();
            }
        });

        next.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 4 - Call the method that creating callback after being attached to parent activity
        this.createCallbackToParentActivity();
    }

    // 3 - Create callback to parent activity
    private void createCallbackToParentActivity(){
        try {
            //Parent activity will automatically subscribe to callback
            mCallback = (OnButtonPaymentClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement OnButtonPaymentClickedListener");
        }
    }

    @Override
    public void onClick(View v) {
        // 5 - Spread the click to the parent activity
        mCallback.onButtonPaymentClicked(v);
    }
}