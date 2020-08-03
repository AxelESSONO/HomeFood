package com.obiangetfils.homefood.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.fragments.sub_fragment.OrderCompletedFragment;
import com.obiangetfils.homefood.fragments.sub_fragment.OrderTypeFragment;
import com.obiangetfils.homefood.fragments.sub_fragment.PaymentMethodFragment;

public class CheckoutActivity extends AppCompatActivity implements OrderTypeFragment.OnButtonClickedListener
        , PaymentMethodFragment.OnButtonPaymentClickedListener{

    //private Button continueButn;
    private ImageView comeBack;
    private TextView title;

    // 1 - Declare main fragment
    private OrderTypeFragment orderTypeFragment;

    private PaymentMethodFragment paymentMethodFragment;
    private OrderCompletedFragment orderCompletedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //continueButn = (Button) findViewById(R.id.continue_btn);
        comeBack = (ImageView) findViewById(R.id.come_back_checkout);

        paymentMethodFragment = new PaymentMethodFragment();
        orderCompletedFragment = new OrderCompletedFragment();

        // 2 - Configure and show home fragment
        this.configureAndShowMainFragment();

        comeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void configureAndShowMainFragment() {

        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        orderTypeFragment = (OrderTypeFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);

        if (orderTypeFragment == null) {
            // B - Create new main fragment
            orderTypeFragment = new OrderTypeFragment();
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_main, orderTypeFragment)
                    .commit();
        }

    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_main, fragment)
                .commit();
    }

    @Override
    public void onButtonClicked(View view) {
        changeFragment(paymentMethodFragment);
    }

    @Override
    public void onButtonPaymentClicked(View view) {
        changeFragment(orderCompletedFragment);
    }
}