package com.example.financebudgetingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PrimaryAdapter extends RecyclerView.Adapter<PrimaryAdapter.PrimaryViewHolder> {

    private LayoutInflater layoutInflater;
    private Activity activity;
    private ArrayList<GoalModel> dataModelArrayList;
    private DatabaseController databaseController;

    public PrimaryAdapter(Activity activity, ArrayList<GoalModel> dataModelArrayList) {
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.dataModelArrayList = dataModelArrayList;
        this.databaseController = new DatabaseController(activity);
    }

    @NonNull
    @Override
    public PrimaryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.layout_recyclerview, viewGroup, false);
        return new PrimaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrimaryViewHolder primaryViewHolder, int i) {
        GoalModel goal = dataModelArrayList.get(i);
        primaryViewHolder.tvTitle.setText(goal.getGoalName());
        primaryViewHolder.tvPerc.setText(String.valueOf(goal.getGoalAmount()));

        double progress = (goal.getCurrentAmount() / 200000.0) * 100;
        primaryViewHolder.progress_bar.setProgress((int) progress);

        primaryViewHolder.constraint.setOnClickListener(v -> createAlertDialog(i, primaryViewHolder));
    }

    @Override
    public int getItemCount() {
        if (dataModelArrayList != null) {
            return dataModelArrayList.size();
        } else {
            return 0;
        }
    }

    private void createAlertDialog(int position, PrimaryViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_edit_goal, null);

        EditText editTextGoalAmount = view.findViewById(R.id.editgoalamount);

        GoalModel goal = dataModelArrayList.get(position);
        editTextGoalAmount.setText(String.valueOf(goal.getGoalAmount()));

        AppCompatButton btnSubmit = view.findViewById(R.id.btnSubmit);
        AppCompatButton btnCancel = view.findViewById(R.id.btnCancel);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnSubmit.setOnClickListener(v -> {
            double goalAmount = Double.parseDouble(editTextGoalAmount.getText().toString());
            double progress = (goalAmount / 20000.0) * 100;

            GoalModel updatedGoal = new GoalModel(goal.getGoalName(), goalAmount);
            databaseController.updateGoal(updatedGoal);
            dataModelArrayList.set(position, updatedGoal);
            notifyItemChanged(position);

            viewHolder.progress_bar.setProgress((int) progress);

            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    static class PrimaryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPerc;
        ProgressBar progress_bar;
        ConstraintLayout constraint;

        public PrimaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPerc = itemView.findViewById(R.id.tvPerc);
            progress_bar = itemView.findViewById(R.id.progress_bar);
            constraint = itemView.findViewById(R.id.constraint);
        }
    }
}
