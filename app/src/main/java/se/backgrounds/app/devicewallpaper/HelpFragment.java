package se.backgrounds.app.devicewallpaper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stude on 2018-04-26.
 */

public class HelpFragment extends DialogFragment {

    private View helpFragmentView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        helpFragmentView = inflater.inflate(R.layout.help_fragment_content_layout, container, false);

        return helpFragmentView;
    }

}
