package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by OWNE on 3/27/2018.
 */

public class studentGroupFragment extends Fragment {

    private String coursename;
    private String groupname;
    private RecyclerView group_ListStudent;
    private FirebaseRecyclerOptions<GroupStudentListModel> options;
    private FirebaseRecyclerAdapter<GroupStudentListModel, GroupListModelViewHolder>
            firebaseRecyclerAdapter;
    private DatabaseReference dbref;
    private FirebaseDatabase database;
    private Query query;
    private String groupkey;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }

        coursename = getArguments().getString("coursename");
        groupname = getArguments().getString("groupname");

        return inflater.inflate(R.layout.studentgroup, container, false);


    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView studentGroupName = view.findViewById(R.id.studentgroupTextView);
        studentGroupName.setText(groupname);


        //start database for queries
        database = FirebaseDatabase.getInstance();
        //set reference to Groups > COURSENAME
        dbref = database.getReference().child("GroupsLists").child(coursename);

        group_ListStudent = view.findViewById(R.id.studentGroupListRecycler);
        group_ListStudent.setHasFixedSize(true);
        group_ListStudent.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<GroupStudentListModel>().
                setQuery(dbref.orderByChild("isMemberOf").equalTo(groupname), GroupStudentListModel.class).build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<GroupStudentListModel, GroupListModelViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull GroupListModelViewHolder holder, int position, @NonNull GroupStudentListModel model) {
                        holder.setFullname(model.getFullname());
                        holder.setSid(model.getSid());
                    }


                    @NonNull
                    @Override
                    public GroupListModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_studentlist, parent, false);
                        return new GroupListModelViewHolder(view1);
                    }
                };
        group_ListStudent.setAdapter(firebaseRecyclerAdapter);


        //calendar
        TextView groupCalendar = view.findViewById(R.id.groupCalendarTextView);
        groupCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle click
                Fragment newFragment = new GroupCalendarFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                Bundle args = new Bundle();
                args.putString("coursename", coursename);
                args.putString("groupname", groupname);
                newFragment.setArguments(args);

                transaction.replace(R.id.studentGroupFrame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();

            }
        });

        //events
        TextView groupEvents = view.findViewById(R.id.groupEventsTextView);
        groupEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle click

                //get key
                dbref = database.getReference().child("Groups").child(coursename);


                query = dbref.orderByChild("groupname").equalTo(groupname);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            groupkey = issue.getKey().toString();

                Fragment newFragment = new GroupEventsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                Bundle args = new Bundle();
                args.putString("coursename",coursename);
                args.putString("groupname",groupname);
                args.putString("groupkey",groupkey);
                newFragment.setArguments(args);

                transaction.replace(R.id.studentGroupFrame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        //chat
        TextView groupChat = view.findViewById(R.id.groupChat2TextView);
        groupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //backbtn
        View backBtn = view.findViewById(R.id.studentGroupBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle click


                Fragment newFragment = new CourseSelectFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                Bundle args = new Bundle();
                args.putString("coursename",coursename);

                newFragment.setArguments(args);

                transaction.replace(R.id.studentGroupFrame, newFragment);
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


    public class GroupListModelViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public GroupListModelViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setSid(String sid) {
            TextView sidTextView = mView.findViewById(R.id.sidTextView);
            sidTextView.setText(sid);
        }

        public void setFullname(String fullname) {
            TextView fullnameTextView = mView.findViewById(R.id.fullnameTextView);
            fullnameTextView.setText(fullname);
        }

    }


}
