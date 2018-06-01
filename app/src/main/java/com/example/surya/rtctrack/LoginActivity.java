package com.example.surya.rtctrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.EMAIL) EditText txtUsername;
    @BindView(R.id.PASSWORD) EditText txtPassword;
    @BindView(R.id.loginbutton) Button buttonlogin;
    // User Session Manager Class
    UserSessionManager session;
    private ProgressDialog loading;
    private String name,lastname;
    public String phone;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        session = new UserSessionManager(getApplicationContext());
}
    public void Login(View view) {
        if(CheckInternet.isInternetAvailable(LoginActivity.this))  //if connection available
        {
            login();

        }
        else
        {
            Snackbar.make(view, "Not connected to INTERNET", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }


}
    public void Register(View view) {
        Intent intent = new Intent(LoginActivity.this, EmailVerify.class);
        //startActivity(intent);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }

    public void forgotpass(View view) {
        Intent intent = new Intent(LoginActivity.this, SendOTP.class);
        //tartActivity(intent);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }


    public boolean validate() {
        boolean valid = true;

        String emailll = txtUsername.getText().toString();
        String passworddd = txtPassword.getText().toString();

        if (emailll.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailll).matches()) {

            txtUsername.setError("enter a valid email address");
            valid = false;


        } else {
            txtUsername.setError(null);
        }

        if (passworddd.isEmpty() || passworddd.length() < 4 || passworddd.length() > 10) {
            txtPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        return valid;
    }
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            // onLoginFailed();
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        progressDialog.dismiss();


        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                // On complete call either onLoginSuccess or onLoginFailed
                progressDialog.dismiss();

                insertUser();
                // onLoginSuccess();
                // onLoginFailed();

            }
        }, 30);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }
    public void insertUser(){
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        String email = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();



        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Url.URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        loginverify api = adapter.create(loginverify.class);

        //Defining the method insertuser of our interface
        api.insertUser(
                //Passing the values by getting it from editTexts
                email,
                password,
                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

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
        if(check==11)
        {
            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
            getData(txtUsername.getText().toString());
        }
        else if(check==403)
        {
            Toast.makeText(getApplicationContext(), "invalid email/password", Toast.LENGTH_LONG).show();
        }
        else if(check==404)
        {
            Toast.makeText(getApplicationContext(), " network error", Toast.LENGTH_LONG).show();
            //onSignupSuccess();
            //confirmOtp(output);
        }
        else
        {
            Toast.makeText(getApplicationContext(), " somthing error", Toast.LENGTH_LONG).show();
        }
    }

    private void getData(String email) {
        //String id = LoginActivityy.emaill.getText().toString().trim();

        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);



        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Url.URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        data api = adapter.create(data.class);

        //Defining the method insertuser of our interface
        api.insertUser(
                //Passing the values by getting it from editTexts
                email,
                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

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

                        showJSON(output);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast

                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void showJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(config.KEY_NAME);
            lastname = collegeData.getString(config.KEY_ADDRESS);
            phone=collegeData.getString(config.KEY_PHONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (name != "" && lastname != "") {
            loading.dismiss();
            onLoginSuccess(name, lastname,phone);
            //Toast.makeText(this,name+lastname,Toast.LENGTH_LONG).show();
        } else {
            loading.dismiss();
            //  Toast.makeText(this,"error",Toast.LENGTH_LONG).show();
        }
    }
    public void onLoginSuccess(String firstname,String lastname,String phone) {
        session.createUserLoginSession(firstname,lastname,phone,txtUsername.getText().toString());


        // Starting MainActivity
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        finish();

    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }



}
