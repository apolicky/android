package cz.apolicky.summercampapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * An adapter for displaying links from Other.
 */
public class OtherLinksAdapter extends BaseAdapter {

    private Context context;
    private List<String> linkURLs;
    private List<String> linkNames;
    private LayoutInflater mInflater;

    public OtherLinksAdapter(Context c, List<String> urls, List<String> names){
        context = c;
        linkURLs = urls;
        linkNames = names;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return linkNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * For i-th link on click launches activity that should open the website link points to.
     * @param i index of element
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.other_info_adapter, null);

        final TextView link = v.findViewById(R.id.otherInfoAdapterTextView);
        link.setText(linkNames.get(i));
        link.setOnClickListener(new ArgumentedOnClickListener(i) {
            @Override
            public void onClick(View view) {
                String url = linkURLs.get(argument);
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });


        return  v;
    }
}
