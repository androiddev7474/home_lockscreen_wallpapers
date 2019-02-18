package se.backgrounds.app.devicewallpaper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

import static android.content.Context.MODE_PRIVATE;
import static se.backgrounds.app.devicewallpaper.MainActivity.MY_PREFS_NAME_TIMER;
import static se.backgrounds.app.devicewallpaper.MainActivity.MY_PREFS_TIMER_BUTTON_VALUE;
import static se.backgrounds.app.devicewallpaper.MainActivity.MY_PREFS_TIMER_VALUE;

/**
 * Created by stude on 2018-04-26.
 */

public class TimerFragment extends DialogFragment {

    //primitiver
    private static final int N_TIMERS = 4;
    private static final int TIMER1_INDEX = 0;
    private static final int TIMER2_INDEX = 1;
    private static final int TIMER3_INDEX = 2;
    private static final int TIMER4_INDEX = 3;
    public static final int TEN_SECONDS = 10000;
    public static final int ONE_MINUTE = 60000;
    public static final int ONE_HOUR = 3600000;
    public static final int ONE_DAY = 86400000;
    public static final int TIMER_DEFAULT_VALUE = ONE_DAY;
    int[] radioIDs = {R.id.timer1_radio_id, R.id.timer2_radio_id, R.id.timer3_radio_id, R.id.timer4_radio_id};
    private static final String MY_PREFS_BUTTON_KEY = "radio_button_set";
    private int storedRadioButtonIndex; //indexväde för radioButton - lagrat i shared_preferences. default värde är "varje dag"

    //Android API klasser
    private View timerFragView;
    private RadioButton[] timerRadios = new RadioButton[N_TIMERS];
    private Switch aSwitch;

    //egna klasser
    private ServiceChangeNotifier serviceChangeNotifier;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_TIMER_BUTTON_VALUE, MODE_PRIVATE);
        storedRadioButtonIndex = prefs.getInt(MY_PREFS_BUTTON_KEY, TIMER4_INDEX);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        timerFragView = inflater.inflate(R.layout.timer_dialog_content_layout, container, false);

        for (int i = 0; i < timerRadios.length; i++) {
            timerRadios[i] = timerFragView.findViewById(radioIDs[i]);
        }
        timerRadios[storedRadioButtonIndex].setChecked(true);

        aSwitch = (Switch) timerFragView.findViewById(R.id.service_switch_id);
        boolean isOn = getArguments().getBoolean("serviceState");
        aSwitch.setChecked(isOn);

        initSwitchListener();

        initRadioButtonListeners();

        return  timerFragView;
    }


    public void setServiceSwitch(boolean on) {

        aSwitch.setChecked(on);
    }


    /**
     * initiera interfacet för tjänst
     * @param serviceChangeNotifier
     */
    public void setSwitchInterface(ServiceChangeNotifier serviceChangeNotifier) {

        this.serviceChangeNotifier = serviceChangeNotifier;

    }


    /**
     * switch-on: tjänsten börjar som går via ett callbackinterace . switch-on - tjänsten avslutas
     */
    private void initSwitchListener() {

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean serviceOn) {

                if (serviceOn)
                    serviceChangeNotifier.startStopService(true);
                else
                    serviceChangeNotifier.startStopService(false);

            }
        });
    }


    /**
     * lysnnare för radiobuttons där man väljer hur ofta bytet av bakgrundsbild ska inträffa
     */
    private void initRadioButtonListeners() {

        int i;
        for (i = 0; i < timerRadios.length; i++) {
            final int index = i;
            timerRadios[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        for (int j = 0; j < timerRadios.length; j++) {

                            if (j != index)
                                timerRadios[j].setChecked(false);
                            else if (j == index) {

                                aSwitch.setChecked(false);
                                serviceChangeNotifier.startStopService(false);
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_TIMER_BUTTON_VALUE, MODE_PRIVATE).edit();
                                editor.putInt(MY_PREFS_BUTTON_KEY, index);
                                editor.apply();
                            }
                        }

                        int timerValue;
                        switch (index) {

                            case TIMER1_INDEX:
                                timerValue = TEN_SECONDS;
                                break;
                            case TIMER2_INDEX:
                                timerValue = ONE_MINUTE;
                                break;
                            case TIMER3_INDEX:
                                timerValue = ONE_HOUR;
                                break;
                            case TIMER4_INDEX:
                                timerValue = ONE_DAY;
                                break;
                            default:
                                timerValue = ONE_DAY;
                                break;
                        }

                        //spara i SharedPreferences
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME_TIMER, MODE_PRIVATE).edit();
                        editor.putInt(MY_PREFS_TIMER_VALUE, timerValue);
                        editor.apply();

                    }

                }
            });
        }



    }



}
