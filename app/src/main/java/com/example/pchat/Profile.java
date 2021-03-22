package com.example.pchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Element;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;


import java.io.IOException;
import java.util.Collection;



import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class Profile extends AppCompatActivity {



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
        setContentView(R.layout.activity_profile);



        uname=getIntent().getStringExtra(Profile.EXTRA_NAME);
        pword=getIntent().getStringExtra(Profile.EXTRA_PWD);


        //listen for any messages
        new listenForMessages().execute();

        //create chatroom handler
        Button createchatroom_button= findViewById(R.id.createchatbutton);
        createchatroom_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(context, CreateGroup.class);
                intent.putExtra(EXTRA_PWD, pword);
               intent.putExtra(EXTRA_NAME, uname);
                startActivity(intent);


                //new createChatRoom().execute();



                //Toast.makeText(preacher.this,  "Posted",  Toast.LENGTH_LONG).show();
            }
        });



            //send invite handler
        Button sendinvite_button= findViewById(R.id.sendinvitationbutton);
        sendinvite_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(context, SendInvitation.class);
                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);
                startActivity(intent);



            }
        });


        //join chatroom handler
        Button MUCChat_button= findViewById(R.id.MUCChatButton);
        MUCChat_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                Intent intent = new Intent(context, JoinChatroom.class);
                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);
                startActivity(intent);

            }
        });




        //logout handler
        Button logout_button= findViewById(R.id.LogoutFromProfileButton);
        logout_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                uname="";
                pword="";
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

                //Toast.makeText(preacher.this,  "Posted",  Toast.LENGTH_LONG).show();
            }
        });



    }//end of oncreate










    private class  createChatRoom extends AsyncTask<Void, Void, Void> {
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





                EntityBareJid mucJid = null;
                MultiUserChat muc = null;
                MultiUserChatManager manager = null;





            }//end of connection


            return null;
        }//end of do in background



    }//end of inner class



    //listen for any messages

    private class  listenForMessages extends AsyncTask<Void, Void, Void> {
        String result;

        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Connecting...");
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




            //already connected
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

                                        runOnUiThread(new Runnable(){
                                            public void run()
                                            {


                                                //check whether incoming message is an xml type format
                                                int valid_xml=CheckforXML(message.getBody());
                                                if(valid_xml==1)
                                                {

                                                    //send a roster request to the new client
                                                    String xmlstring=message.getBody();
                                                    DocumentBuilder builder = null;
                                                    try {
                                                        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                                                    } catch (ParserConfigurationException e) {
                                                        e.printStackTrace();
                                                    }
                                                    InputSource src = new InputSource();
                                                    src.setCharacterStream(new StringReader(xmlstring));
                                                    Document doc = null;
                                                    try {
                                                        doc = builder.parse(src);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } catch (SAXException e) {
                                                        e.printStackTrace();
                                                    }

                                                    //check if the type==1
                                                    String type=doc.getElementsByTagName("type").item(0).getTextContent();
                                                    String groupname = doc.getElementsByTagName("groupname").item(0).getTextContent();
                                                    String jid = groupname+"@conference.pchat";


                                                    if(type.equals("one"))
                                                    {
                                                        Roster roster = Roster.getInstanceFor(connection);
                                                        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
                                                        try {
                                                            roster.createEntry(groupname, jid, null);
                                                        } catch (SmackException.NotLoggedInException e) {
                                                            e.printStackTrace();
                                                        } catch (SmackException.NoResponseException e) {
                                                            e.printStackTrace();
                                                        } catch (XMPPException.XMPPErrorException e) {
                                                            e.printStackTrace();
                                                        } catch (SmackException.NotConnectedException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }//end of type =1


                                                    /*//prayer requests
                                                    if(type.equals("two")) {
                                                        //put the requests in a db

                                                        String m_time=doc.getElementsByTagName("time").item(0).getTextContent();
                                                        String m_name = doc.getElementsByTagName("name").item(0).getTextContent();
                                                        String m_matter=doc.getElementsByTagName("prayer").item(0).getTextContent();
                                                        InsertPrayerRequestsIntoDB ip=new InsertPrayerRequestsIntoDB();
                                                        ip.insertrequest(m_name, m_time, m_matter);

                                                    }

                                                     */




                                                }//end of valid xml


                                            }//end of run

                                        });



                                    }
                                });

                                Log.w("app", chat.toString());
                            }// end of chat created
                        });










            }//end of connection



            return null;
        }//end of do in background




        int CheckforXML(String ms)
        {
            try {
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(ms)));
                return 1;
            } catch (Exception e) {
                return 0;
            }
        }



    }//end of inner class














}//end of outer class