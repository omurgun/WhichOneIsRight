package com.omurgun.whichoneisright.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omurgun.whichoneisright.Introduction.PreferenceManager;
import com.omurgun.whichoneisright.Manager.HomeMenuActivity;
import com.omurgun.whichoneisright.R;
import java.util.HashMap;

public class UserSetNameActivity extends AppCompatActivity {

    private Button btnUsernameSave;
    private EditText txtUserName;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_set_name);
        translucentStatusBarFlag();
        init();
        if (new PreferenceManager(this).checkPreference()) {
            goHomeMenu();
        }
        btnUsernameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveUsername();
            }
        });
    }
    private void SaveUsername() {
        username = txtUserName.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this,"Username Cannot Be Empty!",Toast.LENGTH_LONG).show();
        }
        else
        {
            System.out.println("username :\t"+username);

            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        System.out.println("Signed in Anonymously");
                        UserSaveToDatabase();
                        //goHomeMenu();

                    } else {
                        System.out.println("There was an error signing in");
                    }

                }
            });

            new PreferenceManager(this).writePreference();

            goHomeMenu();

        }
    }
    private void init() {
        btnUsernameSave = findViewById(R.id.btnUsernameSave);
        txtUserName = findViewById(R.id.usernametxt);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    private void goHomeMenu() {
        Toast.makeText(UserSetNameActivity.this,"Your account has been successfully created",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(UserSetNameActivity.this, HomeMenuActivity.class);
        startActivity(intent);
        finish();
    }
    private void translucentStatusBarFlag() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void UserSaveToDatabase() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String uid = firebaseUser.getUid();
        System.out.println(firebaseUser.getUid());
        HashMap<String,Object> maxMap = new HashMap<>();
        maxMap.put("Username",username);
        maxMap.put("Uid",uid);
        maxMap.put("MaxScore",0);
        maxMap.put("date", FieldValue.serverTimestamp());
        firebaseFirestore.collection("Users")
                .document(uid).set(maxMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("kaydedildi ");
                Toast.makeText(UserSetNameActivity.this, "save..", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserSetNameActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}