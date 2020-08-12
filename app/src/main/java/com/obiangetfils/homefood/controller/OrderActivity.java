package com.obiangetfils.homefood.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.fragments.navigation_fragments.OrderFragment;

public class OrderActivity extends AppCompatActivity {

    private OrderFragment orderFragment;
    private ImageView come_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        come_back = findViewById(R.id.come_back);

        orderFragment = new OrderFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_order, orderFragment)
                .commit();

        come_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}