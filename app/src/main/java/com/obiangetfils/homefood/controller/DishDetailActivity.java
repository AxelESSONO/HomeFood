package com.obiangetfils.homefood.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.obiangetfils.homefood.R;

public class DishDetailActivity extends AppCompatActivity {

    private int quantity = 1;
    private int price = 0;
    private int total_price = 0;
    private String nameIntent, imageIntent, priceIntent;
    private String nameCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);


        nameCategory = "Détail de la commande";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(nameCategory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ImageView imageviewWidget = (ImageView) findViewById(R.id.imageview_widget);
        final TextView priceTxt = (TextView) findViewById(R.id.price);
        TextView nameWidget = (TextView) findViewById(R.id.name_widget);
        TextView descriptionTxt = (TextView) findViewById(R.id.product_description);
        final TextView quantityTxt = (TextView) findViewById(R.id.product_item_quantity);
        AppCompatImageButton minusBtn = (AppCompatImageButton) findViewById(R.id.product_item_quantity_minusBtn);
        AppCompatImageButton addBtn = (AppCompatImageButton) findViewById(R.id.product_item_quantity_plusBtn);
        Button addToCart = (Button) findViewById(R.id.add_to_cart);

        Intent intent = getIntent();
        nameIntent = intent.getStringExtra("NAME");
        imageIntent = intent.getStringExtra("IMAGE");
        priceIntent = intent.getStringExtra("PRICE");

        Glide.with(getApplicationContext()).load(imageIntent).into(imageviewWidget);
        nameWidget.setText(nameIntent);
        price = Integer.parseInt(priceIntent);

        descriptionTxt.setText(R.string.dish_description);
        quantityTxt.setText("" + quantity);
        total_price = price;
        priceTxt.setText("" + price + "€");

        quantity = Integer.parseInt(quantityTxt.getText().toString());

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {

                    total_price = total_price - price;
                    quantity--;
                    priceTxt.setText("" + total_price + "€");
                    quantityTxt.setText("" + quantity);

                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                total_price = total_price + price;
                quantity++;
                priceTxt.setText("" + total_price + "€");
                quantityTxt.setText("" + quantity);
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartActivity = new Intent(getApplicationContext(), CartActivity.class);
                cartActivity.putExtra("QUANTITY", quantity);
                cartActivity.putExtra("PRICE_UNIT", price);
                cartActivity.putExtra("DISH_NAME", nameIntent);
                cartActivity.putExtra("DISH_IMAGE", imageIntent);
                startActivity(cartActivity);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        menu.add("Détail de la commande");
        return true;
    }

}