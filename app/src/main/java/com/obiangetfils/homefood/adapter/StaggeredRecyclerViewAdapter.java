package com.obiangetfils.homefood.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.controller.DishDetailActivity;
import com.obiangetfils.homefood.model.DishItem;

import java.util.List;

public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "StaggeredRecyclerViewAd";

    private List<DishItem> dishItemList;
    private Context mContext;

    public StaggeredRecyclerViewAdapter(Context context, List<DishItem> dishItemList) {

        this.mContext = context;
        this.dishItemList = dishItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_dish_example);

        Glide.with(mContext)
                .load(dishItemList.get(position).getDishUri())
                .apply(requestOptions)
                .into(holder.image);
        holder.name.setText(dishItemList.get(position).getDishName());
        holder.priceItem.setText(dishItemList.get(position).getDishPrice() + " euros");

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(mContext, DishDetailActivity.class);
                detailIntent.putExtra("DISH_ITEM_LIST", dishItemList.get(position));
                mContext.startActivity(detailIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return dishItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView priceItem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.imageview_widget);
            this.name = itemView.findViewById(R.id.name_widget);
            this.priceItem = itemView.findViewById(R.id.price_item);
        }
    }
}