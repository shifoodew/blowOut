package com.blowout.blowout.MyAdapter;

import android.app.ProgressDialog;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.blowout.blowout.ProductData;
import com.blowout.blowout.R;
import com.blowout.blowout.app.AppConfig;
import com.blowout.blowout.app.AppController;
import com.blowout.blowout.helper.SQLiteHandler;
import com.blowout.blowout.helper.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by shifoodew on 2/19/2018.
 */

public class EstablishmentSingleProductAdapter extends RecyclerView.Adapter<EstablishmentSingleProductAdapter.EstablishmentSingleProductViewHolder> {

    private SQLiteHandler db;
    private Context context;
    private ArrayList<ProductData> product;
    private EstablishmentSingleProductAdapter.ClickListener mListener;

    //added
    private String[] mDataset;

    public EstablishmentSingleProductAdapter(String[] myDataset) {
        mDataset = myDataset;
    }
    //end of added

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


        private ProgressDialog pDialog;
        private SessionManager session;

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

//        final String qty= holder.productQty.getText().toString();

        final String qty= "10";

        //Button addToCart
        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // SqLite database handler
                db = new SQLiteHandler(context);

                //Fetching user details from SQLite
                HashMap<String, String> dbData = db.getUserDetails();
                ProductData productData= product.get(position);

                String item_id= productData.product_id;
                String item_type= productData.product_type_id;
                String user= dbData.get("u_id");

                addToCart(item_id, item_type, qty, user);

                Toast.makeText(v.getContext(),
                        "Qty: " +qty, Toast.LENGTH_LONG)
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
    private void addToCart(final String item_id, final String item_type, final String qty, final String user){
        // Tag used to cancel the request
        String tag_string_req = "submit";

        StringRequest stringRequest= new StringRequest(Request.Method.POST, AppConfig.URL_ADD_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Cart", "Cart Response: " + response.toString());
                        try{
                            JSONObject jObj = new JSONObject(response.toString());
                            Log.d("AddToCart", "Checking JSON Object" +jObj);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                Log.d(TAG, "Product was successfully added to user's cart");

                                JSONObject user = jObj.getJSONObject("add");
                                    Log.d("Cart", "User cart Object         : " + user.toString());
                                String message = user.getString("message");
                                    Log.d(TAG, "Message                         : " + message);

                                //Message for new registered member
                                Toast.makeText(context, " "+message , Toast.LENGTH_LONG).show();
                            } else {
                                // Error occurred in adding cart. Get the error message
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(context,
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e){
                            // JSON error
                            e.printStackTrace();
                            Log.d("AddingCart", "" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Cart Error: " + "Product was not added to user's cart " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", user);
                params.put("item_id", item_id);
                params.put("item_type", item_type);
                params.put("quantity", String.valueOf(qty));

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    @Override
    public int getItemCount() {
        if(product != null){
            return product.size();
        }
        return 0;
    }
}
