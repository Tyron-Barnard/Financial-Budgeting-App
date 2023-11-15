package com.example.financebudgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Questionaire extends AppCompatActivity {



    private int maxList = 0;
    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quesrtionnaire_page);

        String userEmail = getIntent().getStringExtra("userEmail");

        //editText average salary
        EditText edtAverageSalary = findViewById(R.id.edtAverageSalary);
        LinearLayout main_container = findViewById(R.id.main_container);

        //Emergency Fund components
        RadioButton rbEmergencyFundYes = (RadioButton) findViewById(R.id.rbEmergencyYes);
        RadioButton rbEmergencyFundNo = (RadioButton) findViewById(R.id.rbEmergencyNo);
        LinearLayout llEmergencyFund = (LinearLayout) findViewById(R.id.llEmergencyFund);
        EditText edtEmergencyFund = findViewById(R.id.edtEmergencyFund);

        //setting Emergency fund components visibility based on radio button inputs
        rbEmergencyFundYes.setOnClickListener(v -> {
            if (rbEmergencyFundYes.isChecked()) {
                llEmergencyFund.setVisibility(View.VISIBLE);
            }
        });


        rbEmergencyFundNo.setOnClickListener(v -> {
            if (rbEmergencyFundNo.isChecked()){
                llEmergencyFund.setVisibility(View.GONE);
            }
        });


        //Retirement Fund Components
        RadioButton rbRetirementFundYes = (RadioButton) findViewById(R.id.rbRetirementYes);
        RadioButton rbRetirementFundNo = (RadioButton) findViewById(R.id.rbRetirementNo);
        LinearLayout llRetirementFund = (LinearLayout) findViewById(R.id.llRetirementFund);
        EditText edtRetirementFund = findViewById(R.id.edtRetirementFund);

        //setting Retirement fund components visibility based on radio button inputs

        rbRetirementFundYes.setOnClickListener(v -> {
                if (rbRetirementFundYes.isChecked()) {
                    llRetirementFund.setVisibility(View.VISIBLE);
                }
            });


        rbRetirementFundNo.setOnClickListener(v -> {
            if (rbRetirementFundNo.isChecked()){
                llRetirementFund.setVisibility(View.GONE);
            }
        });



        //initializing initial spinner components
        Spinner edtInitialNeedName = findViewById(R.id.edtInitialNeedName);
        String[] spNeedsListItems = new String[]{
                "Rent", "Utilities", "Food", "Transport", "Clothing", "Debt", "Other"
        };
        ArrayAdapter<String> adapterTest = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spNeedsListItems);
        adapterTest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtInitialNeedName.setAdapter(adapterTest);


        Spinner spWantName = findViewById(R.id.spWantName);
        String[] spWantsListItems = new String[]{
                "Entertainment", "Eating Out", "Travel", "Hobbies", "Clothing", "Other"
        };
        ArrayAdapter<String> adapterWants = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spWantsListItems);
        adapterWants.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWantName.setAdapter(adapterWants);




        //adding 'need' fields
        ImageButton ibAddNeed = findViewById(R.id.ibAddNeed);
        LinearLayout llNeedsList = findViewById(R.id.llNeedsList);
        ibAddNeed.setOnClickListener(v -> {

            llNeedsList.addView(createNeedComponents());

        });


        //want components
        LinearLayout llWantsList = findViewById(R.id.llWantsList);
        ImageButton ibAddWant = findViewById(R.id.ibAddWant);
        ibAddWant.setOnClickListener(v -> {

            llWantsList.addView(createWantComponents());

        });

        LinearLayout llInvestmentList = findViewById(R.id.llInvestmentList);
        ImageButton ibAddInvestment = findViewById(R.id.ibAddInvestment);
        ibAddInvestment.setOnClickListener(v ->{
            llInvestmentList.addView(createInvestmentComponent());
        });


        DatabaseController dbHelper = new DatabaseController(getApplicationContext());
        Button btnSubmit = findViewById(R.id.btnQuestionnaireSubmit);
        btnSubmit.setOnClickListener(v -> {
            boolean validCredentials = false;

            //input validation
            //EditText AverageSalary
            if (edtAverageSalary.getText().toString().isEmpty()){
                edtAverageSalary.setBackground(getDrawable(R.drawable.edit_validation_background));
            }else{
                validCredentials = true;
                double AverageSalary = Double.parseDouble(edtAverageSalary.getText().toString());
                dbHelper.insert(userEmail, "Salary", AverageSalary, "income");
            }
            edtAverageSalary.setOnClickListener(edtAverageSalaryListener -> {
                edtAverageSalary.setBackground(getDrawable(R.drawable.edit_text_background));
            });

            //Validating Retirement Fund Radio Buttons
            if (!rbRetirementFundNo.isChecked() && !rbRetirementFundYes.isChecked()){
                rbRetirementFundYes.setButtonTintList(getColorStateList(R.color.red));
                rbRetirementFundNo.setButtonTintList(getColorStateList(R.color.red));
            }
            rbRetirementFundNo.setOnClickListener(rbRetirementNoListener -> {
                rbRetirementFundNo.setButtonTintList(getColorStateList(R.color.text));
                rbRetirementFundYes.setButtonTintList(getColorStateList(R.color.text));
                llRetirementFund.setVisibility(View.GONE);
            });
            rbRetirementFundYes.setOnClickListener(rbRetirementNoListener -> {
                rbRetirementFundNo.setButtonTintList(getColorStateList(R.color.text));
                rbRetirementFundYes.setButtonTintList(getColorStateList(R.color.text));
                llRetirementFund.setVisibility(View.VISIBLE);
            });


            //Validating Emergency Fund Radio Buttons
            if (!rbEmergencyFundNo.isChecked() && !rbEmergencyFundYes.isChecked()){
                rbEmergencyFundNo.setButtonTintList(getColorStateList(R.color.red));
                rbEmergencyFundYes.setButtonTintList(getColorStateList(R.color.red));
            }
            rbEmergencyFundNo.setOnClickListener(rbEmergencyNoListener -> {
                rbEmergencyFundNo.setButtonTintList(getColorStateList(R.color.text));
                rbEmergencyFundYes.setButtonTintList(getColorStateList(R.color.text));
                llEmergencyFund.setVisibility(View.GONE);
            });
            rbEmergencyFundYes.setOnClickListener(rbEmergencyNoListener -> {
                rbEmergencyFundNo.setButtonTintList(getColorStateList(R.color.text));
                rbEmergencyFundYes.setButtonTintList(getColorStateList(R.color.text));
                llEmergencyFund.setVisibility(View.VISIBLE);
            });

            //Emergency fund EditText
            if (!edtEmergencyFund.getText().toString().equals("")) {
                double EmergencyFund = Double.parseDouble(edtEmergencyFund.getText().toString());
                dbHelper.insert(userEmail, "Emergency Fund", EmergencyFund, "Emergency Fund");
            }else{
                edtEmergencyFund.setBackground(getDrawable(R.drawable.edit_validation_background));
            }

            //Retirement fund EditText
            if (!edtRetirementFund.getText().toString().isEmpty() && rbRetirementFundYes.isChecked()) {
                double RetirementFund = Double.parseDouble(edtRetirementFund.getText().toString());
                dbHelper.insert(userEmail, "Retirement Fund", RetirementFund, "Retirement Fund");
            }else{
                edtEmergencyFund.setBackground(getDrawable(R.drawable.edit_validation_background));
            }

            String edtNeedItem = null;
            String spNeedItem = null;
            for (int i = 0; i < llNeedsList.getChildCount(); i++){
                LinearLayout child = (LinearLayout) llNeedsList.getChildAt(i);
                for (int a = 0; a < child.getChildCount(); a++){
                    if (child.getChildAt(a) instanceof EditText){
                        EditText subChild = (EditText) child.getChildAt(a);
                        edtNeedItem = subChild.getText().toString();
                    }
                    if (child.getChildAt(a) instanceof Spinner){
                        Spinner subChild = (Spinner) child.getChildAt(a);
                        spNeedItem = subChild.getSelectedItem().toString();
                    }
                    if (child.getChildAt(a) instanceof LinearLayout){
                        Spinner subChild = (Spinner) ((LinearLayout) child.getChildAt(a)).getChildAt(0);
                        spNeedItem = subChild.getSelectedItem().toString();
                    }
                }
                dbHelper.insert(userEmail, spNeedItem, Double.parseDouble(edtNeedItem), "need");
            }

            //inserting all want component values into database
            String edtWantItem = null;
            String spWantItem = null;
            for (int i = 0; i < llWantsList.getChildCount(); i++){
                LinearLayout child = (LinearLayout) llWantsList.getChildAt(i);
                for (int a = 0; a < child.getChildCount(); a++){
                    if (child.getChildAt(a) instanceof EditText){
                        EditText subChild = (EditText) child.getChildAt(a);
                        edtWantItem = subChild.getText().toString();
                    }
                    if (child.getChildAt(a) instanceof Spinner){
                        Spinner subChild = (Spinner) child.getChildAt(a);
                        spWantItem = subChild.getSelectedItem().toString();
                    }
                    if (child.getChildAt(a) instanceof LinearLayout){
                        Spinner subChild = (Spinner) ((LinearLayout) child.getChildAt(a)).getChildAt(0);
                        spWantItem = subChild.getSelectedItem().toString();
                    }
                }
                dbHelper.insert(userEmail, spWantItem, Double.parseDouble(edtWantItem), "want");
            }

            //inserting all investment component values into database
            String edtInvestmentName = null;
            String edtInvestmentAmount = null;
            for (int i = 0; i < llInvestmentList.getChildCount(); i++){
                LinearLayout child = (LinearLayout) llInvestmentList.getChildAt(i);
                for (int a = 0; a < child.getChildCount(); a++){
                    if (child.getChildAt(0) instanceof EditText){
                        EditText subChild = (EditText) child.getChildAt(0);
                        edtInvestmentName = subChild.getText().toString();
                    }
                    if (child.getChildAt(1) instanceof EditText){
                        EditText subChild = (EditText) child.getChildAt(1);
                        edtInvestmentAmount = subChild.getText().toString();
                    }

                }
                dbHelper.insert(userEmail, edtInvestmentName, Double.parseDouble(edtInvestmentAmount), "investment");
            }


            if (validCredentials) {
                Intent intent = new Intent(this, OverviewPage.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Required Field is Empty", Toast.LENGTH_SHORT).show();
            }

        });

    }


    private int dpToPx(int dp) {
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private LinearLayout createNeedComponents(){
        //drawable for edit component
        @SuppressLint("UseCompatLoadingForDrawables") Drawable edit_backkground = getResources().getDrawable(R.drawable.edit_text_background);
        int color = getApplicationContext().getColor(R.color.text);

        LinearLayout llNeed = new LinearLayout(getApplicationContext());
        llNeed.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llNeed.setPadding(0, 0, 10, 0);

        //spinner for adding need
        LinearLayout spinnerContainer = new LinearLayout(this);
        LinearLayout.LayoutParams needContainerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        needContainerLayoutParams.setMargins(0, dpToPx(5), dpToPx(10), 0);
        spinnerContainer.setLayoutParams(needContainerLayoutParams);


        Spinner spNeedList = new Spinner(getApplicationContext());
        LinearLayout.LayoutParams spNeedParams = new LinearLayout.LayoutParams(dpToPx(110), dpToPx(48));
        spNeedList.setLayoutParams(spNeedParams);
        spNeedList.setPadding(dpToPx(10), 0, 0, 0);
        spNeedList.setBackground(edit_backkground);

        spinnerContainer.addView(spNeedList);

        //initializing spinner component
        String[] spinnerContent = new String[]{
                "Rent", "Utilities", "Food", "Transport", "Clothing", "Debt", "Other"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerContent);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNeedList.setAdapter(adapter);


        //EditText for adding want amount
        EditText edtNeedAmount = new EditText(getApplicationContext());
        LinearLayout.LayoutParams editNeedAmountLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editNeedAmountLayoutParams.setMargins(0, dpToPx(5), dpToPx(10), 0);
        edtNeedAmount.setLayoutParams(editNeedAmountLayoutParams);
        edtNeedAmount.setWidth(dpToPx(110));
        edtNeedAmount.setHeight(dpToPx(48));
        edtNeedAmount.setPadding(dpToPx(10), 0, 0, 0);
        edtNeedAmount.setBackground(edit_backkground);
        edtNeedAmount.setTextColor(color);
        edtNeedAmount.setEllipsize(TextUtils.TruncateAt.END);

        //ImageButton for removing the component
        ImageButton ibRemoveNeed = new ImageButton(this);
        ibRemoveNeed.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ibRemoveNeed.setBackground(null);
        ibRemoveNeed.setImageDrawable(getResources().getDrawable(R.drawable.remove_icon));
        ibRemoveNeed.setForegroundGravity(Gravity.CENTER);

        //Removing components when ibRemove is clicked
        ibRemoveNeed.setOnClickListener(view -> {
            llNeed.removeView(spinnerContainer);
            llNeed.removeView(edtNeedAmount);
            llNeed.removeView(ibRemoveNeed);
        });

        //add component to layout
        llNeed.addView(spinnerContainer);
        llNeed.addView(edtNeedAmount);
        llNeed.addView(ibRemoveNeed);

        return llNeed;
    }

    private LinearLayout createWantComponents(){
        //drawable for edit component
        @SuppressLint("UseCompatLoadingForDrawables") Drawable edit_backkground = getResources().getDrawable(R.drawable.edit_text_background);
        int color = getApplicationContext().getColor(R.color.text);

        LinearLayout llWant = new LinearLayout(getApplicationContext());
        llWant.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llWant.setPadding(0, 0, 10, 0);

        //EditText for adding want
        LinearLayout spinnerWantContainer = new LinearLayout(this);
        LinearLayout.LayoutParams spContainerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        spContainerLayoutParams.setMargins(0, dpToPx(5), dpToPx(10), 0);
        spinnerWantContainer.setLayoutParams(spContainerLayoutParams);


        Spinner spWantList = new Spinner(getApplicationContext());
        LinearLayout.LayoutParams spWantParams = new LinearLayout.LayoutParams(dpToPx(110), dpToPx(48));
        spWantList.setLayoutParams(spWantParams);
        spWantList.setPadding(dpToPx(10), 0, 0, 0);
        spWantList.setBackground(edit_backkground);

        spinnerWantContainer.addView(spWantList);

        //initializing spinner component
        String[] spinnerContent = new String[]{
                "Entertainment", "Eating Out", "Travel", "Hobbies", "Clothing", "Other"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerContent);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWantList.setAdapter(adapter);

        //EditText for adding want amount
        EditText edtWantAmount = new EditText(getApplicationContext());
        LinearLayout.LayoutParams editNeedAmountLayoutParams = new LinearLayout.LayoutParams(dpToPx(110), dpToPx(48));
        editNeedAmountLayoutParams.setMargins(0, dpToPx(5), dpToPx(10), 0);
        edtWantAmount.setLayoutParams(editNeedAmountLayoutParams);
        edtWantAmount.setWidth(dpToPx(110));
        edtWantAmount.setHeight(dpToPx(48));
        edtWantAmount.setPadding(dpToPx(10), 0, 0, 0);
        edtWantAmount.setBackground(edit_backkground);
        edtWantAmount.setTextColor(color);
        edtWantAmount.setEllipsize(TextUtils.TruncateAt.END);
        edtWantAmount.setInputType(InputType.TYPE_CLASS_NUMBER);

        //ImageButton for removing the component
        ImageButton ibRemoveWant = new ImageButton(this);
        ibRemoveWant.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ibRemoveWant.setBackground(null);
        ibRemoveWant.setImageDrawable(getResources().getDrawable(R.drawable.remove_icon));
        ibRemoveWant.setForegroundGravity(Gravity.CENTER);


        //Removing components when ibRemove is clicked
        ibRemoveWant.setOnClickListener(view -> {
            llWant.removeView(spinnerWantContainer);
            llWant.removeView(edtWantAmount);
            llWant.removeView(ibRemoveWant);
        });

        //add component to layout
        llWant.addView(spinnerWantContainer);
        llWant.addView(edtWantAmount);
        llWant.addView(ibRemoveWant);

        return llWant;
    }

    private LinearLayout createInvestmentComponent(){


        //drawable for edit component
        @SuppressLint("UseCompatLoadingForDrawables") Drawable edit_backkground = getResources().getDrawable(R.drawable.edit_text_background);
        int color = getApplicationContext().getColor(R.color.text);

        LinearLayout llInvestment = new LinearLayout(getApplicationContext());
        llInvestment.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llInvestment.setPadding(0, 0, 10, 0);

        //EditText for adding investment name
        EditText edtInvestmentName = new EditText(getApplicationContext());
        LinearLayout.LayoutParams editInvestmentNameLayoutParams = new LinearLayout.LayoutParams(dpToPx(110), dpToPx(48));
        editInvestmentNameLayoutParams.setMargins(0, dpToPx(5), dpToPx(10), 0);
        edtInvestmentName.setLayoutParams(editInvestmentNameLayoutParams);
        //edtInvestmentName.setWidth(dpToPx(110));
        //edtInvestmentName.setHeight(dpToPx(48));
        edtInvestmentName.setPadding(dpToPx(10), 0, 0, 0);
        edtInvestmentName.setBackground(edit_backkground);
        edtInvestmentName.setTextColor(color);
        edtInvestmentName.setEllipsize(TextUtils.TruncateAt.END);


        //EditText for adding Investment amount
        EditText edtInvestmentAmount = new EditText(getApplicationContext());
        LinearLayout.LayoutParams editNeedAmountLayoutParams = new LinearLayout.LayoutParams(dpToPx(110), dpToPx(48));
        editNeedAmountLayoutParams.setMargins(0, dpToPx(5), dpToPx(10), 0);
        edtInvestmentAmount.setLayoutParams(editNeedAmountLayoutParams);
        edtInvestmentAmount.setWidth(dpToPx(110));
        edtInvestmentAmount.setHeight(dpToPx(48));
        edtInvestmentAmount.setPadding(dpToPx(10), 0, 0, 0);
        edtInvestmentAmount.setBackground(edit_backkground);
        edtInvestmentAmount.setTextColor(color);
        edtInvestmentAmount.setEllipsize(TextUtils.TruncateAt.END);

        //ImageButton for removing the component
        ImageButton ibRemoveWant = new ImageButton(this);
        ibRemoveWant.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ibRemoveWant.setBackground(null);
        ibRemoveWant.setImageDrawable(getResources().getDrawable(R.drawable.remove_icon));
        ibRemoveWant.setForegroundGravity(Gravity.CENTER);


        //Removing components when ibRemove is clicked
        ibRemoveWant.setOnClickListener(view -> {
            llInvestment.removeView(edtInvestmentName);
            llInvestment.removeView(edtInvestmentAmount);
            llInvestment.removeView(ibRemoveWant);
        });

        //add component to layout
        llInvestment.addView(edtInvestmentName);
        llInvestment.addView(edtInvestmentAmount);
        llInvestment.addView(ibRemoveWant);

        return llInvestment;
    }

}
