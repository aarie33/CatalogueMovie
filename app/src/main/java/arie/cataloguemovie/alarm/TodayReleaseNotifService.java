package arie.cataloguemovie.alarm;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

import arie.cataloguemovie.R;

public class TodayReleaseNotifService extends IntentService {
    public static String REPLY_ACTION = "arie.cataloguemovie.alarm.REPLY_ACTION";
    private static String KEY_REPLY = "key_reply_message";

    private int mNotificationId;
    private int mMessageId;
    private String title, message;
    public TodayReleaseNotifService() {
        super("TodayReleaseNotifService");
    }
    public TodayReleaseNotifService(String title, String message) {
        super("TodayReleaseNotifService");

        this.title = title;
        this.message = message;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            showNotification();
        }
    }

    private void showNotification() {
        mNotificationId = 1;
        mMessageId = 123;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(this.title)
                .setContentText(this.message)
                .setShowWhen(true);

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }

    private PendingIntent getReplyPendingIntent() {
        Intent intent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = TodayReleaseBroadcastReceiver.getReplyMessageIntent(this, mNotificationId, mMessageId);
            return PendingIntent.getBroadcast(getApplicationContext(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
//        } else {
//            intent = ReplyActivity.getReplyMessageIntent(this, mNotificationId, mMessageId);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            return PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
    }

    public static CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_REPLY);
        }
        return null;
    }
}
