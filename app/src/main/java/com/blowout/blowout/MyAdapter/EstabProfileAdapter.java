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
import com.blowout.blowout.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shifoodew on 2/9/2018.
 */

public class EstabProfileAdapter extends RecyclerView.Adapter<EstabProfileAdapter.EstablishmentProductViewHolder> {

    private Context context;
    private ArrayList<EstablishmentData> estabProflieData;
    private EstabProfileAdapter.ClickListener mListener;


    public interface ClickListener{
        void onItemClicked(EstablishmentData estabProfile);
    }

    //This is the ViewHolder class
    public static class EstablishmentProductViewHolder extends RecyclerView.ViewHolder{


        public CardView cvItem;
        public ImageView estabLogo;
        public TextView estabName;
        public TextView estabEmail;
        public TextView estabAddress;
        public TextView estabDescription;

        public EstablishmentProductViewHolder(View itemView) {
            super(itemView);

            cvItem              = itemView.findViewById(R.id.cvEstabProfile);
            estabLogo           = itemView.findViewById(R.id.estabLogo);
            estabName           = itemView.findViewById(R.id.estabName);
            estabEmail          = itemView.findViewById(R.id.estabEmail);
            estabAddress        = itemView.findViewById(R.id.estabAddress);
            estabDescription    = itemView.findViewById(R.id.estabDescription);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EstabProfileAdapter(Context context, ArrayList<EstablishmentData> estabProflieData, EstabProfileAdapter.ClickListener listener){
        this.context= context;
        this.estabProflieData= estabProflieData;
        this.mListener= listener;
    }

    @Override
    public EstabProfileAdapter.EstablishmentProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.from(parent.getContext()).inflate(R.layout.card_estab_profile, parent, false);

        EstabProfileAdapter.EstablishmentProductViewHolder productHolder = new EstabProfileAdapter.EstablishmentProductViewHolder(view);

        return productHolder;
    }

    @Override
    public void onBindViewHolder(final EstabProfileAdapter.EstablishmentProductViewHolder holder, final int position) {

        final EstablishmentData estabProfile= estabProflieData.get(position);

//        String image_url= "http://192.168.43.150/blowOut/" + estabProfile.image;
        String image_url= "https://img00.deviantart.net/c164/i/2017/120/6/f/jollibee__the_fast_food_family__by_pastelaine_art-db7lx2j.png";

        Picasso.with(context)
                .load(image_url)
                .placeholder(R.drawable.blowout)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.estabLogo);

        holder.estabName.setText(estabProfile.name);
        holder.estabName.setTextColor(Color.rgb(66,139,202));

        holder.estabEmail.setText("email: "+estabProfile.email);
        holder.estabEmail.setTextColor(Color.rgb(66,139,202));

        holder.estabAddress.setText("address: "+estabProfile.address);
        holder.estabAddress.setTextColor(Color.rgb(66,139,202));

        holder.estabDescription.setText(estabProfile.description);
//        holder.estabDescription.setText("Wala lng gud");

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View view){

                EstablishmentData estabProfile= estabProflieData.get(position);

                String productName= estabProflieData.get(position).name;

                Log.d("Estab Adapter","Product name: "    +estabProfile.name);
                Log.d("Estab Adapter","Product price: "   +estabProfile.email);

                Toast.makeText(view.getContext(), "Establishment name: "+productName, Toast.LENGTH_SHORT);

                mListener.onItemClicked(estabProfile);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(estabProflieData != null){
            return estabProflieData.size();
        }
        return 0;
    }
}
