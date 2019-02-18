package se.backgrounds.app.devicewallpaper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by stude on 2018-04-24.
 */

public class IntroAnimView extends View {

    private static final int N_FRAMES = 6;
    private int n_blinks = 1;
    private int index, modulo, frameCnt;
    private int width, height;
    private boolean isBlinking, awake; //isBlinking: animation igång; awake: i blinkningscykeln - när spriten tittar fullt ut
    private Paint paint = new Paint();

    private Bitmap[] smileyImages = new Bitmap[N_FRAMES];

    public IntroAnimView(Context context, AttributeSet attributes) {
        super(context, attributes);

        //hämta från xml
        TypedArray myAttrubutes = context.obtainStyledAttributes(attributes, R.styleable.IntroAnimViewAttributes);
        width = myAttrubutes.getDimensionPixelSize(R.styleable.IntroAnimViewAttributes_sprite_width, 150);

        //generera anpassade bilder
        BitmapCreator.createBitmaps("smiley", smileyImages, context, "drawable", width, Bitmap.Config.RGB_565);

        height = smileyImages[0].getHeight();

        //initiera
        isBlinking = true;
        modulo = 2;
    }


    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawBitmap(smileyImages[index], 0, 0, paint);
        blink();

        if (!isBlinking) {

            double d = Math.random();
            if (d > 0.99) {
                isBlinking = true;

               if (Math.random() > 0.5) {
                   modulo = 2;
                   n_blinks = 1;
               }
               else {
                   modulo = 1;
                   n_blinks = 2;
               }
            }

        }

        invalidate();


    }

    private void blink() {

        //1 generera blinkningar tills villkoret ej längre uppfylls
        int n = (N_FRAMES - 1) * 2 * n_blinks; // totala antalet iterationer för att åstadkomma nBlinks
        if (frameCnt != n * modulo && isBlinking) { // dvs förändra ögonen till nBlinks uppnåtts

            iterateBlink();
        } else { // färdig = nollställ
            frameCnt = 0;
            index = 0;
            isBlinking = false; // kan börja randomisera på nytt
        }
    }

    private void iterateBlink() {

        int rest = 0;
        if (modulo > 0) {
            rest = frameCnt % modulo;
        }

        if (index == N_FRAMES - 1) {
            awake = false; //blundar
        } else if (index == 0) {
            awake = true; // tittar
        }

        //sluter och öppnar ögonen
        if (rest == 0 && awake) {
            index++;
        } else if (rest == 0 && !awake) {
            index--;
        }

        frameCnt++;
    }

    public int getImageWidth() {

        return width;
    }

    public int getImageHeight() {

        return height;
    }

}
