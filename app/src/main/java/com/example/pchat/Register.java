package com.example.pchat;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

import org.jivesoftware.smack.roster.*; /*you may have been missing this*/
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.tcp.*;
import java.util.Collection; /*optional*/



import java.io.IOException;

import org.jivesoftware.smack.roster.*;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class Register extends AppCompatActivity {


    public static final String EXTRA_NAME = "a";
    public static final String EXTRA_PWD = "b";

    final Context context = this;


    public String uname;
    public String pword;
    public boolean IsAccountCreated = false;


    AbstractXMPPConnection connection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register_button = findViewById(R.id.Register);
        register_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new createnewuser().execute();


                //if account is created go to profile of the client


                Intent intent = new Intent(context, RegistrationSuccess.class);

                startActivity(intent);




            }
        });





    }//end of oncreate





    //starting of inner class
    private class createnewuser extends AsyncTask<Void, Void, Void> {
        String result;



        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(" ");
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


            EditText et = (EditText) findViewById(R.id.register_username);
            uname = et.getText().toString();

            EditText etp = (EditText) findViewById(R.id.register_password);
            pword = etp.getText().toString();


            String Username = "admin";
            String Password = "punnooseak";


//login as admin
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(Username, Password)
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
                //create new user
                AccountManager accountManager = AccountManager.getInstance(connection);
                try {
                    if (accountManager.supportsAccountCreation()) {
                        accountManager.sensitiveOperationOverInsecureConnection(true);
                        accountManager.createAccount(uname, pword);

                    }
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                //at this point disconnect
                connection.disconnect();
                IsAccountCreated = true;


            }//end of connection as localhost






            /*
            //at this point login as the client
            Username = uname;
            Password = pword;


            config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(Username, Password)
                    .setServiceName("localhost")
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


            //roster


            if (connection.isAuthenticated()) {


                String InitialMessageToPreacher = "";
                //create an xml message manually
                InitialMessageToPreacher="<message><type>one</type><name>"+uname+"</name></message>";

                //send the message to the preacher
                String toPerson = "rony@localhost";
                EntityBareJid jid = null;
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                try {
                    jid = JidCreate.entityBareFrom(toPerson);
                } catch (XmppStringprepException ex) {
                    ex.printStackTrace();
                }
                Chat chat = chatManager.createChat(String.valueOf(jid));
                try {
                    chat.sendMessage(InitialMessageToPreacher);
                } catch (SmackException.NotConnectedException es) {
                    es.printStackTrace();
                }


                //disconnect
                connection.disconnect();
            }




            */

            return null;
        }// end of doinbackground function


    }//end of inner class




}//end of outer class