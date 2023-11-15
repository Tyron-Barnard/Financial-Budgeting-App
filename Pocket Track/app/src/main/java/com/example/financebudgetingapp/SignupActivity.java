package com.example.financebudgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.financebudgetingapp.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        databaseHelper = new DatabaseHelper(this);

        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPassword = findViewById(R.id.edtPassword);
        EditText edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        TextView loginText = findViewById(R.id.loginText);

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        Button buttonSignup = findViewById(R.id.buttonSignup);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();

                if (email.equals("") || password.equals("") || confirmPassword.equals(""))
                    Toast.makeText(SignupActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                else {
                    if (!email.matches(emailRegex))
                        Toast.makeText(SignupActivity.this, "Email Format is Invalid", Toast.LENGTH_SHORT).show();
                    else {
                        if (password.length() < 8)
                            Toast.makeText(SignupActivity.this, "Password has to be 8 Characters or Longer", Toast.LENGTH_SHORT).show();
                        else {
                            if (password.equals(confirmPassword)) {
                                Boolean checkUserEmail = databaseHelper.checkEmail(email);

                                if (checkUserEmail == false) {
                                    Boolean insert = databaseHelper.insert(email, password);

                                    if (insert == true) {
                                        Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Questionaire.class);
                                        intent.putExtra("userEmail", edtEmail.getText().toString());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(SignupActivity.this, "User already exists, Please Login", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignupActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}