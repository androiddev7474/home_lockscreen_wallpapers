package se.backgrounds.app.devicewallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by stude on 2018-04-17.
 */

public class StopServiceReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(StopServiceReceiver.class.getSimpleName(), "Service stoppades - startar på nytt!!!");

        context.startService(new Intent(context, BackgroundLoaderService.class));
    }
}
