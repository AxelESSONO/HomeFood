package com.obiangetfils.homefood.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.adapter.SpacesItemDecoration;
import com.obiangetfils.homefood.adapter.StaggeredRecyclerViewAdapter;
import com.obiangetfils.homefood.model.DishItem;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {

    private ImageView collapsedImage;
    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private String CATEGORY_IMAGE = "CATEGORY_IMAGE";
    private String nameCategory;
    private int imageCategory;
    private Intent intent;
    private static final String TAG = "FoodActivity";
    private static final int NUM_COLUMNS = 2;
    private List<DishItem> dishItemList;
    private  StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

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
        dishItemList = new ArrayList<>();
        dishItemList.add(new DishItem("https://resize-elle.ladmedia.fr/r/625,,forcex/crop/625,804,center-middle,forcex,ffffff/img/var/plain_site/storage/images/elle-a-table/les-dossiers-de-la-redaction/news-de-la-redaction/glace-sante-3489962/81131123-1-fre-FR/Les-glaces-sante-de-l-ete.jpg",
                "Glace santé - Elle à Table",
                "15€"));

        dishItemList.add(new DishItem("https://fac.img.pmdstatic.net/fit/http.3A.2F.2Fprd2-bone-image.2Es3-website-eu-west-1.2Eamazonaws.2Ecom.2Ffac.2F2018.2F07.2F30.2Fc5b01c14-a0f4-46f9-92dd-c9758b127651.2Ejpeg/850x478/quality/90/crop-from/center/glace-au-yaourt-et-aux-fraises.jpeg",
                "3 Accords Gourmands", "5€"));

        dishItemList.add(new DishItem("https://fac.img.pmdstatic.net/fit/http.3A.2F.2Fprd2-bone-image.2Es3-website-eu-west-1.2Eamazonaws.2Ecom.2Ffac.2F2018.2F07.2F30.2Fc5b01c14-a0f4-46f9-92dd-c9758b127651.2Ejpeg/850x478/quality/90/crop-from/center/glace-au-yaourt-et-aux-fraises.jpeg",
                "Glace au yaourt et aux fraises",
                "20€"));

        dishItemList.add(new DishItem("https://qccdn.ar-cdn.com/recipes/port250/652092bd-f082-4ffc-b214-c7e38c3ae3e5.jpg",
                "Crème glacée érable et noix",
                "30€"));

        dishItemList.add(new DishItem("https://www.cookomix.com/wp-content/uploads/2018/05/glace-yaourt-thermomix-800x600.jpg",
                "Glace au yaourt au thermomix",
                "30€"));

        dishItemList.add(new DishItem("https://www.galbani.fr/wp-content/uploads/2017/07/glace_vanille_igi.jpg",
                "Glace Vanille et Mascarpone",
                "18€"));

        dishItemList.add(new DishItem("https://fac.img.pmdstatic.net/fit/http.3A.2F.2Fprd2-bone-image.2Es3-website-eu-west-1.2Eamazonaws.2Ecom.2Ffac.2F2019.2F06.2F26.2Fa0197493-5106-49d0-9dc8-0924668b1372.2Ejpeg/750x562/quality/80/crop-from/center/cr/wqkgU2h1dHRlcnN0b2Nrwq4gLyBGZW1tZSBBY3R1ZWxsZQ%3D%3D/glace-au-chocolat.jpeg",
                "Glace au chocolat",
                "20€"));

        dishItemList.add(new DishItem("https://bo.miogo.fr/asset/php6bo1YO_57ecc12d7ace5.jpg",
                "Glace au chocolat",
                "19€"));

        dishItemList.add(new DishItem("https://france3-regions.francetvinfo.fr/bourgogne-franche-comte/sites/regions_france3/files/styles/top_big/public/assets/images/2019/08/22/cornets_de_glace_ete-4388827.jpg?itok=OdI6GBaa",
                "Glaces sans allergènes",
                "8€"));

        dishItemList.add(new DishItem("https://www.picard.fr/dw/image/v2/AAHV_PRD/on/demandware.static/-/Sites-catalog-picard/default/dw2339f873/produits/glaces-sorbets/edition/000000000000065540_E1.jpg?sw=672&sh=392",
                "Nougat glacé framboise",
                "16€"));

        initRecyclerView();

    }

    private void initRecyclerView() {

        Log.d(TAG, "initRecyclerView: initializing staggered recyclerview.");

        recyclerView = findViewById(R.id.recycler_view_collapse);
        staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter(this, dishItemList);
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