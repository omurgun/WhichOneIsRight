package com.omurgun.whichoneisright.Game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.omurgun.whichoneisright.Model.User;
import com.omurgun.whichoneisright.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class ShowMaxScoresActivity extends AppCompatActivity {

    private ListView listView;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_max_scores);
        init();
        translucentStatusBarFlag();
        getDataFromFireStore();
    }
    private void init() {
        listView = findViewById(R.id.listView);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    private void getDataFromFireStore() {
        final ArrayList<User> usersMaxScoreSort = new ArrayList<>();
        firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null)
                {
                    Toast.makeText(ShowMaxScoresActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT);
                }
                if(queryDocumentSnapshots !=null)
                {
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments())
                    {
                        Map<String,Object> data = snapshot.getData();
                        String username = data.get("Username").toString();
                        String maxScore = data.get("MaxScore").toString();
                        usersMaxScoreSort.add(new User(username,maxScore));

                    }
                }
                if(!usersMaxScoreSort.isEmpty())
                {
                    Collections.sort(usersMaxScoreSort);
                }
                int i=0;
                String userNames[] = new String[usersMaxScoreSort.size()];
                String scores[] = new String[usersMaxScoreSort.size()];
                String numbers[] = new String[usersMaxScoreSort.size()];
                for (User user : usersMaxScoreSort)
                {
                    i++;
                    userNames[i-1] =user.getUsername();
                    scores[i-1] = user.getMaxScore();
                    numbers[i-1] = String.valueOf(i);
                }
                showGetData(userNames,scores,numbers);
            }
        });
    }
    private void showGetData(String username[], String score[], String i[]) {
        MyAdapter adapter = new MyAdapter(this, username, score ,i);
        listView.setAdapter(adapter);
    }
    private class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String username[];
        String score[];
        String number[];

        MyAdapter (Context c, String username[], String score[], String i[]) {
            super(c, R.layout.row, R.id.txt_username,username);
            this.context = c;
            this.username = username;
            this.score = score;
            this.number = i;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            Button btnNumber = row.findViewById(R.id.btn_number);
            TextView myUsername = row.findViewById(R.id.txt_username);
            TextView myScore = row.findViewById(R.id.txt_score);
            Drawable drawable_one = getResources().getDrawable(R.drawable.medal_one);
            Drawable drawable_two = getResources().getDrawable(R.drawable.medal_two);
            Drawable drawable_three = getResources().getDrawable(R.drawable.medal_three);

            if(number[position].equals("1"))
            {
                btnNumber.setBackground(drawable_one);
            }
            else if(number[position].equals("2"))
            {
                btnNumber.setBackground(drawable_two);
            }
            else if(number[position].equals("3"))
            {
                btnNumber.setBackground(drawable_three);
            }
            else
            {
                btnNumber.setText(number[position]);
            }

            myUsername.setText(username[position]);
            myScore.setText(score[position]);
            return row;
        }
    }
    private void translucentStatusBarFlag() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}