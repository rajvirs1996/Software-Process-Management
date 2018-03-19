package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by Athens on 2/20/2018.
 */

public class CourseSelectFragment extends Fragment {

    private String coursename = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(container != null) {
            container.removeAllViews();
        }
        coursename = getArguments().getString("coursename");

        return inflater.inflate(R.layout.course_select, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        final TextView courseName = view.findViewById(R.id.courseName);
        courseName.setText(coursename);

    }

}
