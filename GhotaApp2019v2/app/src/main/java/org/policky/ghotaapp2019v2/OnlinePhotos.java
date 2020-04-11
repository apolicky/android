package org.policky.ghotaapp2019v2;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import java.util.HashSet;

public class OnlinePhotos extends Fragment {

    ImageView iV;
    HashSet linksRead;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        linksRead = new HashSet<String>();

        View view = inflater.inflate(R.layout.frag_online_photos_layout,container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        iV = view.findViewById(R.id.imageView);

        iV.setImageDrawable(LoadImageFromWebOperations("http://ghota.net/2019/graphics/online/12-m-1.jpg"));

        getPageLinks("http://ghota.net/2019/graphics/online/");

        for(Object o : linksRead){
            System.out.println(o);
        }

        return view;
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            System.out.println("height: " + d.getMinimumHeight() + " width: " + d.getMinimumWidth());
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getPageLinks(String URL) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (!linksRead.contains(URL)) {
            try {
                //4. (i) If not add it to the index
                if (linksRead.add(URL)) {
                    System.out.println(URL);
                }
                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[img]");
                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:img"));
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

}

