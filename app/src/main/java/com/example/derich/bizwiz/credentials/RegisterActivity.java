package com.example.derich.bizwiz.credentials;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.helper.InputValidation;
import com.example.derich.bizwiz.model.User;
import com.example.derich.bizwiz.sales.CustomAdapter;
import com.example.derich.bizwiz.sql.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by group 7 CS project on 3/11/18.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutAnswer;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputEditText textInputEditTextName,textInputEditTextEmail,textInputEditTextPassword,textInputEditTextConfirmPassword,textInputEditTextAnswer;
    private AppCompatButton appCompatButtonRegister;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    Spinner mSpinnerQuestions;
    String[] questions = {"What is your home city?", "What is the name of your pet?", "Who is your best friend?", "What is your nickname?","How old are you?"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        mSpinnerQuestions = findViewById(R.id.spinner_register_question);
        initViews();
        initListeners();
        initObjects();
        CustomAdapter customAdapter= new CustomAdapter(RegisterActivity.this, questions);
        mSpinnerQuestions.setAdapter(customAdapter);
    }

    private void initViews(){
        nestedScrollView = findViewById(R.id.nestedScrollView);
        textInputEditTextAnswer = findViewById(R.id.textInputEditTextRegisterAnswer);
        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutAnswer = findViewById(R.id.textInputLayoutAnswer);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextUsername);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);
        appCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);

    }

    private void initListeners(){
        appCompatButtonRegister.setOnClickListener(this);
    }

    private void initObjects(){
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.appCompatButtonRegister) {
            postDataToSQLite();
        }
    }

    private void postDataToSQLite(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextAnswer, textInputLayoutAnswer, getString(R.string.error_message_valid_answer))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());
            user.setQuestion(mSpinnerQuestions.getSelectedItem().toString().trim());
            user.setAnswer(textInputEditTextAnswer.getText().toString().trim());

            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }


    }

    private void emptyInputEditText(){
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextAnswer.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }

}