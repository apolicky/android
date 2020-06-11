package cz.apolicky.summercampapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for online photos.
 */
public class OnlinePhotos extends Fragment {

    ListView photosListView;

    private ConfigurationManager CM;

    public OnlinePhotos(ConfigurationManager CM_){
        CM = CM_;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_photos,container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        photosListView = view.findViewById(R.id.photos_list_view);
        String webPage = CM.getValue(getString(R.string.photos_web_directory));

        OnlinePhotosAdapter opa = new OnlinePhotosAdapter(getContext(), webPage, imageLinks(webPage));
        photosListView.setAdapter(opa);

        return view;
    }

    /**
     * Extracts the links to images on web page 'webDirectory'
     * @param webDirectory a web page where photos are located
     * @return list of urls as strings where images should be taken from
     */
    private List<String> imageLinks(String webDirectory) {
        List<String> imageUrls = new ArrayList<>();
        try{
            String pageSource = HTMLDownloader.getPage(webDirectory);
            Pattern p = Pattern.compile(getString(R.string.photos_href_pattern));
            Matcher m = p.matcher(pageSource);
            while(m.find()){
                imageUrls.add(m.group().split("\"")[1]);
            }

            return imageUrls;
        }
        catch (IOException e){
            Toast.makeText(getContext(), webDirectory + getString(R.string.create_photos_webpage_does_not_exist_cz), Toast.LENGTH_LONG).show();
        }
        imageUrls.clear();
        return imageUrls;
    }

}

