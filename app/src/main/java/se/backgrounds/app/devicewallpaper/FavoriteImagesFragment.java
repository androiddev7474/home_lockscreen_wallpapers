package se.backgrounds.app.devicewallpaper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by stude on 2018-03-30.
 */

public class FavoriteImagesFragment extends android.support.v4.app.Fragment {

    private GridView imageEtcGridView;
    private FavoImageGridBaseAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initFavoAdapter(Context context) {

        adapter = new FavoImageGridBaseAdapter(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.image_grid_layout, container, false);

        imageEtcGridView = view.findViewById(R.id.gridview);
        imageEtcGridView.setAdapter(adapter);


        return view;
    }


    public static FavoriteImagesFragment newInstance(String text, ArrayList<CheckedFavorites> favoriteList) {

        FavoriteImagesFragment favoriteImagesFragment = new FavoriteImagesFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        favoriteImagesFragment.setArguments(b);

        return favoriteImagesFragment;
    }

    public FavoImageGridBaseAdapter getFavoAdapter() {

        return adapter;
    }


}
