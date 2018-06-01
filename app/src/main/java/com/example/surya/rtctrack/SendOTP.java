package com.example.surya.rtctrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
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

public class SendOTP extends AppCompatActivity {

    private static final String TAG = "SendOTP";
    EditText editTextConfirmOtp;
    AlertDialog alertDialog;
    public static final String ROOT_URL =Url.URL;
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.sendOTP) EditText sendotp;
    ProgressDialog loading;
    String OTPP;

    public static String Emaill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        ButterKnife.bind(this);
    }
    public void Registerr(View view)
    {
        if(CheckInternet.isInternetAvailable(SendOTP.this))  //if connection available
        {
            Verifyemail();

        }
        else
        {
            Snackbar.make(view, "Not connected to INTERNET", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }

    public void Verifyemail()  {
        Log.d(TAG, "verifyemail");

        if (!validate()) {
            onLoginFailed();
            return;
        }




        final ProgressDialog progressDialog = new ProgressDialog(SendOTP.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        //buttonlogin.setEnabled(false);
        progressDialog.dismiss();
        Emaill=sendotp.getText().toString();
//String        String email = emaill.getText().toString();
//        String password = passwordd.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                // On complete call either onLoginSuccess or onLoginFailed
                progressDialog.dismiss();
                insertUser();
                //userLogin();
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

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        // moveTaskToBack(true);
        finish();
    }

    public void onLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
        finish();
        //  buttonlogin.setEnabled(true);

    }

    public void onLoginFailed()  {

        // confirmOtpp();
        // buttonlogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String emailll = sendotp.getText().toString();


        if (emailll.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailll).matches()) {

            sendotp.setError("enter a valid email address");
            valid = false;


        } else {
            sendotp.setError(null);
        }



        return valid;
    }

    private void confirmOtp(String Output) {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        OTPP=Output;
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dailogconfirm, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Displaying a progressbar
                final ProgressDialog loading = ProgressDialog.show(SendOTP.this, "Authenticating", "Please wait while we check the entered code", false,false);

                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();

                if(OTPP.equals(otp))
                {alertDialog.dismiss();
                    loading.dismiss();
                    startActivity(new Intent(SendOTP.this, ResetPassword.class));
                    finish();
                }else
                    loading.dismiss();
                editTextConfirmOtp.setError("Incorrect otp");
            }
        });
    }

    public void insertUser(){
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter
        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);
        //Creating object for our interface
        Sendreset api = adapter.create(Sendreset.class);

        //Defining the method insertuser of our interface
        api.insertUser(

                //Passing the values by getting it from editTexts
                sendotp.getText().toString(),


                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(retrofit.client.Response result, retrofit.client.Response response) {
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
                        loading.dismiss();
                        // Toast.makeText(getApplicationContext(),output,Toast.LENGTH_LONG).show();
                        if(output!=null) switching(output);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast

                        Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    private void switching(String output)
    {   int check=Integer.parseInt(output);
        if(check==0)
        {  Toast.makeText(getApplicationContext(), "you are not Registerd", Toast.LENGTH_LONG).show();}

        else if(check==11)
        {
            Toast.makeText(getApplicationContext(), "Some thing wrong please try agiain later", Toast.LENGTH_LONG).show();
        }
        if(output.length()==6)
        {
            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
            confirmOtp(output);
        }

    }
}
