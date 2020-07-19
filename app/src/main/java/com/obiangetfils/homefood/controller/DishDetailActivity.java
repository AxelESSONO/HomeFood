package com.obiangetfils.homefood.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.model.DishItem;

public class DishDetailActivity extends AppCompatActivity {

    private int quantity = 1;
    private int price = 0;
    private int total_price = 0;
    private String toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);


        toolbarTitle = "Détail du plat";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
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

        DishItem dishItem = dishListParcelableIntent.getParcelableExtra("DISH_ITEM_LIST");
        String name = dishItem.getDishName();


        Glide.with(getApplicationContext()).load(dishItem.getDishUri()).into(imageviewWidget);
        nameWidget.setText(dishItem.getDishName());
        price = Integer.parseInt(dishItem.getDishPrice());

        descriptionTxt.setText(dishItem.getDishDescription());
        quantityTxt.setText("" + quantity);
        total_price = price;
        priceTxt.setText("" + price + "euros");

        quantity = Integer.parseInt(quantityTxt.getText().toString());

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {

                    total_price = total_price - price;
                    quantity--;
                    priceTxt.setText("" + total_price + "euros");
                    quantityTxt.setText("" + quantity);

                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                total_price = total_price + price;
                quantity++;
                priceTxt.setText("" + total_price + "euros");
                quantityTxt.setText("" + quantity);
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartActivity = new Intent(getApplicationContext(), CartActivity.class);
                cartActivity.putExtra("QUANTITY", quantity);

                startActivity(cartActivity);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        menu.add("Détail du plat");
        return true;
    }

}