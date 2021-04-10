package com.example.pchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;


import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.ping.android.ServerPingWithAlarmManager;
import org.json.JSONObject;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;


public class IndividualChat2 extends AppCompatActivity {





    public static final String EXTRA_NAME = "a";
    public static final String EXTRA_PWD = "b";
    public static final String EXTRA_SENDER = "c";


    final Context context = this;



    //uname is the login name
    //sender is the person whom which  individual conversation is happening

    public String uname = "";
    public String pword = "";
    public String sender="";

    AbstractXMPPConnection connection;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;



    CardView cardview,cardview2;
    LinearLayout.LayoutParams layoutparams,layoutparams2,layoutParamsimg;

    RelativeLayout.LayoutParams rlayoutparams,rlayoutparams2,rlayoutparams3, rlayoutparams4, rlayoutparams5,rlayoutparams6;
    TextView textview,textview2;

    EditText MessageToSend;

    LinearLayout LinearLayoutView, LinearLayoutViewBig;

    ScrollView sv;
    CardView []cv;





    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_chat2);


        uname=getIntent().getStringExtra(IndividualChat2.EXTRA_NAME);
        pword=getIntent().getStringExtra(IndividualChat2.EXTRA_PWD);
        sender=getIntent().getStringExtra(IndividualChat2.EXTRA_SENDER);

        IndividualChat2.MessageHandlerP mp=new IndividualChat2.MessageHandlerP();

        ArrayList <String>  senderList=mp.pgetAllSender();
        ArrayList <String> messageList=mp.pgetAllMessage();
        ArrayList <String> Individualmessage=mp.pgetAllIndividual_Message();
        ArrayList <String> timeList=mp.pgetAllTime();
        ArrayList <String> typeList=mp.pgetAllType();



        List <timeIndex> ti=maketimeIndexList(timeList);

        Collections.sort(ti, new Comparator<timeIndex>() {
            @Override
            public int compare(timeIndex o1, timeIndex o2) {

                String date1=o1.time;
                String date2=o2.time;
                String []date1sep=date1.split("-");
                String []date2sep=date2.split("-");


                if(Integer.valueOf(date1sep[2])<Integer.valueOf(date2sep[2]))
                {
                    return -1;
                }
                if(Integer.valueOf(date1sep[2])>Integer.valueOf(date2sep[2]))
                {
                    return 1;
                }
                if(Integer.valueOf(date1sep[2])==Integer.valueOf(date2sep[2]))
                {
                    if(Integer.valueOf(date1sep[1])<Integer.valueOf(date2sep[1]))
                    {
                        return -1;
                    }
                    if(Integer.valueOf(date1sep[1])>Integer.valueOf(date2sep[1]))
                    {
                        return 1;
                    }
                    if(Integer.valueOf(date1sep[1])==Integer.valueOf(date2sep[1]))
                    {
                        if(Integer.valueOf(date1sep[0])<Integer.valueOf(date2sep[0]))
                        {
                            return -1;
                        }

                        if(Integer.valueOf(date1sep[0])>Integer.valueOf(date2sep[0]))
                        {
                            return 1;
                        }
                        if(Integer.valueOf(date1sep[0])==Integer.valueOf(date2sep[0]))
                        {
                            if(Integer.valueOf(date1sep[3])<Integer.valueOf(date2sep[3]))
                            {
                                return -1;
                            }

                            if(Integer.valueOf(date1sep[3])>Integer.valueOf(date2sep[3]))
                            {
                                return 1;
                            }

                            if(Integer.valueOf(date1sep[3])==Integer.valueOf(date2sep[3]))
                            {
                                if(Integer.valueOf(date1sep[4])<Integer.valueOf(date2sep[4]))
                                {
                                    return -1;
                                }

                                if(Integer.valueOf(date1sep[4])>Integer.valueOf(date2sep[4]))
                                {
                                    return 1;
                                }
                                if(Integer.valueOf(date1sep[4])==Integer.valueOf(date2sep[4]))
                                {
                                    if(Integer.valueOf(date1sep[5])<Integer.valueOf(date2sep[5]))
                                    {
                                        return -1;
                                    }

                                    if(Integer.valueOf(date1sep[5])>Integer.valueOf(date2sep[5]))
                                    {
                                        return 1;
                                    }

                                    if(Integer.valueOf(date1sep[5])==Integer.valueOf(date2sep[5]))
                                    {
                                        return 0;
                                    }

                                }

                            }

                        }
                   }
                }
               return 0;
            }
        });









        RelativeLayout rlayoutView = new RelativeLayout(this);
        rlayoutparams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        rlayoutparams2=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        rlayoutparams3=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        rlayoutparams4=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        rlayoutparams5=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        rlayoutparams6=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);


        //rlayoutparams2.setLayoutDirection(LinearLayout.HORIZONTAL);




        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();




        layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        layoutparams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        layoutParamsimg=new LinearLayout.LayoutParams(screenWidth/2, screenWidth/2 );


        sv=new ScrollView(context);

        LinearLayoutView = new LinearLayout(this);
        LinearLayoutView.setOrientation(LinearLayout.VERTICAL);

        LinearLayoutViewBig = new LinearLayout(this);
        LinearLayoutViewBig.setOrientation(LinearLayout.VERTICAL);


        int num_messages_to_display=0;
        String sendername;


        //get the num of cards to display
        for(int i=0;i<messageList.size();i++)
        {

            sendername=senderList.get(i);
            if(sendername.equals(sender) && Individualmessage.get(i).equals("yes"))
            {
                num_messages_to_display++;
                //messagetoDisplay=messagetoDisplay+"\n\n"+sender+":"+messageList.get(i);
            }
            if(sendername.equals(uname) && Individualmessage.get(i).equals("yes"))
            {
                num_messages_to_display++;
                //messagetoDisplay=messagetoDisplay+"\n\n"+sender+":"+messageList.get(i);
            }


        }




        cv=new CardView[num_messages_to_display];
        int j=0;



        //diplay the messages

        String []nowtime=(DateFormat.format("dd-MM-yyyy-hh-mm-ss", new java.util.Date()).toString()).split("-");
        int todaydate=Integer.valueOf(nowtime[0]);



        for(int k=0;k<ti.size();k++)
        {
          int i=ti.get(k).index;

         if((senderList.get(i).equals(sender)||senderList.get(i).equals(uname)) && Individualmessage.get(i).equals("yes"))
         {


                    String type=typeList.get(i);

                    if(type.equals("two"))
                    {

                        cv[j] = new CardView(context);

                        layoutparams.setMargins(15,15,15,15);
                        cv[j].setLayoutParams(layoutparams);
                        cv[j].setRadius(15);
                        cv[j].setPadding(15, 5, 15, 5);
                        cv[j].setCardBackgroundColor(0xFDFDFDFF);
                        cv[j].setMaxCardElevation(10);

                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cv[j].getLayoutParams();
                        lp.gravity=Gravity.LEFT;


                        TextView tv = new TextView(context);
                        tv.setLayoutParams(layoutparams);

                        if(senderList.get(i).equals(sender)) {
                            cv[j].setCardBackgroundColor(0xFDFDFDFF);

                            tv.setText(" " + sender + ": " + messageList.get(i));
                           String []messagetime=timeList.get(i).split("-");
                           int  messagedate=Integer.valueOf(messagetime[0]);
                           String timeval=messagetime[3]+":"+messagetime[4];
                           String dateval=messagetime[0]+":"+messagetime[1]+":"+messagetime[2];
                           if(todaydate==messagedate)
                           {
                               tv.setTextSize(6);
                               tv.setTextColor(Color.GRAY);
                               int spaceslen=messageList.get(i).length();
                               tv.append("\n");
                               for(int r=0;r<spaceslen;r++) {
                               tv.append("\t");
                               }
                               tv.append(timeval);
                               tv.setTextSize(12);
                               tv.setTextColor(Color.BLACK);

                           }
                           else
                           {
                               tv.setTextSize(6);
                               tv.setTextColor(Color.GRAY);

                               int spaceslen=messageList.get(i).length();
                               tv.append("\n");
                               for(int r=0;r<spaceslen;r++) {
                                   tv.append("\t");
                               }

                               tv.append(dateval+" "+timeval);
                               tv.setTextSize(12);
                               tv.setTextColor(Color.BLACK);
                           }


                        }


                        if(senderList.get(i).equals(uname))
                        {
                            cv[j].setCardBackgroundColor(0xFFFFFFCC);

                            tv.setText("me: " + messageList.get(i));

                            String []messagetime=timeList.get(i).split("-");
                            int  messagedate=Integer.valueOf(messagetime[0]);
                            String timeval=messagetime[3]+":"+messagetime[4];
                            String dateval=messagetime[0]+":"+messagetime[1]+":"+messagetime[2];
                            if(todaydate==messagedate)
                            {
                                tv.setTextSize(10);
                                tv.setTextColor(Color.GRAY);

                                int spaceslen=messageList.get(i).length();
                                tv.append("\n");
                                for(int r=0;r<spaceslen;r++) {
                                    tv.append("\t");
                                }
                                tv.append(timeval);
                                tv.setTextSize(12);
                                tv.setTextColor(Color.BLACK);

                            }
                            else
                            {
                                tv.setTextSize(10);
                                tv.setTextColor(Color.GRAY);
                                int spaceslen=messageList.get(i).length();
                                tv.append("\n");
                                for(int r=0;r<spaceslen;r++) {
                                    tv.append("\t");
                                }
                                tv.append(dateval+" "+timeval);
                                tv.setTextSize(12);
                                tv.setTextColor(Color.BLACK);
                            }



                        }
                        cv[j].addView(tv);
                        LinearLayoutView.addView(cv[j]);


                        j++;
                    }

                   if(type.equals("three")) {

                       /*
                        String imgurl=doc.getElementsByTagName("path").item(0).getTextContent();
                        //download the image
                        try {
                            downloadImage(imgurl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        */

                    }//end of type 3





            }//end of relevant message between sender and uname










        }//end of messages






        layoutparams2.height=8*screenHeight/10;

        //LinearLayoutView.setLayoutParams(layoutparams2);


        sv.addView(LinearLayoutView);
         sv.setLayoutParams(layoutparams2);
         sv.setId(101010);

        rlayoutView.addView(sv);







