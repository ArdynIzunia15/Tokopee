package com.pee.tokopee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {
    TextInputLayout layoutInputUsername, layoutInputEmail, layoutInputPassword, layoutInputConfirmPassword;
    TextInputEditText inputUsername, inputEmail, inputPassword, inputConfirmPassword;
    MaterialButton btnSignup;
    DatabaseHandler databaseHandler;
    TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Status Bar Customization
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Init
        layoutInputUsername = findViewById(R.id.layoutInputUsername);
        layoutInputEmail = findViewById(R.id.layoutInputEmail);
        layoutInputPassword = findViewById(R.id.layoutInputPassword);
        layoutInputConfirmPassword = findViewById(R.id.layoutInputConfirmPassword);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        databaseHandler = new DatabaseHandler(this);
        btnLogin = findViewById(R.id.btnLogin);

        // Ops
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation
                String username = inputUsername.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String confirmPassword = inputConfirmPassword.getText().toString();
                boolean isValidUsername = false;
                boolean isValidEmail = false;
                boolean isValidPassword = false;
                // Username
                if(username.equals("") || username.equals(null)){
                    layoutInputUsername.setErrorEnabled(true);
                    layoutInputUsername.setError("Username cannot be empty");
                    isValidUsername = false;
                }
                else{
                    layoutInputUsername.setErrorEnabled(false);
                    layoutInputUsername.setError(null);
                    isValidUsername = true;
                }
                // Email
                if(databaseHandler.searchUserByEmail(email) != null){
                    layoutInputEmail.setErrorEnabled(true);
                    layoutInputEmail.setError("Email already registered");
                    isValidEmail = false;
                }
                else{
                    layoutInputEmail.setErrorEnabled(false);
                    layoutInputEmail.setError(null);
                    isValidEmail = true;
                }
                if(email.equals("") || email.equals(null)){
                    layoutInputEmail.setErrorEnabled(true);
                    layoutInputEmail.setError("Email cannot be empty");
                    isValidEmail = false;
                }
                // Password
                if(!confirmPassword.equals(password)){
                    layoutInputConfirmPassword.setErrorEnabled(true);
                    layoutInputConfirmPassword.setError("Password not matching");
                    isValidPassword = false;
                }
                else{
                    layoutInputConfirmPassword.setErrorEnabled(false);
                    layoutInputConfirmPassword.setError(null);
                    isValidPassword = true;
                }
                if(password.equals("") || password.equals(null)){
                    layoutInputPassword.setErrorEnabled(true);
                    layoutInputPassword.setError("Password cannot be empty");
                    isValidPassword = false;
                }
                else{
                    layoutInputPassword.setErrorEnabled(false);
                    layoutInputPassword.setError(null);
                }
                // Finalize
                if(isValidEmail && isValidPassword && isValidUsername){
                    User user = new User(null, username, email, password);
                    if(databaseHandler.insertUser(user) == 1){
                        // Assign new User ID
                        user.setId(databaseHandler.searchUserByEmail(email).getId());

                        Toast.makeText(SignupActivity.this, "Sign up success!", Toast.LENGTH_SHORT).show();

                        finish();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}