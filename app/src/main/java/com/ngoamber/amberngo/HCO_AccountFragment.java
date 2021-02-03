package com.ngoamber.amberngo;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import static com.ngoamber.amberngo.HC_OwnersActivity.healthcard_imp_info;


/**
 * A simple {@link Fragment} subclass.
 */
public class HCO_AccountFragment extends Fragment {

    MaterialButton material_button;
    /* layouts of hc imp details */
    TextView tv_hc_id;
    TextView tv_created_time;
    TextView tv_hcm_name;
    TextView tv_hcm_id;


    public HCO_AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hco__account, container, false);

        /* health card imp info*/
        tv_hc_id = rootView.findViewById(R.id.tv_hc_id);
        tv_created_time = rootView.findViewById(R.id.tv_created_time);
        tv_hcm_id = rootView.findViewById(R.id.tv_hcm_id);
        tv_hcm_name = rootView.findViewById(R.id.tv_hcm_name);
        tv_hc_id.setText(healthcard_imp_info.get(0));
        tv_created_time.setText(healthcard_imp_info.get(1));
        tv_hcm_name.setText(healthcard_imp_info.get(2));
        tv_hcm_id.setText(healthcard_imp_info.get(3));


        /* LOGOUT BUTTON*/
        material_button = rootView.findViewById(R.id.material_button);
        material_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("LOGOUT", true);
                startActivity(intent);
                getActivity().finish();


            }});











        return rootView;
    }

}
