package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by Tony on 19/2/2018.
 */

public class GroupEventsFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private String groupkey;
    private RecyclerView allEventsList;
    private DatabaseReference allDatabaseEventReference;
    private FirebaseRecyclerOptions<allEvents> options ;
    private FirebaseRecyclerAdapter<allEvents, allEventsViewHolder> firebaseRecyclerAdapter;
    private FirebaseDatabase mDatabse;
    private String groupname;
    private String coursename;
    private Query query;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }

        groupkey = getArguments().getString("groupkey");
        coursename = getArguments().getString("coursename");
        groupname = getArguments().getString("groupname");

        return inflater.inflate(R.layout.events, container, false);


    }

    public void onViewCreated(View view, Bundle savedInstanceState) {





        mDatabse = FirebaseDatabase.getInstance();

        //List view
        allEventsList = (RecyclerView) view.findViewById(R.id.all_events_list);
        allEventsList.setHasFixedSize(true);
        //must set the layout
        allEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        //grab all the events
        allDatabaseEventReference = mDatabse.getReference().child("GroupEvents").child(groupkey);

        //Options needed for the firebaserecyleadpater/list
        options = new FirebaseRecyclerOptions.Builder<allEvents>().
                setQuery(allDatabaseEventReference,allEvents.class).build();



        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<allEvents, allEventsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull allEventsViewHolder holder, int position, @NonNull allEvents model) {
                        //Binding the object
                        holder.setDate(model.getDate());
                        holder.setEvent_name(model.getEvent_name());
                    }

                    @NonNull
                    @Override
                    public allEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_events_layout,parent,false);
                        return new allEventsViewHolder(view1);
                    }
                };


        allEventsList.setAdapter(firebaseRecyclerAdapter);

        //back button
        View backBtn = view.findViewById(R.id.eventsBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment newFragment = new GroupCalendarFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                Bundle args = new Bundle();
                args.putString("coursename",coursename);
                args.putString("groupname",groupname);
                newFragment.setArguments(args);

                transaction.replace(R.id.eventsFrame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();


            }
        });
        //end back button


    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }


    public class allEventsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public allEventsViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setDate(String date){
            TextView date_TextView = (TextView) mView.findViewById(R.id.all_events_date);
            date_TextView.setText(date);
        }

        public void setEvent_name(String event_name)
        {
            final Button name_ButtonView = mView.findViewById(R.id.Event_nameBtn);
            name_ButtonView.setText(event_name);

            name_ButtonView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    allDatabaseEventReference = mDatabse.getReference().child("Groups").child(coursename);

                    query = allDatabaseEventReference.orderByChild("groupname").equalTo(groupname);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                String groupkey = "";
                                groupkey = issue.getKey().toString();


                    //handle click
                    Fragment newFragment = new GroupEventDetailsFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("eventowner",groupkey);
                    args.putString("eventname",name_ButtonView.getText().toString());
                    args.putString("coursename",coursename);
                    args.putString("groupname",groupname);
                    newFragment.setArguments(args);

                    transaction.replace(R.id.eventsFrame, newFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //end grabbing group key

                }
            });


        }

    }


}
