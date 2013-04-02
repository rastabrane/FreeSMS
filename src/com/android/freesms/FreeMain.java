package com.android.freesms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
 
import android.app.Activity;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
 
public class FreeMain extends Activity implements OnClickListener{
 
	private EditText telTextField;
	private EditText msgTextField;
	private Button buttonSend;
	private ProgressBar progressBar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_free);
		
		telTextField = (EditText)findViewById(R.id.telTextField);
		msgTextField = (EditText)findViewById(R.id.msgTextField);
		buttonSend = (Button)findViewById(R.id.sendButton);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.GONE);
		buttonSend.setOnClickListener(this);
	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_free, menu);
		return true;
	}
 
	public void onClick(View v) {
		// TODO Auto-generated method stub
			if(telTextField.getText().toString().length()<1 || msgTextField.getText().toString().length()<1){
				
				// out of range
				Toast.makeText(this, "please enter something", Toast.LENGTH_LONG).show();
			}else{
				progressBar.setVisibility(View.VISIBLE);
				new MyAsyncTask().execute(msgTextField.getText().toString());		
			}
		
 
	} 
 
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
 
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(params[0]);
			return null;
		}
 
		protected void onPostExecute(Double result){
			progressBar.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(), "SMS sent Successfully ", Toast.LENGTH_LONG).show();
		}
		protected void onProgressUpdate(Integer... progress){
			progressBar.setProgress(progress[0]);
		}
 
		public void postData(String valueIWantToSend) {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://diegogomes.net/sms.php");
			
			String telNumber = telTextField.getText().toString();
			String msgSend = msgTextField.getText().toString();
			
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("tel", telNumber));
				nameValuePairs.add(new BasicNameValuePair("msg", msgSend));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
 
	}
}
