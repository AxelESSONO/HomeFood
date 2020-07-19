package com.obiangetfils.homefood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.controller.FoodActivity;
import com.obiangetfils.homefood.model.MenuObject;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private String CATEGORY_IMAGE = "CATEGORY_IMAGE";
    private List<MenuObject> menuObjectList;
    private Context context;
    private LayoutInflater mInflater;

    public MenuAdapter(List<MenuObject> menuObjectList, Context context) {
        this.menuObjectList = menuObjectList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.menu_item, parent, false);
        return new MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, final int position) {

        String nameMenu = menuObjectList.get(position).getMenuName();
        holder.menuName.setText(nameMenu);
        holder.menuImage.setImageResource(menuObjectList.get(position).getMenuImage());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent foodIntent = new Intent(context, FoodActivity.class);
                foodIntent.putExtra(CATEGORY_NAME, menuObjectList.get(position).getMenuName());
                foodIntent.putExtra(CATEGORY_IMAGE, menuObjectList.get(position).getMenuImage());
                context.startActivity(foodIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuObjectList.size();
    }

    public class MenuHolder extends RecyclerView.ViewHolder {

        private ImageView menuImage;
        private TextView menuName;
        private RelativeLayout relativeLayout;

        public MenuHolder(@NonNull View itemView) {
            super(itemView);

            menuName = (TextView) itemView.findViewById(R.id.menu_name);
            menuImage = (ImageView) itemView.findViewById(R.id.menu_image);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_item);
        }
    }
}
