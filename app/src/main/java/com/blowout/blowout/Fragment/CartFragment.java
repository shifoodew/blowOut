package com.blowout.blowout.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.blowout.blowout.MyAdapter.CartAdapter;
import com.blowout.blowout.MyAdapter.CartData;
import com.blowout.blowout.MySingleton;
import com.blowout.blowout.R;
import com.blowout.blowout.app.AppConfig;
import com.blowout.blowout.helper.SQLiteHandler;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    final String TAG= "CartFragment";

    private SQLiteHandler db;
    private ProgressDialog pDialog;

    RecyclerView rvItem;
    CardView cvItem;

    public CartFragment() {
        // Required empty public constructor
    }

    CartAdapter.ClickListener listener= new CartAdapter.ClickListener() {
        @Override
        public void onItemClicked(CartData cartData) {

            Log.d("onItemClicked", "id: "       +cartData.cart_id);
            Log.d("onItemClicked", "Name: "     +cartData.cart_item_name);
            Log.d("onItemClicked", "Type: "     +cartData.cart_type);

            Toast.makeText(getContext(), "Click to view products "+cartData.cart_id, Toast.LENGTH_SHORT).show();

            //Passing the data to another activity or fragment EstablishmentProfileFragment
//            Bundle bundle=new Bundle();
//            bundle.putString("cartID", cartData.cart_id);
//            bundle.putString("cartUser", cartData.cart_user);
//            bundle.putString("cartItemID", cartData.cart_item_id);
//            bundle.putString("cartItemType", cartData.cart_item_type);
//            bundle.putString("cartQty", cartData.cart_quantity);
//            bundle.putString("cartCreatedAt", cartData.cart_created_at);
//            bundle.putString("cartUpdatedAt", cartData.cart_updated_at);

            //Change to another fragment EstabProfle
//            FragmentManager fm= getFragmentManager();
//            FragmentTransaction ft= fm.beginTransaction();
//            EstablishmentSingleProductFragment singleProductFragment= new EstablishmentSingleProductFragment();
//            singleProductFragment.setArguments(bundle);
//            ft.replace(R.id.content_main_relativelayout_for_fragment, singleProductFragment);//will be replace fragment_establishment_product.xml
//            ft.addToBackStack(null);
//            ft.commit();
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Progress dialog
        pDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        // SqLite database handler
        db = new SQLiteHandler(getContext());

        //Fetching user details from SQLite
        HashMap<String, String> dbData = db.getUserDetails();

        String user= dbData.get("u_id");
        Log.d(TAG, "OnCreateView USER user_id         : " + user);

        //passing data to showCart method
        showCart(user);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        rvItem= rootView.findViewById(R.id.rv_cart); //fragment_cart.xml-> rvItem
        rvItem.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvItem.setLayoutManager(llm);

        return rootView;
    }


    private void showCart(final String user){

        String tag_string_req = "req_data";

        pDialog.setMessage("Retrieving cart ...");
        showDialog();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, AppConfig.URL_SHOW_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Cart json response: " +response);
                        hideDialog();

                        ArrayList<CartData> cartData = new JsonConverter<CartData>()
                                .toArrayList(response, CartData.class);
                        Log.d(TAG, "Cart json response: " +cartData);

                        if(cartData.contains("error") != true){

                            CartAdapter adapter= new CartAdapter(getContext(), cartData, listener);

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
                }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", user);

                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest, tag_string_req);
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
