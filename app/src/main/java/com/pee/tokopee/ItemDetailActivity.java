package com.pee.tokopee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ItemDetailActivity extends AppCompatActivity implements Serializable {
    ImageView imageItem;
    TextView txtItemPrice, txtItemName;
    MaterialButton btnPurchase;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        // Status Bar Customization
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Init
        Item item = (Item) getIntent().getParcelableExtra("item");
        imageItem = findViewById(R.id.imageItem);
        txtItemPrice = findViewById(R.id.txtItemPrice);
        txtItemName = findViewById(R.id.txtItemName);
        btnPurchase = findViewById(R.id.btnPurchase);
        databaseHandler = new DatabaseHandler(this);

        // Ops
        Picasso.get().load(item.getImageUrl()).into(imageItem);
        txtItemName.setText(item.getName());
        txtItemPrice.setText("Rp " + String.format("%,d", item.getPrice()));
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert to database
                SharedPreferences sharedPreferences = getSharedPreferences("activeUser", MODE_PRIVATE);
                Integer id = sharedPreferences.getInt("id", 0);
                databaseHandler.insertHistory(new History(0, id, item, LocalDateTime.now()));

                Intent intent = new Intent(getApplicationContext(), PurchaseSuccessActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}