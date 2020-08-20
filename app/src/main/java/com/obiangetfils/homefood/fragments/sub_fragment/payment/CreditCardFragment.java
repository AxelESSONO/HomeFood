package com.obiangetfils.homefood.fragments.sub_fragment.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.fragments.sub_fragment.payment.creditCard.CreditCardBackFragment;
import com.obiangetfils.homefood.fragments.sub_fragment.payment.creditCard.CreditCardFrontFragment;
import com.obiangetfils.homefood.model.CreditCardObject;

public class CreditCardFragment extends Fragment {

    private CreditCardFrontFragment creditCardFrontFragment;
    private CreditCardBackFragment creditCardBackFragment;
    private Button switchButton;
    private String cardNumber, validity, memberName, cvvCard;
    private int image;
    private CreditCardObject creditCardObject;
    private String switchButtonText;

    public CreditCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit_card, container, false);

        creditCardFrontFragment = new CreditCardFrontFragment();
        creditCardBackFragment = new CreditCardBackFragment();
        initView(creditCardFrontFragment);

        switchButton = (Button) view.findViewById(R.id.switch_fragment);
        switchButton.setText("ARRIERE DE LA CARTE");

        switchButtonText = switchButton.getText().toString().trim();

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switchButton.getText().toString().equals("ARRIERE DE LA CARTE")) {
                    switchFragment(creditCardBackFragment);
                    switchButton.setText("AVANT DE LA CARTE");
                }else if (switchButton.getText().toString().equals("AVANT DE LA CARTE")){
                    switchFragment(creditCardFrontFragment);
                    switchButton.setText("ARRIERE DE LA CARTE");
                }
                else {
                    Toast.makeText(getContext(), "Rien à faire", Toast.LENGTH_SHORT).show();
                    switchButton.setText("ARRIERE DE LA CARTE");
                }
            }
        });

        return view;
    }

    private void initView(CreditCardFrontFragment creditCardFrontFragment) {

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.credit_card_inititial_container, creditCardFrontFragment)
                .commit();

    }

    private void switchFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.credit_card_inititial_container, fragment)
                .commit();
    }
}