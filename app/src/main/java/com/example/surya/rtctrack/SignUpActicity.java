package com.example.surya.rtctrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignUpActicity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    @BindView(R.id.FirstName) EditText editTexfirsttName;
    @BindView(R.id.LastName) EditText editTexUserName;
    @BindView(R.id.Phone) EditText editTextPhone;
    @BindView(R.id.password) EditText _passwordText;
    //
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.Register) Button registerrr;
    @BindView(R.id.AlreadyRegister) TextView Alreadyregister;
    public static final String ROOT_URL =Url.URL;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_acticity);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    public void Registerdetails(View view)
    {
        //setSnackbarMessage("Not connected to Internet");


        if(CheckInternet.isInternetAvailable(SignUpActicity.this))  //if connection available
        {
            signup();

        }
        else
        {
            Snackbar.make(view, "Not connected to INTERNET", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
    public void alreadyRegister(View view)
    {

        finish();
    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            onSignupFailed();
            return;
        }

        //registerrr.setEnabled(false);
        progressDialog = new ProgressDialog(SignUpActicity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account.....");
        progressDialog.show();




        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed

                        insertUser();

                    }
                }, 300);
    }


    public void onSignupSuccess() {
        registerrr.setEnabled(true);
        progressDialog.dismiss();
        Toast.makeText(getBaseContext(), "Register success", Toast.LENGTH_LONG).show();
        Intent intt=new Intent(SignUpActicity.this,LoginActivity.class);
        startActivityForResult(intt, REQUEST_SIGNUP);

        finish();
    }
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
        finish();
    }
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Registration  failed", Toast.LENGTH_LONG).show();

        registerrr.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = editTexfirsttName.getText().toString();
        String lastname=editTexUserName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String password = _passwordText.getText().toString();


        if (name.isEmpty() || name.length() < 5) {
            editTexfirsttName.setError("at least 5 characters");
            valid = false;
        } else {
            editTexfirsttName.setError(null);
        }
        if (lastname.isEmpty() || lastname.length() < 3) {
            editTexUserName.setError("at least 3 characters");
            valid = false;
        } else {
            editTexUserName.setError(null);
        }

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()||phone.length()!=10) {

            editTextPhone.setError("please enter a valid Phone number");
            valid = false;

        } else {
            editTextPhone.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public void insertUser(){
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        RegisterAPI api = adapter.create(RegisterAPI.class);

        //Defining the method insertuser of our interface
        api.insertUser(

                //Passing the values by getting it from editTexts
                editTexfirsttName.getText().toString(),
                editTexUserName.getText().toString(),
                EmailVerify.EMAILTEXT,
                editTextPhone.getText().toString(),
                _passwordText.getText().toString(),


                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader;

                        //An string to store output from the server
                        String output = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        switching(output);

                    }
                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast

                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    private void switching(String output)
    {   int check=Integer.parseInt(output);
        if(check==0)
        {
            Toast.makeText(getApplicationContext(), "Name Or EMAIL ID already exists", Toast.LENGTH_LONG).show();
        }
        else if(check==99)
        {
            Toast.makeText(getApplicationContext(), "Some thing wrong please try agiain later", Toast.LENGTH_LONG).show();
        }
        else if(check==1)
        {
            onSignupSuccess();
            //confirmOtp(output);
        }
        else
        {
            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
        }
    }    @Override
    protected  void onResume(){
        super.onResume();
    }
    @Override
    protected  void onPause(){
        super.onPause();
    }
    @Override
    protected  void onStop(){
        super.onStop();
    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
    }
}
