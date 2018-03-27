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
import android.widget.LinearLayout;
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
 * Created by Athens on 2/20/2018.
 */

public class CourseSelectFragment extends Fragment {

    private String coursename = "";
    private DatabaseReference reference;
    private Map<String,Object> updateDatabase;

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

        updateDatabase = new HashMap<String,Object>();
        reference = FirebaseDatabase.getInstance()
                .getReference().child("Courses").child(coursename);

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
        final View backBtn = view.findViewById(R.id.courseBackBtn);
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

        //editcourse
        View courseEditBtn = view.findViewById(R.id.editCourseBtn);
        courseEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Input course name and lecturer name: ");

                // Set up the input
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText input1 = new EditText(getContext());
                input1.setHint("Course Name");
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                layout.addView(input1);

                final EditText input2 = new EditText(getContext());
                input2.setHint("Lecturer Name");
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                layout.addView(input2);

                builder.setView(layout);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //get user input
                        coursename = input1.getText().toString();
                        updateDatabase.put("coursename", input1.getText().toString());
                        updateDatabase.put("coursehead", input2.getText().toString());
                        reference.updateChildren(updateDatabase);
                        courseName.setText(input1.getText().toString());


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

        //delete course
        View deleteCourseBtn = view.findViewById(R.id.deleteCourseBtn);
        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask for user confirmation
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Delete course")
                        .setMessage("Are you sure you want to delete this course?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                // FIND THE SPECIFIC KEY THROUGH QUERY
                                reference = FirebaseDatabase.getInstance().getReference().child("Courses");
                                Query query = reference.orderByChild("coursename").equalTo(coursename);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {

                                            //get the values of the retrieved node
                                            for (DataSnapshot issue : dataSnapshot.getChildren()){

                                                //delete node (this points to the courses child node)
                                                issue.getRef().removeValue();

                                                //make a hasty retreat to eventsfragment before everything crashes
                                                Fragment newFragment = new CoursesFragment();
                                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                                transaction.replace(R.id.course_SelectFrame, newFragment);
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


    }

}
