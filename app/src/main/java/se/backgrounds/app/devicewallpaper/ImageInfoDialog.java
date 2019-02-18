package se.backgrounds.app.devicewallpaper;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import se.test.app.bitmapdecoder.BitmapDecoder;

/**
 * Created by stude on 2018-04-24.
 */

public class ImageInfoDialog extends DialogFragment {

    private View dialogView;
    private ImageView dialogImage;
    private TextView dialogText;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {



        dialogView = layoutInflater.inflate(R.layout.image_info_dialog_content, container, false);
        dialogImage = dialogView.findViewById(R.id.image_info_diaolog_id);
        dialogText = dialogView.findViewById(R.id.text_info_dialog_id);

        int position = getArguments().getInt("gridViewPosition");
        TypedArray typedArray = getResources().obtainTypedArray(R.array.images);
        int resID =  typedArray.getResourceId(position, 0);
        Bitmap bitmap = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), resID, 300, 570, Bitmap.Config.RGB_565);
        dialogImage.setImageBitmap(bitmap);

        String[] imageInfoTextArray = getResources().getStringArray(R.array.image_info_text);
        dialogText.setText(imageInfoTextArray[position]);



        return dialogView;
    }

}
