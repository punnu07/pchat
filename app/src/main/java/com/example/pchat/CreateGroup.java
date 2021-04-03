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
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class CreateGroup extends AppCompatActivity {



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
        setContentView(R.layout.activity_create_group);

        uname=getIntent().getStringExtra(CreateGroup.EXTRA_NAME);
        pword=getIntent().getStringExtra(CreateGroup.EXTRA_PWD);

        //create chatroom handler
        Button creategroup_button= findViewById(R.id.creategroupbutton);
        creategroup_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new createGroup().execute();


                }
        });




        Button individualmessagesend_button= findViewById(R.id.individualmessagesendbutton);
        individualmessagesend_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new individualmessagesend().execute();


            }
        });






    }//end of oncreate



    private class  createGroup extends AsyncTask<Void, Void, Void> {
        String result;


        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Creating  Group");
            dialog.show();
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            //after group creation go back to the profile
            Intent intent = new Intent(context, MyGroups.class);
            intent.putExtra(EXTRA_PWD, pword);
            intent.putExtra(EXTRA_NAME, uname);
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



                Presence presence = new Presence(Presence.Type.available);
                try {
                    connection.sendStanza(presence);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                EntityBareJid mucJid = null;
                MultiUserChat muc = null;
                MultiUserChatManager manager = null;


                EditText et;
                et=findViewById(R.id.GroupName);

                String roomname=et.getText().toString();
                try {

                    manager= MultiUserChatManager.getInstanceFor(connection);
                    muc= manager.getMultiUserChat(roomname+"@conference.pchat");

                    //Log.d("service names", String.valueOf(manager.getServiceNames()));


                    // Log.d("Is service enabled", String.valueOf(manager.isServiceEnabled("mine@conference.pchat")));
                    muc.create("Room");

                    Form form=new Form(DataForm.Type.submit);

                    Form form1 = muc.getConfigurationForm();
                    Form submitForm = form1.createAnswerForm();

                    muc.sendConfigurationForm(form);

                    submitForm.setAnswer("muc#roomconfig_persistentroom", true);
                    muc.sendConfigurationForm(submitForm);


                    //create roster
                    Roster roster = Roster.getInstanceFor(connection);
                    roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
                    String jid = roomname + "@conference.pchat";
                    roster.createEntry(roomname, jid, null);

                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();

                    Log.d("xmpp: ", "Chat room join Error: " + e.getMessage());
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                    Log.d("xmpp: ", "Chat room join Error: " + e.getMessage());
                } catch (SmackException e) {
                    Log.d("xmpp: ", "Chat room join Error: " + e.getMessage());
                    e.printStackTrace();
                }

                if(muc.isJoined())
                {
                    Log.d("xmpp: ", "user has Joined  the chat room");

                }

                /*

                try {
                    muc.sendMessage("Hi all");
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                */

                /*
                FullJid otherJid = null;
                try {
                    otherJid = JidCreate.fullFrom("joseph@pchat/Smack");
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }


                try {
                    muc.invite(String.valueOf(otherJid),"check this group out");
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                 */

            }//end of connection




            return null;
        }//end of do in background



    }//end of inner class







    //next inner class

    private class  individualmessagesend extends AsyncTask<Void, Void, Void> {
        String result;


        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Sending");
            dialog.show();
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            /*
            //after group creation go back to the profile
            Intent intent = new Intent(context, MyGroups.class);
            intent.putExtra(EXTRA_PWD, pword);
            intent.putExtra(EXTRA_NAME, uname);
            startActivity(intent);

             */
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
                et=(EditText)findViewById(R.id.individualmessageto);
                String toPerson=et.getText().toString()+"@pchat";

                et=(EditText)findViewById(R.id.individualmessage);
                String Messagetosend=et.getText().toString();

                EntityBareJid jid = null;
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                try {
                    jid = JidCreate.entityBareFrom(toPerson);
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
                Chat chat = chatManager.createChat(String.valueOf(jid));
                try {
                    chat.sendMessage(Messagetosend);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                //clear the chat window



                runOnUiThread(new Runnable(){
                    public void run() {

                       EditText ett=(EditText)findViewById(R.id.individualmessage);

                        ett.setText("");

                        ett=(EditText)findViewById(R.id.individualmessageto);
                        ett.setText("");



                    }
                });





            }//end of connection





            return null;
        }//end of do in background



    }//end of inner class








}//end of outer classs