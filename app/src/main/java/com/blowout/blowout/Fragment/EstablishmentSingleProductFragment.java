package com.blowout.blowout.Fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.blowout.blowout.EstablishmentData;
import com.blowout.blowout.MyAdapter.EstabProfileAdapter;
import com.blowout.blowout.MyAdapter.EstablishmentProductAdapter;
import com.blowout.blowout.MyAdapter.EstablishmentSingleProductAdapter;
import com.blowout.blowout.MySingleton;
import com.blowout.blowout.ProductData;
import com.blowout.blowout.R;
import com.blowout.blowout.app.AppConfig;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EstablishmentSingleProductFragment extends Fragment {

    final String TAG= "EstablishmentSingleProductFragment";

    private ProgressDialog pDialog;

    RecyclerView rvProduct;
    CardView cvProduct;

    public EstablishmentSingleProductFragment() {
        // Required empty public constructor
    }

    EstablishmentSingleProductAdapter.ClickListener listener= new EstablishmentSingleProductAdapter.ClickListener() {
        @Override
        public void onItemClicked(ProductData productData) {

            Log.d("onItemClicked", "id: "       +productData.estab_id );
            Log.d("onItemClicked", "Name: "     +productData.product_name );

            Toast.makeText(getContext(), "Establishment name: "+productData.product_name, Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Progress dialog
        pDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        //Fetching data from EstablishmentProductFragment.java
        String estab_id = getArguments().getString("estab_id");
        String product_id = getArguments().getString("product_id");

        //passing data to getEstabProduct method
        getEstabProduct(estab_id, product_id);

        View rootView = inflater.inflate(R.layout.fragment_establishment_single_product, container, false);

        rvProduct = rootView.findViewById(R.id.rv_recycler_view_single_product); //can be found in fragment_establishment_single_product
        rvProduct.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvProduct.setLayoutManager(llm);

        return rootView;
    }

    private void getEstabProduct(final String estab_id, final String product_id){

        String tag_string_req = "req_product";

        pDialog.setMessage("Retrieving product list ...");
        showDialog();

        StringRequest stringRequest= new StringRequest(Request.Method.POST,  AppConfig.URL_PRODUCT_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        hideDialog();

                        ArrayList<ProductData> productData = new JsonConverter<ProductData>()
                                .toArrayList(response, ProductData.class);

                        Log.d("Check data"," "+productData);

                        if(productData != null){

                            EstablishmentSingleProductAdapter adapter= new EstablishmentSingleProductAdapter(getContext(), productData, listener);

                            rvProduct.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                        }else{

                            Toast.makeText(getContext(),"Product is empty", Toast.LENGTH_SHORT).show();

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
                params.put("product_id", product_id);

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
