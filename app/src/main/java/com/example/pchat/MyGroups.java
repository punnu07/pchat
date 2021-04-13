package com.example.pchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kotlinx.coroutines.channels.Send;


public class MyGroups extends AppCompatActivity {



    Set<String> hash_Set = new HashSet<String>();



    public static final String EXTRA_NAME = "a";
    public static final String EXTRA_PWD = "b";
    public static final String EXTRA_SENDER = "c";

    public static final String EXTRA_ROOM = "c";


    final Context context = this;


    public String uname = "";
    public String pword = "";


    AbstractXMPPConnection connection;

    CardView cardview,cardview2;
    LinearLayout.LayoutParams layoutparams,layoutparams2;

    RelativeLayout.LayoutParams rlayoutparams,rlayoutparams2,rlayoutparams3;
    TextView textview,textview2;

    EditText MessageToSend;

    LinearLayout LinearLayoutView;

    ScrollView sv;
    CardView []cv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);



        uname=getIntent().getStringExtra(MyGroups.EXTRA_NAME);
        pword=getIntent().getStringExtra(MyGroups.EXTRA_PWD);


        new GetMyGroups().execute();




        layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );





        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();


        //sv=new ScrollView(context);
        LinearLayoutView = new LinearLayout(this);
        LinearLayoutView.setOrientation(LinearLayout.VERTICAL);

        GroupInfoDatabaseAdapter gdba = new GroupInfoDatabaseAdapter(context);

        ArrayList<String> GroupList=gdba.getAllGroups();



        cv=new CardView[GroupList.size()];

         sv=new ScrollView(context);

        for (int i=0; i<GroupList.size();i++){

            cv[i] = new CardView(context);

            layoutparams.setMargins(5,5,5,5);
            cv[i].setLayoutParams(layoutparams);
            cv[i].setRadius(5);
            cv[i].setPadding(5, 5, 5, 5);
            cv[i].setCardBackgroundColor(0xFDFDFDFF);
            cv[i].setMaxCardElevation(10);


           String []firstname=GroupList.get(i).split("@");


            Button groupbutton=new Button(context);
            groupbutton.setText(firstname[0]);

            hash_Set.add(firstname[0]);
            //groupbutton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            groupbutton.setBackgroundColor(Color.WHITE);
            groupbutton.setPadding(25,25,25,25);

            groupbutton.setTextColor(0xFF6EA470);
            cv[i].addView(groupbutton);


            int finalI = i;
            groupbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Intent intent = new Intent(context, MyChatroom.class);
                    intent.putExtra(EXTRA_PWD, pword);
                    intent.putExtra(EXTRA_NAME, uname);
                    intent.putExtra(EXTRA_ROOM,GroupList.get(finalI));


                    startActivity(intent);


                }
            });

            //cv[i].addView(textview);

            cv[i].setClickable(true);
            LinearLayoutView.addView(cv[i]);

        }//end of all the groups



        //now add the individual senders
        MessageStoreDatabaseAdapter msda=new MessageStoreDatabaseAdapter(context);
        ArrayList <String> userList=msda.getAllSender();
        ArrayList <String> recList=msda.getAllRecepients();


        ArrayList <String> Individualsenderlist=msda.getAllIndividualMessage();



        for(int i=0;i<userList.size();i++)
        {
            String user= userList.get(i);

            if(!hash_Set.contains(user) && !hash_Set.contains(recList.get(i)) && Individualsenderlist.get(i).equals("yes")) {

                if(user.equals(uname))
                {
                    hash_Set.add(recList.get(i));
                }
                else
                    {
                    hash_Set.add(user);
                }

                CardView cvv = new CardView(context);

                layoutparams.setMargins(5, 5, 5, 5);
                cvv.setLayoutParams(layoutparams);
                cvv.setRadius(5);
                cvv.setPadding(5, 5, 5, 5);
                cvv.setCardBackgroundColor(0xFDFDFDFF);
                cvv.setMaxCardElevation(10);


                Button groupbutton = new Button(context);


                if(user.equals(uname))
                {
                    groupbutton.setText(recList.get(i));
                }
                else {
                    groupbutton.setText(user);
                }
                    groupbutton.setBackgroundColor(Color.WHITE);
                groupbutton.setPadding(25, 25, 25, 25);

                groupbutton.setTextColor(0xFF6EA470);



                cvv.addView(groupbutton);


                int finalI = i;
                groupbutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //Intent intent = new Intent(context, IndividualChat.class);
                        Intent intent = new Intent(context, IndividualChat2.class);
                        intent.putExtra(EXTRA_PWD, pword);
                        intent.putExtra(EXTRA_NAME, uname);

                        if(user.equals(uname)) {
                            intent.putExtra(EXTRA_SENDER, recList.get(finalI));
                        }
                         else
                        {
                            intent.putExtra(EXTRA_SENDER, user);
                        }

                        startActivity(intent);

                    }
                });

                //cv[i].addView(textview);
                cvv.setClickable(true);
                LinearLayoutView.addView(cvv);
            }


       }










        FloatingActionButton fab = new FloatingActionButton(this);
        fab.setId(View.generateViewId());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(context, CreateGroup.class);
                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);
                startActivity(intent);

            }
        });
        //fab.setImageResource(R.drawable.plus);
        fab.setElevation(2);
        fab.setBackgroundResource(R.drawable.plus);
        //fab.setBackgroundDrawable(Drawable.createFromPath("/home/punnoose/plus.png"));
       // fab.setSize(android.support.design.widget.FloatingActionButton.SIZE_MINI);
        fab.setFocusable(true);

        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //lay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //lay.addRule(RelativeLayout.ALIGN_PARENT_END);


        fab.setBackgroundColor(0xFFE27070);
        fab.setBackgroundTintList(ColorStateList.valueOf(0xFFE27070));


        lay.setMargins(8*screenWidth/10,10,2,2);
        fab.setLayoutParams(lay);

        fab.setImageBitmap(textAsBitmap("+", 44, Color.WHITE));



        LinearLayoutView.addView(fab);





        sv.addView(LinearLayoutView);

        setContentView(sv);








    }//end of oncreate



    protected void onDestroy(){
        super.onDestroy();


        if(connection.isConnected())
        {
            connection.disconnect();
        }
        uname="";
        pword="";
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);

        finish();
    }//end of onDestroy






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
            if(connection.isAuthenticated()) {


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

                                                Log.d("message received", message.getBody());
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

                                                    //one means a user has to be added to a group
                                                    if(type.equals("one"))
                                                    {

                                                        String groupname = doc.getElementsByTagName("groupname").item(0).getTextContent();
                                                        String jid = groupname+"@conference.pchat";

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

                                                    //type=2 means individual text message with time
                                                    if(type.equals("two")) {

                                                        String content = doc.getElementsByTagName("content").item(0).getTextContent();
                                                        String time =  doc.getElementsByTagName("time").item(0).getTextContent();
                                                        String sender =  doc.getElementsByTagName("sender").item(0).getTextContent();
                                                        MessageHandlerG mggg=new MessageHandlerG();
                                                        mggg.ginsertMessage(content,time, sender,"null","yes",uname,"two");

                                                    }



                                                    if(type.equals("three"))
                                                    {

                                                        String content = doc.getElementsByTagName("content").item(0).getTextContent();
                                                        String time =  doc.getElementsByTagName("time").item(0).getTextContent();
                                                        String sender =  doc.getElementsByTagName("sender").item(0).getTextContent();

                                                        MessageHandlerG mggg=new MessageHandlerG();
                                                        mggg.ginsertMessage(content,time, sender,"null","yes",uname,"three");


                                                    }//end of type =3




                                                }//end of valid xml
                                                //if message coming is not xml then it means that it is a message from another user and a text message
                                                else
                                                {

                                                    /*
                                                   MessageHandlerG mg=new MessageHandlerG();
                                                   mg.ginsertMessage(message.getBody(),"null", message.getFrom().toString(),"null","yes");
                                                    String []messageFrom=message.getFrom().split("@");


                                                    if(!hash_Set.contains(messageFrom[0]))                                                    //groupbutton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                                    {

                                                        hash_Set.add(messageFrom[0]);

                                                        CardView cvv = new CardView(context);

                                                        layoutparams.setMargins(5, 5, 5, 5);
                                                        cvv.setLayoutParams(layoutparams);
                                                        cvv.setRadius(5);
                                                        cvv.setPadding(5, 5, 5, 5);
                                                        cvv.setCardBackgroundColor(0xFDFDFDFF);
                                                        cvv.setMaxCardElevation(10);


                                                        Button groupbutton = new Button(context);


                                                        groupbutton.setText(messageFrom[0]);

                                                        groupbutton.setBackgroundColor(Color.WHITE);
                                                        groupbutton.setPadding(25, 25, 25, 25);

                                                        groupbutton.setTextColor(0xFF6EA470);
                                                        cvv.addView(groupbutton);


                                                        groupbutton.setOnClickListener(new View.OnClickListener() {
                                                            public void onClick(View v) {

                                                                //Intent intent = new Intent(context, IndividualChat.class);

                                                                Intent intent = new Intent(context, IndividualChat2.class);
                                                                intent.putExtra(EXTRA_PWD, pword);
                                                                intent.putExtra(EXTRA_NAME, uname);
                                                                intent.putExtra(EXTRA_SENDER, messageFrom[0]);
                                                                startActivity(intent);


                                                            }
                                                        });

                                                        //cv[i].addView(textview);

                                                        cvv.setClickable(true);


                                                        runOnUiThread(new Runnable() {
                                                            public void run() {

                                                                LinearLayoutView.addView(cvv);
                                                                //sv.addView();
                                                            }
                                                        });


                                                    }//add to user list



                                                    */

                                                }//end of individual message

                                    } //end of process message
                                });

                                Log.w("app", chat.toString());
                            }// end of chat created
                        });




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
                InsertGroupInfoIntoDB ig=new InsertGroupInfoIntoDB();

                //tuncate group db first
                ig.truncate();

                for (RosterEntry entry : entries) {

                    //String toPerson = entry.getName();
                    String groupname= entry.getName();
                    ig.insertGroup(groupname);

                }//end of roster entries




           }//end of connection

        return null;
        }//end of doinbackground




        int CheckforXML(String ms)
        {
            try {
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(ms)));
                return 1;
            } catch (Exception e) {
                return 0;
            }
        }








        //class for  inserting groupname into the group.
        private class InsertGroupInfoIntoDB  {

            String  groupname;

            public void insertGroup(String g)
            {
                groupname=g;

                GroupInfoDatabaseAdapter gda = new GroupInfoDatabaseAdapter(context);
                Log.d("DB created", "done");
                gda.addGroup(groupname);

            }

            public void truncate() {

                GroupInfoDatabaseAdapter gdb = new GroupInfoDatabaseAdapter(context);
                gdb.truncatetable();

            }
        }//wnd of db insert class




        //class for db for message storing


        private class MessageHandlerG
        {
            MessageStoreDatabaseAdapter gdba1,gdba2,gdba3;

            MessageHandlerG()
            {


            }
            public void gclearchat()
            {
                gdba3=new MessageStoreDatabaseAdapter(context);
                gdba3.truncatetable();
            }


            public void ginsertMessage(String msg, String time, String sender, String groupname, String individual_message, String rec, String type)
            {
                gdba1=new MessageStoreDatabaseAdapter(context);
                long a= gdba1.addMessage(msg,time, sender, groupname, individual_message, rec,type);
            }


            public ArrayList<String> ggetAllMessage()
            {
                gdba2=new MessageStoreDatabaseAdapter(context);
                ArrayList<String > MessageArrayList=gdba2.getAllMessage();
                return MessageArrayList;
            }




            public ArrayList<String> ggetAllTime()
            {
                gdba2=new MessageStoreDatabaseAdapter(context);
                ArrayList<String > TimeArrayList=gdba2.getAllTime();
                return TimeArrayList;
            }



            public ArrayList<String> ggetAllSender()
            {
                gdba2=new MessageStoreDatabaseAdapter(context);
                ArrayList<String > SenderArrayList=gdba2.getAllSender();
                return SenderArrayList;
            }


            public ArrayList<String> ggetAllGroupName()
            {
                gdba2=new MessageStoreDatabaseAdapter(context);
                ArrayList<String > GroupNameArrayList=gdba2.getAllGroupname();
                return GroupNameArrayList;
            }


            public ArrayList<String> ggetAllIndividual_Message()
            {
                gdba2=new MessageStoreDatabaseAdapter(context);
                ArrayList<String > IndividualMessageArrayList=gdba2.getAllIndividualMessage();
                return IndividualMessageArrayList;
            }



            public ArrayList<String> ggetAllType()
            {
                gdba2=new MessageStoreDatabaseAdapter(context);
                ArrayList<String > TypeArrayList=gdba2.getAllType();
                return TypeArrayList;
            }










        }//end of class










    }//end of inner class













}//end of outer class