package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by Tony on 19/2/2018.
 */

public class EventsFragment extends Fragment {

    private RecyclerView allEventsList;
    private DatabaseReference allDatabaseEventReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseRecyclerOptions<allEvents> options ;
    private FirebaseRecyclerAdapter<allEvents, allEventsViewHolder> firebaseRecyclerAdapter;
    private FirebaseDatabase mDatabse;
    private Query query;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.events, container, false);


    }

    public void onViewCreated(View view, Bundle savedInstanceState) {


        mDatabse = FirebaseDatabase.getInstance();
        Log.d("Success","We've passed this!");

        //List view
        allEventsList = (RecyclerView) view.findViewById(R.id.all_events_list);
        allEventsList.setHasFixedSize(true);
        //must set the layout
        allEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        //grab all the events
        allDatabaseEventReference = mDatabse.getReference().child("Events");

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


    public static class allEventsViewHolder extends RecyclerView.ViewHolder
    {
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
            TextView name_TextView = (TextView) mView.findViewById(R.id.all_events_title);
            name_TextView.setText(event_name);
        }

    }


}
