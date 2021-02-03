package com.ngoamber.amberngo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Create_HCMActivity extends AppCompatActivity {



    /**
     * FIRESTORE AND FIREBASE STORAGE RELATED VARIABLES
     */
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference img_fileRef;
    DocumentReference doc_ref;

    /**
     * SOME LAYOUT VARIABLES
     */
    Button add_HCM_Button;
    EditText editText_HCM_ID;
    EditText editText_name;
    EditText editText_number1;
    EditText editText_number2;
    EditText editText_gmail_id;
    EditText editText_address1;
    EditText editText_address2;
    ImageView imageView_dp;
    ImageView imageView_ip;
    ImageButton floatingActionButton_set_dp;
    ImageButton floatingActionButton_set_ip;
    Uri hcm_dp;
    Uri hcm_ip;

    /**
     * IMAGE PICK AND DISPLAY RELATED VARIABLES
     */
    public final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;
    Boolean dp_fab_clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__hcm);


        /* INITIALIZING ALL LAYOUT OBJECTS */
        add_HCM_Button = findViewById(R.id.button_add);
        editText_HCM_ID = findViewById(R.id.editText_hcm_id);
        editText_name = findViewById(R.id.editText_name);
        editText_number1 = findViewById(R.id.editText_number1);
        editText_number2 = findViewById(R.id.editText_number2);
        editText_gmail_id = findViewById(R.id.editText_gmail_id);
        editText_address1 = findViewById(R.id.editText_address1);
        editText_address2 = findViewById(R.id.editText_address2);
        imageView_dp = findViewById(R.id.imageView_dp);
        floatingActionButton_set_dp = findViewById(R.id.floatingActionButton);
        imageView_ip = findViewById(R.id.imageView_ip_hcm);
        floatingActionButton_set_ip = findViewById(R.id.floatingActionButton2);



    }

    /**
     * UPLOAD ALL DATA TO FIRESTORE AND FIREBASE
     */
    public void addToFirestoreDb_HCM(View v) {

        /* CHECK WHETHER ANY DATA IS NULL OR EMPTY AND THEN UPLOADS EVERYTHING */
        if (should_upload_data()) {

            /* ADDING HCM */
            Map<String, Object> healthcard_maker = new HashMap<>();
            healthcard_maker.put("NAME", editText_name.getText().toString());
            healthcard_maker.put("NUMBER_1", editText_number1.getText().toString());
            healthcard_maker.put("NUMBER_2", editText_number2.getText().toString());
            healthcard_maker.put("GMAIL_ID", editText_gmail_id.getText().toString());
            healthcard_maker.put("ADDRESS_1", editText_address1.getText().toString());
            healthcard_maker.put("ADDRESS_2", editText_address2.getText().toString());
            healthcard_maker.put("CARDS_CREDITED", 0);
            healthcard_maker.put("CARDS_SOLD", 0);

                doc_ref = FirebaseFirestore.getInstance().document
                        ("HEALTHCARD_MAKERS/" + editText_HCM_ID.getText().toString());
                doc_ref.set(healthcard_maker).addOnFailureListener(Create_HCMActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Create_HCMActivity.this,
                                "PROBLEM OCCURRED ADDING HCM", Toast.LENGTH_SHORT).show();
                    }
                });


                /* UPLOADING PROFILE IMAGE */
                img_fileRef = storageRef.child("HCM/" + editText_HCM_ID.getText().toString() + "/PROFILE_IMAGE.jpg");
                if (hcm_dp != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    img_fileRef.putFile(hcm_dp)
                            .addOnSuccessListener(Create_HCMActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                }
                            })
                            .addOnFailureListener(Create_HCMActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Create_HCMActivity.this,
                                            "FAILED UPLOADING MEMBER IMAGE" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(Create_HCMActivity.this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
                }


                /* UPLOADING IDENTITY IMAGE */
                img_fileRef = storageRef.child("HCM/" + editText_HCM_ID.getText().toString() + "/IDENTITY_IMAGE.jpg");
                if (hcm_ip != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    img_fileRef.putFile(hcm_ip)
                            .addOnSuccessListener(Create_HCMActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Create_HCMActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(Create_HCMActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Create_HCMActivity.this,
                                            "FAILED UPLOADING IDENTITY PROOF" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(Create_HCMActivity.this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
                }

            /* ADDING HEALTHCARDMAKER ID UNDER USERS DB TO ALLOW LOGIN */
            Map<String, Object> HC_MEMBER_LOGIN_ID = new HashMap<>();
            HC_MEMBER_LOGIN_ID.put("LEVEL","1");
            HC_MEMBER_LOGIN_ID.put("ALLOW",true);
            HC_MEMBER_LOGIN_ID.put("NAME",editText_name.getText().toString());
            doc_ref = FirebaseFirestore.getInstance().document
                    ("USERS/" + editText_HCM_ID.getText().toString());
            doc_ref.set(HC_MEMBER_LOGIN_ID).addOnFailureListener(Create_HCMActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Create_HCMActivity.this,
                            "PROBLEM OCCURRED MAKING LOGIN ID", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    /**
     * CHECKS ALL THE ENTRIES NEEDED FOR MAKING HEALTHCARD AND TELLS IF SOMETHING IS WRONG
     */
    public boolean should_upload_data() {

        /* CHECK HEALTHCARD NO. */
        if (editText_HCM_ID.getText().toString().isEmpty() || editText_HCM_ID.getText().toString() == null) {
            Toast.makeText(this, "ENTER HCM ID!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
                if (editText_name.getText().toString().isEmpty() || editText_HCM_ID.getText().toString() == null ||
                        editText_number1.getText().toString().isEmpty() || editText_number1.getText().toString() == null ||
                        editText_number2.getText().toString().isEmpty() || editText_number2.getText().toString() == null ||
                        editText_gmail_id.getText().toString().isEmpty() || editText_gmail_id.getText().toString() == null ||
                        editText_address1.getText().toString().isEmpty() || editText_address1.getText().toString() == null ||
                        editText_address2.getText().toString().isEmpty() || editText_address2.getText().toString() == null ||
                        hcm_ip == null || hcm_dp == null
                ) {
                    Toast.makeText(this, "ENTER FIELDS CORRECTLY", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

        return true;
    }

    /**
     * ADD URI TO HCM ID AND DP URI VARIABLE TO UPLOAD TO DB
     */
    public void select_image_fab_dp (View view){
        dp_fab_clicked = true;
        select_image();
    }

    /**
     * ADD URI TO HCM ID AND DP URI VARIABLE TO UPLOAD TO DB
     */
    public void select_image_fab_ip (View view){
        dp_fab_clicked = false;
        select_image();
    }


    /**
     * SELECTS IMAGE FROM GALLERY
     */
    public void select_image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                if (dp_fab_clicked) {
                    hcm_dp = imageUri;
                    imageView_dp.setImageURI(hcm_dp);
                    imageView_dp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    hcm_ip = imageUri;
                    imageView_ip.setImageURI(hcm_ip);
                    imageView_ip.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
