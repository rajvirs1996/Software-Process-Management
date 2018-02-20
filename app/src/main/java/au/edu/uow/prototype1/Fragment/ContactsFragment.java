package au.edu.uow.prototype1.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import au.edu.uow.prototype1.R;

/**
 * Created by Athens on 2/20/2018.
 */

public class ContactsFragment extends Fragment {

    boolean hasUserAlready = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.contacts, container, false);


    }
    public void onViewCreated(View view, Bundle savedInstanceState) {

        final LinearLayout contactList = view.findViewById(R.id.contactslayout);
        final ViewGroup.LayoutParams contactsParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView inText = view.findViewById(R.id.contactsIn);
        View addBtn = view.findViewById(R.id.contactsAddBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String a = inText.getText().toString();
                //on click off 'Add Contact Button'

                //add contact
                if (!hasUserAlready && a.equals("Test123")) {
                    Button newBtn = new Button(getContext());
                    newBtn.setText("Test123");
                    contactList.addView(newBtn,contactsParam);


                    //newBtn.setOnClickListener(new Button.OnClickListener() {

                        //open chat with user

                    //});

                    inText.setText("");
                    hasUserAlready = true;
                }

                //add user but user already added
                else if (hasUserAlready && a.equals("Test123")) {
                    String b = "Already added!";
                    Toast.makeText(getContext(), b, Toast.LENGTH_LONG).show();
                    inText.setText("");
                }

                //wrong username
                else {
                    String b = "User not found!";
                    Toast.makeText(getContext(), b, Toast.LENGTH_LONG).show();
                    inText.setText("");
                }
            }

        });








    }

}
