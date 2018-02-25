package com.blowout.blowout.Fragment;


import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.blowout.blowout.EstablishmentData;
import com.blowout.blowout.MyAdapter.EstablishmentAdapter;
import com.blowout.blowout.MySingleton;
import com.blowout.blowout.R;
import com.blowout.blowout.app.AppConfig;
import com.blowout.blowout.app.AppController;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EstablishmentFragment extends Fragment {

    final String TAG= "EstablishmentFragment";

    private ProgressDialog pDialog;

    RecyclerView rvItem;
    CardView cvItem;

    public EstablishmentFragment() {
        // Required empty public constructor
    }

    EstablishmentAdapter.ClickListener listener= new EstablishmentAdapter.ClickListener() {
        @Override
        public void onItemClicked(EstablishmentData establishmentData) {

            Log.d("onItemClicked", "id: "       +establishmentData.id );
            Log.d("onItemClicked", "Name: "     +establishmentData.name );
            Log.d("onItemClicked", "Type: "     +establishmentData.type );
            Log.d("onItemClicked", "Address: "  +establishmentData.address );

            Toast.makeText(getContext(), "Click to view products", Toast.LENGTH_SHORT).show();

            //Passing the data to another activity or fragment EstablishmentProfileFragment
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

            //Change to another fragment EstabProfle
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            EstablishmentProfileFragment estab_profile= new EstablishmentProfileFragment();
            estab_profile.setArguments(bundle);
            ft.replace(R.id.content_main_relativelayout_for_fragment, estab_profile);//will be replace fragment_establishment_product.xml
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

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_establishment, container, false);

        rvItem= rootView.findViewById(R.id.rv_recycler_view_fragment_accounts); //fragment_establishment.xml-> rvItem
        rvItem.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvItem.setLayoutManager(llm);

        String tag_string_req = "req_data";

        pDialog.setMessage("Retrieving establishment ...");
        showDialog();

        StringRequest stringRequest= new StringRequest(Request.Method.GET,  AppConfig.URL_ESTABLISHMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        hideDialog();

                        ArrayList<EstablishmentData> establishmentData = new JsonConverter<EstablishmentData>()
                                .toArrayList(response, EstablishmentData.class);

                        if(establishmentData != null){

                            EstablishmentAdapter adapter= new EstablishmentAdapter(getContext(), establishmentData, listener);

                            rvItem.setAdapter(adapter);

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
        );
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest, tag_string_req);
//        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        return rootView;
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
