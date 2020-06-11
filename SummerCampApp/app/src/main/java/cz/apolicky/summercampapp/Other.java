package cz.apolicky.summercampapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A fragment for displaying other information than contacts and organization stuff.
 */
public class Other extends Fragment {

    private ConfigurationManager CM;
    private TextView otherInfoTextView;
    private ListView linksListView;

    public Other(ConfigurationManager CM_){
        CM = CM_;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_other_info,container, false);

        linksListView = view.findViewById(R.id.linkListView);
        otherInfoTextView = view.findViewById(R.id.otherInfoTextView);

        findLinks(CM.getValue(getResources().getString(R.string.other_info_text)));

        return view;
    }

    /**
     * Finds links of MarkDown format in text. Replaces them with the name part
     * and the link part is passed to linksListView, on which the user can click
     * and display the websites.
     * @param oldText the text from config containing links.
     */
    private void findLinks(String oldText){
        List<String> urls = new ArrayList<>();
        List<String> names = new ArrayList<>();
        String newText = oldText;

        Pattern p = Pattern.compile(getString(R.string.other_info_link_pattern));
        Matcher m = p.matcher(oldText);
        while(m.find()){
            String match = m.group();
            String[] link_name_and_url = match.replaceAll("\\[","").replaceAll("\\)","").split("\\]\\(");
            urls.add(link_name_and_url[1]);
            names.add(link_name_and_url[0]);
            newText = newText.replace(match,link_name_and_url[0]);
        }
        linksListView.setAdapter(new OtherLinksAdapter(getContext(), urls, names));
        otherInfoTextView.setText(newText);
    }

}