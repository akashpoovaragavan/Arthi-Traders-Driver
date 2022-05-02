package com.arthitraders.driver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {
    private Activity context;
    private List<HomeModel> dr;

    public DeliveryAdapter(Activity context, List<HomeModel> dr) {
        this.context = context;
        this.dr = dr;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.delivery_code.setText(dr.get(position).getOrderId());
        holder.delivery_date.setText(dr.get(position).getProductordercreatedate());
        holder.delivery_status.setText(dr.get(position).getOrderstatus());
        holder.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    updateOrder(dr.get(position).getOrderId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dr.size();
    }

    public class DeliveryViewHolder extends RecyclerView.ViewHolder {
        TextView delivery_code,delivery_date,delivery_status;
        Button delivered;
        public DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            delivery_code=itemView.findViewById(R.id.delivery_code);
            delivery_date=itemView.findViewById(R.id.delivery_date);
            delivered=itemView.findViewById(R.id.delivered);
            delivery_status=itemView.findViewById(R.id.order_status);
        }
    }


    public void updateOrder(String dol_order_id) {
        try {
            Helper.loading(context);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Helper.Updateorder_url,
                    response -> {
                        Helper.mProgressBarHandlerdriver.hide();
                        Log.e("updateOrder", response);
                        try {

                            JSONArray jarr = new JSONArray(response);
                            JSONObject objj = jarr.getJSONObject(0);
                            if(!objj.getString("Message").equals("Success")) {
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(context,"Successfully Updated",Toast.LENGTH_LONG).show();
                                context.startActivity(new Intent(context, HomeScreen.class));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Helper.mProgressBarHandlerdriver.hide();
                        }

                    },
                    error -> {
                        try {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            Helper.mProgressBarHandlerdriver.hide();
                            Log.e("err", error.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("order_id", dol_order_id);
                    return params;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
