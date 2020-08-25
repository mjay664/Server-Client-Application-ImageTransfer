package com.example.mjay.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {
    ImageButton img_b;
    public MediaPlayer mp;
    public TextView tv;
    public String rc_flag;
    EditText mess;
    Button send;
    public Socket socket;
    Intent intent;
    public Bitmap bmp;
    ProgressDialog progressDialog;
    public static boolean brr = true;
    public static int size_max;
    public Bitmap ready_to_send;
    MySendThread t1;
    MyRecieveThread t2;
    public static byte[] byteArray;
    public static String message_in_it;
    public static String flag;
    public LinearLayout my_layout;
    static int count = 1;
    static String recieve;
    public ProgressDialog recieve_image_dialog;
    public Drawable d_sender;
    public Drawable d_reciever;
    public static ScrollView sv;
    public Drawable d_divider;
    public Drawable d_sender_img;
    public Drawable d_reciever_img;
    public Main2Activity my_ins;
    DateFormat df2 = new SimpleDateFormat("EEE dd mm yy, hh mm");
    public final String date2 = df2.format(Calendar.getInstance().getTime());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        my_ins = Main2Activity.this;

        setContentView(R.layout.activity_main2);

        getSupportActionBar().setTitle("Message");
        socket = SocketSer.getSocket();
        getConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                File f = new File(Environment.getExternalStorageDirectory().toString());

                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.withAppendedPath(Uri.fromFile(f), "/Test Project/"), "image/*");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void getConnected() {
        sv = (ScrollView) findViewById(R.id.scrollView2);

        d_divider = getResources().getDrawable(R.drawable.new_divider);

        my_layout = (LinearLayout) findViewById(R.id.layout1);
        my_layout.setOrientation(LinearLayout.VERTICAL);

        mp = MediaPlayer.create(Main2Activity.this, R.raw.recieve_message);

        img_b = (ImageButton) findViewById(R.id.ib_id);
        mess = (EditText) findViewById(R.id.message_id);
        send = (Button) findViewById(R.id.sb_id);
        progressDialog = new ProgressDialog(my_ins);

        progressDialog.setTitle("Sending Image");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setButton(ProgressDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });
        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                flag = "Interrupt";
                progressDialog.dismiss();
            }
        });
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);

        recieve_image_dialog = new ProgressDialog(my_ins);
        recieve_image_dialog.setTitle("Recieving Image");
        recieve_image_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        recieve_image_dialog.setButton(ProgressDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recieve_image_dialog.dismiss();
            }
        });
        recieve_image_dialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                flag = "!@#$Stop It!@#$";
                recieve_image_dialog.dismiss();
            }
        });
        recieve_image_dialog.setCancelable(false);
        recieve_image_dialog.setProgress(0);

        new LetMeDo().execute();

        img_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if (ImageContainer.isImageSelected()) {
                    ready_to_send = ImageContainer.getBitmapImage();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    ready_to_send.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    size_max = byteArray.length - 1;

                    progressDialog.setMax(size_max);
                    progressDialog.show();
                    progressDialog.getButton(ProgressDialog.BUTTON_POSITIVE).setVisibility(View.INVISIBLE);
                    progressDialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);

                    flag = "!@#$image!@#$";

                    Toast.makeText(Main2Activity.this, "Image Selected", Toast.LENGTH_SHORT).show();
                    ImageContainer.setBoolValue(false);
                } else if (mess.getText().toString().length() == 0) {
                    mess.setError("Nothing to Send");
                    flag = "sent";
                } else {
                    message_in_it = mess.getText().toString();

                    mess.setText("");

                    tv = new TextView(my_ins);
                    tv.setText(message_in_it);
                    d_sender = getResources().getDrawable(R.drawable.new_border);
                    RecordCreator.setSenderRecord(message_in_it + " ::" + date2);
                    tv.setTextSize(18);
                    tv.setTextColor(Color.BLACK);
                    tv.setBackgroundColor(Color.WHITE);
                    tv.setBackground(d_sender);
                    my_layout.addView(tv);
                    sv.post(new Runnable() {
                        @Override
                        public void run() {
                            sv.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    flag = "!@#$message!@#$";
                }
            }
        });
    }

    @Override
    public void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (data == null) {
            //Nothing selected
            return;
        }

        try {
            ImageContainer.setFile(new File(data.getData().getPath()));
            InputStream is = getBaseContext().getContentResolver().openInputStream(data.getData());
            bmp = BitmapFactory.decodeStream(is);
            ImageContainer.setBitmapImage(bmp);

            Intent intent1 = new Intent(Main2Activity.this, Main3Activity.class);
            startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MySendThread extends Thread implements Runnable {
        String str;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {

            DataOutputStream dout;

            while (true) {
                try {
                    dout = new DataOutputStream(socket.getOutputStream());

                    if (!flag.equals("sent"))
                        dout.writeUTF(flag);
                    if (flag.equals("!@#$image!@#$")) {
                        dout.writeUTF("" + byteArray.length);
                        progressDialog.setProgress(0);
                        for (int i = 0; i < byteArray.length; i++) {
                            if (flag.equals("Interrupt")) {
                                dout.writeUTF("!@#$Stop It!@#$");
                                break;
                            } else {

                                dout.writeUTF("" + intToStr((byteArray[i])));
                                progressDialog.incrementProgressBy(1);
                            }
                        }
                        dout.writeUTF("!@#$EOF!@#$");

                        flag = "sent";

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RecordCreator.setSenderRecord("Image " + ImageContainer.getFile().toString() + " ::" + date2);
                                ImageButton be = new ImageButton(my_ins);
                                be.setImageBitmap(Bitmap.createScaledBitmap(ImageContainer.getBitmapImage(), 120, 120, false));
                                d_reciever_img = getResources().getDrawable(R.drawable.send_img_border);
                                be.setBackground(d_reciever_img);
                                be.setClickable(true);

                                my_layout.addView(be);
                                progressDialog.getButton(ProgressDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                                progressDialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setVisibility(View.INVISIBLE);
                            }
                        });

                    } else if (flag.equals("!@#$message!@#$")) {
                        flag = "sent";
                        dout.writeUTF(message_in_it);
                        message_in_it = "";
                    }
                    //else
                    //Toast.makeText(Main2Activity.this, flag, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyRecieveThread extends Thread implements Runnable {


        @Override
        public void run() {

            DateFormat df = new SimpleDateFormat("dD MM yy, HH mm");
            DateFormat df2 = new SimpleDateFormat("EEE dd mm yy, hh mm");
            final String date2 = df2.format(Calendar.getInstance().getTime());
            final String date = df.format(Calendar.getInstance().getTime());
            DataInputStream inputStream;

            try {
                inputStream = new DataInputStream(socket.getInputStream());

                while (true) {
                    rc_flag = inputStream.readUTF();
                    tv = new TextView(Main2Activity.this);

                    if (rc_flag.equals("!@#$Stop It!@#$") && flag.equals("!@#$image!@#$")) {
                        flag = "Interrupt";
                    }

                    if (rc_flag.equals("~!@#$%i@#am@#closing~!@#$%")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Main2Activity.this, "Other side closed.\nExiting...", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }

                    if (rc_flag.equals("!@#$message!@#$")) {
                        recieve = inputStream.readUTF();


                        mp.start();

                        runOnUiThread(new Runnable() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void run() {
                                tv.setText(recieve);
                                d_reciever = getResources().getDrawable(R.drawable.border_sender);
                                RecordCreator.setRecieverRecord(recieve + " ::" + date2);
                                recieve = "";
                                tv.setTextSize(18);
                                tv.setTextColor(Color.BLACK);
                                tv.setBackgroundColor(Color.WHITE);
                                tv.setBackground(d_reciever);
                                my_layout.addView(tv);

                                sv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });
                            }
                        });


                        count++;
                    }
                    else if (rc_flag.equals("!@#$image!@#$"))
                    {

                        int length = Integer.parseInt(inputStream.readUTF());

                        byte[] b1 = new byte[length];

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recieve_image_dialog.show();
                                recieve_image_dialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
                                recieve_image_dialog.getButton(ProgressDialog.BUTTON_POSITIVE).setVisibility(View.INVISIBLE);
                            }
                        });

                        recieve_image_dialog.setMax(length - 1);
                        recieve_image_dialog.setProgress(0);
                        int i = 0;
                        for (i = 0; i < length; i++) {
                            recieve = inputStream.readUTF();

                            if (recieve.equals("!@#$Stop It!@#$")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(my_ins, "Image Recieve Canceled", Toast.LENGTH_SHORT).show();
                                        recieve_image_dialog.setProgress(0);
                                        recieve_image_dialog.dismiss();
                                    }
                                });
                                break;
                            }

                            b1[i] = (byte) Integer.parseInt("" + strToInt(recieve));

                            recieve_image_dialog.incrementProgressBy(1);
                        }

                        mp.start();

                        if (i == length) {
                            final byte[] imageAsBytes = b1;
                            runOnUiThread(new Runnable() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void run() {
                                    Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                                    ImageButton be = new ImageButton(my_ins);
                                     be.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
                                     d_sender_img = getResources().getDrawable(R.drawable.image_border);
                                     be.setBackground(d_sender_img);
                                     my_layout.addView(be);

                                    recieve_image_dialog.getButton(ProgressDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                                    recieve_image_dialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setVisibility(View.INVISIBLE);
                                }
                            });

                            String path = StringsContainer.getDirectoryPath();
                            File f = new File(path, "rec_img"+date+".bmp");
                            RecordCreator.setRecieverRecord("Image " + f.toString() + " ::" + date2);
                            FileOutputStream fs = new FileOutputStream(f);
                            fs.write(b1);
                            fs.close();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static String intToStr(int j) {
        String str = "";
        if (j < 0) {
            j = (j * (-1));
            str = "-";
        }
        while (j > 0) {
            str = str + "" + getChar((j % 26));

            j =  (j / 26);
        }

        return str;
    }

    private static int strToInt(String str) {
        int  h = 0;
        int r = 1;
        if (str.contains("-")) {
            str = str.substring(1, str.length());
            r = -1;
        }
        char[] c = str.toCharArray();
        for (int i = c.length - 1; i >= 0; i--) {
            h =  (h * 26 + getInt(c[i]));
        }
        return  (h * r);
    }

    private static int getInt(char c) {
        switch(c)
        {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            case 'k':
                return 10;
            case 'l':
                return 11;
            case 'm':
                return 12;
            case 'n':
                return 13;
            case 'o':
                return 14;
            case 'p':
                return 15;


            case 'q':
                return 16;
            case 'r':
                return 17;
            case 's':
                return 18;
            case 't':
                return 19;
            case 'u':
                return 20;

            case 'v':
                return 21;
            case 'w':
                return 22;

            case 'x':
                return 23;
            case 'y':
                return 24;
            case 'z':
                return 25;

        }
        return 0;
    }

    private static char getChar(int i) {
        switch (i)
        {
            case 0:
                return 'a';
            case 1:
                return 'b';
            case 2:
                return 'c';
            case 3:
                return 'd';
            case 4:
                return 'e';
            case 5:
                return 'f';
            case 6:
                return 'g';
            case 7:
                return 'h';
            case 8:
                return 'i';
            case 9:
                return 'j';
            case 10:
                return 'k';
            case 11:
                return 'l';
            case 12:
                return 'm';
            case 13:
                return 'n';
            case 14:
                return 'o';
            case 15:
                return 'p';
            case 16:
                return 'q';
            case 17:
                return 'r';
            case 18:
                return 's';
            case 19:
                return 't';
            case 20:
                return 'u';
            case 21:
                return 'v';
            case 22:
                return 'w';
            case 23:
                return 'x';
            case 24:
                return 'y';
            case 25:
                return 'z';

        }
        return '1';
    }


    class LetMeDo extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] params) {
            t1 = new MySendThread();
            t2 = new MyRecieveThread();
            t1.start();
            t2.start();
            return null;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            try
            {
                DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                dout.writeUTF("~!@#$%i@#am@#closing~!@#$%");
                socket.close();
                Toast.makeText(my_ins, "Connection Closed", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                Toast.makeText(my_ins, "Unable to close Connection", Toast.LENGTH_SHORT).show();
            }
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



