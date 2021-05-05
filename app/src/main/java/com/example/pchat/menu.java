package com.example.pchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.Collection;


public class menu extends AppCompatActivity {


    final Context context = this;

    public static final String EXTRA_NAME = "a";
    public static final String EXTRA_PWD = "b";
    public static final String EXTRA_SENDER = "c";


    public String uname = "";
    public String pword = "";
    public String  roomname="unassignedandinvalidroom";

    AbstractXMPPConnection connection;

    ScrollView sv;
    CardView[]cv;

    LinearLayout LinearLayoutView, LinearLayoutViewHor,LinearLayoutViewHor2;
    LinearLayout.LayoutParams  layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );

    LinearLayout.LayoutParams  layoutparamsicon = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );


    LinearLayout.LayoutParams  layoutparamscard = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );


    CardView cvv;

    ImageView iv;

    TextView tv;










    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        uname=getIntent().getStringExtra(menu.EXTRA_NAME);
        pword=getIntent().getStringExtra(menu.EXTRA_PWD);





        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();


        layoutparamsicon.setMargins(50,10,50,10);
        layoutparams.setMargins(0,10,50,10);
        layoutparamscard.setMargins(0,0,0,0);



        sv=new ScrollView(context);

        LinearLayoutView = new LinearLayout(this);
        LinearLayoutView.setOrientation(LinearLayout.VERTICAL);






        //for a horizontal row
        LinearLayoutViewHor = new LinearLayout(this);
        LinearLayoutViewHor.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutViewHor.setLayoutParams(layoutparamscard);


        //add contacts
        cvv = new CardView(context);
        cvv.setRadius(0);
        cvv.setLayoutParams(layoutparamscard);
        cvv.setPadding(25, 25, 25, 15);
        cvv.setCardBackgroundColor(0xFCFCFCFF);
        cvv.setMaxCardElevation(10);
        cvv.setId(99999901);


        ImageView iv=new ImageView(context);
        iv.setImageResource(R.drawable.contact);
        iv.setLayoutParams(layoutparamsicon);
        LinearLayoutViewHor.addView(iv);


        TextView tv = new TextView(context);
        tv.setLayoutParams(layoutparams);
        tv.setText("Add Contact");
        LinearLayoutViewHor.addView(tv);
        cvv.addView(LinearLayoutViewHor);
        LinearLayoutView.addView(cvv);

        //add group
        LinearLayoutViewHor = new LinearLayout(this);
        LinearLayoutViewHor.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutViewHor.setLayoutParams(layoutparamscard);


        cvv = new CardView(context);
        cvv.setLayoutParams(layoutparamscard);
        cvv.setRadius(0);
        cvv.setPadding(25, 25, 25, 15);
        cvv.setCardBackgroundColor(0xFCFCFCFF);
        cvv.setMaxCardElevation(10);
        cvv.setId(99999902);

        iv = new ImageView(context);
        iv.setImageResource(R.drawable.group);
        iv.setLayoutParams(layoutparamsicon);
        LinearLayoutViewHor.addView(iv);

        tv = new TextView(context);
        tv.setLayoutParams(layoutparams);
        tv.setText("Add Group");

        LinearLayoutViewHor.addView(tv);
        cvv.addView(LinearLayoutViewHor);
        LinearLayoutView.addView(cvv);

        cvv.setClickable(true);



        //add listener
        cvv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(context, CreateGroup.class);
                intent.putExtra(EXTRA_PWD, pword);
                intent.putExtra(EXTRA_NAME, uname);
                startActivity(intent);

            }
        });



        new fetchcustomer().execute();

        //add the linearlayoutview to the scroll view
        sv.addView(LinearLayoutView);
        //set the contentvie
        setContentView(sv);



    }//end of oncreate


    //inner class for conneting
    //starting of inner class
    private class fetchcustomer extends AsyncTask<Void, Void, Void> {
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



        @Override
        protected Void doInBackground(Void... voids) {



            String Username = "admin";
            String Password = "punnooseak";


//login as admin
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(Username, Password)
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




            if (connection.isAuthenticated()) {

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

                for (RosterEntry entry : entries) {


                    LinearLayout LinearLayoutViewHor2;


                    LinearLayoutViewHor2 = new LinearLayout(context);
                    LinearLayoutViewHor2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayoutViewHor2.setLayoutParams(layoutparamscard);


                    int id=99999903;
                    //String toPerson = entry.getName();

                    String []customername= entry.getName().split("@");
                    Log.d("customer name", customername[0]);



                    //add contacts
                    CardView cvv = new CardView(context);
                    cvv.setLayoutParams(layoutparamscard);
                    cvv.setRadius(0);
                    cvv.setPadding(25, 25, 25, 15);
                    cvv.setCardBackgroundColor(0xFCFCFCFF);
                    cvv.setMaxCardElevation(10);
                    cvv.setId(id++);


                    ImageView iv = new ImageView(context);
                    iv.setImageResource(R.drawable.person);
                    iv.setLayoutParams(layoutparamsicon);


                    runOnUiThread(new Runnable(){
                        public void run()
                        {
                            if(iv.getParent() != null) {
                                ((ViewGroup)iv.getParent()).removeView(iv); // <- fix
                            }
                            LinearLayoutViewHor2.addView(iv);
                        }
                    });

                    TextView tv = new TextView(context);
                    tv.setLayoutParams(layoutparams);
                    tv.setText(customername[0]);



                    runOnUiThread(new Runnable(){
                        public void run()
                        {
                            LinearLayoutViewHor2.addView(tv);

                            if(LinearLayoutViewHor2.getParent() != null) {
                                ((ViewGroup)LinearLayoutViewHor2.getParent()).removeView(LinearLayoutViewHor2); // <- fix
                            }
                            cvv.addView(LinearLayoutViewHor2);
                            LinearLayoutView.addView(cvv);
                        }
                    });



                    cvv.setClickable(true);


                    cvv.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){

                            Intent intent = new Intent(context, IndividualChat2.class);
                            intent.putExtra(EXTRA_PWD, pword);
                            intent.putExtra(EXTRA_NAME, uname);
                            intent.putExtra(EXTRA_SENDER, customername[0]);
                            startActivity(intent);

                        }
                    });




                }

            }//end of connection as localhost


            return null;
        }// end of doinbackground function


    }//end of inner class




}//end of outer class