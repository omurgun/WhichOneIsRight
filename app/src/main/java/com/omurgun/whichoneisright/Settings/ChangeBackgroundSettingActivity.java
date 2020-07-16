package com.omurgun.whichoneisright.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.omurgun.whichoneisright.R;

public class ChangeBackgroundSettingActivity extends AppCompatActivity {
    private Spinner spinner;
    private SharedPreferences sharedPreferences;
    private MyAdapter adapter;
    private Button btnSave;
    private String[] names = {"back_default","back_0","back_1","back_2","back_3","back_4","back_5","back_6","back_7","back_8","back_9"};
    private int[] backs = {R.drawable.ripple_effect_raise_white,R.drawable.back,R.drawable.back1,R.drawable.back2,R.drawable.back3,R.drawable.back4,R.drawable.back5,R.drawable.back6,R.drawable.back7,R.drawable.back8,R.drawable.back9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        translucentStatusBarFlag();
        init();
        fillInTheSpinner();
    }
    private void fillInTheSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferences.edit().putString("back", names[position]).apply();
                        Toast.makeText(getApplicationContext(),names[position],Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String names[];
        int imageViews[];

        MyAdapter (Context context,String[] names, int[] imageViews) {
            super(context,R.layout.row_backs,R.id.txt_username,names);
            this.context = context;
            this.names = names;
            this.imageViews = imageViews;

        }
        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row_back = inflater.inflate(R.layout.row_backs,null);
            TextView textView = row_back.findViewById(R.id.txt_username);
            ImageView imageView = row_back.findViewById(R.id.imageView);

            textView.setText(names[position]);
            imageView.setImageResource(imageViews[position]);

            return  row_back;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row_back = inflater.inflate(R.layout.row_backs,null);
            TextView textView = row_back.findViewById(R.id.txt_username);
            ImageView imageView = row_back.findViewById(R.id.imageView);

            textView.setText(names[position]);
            imageView.setImageResource(imageViews[position]);

            return  row_back;
        }
    }
    private void init() {
        spinner = findViewById(R.id.spinner_game_back);
        adapter = new MyAdapter(this,names,backs);
        spinner.setAdapter(adapter);
        btnSave = findViewById(R.id.btnSave);
        sharedPreferences = this.getSharedPreferences("com.omurgun.whiconeisright", Context.MODE_PRIVATE);
    }
    private void translucentStatusBarFlag() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}