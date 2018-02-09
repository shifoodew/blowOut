package com.blowout.blowout.MyAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blowout.blowout.EstablishmentData;
import com.blowout.blowout.ProductData;
import com.blowout.blowout.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shifoodew on 2/4/2018.
 */

public class EstablishmentProductAdapter extends RecyclerView.Adapter<EstablishmentProductAdapter.EstablishmentProductViewHolder> {

    private Context context;
    private ArrayList<ProductData> productList;
    private EstablishmentProductAdapter.ClickListener mListener;


    public interface ClickListener{
        void onItemClicked(ProductData productData);
    }

    //This is the ViewHolder class
    public static class EstablishmentProductViewHolder extends RecyclerView.ViewHolder{

        public CheckBox mCheckBox;
        public CardView cvItem;
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;

        public EstablishmentProductViewHolder(View itemView) {
            super(itemView);

            mCheckBox         = itemView.findViewById(R.id.checkboxSelect);
            cvItem            = itemView.findViewById(R.id.productItem);
            productImage      = itemView.findViewById(R.id.productImage);
            productName       = itemView.findViewById(R.id.productName);
            productPrice      = itemView.findViewById(R.id.productPrice);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EstablishmentProductAdapter(Context context, ArrayList<ProductData> productList, EstablishmentProductAdapter.ClickListener listener){
        this.context= context;
        this.productList= productList;
        this.mListener= listener;
    }

    @Override
    public EstablishmentProductAdapter.EstablishmentProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.from(parent.getContext()).inflate(R.layout.card_product, parent, false);

        EstablishmentProductAdapter.EstablishmentProductViewHolder productHolder = new EstablishmentProductAdapter.EstablishmentProductViewHolder(view);

        return productHolder;
    }

    @Override
    public void onBindViewHolder(final EstablishmentProductAdapter.EstablishmentProductViewHolder holder, final int position) {

        final ProductData productData= productList.get(position);

        String image_url= "http://192.168.43.150/blowOut/" + productData.image;

        Picasso.with(context)
                .load(image_url)
                .placeholder(R.drawable.blowout)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.productImage);

        holder.productName.setText(productData.product_name);
        holder.productName.setTextColor(Color.rgb(66,139,202));

        holder.productPrice.setText(productData.price);
        holder.productPrice.setTextColor(Color.rgb(66,139,202));

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View view){

                ProductData productData= productList.get(position);

                String productName= productList.get(position).product_name;

                Log.d("Estab Adapter","Product name: "    +productData.product_name);
                Log.d("Estab Adapter","Product price: "   +productData.price);
                Log.d("Estab Adapter","Product name: "    +productName);

                Toast.makeText(view.getContext(), "Establishment name: "+productName, Toast.LENGTH_SHORT);

                mListener.onItemClicked(productData);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(productList != null){
            return productList.size();
        }
        return 0;
    }
}
