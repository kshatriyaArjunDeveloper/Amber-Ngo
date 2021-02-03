package com.ngoamber.amberngo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {


    /* FIREBASE LOGIN RELATED VARIABLES */
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    /* REFERENCES TO USERS DB FOR CHECKING USERS */
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("USERS");
    DocumentReference docRef;
    static String USER_NO;

    /* INTENT TO OPEN ACTIVITY A.T. TO USERS LEVEL */
    Intent intent;

    /**
     * HEALTHCARD MAKER DETAILS
     */
    public static String HCM_ID;
    public static String HCM_NAME;
    public static String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        final AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.login_screen)
                .setPhoneButtonId(R.id.material_button_login)
                .setGoogleButtonId(R.id.button)
                .build();

        /* Initialize Firebase components */
        mFirebaseAuth = FirebaseAuth.getInstance();
        /* Initializing  Auth Listener
           RUNS EVERY TIME WHEN ACTIVITY OR APP OPENS */
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                /* USER IS LOGGED IN */
                if (user != null) {

                    /* NO. WITH +91 ADDED AND MAKING IT NORMAL 10 DIGIT NO. */
                    USER_NO = user.getPhoneNumber().substring(3);

                    /* MATCHING USER TO DATABASE */
                    docRef = collectionReference.document(USER_NO);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {


                                    Boolean ALLOW = document.getBoolean("ALLOW");

                                    if (ALLOW) {

                                        /* IF USER IS IN DB */
                                        level = document.getString("LEVEL");

                                        /* OPENS CORRECT ACTIVITY A.T. TO LEVEL OF USER */
                                        switch (level) {

                                            /* FOR FOUNDERS */
                                            case "0":

                                                /* INITIALIZING HC MAKER ID */
                                                HCM_ID = USER_NO;

                                                /* INITIALIZING HC MAKER NAME */
                                                HCM_NAME = document.getString("NAME");

                                                intent = new Intent(LoginActivity.this,
                                                        FoundersActivity.class);
                                                startActivity(intent);
                                                break;

                                            /* FOR HC MAKERS */
                                            case "1":

                                                /* INITIALIZING HC MAKER ID */
                                                HCM_ID = USER_NO;

                                                /* INITIALIZING HC MAKER NAME */
                                                HCM_NAME = document.getString("NAME");

                                                intent = new Intent(LoginActivity.this,
                                                        HC_MakersActivity.class);
                                                startActivity(intent);
                                                break;

                                            /* FOR HC USERS */
                                            case "2":
                                                intent = new Intent(LoginActivity.this,
                                                        HC_OwnersActivity.class);
                                                startActivity(intent);
                                                break;
                                        }

                                        Toast.makeText(LoginActivity.this, "Welcome " + user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        /* USER NOT FOUND IN DB */
                                        Toast.makeText(LoginActivity.this,
                                                "PLEASE CONTACT AMBER NGO", Toast.LENGTH_LONG).show();
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("NOT ALLOWED", "User account deleted.");
                                                        }
                                                    }
                                                });
                                    }

                                } else {
                                    /* USER NOT FOUND IN DB */
                                    Toast.makeText(LoginActivity.this,
                                            "PLEASE CONTACT AMBER NGO", Toast.LENGTH_LONG).show();
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        Log.d("NOT ALLOWED", "User account deleted.");
                                                    }
                                                }
                                            });
                                }
                            } else {
                                /* SIGN IN DIDN'T WORKED */
                                Toast.makeText(LoginActivity.this,
                                        "PLEASE CONTACT AMBER NGO", Toast.LENGTH_LONG).show();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("NOT ALLOWED", "User account deleted.");
                                                }
                                            }
                                        });
                                Log.d("onAuthStateChanged", "get failed with ", task.getException());
                            }
                        }
                    });
                } else {

                    /* IF NO USER IS SIGNED IN THEN Create and launch sign-in intent */
                    startActivityForResult(
                            AuthUI.getInstance().createSignInIntentBuilder()
                                    // ...
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build()))
                                    .setAuthMethodPickerLayout(customLayout)
                                    .build(), RC_SIGN_IN);

                }
            }
        };


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                /* NO. WITH +91 ADDED AND MAKING IT NORMAL 10 DIGIT NO. */
                USER_NO = user.getPhoneNumber().substring(3);
                Log.i("SIGNIN",USER_NO);

                /* MATCHING USER TO DATABASE */
                docRef = collectionReference.document(USER_NO);
                docRef.get().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                Log.i("SIGNIN 2",document.toString());
                                Boolean ALLOW = document.getBoolean("ALLOW");

                                if (ALLOW) {

                                    /* IF USER IS IN DB */
                                    String level = document.getString("LEVEL");

                                    /* OPENS CORRECT ACTIVITY A.T. TO LEVEL OF USER */
                                    switch (level) {

                                        /* FOR FOUNDERS */
                                        case "0":

                                            /* INITIALIZING HC MAKER ID */
                                            HCM_ID = USER_NO;

                                            /* INITIALIZING HC MAKER NAME */
                                            HCM_NAME = document.getString("NAME");

                                            intent = new Intent(LoginActivity.this,
                                                    FoundersActivity.class);
                                            startActivity(intent);
                                            break;

                                        /* FOR HC MAKERS */
                                        case "1":

                                            /* INITIALIZING HC MAKER ID */
                                            HCM_ID = USER_NO;

                                            /* INITIALIZING HC MAKER NAME */
                                            HCM_NAME = document.getString("NAME");

                                            intent = new Intent(LoginActivity.this,
                                                    HC_MakersActivity.class);
                                            startActivity(intent);
                                            break;

                                        /* FOR HC USERS */
                                        case "2":
                                            intent = new Intent(LoginActivity.this,
                                                    HC_OwnersActivity.class);
                                            startActivity(intent);
                                            break;
                                    }

                                    Toast.makeText(LoginActivity.this, "Welcome " + user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                                } else {
                                    /* USER NOT FOUND IN DB */
                                    Toast.makeText(LoginActivity.this,
                                            "PLEASE CONTACT AMBER NGO", Toast.LENGTH_LONG).show();
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("NOT ALLOWED", "User account deleted.");
                                                    }
                                                }
                                            });
                                }


                            } else {
                                /* USER NOT FOUND IN DB */
                                Toast.makeText(LoginActivity.this,
                                        "PLEASE CONTACT AMBER NGO", Toast.LENGTH_LONG).show();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("NOT ALLOWED", "User account deleted.");
                                                }
                                            }
                                        });
                            }
                        } else {
                            /* SIGN IN DIDN'T WORKED */
                            Toast.makeText(LoginActivity.this,
                                    "PLEASE CONTACT AMBER NGO", Toast.LENGTH_LONG).show();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("NOT ALLOWED", "User account deleted.");
                                            }
                                        }
                                    });
                            Log.d("onAuthStateChanged", "get failed with ", task.getException());
                        }
                    }
                });
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /* Attach Listener on activity running */
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    /* Detach Listener on activity not running */
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

}
