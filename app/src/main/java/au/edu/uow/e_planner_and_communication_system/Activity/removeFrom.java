package au.edu.uow.e_planner_and_communication_system.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import au.edu.uow.e_planner_and_communication_system.R;

public class removeFrom extends DialogFragment {

    private TextView choice;
    private TextView groupOrCourse;
    private TextView titleText;
    private TextView groupTextView;
    private Spinner firstSpinner;
    private Spinner secondSpinner;
    private Spinner thirdSpinner;
    private Button confirmButton;
    private Button cancelButton;

    private String selectedUserID;
    private String name;
    private String SID;
    private String email;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_add_to, null);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        choice = (TextView) view.findViewById(R.id.add_to_choice);
        groupOrCourse = (TextView) view.findViewById(R.id.add_to_towhere_text);
        titleText = (TextView) view.findViewById(R.id.add_to_title_text);
        groupTextView = (TextView) view.findViewById(R.id.add_to_gorup_text);

        firstSpinner = (Spinner) view.findViewById(R.id.add_to_spinner_1);
        secondSpinner = (Spinner) view.findViewById(R.id.add_to_spinner_2);
        thirdSpinner = (Spinner) view.findViewById(R.id.add_to_third_Spinner);

        confirmButton = (Button) view.findViewById(R.id.add_to_ok_button);
        cancelButton = (Button) view.findViewById(R.id.add_to_cancel_button);

        choice.setVisibility(TextView.INVISIBLE);
        groupTextView.setVisibility(TextView.INVISIBLE);
        secondSpinner.setVisibility(Spinner.INVISIBLE);
        thirdSpinner.setVisibility(Spinner.INVISIBLE);

        titleText.setText("Remove:");
        titleText.setVisibility(TextView.VISIBLE);

        String[] itemsSpinner1 = new String[]{"Please Select", "Course", "Group"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, itemsSpinner1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        firstSpinner.setAdapter(adapter1);
        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, final View view, int i, long l) {
                final View mView = view;

                switch (i) {
                    case 0:
                        choice.setVisibility(TextView.INVISIBLE);
                        groupTextView.setVisibility(TextView.INVISIBLE);
                        secondSpinner.setVisibility(Spinner.INVISIBLE);
                        thirdSpinner.setVisibility(Spinner.INVISIBLE);
                        break;
                    case 1:
                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                        reference.child("Courses_Student_List").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                final List<String> courseList = new ArrayList<String>();

                                for (DataSnapshot courseSnapshot: dataSnapshot.getChildren())
                                {
                                    String user = courseSnapshot.child(selectedUserID).getKey().toString();
                                    Toast.makeText(getActivity(),user,Toast.LENGTH_SHORT).show();
                                    if (user.equals(selectedUserID)) {
                                        courseList.add(courseSnapshot.getKey());
                                    }
                                }
                                ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, courseList);
                                courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                secondSpinner.setAdapter(courseAdapter);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        thirdSpinner.setVisibility(Spinner.INVISIBLE);
                        groupTextView.setVisibility(TextView.INVISIBLE);

                        choice.setText("Course");
                        choice.setVisibility(TextView.VISIBLE);
                        secondSpinner.setVisibility(Spinner.VISIBLE);


                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String course = secondSpinner.getSelectedItem().toString().trim();
                                Toast.makeText(getActivity(),course,Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(),selectedUserID,Toast.LENGTH_SHORT).show();
                                DatabaseReference courseRefSet =  FirebaseDatabase.getInstance().getReference().child("Courses_Student_List").child(course).child(selectedUserID);

                                courseRefSet.removeValue();

                                removeFrom.this.dismiss();
                            }
                        });

                        break;
                    case 2:

                        choice.setVisibility(TextView.VISIBLE);
                        groupTextView.setVisibility(TextView.INVISIBLE);

                        secondSpinner.setVisibility(Spinner.INVISIBLE);
                        thirdSpinner.setVisibility(Spinner.INVISIBLE);

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();

                        reference2.child("Courses_Student_List").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                final List<String> courseList = new ArrayList<String>();

                                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                                    String user = courseSnapshot.child(selectedUserID).getKey().toString();

                                    if (user.equals(selectedUserID)) {
                                        courseList.add(courseSnapshot.getKey());
                                    }
                                }
                                ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, courseList);
                                courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                secondSpinner.setAdapter(courseAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        secondSpinner.setVisibility(Spinner.VISIBLE);




                        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String selectedChanged = adapterView.getItemAtPosition(i).toString().trim();
                                Toast.makeText(getActivity(),selectedChanged,Toast.LENGTH_SHORT).show();

                                FirebaseDatabase.getInstance().getReference().child("Groups").child(selectedChanged).
                                        addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                final List<String> groupNameList = new ArrayList<String>();


                                                for (DataSnapshot groupSnapShot : dataSnapshot.getChildren())
                                                {
                                                    for(DataSnapshot groupSnapShot2 : groupSnapShot.child("Group__List").getChildren()) {
                                                        String groupTest = groupSnapShot2.child(selectedUserID).getKey().toString();
                                                        if (groupTest.equals(selectedUserID)) {
                                                            groupNameList.add(groupSnapShot.child("info").child("groupname").getValue().toString());
                                                        }
                                                    }
                                                }
                                                ArrayAdapter groupAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,groupNameList);
                                                groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                                thirdSpinner.setAdapter(groupAdapter);

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        groupTextView.setVisibility(TextView.VISIBLE);
                        thirdSpinner.setVisibility(Spinner.VISIBLE);



                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String course = secondSpinner.getSelectedItem().toString().trim();
                                String group = thirdSpinner.getSelectedItem().toString().trim();

                                DatabaseReference getUIDRef = FirebaseDatabase.getInstance().getReference().child("Group_Details")
                                        .child(course).child(group).child("Group_UID");

                                getUIDRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String uid = dataSnapshot.getValue().toString();

                                        final DatabaseReference groupRef1 = FirebaseDatabase.getInstance().getReference()
                                                .child("Groups").child(course).child(uid).child("Group__List").child(selectedUserID);

                                        groupRef1.removeValue();
                                        removeFrom.this.dismiss();
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });






                            }
                        });

                        break;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void setSelectedUserID(String ID,String name, String SID , String email)
    {
        selectedUserID = ID;
        this.name = name;
        this.SID =SID;
        this.email = email;

    }
}
