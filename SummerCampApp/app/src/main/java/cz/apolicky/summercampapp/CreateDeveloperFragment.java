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

import java.util.List;

/**
 * A class for creating developers information.
 */
public class CreateDeveloperFragment extends Fragment {

    private TextInputLayout author, email, configName;

    private CreateConfiguration CC;
    public CreateDeveloperFragment(CreateConfiguration cc){
        CC = cc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_developer, container, false);

        initializeFields(view);

        final Button backButton = view.findViewById(R.id.developerBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CC.setTab(3);
            }
        });

        final Button sendButton = view.findViewById(R.id.developerSendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()){
                    CC.sendConfiguration();
                }
            }
        });

        return view;
    }

    /**
     * Initializes fields wrt view 'v'
     * @param v
     */
    private void initializeFields(View v){
        author = v.findViewById(R.id.devName);
        email = v.findViewById(R.id.developerEmail);
        configName = v.findViewById(R.id.create_config_name_text_input_layout);
    }

    /**
     * Checks fields
     * @return
     */
    private boolean checkFields() {
        int indicator = 0;
        indicator += nameOK();
        indicator += emailOK();
        indicator += configNameOK();
        return indicator == 3;
    }

    /**
     * Checks name format
     * @return 1 if OK, 0 if not
     */
    private int nameOK(){
        if(!author.getEditText().getText().toString().isEmpty()){
            author.setError(null);
            return 1;
        }
        author.setError(getString(R.string.create_organization_fill_in_name_cz));
        return 0;
    }

    /**
     * Checks email format
     * @return 1 if OK, 0 if not
     */
    private int emailOK(){
        String addr = email.getEditText().getText().toString();
        if(!addr.isEmpty()){
            String[] parts = addr.trim().split("@");
            if(parts.length == 2){
                if(parts[1].split("\\.").length > 1){
                    email.setError(null);
                    return 1;
                }
            }
            email.setError(getString(R.string.create_developer_invalid_mail_cz));
            return 0;
        }
        email.setError(getString(R.string.create_developer_fill_in_email_cz));
        return 0;
    }

    /**
     * Checks if config name is OK, if it is duplicates, suggests non-duplicate one.
     * @return
     */
    private int configNameOK(){
        String name = configName.getEditText().getText().toString().trim().toLowerCase().replaceAll("\\.","");
        List<String> takenNames = SelectConfiguration.takenConfNames(CC);
        if(!takenNames.contains(name)){
           configName.setError(null);
           return 1;
        }
        else{
            configName.setError("Name " + name + " is taken. Try this one.");
            int tmp_help = 1;
            while(takenNames.contains(name + tmp_help)){
                tmp_help++;
            }
            configName.getEditText().setText(name + tmp_help);
            return 0;
        }

    }

    /**
     * @return a name of configuration's author
     */
    public String getAuthorName(){
        return author.getEditText().getText().toString();
    }

    /**
     * @return an author's email
     */
    public String getAuthorEmail(){
        return email.getEditText().getText().toString();
    }

    /**
     * @return configurations name
     */
    public String getConfigurationName(){
        return configName.getEditText().getText().toString();
    }

}
