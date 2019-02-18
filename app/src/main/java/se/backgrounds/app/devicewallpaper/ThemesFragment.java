package se.backgrounds.app.devicewallpaper;

import android.os.Bundle;

/**
 * Created by stude on 2018-03-30.
 */

public class ThemesFragment extends android.support.v4.app.Fragment {



    public static ImageGridFragment newInstance(String text) {

        ImageGridFragment imageGridFragment = new ImageGridFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        imageGridFragment.setArguments(b);

        return imageGridFragment;
    }
}
