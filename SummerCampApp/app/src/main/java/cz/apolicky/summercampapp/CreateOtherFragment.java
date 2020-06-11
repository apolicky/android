package cz.apolicky.summercampapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for other information fragment.
 */
public class CreateOtherFragment extends Fragment {

    private TextInputLayout otherInfoTextInputLayout;
    private boolean noOtherInfo = false;
    private boolean numberOfLinksShown = false;
    private String lastText = "";
    private CreateConfiguration CC;

    public CreateOtherFragment(CreateConfiguration cc){
        CC = cc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_other, container, false);

        otherInfoTextInputLayout = view.findViewById(R.id.other_info_text_input_layout);

        final Button skipButton = view.findViewById(R.id.skipOtherInfoButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noOtherInfo = true;
                CC.setTab(3);
            }
        });

        final Button checkButton = view.findViewById(R.id.createOtherCheckButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields() && numberOfLinksShown){
                    CC.setTab(3);
                }
            }
        });

        final Button backButton = view.findViewById(R.id.createOtherExitButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CC.setTab(1);
            }
        });

        return view;
    }

    /**
     * Checks fields. For the first try returns false and tells the user, how many links have been
     * found in the text.
     * @return true if all fields are ok, else false
     */
    private boolean checkFields(){
        String text = otherInfoTextInputLayout.getEditText().getText().toString();

        if(text.equals(lastText) && numberOfLinksShown){
            otherInfoTextInputLayout.setError(null);
            return true;
        }

        if(text.isEmpty()){
            noOtherInfo = true;
            numberOfLinksShown = true;
            lastText = text;
            otherInfoTextInputLayout.setError(getString(R.string.create_other_info_no_link_found_cz));
            return false;
        }
        else{
            numberOfLinksShown = true;
            Pattern p = Pattern.compile(getResources().getString(R.string.other_info_link_pattern));
            Matcher m = p.matcher(text);
            int i = 0;
            while(m.find()){
                i++;
            }
            numberOfLinksShown = true;
            otherInfoTextInputLayout.setError(i + getString(R.string.create_other_info_x_links_found_cz));
            lastText = text;
            return false;
        }
    }

    public boolean noOtherInfo(){
        return noOtherInfo;
    }

    /**
     * @return the text with other information
     */
    public String getOtherInfo(){
        return otherInfoTextInputLayout.getEditText().getText().toString();
    }
}
