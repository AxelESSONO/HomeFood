package com.obiangetfils.homefood.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.model.DishItem;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class DishDetailActivity extends AppCompatActivity {

    private int quantity = 1;
    private int price = 0;
    private int total_price = 0;
    private String toolbarTitle;
    private DishItem dishItem;
    private Menu cartMenu;
    private TextView cartCountTxt;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        toolbarTitle = "Détail du plat";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        //toolbar.setBackgroundColor(getResources().getColor(R.color.toolbarTransparentColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(toolbarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageviewWidget = (ImageView) findViewById(R.id.imageview_widget);
        final TextView priceTxt = (TextView) findViewById(R.id.price);
        TextView nameWidget = (TextView) findViewById(R.id.name_widget);
        TextView descriptionTxt = (TextView) findViewById(R.id.product_description);
        final TextView quantityTxt = (TextView) findViewById(R.id.product_item_quantity);
        AppCompatImageButton minusBtn = (AppCompatImageButton) findViewById(R.id.product_item_quantity_minusBtn);
        AppCompatImageButton addBtn = (AppCompatImageButton) findViewById(R.id.product_item_quantity_plusBtn);
        Button addToCart = (Button) findViewById(R.id.add_to_cart);


        Intent dishListParcelableIntent = getIntent();

        dishItem = dishListParcelableIntent.getParcelableExtra("DISH_ITEM_LIST");

        Glide.with(getApplicationContext()).load(dishItem.getDishUri()).into(imageviewWidget);
        nameWidget.setText(dishItem.getDishName());
        price = Integer.parseInt(dishItem.getDishPrice());

        descriptionTxt.setText(dishItem.getDishDescription());
        quantityTxt.setText("" + quantity);
        total_price = price;
        priceTxt.setText("" + price + " euros");

        quantity = Integer.parseInt(quantityTxt.getText().toString());

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    total_price = total_price - price;
                    quantity--;
                    priceTxt.setText("" + total_price + " euros");
                    quantityTxt.setText("" + quantity);
                    cartCountTxt.setText("" + quantity);
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                total_price = total_price + price;
                quantity++;
                priceTxt.setText("" + total_price + " euros");
                quantityTxt.setText("" + quantity);
                cartCountTxt.setText("" + quantity);
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCartFieldInDataBase(dishItem, ""+quantity);
                gotoCartActivity(dishItem);
            }
        });

    }

    private void loadCartFieldInDataBase(DishItem dishItem, String quantity) {

        String userName, userId;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name
            userName = user.getDisplayName();
            // The user's ID
            userId = user.getUid();

            uploadCart(dishItem, quantity, userId, userName);

        } else {
            Toast.makeText(this, "Vous n'êtes pas connecté!!", Toast.LENGTH_SHORT).show();
        }


    }

    private void uploadCart(final DishItem dishItem, final String quantity, final String userId, final String userName) {

        final String cartKey = dishItem.getDishKey();
        final DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference();
        cartReference.child("CartList").child(userId).child(cartKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotCart) {


                if (!dataSnapshotCart.child("CartList").child(userId).child(cartKey).exists()) {

                    HashMap<String, Object> cartHashMap = new HashMap<>();
                    cartHashMap.put("cartKey", cartKey);
                    cartHashMap.put("cartName", dishItem.getDishName());
                    cartHashMap.put("cartDescription", dishItem.getDishDescription());
                    cartHashMap.put("cartPrice", dishItem.getDishPrice());
                    cartHashMap.put("cartCategory", dishItem.getDishCategory());
                    cartHashMap.put("cartUri", dishItem.getDishUri());

                    cartHashMap.put("cartQuantity", "" + quantity);
                    cartHashMap.put("userID", userId);
                    cartHashMap.put("userName", userName);

                    cartReference.child("CartList").child(userId).child(cartKey)
                            .updateChildren(cartHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toasty.success(DishDetailActivity.this,
                                    "Plat ajouté au panier",
                                    Toast.LENGTH_SHORT, true).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void gotoCartActivity(DishItem dishItem) {
        Intent cartActivity = new Intent(getApplicationContext(), CartActivity.class);
        cartActivity.putExtra("QUANTITY", quantity);
        cartActivity.putExtra("DISH_CART_DETAIL", dishItem);
        startActivity(cartActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        menu.add("Détail du plat");

        relativeLayout = (RelativeLayout) menu.findItem(R.id.action_cart).getActionView();
        cartCountTxt = (TextView) relativeLayout.findViewById(R.id.dish_cart_count);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cart: {
                gotoCartActivity(dishItem);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}