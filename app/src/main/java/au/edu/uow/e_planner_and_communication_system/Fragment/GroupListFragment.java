package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by OWNE on 3/26/2018.
 */

public class GroupListFragment extends Fragment {

    private String course = "";
    private RecyclerView listofGroupsRecycler;
    private DatabaseReference dbref;
    private FirebaseRecyclerOptions<GroupModel> options;
    private FirebaseRecyclerAdapter<GroupModel, GroupModelViewHolder> firebaseRecyclerAdapter;
    private FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        course = getArguments().getString("course");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.group_list, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();

        listofGroupsRecycler = view.findViewById(R.id.listOfGroupsRecycler);
        listofGroupsRecycler.setHasFixedSize(true);
        listofGroupsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        dbref = database.getReference().child("Groups").child(course);

        options = new FirebaseRecyclerOptions.Builder<GroupModel>().
                setQuery(dbref,GroupModel.class).build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<GroupModel, GroupModelViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull GroupModelViewHolder holder, int position, @NonNull GroupModel model) {
                        holder.setGroupName(model.getGroupname());
                    }

                    @NonNull
                    @Override
                    public GroupModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_groups_list,parent,false);
                        return new GroupModelViewHolder(view1);
                    }
                };
        listofGroupsRecycler.setAdapter(firebaseRecyclerAdapter);

        //add new group
        View addNewGroupBtn = view.findViewById(R.id.addNewGroupBtn);
        addNewGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add new group: ");

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

                        String uid = UUID.randomUUID().toString();
                        Map<String,Object> addToDatabase = new HashMap<>();
                        addToDatabase.put("groupname", input.getText().toString() );
                        dbref.child(uid).child("info").updateChildren(addToDatabase);

                        DatabaseReference groupsDetailsRef = FirebaseDatabase.getInstance().getReference()
                                .child("Group_Details").child(course).child(input.getText().toString());

                        groupsDetailsRef.child("Group_UID").setValue(uid);


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

    public class GroupModelViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public GroupModelViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setGroupName(String groupname){
            final Button group_ButtonView = mView.findViewById(R.id.groupBtn);
            group_ButtonView.setText(groupname);

            group_ButtonView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //handle click
                    Fragment newFragment = new GroupSelectAdminFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("groupname",group_ButtonView.getText().toString());
                    args.putString("coursename", course);
                    newFragment.setArguments(args);

                    transaction.replace(R.id.groupListFrame, newFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();


                }
            });


        }
    }

}
