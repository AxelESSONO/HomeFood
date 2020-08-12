package com.obiangetfils.homefood.fragments.sub_fragment.payment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.fragments.sub_fragment.payment.creditCard.CreditCardBackFragment;
import com.obiangetfils.homefood.fragments.sub_fragment.payment.creditCard.CreditCardFrontFragment;

public class CreditCardFragment extends Fragment {

    private CreditCardFrontFragment creditCardFrontFragment;
    private CreditCardBackFragment creditCardBackFragment;
    private Button switchButton;
    private Boolean side;

    public CreditCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit_card, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("FRAGMENT_PAYMENT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SIDE", true);
        editor.commit();

        switchButton = (Button) view.findViewById(R.id.switch_fragment);
        switchButton.setText("Arrière de la carte");

        creditCardFrontFragment = new CreditCardFrontFragment();
        creditCardBackFragment = new CreditCardBackFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.credit_card_inititial_container, creditCardFrontFragment)
                .commit();

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getBoolean("SIDE", true)) {
                    switchButton.setText("Arrière de la carte");
                    editor.putBoolean("SIDE", false);
                    editor.apply();
                    switchFragment(creditCardFrontFragment);

                } else  {
                    switchButton.setText("Avant de la carte");
                    editor.putBoolean("SIDE", true);
                    editor.apply();
                    switchFragment(creditCardBackFragment);
                }
            }
        });

        return view;
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