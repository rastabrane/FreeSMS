package com.android.freesms;

import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.freesms.library.DatabaseHandler;
import com.android.freesms.library.UserFunctions;
 
public class DashboardActivity extends Activity {
    UserFunctions userFunctions;
    Button btnLogout;
    Button btnSMS;
    DatabaseHandler dbHandler;
    private HashMap<String, String> user;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
       // user already logged in show databoard
            setContentView(R.layout.dashboard);
            btnLogout = (Button) findViewById(R.id.btnLogout);
            btnSMS = (Button) findViewById(R.id.btnSMS);
            
            dbHandler = new DatabaseHandler(getApplicationContext());
            user = dbHandler.getUserDetails();
            TextView emailTextView = (TextView) findViewById(R.id.emailTextView);
            emailTextView.setText(user.get("email"));
            
 
            btnLogout.setOnClickListener(new View.OnClickListener() {
 
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    userFunctions.logoutUser(getApplicationContext());
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Closing dashboard screen
                    finish();
                }
            });
            
            // Link to Register Screen
            btnSMS.setOnClickListener(new View.OnClickListener() {
     
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(),
                            FreeMain.class);
                    startActivity(i);
                    finish();
                }
            });
 
        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
        }
    }
}