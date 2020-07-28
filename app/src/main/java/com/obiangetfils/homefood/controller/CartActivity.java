package com.obiangetfils.homefood.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.obiangetfils.homefood.model.CartObject;
import com.obiangetfils.homefood.model.DishItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecycler;
    private CartAdapter cartAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private TextView cartSubtotalTxt, reductionTxt, totalPriceTxt;
    private LinearLayout cartViewEmpty;
    private int cartSubtotal, reduction, totalPrice;
    private ImageView comeBack;
    private LinearLayout toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = (LinearLayout) findViewById(R.id.toolbar);
        cartViewEmpty = (LinearLayout) findViewById(R.id.cart_view_empty);
        comeBack = (ImageView) toolbar.findViewById(R.id.come_back);

        comeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fetchData();

    }

    private void fetchData() {
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

                        initRecyclerView(cartObjectList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void initRecyclerView(List<CartObject> cartObjectList) {

        cartRecycler = (RecyclerView) findViewById(R.id.cart_items_recycler);
        if (cartObjectList.size() == 0) {
            cartViewEmpty.setVisibility(View.VISIBLE);
            cartRecycler.setVisibility(View.GONE);
        } else {
            cartViewEmpty.setVisibility(View.GONE);
            cartRecycler.setVisibility(View.VISIBLE);
            cartAdapter = new CartAdapter(this, cartObjectList);
            linearLayoutManager = new LinearLayoutManager(this);
            cartRecycler.setLayoutManager(linearLayoutManager);

            DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL);
            cartRecycler.addItemDecoration(itemDecor);

            cartRecycler.setAdapter(cartAdapter);

            cartSubtotalTxt = (TextView) findViewById(R.id.cart_subtotal);
            reductionTxt = (TextView) findViewById(R.id.cart_discount);
            totalPriceTxt = (TextView) findViewById(R.id.prix_total);

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
        cartSubtotalTxt.setText("" + initPrice + " Euros");
        reductionTxt.setText("50 Euros");
        totalPriceTxt.setText("" + (initPrice - 50) + " Euros");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        menu.add(R.string.my_cart);
        return true;
    }

}