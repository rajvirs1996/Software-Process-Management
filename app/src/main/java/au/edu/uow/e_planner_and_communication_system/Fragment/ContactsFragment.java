package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Athens on 2/20/2018.
 */

public class ContactsFragment extends Fragment {

    private RecyclerView allUsersList;
    private DatabaseReference allDatabaseUserReference;
    private FirebaseRecyclerOptions<allUsers> options ;
    private FirebaseRecyclerAdapter<allUsers, allUsersViewHolder> firebaseRecyclerAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabse;
    private Query query;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.contacts, container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState){

        mDatabse = FirebaseDatabase.getInstance();

        //List view
        allUsersList = (RecyclerView) view.findViewById(R.id.user_contact_list);
        allUsersList.setHasFixedSize(true);
        //must set the layout
        allUsersList.setLayoutManager(new LinearLayoutManager(getContext()));
        //grab all the users
        allDatabaseUserReference = mDatabse.getReference().child("Users");

        //Options needed for the firebaserecyleadpater/list
        options = new FirebaseRecyclerOptions.Builder<allUsers>().
                setQuery(allDatabaseUserReference,allUsers.class).build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<allUsers, allUsersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull allUsersViewHolder holder, int position, @NonNull allUsers model) {
                        //Binding the object
                        holder.setName(model.getName());
                        holder.setUser_status(model.getUser_status());
                    }

                    @NonNull
                    @Override
                    public allUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_users_contacts_layout,parent,false);
                        return new allUsersViewHolder(view1);
                    }
                };

        allUsersList.setAdapter(firebaseRecyclerAdapter);
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

    public static class allUsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public allUsersViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name)
        {
            TextView name_TextView = (TextView) mView.findViewById(R.id.all_users_username);
            name_TextView.setText(name);
        }

        public void setUser_status(String user_status){
            TextView status = (TextView) mView.findViewById(R.id.all_users_status);
            status.setText(user_status);
        }

    }
}
