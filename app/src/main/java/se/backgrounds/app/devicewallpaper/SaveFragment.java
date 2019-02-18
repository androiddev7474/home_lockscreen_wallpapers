package se.backgrounds.app.devicewallpaper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by stude on 2018-04-07.
 */

public class SaveFragment extends DialogFragment {

    private int nFavosToBeSaved;

    private View fragmentView;
    private TextView nFavosInfoTextView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //nFavosToBeSaved = getArguments().getInt(MainActivity.N_FAVOS_TO_BE_SAVED, 0);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.save_fragment_layout, container, false);
        //nFavosInfoTextView.setText(getString(R.string.save_frag_info_text) + nFavosToBeSaved);

        Button okButton = (Button) fragmentView.findViewById(R.id.okbutton_id);

        return fragmentView;
    }


}
