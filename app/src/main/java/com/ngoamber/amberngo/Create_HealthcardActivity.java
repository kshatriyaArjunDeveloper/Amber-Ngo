package com.ngoamber.amberngo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ngoamber.amberngo.LoginActivity.HCM_ID;
import static com.ngoamber.amberngo.LoginActivity.HCM_NAME;
import static com.ngoamber.amberngo.LoginActivity.USER_NO;
import static com.ngoamber.amberngo.LoginActivity.level;
import static com.ngoamber.amberngo.RecyclerView_Adapter.healthcard_members_al_main;

public class Create_HealthcardActivity extends AppCompatActivity {

    /**
     * HEALTHCARD RELATED VARIABLES
     */
    public ArrayList<HC_member> memberlist_rv;
    public static final String HEALTH_CARD_ID = "HEALTH_CARD_ID";
    public static final String HEALTH_CARD_MAKER_ID = "HEALTH_CARD_MAKER_ID";
    public static final String HEALTH_CARD_MAKER_NAME = "HEALTH_CARD_MAKER_NAME";
    public static final String HEALTH_CARD_CREATED_TIME = "HEALTH_CARD_CREATED_TIME";


    /**
     * RECYCLERVIEW RELATED VARIABLES
     */
    private RecyclerView recyclerView;
    private RecyclerView_Adapter hc_rv_adapter;
    public final int PICK_IMAGE_REQUEST = 1;
    static Uri imageUri;
    Integer image_position_al;
    static Boolean dp_fab_clicked;

    /**
     * FIRESTORE AND FIREBASE STORAGE RELATED VARIABLES
     */
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference spaceRef;
    DocumentReference doc_ref;

