package com.obiangetfils.homefood.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.adapter.SpacesItemDecoration;
import com.obiangetfils.homefood.adapter.StaggeredRecyclerViewAdapter;
import com.obiangetfils.homefood.model.DishItem;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {

    private static final String MENUS = "menus";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private String CATEGORY_IMAGE = "CATEGORY_IMAGE";
    private ImageView collapsedImage;
    private String nameCategory;
    private int imageCategory;
    private Intent intent;
    private static final String TAG = "FoodActivity";
    private static final int NUM_COLUMNS = 2;
    private  StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView recyclerView;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        intent = getIntent();
        nameCategory = intent.getStringExtra(CATEGORY_NAME);
        imageCategory = intent.getIntExtra(CATEGORY_IMAGE, 0);
        collapsedImage = (ImageView) findViewById(R.id.collapsed_image);
        collapsedImage.setImageResource(imageCategory);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(nameCategory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initImageBitmaps();

    }

    private void initImageBitmaps() {

        // Get a reference
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child(MENUS).child(nameCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> dishKeyList = new ArrayList<>();
                for (DataSnapshot dataKeySnapshot : dataSnapshot.getChildren()){
                    dishKeyList.add(dataKeySnapshot.getKey());
                }

                List<DishItem> dishItemArrayList = new ArrayList<>();
                for (int i = 0; i < dishKeyList.size(); i++){

                    String dishKey = dataSnapshot.child(dishKeyList.get(i)).child("dishKey").getValue(String.class);
                    String dishUri = dataSnapshot.child(dishKeyList.get(i)).child("dishUri").getValue(String.class);
                    String dishCategory = dataSnapshot.child(dishKeyList.get(i)).child("dishCategory").getValue(String.class);
                    String dishPrice = dataSnapshot.child(dishKeyList.get(i)).child("dishPrice").getValue(String.class);
                    String dishDescription = dataSnapshot.child(dishKeyList.get(i)).child("dishDescription").getValue(String.class);
                    String dishName = dataSnapshot.child(dishKeyList.get(i)).child("dishName").getValue(String.class);

                    DishItem dishObject = new DishItem(dishName, dishDescription, dishPrice, dishCategory, dishUri, dishKey);
                    dishItemArrayList.add(dishObject);
                }

                initRecyclerView(dishItemArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    private void initRecyclerView(List<DishItem> dishItemArrayList) {

        Log.d(TAG, "initRecyclerView: initializing staggered recyclerview.");

        recyclerView = findViewById(R.id.recycler_view_collapse);
        staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter(this, dishItemArrayList);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        menu.add("Choisir son repas");
        return true;
    }

}