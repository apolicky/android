package cz.apolicky.summercampapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

/**
 * A class for online photos.
 */
public class CreatePhotosFragment extends Fragment {

    private boolean noPhotos = false;
    private String lastText;
    private TextInputLayout webPage;
    private CreateConfiguration CC;

    public CreatePhotosFragment(CreateConfiguration cc){
        CC = cc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_photos, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        webPage = view.findViewById(R.id.photos_text_input_layout);

        final Button skipButton = view.findViewById(R.id.create_photos_skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noPhotos = true;
                CC.setTab(4);
            }
        });

        final Button checkButton = view.findViewById(R.id.create_photos_check_button);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(webPageOK()){
                    CC.setTab(4);
                }
            }
        });

        final Button backButton = view.findViewById(R.id.create_photos_exit_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CC.setTab(2);
            }
        });


        return view;
    }

    /**
     * Checks if web page for online photos are correct.
     * @return true if web page exists, false if not
     */
    private boolean webPageOK(){
        String text = webPage.getEditText().getText().toString();

        if(lastText != null && text.equals(lastText)){
            webPage.setError(null);
            return true;
        }

        if(text.isEmpty()){
            webPage.setError(getResources().getString(R.string.create_photos_no_webpage_cz));
            noPhotos = true;
            lastText = text;
            return false;
        }
        else{
            if(HTMLDownloader.tryPage(text)){
                return true;
            }
            else{
                webPage.setError(text + getResources().getString(R.string.create_photos_webpage_does_not_exist_cz));
                lastText = text;
                return false;
            }

        }
    }

    /**
     * @return true if tab is not used else false
     */
    public boolean noPhotos(){
        return noPhotos;
    }

    /**
     * @return the web page where photos are located
     */
    public String getPhotoAddress(){
        return webPage.getEditText().getText().toString();
    }

}
