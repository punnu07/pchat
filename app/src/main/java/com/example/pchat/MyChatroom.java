  package com.example.pchat;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;

    private String mCurrentVideoPath;
    private ImageView mImageView;


    private String ImageDownloadedPath="";


    private static final int VIDEO_CAPTURE = 101;
    Uri videoUri;


    LinearLayout.LayoutParams layoutparams,layoutparams2,layoutParamsimg,layoutParamsvideo;

    RelativeLayout.LayoutParams rlayoutparams,rlayoutparams2,rlayoutparams3, rlayoutparams4, rlayoutparams5,rlayoutparams6,rlayoutparams7;


    LinearLayout LinearLayoutView, LinearLayoutViewBig;

    ScrollView sv;



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chatroom);

        uname=getIntent().getStringExtra(MyChatroom.EXTRA_NAME);
        pword=getIntent().getStringExtra(MyChatroom.EXTRA_PWD);
        roomname=getIntent().getStringExtra(MyChatroom.EXTRA_ROOM);


        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();


        layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        layoutparams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        layoutParamsimg=new LinearLayout.LayoutParams(8*screenWidth/10, screenHeight );
        layoutParamsvideo=new LinearLayout.LayoutParams(8*screenWidth/10, screenHeight );




        String []str=roomname.split("@");
        String groupname=str[0];

        TextView tvv=findViewById(R.id.MyRoomMUCChatwindow);
        tvv.setMovementMethod(new ScrollingMovementMethod());




        sv=new ScrollView(context);

        LinearLayoutView = new LinearLayout(this);
        LinearLayoutView.setOrientation(LinearLayout.VERTICAL);


        RelativeLayout rlayoutView = new RelativeLayout(this);
        rlayoutparams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlayoutparams2=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlayoutparams3=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlayoutparams4=new RelativeLayout.LayoutParams(3*screenWidth/30,50);
        rlayoutparams5=new RelativeLayout.LayoutParams(3*screenWidth/30,50);
        rlayoutparams6=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        rlayoutparams7=new RelativeLayout.LayoutParams(3*screenWidth/30,50);



        LinearLayoutViewBig = new LinearLayout(this);
        LinearLayoutViewBig.setOrientation(LinearLayout.HORIZONTAL);

        sv.addView(LinearLayoutView);

        layoutparams2.height=8*screenHeight/10;
        sv.setLayoutParams(layoutparams2);
        sv.setId(787878);
        rlayoutView.addView(sv);




