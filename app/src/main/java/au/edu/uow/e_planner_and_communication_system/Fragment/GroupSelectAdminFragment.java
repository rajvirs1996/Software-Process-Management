package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    private RecyclerView group_ListStudents_admin;
    private FirebaseRecyclerOptions<GroupStudentListModel> options;
    private FirebaseRecyclerAdapter<GroupStudentListModel, GroupStudentListModelViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference dbref;
    private FirebaseDatabase database;
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



        TextView groupName = view.findViewById(R.id.group_name_admin);
        groupName.setText(groupname);

        //start database for queries
        database = FirebaseDatabase.getInstance();
        //set reference to Groups > COURSENAME
        dbref = database.getReference().child("Groups").child(coursename);


        database.getReference().child("Group_Details").child(coursename).child("Group_UID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String grabUID =  dataSnapshot.getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        group_ListStudents_admin = view.findViewById(R.id.group_ListStudents_admin);
        group_ListStudents_admin.setHasFixedSize(true);
        group_ListStudents_admin.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<GroupStudentListModel>().
                setQuery(dbref.child("").equalTo(groupname),GroupStudentListModel.class).build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<GroupStudentListModel, GroupStudentListModelViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull GroupStudentListModelViewHolder holder, int position, @NonNull GroupStudentListModel model) {
                        holder.setFullname(model.getFullname());
                        holder.setSid(model.getSid());
                    }

                    @NonNull
                    @Override
                    public GroupStudentListModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_studentlist,parent,false);
                        return new GroupStudentListModelViewHolder(view1);
                    }
                };
        group_ListStudents_admin.setAdapter(firebaseRecyclerAdapter);

        Button group_Add_Student = view.findViewById(R.id.group_Add_Student);
        group_Add_Student.setText("Add Student");
        Button group_Remove_Student = view.findViewById(R.id.group_Remove_Student);
        group_Remove_Student.setText("Remove Student");
        Button group_Delete_Group = view.findViewById(R.id.group_Delete_group);

        //add student
        group_Add_Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add new student: ");

                // Set up the input
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText input1 = new EditText(getContext());
                input1.setHint("SID");
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                layout.addView(input1);

                final EditText input2 = new EditText(getContext());
                input2.setHint("Full name");
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                layout.addView(input2);

                builder.setView(layout);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //get user input
                        final Map<String,Object> addToDatabase = new HashMap<>();
                        addToDatabase.put("sid", input1.getText().toString() );
                        addToDatabase.put("fullname", input2.getText().toString());
                        addToDatabase.put("isMemberOf", groupname);

                        dbref.child(input1.getText().toString()).updateChildren(addToDatabase);


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

        //remove student
        group_Remove_Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete student: ");

                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        //get user input
                        query = dbref.orderByChild("isMemberOf").equalTo(groupname);

                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()){
                                    GroupStudentListModel temp = dataSnapshot.getValue(GroupStudentListModel.class);
                                    if (dataSnapshot.getKey().equals(input.getText().toString())){
                                        dataSnapshot.getRef().removeValue();
                                    }
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

                                    // FIND THE SPECIFIC KEY THROUGH QUERY
                                    dbref = database.getReference().child("Groups").child(coursename);
                                    query = dbref.orderByChild("groupname").equalTo(groupname);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {

                                                //get the values of the retrieved node
                                                for (DataSnapshot issue : dataSnapshot.getChildren()) {

                                                    //delete node (this points to the event child node)
                                                    issue.getRef().removeValue();

                                                    Bundle args = new Bundle();
                                                    args.putString("course", coursename);
                                                    //make a hasty retreat to grouplistfragment before everything crashes
                                                    Fragment newFragment = new GroupListFragment();
                                                    newFragment.setArguments(args);
                                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                                    transaction.replace(R.id.group_select_adminFrame, newFragment);
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


        //backbutton
        View backbtn = view.findViewById(R.id.group_admin_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //handle click
                Fragment newFragment = new CoursesFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.group_select_adminFrame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });


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


    public class GroupStudentListModelViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public GroupStudentListModelViewHolder(View itemView) {
            super (itemView);
            mView = itemView;
        }

        public void setSid (String sid) {
            TextView sidTextView = mView.findViewById(R.id.sidTextView);
            sidTextView.setText(sid);
        }

        public void setFullname (String fullname) {
            TextView fullnameTextView = mView.findViewById(R.id.fullnameTextView);
            fullnameTextView.setText(fullname);
        }

    }




}
