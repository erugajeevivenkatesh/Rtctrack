package com.example.surya.rtctrack;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class EmailVerify extends AppCompatActivity {
    private static final String TAG = "EmailVerify";
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.EMAILVERIFY) EditText verifyemail;
    EditText editTextConfirmOtp;
    ProgressDialog loading;
    AlertDialog alertDialog;
    AlertDialog.Builder alert;
    public static String EMAILTEXT;
    String verifyOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);
        ButterKnife.bind(this);
    }
    public void Registerr(View view)
    {
        Verifyemail();
    }
    public void Verifyemail()  {
        Log.d(TAG, "verifyemail");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        EMAILTEXT= verifyemail.getText().toString();



        final ProgressDialog progressDialog = new ProgressDialog(EmailVerify.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        //buttonlogin.setEnabled(false);
        progressDialog.dismiss();
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

        // buttonlogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String emailll = verifyemail.getText().toString();


        if (emailll.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailll).matches()) {

            verifyemail.setError("enter a valid email address");
            valid = false;


        } else {
            verifyemail.setError(null);
        }



        return valid;
    }
    private void confirmOtp(String OTP) {
        //Creating a LayoutInflater object for the dialog box
        verifyOTP=OTP;
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dailogconfirm, null);
        AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);


        alertDialog = alert.create();
        alertDialog.show();


    }
    public void confirmm(View view)
    {



        //Displaying a progressbar
        final ProgressDialog loadingg = ProgressDialog.show(EmailVerify.this, "Authenticating", "Please wait while we check the entered code", false,false);

        //Getting the user entered otp from edittext
        final String otp = editTextConfirmOtp.getText().toString().trim();

        if(verifyOTP.equals(otp)) {
            alertDialog.dismiss();
            loadingg.dismiss()
            ;
            startActivity(new Intent(EmailVerify.this, SignUpActicity.class));
            finish();
        }
        else{
            loadingg.dismiss();
            editTextConfirmOtp.setError("invalid code");
        }
    }


    public void insertUser(){
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Url.URL) //Setting the Root URL
                .build(); //Finally building the adapter
        loading = ProgressDialog.show(this, "Please wait...", "Sending...", false, false);
        //Creating object for our interface
        Verifyemail api = adapter.create(Verifyemail.class);

        //Defining the method insertuser of our interface
        api.insertUser(

                //Passing the values by getting it from editTexts
                verifyemail.getText().toString(),

                new Callback<Response>() {
                    @Override
                    public void success(retrofit.client.Response result, retrofit.client.Response response) {
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
                        loading.dismiss();
                        // Toast.makeText(getApplicationContext(),output,Toast.LENGTH_LONG).show();
                        if(output!=null) switching(output);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast

                        Toast.makeText(getApplicationContext(),"PLEASE CHECK INTERNET",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    private void switching(String output)
    {   int check=Integer.parseInt(output);
        if(check==0)
        {verifyemail.setError("Email id is already registerd");

        }
        else if(check==11)
        {
            Toast.makeText(getApplicationContext(), "Some thing wrong please try agiain later", Toast.LENGTH_LONG).show();
        }
        else if(check==403)
        {
            Toast.makeText(getApplicationContext(), "Some thing wrong please try agiain later", Toast.LENGTH_LONG).show();
        }
        if(output.length()==6)
        {
            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
            confirmOtp(output);
        }else
        {
            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
        }
    }

    public void Register(View view) {
        finish();
    }
}