//cardview at the bottom for message
        CardView cvv = new CardView(context);
        cvv.setRadius(0);
        cvv.setPadding(25, 25, 25, 15);
        cvv.setCardBackgroundColor(0xFCFCFCFF);
        cvv.setMaxCardElevation(10);
        cvv.setId(898989);


        EditText ett=new EditText(context);
        rlayoutparams3.width=7*screenWidth/10;
        rlayoutparams3.height=RelativeLayout.LayoutParams.MATCH_PARENT;
        //rlayoutparams3.addRule(RelativeLayout.ALIGN_PARENT_START);
        rlayoutparams3.alignWithParent=true;


        rlayoutparams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ett.setLayoutParams(rlayoutparams3);
        ett.setGravity(Gravity.BOTTOM);
        ett.setTextSize(14);
        ett.setId(676767);


        rlayoutparams2.setMargins(5,5,5,15);
        cvv.addView(ett);
        cvv.setLayoutParams(rlayoutparams2);
        rlayoutView.addView(cvv);



        rlayoutparams2.height=2*screenHeight/10;
        rlayoutparams2.width=rlayoutparams3.width;
        rlayoutparams.setMargins(15,15,15,35);

        rlayoutparams2.addRule(RelativeLayout.BELOW,787878);
        rlayoutparams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);



        //cardview for buttons

        CardView cvvv = new CardView(context);
        cvvv.setRadius(0);
        cvvv.setPadding(5, 5, 5, 5);
        cvvv.setCardBackgroundColor(0xFCFCFCFF);
        cvvv.setMaxCardElevation(0);
        cvvv.setId(565656);


       // rlayoutparams6.setLayoutDirection(LinearLayout.HORIZONTAL);
        rlayoutparams6.addRule(RelativeLayout.RIGHT_OF,898989);
        rlayoutparams6.addRule(RelativeLayout.BELOW,787878);

        rlayoutparams6.height=2*screenHeight/10;
        rlayoutparams6.width=3*screenWidth/10;
        rlayoutparams6.setMargins(1,5,10,15);


        cvvv.setLayoutParams(rlayoutparams6);







        ImageButton  individualmessagesend_button= new ImageButton(this);
        individualmessagesend_button.setImageResource(R.drawable.messenger);
        individualmessagesend_button.setLayoutParams(rlayoutparams4);
        individualmessagesend_button.setScaleType(ImageView.ScaleType.CENTER);
        //individualmessagesend_button.setAdjustViewBounds(true);
        individualmessagesend_button.setBackgroundColor(Color.TRANSPARENT);
        individualmessagesend_button.setId(454545);
        LinearLayoutViewBig.addView(individualmessagesend_button);




        ImageButton  takepic_button= new ImageButton(this);
        takepic_button.setImageResource(R.drawable.camera);
        takepic_button.setLayoutParams(rlayoutparams5);
        takepic_button.setScaleType(ImageView.ScaleType.CENTER);
        takepic_button.setBackgroundColor(Color.TRANSPARENT);
        takepic_button.setId(343434);
        LinearLayoutViewBig.addView(takepic_button);


        ImageButton  takevideo_button= new ImageButton(this);
        takevideo_button.setImageResource(R.drawable.video);
        takevideo_button.setLayoutParams(rlayoutparams7);
        takevideo_button.setScaleType(ImageView.ScaleType.CENTER);
        takevideo_button.setBackgroundColor(Color.TRANSPARENT);
        takevideo_button.setId(545454);
        LinearLayoutViewBig.addView(takevideo_button);



        rlayoutparams4.addRule(RelativeLayout.LEFT_OF,343434);
        rlayoutparams7.addRule(RelativeLayout.RIGHT_OF,343434);

        cvvv.addView(LinearLayoutViewBig);


        rlayoutView.addView(cvvv);
        setContentView(rlayoutView);



        new joinChat().execute();


        //send message handler
        ImageButton sendChat_button= findViewById(454545);
        sendChat_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new MyChatroom.sendChat().execute();

            }
        });


        FrameLayout fl=new FrameLayout(context);

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CLIP_VERTICAL);
        flp.setMargins(0,0,100,100);
        flp.gravity = (Gravity.TOP |Gravity.RIGHT);

        //fl.setForegroundGravity(Gravity.CLIP_VERTICAL);
        //fl.setLayoutParams(flp);
        FloatingActionButton fb=new FloatingActionButton(context);


        fb.setImageBitmap(textAsBitmap("+", 44, Color.WHITE));
        //fb.setLayoutParams(flp);
        fb.setForegroundGravity(Gravity.RIGHT);


        fb.setBackgroundColor(0xFFE27070);
        fb.setBackgroundTintList(ColorStateList.valueOf(0xFFE27070));


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



        fl.addView(fb);
        fl.setForegroundGravity(5);

        fl.setPadding(8*screenWidth/10,0*screenHeight/10,0,0);

        rlayoutView.addView(fl);


        ImageButton picuploadbutton=findViewById(343434);
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







        //send message handler
        ImageButton sendvideo_button= findViewById(545454);
        sendvideo_button.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v){

                try {
                    startRecordingVideo();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });








    }//end of oncreate()





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
    public void startRecordingVideo() throws IOException {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String videoFileName = "MP4_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File videofile = File.createTempFile(videoFileName,   ".mp4",     storageDir );

            Uri videoUri = FileProvider.getUriForFile(context,"com.example.pchat.fileprovider" , videofile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            mCurrentVideoPath=videofile.getAbsolutePath();
            startActivityForResult(intent, VIDEO_CAPTURE);
        } else {
            Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
        }
    }








    /*
    public void playbackRecordedVideo() {
        VideoView mVideoView = (VideoView) findViewById(R.id.video_view);
        mVideoView.setVideoURI(videoUri);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
        mVideoView.start();
    }
*/


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

            try {
                mImageBitmap=  MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
            } catch (IOException e) {
                e.printStackTrace();

            }

            new FileUpload().execute();

        }


        //for video capture
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                //Toast.makeText(this, "\n" + data.getData(), Toast.LENGTH_LONG).show();
                // playbackRecordedVideo();

                new VideoFileUpload().execute();



            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",  Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",  Toast.LENGTH_LONG).show();
            }
        }



    }




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
            dialog.setMessage(" ");
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


                runOnUiThread(new Runnable() {
                    public void run() {
                        LinearLayoutView.removeAllViewsInLayout();
                        for(int i=0;i<LinearLayoutView.getChildCount();i++)
                        {
                            LinearLayoutView.removeViewAt(i);
                        }

                    }
                });



                muc.addMessageListener(new MessageListener() {
                    public void processMessage(Message message) {


                        Log.d("message",message.getBody());
                        String []address=message.getFrom().split("/");


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

                         String type=doc.getElementsByTagName("type").item(0).getTextContent();


                        //type two
                        if(type.equals("two")) {

                            String content = doc.getElementsByTagName("content").item(0).getTextContent();
                            String time = doc.getElementsByTagName("time").item(0).getTextContent();

                            String fromwhom = doc.getElementsByTagName("sender").item(0).getTextContent();


                            String[] nowtime = (DateFormat.format("dd-MM-yyyy-hh-mm-ss", new java.util.Date()).toString()).split("-");
                            int todaydate = Integer.valueOf(nowtime[0]);


                            MyChatroom.MessageHandler mh=new MyChatroom.MessageHandler();
                            mh.insertMessage(message.getBody(),time,message.getFrom().toString(),"null","no","","two");

                            //receiver is uname
                            //first inserrt the message into the db

                            //display the message
                            CardView cvj = new CardView(context);
                            layoutparams.setMargins(15, 15, 15, 15);
                            cvj.setLayoutParams(layoutparams);
                            cvj.setRadius(15);
                            cvj.setPadding(15, 5, 15, 5);
                            cvj.setCardBackgroundColor(0xFDFDFDFF);
                            cvj.setMaxCardElevation(10);

                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cvj.getLayoutParams();
                            lp.gravity = Gravity.LEFT;

                            TextView tv = new TextView(context);
                            tv.setLayoutParams(layoutparams);


                            cvj.setCardBackgroundColor(0xFFFFFFCC);

                            String senderdisplayname = "";
                            if (fromwhom.equals(uname)) {
                                senderdisplayname = "me";

                            } else {
                                senderdisplayname = fromwhom;
                            }

                            tv.setText(" " + senderdisplayname + ": " + content);
                            String[] messagetime = time.split("-");
                            int messagedate = Integer.valueOf(messagetime[0]);
                            String timeval = messagetime[3] + ":" + messagetime[4];
                            String dateval = messagetime[0] + ":" + messagetime[1] + ":" + messagetime[2];
                            if (todaydate == messagedate) {
                                tv.setTextSize(6);
                                tv.setTextColor(Color.GRAY);
                                int spaceslen = content.length();
                                tv.append("\n");
                                for (int r = 0; r < spaceslen; r++) {
                                    tv.append("\t");
                                }
                                tv.append(timeval);
                                tv.setTextSize(12);
                                tv.setTextColor(Color.BLACK);

                            } else {

                                tv.setTextSize(6);
                                tv.setTextColor(Color.GRAY);

                                int spaceslen = content.length();
                                tv.append("\n");
                                for (int r = 0; r < spaceslen; r++) {
                                    tv.append("\t");
                                }

                                tv.append(dateval + " " + timeval);
                                tv.setTextSize(12);
                                tv.setTextColor(Color.BLACK);
                            }
                            cvj.addView(tv);

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    LinearLayoutView.addView(cvj);
                                }

                            });


                        }//end of two
                        if(type.equals("three")) {


                            String content = doc.getElementsByTagName("content").item(0).getTextContent();
                            String time = doc.getElementsByTagName("time").item(0).getTextContent();

                            String fromwhom = doc.getElementsByTagName("sender").item(0).getTextContent();


                            String[] nowtime = (DateFormat.format("dd-MM-yyyy-hh-mm-ss", new java.util.Date()).toString()).split("-");
                            int todaydate = Integer.valueOf(nowtime[0]);






                            TextView tv=new TextView(context);
                            tv.setLayoutParams(layoutparams);


                            if(fromwhom.equals(uname)) {
                                tv.setText("me:\n");
                            }

                            else {
                                tv.setText(fromwhom+":\n");
                            }


                            CardView cvi = new CardView(context);
                            layoutparams.setMargins(15,15,15,15);
                            cvi.setLayoutParams(layoutparams);
                            cvi.setRadius(5);
                            cvi.setPadding(15, 5, 15, 5);
                            cvi.setCardBackgroundColor(0xFFFFFFCC);
                            cvi.setMaxCardElevation(10);

                            cvi.addView(tv);




                            //At this point add the image to the present chat window
                            ImageView imageView = new ImageView(context);
                            imageView.setLayoutParams(layoutParamsimg);

                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Picasso.with(context).load(content).into(imageView);
                                }
                            });


                            //imageView.setImageDrawable(Drawable.createFromPath(imgpath));


                            CardView cvt = new CardView(context);
                            layoutparams.setMargins(15,15,15,15);
                            cvt.setLayoutParams(layoutParamsimg);
                            cvt.setRadius(5);
                            cvt.setPadding(15, 5, 15, 5);
                            cvt.setCardBackgroundColor(0xFFFFFFCC);
                            cvt.setMaxCardElevation(10);
                            cvt.addView(imageView);

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    LinearLayoutView.addView(cvi);
                                    LinearLayoutView.addView(cvt);

                                }
                            });

                        }//end of type 3


                        if(type.equals("four")) {

                            String content = doc.getElementsByTagName("content").item(0).getTextContent();
                            String time = doc.getElementsByTagName("time").item(0).getTextContent();

                            String fromwhom = doc.getElementsByTagName("sender").item(0).getTextContent();
                            String[] nowtime = (DateFormat.format("dd-MM-yyyy-hh-mm-ss", new java.util.Date()).toString()).split("-");
                            int todaydate = Integer.valueOf(nowtime[0]);


                            TextView tv=new TextView(context);
                            tv.setLayoutParams(layoutparams);


                            if(fromwhom.equals(uname)) {
                                tv.setText("me:\n");
                            }

                            else {
                                tv.setText(fromwhom+":\n");
                            }


                            CardView cvi = new CardView(context);
                            layoutparams.setMargins(15,15,15,15);
                            cvi.setLayoutParams(layoutparams);
                            cvi.setRadius(5);
                            cvi.setPadding(15, 5, 15, 5);
                            cvi.setCardBackgroundColor(0xFFFFFFCC);
                            cvi.setMaxCardElevation(10);

                            cvi.addView(tv);




                            //At this point add the video to the present chat window
                            //firs check wheether the video is donwloaded or not

                            //get the file name exact from the url

                            String []fn=content.split("/");
                            String filename=fn[fn.length-1];
                            Log.d("fn", filename);

                            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                            boolean filepresent=false;

                            File []files= storageDir.listFiles();
                            for(int i=0;i<files.length;i++)
                            {
                                if(filename.equals(files[i].getName()))
                                {
                                    filepresent=true;break;
                                }
                            }


                            Log.d("file present", String.valueOf(filepresent));

                            String destinationfilename = storageDir.getAbsolutePath() + "/" + filename;

                             if(filepresent==false) {


                                    URL u = null;
                                    try {
                                        u = new URL(content);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    URLConnection conn = null;
                                    try {
                                        conn = u.openConnection();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    int contentLength = conn.getContentLength();

                                    DataInputStream stream = null;
                                    try {
                                        stream = new DataInputStream(u.openStream());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    byte[] buffer = new byte[contentLength];
                                    try {
                                        stream.readFully(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        stream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    DataOutputStream fos = null;
                                    try {
                                        fos = new DataOutputStream(new FileOutputStream(destinationfilename));
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        fos.write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        fos.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }



                                 VideoView videoview=new VideoView(context);
                                 videoview.setLayoutParams(layoutParamsvideo);
                                 videoview.setVideoURI(Uri.parse(content)); //the string of the URL mentioned above

                                 MediaController mediaController = new MediaController(context);
                                 //mediaController.setAnchorView(videoview);
                                 videoview.setMediaController(mediaController);


                                 videoview.requestFocus();

                                 runOnUiThread(new Runnable() {
                                     public void run() {
                                         videoview.start();
                                     }
                                 });

                                 CardView cvt = new CardView(context);
                                 layoutparams.setMargins(15,15,15,15);
                                 cvt.setLayoutParams(layoutparams);
                                 cvt.setRadius(5);
                                 cvt.setPadding(15, 5, 15, 5);
                                 cvt.setCardBackgroundColor(0xFFFFFFCC);
                                 cvt.setMaxCardElevation(10);
                                 cvt.addView(videoview);

                                 runOnUiThread(new Runnable() {
                                     public void run() {
                                         LinearLayoutView.addView(cvi);
                                         LinearLayoutView.addView(cvt);

                                     }
                                 });

                             }
                             else
                             {

                                 VideoView videoview=new VideoView(context);
                                 videoview.setLayoutParams(layoutParamsvideo);
                                 videoview.setVideoURI(Uri.parse(destinationfilename)); //the string of the URL mentioned above

                                 MediaController mediaController = new MediaController(context);
                                 //mediaController.setAnchorView(videoview);
                                 videoview.setMediaController(mediaController);

                                 videoview.requestFocus();

                                 runOnUiThread(new Runnable() {
                                     public void run() {
                                         videoview.start();
                                     }
                                 });
                                 CardView cvt = new CardView(context);
                                 layoutparams.setMargins(15,15,15,15);
                                 cvt.setLayoutParams(layoutparams);
                                 cvt.setRadius(5);
                                 cvt.setPadding(15, 5, 15, 5);
                                 cvt.setCardBackgroundColor(0xFFFFFFCC);
                                 cvt.setMaxCardElevation(10);
                                 cvt.addView(videoview);

                                 runOnUiThread(new Runnable() {
                                     public void run() {
                                         LinearLayoutView.addView(cvi);
                                         LinearLayoutView.addView(cvt);

                                     }
                                 });

                             }

                        }//end of type 4


                    }//end of message
                });//end of listener


            }//end of connection


            return null;
        }//end of do in background


    }//end of inner class








    //this will just send the messages.
    private class  sendChat extends AsyncTask<Void, Void, Void> {


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



        protected Void doInBackground(Void... voids) {


            if(roomname.equals("unassignedandinvalidroom"))
            {
                return null;
            }
            //join as the second person
            String userName=uname;
            String password=pword;

            EditText et;

            final TextView[] tv = new TextView[1];
            //get the message to send

            String messagetosend="";

            et=(EditText)findViewById(676767);

            messagetosend=et.getText().toString();

            String Currenttime=(DateFormat.format("dd-MM-yyyy-hh-mm-ss", new java.util.Date()).toString());

            Log.d ("Currenttime", Currenttime);
            String xmlmessagetosend="<message><type>two</type><content>"+messagetosend+"</content><time>"+Currenttime+"</time><sender>"+uname+"</sender><recepient>"+roomname+"</recepient></message>";


            MyChatroom.MessageHandler mh=new MyChatroom.MessageHandler();

            //already connected
            if(connection.isConnected())
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
                    muc.sendMessage(xmlmessagetosend);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable(){
                    public void run()
                    {
                        LinearLayoutView.removeAllViewsInLayout();
                      et.setText("");
                    }

                });


            }//end of connection
            else
            {

                runOnUiThread(new Runnable(){
                    public void run()
                    {
                        Toast.makeText(context, "Not Connected.",  Toast.LENGTH_LONG).show();
                    }

                });

                  Intent intent = getIntent();
                  intent.putExtra(EXTRA_PWD, pword);
                  intent.putExtra(EXTRA_NAME, uname);
                  intent.putExtra(EXTRA_ROOM,roomname);

                  intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                  finish();
                  overridePendingTransition(0, 0);
                  startActivity(intent);
                  overridePendingTransition(0, 0);

            }

            return null;
        }//end of do in background

    }//end of inner class





    private class FileUpload  extends AsyncTask<Void, Void, Void> {


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


        protected Void doInBackground(Void... voids) {

            String userName=uname;
            String password=pword;

            if(connection.isConnected())
            {

                uploadFile();

            }//end of received message

            else
            {
                runOnUiThread(new Runnable(){
                    public void run()
                    {
                        Toast.makeText(context, "Not Connected.",  Toast.LENGTH_LONG).show();
                    }

                });

                Intent intent = getIntent();
                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);
                intent.putExtra(EXTRA_ROOM,roomname);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);

                startActivity(intent);
                overridePendingTransition(0, 0);


            }

            return null;
        }//end of do in background



        private void uploadFile() {

            File file = new File(mCurrentPhotoPath);

              EditText  et=(EditText)findViewById(676767);

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
            String Currenttime=(DateFormat.format("dd-MM-yyyy-hh-mm-ss", new java.util.Date()).toString());
            Log.d ("Currenttime", Currenttime);
            String fn=file2.getName();

            String uploadedPath="https://www.flarespeech.com/pchat_uploads/"+fn;
            String PicMessagetoSend="<message><type>three</type><sender>"+uname+"</sender><time>"+Currenttime+"</time><recepient>"+roomname+"</recepient><content>"+uploadedPath+"</content></message>";


            if(connection.isConnected()) {
                EntityBareJid mucJid = null;
                MultiUserChat muc = null;
                MultiUserChatManager manager = null;

                manager = MultiUserChatManager.getInstanceFor(connection);
                muc = manager.getMultiUserChat(roomname);

                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(MAX_STANZAS);

                try {
                    muc.join(uname, "", history, SmackConfiguration.getDefaultPacketReplyTimeout());
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                Log.d("Sending fn", "Joined");
                Log.d("history", String.valueOf(history));

                try {
                    muc.sendMessage(PicMessagetoSend);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                //clear the whole chat window as the message once received will send all the messages back again
                runOnUiThread(new Runnable(){
                    public void run()
                    {
                        LinearLayoutView.removeAllViewsInLayout();
                        et.setText("");
                    }

                });


            }
            else
            {
                runOnUiThread(new Runnable(){
                    public void run()
                    {
                        Toast.makeText(context, "Not Connected.",  Toast.LENGTH_LONG).show();
                    }

                });

                Intent intent = getIntent();
                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);
                intent.putExtra(EXTRA_ROOM,roomname);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);

                startActivity(intent);
                overridePendingTransition(0, 0);


            }


            //add the message to db
            MessageHandler mppp=new MessageHandler();

            String content=uploadedPath;
            String time=Currenttime;
            String fromwhom=uname;
            String towhom=roomname;

            mppp.insertMessage(content,time, fromwhom,roomname,"no","null","three");

        }//end of upload

    }//end of inner class




    //inner class for video upload
    private class VideoFileUpload  extends AsyncTask<Void, Void, Void> {


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


        protected Void doInBackground(Void... voids) {

            String userName=uname;
            String password=pword;

            if(connection.isConnected())
            {

                uploadFile();

            }
            else
            {
                runOnUiThread(new Runnable(){
                    public void run()
                    {
                        Toast.makeText(context, "Not Connected.",  Toast.LENGTH_LONG).show();
                    }

                });

                Intent intent = getIntent();
                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);
                intent.putExtra(EXTRA_ROOM,roomname);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);

                startActivity(intent);
                overridePendingTransition(0, 0);

            }

            return null;
        }//end of do in background



        private void uploadFile() {

            File file = new File(mCurrentVideoPath);


            String successcode="";

            try {

                final MediaType MEDIA_TYPE_MP4 = MediaType.parse("video/mp4");

                RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file",file.getName(), RequestBody.create(MEDIA_TYPE_MP4, file)).build();

                Request request = new Request.Builder()
                        .url("https://www.flarespeech.com/pchat_upload.php")
                        .post(req)
                        .build();

                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();

                Log.d("response", "uploadVideo:"+response.body().string());

                successcode=response.body().string();

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e( "Error: " , e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("Other Error: ", e.getLocalizedMessage());
            }


            File file2 = new File(mCurrentVideoPath);
            String Currenttime=(DateFormat.format("dd-MM-yyyy-hh-mm-ss", new java.util.Date()).toString());
            Log.d ("Currenttime", Currenttime);
            String fn=file2.getName();

            String uploadedPath="https://www.flarespeech.com/pchat_uploads/"+fn;
            String PicMessagetoSend="<message><type>four</type><sender>"+uname+"</sender><time>"+Currenttime+"</time><recepient>"+roomname+"</recepient><content>"+uploadedPath+"</content></message>";


            if(connection.isConnected()) {
                EntityBareJid mucJid = null;
                MultiUserChat muc = null;
                MultiUserChatManager manager = null;

                manager = MultiUserChatManager.getInstanceFor(connection);
                muc = manager.getMultiUserChat(roomname);

                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(MAX_STANZAS);

                try {
                    muc.join(uname, "", history, SmackConfiguration.getDefaultPacketReplyTimeout());
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


                Log.d("Sending fn", "Joined");
                Log.d("history", String.valueOf(history));

                try {
                    muc.sendMessage(PicMessagetoSend);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                //clear the whole chat window as the message once received will send all the messages back again
                runOnUiThread(new Runnable(){
                    public void run()
                    {
                        LinearLayoutView.removeAllViewsInLayout();

                    }

                });


            }
            else
            {

                runOnUiThread(new Runnable(){
                    public void run()
                    {
                        Toast.makeText(context, "Not Connected.",  Toast.LENGTH_LONG).show();
                    }

                });

                Intent intent = getIntent();
                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);
                intent.putExtra(EXTRA_ROOM,roomname);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);

                startActivity(intent);
                overridePendingTransition(0, 0);

            }

            //add the message to db
            MessageHandler mppp=new MessageHandler();

            String content=uploadedPath;
            String time=Currenttime;
            String fromwhom=uname;
            String towhom=roomname;

            mppp.insertMessage(content,time, fromwhom,roomname,"no","null","three");

        }//end of upload

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


        public void insertMessage(String msg, String time, String fromwhom, String groupname, String individual_message, String rec, String type)
        {
            dba1=new MessageStoreDatabaseAdapter(context);
            long a= dba1.addMessage(msg,time,fromwhom, groupname, individual_message, rec,type);
        }


        public ArrayList<String> getAllMessage()
        {
            dba2=new MessageStoreDatabaseAdapter(context);
            ArrayList<String > MessageArrayList=dba2.getAllMessage();
            return MessageArrayList;
        }
    }//end of class






}//end of outer class