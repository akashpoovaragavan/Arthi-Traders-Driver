package com.arthitraders.driver;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class Fragment_Profile extends Fragment {
    TextInputEditText vehicle_no,vehicle_type;
    String num,type;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        vehicle_no=view.findViewById(R.id.vehicle_no);
        vehicle_type=view.findViewById(R.id.vehicle_type);

        Helper.sharedpreferencesdriver = getActivity().getSharedPreferences(Helper.MyPREFERENCES, Context.MODE_PRIVATE);

        num= Helper.sharedpreferencesdriver.getString("dol_vehicle_no","");
        type= Helper.sharedpreferencesdriver.getString("dol_vehicle_type","");

        vehicle_no.setText(num);
        vehicle_type.setText(type);

        return view;
    }
}
