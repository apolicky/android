package org.policky.ghotaapp2019v2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OnlinePhotosAdapter extends BaseAdapter {

    private Context context;
    private List<String> imageUrls;
    private List<Drawable> images;
    private LayoutInflater mInflater;

    public OnlinePhotosAdapter(Context c, List<String> i_urls){
        context = c;
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
        View v = (View) mInflater.inflate(R.layout.online_photos_layout, null);

        final ImageView image = (ImageView) v.findViewById(R.id.photos_image_view);
        image.setImageDrawable(images.get(i));

        return  v;
    }

    private void downloadImages(){
        for(String im_url : imageUrls){
            try {
                InputStream is = (InputStream) new URL(im_url).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                images.add(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
