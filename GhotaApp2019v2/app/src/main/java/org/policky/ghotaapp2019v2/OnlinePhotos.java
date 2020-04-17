package org.policky.ghotaapp2019v2;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnlinePhotos extends Fragment {

    ListView photosListView;

    private ConfigManager CM;

    public OnlinePhotos(ConfigManager CM_){
        CM = CM_;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_online_photos_layout,container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        photosListView = view.findViewById(R.id.photos_list_view);
        String webPage = CM.getValue(getString(R.string.photos_web_directory));
        OnlinePhotosAdapter opa = new OnlinePhotosAdapter(getContext(),imageLinks(webPage));

        photosListView.setAdapter(opa);

        return view;
    }

    // TODO vymysli, jak bude muset byt zadana slozka s obrazky. Budou muset byt ulozeny primo na miste nebo bude stacit tam mit odkazy na ne a udelat navigaci s relativni cestou.

    private List<String> imageLinks(String webDirectory) {
        List<String> imageUrls = new ArrayList<>();
        try{
            String pageSource = HTMLDownloader.getPage(webDirectory);
            Pattern p = Pattern.compile(getString(R.string.photos_href_pattern));
            Matcher m = p.matcher(pageSource);
            while(m.find()){
                imageUrls.add(m.group());
            }

            return imageUrls;
        }
        catch (IOException e){
            Toast.makeText(getContext(), "Could not find web page " + webDirectory, Toast.LENGTH_LONG).show();
        }
        imageUrls.clear();
        return imageUrls;
    }

}

