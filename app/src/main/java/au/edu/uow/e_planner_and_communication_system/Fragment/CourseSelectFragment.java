package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        if (container != null) {
            container.removeAllViews();
        }
        coursename = getArguments().getString("coursename");

        return inflater.inflate(R.layout.course_select, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        final TextView courseName = view.findViewById(R.id.courseName);
        courseName.setText(coursename);

        //Thus is for listofGroups
        final TextView listOfGroups = view.findViewById(R.id.listofGroupsText);
        listOfGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //handle click
                Fragment newFragment = new GroupListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                Bundle args = new Bundle();
                args.putString("course", coursename);
                newFragment.setArguments(args);

                transaction.replace(R.id.course_SelectFrame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();


            }
        });

        //BACK BUTTON
        //GOES BACK TO COURSES
        final Button backBtn = view.findViewById(R.id.courseBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle click
                Fragment newFragment = new CoursesFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.course_SelectFrame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();


            }
        });


    }

}
