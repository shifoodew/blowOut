package com.blowout.blowout.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.blowout.blowout.EstablishmentData;
import com.blowout.blowout.MyAdapter.EstablishmentAdapter;
import com.blowout.blowout.MyAdapter.EstablishmentProductAdapter;
import com.blowout.blowout.MySingleton;
import com.blowout.blowout.ProductData;
import com.blowout.blowout.R;
import com.blowout.blowout.app.AppConfig;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EstablishmentProductFragment extends DialogFragment {

    final String TAG= "EstabProductFragment";

    private ProgressDialog pDialog;

    RecyclerView rvItem;
    CardView cvItem;

    public EstablishmentProductFragment() {
        // Required empty public constructor
    }

    EstablishmentProductAdapter.ClickListener listener= new EstablishmentProductAdapter.ClickListener() {
        @Override
        public void onItemClicked(ProductData productData) {

            Log.d("onItemClicked", "estab id: "+productData.estab_id );
            Log.d("onItemClicked", "product id: "+productData.product_id );
            Log.d("onItemClicked", "product type id: "+productData.product_type_id );
            Log.d("onItemClicked", "product name: "+productData.product_name);
            Log.d("onItemClicked", "product type name: "+productData.product_type_name );
            Log.d("onItemClicked", "description: "+productData.description );
            Log.d("onItemClicked", "image: "+productData.image );
            Log.d("onItemClicked", "price: "+productData.price );

            Toast.makeText(getContext(), "Product name: "+productData.product_name, Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Progress dialog
        pDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        //Fetching data from EstablishmentFragment.java
        String estab_id = getArguments().getString("estab_id");

        //passing data to getEstabProduct method
        getEstabProduct(estab_id);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_establishment_product, container, false);

        rvItem = rootView.findViewById(R.id.rv_recycler_view_estab_product); //fragment_establishment.xml-> rvItem
        rvItem.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvItem.setLayoutManager(llm);

        return rootView;
    }

    private void getEstabProduct(final String estab_id){

        String tag_string_req = "req_product";

        pDialog.setMessage("Retrieving product list ...");
        showDialog();

        StringRequest stringRequest= new StringRequest(Request.Method.POST,  AppConfig.URL_PRODUCT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        hideDialog();

                        ArrayList<ProductData> productData = new JsonConverter<ProductData>()
                                .toArrayList(response, ProductData.class);

                        if(!productData.isEmpty()){

                            EstablishmentProductAdapter adapter= new EstablishmentProductAdapter(getContext(), productData, listener);

                            rvItem.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                        }else{

                            Toast.makeText(getContext(),"Product list empty", Toast.LENGTH_SHORT).show();

                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Log.d(TAG, error.getMessage());
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT);
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("estab_id", estab_id);

                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest, tag_string_req);
//        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
