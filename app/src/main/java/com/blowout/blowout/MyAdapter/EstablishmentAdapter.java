package com.blowout.blowout.MyAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blowout.blowout.EstablishmentData;
import com.blowout.blowout.R;
import com.blowout.blowout.app.AppConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shifoodew on 1/19/2018.
 */

public class EstablishmentAdapter extends RecyclerView.Adapter<EstablishmentAdapter.EstablishementViewHolder>{

    private Context context;
    private ArrayList<EstablishmentData> establishmentList;
    private ClickListener mListener;


    public interface ClickListener{
        void onItemClicked(EstablishmentData estabData);
    }

    //This is the ViewHolder class
    public static class EstablishementViewHolder extends RecyclerView.ViewHolder{

        public CardView cvItem;
        public ImageView estabImage;
        public TextView estabName;
        public TextView estabAddress;

        public EstablishementViewHolder(View itemView) {
            super(itemView);

            cvItem          = itemView.findViewById(R.id.cvItem);
            estabImage      = itemView.findViewById(R.id.estabImage);
            estabName       = itemView.findViewById(R.id.estabName);
            estabAddress    = itemView.findViewById(R.id.estabAddress);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EstablishmentAdapter(Context context, ArrayList<EstablishmentData> establishmentList, ClickListener listener){
        this.context= context;
        this.establishmentList= establishmentList;
        this.mListener= listener;
    }

    @Override
    public EstablishementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater= LayoutInflater.from(parent.getContext());
            View view= inflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

            EstablishementViewHolder estabHolder = new EstablishementViewHolder(view);

            return estabHolder;
    }

    @Override
    public void onBindViewHolder(final EstablishementViewHolder holder, final int position) {

        EstablishmentData establishmentData= establishmentList.get(position);
//        String image_url= "http://192.168.43.150/blowOut/" + establishmentData.image;
        String image_url= AppConfig.IMAGE_URL + establishmentData.image;

        Picasso.with(context)
                .load(image_url)
                .placeholder(R.drawable.blowout)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.estabImage);

        holder.estabName.setText(establishmentData.name);
        holder.estabName.setTextColor(Color.rgb(66,139,202));

        holder.estabAddress.setText(establishmentData.address);
        holder.estabAddress.setTextColor(Color.rgb(66,139,202));

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View view){

                EstablishmentData establishmentData= establishmentList.get(position);

                String estab_name= establishmentList.get(position).name;

                Log.d("Estab Adapter","Establishment name: "    +establishmentData.name);
                Log.d("Estab Adapter","Establishment address: " +establishmentData.address);
                Log.d("Estab Adapter","Establishment name: "    +estab_name);

                Toast.makeText(view.getContext(), "Click to view products: "+estab_name, Toast.LENGTH_SHORT);

                mListener.onItemClicked(establishmentData);
            }
        });
    }

    @Override
    public int getItemCount() {

        if(establishmentList != null){
            return establishmentList.size();
        }
        return 0;
    }
}
