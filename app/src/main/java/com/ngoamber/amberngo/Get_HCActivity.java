package com.ngoamber.amberngo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class Get_HCActivity extends AppCompatActivity {

    DocumentReference docRef;
    EditText get_hc_no_editText;
    Button get_hc;
    ListView listView;
    ArrayList<Map> healthcard_members;
    HC_arrayadapter hc_arrayadapter;
    String hc_id;

    /* layouts of hc imp details */
    TextView tv_hc_id;
    TextView tv_created_time;
    TextView tv_hcm_name;
    TextView tv_hcm_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__hc);

        // Create the adapter to convert the array to views
        healthcard_members = new ArrayList<>();


    }

    public void get_hc(View view) {
        get_hc_no_editText = findViewById(R.id.get_hc_no_editText);
        get_hc = findViewById(R.id.get_hc);
        tv_hc_id = findViewById(R.id.tv_hc_id);
        tv_created_time = findViewById(R.id.tv_created_time);
        tv_hcm_id = findViewById(R.id.tv_hcm_id);
        tv_hcm_name = findViewById(R.id.tv_hcm_name);
        healthcard_members.clear();


        /* CHECK HEALTHCARD NO. */
        if (get_hc_no_editText.getText().toString().isEmpty() || get_hc_no_editText.getText().toString() == null) {
            Toast.makeText(this, "ENTER HEALTHCARD NUMBER!", Toast.LENGTH_SHORT).show();
        } else {

            hc_arrayadapter = new HC_arrayadapter(this, healthcard_members, get_hc_no_editText.getText().toString());
            // Attach the adapter to a ListView
            listView = findViewById(R.id.lv);
            listView.setAdapter(hc_arrayadapter);

            /* get the health card members */
            hc_id = get_hc_no_editText.getText().toString();
            docRef = FirebaseFirestore.getInstance().collection("HEALTHCARDS").document(hc_id);
            docRef.get().addOnCompleteListener(Get_HCActivity.this, new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot d = task.getResult();
                        if (d.exists()) {

                            tv_hc_id.setText(d.getString("HEALTH_CARD_ID"));

                            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                            tv_created_time.setText(sfd.format(d.getTimestamp("HEALTH_CARD_CREATED_TIME").toDate()));
                            tv_hcm_name.setText(d.getString("HEALTH_CARD_MAKER_NAME"));
                            tv_hcm_id.setText(d.getString("HEALTH_CARD_MAKER_ID"));


                            /* Retreiving all the members */
                            FirebaseFirestore.getInstance().collection("HEALTHCARDS/" + hc_id + "/MEMBERS")
                                    .get()
                                    .addOnCompleteListener(Get_HCActivity.this, new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    healthcard_members.add(document.getData());
                                                    Log.d("GET MEMBERS", document.getId() + " => " + document.getData());
                                                }
                                                hc_arrayadapter.notifyDataSetChanged();

                                            } else {
                                                Log.d("GET MEMBERS", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });


                            Toast.makeText(Get_HCActivity.this, "HC ID :" + hc_id + " FOUND ", Toast.LENGTH_SHORT).show();
                            Log.d("GET", "DocumentSnapshot data: " + d.getData());
                        } else {
                            Toast.makeText(Get_HCActivity.this, "NOT FOUND", Toast.LENGTH_SHORT).show();
                            Log.d("GET", "No such document");
                        }
                    } else {
                        Log.d("GET", "get failed with ", task.getException());
                    }
                }
            });
        }
    }
}
