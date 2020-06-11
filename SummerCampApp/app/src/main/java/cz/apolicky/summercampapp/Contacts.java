package cz.apolicky.summercampapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A fragment of Main Activity where contacts can be dialed from.
 */
public class Contacts extends Fragment {

    /**
     * A reference to ConfigurationManager
     */
    private ConfigurationManager CM;

    /**
     * List view with contacts.
     */
    private ListView contactListView;

    /**
     * Constructor.
     * @param CM_ a reference to ConfigurationManager application uses.
     */
    public Contacts(ConfigurationManager CM_){
        CM = CM_;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contacts, container, false);

        contactListView = view.findViewById(R.id.contacts_list_view);

        refreshContactListView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshContactListView();
    }

    /**
     * Populates contactListView with contents of 'contacts' field in resource file
     */
    private void refreshContactListView(){
        ContactsAdapter ca = new ContactsAdapter(getContext(), CM.getValueMap("contacts"));
        contactListView.setAdapter(ca);
    }
}
