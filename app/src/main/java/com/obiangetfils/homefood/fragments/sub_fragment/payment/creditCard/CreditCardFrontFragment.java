package com.obiangetfils.homefood.fragments.sub_fragment.payment.creditCard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.fragments.sub_fragment.payment.CreditCardFragment;
import com.obiangetfils.homefood.model.CreditCardObject;
import com.obiangetfils.homefood.utils.CreditCardExpiryTextWatcher;
import com.obiangetfils.homefood.utils.CreditCardFormattingTextWatcher;

import static com.obiangetfils.homefood.utils.CreditCardUtils.AMEX;
import static com.obiangetfils.homefood.utils.CreditCardUtils.DISCOVER;
import static com.obiangetfils.homefood.utils.CreditCardUtils.MASTERCARD;
import static com.obiangetfils.homefood.utils.CreditCardUtils.NONE;
import static com.obiangetfils.homefood.utils.CreditCardUtils.VISA;

public class CreditCardFrontFragment extends Fragment {

    public CreditCardObject creditCardObject;
    private EditText tv_card_number, tv_validity, tv_member_name;
    private ImageView ivType;
    private int count = 0;
    private boolean isDelete;

    public CreditCardFrontFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit_card_front, container, false);

        tv_card_number = (EditText) view.findViewById(R.id.tv_card_number);
        tv_validity = (EditText) view.findViewById(R.id.tv_validity);
        tv_member_name = (EditText) view.findViewById(R.id.tv_member_name);
        ivType = (ImageView) view.findViewById(R.id.ivType);

        tv_card_number.addTextChangedListener(new CreditCardFormattingTextWatcher(tv_card_number));
        tv_validity.addTextChangedListener(new CreditCardExpiryTextWatcher(tv_validity));

        tv_card_number.addTextChangedListener(new CreditCardFormattingTextWatcher(tv_card_number, this::setCardType));


        CreditCardFragment creditCardFragment= new CreditCardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("CARD_NUMBER", tv_card_number.getText().toString().trim());
        bundle.putString("CARD_VALIDITY", tv_validity.getText().toString().trim());
        bundle.putString("CARD_HOLDER", tv_member_name.getText().toString().trim());
        creditCardFragment.setArguments(bundle);


        return view;
    }

    public String getCardNumber() {
        String number = this.tv_card_number.getText().toString().trim();
        if (number.equals("")){
            number = "XXXX XXXX XXXX XXXX";
        }
        return number;
    }

    public String getValidity() {
        String validity = this.tv_validity.getText().toString().trim();
        if (validity.equals("")){
            validity = "MM/yy";
        }
        return validity;
    }

    public String getMemberName() {
        String cardHolder = this.tv_member_name.getText().toString().trim();
        if (cardHolder.equals("")){
            cardHolder = "Nom du détenteur de la carte";
        }
        return cardHolder;
    }

    public void setCardType(int type) {
        switch (type) {
            case VISA:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_visa));
                break;
            case MASTERCARD:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mastercard));
                break;
            case AMEX:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_amex));
                break;
            case DISCOVER:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_discover));
                break;
            case NONE:
                ivType.setImageResource(android.R.color.transparent);
                break;
        }
    }

}