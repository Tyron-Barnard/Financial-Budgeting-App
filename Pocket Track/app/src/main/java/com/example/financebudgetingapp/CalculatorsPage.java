package com.example.financebudgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CalculatorsPage extends AppCompatActivity {

    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String userEmail = getIntent().getStringExtra("userEmail");

        DatabaseController dbHelper = new DatabaseController(getApplicationContext());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculators_page);

        //all calculators
        LinearLayout ll50_30_20 = findViewById(R.id.ll50_30_20);
        LinearLayout llEmergencyFundCalculator = findViewById(R.id.llEmergencyFundCalculator);
        LinearLayout llCarAffordabilityCalculator = findViewById(R.id.llCarAffordabilityCalculator);
        LinearLayout llHouseAffordabilityCalculator = findViewById(R.id.llHouseAffordabilityCalculator);

        //spinner code for changing calculators
        Spinner spCalculatorType = findViewById(R.id.spCalculatorType);
        String[] spinnerContent = new String[] {
                "50/30/20 Rule", "Emergency Fund", "Car Affordability", "House Affordability"
        };
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerContent);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCalculatorType.setAdapter(adapter);
        spCalculatorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    ll50_30_20.setVisibility(View.VISIBLE);
                }else{
                    ll50_30_20.setVisibility(View.GONE);
                }
                if (position == 1){
                    llEmergencyFundCalculator.setVisibility(View.VISIBLE);
                }else{
                    llEmergencyFundCalculator.setVisibility(View.GONE);
                }
                if (position == 2){
                    llCarAffordabilityCalculator.setVisibility(View.VISIBLE);
                }else{
                    llCarAffordabilityCalculator.setVisibility(View.GONE);
                }
                if (position == 3){
                    llHouseAffordabilityCalculator.setVisibility(View.VISIBLE);
                }else{
                    llHouseAffordabilityCalculator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //50/30/20
        EditText edtGrossIncome = findViewById(R.id.edtGrossIncome);
        Button btnCalculate503020 = findViewById(R.id.btnCalculate503020);
        EditText edtBreakdownNeeds = findViewById(R.id.edtBreakdownNeeds);
        EditText edtBreakdownWants = findViewById(R.id.edtBreakdownWants);
        EditText edtBreakdownSavingInvesting = findViewById(R.id.edtBreakdownSavingInvesting);

        Cursor fiftyThirtyTwentyCursor = dbHelper.getIncomeByEmail(userEmail);
        if (fiftyThirtyTwentyCursor != null && fiftyThirtyTwentyCursor.moveToFirst()) {
            edtGrossIncome.setText(fiftyThirtyTwentyCursor.getString(fiftyThirtyTwentyCursor.getColumnIndex("financeAmount")));
            fiftyThirtyTwentyCursor.close();
        }




        ArrayList<Double> allValues = get503020(Double.parseDouble(edtGrossIncome.getText().toString()));

        if (!edtGrossIncome.getText().toString().isEmpty()){
            edtBreakdownNeeds.setText(Double.toString(allValues.get(0)));
            edtBreakdownWants.setText(Double.toString(allValues.get(1)));
            edtBreakdownSavingInvesting.setText(Double.toString(allValues.get(2)));
        }


        btnCalculate503020.setOnClickListener(v -> {
            String grossIncome = edtGrossIncome.getText().toString();
            if (!grossIncome.isEmpty()){
                edtBreakdownNeeds.setText(Double.toString(Integer.parseInt(grossIncome) * 0.5));
                edtBreakdownWants.setText(Double.toString(Integer.parseInt(grossIncome) * 0.3));
                edtBreakdownSavingInvesting.setText(Double.toString(Integer.parseInt(grossIncome) * 0.2));
            }
        });

        //Emergency Fund
        EditText edtMonthlyIncome = findViewById(R.id.edtMonthlyIncome);
        TextView tvEmergencyFundAmount = findViewById(R.id.tvEmergencyFund);
        Slider sdNumMonths = findViewById(R.id.sdNumMonths);

        Cursor EmergencyFundCursor = dbHelper.getEmergencyFundByEmail(userEmail);
        if (EmergencyFundCursor != null && EmergencyFundCursor.moveToFirst()) {
            double emergencyFund = Double.parseDouble(EmergencyFundCursor.getString(EmergencyFundCursor.getColumnIndex("financeAmount")));
            tvEmergencyFundAmount.setText(Double.toString(emergencyFund));
            EmergencyFundCursor.close();

            Cursor IncomeCursor = dbHelper.getIncomeByEmail(userEmail);
            if(IncomeCursor.moveToFirst()) {
                double incomeAmount = Double.parseDouble(IncomeCursor.getString(IncomeCursor.getColumnIndex("financeAmount")));
                IncomeCursor.close();

                int numMonths = (int) (emergencyFund / incomeAmount);
                sdNumMonths.setValue(numMonths);

                edtMonthlyIncome.setText("R" + incomeAmount);
            }

        }



        sdNumMonths.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if (!edtMonthlyIncome.toString().isEmpty()){
                    double MonthlyIncome = Double.parseDouble(edtMonthlyIncome.getText().toString());
                    tvEmergencyFundAmount.setText("R" + (value * MonthlyIncome));
                }
            }
        });



        //create on change for edt


        //Car affordability calculator
        ImageButton ibAddCarCost = findViewById(R.id.ibAddCarCost);
        LinearLayout llCostContainer = findViewById(R.id.llCostsContainer);
        //llCostContainer.setOrientation(LinearLayout.VERTICAL);


        ibAddCarCost.setOnClickListener(new View.OnClickListener() {

            String[] hintText = {"Insurance", "Maintenance", "Petrol", "Repayments"};
            int hintTextIndex = 0;
            int maxCarComponents = 0;
            @Override
            public void onClick(View v) {
                if (maxCarComponents < 4) {
                    LinearLayout llCarCostContainer = new LinearLayout(getApplicationContext());
                    LinearLayout.LayoutParams llCarCostContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    llCarCostContainerParams.setMargins(0, dpToPx(10), dpToPx(10), 0);
                    llCarCostContainer.setLayoutParams(llCarCostContainerParams);

                    EditText edtCarCost = new EditText(getApplicationContext());
                    edtCarCost.setHint(hintText[hintTextIndex]);
                    edtCarCost.setWidth(dpToPx(200));
                    edtCarCost.setHeight(dpToPx(24));
                    edtCarCost.setPadding(dpToPx(2), dpToPx(2), dpToPx(2), dpToPx(2));
                    edtCarCost.setBackground(getDrawable(R.drawable.edit_text_background));
                    edtCarCost.setTextColor(getColor(R.color.text));
                    edtCarCost.setInputType(InputType.TYPE_CLASS_NUMBER);

                    edtCarCost.setEllipsize(TextUtils.TruncateAt.END);


                    llCarCostContainer.addView(edtCarCost);
                    llCostContainer.addView(llCarCostContainer);

                    hintTextIndex++;
                    maxCarComponents++;
                }
            }
        });

        TextView tvMonthlyCarExpenses = findViewById(R.id.tvMonthlyCarExpenses);
        Button btnCalculateCarAffordability = findViewById(R.id.btnCalculateCarAffordability);
        btnCalculateCarAffordability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double monthlyCarExpenses = sumAllChildren(llCostContainer);
                ;
                tvMonthlyCarExpenses.setText(Double.toString(monthlyCarExpenses));

                //setting monthly budget textview
                TextView tvMonthlyBudget = findViewById(R.id.tvMonthlyBudget);
                double monthlyBudget = getMonthlyBudget(userEmail, dbHelper);
                tvMonthlyBudget.setText(Double.toString(monthlyBudget - monthlyCarExpenses));

                //setting percentage of budget textview
                TextView tvPercentageOfBudget = findViewById(R.id.tvPercentageOfBudget);
                double percentageOfBudget = (monthlyCarExpenses / monthlyBudget) * 100;
                tvPercentageOfBudget.setText(Double.toString(percentageOfBudget));

                setCarConclusion(percentageOfBudget);
            }
        });

        //house affordability calculator
        ImageButton ibAddHouseCost = findViewById(R.id.ibAddHouseCost);
        LinearLayout llHouseCostsContainer = findViewById(R.id.llHouseCostsContainer);
        ibAddHouseCost.setOnClickListener(new View.OnClickListener() {

            String[] hintText = {"Repayments", "Insurance", "Maintenance"};
            int hintTextIndex = 0;
            int maxHouseComponents = 0;
            @Override
            public void onClick(View v) {
                if (maxHouseComponents < 3) {
                    LinearLayout llHomeCostContainer = new LinearLayout(getApplicationContext());
                    LinearLayout.LayoutParams llHouseCostContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    llHouseCostContainerParams.setMargins(0, dpToPx(10), dpToPx(10), 0);
                    llHouseCostsContainer.setLayoutParams(llHouseCostContainerParams);

                    EditText edtHouseCost = new EditText(getApplicationContext());
                    edtHouseCost.setHint(hintText[hintTextIndex]);
                    edtHouseCost.setWidth(dpToPx(200));
                    edtHouseCost.setHeight(dpToPx(24));
                    edtHouseCost.setPadding(dpToPx(2), dpToPx(2), dpToPx(2), dpToPx(2));
                    edtHouseCost.setBackground(getDrawable(R.drawable.edit_text_background));
                    edtHouseCost.setTextColor(getColor(R.color.text));
                    edtHouseCost.setInputType(InputType.TYPE_CLASS_NUMBER);

                    edtHouseCost.setEllipsize(TextUtils.TruncateAt.END);


                    llHouseCostsContainer.addView(edtHouseCost);
                    llHomeCostContainer.addView(llHouseCostsContainer);

                    hintTextIndex++;
                    maxHouseComponents++;
                }
            }
        });

    }

    private int dpToPx(int dp) {
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private ArrayList<Double> get503020(double grossIncome){
        ArrayList<Double> values = new ArrayList<>();
        values.add(grossIncome * 0.5);
        values.add(grossIncome * 0.3);
        values.add(grossIncome * 0.2);
        return values;
    }

    @SuppressLint("Range")
    private double getMonthlyBudget(String userEmail, DatabaseController dbHelper){
        double totalExpenses = 0;
        Cursor needsCursor = dbHelper.getNeedsByEmail(userEmail);
        while (needsCursor.moveToNext()){
            @SuppressLint("Range") double needsAmount = Double.parseDouble(needsCursor.getString(needsCursor.getColumnIndex("financeAmount")));
            totalExpenses += needsAmount;
        }
        needsCursor.close();

        Cursor wantsCursor = dbHelper.getNeedsByEmail(userEmail);
        while (needsCursor.moveToNext()){
            @SuppressLint("Range") double wantsAmount = Double.parseDouble(wantsCursor.getString(wantsCursor.getColumnIndex("financeAmount")));
            totalExpenses += wantsAmount;
        }
        wantsCursor.close();

        double totalIncome = 0;
        Cursor incomeCursor = dbHelper.getIncomeByEmail(userEmail);
        if (incomeCursor.moveToFirst()){
            totalIncome = Double.parseDouble(incomeCursor.getString(incomeCursor.getColumnIndex("financeAmount")));
        }

        return totalIncome - totalExpenses;
    }

    private void setCarConclusion(double percentageOfBudget){
        TextView tvCarConclusion = findViewById(R.id.tvCarConclusion);
        if (percentageOfBudget < 20){
            tvCarConclusion.setText("Can Afford");
            tvCarConclusion.setTextColor(getColor(R.color.green));
        }else if ((percentageOfBudget > 20) && (percentageOfBudget < 30)){
            tvCarConclusion.setText("Not Recommended");
            tvCarConclusion.setTextColor(getColor(R.color.yellow));
        }else{
            tvCarConclusion.setText("Cannot Afford");
            tvCarConclusion.setTextColor(getColor(R.color.red));
        }
    }

    private double sumAllChildren(LinearLayout container){

        double sumAllValue = 0;

        for (int i = 0; i < container.getChildCount(); i++){
            if (container.getChildAt(i) instanceof EditText) {
                EditText childEdit = (EditText) container.getChildAt(i);
                if (!childEdit.getText().toString().isEmpty()) {
                    sumAllValue += Double.parseDouble(childEdit.getText().toString());
                }
            }
            if (container.getChildAt(i) instanceof  LinearLayout){
                LinearLayout childLayout = (LinearLayout) container.getChildAt(i);
                for (int a = 0; a < childLayout.getChildCount(); a ++){
                    if (childLayout.getChildAt(a) instanceof EditText){
                        EditText subChildEdit = (EditText) container.getChildAt(a);
                        if (!subChildEdit.getText().toString().isEmpty()){
                            sumAllValue += Double.parseDouble(subChildEdit.getText().toString());
                        }
                    }
                }
            }
        }


        return sumAllValue;
    }

}