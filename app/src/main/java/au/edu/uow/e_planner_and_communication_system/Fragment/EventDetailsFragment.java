package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by Athens on 2018/03/16.
 */

public class EventDetailsFragment extends Fragment {

    private String eventNameFromEvents = "";
    private String eventOwnerFromEvents = "";
    private DatabaseReference reference;
    private Query query;
    private Map<String,Object> updateDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }
        updateDatabase = new HashMap<String,Object>();
        eventNameFromEvents = getArguments().getString("eventname");
        eventOwnerFromEvents = getArguments().getString("eventowner");

        //inflater
        return inflater.inflate(R.layout.eventdetails, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        final TextView eventTitle = view.findViewById(R.id.eventTitle);
        eventTitle.setText(eventNameFromEvents);
        final EditText event_title = view.findViewById(R.id.event_title);
        final TextView event_date = view.findViewById(R.id.event_date);
        final EditText event_description = view.findViewById(R.id.event_description);

        event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Change date: ");

                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        event_date.setText(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


        //Add event end
            }
        });

        //initialize the start
        reference = FirebaseDatabase.getInstance()
                .getReference().child("Events").child(eventOwnerFromEvents);

        //gets the node that has the key
        query = reference.orderByChild("event_name").equalTo(eventNameFromEvents);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    //get the values of the retrieved node
                    for (DataSnapshot issue : dataSnapshot.getChildren()){
                    allEvents selectedevent = issue.getValue(allEvents.class);

                    event_title.setText(selectedevent.getEvent_name());
                    event_date.setText(selectedevent.getDate());
                    event_description.setText(selectedevent.getEvent_description());
                }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //SAVE FUNCTION
        Button saveButton = view.findViewById(R.id.saveEventDetailsBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //put the needed data into hashtable
                // .put (KEY, THE NEW VALUES)
                updateDatabase.put("event_date", event_date.getText().toString());
                updateDatabase.put("event_name",event_title.getText().toString());
                updateDatabase.put("event_description", event_description
                .getText().toString());

                // FIND THE SPECIFIC KEY THROUGH QUERY
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            //get the values of the retrieved node
                            for (DataSnapshot issue : dataSnapshot.getChildren()){

                                //update child using datasnapshot
                                issue.getRef().updateChildren(updateDatabase);
                                eventNameFromEvents = event_title.getText().toString();
                                eventTitle.setText(eventNameFromEvents);
                                Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }



        });
        //SAVE END

        //BACK BUTTON
        Button backButton = view.findViewById(R.id.backEventDetailsBtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new EventsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.eventdetailsFrame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        //BACK BUTTON END


        // DELETE FUNCTION
        View deleteBtn = view.findViewById(R.id.deleteEventBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Ask for user confirmation
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Delete event")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                // FIND THE SPECIFIC KEY THROUGH QUERY
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {

                                            //get the values of the retrieved node
                                            for (DataSnapshot issue : dataSnapshot.getChildren()){

                                                //delete node (this points to the event child node)
                                                issue.getRef().removeValue();

                                                //make a hasty retreat to eventsfragment before everything crashes
                                                Fragment newFragment = new EventsFragment();
                                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                                transaction.replace(R.id.eventdetailsFrame, newFragment);
                                                transaction.addToBackStack(null);

                                                transaction.commit();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                //end query
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                //end of confirmation
            }
        });


        //DELETE END
    }



}
