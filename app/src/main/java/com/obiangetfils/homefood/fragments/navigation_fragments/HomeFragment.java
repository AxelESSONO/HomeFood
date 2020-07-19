package com.obiangetfils.homefood.fragments.navigation_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.adapter.MenuAdapter;
import com.obiangetfils.homefood.adapter.SpacesItemDecoration;
import com.obiangetfils.homefood.model.MenuObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewMenu;
    private List<MenuObject> menuObjectList;
    private MenuAdapter menuAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        menuAdapter = new MenuAdapter(menuObjectList, getContext());

        // Load List
        menuObjectList = new ArrayList<>();
        menuObjectList.add(new MenuObject("Entrée", R.drawable.ic_entree));
        menuObjectList.add(new MenuObject("Dessert", R.drawable.ic_dessert_icon));
        menuObjectList.add(new MenuObject("Resistance", R.drawable.ic_resistance));
        menuObjectList.add(new MenuObject("Sandwich", R.drawable.ic_sandwich));
        menuObjectList.add(new MenuObject("Glace", R.drawable.ic_glass_icon));
        menuObjectList.add(new MenuObject("Jus et Fruit", R.drawable.ic_fruit_and_drink));

        initRecyclerView(menuObjectList, root);
        return root;
    }

    private void initRecyclerView(List<MenuObject> menuObjectList, View root) {

        menuAdapter = new MenuAdapter(menuObjectList, getContext());
        recyclerViewMenu = (RecyclerView) root.findViewById(R.id.recycler_view_menu);
        recyclerViewMenu.setHasFixedSize(false);

        // get the reference of RecyclerView
        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a StaggeredGridLayoutManager with 3 number of columns and vertical orientation
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerViewMenu.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerViewMenu.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerViewMenu.setAdapter(menuAdapter);

    }
}