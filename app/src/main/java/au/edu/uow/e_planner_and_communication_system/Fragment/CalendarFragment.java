package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by Tony on 19/2/2018.
 */

public class CalendarFragment extends Fragment {

    //text to appear in button
    private String m_Text = "";
    //this is for the date (Current date, selected date)
    private String dateVar = "";
    private RecyclerView eventsList;
    private DatabaseReference dbref;
    private FirebaseRecyclerOptions<allEvents> options;
    private FirebaseRecyclerAdapter<allEvents, CalendarViewHolder> firebaseRecyclerAdapter;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private String curruser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_calendar, container, false);


    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {

        //set up the vars

        //current user
        curruser = firebaseAuth.getInstance().getCurrentUser().getUid();
        eventsList = view.findViewById(R.id.eventsList);
        eventsList.setHasFixedSize(true);
        //set layout
        eventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        //grab relevant events



        final ViewGroup.LayoutParams eventsListParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final CompactCalendarView compactCalendar = view.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        dateVar = df.format(new Date());


        final TextView showdate = view.findViewById(R.id.showdate);
        showdate.setText(new SimpleDateFormat("MM-yyyy").format(compactCalendar.getFirstDayOfCurrentMonth()));

        database = FirebaseDatabase.getInstance();
        dbref = database.getReference().child("Events").child(curruser);

        //TESTPURPOSESONLY
        /*
        try {
            long epoch2 = df.parse("25-03-2018").getTime();
            Toast.makeText(getContext(), Long.toString(epoch2), Toast.LENGTH_LONG).show();
            Event evtest = new Event(Color.RED,epoch2,"TEST");
            compactCalendar.addEvent(evtest);
        } catch (ParseException e) {
            //
        }
        */

        //populate calendar
        dbref.orderByChild("date").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                allEvents allev = dataSnapshot.getValue(allEvents.class);
                try {
                    long epoch = df.parse(allev.getDate()).getTime();
                Event ev1 = new Event(Color.RED, epoch, allev.getEvent_name());
                compactCalendar.addEvent(ev1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //end populate calendar


        options = new FirebaseRecyclerOptions.Builder<allEvents>().
                setQuery(dbref.orderByChild("date"),allEvents.class).build();


        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<allEvents, CalendarViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CalendarViewHolder holder, int position, @NonNull allEvents model) {
                        //Binding the object
                        holder.setDate(model.getDate());
                        holder.setEvent_name(model.getEvent_name());

                    }

                    @NonNull
                    @Override
                    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_events_layout,parent,false);
                        return new CalendarViewHolder(view1);
                    }
                };

        eventsList.setAdapter(firebaseRecyclerAdapter);
        //end fetching today's list

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                dateVar = new SimpleDateFormat("dd-MM-yyyy").format(dateClicked);



        }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                showdate.setText(new SimpleDateFormat("MM-yyyy").format(firstDayOfNewMonth));
            }




        });


        //ADD EVENT
        View addEventBtn = view.findViewById(R.id.addEventBtn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                System.out.println("Test");

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add event: ");

                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        m_Text = input.getText().toString();

                        //create a new unique event ID
                        String uid = UUID.randomUUID().toString();

                        //get user input
                        Map<String,Object> addToDatabase = new HashMap<>();
                        addToDatabase.put("date",dateVar.toString() );
                        addToDatabase.put("event_name",m_Text);
                        addToDatabase.put("event_description","");

                        //push to database
                        dbref.child(uid).updateChildren(addToDatabase);

                        //escape to eventdetails
                        Fragment newFragment = new EventDetailsFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        Bundle args = new Bundle();
                        args.putString("eventowner",curruser);
                        args.putString("eventname",m_Text.toString());
                        newFragment.setArguments(args);

                        transaction.replace(R.id.calendarFrame, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }

        });
        //Add event end


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


        public class CalendarViewHolder extends RecyclerView.ViewHolder {
            View mView;

            public CalendarViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
            }

            public void setDate(String date) {
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
                        //handle click
                        Fragment newFragment = new EventDetailsFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        Bundle args = new Bundle();
                        args.putString("eventowner",curruser);
                        args.putString("eventname",name_ButtonView.getText().toString());
                        newFragment.setArguments(args);

                        transaction.replace(R.id.calendarFrame, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();


                    }
                });


            }
        }

    }









