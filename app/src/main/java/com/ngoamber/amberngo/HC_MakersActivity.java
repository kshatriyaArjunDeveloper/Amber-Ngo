package com.ngoamber.amberngo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.ngoamber.amberngo.LoginActivity.USER_NO;

public class HC_MakersActivity extends AppCompatActivity {

    Button button_create_health_card;
    ImageButton button_logout;

    /* layout of hcm info */
    ImageView imageView_dp_hcm;
    ImageView imageView_ip_hcm;
    TextView name_hcm;
    TextView tv_gmail_id_hcm;
    TextView tv_address_1;
    TextView tv_address_2;
    TextView tv_mobile_1;
    TextView tv_mobile_2;
    TextView tv_card_credited;
    TextView tv_card_sold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hc__makers);

        /* LOGOUT BUTTON*/
        button_logout = findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HC_MakersActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("LOGOUT", true);
                startActivity(intent);
                HC_MakersActivity.this.finish();
            }
        });

        /* load hcm details */
        get_hcm();
    }

    public void open_Create_HealthcardActivity_hcm(View view) {
        button_create_health_card = findViewById(R.id.button_create_health_card_hcm);
        Intent intent = new Intent(HC_MakersActivity.this, Create_HealthcardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    /* displays hcm info */
    public void get_hcm() {

        imageView_dp_hcm = findViewById(R.id.imageView_dp_hcm);
        imageView_ip_hcm = findViewById(R.id.imageView_ip_hcm);
        name_hcm = findViewById(R.id.name_hcm);
        tv_gmail_id_hcm = findViewById(R.id.tv_gmail_id_hcm);
        tv_address_1 = findViewById(R.id.tv_address_1);
        tv_address_2 = findViewById(R.id.tv_address_2);
        tv_mobile_1 = findViewById(R.id.tv_mobile_1);
        tv_mobile_2 = findViewById(R.id.tv_mobile_2);
        tv_card_credited = findViewById(R.id.tv_card_credited);
        tv_card_sold = findViewById(R.id.tv_card_sold);


        /* get details */
        final String hcm_id = USER_NO;
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("HEALTHCARD_MAKERS").document(hcm_id);
        documentReference.get().addOnCompleteListener(HC_MakersActivity.this, new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot d = task.getResult();
                    if (d.exists()) {

                        name_hcm.setText(d.getString("NAME"));
                        tv_gmail_id_hcm.setText(d.getString("GMAIL_ID"));
                        tv_address_1.setText(d.getString("ADDRESS_1"));
                        tv_address_2.setText(d.getString("ADDRESS_2"));
                        tv_mobile_1.setText(d.getString("NUMBER_1"));
                        tv_mobile_2.setText(d.getString("NUMBER_2"));
                        tv_card_credited.setText("CARD CREDITED : " + d.getLong("CARDS_CREDITED").toString());
                        tv_card_sold.setText("CARD SOLD : " + d.getLong("CARDS_SOLD").toString());

                        /* loading images */
                        // Reference to an image file in Cloud Storage
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("HCM/" + hcm_id
                                + "/PROFILE_IMAGE.jpg");


                        // Download directly from StorageReference using Glide
                        // (See MyAppGlideModule for Loader registration)
                        Log.i("IMAGE", "HCM/" + hcm_id + "/PROFILE_IMAGE.jpg");
                        GlideApp.with(HC_MakersActivity.this /* context */)
                                .load(storageReference)
                                .into(imageView_dp_hcm);

                        // Reference to an image file in Cloud Storage
                        storageReference = FirebaseStorage.getInstance().getReference("HCM/" + hcm_id
                                + "/PROFILE_IMAGE.jpg");

                        // Download directly from StorageReference using Glide
                        // (See MyAppGlideModule for Loader registration)
                        GlideApp.with(HC_MakersActivity.this /* context */)
                                .load(storageReference)
                                .into(imageView_ip_hcm);

                        imageView_dp_hcm.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView_ip_hcm.setScaleType(ImageView.ScaleType.FIT_XY);

                        Toast.makeText(HC_MakersActivity.this, "HC ID :" + hcm_id + " FOUND ", Toast.LENGTH_SHORT).show();
                        Log.d("GET", "DocumentSnapshot data: " + d.getData());
                    } else {
                        Toast.makeText(HC_MakersActivity.this, "NOT FOUND", Toast.LENGTH_SHORT).show();
                        Log.d("GET", "No such document");
                    }
                } else {
                    Log.d("GET", "get failed with ", task.getException());
                }
            }
        });
    }

}