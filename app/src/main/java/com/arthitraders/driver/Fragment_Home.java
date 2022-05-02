package com.arthitraders.driver;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_Home extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView delivery_recycler;
    DeliveryAdapter deliveryAdapter;
    List<HomeModel> dr;
    SwipeRefreshLayout swipelayout;
    View emptyLayout;
    String dol_vehicle_no;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Helper.sharedpreferencesdriver = getActivity().getSharedPreferences(Helper.MyPREFERENCES, Context.MODE_PRIVATE);
        dol_vehicle_no                = Helper.sharedpreferencesdriver.getString("dol_vehicle_no","");

        swipelayout=view.findViewById(R.id.swipelayout);
        emptyLayout=view.findViewById(R.id.emptyLayout);
        swipelayout.setOnRefreshListener(this);
        swipelayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        delivery_recycler=view.findViewById(R.id.delivery_recycler);
        delivery_recycler.setHasFixedSize(true);
        delivery_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        dr=new ArrayList<>();

        getOrderHistory();
        deliveryAdapter=new DeliveryAdapter(getActivity(),dr);
        delivery_recycler.setAdapter(deliveryAdapter);
        return view;
    }

    @Override
    public void onRefresh() {
        getOrderHistory();
    }

    public void getOrderHistory() {
        Helper.loading(getActivity());
        swipelayout.setRefreshing(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Helper.Shippinglist_url,
                response -> {
                    Helper.mProgressBarHandlerdriver.hide();
                    Log.e("orderhistory", response);
                    try {
                        JSONObject job = new JSONObject(response);
                        JSONArray jarr = job.getJSONArray("data");
                        if(jarr.length()!=0) {
                            List<HomeModel> items = new ArrayList<>();
                            for (int v = 0; v < jarr.length(); v++) {
                                JSONObject obj = jarr.getJSONObject(v);
                                if(obj.getString("message").equals("failed")){
                                    delivery_recycler.setVisibility(View.GONE);
                                    emptyLayout.setVisibility(View.VISIBLE);
                                }
                                else {
                                    String dol_order_id = obj.getString("dol_order_id");
                                    String dol_order_user_id = obj.getString("dol_order_user_id");
                                    String dol_order_product_code = obj.getString("dol_order_product_code");
                                    String dol_order_product_qty = obj.getString("dol_order_product_qty");
                                    String dol_order_product_price = obj.getString("dol_order_product_price");
                                    String dol_order_total = obj.getString("dol_order_total");
                                    String dol_order_sum = obj.getString("dol_order_sum");
                                    String dol_order_shipping = obj.getString("dol_order_shipping");
                                    String dol_order_status = obj.getString("dol_order_status");
                                    String dol_order_created_at = obj.getString("dol_order_created_at");
                                    String dol_order_updated_at = obj.getString("dol_order_updated_at");
                                    String dol_category = obj.getString("dol_category");
                                    String dol_sub_category = obj.getString("dol_sub_category");
                                    String dol_product_name = obj.getString("dol_product_name");
                                    String dol_product_description = obj.getString("dol_product_description");
                                    String dol_product_pack = obj.getString("dol_product_pack");
                                    String dol_product_weight = obj.getString("dol_product_weight");
                                    String dol_product_image = obj.getString("dol_product_image");


                                    HomeModel orderHistory = new HomeModel();
                                    orderHistory.setOrderId(dol_order_id);
                                    orderHistory.setUserid(dol_order_user_id);
                                    orderHistory.setProductordercode(dol_order_product_code);
                                    orderHistory.setProductorderquantity(dol_order_product_qty);
                                    orderHistory.setProductorderprice(dol_order_product_price);
                                    orderHistory.setProductordertotal(dol_order_total);
                                    orderHistory.setProductordersum(dol_order_sum);
                                    orderHistory.setProductordershipping(dol_order_shipping);
                                    orderHistory.setOrderstatus(dol_order_status);
                                    orderHistory.setProductordercreatedate(dol_order_created_at);
                                    orderHistory.setProductorderupdatedate(dol_order_updated_at);
                                    orderHistory.setProductordercategory(dol_category);
                                    orderHistory.setProductordersubcategory(dol_sub_category);
                                    orderHistory.setProductordername(dol_product_name);
                                    orderHistory.setProductorderdesc(dol_product_description);
                                    orderHistory.setProductorderpack(dol_product_pack);
                                    orderHistory.setProductorderweight(dol_product_weight);
                                    orderHistory.setProductorderimage(dol_product_image);


                                    items.add(orderHistory);
                                }
                            }
                            if (items.size() != 0) {
                                Log.e("it--- ",items.size()+".");
                                List<HomeModel> allEvents = items;
                                List<HomeModel> noRepeat = new ArrayList<>();

                                for (HomeModel event : allEvents) {
                                    boolean isFound = false;
                                    // check if the event name exists in noRepeat
                                    for (HomeModel e : noRepeat) {
                                        if (e.getOrderId().equals(event.getOrderId()) || (e.equals(event))) {
                                            isFound = true;
                                            break;
                                        }
                                    }
                                    if (!isFound) noRepeat.add(event);
                                }
                                items = noRepeat;
                                deliveryAdapter = new DeliveryAdapter(getActivity(), items);
                                delivery_recycler.setAdapter(deliveryAdapter);
                                deliveryAdapter.notifyDataSetChanged();
                            } else {
                                delivery_recycler.setVisibility(View.GONE);
                                emptyLayout.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            delivery_recycler.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    try {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Helper.mProgressBarHandlerdriver.hide();
                        Log.e("err", error.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("vehicle_no", dol_vehicle_no);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


}
