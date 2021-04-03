package com.example.pchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;

import java.io.IOException;
import java.util.ArrayList;

public class MyChatroom extends AppCompatActivity {


    public static final String EXTRA_NAME = "a";
    public static final String EXTRA_PWD = "b";
    public static final String EXTRA_ROOM = "c";


    public int MAX_STANZAS= 200000;

    final Context context = this;


    public String uname = "";
    public String pword = "";
    public String  roomname="unassignedandinvalidroom";

    AbstractXMPPConnection connection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chatroom);

        uname=getIntent().getStringExtra(MyChatroom.EXTRA_NAME);
        pword=getIntent().getStringExtra(MyChatroom.EXTRA_PWD);
        roomname=getIntent().getStringExtra(MyChatroom.EXTRA_ROOM);

        String []str=roomname.split("@");
        String groupname=str[0];

        TextView tvv=findViewById(R.id.MyRoomMUCChatwindow);
        tvv.setMovementMethod(new ScrollingMovementMethod());




        new joinChat().execute();



        //send message handler
        Button sendChat_button= findViewById(R.id.MyRoomMessageSendButton);
        sendChat_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new MyChatroom.sendChat().execute();


            }
        });


        FloatingActionButton fb;
        fb=findViewById(R.id.floatingActionButonAddUser);
        fb.setImageBitmap(textAsBitmap("+", 44, Color.WHITE));

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, SendInvitation.class);

                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);
                intent.putExtra(EXTRA_ROOM, groupname);

                startActivity(intent);


            }
        });


        /*
        //handler for clearing the chatwindow
        Button clearchat_button= findViewById(R.id.MyRoomclearchatbutton);
        clearchat_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                MyChatroom.MessageHandler mg=new MyChatroom.MessageHandler();
                mg.clearchat();

                //clear the window

                TextView tvv=findViewById(R.id.MyRoomMUCChatwindow);
                tvv.setText("");



            }
        });


         */


        /*
        //handler for logout
        Button Logout_button= findViewById(R.id.MyRoomLogoutFromChatButton);
        Logout_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){



                if(connection.isConnected())
                {
                    connection.disconnect();
                }

                uname="";
                pword="";

                Intent intent=new Intent(context,MainActivity.class);
                startActivity(intent);

            }
        });

        */

    }//end of oncreate()




    //method to convert your text to image
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }




    /*
    protected void onDestroy(){
        super.onDestroy();


        if(connection.isConnected())
        {
            connection.disconnect();
        }
       // uname="";
        //pword="";
        //Intent intent = new Intent(context, MainActivity.class);
        //startActivity(intent);

        finish();
    }//end of onDestroy


*/



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


            final TextView[] tv = new TextView[1];




            MyChatroom.MessageHandler mh=new MyChatroom.MessageHandler();
            //already connected
            if(connection.isAuthenticated())
            {



                EntityBareJid mucJid = null;
                MultiUserChat muc = null;
                MultiUserChatManager manager = null;

                manager= MultiUserChatManager.getInstanceFor(connection);
                muc= manager.getMultiUserChat(roomname);


                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(MAX_STANZAS);

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




                runOnUiThread(new Runnable(){
                    public void run() {

                        tv[0] = findViewById(R.id.MyRoomMUCChatwindow);
                        tv[0].setText("\n");

                    }
                });


                muc.addMessageListener(new MessageListener() {
                    public void processMessage(Message message) {

                        Log.d("message",message.getBody());
                        mh.insertMessage(message.getBody(),"null",message.getFrom().toString(),"null","no");
                        String []address=message.getFrom().split("/");



                        runOnUiThread(new Runnable(){
                            public void run() {

                                tv[0].append(address[1]+":"+message.getBody()+"\n");
                                tv[0].append("\n");

                            }
                        });



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


            /*
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

            */


            EditText et;

            final TextView[] tv = new TextView[1];

            //get the message to send
            String messagetosend="";

            et=(EditText)findViewById(R.id.MyRoomMessageToSend);
            messagetosend=et.getText().toString();

            MyChatroom.MessageHandler mh=new MyChatroom.MessageHandler();
            //already connected
            if(connection.isAuthenticated())
            {


               EntityBareJid mucJid = null;
                MultiUserChat muc = null;
                MultiUserChatManager manager = null;

                manager= MultiUserChatManager.getInstanceFor(connection);
                muc= manager.getMultiUserChat(roomname);






                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(MAX_STANZAS);

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






                runOnUiThread(new Runnable(){
                    public void run()
                    {

                        tv[0] =findViewById(R.id.MyRoomMUCChatwindow);
                         tv[0].setText("\n");
                        et.setText("");

                    }

                });


                /*
                muc.addMessageListener(new MessageListener() {
                    public void processMessage(Message message) {

                       // Log.d("message",message.getBody());
                       // mh.insertMessage(message.getBody(),"null",message.getFrom().toString(),"null");
                        String []address=message.getFrom().split("/");



                        runOnUiThread(new Runnable(){
                            public void run()
                            {
                                tv[0].append(address[1]+":"+message.getBody()+"\n");
                                tv[0].append("\n");

                            }

                        });


                    }
                });
                */





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


        public void insertMessage(String msg, String time, String fromwhom, String whichdb, String individual_message)
        {
            dba1=new MessageStoreDatabaseAdapter(context);
            long a= dba1.addMessage(msg,time,fromwhom, whichdb, individual_message);
        }


        public ArrayList<String> getAllMessage()
        {
            dba2=new MessageStoreDatabaseAdapter(context);
            ArrayList<String > MessageArrayList=dba2.getAllMessage();
            return MessageArrayList;
        }
    }//end of class













}//end of outer class