package com.pee.tokopee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    RecyclerView recyclerviewItem;
    ArrayList<Item> listItem;
    ImageView iconHistory, iconMenu;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // Status Bar Customization
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.persian_blue));
        }

        // Check Active User
        SharedPreferences sharedPreferences = getSharedPreferences("activeUser", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String username = sharedPreferences.getString("username", "");
        Integer id = sharedPreferences.getInt("id", 0);
        if(email.equals("") || username.equals("") || id == 0){
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Init
        databaseHandler = new DatabaseHandler(this);
        sharedPreferences = getSharedPreferences("itemDatabase", MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("isInserted", false)){
            listItem = new ArrayList<Item>();
            listItem.add(new Item(1, "https://cdn.kyou.id/items/95866-herrscher-of-flamescion-theme-windbreaker-jacket-honkai-impact-3rd-smlxlxxlxxxl-size.jpg.webp", "Herrscher of Flamescion Theme Windbreaker Jacket - Honkai Impact 3rd (Size XL)", 1650000, "XL"));
            listItem.add(new Item(2, "https://cdn.kyou.id/items/212343-elysia-herrscher-of-human-ego-series-jacket-size-l-honkai-impact-3rd.jpg.webp", "Elysia Herrscher of Human: Ego Series Jacket (Size L)- Honkai Impact 3rd", 960000, "L"));
            listItem.add(new Item(3, "https://cdn.kyou.id/items/152664-dec-2022-release-durandal-platinum-equinox-coat-black-smlxlxxlxxxl-honkai-imapct-3rd.jpg.webp", "Durandal / Palatinus Equinox Coat Black (Size XXXL) - Honkai Impact 3rd", 1450000, "XXL"));
            listItem.add(new Item(4, "https://cdn.kyou.id/items/206822-elysia-herrscher-of-humanego-because-of-you-black-short-pants-size-smlxlxxlxxxl-honkai-impact-3rd.jpg.webp", "[AniSummer 2023 SALE] Elysia Herrscher of Human:EGO \"Because of You\" Black Short Pants (Size XL) - Honkai Impact 3rd", 600000, "XL"));
            listItem.add(new Item(5, "https://cdn.kyou.id/items/202994-kamisato-ayaka-school-ver-short-pants-xssmlxlxxlxxxl-genshin-impact-x-heytea.jpg.webp", "[Bonus Acrylic Keychain] Kamisato Ayaka School Ver. Short Pants (XS/S/M/L/XL/XXL/XXXL) - Genshin Impact x HeyTea", 630000, "XS/S/M/L/XL/XXL/XXXL"));
            for(int i = 0 ; i < listItem.size() ; i++){
                databaseHandler.insertItem(listItem.get(i));
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isInserted", true);
            editor.apply();
        }
        listItem = databaseHandler.getItems();
        recyclerviewItem = findViewById(R.id.recyclerviewItem);
        iconHistory = findViewById(R.id.iconHistory);
        iconMenu = findViewById(R.id.iconMenu);

        // Ops
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerviewItem.setLayoutManager(gridLayoutManager);
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(this, listItem);
        recyclerviewItem.setAdapter(adapter);
        iconHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
                if(!isLoggedIn()){
                    finish();
                }
            }
        });
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if(!isLoggedIn()){
                finish();
            }
        }
        else if(requestCode == 1 && resultCode == Activity.RESULT_FIRST_USER){
            Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
            if(!isLoggedIn()){
                finish();
            }
        }
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