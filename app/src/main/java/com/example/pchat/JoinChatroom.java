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
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.ArrayList;

public class JoinChatroom extends AppCompatActivity {




    String roomname="unassignedandinvalidroom";

    public static final String EXTRA_NAME = "a";
    public static final String EXTRA_PWD = "b";
    public static final String EXTRA_MSG = "c";



    final Context context = this;


    public String uname = "";
    public String pword = "";

    AbstractXMPPConnection connection1, connection2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_chatroom);


        uname=getIntent().getStringExtra(JoinChatroom.EXTRA_NAME);
        pword=getIntent().getStringExtra(JoinChatroom.EXTRA_PWD);


        //connect to group
        Button ConnecttoChat_button= findViewById(R.id.ConnectToGroupButton);
        ConnecttoChat_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new joinChat().execute();


            }
        });



        //send message handler
        Button sendChat_button= findViewById(R.id.MessageSendButton);
        sendChat_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new sendChat().execute();


            }
        });



        //handler for clearing the chatwindow
        Button clearchat_button= findViewById(R.id.clearchatbutton);
        clearchat_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                MessageHandler mg=new MessageHandler();
                mg.clearchat();

                //clear the window

                TextView tvv=findViewById(R.id.MUCChatwindow);
                tvv.setText("");



            }
        });




        //handler for logout
        Button Logout_button= findViewById(R.id.LogoutFromChatButton);
        Logout_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){




                uname="";
                pword="";

                Intent intent=new Intent(context,MainActivity.class);
                startActivity(intent);

            }
        });






    }//end of oncreate



    //this will just display the messages.
    private class  joinChat extends AsyncTask<Void, Void, Void> {


        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Connecting");
            dialog.show();
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            //after group creation go back to the profile
           // Intent intent = new Intent(context, Profile.class);
           // startActivity(intent);

        }



        protected Void doInBackground(Void... voids) {



            //join as the second person
           String userName=uname;
            String password=pword;

        XMPPTCPConnectionConfiguration  config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(userName, password)
                    .setServiceName("pchat")
                    .setHost("3.139.90.132")
                    .setPort(5222)
                    .setConnectTimeout(25000)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) // Do not disable TLS except for test purposes!
                    .setDebuggerEnabled(true)
                    .setSendPresence(true)
                    .build();



            connection1 = new XMPPTCPConnection(config);
            ((XMPPTCPConnection) connection1).setUseStreamManagement(true);           //changed from false to true
            ((XMPPTCPConnection) connection1).setUseStreamManagementResumption(true); //added
            try {
                connection1.connect().login();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            EditText et;
            et=findViewById(R.id.GroupToConnect);
            //set the roomname
            roomname=et.getText().toString();
            TextView tv;




            MessageHandler mh=new MessageHandler();
            //already connected
            if(connection1.isAuthenticated())
            {



                EntityBareJid mucJid = null;
                MultiUserChat muc = null;
                MultiUserChatManager manager = null;

                manager= MultiUserChatManager.getInstanceFor(connection1);
                muc= manager.getMultiUserChat(roomname+"@conference.pchat");


                    /*
                    String roomname="";
                    muc.create("secondRoom");
                    Form form=new Form(DataForm.Type.submit);

                    Form form1 = muc.getConfigurationForm();
                    Form submitForm = form1.createAnswerForm();

                    muc.sendConfigurationForm(form);

                    submitForm.setAnswer("muc#roomconfig_persistentroom", true);
                    muc.sendConfigurationForm(submitForm);

                     */


                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(8);

                try {
                    muc.join(uname,"",history, SmackConfiguration.getDefaultPacketReplyTimeout());
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                Log.d("receiving fn","Joined");
                Log.d("history", String.valueOf(history));




                 tv=findViewById(R.id.MUCChatwindow);
                 tv.setText("\n");



                 muc.addMessageListener(new MessageListener() {
                       public void processMessage(Message message) {

                        Log.d("message",message.getBody());
                        mh.insertMessage(message.getBody(),"null",message.getFrom().toString(),"null");
                        String []address=message.getFrom().split("/");
                        tv.append(address[1]+":"+message.getBody()+"\n");
                        tv.append("\n");

                    }
                });










                /*
                String chathistory="";

                //at this point dispaly the messages
                tv=findViewById(R.id.MUCChatwindow);
                ArrayList<String> messsageList=mh.getAllMessage();
                for(int i=0;i<messsageList.size();i++)
                {
                    chathistory=chathistory+messsageList.get(i)+"\n";
                   // tv.setText(messsageList.get(i)+"\n");
                }

                tv.setText(chathistory);

                 */
            }//end of connection


           // disconnect the connection
            //connection1.disconnect();

            return null;
        }//end of do in background



    }//end of inner class






    //this will just send the messages.
    private class  sendChat extends AsyncTask<Void, Void, Void> {


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

        }



        protected Void doInBackground(Void... voids) {




            if(roomname.equals("unassignedandinvalidroom"))
            {
                return null;
            }
            //join as the second person
            String userName=uname;
            String password=pword;


            XMPPTCPConnectionConfiguration  config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(userName, password)
                    .setServiceName("pchat")
                    .setHost("3.139.90.132")
                    .setPort(5222)
                    .setConnectTimeout(25000)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) // Do not disable TLS except for test purposes!
                    .setDebuggerEnabled(true)
                    .setSendPresence(true)
                    .build();



            connection2 = new XMPPTCPConnection(config);
            ((XMPPTCPConnection) connection2).setUseStreamManagement(true);           //changed from false to true
            ((XMPPTCPConnection) connection2).setUseStreamManagementResumption(true); //added
            try {
                connection2.connect().login();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




            EditText et;
            et=findViewById(R.id.GroupToConnect);

            TextView tv;

            //get the message to send
            String messagetosend="";

            et=(EditText)findViewById(R.id.MessageToSend);
            messagetosend=et.getText().toString();

            MessageHandler mh=new MessageHandler();
            //already connected
            if(connection2.isAuthenticated())
            {



                EntityBareJid mucJid = null;
                MultiUserChat muc = null;
                MultiUserChatManager manager = null;

                manager= MultiUserChatManager.getInstanceFor(connection2);
                muc= manager.getMultiUserChat(roomname+"@conference.pchat");


                tv=findViewById(R.id.MUCChatwindow);
                tv.setText("\n");




                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(8);

                try {
                    muc.join(uname,"",history, SmackConfiguration.getDefaultPacketReplyTimeout());
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                Log.d("Sending fn","Joined");
                Log.d("history", String.valueOf(history));






                try {
                    muc.sendMessage(messagetosend);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();

                }

                et.setText("");



                muc.addMessageListener(new MessageListener() {
                    public void processMessage(Message message) {

                        Log.d("message",message.getBody());
                        mh.insertMessage(message.getBody(),"null",message.getFrom().toString(),"null");
                        String []address=message.getFrom().split("/");
                        tv.append(address[1]+":"+message.getBody()+"\n");
                        tv.append("\n");

                    }
                });






            }//end of connection


            //disconnect the connection
            // connection2.disconnect();

            return null;
        }//end of do in background



    }//end of inner class









    //class for handling db
    private class MessageHandler
    {
        MessageStoreDatabaseAdapter dba1,dba2,dba3;

        MessageHandler()
        {


        }



        public void clearchat()
        {
            dba3=new MessageStoreDatabaseAdapter(context);
            dba3.truncatetable();
        }


        public void insertMessage(String msg, String time, String fromwhom, String whichdb)
        {
            dba1=new MessageStoreDatabaseAdapter(context);
           long a= dba1.addMessage(msg,time,fromwhom, whichdb);
        }


        public ArrayList<String> getAllMessage()
        {
            dba2=new MessageStoreDatabaseAdapter(context);
            ArrayList<String > MessageArrayList=dba2.getAllMessage();
            return MessageArrayList;
        }
    }//end of class



}//end of outer class