//new card view at the bottom

        CardView cvv = new CardView(context);
        cvv.setRadius(0);
        cvv.setPadding(25, 25, 25, 15);
        cvv.setCardBackgroundColor(0xFCFCFCFF);
        cvv.setMaxCardElevation(10);
        cvv.setId(202020);


        EditText ett=new EditText(context);
        rlayoutparams3.width=7*screenWidth/10;
        rlayoutparams3.height=RelativeLayout.LayoutParams.MATCH_PARENT;
        //rlayoutparams3.addRule(RelativeLayout.ALIGN_PARENT_START);
        rlayoutparams3.alignWithParent=true;



        rlayoutparams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ett.setLayoutParams(rlayoutparams3);
        ett.setGravity(Gravity.BOTTOM);
        ett.setTextSize(14);
        ett.setId(303030);

        cvv.addView(ett);
        cvv.setLayoutParams(rlayoutparams2);
        rlayoutView.addView(cvv);








        rlayoutparams2.height=2*screenHeight/10;
        rlayoutparams2.width=rlayoutparams3.width;
        rlayoutparams.setMargins(15,15,15,35);

      rlayoutparams2.addRule(RelativeLayout.BELOW,101010);
      rlayoutparams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);





      //cardview for buttons
        CardView cvvv = new CardView(context);
        cvvv.setRadius(0);
        cvvv.setPadding(5, 5, 5, 5);
        cvvv.setCardBackgroundColor(0xFCFCFCFF);
        cvvv.setMaxCardElevation(0);
        cvvv.setId(404040);


        rlayoutparams6.setLayoutDirection(LinearLayout.VERTICAL);
        rlayoutparams6.addRule(RelativeLayout.RIGHT_OF,202020);
        rlayoutparams6.addRule(RelativeLayout.BELOW,101010);
        rlayoutparams6.height=2*screenHeight/10;

        cvvv.setLayoutParams(rlayoutparams6);





        Button individualmessagesend_button=new Button(context);
        individualmessagesend_button.setId(505050);
        individualmessagesend_button.setText("Send");
        //individualmessagesend_button.setBackgroundColor(0xFF6EA470);
        individualmessagesend_button.setTextColor(Color.WHITE);
        individualmessagesend_button.setLayoutParams(rlayoutparams4);
        LinearLayoutViewBig.addView(individualmessagesend_button);



        Button takepic_button=new Button(context);
        takepic_button.setId(606060);
        rlayoutparams5.addRule(RelativeLayout.BELOW,505050);
        takepic_button.setLayoutParams(rlayoutparams5);
        takepic_button.setText("pic");
        //takepic_button.setBackgroundColor(0xFF6EA470);
        takepic_button.setTextColor(Color.WHITE);
        LinearLayoutViewBig.addView(takepic_button);




        rlayoutparams4.addRule(RelativeLayout.ABOVE,606060);

        cvvv.addView(LinearLayoutViewBig);
        rlayoutView.addView(cvvv);


        setContentView(rlayoutView);




        //check for incoming messages
        new IndividualChat2.PrivateMessageListen().execute();




