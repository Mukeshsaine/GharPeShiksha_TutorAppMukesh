package com.gharpeshiksha.tutorapp.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.sharedpreference.SessionConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public final String TAG = "MyFirebaseSer.java";
    Intent resultIntent;
    PendingIntent pendingIntent;
    int id = 12;

    // String TAG = "MyFirebaseMessagingService.java";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getData());
    }

    private void showNotification(Map<String, String> data) {

        String title = data.get("title");
        String message = data.get("message");
        String enq_id = data.get("enq_id");
        String phone = data.get("phone");
        String image = data.get("image");
        String url = data.get("url");
        String type = data.get("type");


        Log.e("FirebaseIncomingMsg ",
                "title :  " + title +
                        "\n--> message " + message +
                        "\n----> enq_id  " + enq_id +
                        "\n-----> phone " + phone +
                        "\n-----> url " + url +
                        "\n-----> image url " + image +
                        "\n-----> type " + type);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("com.gharpeshiksha.tutorapp", "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.setVibrationPattern(new long[]{100, 1000, 100, 1000, 100});

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        if (type != null && !type.isEmpty()) {

            Log.d("NotificationStructure", " (First) type =  " + type);

            if (type.equals("class")) {

                // CONDITION WHEN CLASS IS TO BE SHOWN IN CLASSES_FOR_ME_ACTIVITY SO TITLE, MESSAGE, ENQ_ID, PHONE SHOULD NOT BE NULL

                if (phone != null && !phone.isEmpty() && title != null && !title.isEmpty() && message != null && !message.isEmpty() && enq_id != null && !enq_id.isEmpty()) {

                    Log.d("NotificationStructure",
                            " (Second) InImage =>  \nphone = " + phone + "\ntitle = " + title + "\nMessage = " + message + "\nEnq_id = " + enq_id);

                    resultIntent = new Intent(MyFirebaseMessagingService.this, ClassesForMeActivity.class)
                            .putExtra("enqId", enq_id.trim())
                            .putExtra("phone", phone.trim())
                            .putExtra("viewContact", "false")
                            .putExtra("from", "notification");
                    pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, "com.gharpeshiksha.tutorapp")
                            .setContentTitle(title.trim())
                            .setSmallIcon(R.drawable.ic_stat_ic_notification)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_ic_notification))
                            .setAutoCancel(true)
                            .setColorized(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setVibrate(new long[]{100, 1000, 100, 1000, 100})
                            /*.setContentText("Respond now !")*/
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message.trim()))
                            .setContentIntent(pendingIntent);
                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MyFirebaseMessagingService.this);
                    managerCompat.notify(id, builder.build());
                    id++;
                }

            } else if (type.equals("image")) {

                // CONDITION WHEN IMAGE IS TO BE SHOWN HENCE NEED TO CHECK FOR IMAGE, TITLE, MESSAGE IS NOT NULL

                if (image != null && !image.isEmpty() && title != null && !title.isEmpty() && message != null && !message.isEmpty()) {

                    Log.d("NotificationStructure",
                            " (Third) InImage =>  \nimage = " + image + "\ntitle = " + title + "\nMessage = " + message);

                    resultIntent = new Intent(MyFirebaseMessagingService.this, Splash.class);

                    pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);

                    getBitmapfromUrl(image.trim(), title.trim(), message.trim(), pendingIntent);

                }

            } else if (type.equals("webview")) {

                // CONDITION WHEN WEB-VIEW IS NEEDED THUS NEED TO CHECK IF URL, TITLE, MESSAGE, IMAGE IS NOT NULL

                if (url != null && !url.isEmpty() && title != null && !title.isEmpty() && message != null && !message.isEmpty() && image != null && !image.isEmpty()) {

                    Log.d("NotificationStructure",
                            " (Fourth) InWebView =>  \nurl = " + url + "\ntitle = " + title + "\nMessage = " + message);

                    resultIntent = new Intent(MyFirebaseMessagingService.this, OnNotificationWebView.class)
                            .putExtra("url", url.trim());

                    pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT |
                            PendingIntent.FLAG_IMMUTABLE);

                    getBitmapfromUrl(image.trim(), title.trim(), message.trim(), pendingIntent);

                }
            } else if (type.equals("tutorChatNotification")) {
                //Notification when student send message title, message, image should not be null.
                String[] str = title.split(",");
                String name = str[0].trim();
                String studentUUId = str[2].trim();
                String tutorUUId = str[1].trim();
                Log.d(TAG, "showNotification: " + studentUUId + ", " + tutorUUId);
                Intent i = new Intent(MyFirebaseMessagingService.this, Chats_all_StudentsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("tutorname", name);
                i.putExtra("UrlPic", image);
                i.putExtra("studentsUUID1",studentUUId);
                i.putExtra("tutorUUID1", tutorUUId);
                i.putExtra("enqId", enq_id);

                PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,
                        1, i, PendingIntent.FLAG_IMMUTABLE);
                NotificationCompat.Builder notificationBuild = new NotificationCompat.Builder(MyFirebaseMessagingService.this,
                        "com.gharpeshiksha.tutorapp")
                        .setContentTitle("GharPeShiksha")
                        .setContentText(name + " just message you " + "\"" + message + "\"" )

                        .setAutoCancel(true)
                        .setColorized(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentIntent(pendingIntent);

                NotificationManagerCompat compat = NotificationManagerCompat.from(this);
                compat.notify(++id, notificationBuild.build());
            }

        }
    }

    @Override
    public void onNewToken(String token) {
        SessionConfig sessionConfig = new SessionConfig(getApplicationContext());
        sessionConfig.setToken(token);
    }

    public void getBitmapfromUrl(String imageUrl, String title, String message, PendingIntent pendingIntent) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, "com.gharpeshiksha.tutorapp")
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    .setColorized(true)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 1000, 100, 1000, 100})
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(bitmap))
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MyFirebaseMessagingService.this);
            managerCompat.notify(id, builder.build());
            id++;

        } catch (Exception e) {
            e.printStackTrace();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, "com.gharpeshiksha.tutorapp")
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_ic_notification))
                    .setAutoCancel(true)
                    .setColorized(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[]{100, 1000, 100, 1000, 100})
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    /*.setContentText(message)*/
                    .setContentIntent(pendingIntent);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MyFirebaseMessagingService.this);
            managerCompat.notify(id, builder.build());
            id++;
        }
    }
}
