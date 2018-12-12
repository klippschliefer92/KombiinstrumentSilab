package com.example.mauriziosalini.kombiinstrumentsilab;

import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client extends AsyncTask<Void, Float, Float> {

    Socket s;
    DataInputStream dis;
    InputStream is;
    ImageView imageView;
    ImageView imageView2;
    TextView textViewGear;
    TextView textViewkm;

    String server;
    int port;
    double speed;
    double rpm;
    double km;
    double gear;
    float angle;
    float angle2;
    String geartext;



    public Client(String server, int port, ImageView imageView, ImageView imageView2, TextView textViewkm, TextView textViewGear){
        this.server = server;
        this.port = port;
        this.imageView = imageView;
        this.imageView2 = imageView2;
        this.textViewkm = textViewkm;
        this.textViewGear = textViewGear;
    }





    @Override
    protected void onProgressUpdate(Float ... angle){
        super.onProgressUpdate(angle);
        RotateAnimation rotate = new RotateAnimation(angle[0], angle[0], Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);
        imageView.startAnimation(rotate);

        RotateAnimation rotate2 = new RotateAnimation(angle2, angle2, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate2.setDuration(3000);
        rotate2.setInterpolator(new LinearInterpolator());
        rotate2.setFillAfter(true);
        imageView2.startAnimation(rotate2);

        textViewkm.setText(Double.toString(km));
        textViewGear.setText(geartext);

       // textView.setText(Double.toString(speed[0]));


    }


    @Override
    protected Float doInBackground(Void... voids) {
        try {
            s = new Socket(server, port);
            is = s.getInputStream();
            dis = new DataInputStream(is);

            while (s.isConnected()){
                speed = dis.readDouble();
                rpm = dis.readDouble();
                gear = dis.readDouble();
                km = dis.readDouble();


                speed = (speed * 1.038) - 45;
                rpm = Math.round(rpm);
                rpm = (rpm * 0.0337) -45;

                gear = Math.round(gear);
                if (gear == 1){
                    geartext = "1";
                }
                if (gear == 2){
                    geartext = "2";
                }
                if (gear == 3){
                    geartext = "3";
                }
                if (gear == 4){
                    geartext = "4";
                }
                if (gear == 5) {
                    geartext = "5";
                }
                if (gear == 6){
                    geartext = "6";
                }
                if (gear == 7){
                    geartext = "7";
                }

                km = km * 0.001;
                km = Math.round(km * 100.0)/100.0;

                angle = (float)speed;
                angle2 = (float)rpm;
                Log.i("Speed", Double.toString(speed));
                publishProgress(angle);

            }

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
