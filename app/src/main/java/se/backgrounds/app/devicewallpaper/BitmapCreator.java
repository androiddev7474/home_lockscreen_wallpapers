package se.backgrounds.app.devicewallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;

import se.test.app.bitmapdecoder.BitmapDecoder;

/**
 * Created by stude on 2018-02-12.
 * För att skala en bild till önskad storlek. Till detta används biblioteket BitmapDecoder
 *
 */

public class BitmapCreator {

    /**
     *
     * @param name namn på bilden (det riktiga bildnamnet måste alltid vara name + ett indexnummer där första bilden börjar med 0
     * @param bitmap bitmap-vektor som ska fyllas med referenser till en hämtat bild
     * @param context aktuell context
     * @param resFolder resursmapp där bildresursen finns
     * @param width önskad bredd på bilden
     * @param config bitmap-konfig anger antalet bitar för respektive RGBA-kanal.
     * @return
     */
    public static void createBitmaps(String name, Bitmap[] bitmap, Context context, String resFolder, int width, Bitmap.Config config) {


        for (int i = 0; i < bitmap.length; i++) {

            int resId = context.getResources().getIdentifier(name + (i + 1), resFolder, context.getPackageName());
            int iW = ContextCompat.getDrawable(context, resId).getIntrinsicWidth();//ResourcesCompat.getDrawable(context.getResources(), resId, null).geti
            int iH = ContextCompat.getDrawable(context, resId).getIntrinsicHeight();
            float hRatio = ((float) iH) / iW;
            int defW = width;
            int defH = (int) (defW * hRatio);
            bitmap[i] = BitmapDecoder.decodeSampledBitmapFromResource(context.getResources(), resId, defW, defH, config);
        }
    }


}