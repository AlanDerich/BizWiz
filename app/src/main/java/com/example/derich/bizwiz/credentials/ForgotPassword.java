package com.example.derich.bizwiz.credentials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.helper.InputValidation;
import com.example.derich.bizwiz.sales.CustomAdapter;
import com.example.derich.bizwiz.sql.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Created by delaroy on 3/3/18.
 */

public class ForgotPassword extends AppCompatActivity {

    private TextInputEditText textInputEditTextUsername,textInputEditTextAnswer;
    private AppCompatButton appCompatButtonConfirm;
    private InputValidation inputValidation;
    Spinner mSpinnerQuestions;
    private DatabaseHelper databaseHelper;
    private NestedScrollView nestedScrollView;
    String[] questions = {"What is your home city?", "What is the name of your pet?", "Who is your best friend?", "What is your nickname?","How old are you?"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        textInputEditTextUsername = findViewById(R.id.textInputEditTextUsername);
        appCompatButtonConfirm = findViewById(R.id.appCompatButtonConfirm);
        textInputEditTextAnswer = findViewById(R.id.textInputEditTextAnswer);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        databaseHelper = new DatabaseHelper(this);
        mSpinnerQuestions= findViewById(R.id.spinnerForgotPass);
        inputValidation = new InputValidation(this);
        CustomAdapter customAdapter= new CustomAdapter(ForgotPassword.this, questions);
        mSpinnerQuestions.setAdapter(customAdapter);
        setTitle("Recover password");
        appCompatButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyFromSQLite();
            }
        });

    }

    private void verifyFromSQLite(){

        if (textInputEditTextUsername.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill your username", Toast.LENGTH_SHORT).show();
            return;
        }
        String question = mSpinnerQuestions.getSelectedItem().toString();
        if (databaseHelper.checkUserPassword(textInputEditTextUsername.getText().toString().trim(), question, textInputEditTextAnswer.getText().toString().trim())) {
            Intent accountsIntent = new Intent(this, ConfirmPassword.class);
            accountsIntent.putExtra("USERNAME", textInputEditTextUsername.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        } else {
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_username), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText(){
        textInputEditTextUsername.setText("");
        textInputEditTextAnswer.setText("");
    }
}