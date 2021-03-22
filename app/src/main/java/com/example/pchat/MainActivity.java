package com.example.pchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;


import java.io.IOException;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    public static final String EXTRA_NAME = "a";
    public static final String EXTRA_PWD = "b";
    public static final String EXTRA_MSG = "c";

    String extra_msg="a";

    final Context context = this;


    public String uname = "";
    public String pword = "";

    boolean loggedIn;

    AbstractXMPPConnection connection;




    @Override








    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(extra_msg.equals("wrong"))
        {
            Toast toast=Toast.makeText(MainActivity.this,"wrong username or password",Toast.LENGTH_SHORT);

            toast.show();
        }


        final Context context = this;



        TextView tv = findViewById(R.id.notamember);
        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, Register.class);

                startActivity(intent);
            }
        });

        //login button handler
        Button login_button = findViewById(R.id.Login);
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {





                loggedIn = false;
                //Intent intent = new Intent(context, Profile.class);
                EditText et = findViewById(R.id.login_username);
                uname = et.getText().toString();

                EditText etp = findViewById(R.id.login_password);
                pword = etp.getText().toString();

                //intent.putExtra(EXTRA_PWD, pword);
                //intent.putExtra(EXTRA_NAME, uname);

                new checkCredentials().execute();


                //try to connect from here itself.



            }//end of click
        });




    }//end of oncreate


    private class checkCredentials extends AsyncTask<Void, Void, Void> {
        String result;

        ProgressDialog   dialog= new  ProgressDialog(context);

        protected void onPreExecute() {
            super.onPreExecute();


                dialog.setMessage("Logging in ");
                dialog.show();





        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }





        @Override
        protected Void doInBackground(Void... voids) {


            String userName = uname;
            String password = pword;


            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(userName, password)
                    .setServiceName("pchat")
                    .setHost("3.139.90.132")
                    .setPort(5222)
                    .setConnectTimeout(25000)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) // Do not disable TLS except for test purposes!
                    .setDebuggerEnabled(true)
                    .setSendPresence(true)
                    .build();


            connection = new XMPPTCPConnection(config);
            ((XMPPTCPConnection) connection).setUseStreamManagement(true);           //changed from false to true
            ((XMPPTCPConnection) connection).setUseStreamManagementResumption(true); //added
            try {
                connection.connect().login();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (connection.isAuthenticated()) {
                loggedIn = true;
                connection.disconnect();
                Intent intent = new Intent(context, MyGroups.class);

                //Intent intent = new Intent(context, Profile.class);

                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);


                startActivity(intent);


            }//end of received message
            else
            {


                Intent intent_w = new Intent(context, MainActivity.class);
               // intent_w.putExtra(EXTRA_PWD, "wrong");
                extra_msg="wrong";
            startActivity(intent_w);
            }



            return null;
        }//end of do in background


    }//end of inner  class







}//end of outer class




