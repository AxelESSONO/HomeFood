package com.obiangetfils.homefood.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.adapter.SliderAdapterExample;
import com.obiangetfils.homefood.model.SliderItem;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView imageUser;
    private FloatingActionButton addProductToDataBase;
    private TextView nameUser;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private SliderView sliderView;
    private SliderAdapterExample adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        imageUser = (ImageView) findViewById(R.id.user_image);
        nameUser = (TextView) findViewById(R.id.username_drawer);
        addProductToDataBase = (FloatingActionButton) findViewById(R.id.add_product_floating_button);


        sliderView = findViewById(R.id.imageSlider);
        adapter = new SliderAdapterExample(getApplicationContext());


        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);


        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_order,
                R.id.nav_offer,
                R.id.nav_customer_service,
                R.id.nav_settings)
                .setDrawerLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Intent intent = getIntent();
        String loginType = intent.getStringExtra("LOGIN_TYPE");

        addProductToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddDishToDataBaseActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String uri[] = {"https://buzzultra.com/wp-content/uploads/2018/06/recette-avocat-tomate.jpg",
                "https://img-3.journaldesfemmes.fr/MbINIBpux71Onzs4zyyAJ5v-2Rw=/748x499/smart/5c953c0549344ab99e2de2ab3c345aef/recipe-jdf/374879.jpg",
                "https://larecette.net/wp-content/uploads/2020/02/iStock-868408930.jpg",
                "https://prod2.herta.be/sites/default/files/recette-plat-noel-2018-header-final.jpg",
                "https://cac.img.pmdstatic.net/fit/http.3A.2F.2Fprd2-bone-image.2Es3-website-eu-west-1.2Eamazonaws.2Ecom.2Fcac.2F2018.2F09.2F25.2F826a6cdf-f772-4191-ab13-74052753a5eb.2Ejpeg/1107x600/quality/65/magret-de-canard-au-four.jpg",
                "https://www.avenuedesvins.fr/modules/leoblog/views/img/b/b-cover%20glace%20et%20vin.jpg",
                "https://img.cuisineaz.com/660x660/2019-08-07/i149735-glace-cremeuse-a-la-vanille-sans-sorbetiere.jpeg",
                "https://chefcuisto.com/files/2017/02/avocat-au-thon.jpg"
        };

        for (int i = 0; i < uri.length; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setImageUrl(uri[i]);
            adapter.addItem(sliderItem);
            sliderView.startAutoCycle();
        }
    }
}