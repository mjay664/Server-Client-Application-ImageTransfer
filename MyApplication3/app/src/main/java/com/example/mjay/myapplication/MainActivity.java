package com.example.mjay.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public Button connect;
    public EditText host;
    public EditText port;
    public String host_c;
    public String port_c;
    public Socket s;
    MediaPlayer mp;
    public Intent i;
    public static int ssd=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        String path = Environment.getExternalStorageDirectory().toString();
        DateFormat df2 = new SimpleDateFormat("EEE dd mm yy, hh mm");
        final String date2 = df2.format(Calendar.getInstance().getTime());
        File f = new File(path, "test_project_data_file.dbi");
        if (f.exists())
        {
            try {
                FileWriter fout = new FileWriter(f, true);
                fout.append("\n"+date2);
                fout.close();
            }
            catch (Exception e)
            {
                Toast.makeText(MainActivity.this,"File Error!!!",Toast.LENGTH_SHORT).show();
            }
            File dir = new File(path+File.separator+"Test Project");
            File dir2 = new File(dir.toString()+File.separator+"rc_app_ch");
            RecordCreator.setDirectory(dir2.toString());
            RecordReader.setDirectory(dir2.toString());
            StringsContainer.setDirectory_path(dir.toString());
        }
        else
        {
            try
            {
                f.createNewFile();
                FileWriter fout = new FileWriter(f);
                int check_point = 1;
                fout.append(""+check_point+" ::"+date2);
                fout.close();
            }
            catch (Exception e)
            {
                Toast.makeText(MainActivity.this,"Unable to create new file!!!",Toast.LENGTH_SHORT).show();
            }
            File dir = new File(path+File.separator+"Test Project");
            dir.mkdir();
            File dir2 = new File(dir.toString()+File.separator+"rc_app_ch");
            dir2.mkdir();
            File sender = new File(dir2.toString()+File.separator+"#~sender.record");
            File recievver = new File(dir2.toString()+File.separator+"#~reciever.record");
            try{ sender.createNewFile(); recievver.createNewFile(); }catch (IOException e){}
            StringsContainer.setDirectory_path(dir.toString());
            RecordCreator.setDirectory(dir2.toString());
            RecordReader.setDirectory(dir2.toString());
        }
        getConnected();
    }
    void getConnected()
    {
        connect=(Button)findViewById(R.id.connect_id);
        host=(EditText)findViewById(R.id.host_id);
        port=(EditText)findViewById(R.id.port_id);
        mp=MediaPlayer.create(MainActivity.this,R.raw.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check;
                boolean check2;
                if(host.getText().toString().length()==0) {
                    host.setError("Enter Host");
                    check=false;
                }
               else{
                    host_c=host.getText().toString();
                    check=true;
                }

                if(port.getText().toString().length()==0) {
                    port.setError("Enter Port");
                    check2=false;
                }
                else{
                    port_c=port.getText().toString();
                    check2=true;
                }

                if(check&&check2)
                {
                    new MyTask().execute(host_c,port_c);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if(ssd==1){
                                Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_LONG).show();
                                mp.start();
                                SocketSer.setSocket(s);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mp.stop();
                                    }
                                },800);
                                i=new Intent(MainActivity.this,Main22Activity.class);
                                finish();
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Couldn't connect to Host!!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1000);

                }


            }
        });
    }

    class MyTask extends AsyncTask<String, Integer, Socket> {
        @Override
        protected Socket doInBackground(String... params) {
            try{
                s=new Socket(params[0],Integer.parseInt(params[1]));
                if(s.isConnected())
                {
                    ssd=1;
                }
            }
            catch(Exception e)
            {
                e.getMessage();
            }
            return s;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
