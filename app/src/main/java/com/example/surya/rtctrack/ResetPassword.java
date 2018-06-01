package com.example.surya.rtctrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class ResetPassword extends AppCompatActivity {
    private static final String TAG = "ResetPassword";
    @BindView(R.id.pass) EditText password;
    @BindView(R.id.confirm) EditText confirm;
    public static final String ROOT_URL =Url.URL;
    private static final int REQUEST_SIGNUP = 0;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
    }
    public void Reset(View view)
    {
        if(CheckInternet.isInternetAvailable(ResetPassword.this))  //if connection available
        {
            verifypass();

        }
        else
        {
            Snackbar.make(view, "Not connected to INTERNET", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }
    public void verifypass()  {
        Log.d(TAG, "verifyemail");

        if (!validate()) {
            onLoginFailed();
            return;
        }




        progressDialog = new ProgressDialog(ResetPassword.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {


                insertUser();

            }
        }, 30);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        // moveTaskToBack(true);
        finish();
    }

    public void onLoginSuccess() {
        progressDialog.dismiss();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Successfully reset your password", Toast.LENGTH_LONG).show();
        finish();
        //  buttonlogin.setEnabled(true);

    }

    public void onLoginFailed()  {

        // confirmOtpp();
        // buttonlogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String pass = password.getText().toString();
        String confirrm = confirm.getText().toString();
        if(pass.equals(confirrm)) {

            if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
                password.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                password.setError(null);
            }
            if (confirrm.isEmpty() || confirrm.length() < 4 || confirrm.length() > 10) {
                confirm.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                confirm.setError(null);
            }
        }
        else {
            confirm.setError("password did not match");
            valid = false;
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
        resetapi api = adapter.create(resetapi.class);

        //Defining the method insertuser of our interface
        api.insertUser(

                //Passing the values by getting it from editTexts
                SendOTP.Emaill,
                confirm.getText().toString(),


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
                        // Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
                        switching(output);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(getApplicationContext(),"PLEASE CHECK INTERNET CONNECTION",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void switching(String output)
    {   int check=Integer.parseInt(output);
        if(check==1)
        {  Toast.makeText(getApplicationContext(), "you are succesfully reset your password", Toast.LENGTH_LONG).show();
            onLoginSuccess();
        }

        else if(check==33||check==99)
        {
            Toast.makeText(getApplicationContext(), "Some thing wrong please try agiain later", Toast.LENGTH_LONG).show();
        }
        else if(check==45)
        {
            Toast.makeText(getApplicationContext(), "Some thing wrong in request method please  try agiain later", Toast.LENGTH_LONG).show();

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
