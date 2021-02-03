package com.ngoamber.amberngo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import static com.ngoamber.amberngo.LoginActivity.USER_NO;

public class HC_OwnersActivity extends AppCompatActivity {

    static ArrayList<Map> healthcard_members;
    static ArrayList<String> healthcard_imp_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hc__owners);


        //loading the default home fragment

        /* CREATING ALL MEMBERS FOR HOME FRAGMENT AND LOAD IT ALSO FOR ACCOUNT FRAGMENT ALSO */
        healthcard_members = new ArrayList<>();
        healthcard_imp_info = new ArrayList<>();
        display_hco_info();


        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /* HANDLING BOTTOM NAVIGATION */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HCO_HomeFragment();
                    break;
                case R.id.navigation_hospitals:
                    // Do something for navigation
                    break;
                case R.id.navigation_account:
                    fragment = new HCO_AccountFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    /* */
    public void display_hco_info() {
        /* Retreiving all the members */
        FirebaseFirestore.getInstance().collection("HEALTHCARDS/" + USER_NO + "/MEMBERS")
                .get()
                .addOnCompleteListener(HC_OwnersActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                healthcard_members.add(document.getData());
                                Log.d("GET MEMBERS", document.getId() + " => " + document.getData());
                            }
                            loadFragment(new HCO_HomeFragment());
                        } else {
                            Log.d("GET MEMBERS", "Error getting documents: ", task.getException());
                        }
                    }
                });

        /* imp health card details*/

        FirebaseFirestore.getInstance().collection("HEALTHCARDS").document(USER_NO)
                .get().addOnCompleteListener(HC_OwnersActivity.this, new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot d = task.getResult();
                    if (d.exists()) {

                        healthcard_imp_info.add(d.getString("HEALTH_CARD_ID"));

                        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                        healthcard_imp_info.add(sfd.format(d.getTimestamp("HEALTH_CARD_CREATED_TIME").toDate()));
                        healthcard_imp_info.add(d.getString("HEALTH_CARD_MAKER_NAME"));
                        healthcard_imp_info.add(d.getString("HEALTH_CARD_MAKER_ID"));
                        Log.d("GET", "DocumentSnapshot data: " + d.getData());
                    } else {
                        Log.d("GET", "No such document");
                    }
                } else {
                    Log.d("GET", "get failed with ", task.getException());
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        /* JUST FOR SKIPPING LOGIN SCREEN */
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


}

