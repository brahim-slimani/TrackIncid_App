package esi.siw.map.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;

import esi.siw.map.R;


public class LoginActivity extends AppCompatActivity {

    EditText login;
    EditText password;

    InputStream is=null;
    String result=null;
    String line=null;
    int code;

    String responseBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //enable network policy service
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlogin);

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

        final Button bt = (Button) findViewById(R.id.connect);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login.getText().toString();
                String pass = password.getText().toString();

                if(login(email,pass).equals("ACCESS VALID")){

                    final ProgressDialog progressdialog = new ProgressDialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
                    progressdialog.setMessage("Please Wait....");
                    progressdialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressdialog.dismiss();
                            Intent intent =new Intent(LoginActivity.this, IncidentActivity.class);
                            startActivity(intent);
                        }
                    }, 1000);


                }else{

                    Toast.makeText(LoginActivity.this, "access denied, try again ! ", Toast.LENGTH_LONG).show();

                }


            }


          //  }
        });

    }


    public String login(String email,String password) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("email",email));
        nameValuePairs.add(new BasicNameValuePair("password",password));

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.43.120/login.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpclient.execute(httppost,
                    responseHandler);

        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {

            System.out.println(responseBody);

        }
        catch(Exception e)
        {
            Log.e("Fail 3", e.toString());
        }

        return responseBody;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_exit:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialoguexit);
                dialog.setTitle("Close confirmation");
                Button btnExit = (Button) dialog.findViewById(R.id.btn_exit);
                Button btnNoExit = (Button) dialog.findViewById(R.id.btn_noexit);

                dialog.show();
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        dialog.dismiss();
                    }
                });

                btnNoExit.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


        }
        switch (item.getItemId()) {

            case R.id.action_help:
                final Dialog dialog2 = new Dialog(this);
                dialog2.setContentView(R.layout.help_dialogue);
                dialog2.setTitle("A Propos de");
                Button btnHelp = (Button) dialog2.findViewById(R.id.btn_help);

                dialog2.show();
                btnHelp.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });

            default: return super.onOptionsItemSelected(item);
        }

    }





}
