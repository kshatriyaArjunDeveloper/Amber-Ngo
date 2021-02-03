package com.ngoamber.amberngo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Get_HCMActivity extends AppCompatActivity {

    Button get_hcm;
    EditText get_hcm_id_editText;

    /* layout of hcm info */
    ImageView imageView_dp_hcm;
    ImageView imageView_ip_hcm;
    TextView name_hcm;
    TextView tv_gmail_id_hcm;
    TextView tv_address_1;
    TextView tv_address_2;
    TextView tv_mobile_1;
    TextView tv_mobile_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__hcm);
    }

    /* displays hcm info */
    public void get_hcm(View view) {

        get_hcm_id_editText = findViewById(R.id.get_hcm_id_editText);
        get_hcm = findViewById(R.id.get_hcm);

        imageView_dp_hcm = findViewById(R.id.imageView_dp_hcm);
        imageView_ip_hcm = findViewById(R.id.imageView_ip_hcm);
        name_hcm = findViewById(R.id.name_hcm);
        tv_gmail_id_hcm = findViewById(R.id.tv_gmail_id_hcm);
        tv_address_1 = findViewById(R.id.tv_address_1);
        tv_address_2 = findViewById(R.id.tv_address_2);
        tv_mobile_1 = findViewById(R.id.tv_mobile_1);
        tv_mobile_2 = findViewById(R.id.tv_mobile_2);


        /* CHECK HCM ID */
        if (get_hcm_id_editText.getText().toString().isEmpty() || get_hcm_id_editText.getText().toString() == null) {
            Toast.makeText(this, "ENTER HCM ID!", Toast.LENGTH_SHORT).show();
        } else {

            /* get details */
            final String hcm_id = get_hcm_id_editText.getText().toString();
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("HEALTHCARD_MAKERS").document(hcm_id);
            documentReference.get().addOnCompleteListener(Get_HCMActivity.this, new OnCompleteListener<DocumentSnapshot>() {
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

                            /* loading images */
                            // Reference to an image file in Cloud Storage
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference("HCM/"+ hcm_id
                                    + "/PROFILE_IMAGE.jpg");


                            // Download directly from StorageReference using Glide
                            // (See MyAppGlideModule for Loader registration)
                            Log.i("IMAGE","HCM/"+ hcm_id  + "/PROFILE_IMAGE.jpg");
                            GlideApp.with(Get_HCMActivity.this /* context */)
                                    .load(storageReference)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(imageView_dp_hcm);

                            // Reference to an image file in Cloud Storage
                            storageReference = FirebaseStorage.getInstance().getReference("HCM/"+ hcm_id
                                    + "/PROFILE_IMAGE.jpg");

                            // Download directly from StorageReference using Glide
                            // (See MyAppGlideModule for Loader registration)
                            GlideApp.with(Get_HCMActivity.this /* context */)
                                    .load(storageReference)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(imageView_ip_hcm);

                            imageView_dp_hcm.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imageView_ip_hcm.setScaleType(ImageView.ScaleType.FIT_XY);

                            Toast.makeText(Get_HCMActivity.this, "HC ID :" + hcm_id + " FOUND ", Toast.LENGTH_SHORT).show();
                            Log.d("GET", "DocumentSnapshot data: " + d.getData());
                        } else {
                            Toast.makeText(Get_HCMActivity.this, "NOT FOUND", Toast.LENGTH_SHORT).show();
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
