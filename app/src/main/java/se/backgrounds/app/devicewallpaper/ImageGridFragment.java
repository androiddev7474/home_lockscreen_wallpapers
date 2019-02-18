package se.backgrounds.app.devicewallpaper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by stude on 2018-03-30.
 */

public class ImageGridFragment extends android.support.v4.app.Fragment {

    private GridView imageEtcGridView;
    private ImageGridBaseAdapter imageGridBaseAdapter;

    protected void initGalleryAdapter(Context context,  ArrayList <CheckedFavorites> checkedImagesList) {

        imageGridBaseAdapter = new ImageGridBaseAdapter(context, checkedImagesList);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {

        View view = layoutInflater.inflate(R.layout.image_grid_layout, container, false);

        imageEtcGridView = view.findViewById(R.id.gridview);

        imageEtcGridView.setAdapter(imageGridBaseAdapter);

        imageEtcGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), "image " + position, Toast.LENGTH_SHORT).show();

                ImageInfoDialog dialog = new ImageInfoDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("gridViewPosition", position);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "imageInfoDialogFragment");

            }
        });

        return view;
    }


    public static ImageGridFragment newInstance(String text) {

        ImageGridFragment imageGridFragment = new ImageGridFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        imageGridFragment.setArguments(b);

        return imageGridFragment;
    }

    protected ImageGridBaseAdapter getImageGridAdapter() {

        return imageGridBaseAdapter;
    }

}
