package com.blowout.blowout.MyAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blowout.blowout.EstablishmentData;
import com.blowout.blowout.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shifoodew on 2/22/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder> {

    private Context context;
    private ArrayList<CartData> cart;
    private CartAdapter.ClickListener mListener;

    public interface ClickListener{
        void onItemClicked(CartData cartData);
    }

    //This is the ViewHolder class
    public static class CartAdapterViewHolder extends RecyclerView.ViewHolder{

        public CardView cvCartItem;

        public ImageView cartProductImage;
        public TextView cartProductName;
        public TextView cartProductPrice;
        public TextView cartProductqty;
        public TextView cartTotalPrice;

        public CartAdapterViewHolder(View itemView) {
            super(itemView);

            //card_single_product.xml
            cvCartItem              = itemView.findViewById(R.id.cvCartItem);
            cartProductImage        = itemView.findViewById(R.id.cartProductImage);
            cartProductName         = itemView.findViewById(R.id.cartProductName);
            cartProductPrice        = itemView.findViewById(R.id.cartProductPrice);
            cartProductqty          = itemView.findViewById(R.id.cartProductqty);
            cartTotalPrice          = itemView.findViewById(R.id.cartTotalPrice);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CartAdapter(Context context, ArrayList<CartData> cart, CartAdapter.ClickListener listener){
        this.context= context;
        this.cart= cart;
        this.mListener= listener;
    }

    @Override
    public CartAdapter.CartAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.from(parent.getContext()).inflate(R.layout.cart_card, parent, false);

        CartAdapter.CartAdapterViewHolder singleProductHolder = new CartAdapter.CartAdapterViewHolder(view);

        return singleProductHolder;
    }

    @Override
    public void onBindViewHolder(CartAdapter.CartAdapterViewHolder holder, final int position) {

        final CartData cartData= cart.get(position);
        //        String image_url= AppConfig.IMAGE_URL + productData.image;
        String image_url= "https://img00.deviantart.net/c164/i/2017/120/6/f/jollibee__the_fast_food_family__by_pastelaine_art-db7lx2j.png";

        Picasso.with(context)
                .load(image_url)
                .placeholder(R.drawable.blowout)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.cartProductImage);

        holder.cartProductName.setText(cartData.cart_item_name);
        holder.cartProductName.setTextColor(Color.rgb(66,139,202));

//        holder.cartProductPrice.setText(cartData.description);

        holder.cartProductqty.setText(cartData.cart_quantity);
        holder.cartProductqty.setTextColor(Color.rgb(66,139,202));

        holder.cartTotalPrice.setText(cartData.cart_item_price);
        holder.cartTotalPrice.setTextColor(Color.rgb(66,139,202));

        holder.cvCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View view){

                CartData cartData= cart.get(position);


                Log.d("Estab Adapter","Cart id: "    +cartData.cart_id);
                Log.d("Estab Adapter","Cart item type: " +cartData.cart_item_type);
                Log.d("Estab Adapter","Cart item name: " +cartData.cart_item_name);

                Toast.makeText(view.getContext(), "Click to view data: "+cartData.cart_id, Toast.LENGTH_SHORT);

                mListener.onItemClicked(cartData);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(cart != null){
            return cart.size();
        }
        return 0;
    }
}
