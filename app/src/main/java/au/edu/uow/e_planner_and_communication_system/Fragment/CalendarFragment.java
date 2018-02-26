package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by Tony on 19/2/2018.
 */

public class CalendarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.activity_calendar, container, false);
    }
}
