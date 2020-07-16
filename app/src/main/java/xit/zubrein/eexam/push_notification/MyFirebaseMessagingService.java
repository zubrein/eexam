package xit.zubrein.eexam.push_notification;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import xit.zubrein.eexam.HomeActivity;

import static android.content.ContentValues.TAG;


public  class MyFirebaseMessagingService extends FirebaseMessagingService {
NotificationHelper notificationHelper;
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        if (remoteMessage.getNotification() != null) {
//            String title = remoteMessage.getNotification().getTitle();
//            String body = remoteMessage.getNotification().getBody();
//            NotificationHelper.displayNotification(getApplicationContext(), title, body);
//        }
//    }
@Override
public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.e(TAG, "From: " + remoteMessage.getFrom());

    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {

        Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
        try {
            JSONObject json = new JSONObject(remoteMessage.getData().toString());
            handleDataMessage(json);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {


            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            NotificationHelper.displayNotification(getApplicationContext(), title, body);
        }
}
    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        System.out.println("Data Message: "+json);
        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");
            String articleData = payload.getString("article_data");

            //Send notification data to MessageShowActivity class for showing
            Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("timestamp", timestamp);
            resultIntent.putExtra("article_data", articleData);
            resultIntent.putExtra("image", imageUrl);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
            System.out.println("in 1st CATCH");
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            System.out.println("in 2nd CATCH");
        }
    }
    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationHelper = new NotificationHelper(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationHelper.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationHelper = new NotificationHelper(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationHelper.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