//event handler for message send button
        Button privatemessagesendbutton=findViewById(505050);
        privatemessagesendbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new IndividualChat2.PrivateMessageSend().execute();

            }
 });





//event handler for picture upload button
        Button picuploadbutton=findViewById(606060);
        picuploadbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {



                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.i("Camera", "IOException");
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(context, "com.example.pchat.fileprovider",
                                photoFile);

                        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                        // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

                    }
                }








            }
        });











    }//end of oncreate



    protected void onDestroy(){
        super.onDestroy();

if(connection.isConnected())
{
    connection.disconnect();
}


        uname="";
        pword="";


        finish();
    }//end



    @RequiresApi(api = Build.VERSION_CODES.N)
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,   ".jpg",     storageDir );

        // File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        //Log.d("Image Path", storageDir.toString());
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath =  image.getAbsolutePath();
        Log.d("Image Path is", mCurrentPhotoPath);

        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");

            try {
                mImageBitmap=  MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
            } catch (IOException e) {
                e.printStackTrace();

            }



            new IndividualChat2.FileUpload().execute();



        }
    }







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
            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
            reconnectionManager.enableAutomaticReconnection();

          PingManager  pingManager = PingManager.getInstanceFor(connection);
            pingManager.setPingInterval(480);

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
                ServerPingWithAlarmManager.getInstanceFor(connection).setEnabled(true);
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



                                        Log.d("message received", message.getBody());
                                        //check whether incoming message is an xml type format
                                        int valid_xml=CheckforXML(message.getBody());
                                        if(valid_xml==1)
                                        {

                                            //this could be a roster message for group invitation or an individual message
                                            //for image
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


                                            //type two
                                            if(type.equals("two")) {

                                                String content = doc.getElementsByTagName("content").item(0).getTextContent();
                                                String time =  doc.getElementsByTagName("time").item(0).getTextContent();

                                                String fromwhom =  doc.getElementsByTagName("sender").item(0).getTextContent();


                                                String []nowtime=(DateFormat.format("dd-MM-yyyy-hh-mm-ss", new java.util.Date()).toString()).split("-");
                                                int todaydate=Integer.valueOf(nowtime[0]);

                                                //receiver is uname

                                                //first inserrt the message into the db

                                                MessageHandlerP mp=new MessageHandlerP();
                                                mp.pinsertMessage(content,time,fromwhom,"null","yes",uname,"two");

                                                //if the person who sent the message is the sender, then diplay it

                                                if(fromwhom.equals(sender))
                                                {
                                                    //display the message
                                                    CardView cvj=new CardView(context);
                                                    layoutparams.setMargins(15,15,15,15);
                                                    cvj.setLayoutParams(layoutparams);
                                                    cvj.setRadius(15);
                                                    cvj.setPadding(15, 5, 15, 5);
                                                    cvj.setCardBackgroundColor(0xFDFDFDFF);
                                                    cvj.setMaxCardElevation(10);

                                                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cvj.getLayoutParams();
                                                    lp.gravity=Gravity.LEFT;




                                                    TextView tv=new TextView(context);
                                                    tv.setLayoutParams(layoutparams);


                                                    cvj.setCardBackgroundColor(0xFDFDFDFF);

                                                    tv.setText(" " + sender + ": " + content);
                                                    String []messagetime=time.split("-");
                                                    int  messagedate=Integer.valueOf(messagetime[0]);
                                                    String timeval=messagetime[3]+":"+messagetime[4];
                                                    String dateval=messagetime[0]+":"+messagetime[1]+":"+messagetime[2];
                                                    if(todaydate==messagedate)
                                                    {
                                                        tv.setTextSize(6);
                                                        tv.setTextColor(Color.GRAY);
                                                        int spaceslen=content.length();                                                        tv.append("\n");
                                                        for(int r=0;r<spaceslen;r++) {
                                                            tv.append("\t");
                                                        }
                                                        tv.append(timeval);
                                                        tv.setTextSize(12);
                                                        tv.setTextColor(Color.BLACK);

                                                    }
                                                    else
                                                    {

                                                        tv.setTextSize(6);
                                                        tv.setTextColor(Color.GRAY);

                                                        int spaceslen=content.length();
                                                        tv.append("\n");
                                                        for(int r=0;r<spaceslen;r++) {
                                                            tv.append("\t");
                                                        }

                                                        tv.append(dateval+" "+timeval);
                                                        tv.setTextSize(12);
                                                        tv.setTextColor(Color.BLACK);
                                                    }
                                                    cvj.addView(tv);
                                                    runOnUiThread(new Runnable() {
                                                                      public void run() {

                                                                          LinearLayoutView.addView(cvj);
                                                                      }

                                                    });

                                                }//if sender is the  current window






                                            }//end of two






                                            /*
                                            if(type.equals("three"))
                                            {
                                                String []sendername=message.getFrom().split("@");
                                                if(sendername[0].equals(sender)) {//display it in the screen

                                                    String imgurl=doc.getElementsByTagName("path").item(0).getTextContent();









                                                    TextView tv=new TextView(context);
                                                    tv.setLayoutParams(layoutparams);
                                                    tv.setText(sender+":\n");

                                                    CardView cvi = new CardView(context);

                                                    layoutparams.setMargins(15,15,15,15);
                                                    cvi.setLayoutParams(layoutparams);
                                                    cvi.setRadius(5);
                                                    cvi.setPadding(15, 5, 15, 5);
                                                    cvi.setCardBackgroundColor(0xFFF8DE7E);
                                                    cvi.setMaxCardElevation(10);

                                                    cvi.addView(tv);




                                                    //At this point add the image to the present chat window
                                                    ImageView imageView = new ImageView(context);
                                                    imageView.setImageDrawable(Drawable.createFromPath(mCurrentPhotoPath));
                                                    imageView.setLayoutParams(layoutParamsimg);


                                                    CardView cvt = new CardView(context);

                                                    layoutparams.setMargins(15,15,15,15);
                                                    cvt.setLayoutParams(layoutParamsimg);
                                                    cvt.setRadius(5);
                                                    cvt.setPadding(15, 5, 15, 5);
                                                    cvt.setCardBackgroundColor(0xFFF8DE7E);
                                                    cvt.setMaxCardElevation(10);



                                                    cvt.addView(imageView);


                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            LinearLayoutView.addView(cvi);
                                                            LinearLayoutView.addView(cvt);

                                                        }
                                                    });









                                                }//type 3 message from some other sender
                                                else{

                                                    IndividualChat2.MessageHandlerP mppp=new IndividualChat2.MessageHandlerP();

                                                    mppp.pinsertMessage(message.getBody(),"null",message.getFrom(),"null","yes");



                                                }


                                            }//end of type =3
                                              */


                                        }//end of valid xml

                                        else {

                                            /*
                                            IndividualChat2.MessageHandlerP mpp = new IndividualChat2.MessageHandlerP();
                                            String[] sendername = message.getFrom().split("@");
                                            if (sendername[0].equals(sender)) {//display it in the screen


                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        //check whether incoming message is an xml type format
                                                        //if it is an xml its roster request meant for the admin.
                                                        //otherwise  store it and display it
                                                        // tvv.append("\n\n"+sender+":"+message.getBody());

                                                        mpp.pinsertMessage(message.getBody(), "null", message.getFrom(), "null", "yes");


                                                        CardView crv = new CardView(context);

                                                        layoutparams.setMargins(15, 15, 15, 15);
                                                        crv.setLayoutParams(layoutparams);
                                                        crv.setRadius(15);
                                                        crv.setPadding(15, 5, 15, 5);
                                                        crv.setCardBackgroundColor(0xFDFDFDFF);
                                                        crv.setMaxCardElevation(10);


                                                        TextView tv = new TextView(context);
                                                        tv.setLayoutParams(layoutparams);
                                                        tv.setText(" " + sender + ":" + message.getBody() + "\n");

                                                        crv.addView(tv);
                                                        LinearLayoutView.addView(crv);


                                                    }

                                                });

                                            }
                                            else {//store in db

                                                mpp.pinsertMessage(message.getBody(), "null", message.getFrom(), "null", "yes");
                                            }


                                             */
                                        }//end of not a valid xml







                                    }//end of process messsage
                                });//end of chat listener

                                Log.w("app", chat.toString());
                            }
                        });


            }//end of received message

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


            @SuppressLint("ResourceType") EditText et=findViewById(303030);
            String Messagetosend=et.getText().toString();
            String Currenttime=(DateFormat.format("dd-MM-yyyy-hh-mm-ss", new java.util.Date()).toString());

            Log.d ("Currenttime", Currenttime);
            String xmlmessagetosend="<message><type>two</type><content>"+Messagetosend+"</content><time>"+Currenttime+"</time><sender>"+uname+"</sender><recepient>"+sender+"</recepient></message>";

            //Add this to the db;
            MessageHandlerP mp=new MessageHandlerP();
            mp.pinsertMessage(Messagetosend,Currenttime,uname,"null","yes",sender,"two");




            String toPerson=sender+"@pchat";







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
                    chat.sendMessage(xmlmessagetosend);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                //clear the chat window



                runOnUiThread(new Runnable(){
                    public void run() {

                        et.setText("");

                       // tvv.append("\n\n"+uname+":"+Messagetosend);


                        CardView cvr = new CardView(context);

                        layoutparams.setMargins(15,15,15,15);
                        cvr.setLayoutParams(layoutparams);
                        cvr.setRadius(15);
                        cvr.setPadding(15, 5, 15, 5);
                        cvr.setCardBackgroundColor(0xFFFFFFCC);
                        cvr.setMaxCardElevation(10);

                        TextView tv=new TextView(context);
                        tv.setLayoutParams(layoutparams);
                        tv.setText("me:"+Messagetosend+"\n");

                        cvr.addView(tv);
                        LinearLayoutView.addView(cvr);



                    }
                });

            }//end of received message


            return null;
        }//end of do in background


    }//end of inner class



    private class FileUpload  extends AsyncTask<Void, Void, Void> {


        String result;
        final ProgressDialog dialog = new ProgressDialog(context);



        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Uploading...");
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


            if(connection.isAuthenticated())
            {


                String toUser="mine@pchat";
                String description="file transfer check";

                uploadFile();




            }//end of received message


            return null;
        }//end of do in background




        private void uploadFile() {
            //progressDialog.show();

            // Map is used to multipart the file using okhttp3.RequestBody


            // mCurrentPhotoPath="/storage/emulated/0/Android/data/com.example.pchat/files/Pictures/test.jpg";

            File file = new File(mCurrentPhotoPath);

            String successcode="";

            try {

                final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

                RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file",file.getName(), RequestBody.create(MEDIA_TYPE_JPG, file)).build();

                Request request = new Request.Builder()
                        .url("https://www.flarespeech.com/pchat_upload.php")
                        .post(req)
                        .build();

                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();

                Log.d("response", "uploadImage:"+response.body().string());

            successcode=response.body().string();




            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e( "Error: " , e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("Other Error: ", e.getLocalizedMessage());
            }



                File file2 = new File(mCurrentPhotoPath);

                String fn=file2.getName();
                String uploadedPath="https://www.flarespeech.com/pchat_uploads/"+fn;
                String PicMessagetoSend="<message><type>three</type><sender>"+uname+"</sender><recepient>"+sender+"</recepient><path>"+uploadedPath+"</path></message>";


                String toPerson = sender+"@pchat";
                EntityBareJid jid = null;
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                try {
                    jid = JidCreate.entityBareFrom(toPerson);
                } catch (XmppStringprepException ex) {
                    ex.printStackTrace();
                }
                Chat chat = chatManager.createChat(String.valueOf(jid));
                try {
                    chat.sendMessage(PicMessagetoSend);
                } catch (SmackException.NotConnectedException es) {
                    es.printStackTrace();
                }





            TextView tv=new TextView(context);
            tv.setLayoutParams(layoutparams);
            tv.setText("me:\n");

            CardView cvi = new CardView(context);

            layoutparams.setMargins(15,15,15,15);
            cvi.setLayoutParams(layoutparams);
            cvi.setRadius(5);
            cvi.setPadding(15, 5, 15, 5);
            cvi.setCardBackgroundColor(0xFFF8DE7E);
            cvi.setMaxCardElevation(10);

            cvi.addView(tv);




            //At this point add the image to the present chat window
                ImageView imageView = new ImageView(context);
                imageView.setImageDrawable(Drawable.createFromPath(mCurrentPhotoPath));
                imageView.setLayoutParams(layoutParamsimg);


                CardView cvt = new CardView(context);

                layoutparams.setMargins(15,15,15,15);
                cvt.setLayoutParams(layoutParamsimg);
                cvt.setRadius(5);
                cvt.setPadding(15, 5, 15, 5);
                cvt.setCardBackgroundColor(0xFFF8DE7E);
                cvt.setMaxCardElevation(10);



                cvt.addView(imageView);


            runOnUiThread(new Runnable() {
                public void run() {
                LinearLayoutView.addView(cvi);
                    LinearLayoutView.addView(cvt);

                }
            });





        }//end of upload





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


        public void pinsertMessage(String msg, String time, String sender, String groupname, String individual_message, String rec, String type)
        {
            pdba1=new MessageStoreDatabaseAdapter(context);
            long a= pdba1.addMessage(msg,time, sender, groupname, individual_message,rec,type);
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


        public ArrayList<String> pgetAllRecepient()
        {
            pdba2=new MessageStoreDatabaseAdapter(context);
            ArrayList<String > RecepientArrayList=pdba2.getAllRecepients();
            return RecepientArrayList;
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


        public ArrayList<String> pgetAllType()
        {
            pdba2=new MessageStoreDatabaseAdapter(context);
            ArrayList<String > TypeArrayList=pdba2.getAllType();
            return TypeArrayList;
        }



    }//end of class






    int CheckforXML(String ms)
    {
        try {
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(ms)));
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }







    private void downloadImage(@NonNull String url) throws IOException {

        String imageFileName="sample";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,   ".jpg",     storageDir );


        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {@Override
        public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                if (image.exists()) {
                    boolean fileDeleted = image.delete();
                    Log.v("fileDeleted", fileDeleted + "");
                }
                boolean fileCreated = image.createNewFile();
                BufferedSink sink = Okio.buffer(Okio.sink(image));
                sink.writeAll(response.body().source());
                sink.close();
            }
        });

    }



    //represents a time with index
    private class timeIndex
    {

        String time;
        int index;



    }

    //retun the list of timeIndex
    public List<timeIndex> maketimeIndexList(ArrayList<String> timearray)
    {
        List<timeIndex> ti = new ArrayList<>();



        for(int i=0;i<timearray.size();i++)
        {
            timeIndex TI=new timeIndex();
            TI.time=timearray.get(i);
            TI.index=i;
            ti.add(TI);
        }
        
        return ti;
    }//end of function









}//end of outer class