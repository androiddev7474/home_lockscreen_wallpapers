package se.backgrounds.app.devicewallpaper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by stude on 2018-04-17.
 */

public class BackgroundLoaderService extends Service {

    static boolean restartServiceOnDestroy = true;
    private static Context context;

    private MyTimer myTimer;

    public BackgroundLoaderService() {


        myTimer = new MyTimer(context);
    }


    public BackgroundLoaderService(Context appContext) {
        super();
        context = appContext;
        //myTimer = new MyTimer(context);
        Log.i("HERE", "here I am!");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        myTimer.setContext(getApplicationContext());
        myTimer.startTimer();
        System.err.println("onStartCommand called");
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (restartServiceOnDestroy) {

            context = getApplicationContext();
            System.err.println("context i onDestroy serviceclass: " + context);
            Intent broadcastIntent = new Intent("backgroundServiceRestart");
            sendBroadcast(broadcastIntent);
            myTimer.stoptimertask();
            myTimer.setContext(context);
            Log.i("SERVICE: ", "service stoppad med kommer att startas om :-)" );

        } else {

            myTimer.stoptimertask();
            Log.i("SERVICE: ", "service stoppad" );
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }



}
