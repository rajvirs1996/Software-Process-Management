package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import au.edu.uow.e_planner_and_communication_system.R;

;

/**
 * Created by Tony on 19/2/2018.
 */

public class GroupCalendarFragment extends Fragment {

    //text to appear in button
    private String m_Text = "";
    //this is for the date (Current date, selected date)
    private String dateVar = "";
    private LinearLayout eventsList;
    private LinearLayout.LayoutParams eventsParam;
    private DatabaseReference dbref;
    private FirebaseDatabase database;
    private String groupname;
    private String coursename;
    private CompactCalendarView compactCalendar;
    private List<Event> listofEvents;
    private String groupkey;
    private Query query;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null ) {
            container.removeAllViews();
        }

        coursename = getArguments().getString("coursename");
        groupname = getArguments().getString("groupname");

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_calendar, container, false);


    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {

        //set up the vars



        eventsList = view.findViewById(R.id.eventsList);
        eventsParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //grab relevant events
        listofEvents = new ArrayList<Event>();

        compactCalendar = view.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        dateVar = df.format(new Date());


        final TextView showdate = view.findViewById(R.id.showdate);
        showdate.setText(new SimpleDateFormat("MM-yyyy").format(compactCalendar.getFirstDayOfCurrentMonth()));

        database = FirebaseDatabase.getInstance();

        //get holidays//
        dbref = database.getReference().child("Events").child("HongKong2018");

        dbref.orderByChild("date").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final allEvents allev = dataSnapshot.getValue(allEvents.class);
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


        //end getting holidays


        //get Group Key ID from database here

        dbref = database.getReference().child("Groups").child(coursename);

        query = dbref.orderByChild("groupname").equalTo(groupname);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot issue : dataSnapshot.getChildren()) {
                    groupkey = issue.getKey().toString();
                    setGroupkey(groupkey);




        dbref = database.getReference().child("GroupEvents").child(groupkey);
        //populate calendar
        dbref.orderByChild("date").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final allEvents allev = dataSnapshot.getValue(allEvents.class);
                try {
                    long epoch = df.parse(allev.getDate()).getTime();
                Event ev1 = new Event(Color.RED, epoch, allev.getEvent_name());
                compactCalendar.addEvent(ev1);
                setCalendar(compactCalendar);

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


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                dateVar = new SimpleDateFormat("dd-MM-yyyy").format(dateClicked);


                //fetching today's list
                List<Event> listofEvents = new ArrayList<Event>();


                try {
                    eventsList.removeAllViews();
                    long epoch1 = df.parse(dateVar).getTime();
                    listofEvents = compactCalendar.getEvents(epoch1);
                    for (int i = 0; i < listofEvents.size(); i++) {
                        Button newBtn = new Button(getContext());
                        Date date1 = new Date(listofEvents.get(i).getTimeInMillis());
                        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        String tempdate = format.format(date1);
                        final String tempname = listofEvents.get(i).getData().toString();
                        newBtn.setText(tempdate + " : " + tempname);

                        newBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dbref = database.getReference().child("Groups").child(coursename);

                                query = dbref.orderByChild("groupname").equalTo(groupname);

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
                                args.putString("eventname",tempname);
                                args.putString("coursename",coursename);
                                args.putString("groupname",groupname);
                                newFragment.setArguments(args);

                                transaction.replace(R.id.calendarFrame, newFragment);
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
                        eventsList.addView(newBtn,eventsParam);

                    }
                } catch (ParseException e) {


                }

                //end fetching today's list

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

                        dbref = database.getReference().child("Groups").child(coursename);

                        query = dbref.orderByChild("groupname").equalTo(groupname);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                    String groupkey = "";
                                    groupkey = issue.getKey().toString();
                        Fragment newFragment = new GroupEventDetailsFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        Bundle args = new Bundle();
                        args.putString("eventowner",groupkey);
                        args.putString("eventname",m_Text.toString());
                        args.putString("coursename",coursename);
                        args.putString("groupname",groupname);
                        newFragment.setArguments(args);

                        transaction.replace(R.id.calendarFrame, newFragment);
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

        //back button
        View backBtn = view.findViewById(R.id.calendarBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment newFragment = new studentGroupFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                Bundle args = new Bundle();
                args.putString("coursename",coursename);
                args.putString("groupname",groupname);
                newFragment.setArguments(args);

                transaction.replace(R.id.calendarFrame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();


            }
        });
        //end back button


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //end grabbing group key


    }

        public void setCalendar(CompactCalendarView calendar1){
        this.compactCalendar = calendar1;
        }


        public void setGroupkey(String groupkey) {
            this.groupkey = groupkey;
        }
    }









