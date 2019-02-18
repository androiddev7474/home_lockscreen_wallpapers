package se.backgrounds.app.devicewallpaper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stude on 2018-04-24.
 */

public class IntroFragment extends DialogFragment {

    private View fragmentView;
    private IntroAnimView introAnimView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.intro_fragment_layout, container, false);
        introAnimView = fragmentView.findViewById(R.id.introAnimView_id);
        introAnimView.getLayoutParams().width = introAnimView.getImageWidth();
        introAnimView.getLayoutParams().height = introAnimView.getImageHeight();
        introAnimView.requestLayout();

        return fragmentView;
    }
}
