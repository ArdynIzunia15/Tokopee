package com.pee.tokopee;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.button.MaterialButton;

public class MenuActivity extends AppCompatActivity {
    MaterialButton btnLogout, btnChangePassword, btnDeleteAccount;
    TextView txtUsername, txtEmail;
    DatabaseHandler databaseHandler;
    ImageView iconBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Status Bar Customization
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.persian_blue));
        }

        // Init
        btnLogout = findViewById(R.id.btnLogout);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        SharedPreferences sharedPreferences = getSharedPreferences("activeUser", MODE_PRIVATE);
        txtUsername.setText(sharedPreferences.getString("username", ""));
        txtEmail.setText(sharedPreferences.getString("email", ""));
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        databaseHandler = new DatabaseHandler(this);
        iconBack = findViewById(R.id.iconBack);

        // Ops
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id", 0);
                editor.putString("username", "");
                editor.putString("email", "");
                editor.commit();
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHandler.deleteUser(sharedPreferences.getInt("id", 0));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id", 0);
                editor.putString("username", "");
                editor.putString("email", "");
                editor.commit();
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_FIRST_USER, resultIntent);
                finish();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}