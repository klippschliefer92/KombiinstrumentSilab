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
    TextView textViewConsumption;

    String server;
    int port;
    double speed;
    double rpm;
    double consumption;
    double gear;
    float angle;
    float angle2;



    public Client(String server, int port, ImageView imageView, ImageView imageView2, TextView textViewConsumption, TextView textViewGear){
        this.server = server;
        this.port = port;
        this.imageView = imageView;
        this.imageView2 = imageView2;
        this.textViewConsumption = textViewConsumption;
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

        textViewConsumption.setText(Double.toString(consumption));
        textViewGear.setText(Double.toString(gear));

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
                consumption = dis.readDouble();


                speed = Math.round(speed);
                speed = (speed * 1.038) - 45;
                rpm = Math.round(rpm);
                rpm = (rpm * 0.0337) -45;

                gear = Math.round(gear);

                consumption = consumption * 4.83;
                consumption = Math.round(consumption * 100.0)/100.0;

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
