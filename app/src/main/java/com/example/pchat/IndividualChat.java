package com.example.pchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.ArrayList;

public class IndividualChat extends AppCompatActivity {





    public static final String EXTRA_NAME = "a";
    public static final String EXTRA_PWD = "b";
    public static final String EXTRA_SENDER = "c";


    final Context context = this;


    public String uname = "";
    public String pword = "";
    public String sender="";

    AbstractXMPPConnection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_chat);





        uname=getIntent().getStringExtra(IndividualChat.EXTRA_NAME);
        pword=getIntent().getStringExtra(IndividualChat.EXTRA_PWD);
        sender=getIntent().getStringExtra(IndividualChat.EXTRA_SENDER);


        TextView tvv=findViewById(R.id.PrivateChatWindow);
        tvv.setMovementMethod(new ScrollingMovementMethod());


        //retrieve the message from the database

        MessageHandlerP mp=new MessageHandlerP();

        ArrayList <String>  senderList=mp.pgetAllSender();
        ArrayList<String> messageList=mp.pgetAllMessage();
        ArrayList<String> Individualmessage=mp.pgetAllIndividual_Message();



        String messagetoDisplay="";

        String []sendername;
        for(int i=0;i<messageList.size();i++)
        {

            sendername=senderList.get(i).split("@");


            if(sendername[0].equals(sender) && Individualmessage.get(i).equals("yes"))
            {
                messagetoDisplay=messagetoDisplay+"\n\n"+sender+":"+messageList.get(i);
            }

        }

        TextView tv=findViewById(R.id.PrivateChatWindow);
        tv.setText(messagetoDisplay);


        new PrivateMessageListen().execute();


    Button privatemessagesendbutton=findViewById(R.id.PrivateSendButton);
        privatemessagesendbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                        new PrivateMessageSend().execute();

            }
        });




    }//end of oncreate










    private class PrivateMessageListen  extends AsyncTask<Void, Void, Void> {


        String result;
        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Fetching...");
            dialog.show();
        }



        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }





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



            if(connection.isAuthenticated())
            {
                Log.w("app", "Auth done");
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                chatManager.addChatListener(
                        new ChatManagerListener() {
                            @Override
                            public void chatCreated(Chat chat, boolean createdLocally)
                            {
                                chat.addMessageListener(new ChatMessageListener()
                                {
                                    @Override
                                    public void processMessage(Chat chat, Message message) {

                                        MessageHandlerP mpp=new MessageHandlerP();
                                        String []sendername=message.getFrom().split("@");
                                        if(sendername[0].equals(sender)) {//display it in the screen

                                            TextView tvv=findViewById(R.id.PrivateChatWindow);

                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    //check whether incoming message is an xml type format
                                                    //if it is an xml its roster request meant for the admin.
                                                    //otherwise  store it and display it
                                                    tvv.append("\n\n"+sender+":"+message.getBody());

                                                }

                                            });

                                        }
                                        else
                                        {//store in db

                                            mpp.pinsertMessage(message.getBody(),"null",message.getFrom(),"null","yes");
                                        }


                                    }
                                });

                                Log.w("app", chat.toString());
                            }
                        });
            }//end of received message


            return null;
        }//end of do in background


    }//end of inner class











//class for sending the private message
    private class PrivateMessageSend  extends AsyncTask<Void, Void, Void> {


        String result;
        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Sending...");
            dialog.show();
        }



        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }





        protected Void doInBackground(Void... voids) {


            String userName=uname;
            String password=pword;


            EditText et=findViewById(R.id.PrivateSendWindow);
            String Messagetosend=et.getText().toString();

            String toPerson=sender+"@pchat";
            TextView tvv=findViewById(R.id.PrivateChatWindow);


            if(connection.isAuthenticated())
            {

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

                        et.setText("");
                        tvv.append("\n\n"+uname+":"+Messagetosend);

                    }
                });





            }//end of received message


            return null;
        }//end of do in background


    }//end of inner class








    private class MessageHandlerP
    {
        MessageStoreDatabaseAdapter pdba1,pdba2,pdba3;

        MessageHandlerP()
        {


        }
        public void pclearchat()
        {
            pdba3=new MessageStoreDatabaseAdapter(context);
            pdba3.truncatetable();
        }


        public void pinsertMessage(String msg, String time, String sender, String groupname, String individual_message)
        {
            pdba1=new MessageStoreDatabaseAdapter(context);
            long a= pdba1.addMessage(msg,time, sender, groupname, individual_message);
        }


        public ArrayList<String> pgetAllMessage()
        {
            pdba2=new MessageStoreDatabaseAdapter(context);
            ArrayList<String > MessageArrayList=pdba2.getAllMessage();
            return MessageArrayList;
        }




        public ArrayList<String> pgetAllTime()
        {
            pdba2=new MessageStoreDatabaseAdapter(context);
            ArrayList<String > TimeArrayList=pdba2.getAllTime();
            return TimeArrayList;
        }



        public ArrayList<String> pgetAllSender()
        {
            pdba2=new MessageStoreDatabaseAdapter(context);
            ArrayList<String > SenderArrayList=pdba2.getAllSender();
            return SenderArrayList;
        }


        public ArrayList<String> pgetAllGroupName()
        {
            pdba2=new MessageStoreDatabaseAdapter(context);
            ArrayList<String > GroupNameArrayList=pdba2.getAllGroupname();
            return GroupNameArrayList;
        }


        public ArrayList<String> pgetAllIndividual_Message()
        {
            pdba2=new MessageStoreDatabaseAdapter(context);
            ArrayList<String > IndividualMessageArrayList=pdba2.getAllIndividualMessage();
            return IndividualMessageArrayList;
        }


    }//end of class







}//end of outer class