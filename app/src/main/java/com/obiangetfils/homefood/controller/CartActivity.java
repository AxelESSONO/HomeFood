package com.obiangetfils.homefood.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import com.obiangetfils.homefood.fragments.sub_fragment.CartFragment;
import com.obiangetfils.homefood.model.CartObject;
import com.obiangetfils.homefood.model.DishItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {



    private ImageView comeBack;
    private LinearLayout toolbar;

    private int finalPrice = 0;
    private CartFragment cartFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        toolbar = (LinearLayout) findViewById(R.id.toolbar);
        comeBack = (ImageView) toolbar.findViewById(R.id.come_back);
        cartFragment = new CartFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_cart, cartFragment)
                .commit();

        comeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        menu.add(R.string.my_cart);
        return true;
    }
}