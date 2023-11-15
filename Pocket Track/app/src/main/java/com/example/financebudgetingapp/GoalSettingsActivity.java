package com.example.financebudgetingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GoalSettingsActivity extends AppCompatActivity {
    AppCompatButton btnCancel, btnSubmit;
    ImageView  backButton2;
    EditText editTextTextGoalName, editgoalamount;
    DatabaseController  databaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goalsetting);
        //widgets initialize
        btnCancel = findViewById(R.id.btnCancel);
        btnSubmit = findViewById(R.id.btnSubmit);
        backButton2 = findViewById(R.id.back_button2);
        editTextTextGoalName = findViewById(R.id.editTextTextGoalName);
        editgoalamount = findViewById(R.id.editgoalamount);

        //initialize database
        databaseController=new DatabaseController(this);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String goalName = editTextTextGoalName.getText().toString();
                String goalAmountStr = editgoalamount.getText().toString();

                // Check if EditTexts are empty
                if(goalName.isEmpty() || goalAmountStr.isEmpty()) {
                    Toast.makeText(GoalSettingsActivity.this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
                } else {
                    double goalAmount = Double.parseDouble(goalAmountStr);
                    databaseController.addGoal(new GoalModel(goalName, goalAmount));

                    // Notify the user about the successful operation
                    Toast.makeText(GoalSettingsActivity.this, "Goal has been added successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });


    }
}