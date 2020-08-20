package com.obiangetfils.homefood.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.model.CartObject;
import com.obiangetfils.homefood.model.DishItem;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    private Context mContext;
    private List<CartObject> cartObjectList;
    private int quantity;
    private int price;
    private int total_price = 0;

    public CartAdapter(Context mContext, List<CartObject> cartObjectList) {
        this.mContext = mContext;
        this.cartObjectList = cartObjectList;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_item, parent, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, final int position) {

        String quantity, dishName, dishPrice;

        quantity = "" + cartObjectList.get(position).getQuantity();
        dishName = cartObjectList.get(position).getDishItem().getDishName();
        dishPrice = cartObjectList.get(position).getDishItem().getDishPrice();
        int cartItemTotalPriceTmp = Integer.parseInt(quantity) * Integer.parseInt(dishPrice);


        Glide.with(mContext)
                .load(cartObjectList.get(position).getDishItem().getDishUri())
                .into(holder.dishImage);
        holder.quantityAndName.setText("" + quantity + " x " + dishName);
        holder.cartItemTotalPrice.setText("" + quantity + " x " + dishPrice + " = " + cartItemTotalPriceTmp);

        holder.editeItemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editOrder(position, cartObjectList);
            }
        });

        holder.removeItemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOrder(position, cartObjectList);
            }
        });

    }

    private void removeOrder(int position, List<CartObject> cartObjectList) {
        String userId, cartKey;
        userId = cartObjectList.get(position).getUserId();
        cartKey = cartObjectList.get(position).getDishItem().getDishKey();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("CartList").child(userId).child(cartKey).removeValue();
    }

    private void editOrder(int position, List<CartObject> cartObjectList) {
        //DishItem data
        String cartKeyCart, cartNameCart, cartDescriptionCart, cartPriceCart, cartCategoryCart, cartUriCart;
        String cartQuantityCart, userIDCart, userNameCart;

        //DishItem data
        cartKeyCart = cartObjectList.get(position).getDishItem().getDishKey();
        cartNameCart = cartObjectList.get(position).getDishItem().getDishName();
        cartDescriptionCart = cartObjectList.get(position).getDishItem().getDishDescription();
        cartPriceCart = cartObjectList.get(position).getDishItem().getDishPrice();
        cartCategoryCart = cartObjectList.get(position).getDishItem().getDishCategory();
        cartUriCart = cartObjectList.get(position).getDishItem().getDishUri();

        cartQuantityCart = cartObjectList.get(position).getQuantity();
        userIDCart = cartObjectList.get(position).getUserId();
        userNameCart = cartObjectList.get(position).getUserName();

        DishItem dish = new DishItem(cartNameCart, cartDescriptionCart, cartPriceCart, cartCategoryCart, cartUriCart, cartKeyCart);
        CartObject cart = new CartObject(dish, cartQuantityCart, userIDCart, userNameCart);

        showPopup(position, dish, cartQuantityCart, userIDCart, userNameCart);

    }

    private void showPopup(int position, final DishItem dish, final String cartQuantityCart, final String userIDCart, String userNameCart) {

        final TextView priceEdit, nameEdit, descriptionEdit, quantityEdit;
        ImageView imageEdit;
        AppCompatImageButton minusEdit, plusEdit;
        Button closeEdit, updateEdit;

        final Dialog myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.edit_order);

        priceEdit = (TextView) myDialog.findViewById(R.id.price_order_edit);
        nameEdit = (TextView) myDialog.findViewById(R.id.dish_name_order_edit);
        descriptionEdit = (TextView) myDialog.findViewById(R.id.dish_description_order_edit);
        quantityEdit = (TextView) myDialog.findViewById(R.id.product_item_quantity_order);
        imageEdit = (ImageView) myDialog.findViewById(R.id.image_order_edit);
        minusEdit = (AppCompatImageButton) myDialog.findViewById(R.id.product_item_quantity_minusBtn_order);
        plusEdit = (AppCompatImageButton) myDialog.findViewById(R.id.product_item_quantity_plusBtn_order);
        closeEdit = (Button) myDialog.findViewById(R.id.closeEdit);
        updateEdit = (Button) myDialog.findViewById(R.id.updateEdit);

        priceEdit.setText("" + (Integer.parseInt(dish.getDishPrice()) * Integer.parseInt(cartQuantityCart)) + " Euros");
        nameEdit.setText(dish.getDishName());
        descriptionEdit.setText(dish.getDishDescription());
        quantityEdit.setText("" + cartQuantityCart);
        Glide.with(mContext).load(dish.getDishUri()).into(imageEdit);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        quantity = Integer.parseInt(cartQuantityCart);
        total_price = Integer.parseInt(dish.getDishPrice());

        myDialog.show();
        minusEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    total_price = total_price - price;
                    quantity--;
                    priceEdit.setText("" + (total_price * quantity) + " Euros");
                    quantityEdit.setText("" + quantity);
                }
            }
        });

        plusEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_price = total_price + price;
                quantity++;
                priceEdit.setText("" + (total_price * quantity) + " Euros");
                quantityEdit.setText("" + quantity);
            }
        });


        closeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        updateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference updatecartReference = FirebaseDatabase.getInstance().getReference();
                updatecartReference.child("CartList")
                        .child(userIDCart)
                        .child(dish.getDishKey())
                        .child("cartQuantity").setValue(quantityEdit.getText().toString());
                myDialog.dismiss();

                Toasty.success(mContext,
                        "Commande mise à jour!!",
                        Toast.LENGTH_SHORT, true).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartObjectList.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {

        ImageView dishImage, editeItemCart, removeItemCart;
        TextView quantityAndName, cartItemTotalPrice;

        public CartHolder(@NonNull View itemView) {
            super(itemView);

            dishImage = (ImageView) itemView.findViewById(R.id.image_cart);
            editeItemCart = (ImageView) itemView.findViewById(R.id.edit_cart);
            removeItemCart = (ImageView) itemView.findViewById(R.id.remove_item);
            quantityAndName = (TextView) itemView.findViewById(R.id.quantity_and_name);
            cartItemTotalPrice = (TextView) itemView.findViewById(R.id.cart_item_total_price);
        }
    }
}
