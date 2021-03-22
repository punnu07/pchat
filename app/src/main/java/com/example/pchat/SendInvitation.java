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

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class SendInvitation extends AppCompatActivity {


    public static final String EXTRA_NAME = "a";
    public static final String EXTRA_PWD = "b";
    public static final String EXTRA_MSG = "c";



    final Context context = this;


    public String uname = "";
    public String pword = "";

    AbstractXMPPConnection connection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invitation);

        uname=getIntent().getStringExtra(SendInvitation.EXTRA_NAME);
        pword=getIntent().getStringExtra(SendInvitation.EXTRA_PWD);


        Button sendinvite_button= findViewById(R.id.sendinvitebutton);
        sendinvite_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                new sendInvite().execute();

            }
        });

    }//end of oncreate







    private class  sendInvite extends AsyncTask<Void, Void, Void> {
        String result;


        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Sending Invitation");
            dialog.show();
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            //after group creation go back to the profile
            Intent intent = new Intent(context, Profile.class);
            startActivity(intent);




        }




        @Override
        protected Void doInBackground(Void... voids) {

            String userName=uname;
            String password=pword;

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



            AbstractXMPPConnection connection = new XMPPTCPConnection(config);
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

            //already connected
            if(connection.isAuthenticated())
            {

                EditText et;
                et=findViewById(R.id.sendinvitationgroupname);
                String groupname=et.getText().toString();

                et=findViewById(R.id.sendinvitationusername);
                String username=et.getText().toString();





                        /*
                    manager= MultiUserChatManager.getInstanceFor(connection);
                    muc= manager.getMultiUserChat(groupname+"@conference.pchat");

                try {
                    muc.join("mee");
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                try {
                    muc.invite(toinvite,"Join this");
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                         */


               String InitialMessage="<message><type>one</type><groupname>"+groupname+"</groupname></message>";
                //send the message to the preacher
                String toPerson = username+"@pchat";
                EntityBareJid jid = null;
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                try {
                    jid = JidCreate.entityBareFrom(toPerson);
                } catch (XmppStringprepException ex) {
                    ex.printStackTrace();
                }
                Chat chat = chatManager.createChat(String.valueOf(jid));
                try {
                    chat.sendMessage(InitialMessage);
                } catch (SmackException.NotConnectedException es) {
                    es.printStackTrace();
                }




            }//end of connection







                //disconnect the connection
            connection.disconnect();


            return null;
        }//end of do in background



    }//end of inner class









}//end of outer class