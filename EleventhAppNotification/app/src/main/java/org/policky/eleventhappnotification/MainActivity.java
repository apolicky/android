package org.policky.eleventhappnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button notificationButton;
    NotificationCompat.Builder notification;
    private static final int uniqueID = 42312;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeChannels(this);

        notification = new NotificationCompat.Builder(this,"default");
        notification.setAutoCancel(true);

        notificationButton = (Button) findViewById(R.id.notifyButton);

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification(view);
            }
        });
    }

    private void sendNotification(View view){
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("This is a ticker.");
        try {
            String myDate = "2019/07/24 12:35:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = sdf.parse(myDate);
            long millis = date.getTime();

            notification.setWhen(millis);

        } catch (ParseException e) {
            e.printStackTrace();
            notification.setWhen(System.currentTimeMillis());

        }

        notification.setContentTitle("This is a title.");
        notification.setContentInfo("This is an info.");
        notification.setContentText("This is a message you'll see in action bar.");


        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);


        NotificationManager nM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nM.notify(uniqueID,notification.build());


    }

    public void initializeChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }
}
