package com.blowout.blowout.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.blowout.blowout.MyAdapter.EstablishmentAdapter;
import com.blowout.blowout.MySingleton;
import com.blowout.blowout.R;
import com.blowout.blowout.app.AppConfig;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EstablishmentProfileFragment extends Fragment {

    final String TAG= "Estab Profile Frag";
    private ProgressDialog pDialog;

    private Button viewProducts;
    private Button sendMessage;
    private Button sendTextMessage;

    RecyclerView rvEstabProfile;

    public EstablishmentProfileFragment() {
        // Required empty public constructor
    }

    EstabProfileAdapter.ClickListener listener= new EstabProfileAdapter.ClickListener() {
        @Override
        public void onItemClicked(EstablishmentData establishmentData) {

            Log.d("onItemClicked", "id: "+establishmentData.id );
            Log.d("onItemClicked", "Name: "+establishmentData.name );
            Log.d("onItemClicked", "Type: "+establishmentData.type );
            Log.d("onItemClicked", "Address: "+establishmentData.address );

            Toast.makeText(getContext(), "Establishment name: "+establishmentData.name, Toast.LENGTH_SHORT).show();

            //Passing the data to another activity or fragment
            Bundle bundle=new Bundle();
            bundle.putString("estab_id", establishmentData.id);
            bundle.putString("name", establishmentData.name);
            bundle.putString("type", establishmentData.type);
            bundle.putString("address", establishmentData.address);
            bundle.putString("image", establishmentData.image);
            bundle.putString("description", establishmentData.description);
            bundle.putString("email", establishmentData.email);
            bundle.putString("owner", establishmentData.owner);
            bundle.putString("dti_permit", establishmentData.dti_permit);
            bundle.putString("phone", establishmentData.phone);

            //Change to another fragment EstablishmentProductFragment
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            EstablishmentProductFragment estab_product= new EstablishmentProductFragment();

            estab_product.setArguments(bundle);

            ft.replace(R.id.content_main_relativelayout_for_fragment, estab_product);//will be replace fragment_establishment_product.xml
            ft.addToBackStack(null);
            ft.commit();
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Progress dialog
        pDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        //Fetching data from EstablishmentFragment.java
        String estab_id         = getArguments().getString("estab_id");
        String estab_name       = getArguments().getString("name");
        String estab_type       = getArguments().getString("type");
        String estab_address    = getArguments().getString("address");
        String estab_image      = getArguments().getString("image");
        String estab_desc       = getArguments().getString("description");
        String estab_email      = getArguments().getString("email");
        String estab_permit     = getArguments().getString("dti_permit");
        String estab_phone      = getArguments().getString("phone");

        //passing data to getEstabProduct method
        getEstabDetails(estab_id);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_establishment_profile, container, false);

        rvEstabProfile = rootView.findViewById(R.id.rv_recycler_view_estab_profile); //can be found in fragment_establishment_profile
        rvEstabProfile.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvEstabProfile.setLayoutManager(llm);

        return rootView;
    }

    private void getEstabDetails(final String estab_id){

        String tag_string_req = "req_product";

        pDialog.setMessage("Retrieving product list ...");
        showDialog();

        StringRequest stringRequest= new StringRequest(Request.Method.POST,  AppConfig.URL_ESTAB_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        hideDialog();

                        ArrayList<EstablishmentData> establishmentData = new JsonConverter<EstablishmentData>()
                                .toArrayList(response, EstablishmentData.class);

                        if(establishmentData != null){

                            EstabProfileAdapter adapter= new EstabProfileAdapter(getContext(), establishmentData, listener);

                            rvEstabProfile.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                        }
                        else{

                            Toast.makeText(getContext(),"Establishment list empty", Toast.LENGTH_SHORT).show();

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

