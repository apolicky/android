package cz.apolicky.summercampapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An adapter for creating photos list view.
 */
public class OnlinePhotosAdapter extends BaseAdapter {

    private Context context;
    private String webPage;
    private List<String> imageUrls;
    private List<Drawable> images;
    private LayoutInflater mInflater;

    public OnlinePhotosAdapter(Context c, String webPge, List<String> i_urls){
        context = c;
        webPage = webPge;
        imageUrls = i_urls;
        images = new ArrayList<>();
        downloadImages();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = (View) mInflater.inflate(R.layout.photos_adapter, null);

        final ImageView image = (ImageView) v.findViewById(R.id.photos_image_view);
        image.setImageDrawable(images.get(i));

        return  v;
    }

    /**
     * Downloads images from 'imageUrls' and puts whem in 'images' drawable list.
     */
    private void downloadImages(){
        for(String im_url : imageUrls){
            try {
                String[] urlParts = im_url.split("/");
                String[] webPageParts = webPage.split("/");
                int jumpBack = 0;
                int i = 0;
                while(urlParts[i].equals("..")){
                    jumpBack++;
                    i++;
                }
                String newUrl = "";
                for(i = 0; i < webPageParts.length - jumpBack; i++){
                    newUrl += webPageParts[i] + "/";
                }
                for(i = jumpBack; i < urlParts.length - 1; i++){
                    newUrl += urlParts[i] + "/";
                }
                newUrl += urlParts[i];

                if(!newUrl.startsWith("http://") && !newUrl.startsWith("https://")){
                    newUrl = "http://" + newUrl;
                }

                InputStream is = (InputStream) new URL(newUrl).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                images.add(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
