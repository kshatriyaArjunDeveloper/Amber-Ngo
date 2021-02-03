package com.ngoamber.amberngo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FoundersActivity extends AppCompatActivity {

    Button button_create_health_card;
    Button button_latest_health_card;
    Button button_create_hcm;
    Button button_revoke;
    Button button_open_Get_HCMActivity;
    Button button_credit_hc;
    Button button_add_hospitals;
    ImageButton button_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founders);


        /* LOGOUT BUTTON*/
        button_logout = findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FoundersActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("LOGOUT", true);
                startActivity(intent);
                FoundersActivity.this.finish();


            }
        });
    }


    public void open_Latest_HealthcardActivity(View view) {
        button_create_health_card = findViewById(R.id.button_latest_health_card);
        Intent intent = new Intent(FoundersActivity.this, Get_HCActivity.class);
        startActivity(intent);
    }

    public void open_Create_HealthcardActivity(View view) {
        button_latest_health_card = findViewById(R.id.button_create_health_card_hcm);
        Intent intent = new Intent(FoundersActivity.this, Create_HealthcardActivity.class);
        startActivity(intent);
    }

    public void open_Create_HCMActivity(View view) {
        button_create_hcm = findViewById(R.id.button_create_hcm);
        Intent intent = new Intent(FoundersActivity.this, Create_HCMActivity.class);
        startActivity(intent);
    }

    public void button_open_Get_HCMActivity(View view) {
        button_open_Get_HCMActivity = findViewById(R.id.button_open_Get_HCMActivity);
        Intent intent = new Intent(FoundersActivity.this, Get_HCMActivity.class);
        startActivity(intent);
    }

    public void revoke_authorization(View view) {
        button_revoke = findViewById(R.id.button_revoke);

        final boolean[] ALLOW = new boolean[1];

        //list of items
        final String[] items = getResources().getStringArray(R.array.dialog_single_choice_array);

        final EditText hcm_id = new EditText(FoundersActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        hcm_id.setLayoutParams(lp);
        hcm_id.setInputType(InputType.TYPE_CLASS_PHONE);
        hcm_id.setHint("TYPE HC ID");

        new MaterialAlertDialogBuilder(FoundersActivity.this)
                .setTitle("SET AUTHORIZATION")
                .setView(hcm_id)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                        ALLOW[0] = selectedIndex == 0;
                    }
                })
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore.getInstance().document("USERS/" + hcm_id.getText().toString())
                                .update("ALLOW", ALLOW[0])
                                .addOnSuccessListener(FoundersActivity.this, new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(FoundersActivity.this, "AUTHORIZATION UPDATED", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(FoundersActivity.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FoundersActivity.this, "ERROR, TRY AGAIN LATER", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                })
                .show();
    }

    /* to credit healthcards of an healthcard maker*/
    public void button_credit_hc(View view) {

        button_credit_hc = findViewById(R.id.button_credit_hc);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        /* to enter the hcm id */
        final EditText hcm_id = new EditText(FoundersActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        hcm_id.setLayoutParams(lp);
        hcm_id.setInputType(InputType.TYPE_CLASS_PHONE);
        hcm_id.setHint("TYPE HC ID");
        layout.addView(hcm_id);

        /* to enter the no. of cards to be credited */
        final EditText cards_credited = new EditText(FoundersActivity.this);
        cards_credited.setLayoutParams(lp);
        cards_credited.setInputType(InputType.TYPE_CLASS_NUMBER);
        cards_credited.setHint("NO. OF CARDS TO BE CREDITED");
        layout.addView(cards_credited);

        new MaterialAlertDialogBuilder(FoundersActivity.this)
                .setTitle("CREDIT HEALTHCARD")
                .setView(layout)
                .setPositiveButton("CREDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /* updating data */
                        FirebaseFirestore.getInstance().document("HEALTHCARD_MAKERS/"
                                + hcm_id.getText().toString())
                                .update("CARDS_CREDITED", FieldValue.increment(Long.valueOf(cards_credited.getText().toString())))
                                .addOnSuccessListener(FoundersActivity.this, new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(FoundersActivity.this, "TOTAL CARDS CREDITED UPDATED", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(FoundersActivity.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FoundersActivity.this, "ERROR, TRY AGAIN LATER", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                })
                .show();
    }

    /* to add hospital */
    public void button_add_hospitals(View view) {


        button_add_hospitals = findViewById(R.id.button_add_hospitals);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        /* to enter the name of the hospitals  */
        final EditText hospital_name = new EditText(FoundersActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        hospital_name.setLayoutParams(lp);
        hospital_name.setInputType(InputType.TYPE_CLASS_TEXT);
        hospital_name.setHint("HOSPITAL NAME");
        layout.addView(hospital_name);

        /* to enter the address of the hospital  */
        final EditText hospital_address = new EditText(FoundersActivity.this);
        hospital_address.setLayoutParams(lp);
        hospital_address.setInputType(InputType.TYPE_CLASS_TEXT);
        hospital_address.setHint("HOSPITAL ADDRESS");
        layout.addView(hospital_address);

        new MaterialAlertDialogBuilder(FoundersActivity.this)
                .setTitle("ADD HOSPITALS")
                .setView(layout)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /* ADDING HOSPITAL */
                        Map<String, Object> hospital = new HashMap<>();
                        hospital.put("NAME", hospital_name.getText().toString());
                        hospital.put("ADDRESS", hospital_address.getText().toString());

                        FirebaseFirestore.getInstance().document
                                ("HOSPITALS/" + hospital_name.getText().toString())
                                .set(hospital).addOnFailureListener(FoundersActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FoundersActivity.this,
                                        "PROBLEM OCCURRED ADDING HOSPITAL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


}
