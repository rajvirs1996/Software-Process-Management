package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by Tony on 19/2/2018.
 */

public class CalendarFragment extends Fragment {

    private String m_Text = "";
    private String fulldate = "";
    private String dateVar = "";
    private String showdate= "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_calendar, container, false);


    }

    public void setfulldate (int dayOfMonth, int month, int year){

        fulldate = Integer.toString(dayOfMonth) + "/" + Integer.toString(month) + "/" + Integer.toString(year);

    }



    public void onViewCreated(View view, Bundle savedInstanceState) {

        final LinearLayout eventsList = view.findViewById(R.id.eventsList);
        final ViewGroup.LayoutParams eventsListParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        CompactCalendarView compactCalendar = view.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);




        final TextView showdate = view.findViewById(R.id.showdate);
        showdate.setText(new SimpleDateFormat("MM-yyyy").format(compactCalendar.getFirstDayOfCurrentMonth()));
        /*
        //get selected date start
        CalendarView calendar = view.findViewById(R.id.calendarView);



        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                setfulldate(dayOfMonth,month,year);
            }
        });
        //get selected date end
    */

        //Add Event start
        View addEventBtn = view.findViewById(R.id.addEventBtn);


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                dateVar = new SimpleDateFormat("dd-MM-yyyy").format(dateClicked);

                //TODO reiterate user's eventslist here (e.g. On day click: Show events of that day)
                //here

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                showdate.setText(new SimpleDateFormat("MM-yyyy").format(firstDayOfNewMonth));
            }
        });

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

                        //create event
                        Button newBtn = new Button(getContext());
                        newBtn.setText(dateVar + ":" + m_Text);
                        eventsList.addView(newBtn, eventsListParam);
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
    }


    }