    /**
     * SOME OTHER VARIABLES
     */
    MaterialButton addHealthCard_Button;
    /* HEALTHCARD NUMBER HOLDER */
    EditText card_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__healthcard);

        /* REFERENCING HEALTHCARD UPLOAD BUTTON AND HEALTHCARD NO. EDIT TEXT AND TOTAL MEMBERS TEXT VIEW */
        addHealthCard_Button = findViewById(R.id.add_card);
        card_no = findViewById(R.id.editText_card_no);

        /* RECYCLERVIEW INITIALIZING ...*/
        memberlist_rv = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        hc_rv_adapter = new RecyclerView_Adapter(this, memberlist_rv, new RV_ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                /* ADDS IMAGE IN CORRECT ARRAY ELEMENT */
                image_position_al = position;
                /* SELECTS IMAGE FROM GALLERY*/
                select_image();
                imageUri = null;
            }
        });
        recyclerView.setAdapter(hc_rv_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

    }


    /**
     * UPLOAD ALL DATA TO FIRESTORE AND FIREBASE
     */
    public void addToFirestoreDb(View v) {

        /* CHECK WHETHER ANY DATA IS NULL OR EMPTY AND THEN UPLOADS EVERYTHING */
        if (should_upload_data()) {

            /* HEALTHCARD INFO */
            Map<String, Object> healthcard_data = new HashMap<>();
            healthcard_data.put(HEALTH_CARD_ID, card_no.getText().toString());
            healthcard_data.put(HEALTH_CARD_CREATED_TIME, FieldValue.serverTimestamp());
            healthcard_data.put(HEALTH_CARD_MAKER_NAME, HCM_NAME);
            healthcard_data.put(HEALTH_CARD_MAKER_ID, HCM_ID);


            /* ADDING HEALTH CARD INFO UNDER HC_M DB */
            doc_ref = FirebaseFirestore.getInstance().document
                    ("AMBER_NGO/HEALTHCARD_MAKERS/" + HCM_ID + "/" + card_no.getText().toString());
            doc_ref.set(healthcard_data).addOnFailureListener(Create_HealthcardActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Create_HealthcardActivity.this,
                            "PROBLEM OCCURRED ADDING HEALTH CARD INFO UNDER HCM", Toast.LENGTH_SHORT).show();
                }
            });


            /* ADDING HEALTHCARD INFO UNDER HCO DB */
            doc_ref = FirebaseFirestore.getInstance().document
                    ("HEALTHCARDS/" + card_no.getText().toString());
            doc_ref.set(healthcard_data).addOnFailureListener(Create_HealthcardActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Create_HealthcardActivity.this,
                            "PROBLEM OCCURRED ADDING HEALTH CARD INFO UNDER HCO", Toast.LENGTH_SHORT).show();
                }
            });


            /* ADDING MEMBERS */
            Map<String, Object> healthcard_member = new HashMap<>();
            for (int i = 0; i < healthcard_members_al_main.size(); i++) {
                healthcard_member.put("NAME", healthcard_members_al_main.get(i).getName());
                healthcard_member.put("AGE", healthcard_members_al_main.get(i).getAge());
                healthcard_member.put("F_NAME", healthcard_members_al_main.get(i).getFat_hus_name());
                healthcard_member.put("M_NAME", healthcard_members_al_main.get(i).getMothers_name());
                healthcard_member.put("ADDRESS", healthcard_members_al_main.get(i).getAddress());
                healthcard_member.put("M_NUMBER", healthcard_members_al_main.get(i).getMobile_number());
                doc_ref = FirebaseFirestore.getInstance().document
                        ("HEALTHCARDS/" + card_no.getText().toString() + "/MEMBERS/" + healthcard_member.get("NAME"));
                final int finalI = i;
                doc_ref.set(healthcard_member).addOnFailureListener(Create_HealthcardActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Create_HealthcardActivity.this,
                                "PROBLEM OCCURRED ADDING MEMBER:" + finalI, Toast.LENGTH_SHORT).show();
                    }
                });


                /* UPLOADING PROFILE IMAGES OF MEMBERS */
                spaceRef = storageRef.child("HEALTHCARDS/" + card_no.getText().toString() + "/" + healthcard_member.get("NAME") +
                        "/PROFILE_IMAGE.jpg");
                if (healthcard_members_al_main.get(i).getPerson_image_uri() != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    spaceRef.putFile(healthcard_members_al_main.get(i).getPerson_image_uri())
                            .addOnSuccessListener(Create_HealthcardActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                }
                            })
                            .addOnFailureListener(Create_HealthcardActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Create_HealthcardActivity.this,
                                            "FAILED UPLOADING MEMBER IMAGE" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(Create_HealthcardActivity.this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
                }


                /* UPLOADING IDENTITY IMAGES OF MEMBERS */
                spaceRef = storageRef.child("HEALTHCARDS/" + card_no.getText().toString() + "/" + healthcard_member.get("NAME") +
                        "/IDENTITY_IMAGE.jpg");
                if (healthcard_members_al_main.get(i).getIdentity_image_uri() != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    spaceRef.putFile(healthcard_members_al_main.get(i).getIdentity_image_uri())
                            .addOnSuccessListener(Create_HealthcardActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Create_HealthcardActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(Create_HealthcardActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Create_HealthcardActivity.this,
                                            "FAILED UPLOADING IDENTITY PROOF" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(Create_HealthcardActivity.this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
                }
            }
            /* ADDING HEALTHCARD ID UNDER USERS DB TO ALLOW LOGIN */
            Map<String, Object> HC_MEMBER_LOGIN_ID = new HashMap<>();
            HC_MEMBER_LOGIN_ID.put("LEVEL", "2");
            HC_MEMBER_LOGIN_ID.put("ALLOW", true);
            doc_ref = FirebaseFirestore.getInstance().document
                    ("USERS/" + (card_no.getText().toString()));
            doc_ref.set(HC_MEMBER_LOGIN_ID).addOnFailureListener(Create_HealthcardActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Create_HealthcardActivity.this,
                            "PROBLEM OCCURRED MAKING LOGIN ID", Toast.LENGTH_SHORT).show();
                }
            });

            /* decrease card credited and increase card sold by 1 if it's a hc maker*/
            if (level.equals("1")) {

                /* updating data */
                FirebaseFirestore.getInstance().document("HEALTHCARD_MAKERS/"
                        + USER_NO)
                        .update("CARDS_CREDITED", FieldValue.increment(-1))
                        .addOnSuccessListener(Create_HealthcardActivity.this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Create_HealthcardActivity.this, "TOTAL CARDS CREDITED UPDATED", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(Create_HealthcardActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Create_HealthcardActivity.this, "ERROR, TRY AGAIN LATER", Toast.LENGTH_SHORT).show();

                            }
                        });

                FirebaseFirestore.getInstance().document("HEALTHCARD_MAKERS/"
                        + USER_NO)
                        .update("CARDS_SOLD", FieldValue.increment(1))
                        .addOnSuccessListener(Create_HealthcardActivity.this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Create_HealthcardActivity.this, "TOTAL CARDS CREDITED UPDATED", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(Create_HealthcardActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Create_HealthcardActivity.this, "ERROR, TRY AGAIN LATER", Toast.LENGTH_SHORT).show();

                            }
                        });

            }

        }
    }


    /**
     * CHECKS ALL THE ENTRIES NEEDED FOR MAKING HEALTHCARD AND TELLS IF SOMETHING IS WRONG
     */
    public boolean should_upload_data() {

        /* CHECK HEALTHCARD NO. */
        if (card_no.getText().toString().isEmpty() || card_no.getText().toString() == null) {
            Toast.makeText(this, "ENTER HEALTHCARD NUMBER!", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            /* CHECK ATLEAST ONE MEMBER IS THEIR */
            if (healthcard_members_al_main.size() < 1) {
                Toast.makeText(this, "ADD MEMBER", Toast.LENGTH_SHORT).show();
                return false;
            }

            /* CHECKS EACH MEMBERS DATA */
            for (int i = 0; i < healthcard_members_al_main.size(); i++) {
                if (healthcard_members_al_main.get(i).getName().isEmpty() || healthcard_members_al_main.get(i).getName() == null ||
                        healthcard_members_al_main.get(i).getAge().isEmpty() || healthcard_members_al_main.get(i).getAge() == null ||
                        healthcard_members_al_main.get(i).getMobile_number().isEmpty() || healthcard_members_al_main.get(i).getMobile_number() == null ||
                        healthcard_members_al_main.get(i).getFat_hus_name().isEmpty() || healthcard_members_al_main.get(i).getFat_hus_name() == null ||
                        healthcard_members_al_main.get(i).getMothers_name().isEmpty() || healthcard_members_al_main.get(i).getMothers_name() == null ||
                        healthcard_members_al_main.get(i).getAddress().isEmpty() || healthcard_members_al_main.get(i).getAddress() == null ||
                        healthcard_members_al_main.get(i).getIdentity_image_uri() == null || healthcard_members_al_main.get(i).getPerson_image_uri() == null
                ) {
                    Toast.makeText(this, "ENTER FIELDS CORRECTLY OF MEMBER :" + (i + 1), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * ADD ONE MEMBER TO ARRAYLIST OF HEALTHCARD MEMBERS FOR RECYCLERVIEW ADAPTER
     */
    public void onAddMember(View v) {
        HC_member healthcardmember = new HC_member();
        memberlist_rv.add(healthcardmember);
        hc_rv_adapter.notifyItemInserted(memberlist_rv.size());
        recyclerView.scrollToPosition(memberlist_rv.size() - 1);
        Toast.makeText(this, "Member Added", Toast.LENGTH_SHORT).show();
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
                    healthcard_members_al_main.get(image_position_al).setPerson_image_uri(imageUri);
                } else {
                    healthcard_members_al_main.get(image_position_al).setIdentity_image_uri(imageUri);
                }
                hc_rv_adapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}