package com.android.freesms;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.android.freesms.library.DatabaseHandler;
import com.android.freesms.library.JSONParser;
import com.android.freesms.library.UserFunctions;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterTask extends AsyncTask<String, Void, Integer> {

	private ProgressDialog progressDialog;
	private RegisterActivity activity;
	private int id = -1;
	private JSONParser jsonParser;
	private static String loginURL = "http://davidjkelley.net/android_api/";
	private static String registerURL = "http://davidjkelley.net/android_api/";
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	private int responseCode = 0;
	
	/*Constructor that takes parameters passed by LoginFragment and stores them as class- 
	 * wide fields so that all methods can access necessary variables. 
	 * */
	public RegisterTask(RegisterActivity activity, ProgressDialog progressDialog)
	{
		this.activity = activity;
		this.progressDialog = progressDialog;
	}

	/*A necessary but very simple method that launches a ProgressDialog to show the
	 * user that a background task is operating (registration).*/
	@Override
	protected void onPreExecute()
	{
		progressDialog.show();
	}

	/*This method does almost all of the work for the class. It builds a connection to my 
	 * server, collects the details from the UI of the user's information, and then tries
	 * to register the user with the SQL database. All of the actual HTTP connection work
	 * is done in a background library class for security - including the hashing of a
	 * password into a 64bit encryption. */
	@Override
	protected Integer doInBackground(String... arg0) {
		EditText userName = (EditText)activity.findViewById(R.id.registerEmail);
		EditText passwordEdit = (EditText)activity.findViewById(R.id.registerPassword);
		EditText nameEdit = (EditText)activity.findViewById(R.id.registerName);
		String name = nameEdit.getText().toString();
		String email = userName.getText().toString();
		String password = passwordEdit.getText().toString();
		Log.v(email, password);
		UserFunctions userFunction = new UserFunctions();
        JSONObject json = userFunction.registerUser(name, email, password);

        // check for login response
        try {
            if (json.getString(KEY_SUCCESS) != null) {
                //registerErrorMsg.setText("");
                String res = json.getString(KEY_SUCCESS);
                if(Integer.parseInt(res) == 1){
                    // user successfully registred
                    // Store user details in SQLite Database
                    DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
                    JSONObject json_user = json.getJSONObject("user");

                    // Clear all previous data in database
                    userFunction.logoutUser(activity.getApplicationContext());
                    db.addUser(json_user.getString(KEY_NAME), 
                    	json_user.getString(KEY_EMAIL), json.getString(KEY_UID), 
                    	json_user.getString(KEY_CREATED_AT));
                    //successful registration
                    responseCode = 1;
                }else{
                    // Error in registration
                	responseCode = 0;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
		return responseCode;
    }

	/*This final method concludes the background task. Its responseCode variable is sent from
	 * doInBackground, and this method acts based on the code it is sent. If the code is 1, 
	 * registration was successful and the main activity notifies the user of succes - the
	 * inverse occurs if there is a failure and 0 was sent.*/
	@Override
	protected void onPostExecute(Integer responseCode)
	{
		EditText userName = (EditText)activity.findViewById(R.id.registerEmail);
		EditText passwordEdit = (EditText)activity.findViewById(R.id.registerPassword);
		String s = userName.getText().toString();
		
		if (responseCode == 1) {
			progressDialog.dismiss();
			activity.registerReport(responseCode);
			userName.setText("");
			passwordEdit.setText("");	
		}
		if (responseCode == 0) {
			progressDialog.dismiss();
			activity.registerReport(responseCode);	
		}
	}
}
