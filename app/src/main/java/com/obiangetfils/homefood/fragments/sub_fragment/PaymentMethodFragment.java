package com.obiangetfils.homefood.fragments.sub_fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.fragments.sub_fragment.payment.CreditCardFragment;
import com.obiangetfils.homefood.fragments.sub_fragment.payment.PayCashFragment;
import com.obiangetfils.homefood.fragments.sub_fragment.payment.PayPalFragment;

public class PaymentMethodFragment extends Fragment implements View.OnClickListener {

    // 1 - Declare our interface that will be implemented by any container activity
    public interface OnButtonPaymentClickedListener {
        public void onButtonPaymentClicked(View view);
    }

    //2 - Declare callback
    private OnButtonPaymentClickedListener mCallback;
    private Button prev, next;
    private RelativeLayout relativeLayout;
    private LinearLayout creditCard, payCash, payPal;

    private CreditCardFragment creditCardFragment;
    private PayCashFragment payCashFragment;
    private PayPalFragment payPalFragment;


    public PaymentMethodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payement_method, container, false);

        prev = (Button) view.findViewById(R.id.prev_button);
        next = (Button) view.findViewById(R.id.next_button);
        creditCard = (LinearLayout) view.findViewById(R.id.creditCard);
        payCash = (LinearLayout) view.findViewById(R.id.payCash);
        payPal = (LinearLayout) view.findViewById(R.id.payPal);

        creditCardFragment = new CreditCardFragment();
        payCashFragment = new PayCashFragment();
        payPalFragment = new PayPalFragment();

        //Load PayPal Fragment
        creditCard.setBackground(getResources().getDrawable(R.drawable.selected_backgroung));
        loadNewFragment(creditCardFragment, view);


        onClickMethodPayment(creditCard, payCash, payPal, view);

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

    private void onClickMethodPayment(LinearLayout creditCard, LinearLayout payCash, LinearLayout payPal, View view) {

        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creditCard.setBackground(getResources().getDrawable(R.drawable.selected_backgroung));
                payCash.setBackground(getResources().getDrawable(R.drawable.normal_background));
                payPal.setBackground(getResources().getDrawable(R.drawable.normal_background));
                loadNewFragment(creditCardFragment, view);
            }
        });

        payCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creditCard.setBackground(getResources().getDrawable(R.drawable.normal_background));
                payCash.setBackground(getResources().getDrawable(R.drawable.selected_backgroung));
                payPal.setBackground(getResources().getDrawable(R.drawable.normal_background));
                loadNewFragment(payCashFragment, view);
            }
        });

        payPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creditCard.setBackground(getResources().getDrawable(R.drawable.normal_background));
                payCash.setBackground(getResources().getDrawable(R.drawable.normal_background));
                payPal.setBackground(getResources().getDrawable(R.drawable.selected_backgroung));
                loadNewFragment(payPalFragment, view);
            }
        });

    }

    private void loadNewFragment(Fragment fragment, View view) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.payment_method_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 4 - Call the method that creating callback after being attached to parent activity
        this.createCallbackToParentActivity();
    }

    // 3 - Create callback to parent activity
    private void createCallbackToParentActivity() {
        try {
            //Parent activity will automatically subscribe to callback
            mCallback = (OnButtonPaymentClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnButtonPaymentClickedListener");
        }
    }

    @Override
    public void onClick(View v) {
        // 5 - Spread the click to the parent activity
        mCallback.onButtonPaymentClicked(v);
    }
}