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

public class TertiaryAdapter extends RecyclerView.Adapter<TertiaryAdapter.TertiaryViewHolder> {

    private LayoutInflater layoutInflater;
    private Activity activity;
    private ArrayList<GoalModel> dataModelArrayList;
    private DatabaseController databaseController;

    public TertiaryAdapter(Activity activity, ArrayList<GoalModel> dataModelArrayList,
                           DatabaseController databaseController) {
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.dataModelArrayList = dataModelArrayList;
        this.databaseController = databaseController;
    }

    public TertiaryAdapter(GoalsPage activity, ArrayList<GoalModel> allGoals) {
    }

    @NonNull
    @Override
    public TertiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_recyclerview3, parent, false);
        return new TertiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TertiaryViewHolder holder, final int position) {
        GoalModel goalModel = dataModelArrayList.get(position);
        holder.tvTitle.setText(goalModel.getGoalName());
        holder.tvPerc.setText(String.valueOf(goalModel.getGoalAmount()));
        holder.progress_bar.setProgress(50, true);
        holder.constraint.setOnClickListener(view -> createAlertDialog(position));
    }

    @Override
    public int getItemCount() {
        if (dataModelArrayList != null) {
            return dataModelArrayList.size();
        } else {
            return 0;
        }
    }

    private void createAlertDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_edit_goal, null);

        EditText editTextGoalName = view.findViewById(R.id.editTextTextGoalName);
        EditText editTextGoalAmount = view.findViewById(R.id.editgoalamount);

        GoalModel goalModel = dataModelArrayList.get(position);
        editTextGoalName.setText(goalModel.getGoalName());
        editTextGoalAmount.setText(String.valueOf(goalModel.getGoalAmount()));

        AppCompatButton btnSubmit = view.findViewById(R.id.btnSubmit);
        AppCompatButton btnCancel = view.findViewById(R.id.btnCancel);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        btnSubmit.setOnClickListener(v -> {
            String goalName = editTextGoalName.getText().toString();
            double goalAmount = Double.parseDouble(editTextGoalAmount.getText().toString());

            GoalModel updatedGoal = new GoalModel(goalName, goalAmount);
            databaseController.updateGoal(updatedGoal);
            dataModelArrayList.set(position, updatedGoal);
            notifyItemChanged(position);

            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    static class TertiaryViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvPerc;
        ProgressBar progress_bar;
        ConstraintLayout constraint;

        public TertiaryViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle_tertiary);
            tvPerc = itemView.findViewById(R.id.tvPerc_tertiary);
            progress_bar = itemView.findViewById(R.id.progress_bar_tertiary);
            constraint = itemView.findViewById(R.id.constraint_tertiary);
        }
    }
}
