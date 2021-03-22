package com.example.pchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.Collection;


public class MyGroups extends AppCompatActivity {






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
        setContentView(R.layout.activity_my_groups);



        uname=getIntent().getStringExtra(Profile.EXTRA_NAME);
        pword=getIntent().getStringExtra(Profile.EXTRA_PWD);




        new GetMyGroups().execute();

        /*
        Button mygroups_button= findViewById(R.id.MyGroupsButton);
        mygroups_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new GetMyGroups().execute();

            }
        });
        */








    }//end of oncreate


    private class  GetMyGroups extends AsyncTask<Void, Void, Void> {
        String result;

        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading Groups ");
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





           LinearLayout ll=findViewById(R.id.MyGroupsView);


            //already connected
            if(connection.isAuthenticated()) {


                //get the roster list
                Roster roster = Roster.getInstanceFor(connection);

                if (!roster.isLoaded()) {
                    try {
                        roster.reloadAndWait();
                    } catch (SmackException.NotLoggedInException e) {
                        e.printStackTrace();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                }


                Collection<RosterEntry> entries = roster.getEntries();

                int num_groups=entries.size();

                //Log.d("Group Size",entries.size().toString());


                for (RosterEntry entry : entries) {


                    //String toPerson = entry.getName();
                    String groupname= entry.getName();

                    Button groupbutton = new Button(context);
                    groupbutton.setText(groupname);

                    groupbutton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


                    runOnUiThread(new Runnable(){
                        public void run()
                        {
                    ll.addView(groupbutton);
                             }
                    });




                    /*
                    EntityBareJid jid = null;
                    ChatManager chatManager = ChatManager.getInstanceFor(connection);
                    try {
                        jid = JidCreate.entityBareFrom(toPerson);
                    } catch (XmppStringprepException e) {
                        e.printStackTrace();
                    }
                    Chat chat = chatManager.createChat(String.valueOf(jid));
                    try {
                        chat.sendMessage(MessageToSend);
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }

                    */

                    //clear the chat window

                }//end of roster entries


                //TextView tv=new TextView(context);
                //tv.setText(num_groups);
                //LinearLayoutView.addView(tv);










            }//end of connection



        return null;
        }//end of doinbackground




    }//end of inner class









}//end of outer class