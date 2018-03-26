package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by OWNE on 3/26/2018.
 */

public class GroupSelectAdminFragment extends Fragment {

    private String coursename;
    private String groupname;
    private DatabaseReference dbref;
    private FirebaseDatabase database;
    private LinearLayout group_ListStudents_admin;
    private LinearLayout.LayoutParams group_ListStudent_param;
    private Query query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }

        coursename = getArguments().getString("coursename");
        groupname = getArguments().getString("groupname");
        return inflater.inflate(R.layout.group_select_admin, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {


        group_ListStudents_admin = view.findViewById(R.id.group_ListStudents_admin);
        group_ListStudent_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView groupName = view.findViewById(R.id.group_name_admin);
        groupName.setText(groupname);



        Button group_Add_Student = view.findViewById(R.id.group_Add_Student);
        group_Add_Student.setText("Add Student");
        Button group_Remove_Student = view.findViewById(R.id.group_Remove_Student);
        group_Remove_Student.setText("Remove Student");
        Button group_Delete_Group = view.findViewById(R.id.group_Delete_group);

        //add student
        group_Add_Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //add here

            }
        });

        //remove student
        group_Remove_Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //delete here


            }

        });

        //delete group
        group_Delete_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask for user confirmation
                android.support.v7.app.AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new android.support.v7.app.AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                }
                builder.setTitle("Delete group")
                        .setMessage("Are you sure you want to delete this group?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

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

        final View backBtn = view.findViewById(R.id.group_admin_backbtn);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //handle click
                    Fragment newFragment = new CoursesFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    transaction.replace(R.id.group_select_adminFrame, newFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();


                }
            });


    }

    //updates the list every add or remove
    public void updateList(){



    }


}
