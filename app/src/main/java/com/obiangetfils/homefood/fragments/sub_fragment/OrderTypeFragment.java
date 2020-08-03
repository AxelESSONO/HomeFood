package com.obiangetfils.homefood.fragments.sub_fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.obiangetfils.homefood.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class OrderTypeFragment extends Fragment implements View.OnClickListener{

    // 1 - Declare our interface that will be implemented by any container activity
    public interface OnButtonClickedListener {
        public void onButtonClicked(View view);
    }

    //2 - Declare callback
    private OnButtonClickedListener mCallback;
    private Button mainButton, previousButton;

    private RadioButton delivery, recover;
    private RelativeLayout relativeLayout, relativeLayoutDelivery;

    private ArrayAdapter<CharSequence> adapter;

    public OrderTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_order_type, container, false);

        //Set onClickListener to button "SHOW ME DETAILS"
        mainButton = (Button) result.findViewById(R.id.next);
        previousButton = (Button) result.findViewById(R.id.prev);
        delivery = (RadioButton) result.findViewById(R.id.delivery);
        recover = (RadioButton) result.findViewById(R.id.recover);
        relativeLayout = (RelativeLayout) result.findViewById(R.id.recover_id);
        relativeLayoutDelivery = (RelativeLayout) result.findViewById(R.id.delivery_id);


        relativeLayout.setVisibility(View.GONE);


        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayoutDelivery.setVisibility(View.GONE);
            }
        });

        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                relativeLayoutDelivery.setVisibility(View.VISIBLE);
            }
        });

        mainButton.setOnClickListener(this);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().onBackPressed();
            }
        });

        return result;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 4 - Call the method that creating callback after being attached to parent activity
        this.createCallbackToParentActivity();
    }

    @Override
    public void onClick(View v) {
        // 5 - Spread the click to the parent activity
        mCallback.onButtonClicked(v);
    }

    // 3 - Create callback to parent activity
    private void createCallbackToParentActivity(){
        try {
            //Parent activity will automatically subscribe to callback
            mCallback = (OnButtonClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement OnButtonClickedListener");
        }
    }

}