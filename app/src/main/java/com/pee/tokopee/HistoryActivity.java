package com.pee.tokopee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ArrayList<History> arrHistory;
    RecyclerView recyclerviewHistory;
    HistoryRecyclerViewAdapter adapter;
    DatabaseHandler databaseHandler;
    ImageView iconBack, iconMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Status Bar Customization
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.persian_blue));
        }

        // Init
        SharedPreferences sharedPreferences = getSharedPreferences("activeUser", MODE_PRIVATE);
        Integer id = sharedPreferences.getInt("id", 0);
        databaseHandler = new DatabaseHandler(this);
        arrHistory = databaseHandler.getHistory(id);
        // Recycler Things
        adapter = new HistoryRecyclerViewAdapter(this, arrHistory);
        recyclerviewHistory = findViewById(R.id.recyclerviewHistory);
        recyclerviewHistory.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerviewHistory.setAdapter(adapter);
        // Components
        iconBack = findViewById(R.id.iconBack);
        iconMenu = findViewById(R.id.iconMenu);

        // Ops
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                if(!isLoggedIn()){
                    finish();
                }
            }
        });
    }
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = getSharedPreferences("activeUser", MODE_PRIVATE);
        sharedPreferences.getInt("id", 0);
        sharedPreferences.getString("username", "");
        sharedPreferences.getString("email", "");
        if(sharedPreferences.getInt("id", 0) == 0 || sharedPreferences.getString("username", "").equals("") || sharedPreferences.getString("email", "").equals("")){
            return false;
        }
        return true;
    }
}