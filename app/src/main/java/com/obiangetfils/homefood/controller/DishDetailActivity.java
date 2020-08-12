package com.obiangetfils.homefood.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import hyogeun.github.com.colorratingbarlib.ColorRatingBar;

public class DishDetailActivity extends AppCompatActivity {

    private int quantity = 1;
    private int price = 0;
    private int total_price = 0;
    private String toolbarTitle;
    private DishItem dishItem;
    private Menu cartMenu;
    private TextView cartCountTxt, product_ratings_count;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ImageView imageviewWidget;
    private TextView nameWidget, descriptionTxt;
    private AppCompatImageButton minusBtn, addBtn;
    private Button addToCart;
    private static final int NUM_COLUMNS = 2;
    private DatabaseReference reference;
    private static final String MENUS = "menus";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private String CATEGORY_IMAGE = "CATEGORY_IMAGE";
    private ColorRatingBar ratingBar;
    private CollapsingToolbarLayout myCollapsingToolbar;

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

        myCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        myCollapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimaryToolbar));

        imageviewWidget = (ImageView) findViewById(R.id.imageview_widget);
        final TextView priceTxt = (TextView) findViewById(R.id.price);
        nameWidget = (TextView) findViewById(R.id.name_widget);
        descriptionTxt = (TextView) findViewById(R.id.product_description);
        final TextView quantityTxt = (TextView) findViewById(R.id.product_item_quantity);
        minusBtn = (AppCompatImageButton) findViewById(R.id.product_item_quantity_minusBtn);
        addBtn = (AppCompatImageButton) findViewById(R.id.product_item_quantity_plusBtn);
        addToCart = (Button) findViewById(R.id.add_to_cart);
        ratingBar = (ColorRatingBar) findViewById(R.id.product_rating_bar);
        product_ratings_count = (TextView) findViewById(R.id.product_ratings_count);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                product_ratings_count.setText(""+ratingBar.getRating());
            }
        });

        Intent dishListParcelableIntent = getIntent();

        dishItem = dishListParcelableIntent.getParcelableExtra("DISH_ITEM_LIST");

        loadRecyclerView(dishItem);

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

    private void loadRecyclerView(DishItem dishItem) {

        String nameCategory = dishItem.getDishCategory();
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

                    if (dishItem.isEqualTo(dishObject)){
                        continue;
                    } else {
                        dishItemArrayList.add(dishObject);
                    }
                }

                initRecyclerView(dishItemArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    private void initRecyclerView(List<DishItem> dishItemArrayList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_other_products);
        staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter(this, dishItemArrayList);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
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