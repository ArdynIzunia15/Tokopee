package com.pee.tokopee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputEditText inputOldPassword, inputNewPassword, inputConfirmNewPassword;
    TextInputLayout layoutInputOldPassword, layoutInputNewPassword, layoutInputConfirmNewPassword;
    MaterialButton btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        // Status Bar Customization
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Init
        inputOldPassword = findViewById(R.id.inputOldPassword);
        inputNewPassword = findViewById(R.id.inputNewPassword);
        inputConfirmNewPassword = findViewById(R.id.inputConfirmNewPassword);
        layoutInputOldPassword = findViewById(R.id.layoutInputOldPassword);
        layoutInputNewPassword = findViewById(R.id.layoutInputNewPassword);
        layoutInputConfirmNewPassword = findViewById(R.id.layoutInputConfirmNewPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        SharedPreferences sharedPreferences = getSharedPreferences("activeUser", MODE_PRIVATE);
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        String oldPassword = databaseHandler.searchUserByEmail(sharedPreferences.getString("email", "")).getPassword();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ops
                boolean isValidOldPass = false;
                boolean isValidNewPass = false;
                boolean isValidConfirmNewPass = false;
                // Old Pass
                if(inputOldPassword.getText().toString().equals("")){
                    layoutInputOldPassword.setErrorEnabled(true);
                    layoutInputOldPassword.setError("Required field");
                    isValidOldPass = false;
                }
                else if(!inputOldPassword.getText().toString().equals(oldPassword)){
                    layoutInputOldPassword.setErrorEnabled(true);
                    layoutInputOldPassword.setError("Password not correct");
                    isValidOldPass = false;
                }
                else{
                    layoutInputOldPassword.setErrorEnabled(false);
                    layoutInputOldPassword.setError(null);
                    isValidOldPass = true;
                }
                // New Pass
                if(inputNewPassword.getText().toString().equals("")){
                    layoutInputNewPassword.setErrorEnabled(true);
                    layoutInputNewPassword.setError("Required field");
                    isValidNewPass = false;
                }
                else if(inputNewPassword.getText().toString().equals(oldPassword)){
                    layoutInputNewPassword.setErrorEnabled(true);
                    layoutInputNewPassword.setError("New password cannot be the same");
                    isValidNewPass = false;
                }
                else{
                    layoutInputNewPassword.setErrorEnabled(false);
                    layoutInputNewPassword.setError(null);
                    isValidNewPass = true;
                }
                // Confirm New Pass
                if(!inputConfirmNewPassword.getText().toString().equals(inputNewPassword.getText().toString())){
                    layoutInputConfirmNewPassword.setErrorEnabled(true);
                    layoutInputConfirmNewPassword.setError("Password not matching");
                    isValidConfirmNewPass = false;
                }
                else{
                    layoutInputConfirmNewPassword.setErrorEnabled(false);
                    layoutInputConfirmNewPassword.setError(null);
                    isValidConfirmNewPass = true;
                }
                // Finalize
                if(isValidOldPass && isValidNewPass && isValidConfirmNewPass){
                    if(databaseHandler.updatePassword(sharedPreferences.getInt("id", 0), inputNewPassword.getText().toString())){
                        Toast.makeText(ChangePasswordActivity.this, "Password changed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(ChangePasswordActivity.this, "Password not changed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}