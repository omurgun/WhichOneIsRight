package com.omurgun.whichoneisright.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omurgun.whichoneisright.R;
import java.util.HashMap;
import java.util.Map;

public class ChangeUsernameSettingActivity extends AppCompatActivity {
    private Button btnUsernameSave;
    private EditText txtUserName;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username_setting);
        translucentStatusBarFlag();
        init();
        usernameGetFromDatabase();
    }
    private void usernameGetFromDatabase() {
        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {

                        Map<String,Object> map = document.getData();
                        username = map.get("Username").toString();
                        txtUserName.setText(username);
                    }
                    else
                    {
                        System.out.println("No such document");
                    }
                }
                else
                {
                    System.out.println("get failed with "+ task.getException());
                }
            }
        });
        btnUsernameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUserName != null)
                {
                    userUpdateToDatabase();
                }
            }
        });
    }
    private void init() {
        btnUsernameSave = findViewById(R.id.btnUsernameUpdate);
        txtUserName = findViewById(R.id.txt_username);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    private void userUpdateToDatabase(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        username = txtUserName.getText().toString();
        HashMap<String,Object> maxMap = new HashMap<>();
        maxMap.put("Username",username);
        firebaseFirestore.collection("Users")
                .document(uid).update(maxMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ChangeUsernameSettingActivity.this, "updating..", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangeUsernameSettingActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void translucentStatusBarFlag() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}