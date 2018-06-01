package com.example.surya.rtctrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class Accountdetails extends AppCompatActivity {

    UserSessionManager session;
    TextView firstnamee,lastnamee,emaill,phonee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountdetails);
        Button logout=(Button)findViewById(R.id.logout);

        firstnamee=(TextView)findViewById(R.id.FirstName);
        lastnamee=(TextView)findViewById(R.id.Lastname);
        emaill=(TextView)findViewById(R.id.email);
        phonee=(TextView)findViewById(R.id.phone);

        session = new UserSessionManager(getApplicationContext());

        final SaveHostdata saveroute=new SaveHostdata(getApplicationContext());

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                saveroute.rangeclear();
                saveroute.logoutUser();
                session.logoutUser();
            }
        });
        HashMap<String, String> user = session.getUserDetails();

        // get name
        String name = user.get(UserSessionManager.KEY_NAME);
        String lastname =user.get(UserSessionManager.KEY_LASTNAME);
        String phone=user.get(UserSessionManager.KEY_PHONE);
        String email = user.get(UserSessionManager.KEY_EMAIL);
        firstnamee.setText(name);
        lastnamee.setText(lastname);
        emaill.setText(email);
        phonee.setText(phone);

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
