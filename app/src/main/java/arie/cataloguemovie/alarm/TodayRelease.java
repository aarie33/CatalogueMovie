package arie.cataloguemovie.alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import arie.cataloguemovie.BuildConfig;
import arie.cataloguemovie.SearchActivity;
import arie.cataloguemovie.R;
import arie.cataloguemovie.connections.MovieItems;
import cz.msebera.android.httpclient.Header;

public class TodayRelease extends AsyncTask<String, Void, ArrayList<MovieItems>> {
    private Context ctx;
    private NotificationCompat.Builder notification;
    private Handler handler = new Handler();
    public static final int NOTIFICAITION_ID = 1;
    private final static String GROUP_RELEASE = "group_release";
    private final static int NOTIF_REQUEST_CODE = 200;

    private int idNotif = 0;
    private int maxNotif = 2;
    List<TodayReleaseNotifItem> stackNotif = new ArrayList<>();

    public TodayRelease(Context context) {
        this.ctx = context;
    }

    @Override
    protected ArrayList<MovieItems> doInBackground(String... strings) {
        return getData();
    }

    @Override
    protected void onPostExecute(ArrayList<MovieItems> movieItems) {
        super.onPostExecute(movieItems);

        Toast.makeText(ctx, "Executed", Toast.LENGTH_SHORT).show();

        for (int i = 0; i < movieItems.size(); i++) {
            TodayReleaseNotifItem notificationItem = new TodayReleaseNotifItem(idNotif, movieItems.get(i).getTitle());
            stackNotif.add(new TodayReleaseNotifItem(idNotif, movieItems.get(i).getTitle()));
            sendNotif();
            idNotif = i;
        }
    }

    private ArrayList<MovieItems> getData() {
        SyncHttpClient syncHttpClient = new SyncHttpClient();
        final ArrayList<MovieItems> list = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.ApiKey +
                "&language=en-US";
        syncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject rslt = new JSONObject(result);
                    JSONArray jsonArray = rslt.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject movie = jsonArray.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        list.add(movieItems);
                    }
                    Log.e("Get data succcess", result);
                } catch (Exception e) {
                    Log.e("Exception get Data ", String.valueOf(e));
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Error onFailure gData", String.valueOf(error));
            }
        });
        return list;
    }


    public void sendNotif() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(ctx);

        Bitmap largeIcon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_notifications_black_24dp);
        Intent intent = new Intent(ctx, SearchActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, NOTIF_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = null;
        if (idNotif < maxNotif) {
            notification = new NotificationCompat.Builder(ctx)
                    .setContentTitle("New Email from " + stackNotif.get(idNotif).getMessage())
                    .setContentText(stackNotif.get(idNotif).getMessage())
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_RELEASE)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine(stackNotif.get(idNotif).getMessage())
                    .addLine(stackNotif.get(idNotif - 1).getMessage())
                    .setBigContentTitle(idNotif + " new movies")
                    .setSummaryText("movies fom catalogue movie");
            notification = new NotificationCompat.Builder(ctx)
                    .setContentTitle(idNotif + " new movies")
                    .setContentText("movies fom catalogue movie")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setGroup(GROUP_RELEASE)
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true)
                    .build();
        }
        manager.notify(idNotif, notification);
    }
}
