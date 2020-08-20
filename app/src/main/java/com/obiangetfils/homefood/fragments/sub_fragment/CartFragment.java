package com.obiangetfils.homefood.fragments.sub_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.adapter.CartAdapter;
import com.obiangetfils.homefood.controller.CartActivity;
import com.obiangetfils.homefood.controller.CheckoutActivity;
import com.obiangetfils.homefood.controller.HomeActivity;
import com.obiangetfils.homefood.model.CartObject;
import com.obiangetfils.homefood.model.DishItem;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    private RecyclerView cartRecycler;
    private CartAdapter cartAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private TextView cartSubtotalTxt, reductionTxt, totalPriceTxt;
    private CardView promoCard, pricesCardView;
    private LinearLayout cartViewEmpty;
    private int cartSubtotal, reduction, totalPrice;
    private Button continueShoppingBtn;
    private Button cartContinue;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecycler = (RecyclerView) view.findViewById(R.id.cart_items_recycler);
        pricesCardView = (CardView) view.findViewById(R.id.prices);
        promoCard = (CardView) view.findViewById(R.id.promo_card);
        continueShoppingBtn = (Button) view.findViewById(R.id.continueShoppingBtn);
        cartContinue = (Button) view.findViewById(R.id.cart_continue_btn);

        cartViewEmpty = (LinearLayout) view.findViewById(R.id.linearLayout_empty);

        fetchData(view);

        return view;
    }

    private void fetchData(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference
                .child("CartList").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> cartKeyList = new ArrayList<>();
                        for (DataSnapshot cartDataSnapshot : dataSnapshot.getChildren()) {
                            cartKeyList.add(cartDataSnapshot.getKey());
                        }

                        List<CartObject> cartObjectList = new ArrayList<>();
                        for (int i = 0; i < cartKeyList.size(); i++) {

                            //DishItem data
                            String cartKeyCart = dataSnapshot.child(cartKeyList.get(i)).child("cartKey").getValue(String.class);
                            String cartNameCart = dataSnapshot.child(cartKeyList.get(i)).child("cartName").getValue(String.class);
                            String cartDescriptionCart = dataSnapshot.child(cartKeyList.get(i)).child("cartDescription").getValue(String.class);
                            String cartPriceCart = dataSnapshot.child(cartKeyList.get(i)).child("cartPrice").getValue(String.class);
                            String cartCategoryCart = dataSnapshot.child(cartKeyList.get(i)).child("cartCategory").getValue(String.class);
                            String cartUriCart = dataSnapshot.child(cartKeyList.get(i)).child("cartUri").getValue(String.class);

                            String cartQuantityCart = dataSnapshot.child(cartKeyList.get(i)).child("cartQuantity").getValue(String.class);
                            String userIDCart = dataSnapshot.child(cartKeyList.get(i)).child("userID").getValue(String.class);
                            String userNameCart = dataSnapshot.child(cartKeyList.get(i)).child("userName").getValue(String.class);

                            DishItem dishItem = new DishItem(cartNameCart, cartDescriptionCart, cartPriceCart, cartCategoryCart, cartUriCart, cartKeyCart);
                            CartObject cartObject = new CartObject(dishItem, cartQuantityCart, userIDCart, userNameCart);
                            cartObjectList.add(cartObject);

                        }

                        initRecyclerView(view, cartObjectList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void initRecyclerView(View view, List<CartObject> cartObjectList) {

        if (cartObjectList.size() == 0) {
            cartViewEmpty.setVisibility(View.VISIBLE);
            cartRecycler.setVisibility(View.GONE);
            promoCard.setVisibility(View.GONE);
            pricesCardView.setVisibility(View.GONE);
            cartContinue.setVisibility(View.GONE);

            continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), HomeActivity.class));
                }
            });

        } else {
            cartViewEmpty.setVisibility(View.GONE);
            cartRecycler.setVisibility(View.VISIBLE);
            promoCard.setVisibility(View.VISIBLE);
            pricesCardView.setVisibility(View.VISIBLE);
            cartContinue.setVisibility(View.VISIBLE);

            cartAdapter = new CartAdapter(getContext(), cartObjectList);
            linearLayoutManager = new LinearLayoutManager(getContext());
            cartRecycler.setLayoutManager(linearLayoutManager);
            DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
            cartRecycler.addItemDecoration(itemDecor);
            cartRecycler.setAdapter(cartAdapter);
            cartSubtotalTxt = (TextView) view.findViewById(R.id.cart_subtotal);
            reductionTxt = (TextView) view.findViewById(R.id.cart_discount);
            totalPriceTxt = (TextView) view.findViewById(R.id.prix_total);
            initPrices(cartSubtotalTxt, reductionTxt, totalPriceTxt, cartObjectList);
        }

    }

    private void initPrices(TextView cartSubtotalTxt,
                            TextView reductionTxt,
                            TextView totalPriceTxt,
                            List<CartObject> cartObjectList) {

        int initPrice = 0;
        for (int i = 0; i < cartObjectList.size(); i++) {
            int itemPrice = Integer.valueOf(cartObjectList.get(i).getDishItem().getDishPrice());
            int itemQuantity = Integer.parseInt(cartObjectList.get(i).getQuantity());
            initPrice = initPrice + (itemPrice * itemQuantity);
        }
        final int finalPrice = initPrice - 50;
        cartSubtotalTxt.setText("" + initPrice + " Euros");
        reductionTxt.setText("50 Euros");
        totalPriceTxt.setText("" + finalPrice + " Euros");

        cartContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                DishItem dishItem = cartObjectList.get(1).getDishItem();
                CartObject cartObject = cartObjectList.get(1);

                Intent checkoutIntent = new Intent(getContext(), CheckoutActivity.class);
                checkoutIntent.putExtra("TOTAL_PRICE", finalPrice);
                startActivity(checkoutIntent);
            }
        });
    }
}