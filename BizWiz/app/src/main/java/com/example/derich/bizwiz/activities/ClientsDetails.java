package com.example.derich.bizwiz.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.helper.InputValidation;
import com.example.derich.bizwiz.model.Client;
import com.example.derich.bizwiz.model.User;
import com.example.derich.bizwiz.sql.DatabaseHelper;

/**
 * Created by group 7 CS project on 3/11/18.
 */
public class ClientsDetails extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = ClientsDetails.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutfirstName;
    private TextInputLayout textInputLayoutsecondName;
    private TextInputLayout textInputLayoutclientEmail;
    private TextInputLayout textInputLayoutNumber;

    private TextInputEditText textInputEditTextfirstName;
    private TextInputEditText textInputEditTextsecondName;
    private TextInputEditText textInputEditTextclientEmail;
    private TextInputEditText textInputEditTextNumber;

    private AppCompatButton appCompatButtonAddClient;
    private AppCompatTextView appCompatTextViewClientLink;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_details);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews(){
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutfirstName = (TextInputLayout) findViewById(R.id.textInputLayoutfirstName);
        textInputLayoutsecondName = (TextInputLayout) findViewById(R.id.textInputLayoutsecondName);
        textInputLayoutclientEmail = (TextInputLayout) findViewById(R.id.textInputLayoutclientEmail);
        textInputLayoutNumber = (TextInputLayout) findViewById(R.id.textInputLayoutNumber);

        textInputEditTextfirstName= (TextInputEditText) findViewById(R.id.textInputEditTextfirstName);
        textInputEditTextsecondName = (TextInputEditText) findViewById(R.id.textInputEditTextsecondName);
        textInputEditTextclientEmail = (TextInputEditText) findViewById(R.id.textInputEditTextclientEmail);
        textInputEditTextNumber = (TextInputEditText) findViewById(R.id.textInputEditTextNumber);

        appCompatButtonAddClient = (AppCompatButton) findViewById(R.id.appCompatButtonAddClient);

        appCompatTextViewClientLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewClientLink);
    }

    private void initListeners(){
        appCompatButtonAddClient.setOnClickListener(this);
        appCompatTextViewClientLink.setOnClickListener(this);
    }

    private void initObjects(){
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        client = new Client();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.appCompatButtonAddClient:
                postDataToSQLite();
                break;
            case R.id.appCompatTextViewClientLink:
                finish();
                break;
        }
    }

    private void postDataToSQLite(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextfirstName, textInputLayoutfirstName, getString(R.string.error_message_clientFirstName))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextsecondName, textInputLayoutsecondName, getString(R.string.error_message_clientSecondName))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextclientEmail, textInputLayoutclientEmail, getString(R.string.error_message_clientEmail))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextclientEmail, textInputLayoutclientEmail, getString(R.string.error_message_clientEmail))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextNumber, textInputLayoutNumber, getString(R.string.error_message_number))) {
            return;
        }
        

        if (!databaseHelper.checkClient(textInputEditTextNumber.getText().toString().trim())) {
            client.setfirstName(textInputEditTextfirstName.getText().toString().trim());
            client.setsecondName(textInputEditTextsecondName.getText().toString().trim());
            client.setclientEmail(textInputEditTextclientEmail.getText().toString().trim());
            client.setNumber(textInputEditTextNumber.getText().toString().trim());

            databaseHelper.addClient(client);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_clientMessage), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_message_client), Snackbar.LENGTH_LONG).show();
        }


    }

    private void emptyInputEditText(){
        textInputEditTextfirstName.setText(null);
        textInputEditTextsecondName.setText(null);
        textInputEditTextclientEmail.setText(null);
        textInputEditTextNumber.setText(null);
    }

}