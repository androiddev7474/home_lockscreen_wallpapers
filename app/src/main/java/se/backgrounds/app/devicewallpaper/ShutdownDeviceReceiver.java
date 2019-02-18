package se.backgrounds.app.devicewallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static se.backgrounds.app.devicewallpaper.MainActivity.MY_PREFS_NAME;

/**
 * Created by stude on 2018-04-24.
 * Björn Hallström
 * Version: 1
 * Då enheten stängs ner kommer tjänsten att dödas och därför måste service-switchens mode sättas till false
 * o sparas i sharedpreferences.
 */

public class ShutdownDeviceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("serviceSwitchState", false);
        editor.apply();

    }

}
