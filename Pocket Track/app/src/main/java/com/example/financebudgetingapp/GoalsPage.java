package com.example.financebudgetingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class GoalsPage extends AppCompatActivity {

    private RecyclerView primaryRecyclerView;
    private PrimaryAdapter primaryAdapter;
    private RecyclerView secondaryRecyclerView;
    private SecondaryAdapter secondaryAdapter;
    private RecyclerView tertiaryRecyclerView;
    private TertiaryAdapter tertiaryAdapter;
    private DatabaseController databaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals_page);

        ImageButton ibProfileButton = findViewById(R.id.ibProfileButton);
        ImageView btnSupportPage = findViewById(R.id.btnSupportPage);
        ImageView btnHomePage = findViewById(R.id.btnHomePage);
        ImageView addbtn_goals = findViewById(R.id.addbtn_goals);

        ibProfileButton.setOnClickListener(v -> startProfilePage());
        btnSupportPage.setOnClickListener(v -> startSupportPage());
        btnHomePage.setOnClickListener(v -> startOverviewPage());
        addbtn_goals.setOnClickListener(v -> startGoalPage());

        primaryRecyclerView = findViewById(R.id.layout_recyclerview);
        secondaryRecyclerView = findViewById(R.id.layout_recyclerview2);
        tertiaryRecyclerView = findViewById(R.id.layout_recyclerview3);
        databaseController = new DatabaseController(this);

        primaryAdapter = new PrimaryAdapter(this, databaseController.getAllGoals());
        primaryRecyclerView.setAdapter(primaryAdapter);
        primaryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        secondaryAdapter = new SecondaryAdapter(this, databaseController.getAllGoals(), databaseController);
        secondaryRecyclerView.setAdapter(secondaryAdapter);
        secondaryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        tertiaryAdapter = new TertiaryAdapter(this, databaseController.getAllGoals(), databaseController);
        tertiaryRecyclerView.setAdapter(tertiaryAdapter);
        tertiaryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void startGoalPage() {
        startActivity(new Intent(GoalsPage.this, GoalSettingsActivity.class));
    }

    private void startSupportPage() {
        startActivity(new Intent(this, SupportPage.class));
    }

    private void startProfilePage() {
        startActivity(new Intent(this, ProfilePage.class));
    }

    private void startOverviewPage() {
        startActivity(new Intent(this, OverviewPage.class));
    }
}
