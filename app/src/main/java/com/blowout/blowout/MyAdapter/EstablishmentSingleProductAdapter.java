package com.blowout.blowout.MyAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.blowout.blowout.ProductData;
import com.blowout.blowout.R;
import com.blowout.blowout.app.AppConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shifoodew on 2/19/2018.
 */

public class EstablishmentSingleProductAdapter extends RecyclerView.Adapter<EstablishmentSingleProductAdapter.EstablishmentSingleProductViewHolder> {


    private Context context;
    private ArrayList<ProductData> product;
    private EstablishmentSingleProductAdapter.ClickListener mListener;


    public interface ClickListener{
        void onItemClicked(ProductData productData);
    }

    //This is the ViewHolder class
    public static class EstablishmentSingleProductViewHolder extends RecyclerView.ViewHolder{

        public CardView cvItem;
        public ImageView productImage;

        public TextView productName;
        public TextView productDesc;
        public TextView productPrice;
        public EditText productQty;
        public TextView productTotaPrice;
        public Button addCart;

        public EstablishmentSingleProductViewHolder(View itemView) {
            super(itemView);

            //card_single_product.xml
            cvItem            = itemView.findViewById(R.id.cv_single_product);
            productImage      = itemView.findViewById(R.id.product_image);
            productName       = itemView.findViewById(R.id.product_name);
            productDesc       = itemView.findViewById(R.id.product_description);
            productPrice      = itemView.findViewById(R.id.product_price);
            productQty        = itemView.findViewById(R.id.product_qty);
//            productTotaPrice  = itemView.findViewById(R.id.product_total_price);
            addCart           = itemView.findViewById(R.id.submitProduct);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EstablishmentSingleProductAdapter(Context context, ArrayList<ProductData> product, EstablishmentSingleProductAdapter.ClickListener listener){
        this.context= context;
        this.product= product;
        this.mListener= listener;
    }

    @Override
    public EstablishmentSingleProductAdapter.EstablishmentSingleProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.from(parent.getContext()).inflate(R.layout.card_single_product, parent, false);

        EstablishmentSingleProductAdapter.EstablishmentSingleProductViewHolder singleProductHolder = new EstablishmentSingleProductAdapter.EstablishmentSingleProductViewHolder(view);

        return singleProductHolder;
    }

    @Override
    public void onBindViewHolder(EstablishmentSingleProductAdapter.EstablishmentSingleProductViewHolder holder, final int position) {

        final ProductData productData= product.get(position);

//        String image_url= AppConfig.IMAGE_URL + productData.image;
        String image_url= "https://img00.deviantart.net/c164/i/2017/120/6/f/jollibee__the_fast_food_family__by_pastelaine_art-db7lx2j.png";

        Picasso.with(context)
                .load(image_url)
                .placeholder(R.drawable.blowout)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.productImage);

        holder.productName.setText(productData.product_name);
        holder.productName.setTextColor(Color.rgb(66,139,202));

        holder.productDesc.setText(productData.description);

        holder.productPrice.setText(productData.price);
        holder.productPrice.setTextColor(Color.rgb(66,139,202));

        //Button addToCart
        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(),
                        "something went wrong", Toast.LENGTH_LONG)
                        .show();

            }
        });

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View view){

                ProductData productData= product.get(position);

                String productName= product.get(position).product_name;

                Log.d("SingleProdAdapter","Product name: "              +productData.product_name);

//                Toast.makeText(view.getContext(), "Establishment name: "    +productName, Toast.LENGTH_SHORT);

                mListener.onItemClicked(productData);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(product != null){
            return product.size();
        }
        return 0;
    }
}
