package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import au.edu.uow.e_planner_and_communication_system.Activity.individualChatActivity;
import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Athens on 2018/03/20.
 */

public class AllMessagesFragment extends Fragment {

    private RecyclerView myChatList;

    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerOptions<allMessagesDisplay> firebaseOptions;
    private FirebaseRecyclerAdapter<allMessagesDisplay, allMessagesDisplayViewHolder> firebaseRecyclerAdapter;

    private String onlineUserID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.all_messages_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Recycled XML file ->> Design
        myChatList = (RecyclerView) view.findViewById(R.id.all_messages_chat_list);
        myChatList.setHasFixedSize(true);
        myChatList.setLayoutManager(new LinearLayoutManager(getContext()));

        //Getting the current user's unique ID in the context of Firebase
        mAuth = FirebaseAuth.getInstance();
        onlineUserID = mAuth.getCurrentUser().getUid();

        //Current User Messages
        userReference = FirebaseDatabase.getInstance().getReference().child("Messengers-Linked").child(onlineUserID);

        //
        // ->A Database reference point based on the displayMessageUserID
        // ->It grabs the details of those that the current user has chatted with
        // ->Need to display the chat
        //




        //Options needed for the FirebaseRecylerAdpater
        firebaseOptions = new FirebaseRecyclerOptions.Builder<allMessagesDisplay>().
                setQuery(userReference, allMessagesDisplay.class).build();

        //Firebase Recycler Adapter
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<allMessagesDisplay, allMessagesDisplayViewHolder>(firebaseOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull allMessagesDisplayViewHolder holder, final int position, @NonNull allMessagesDisplay model) {

                        //Binding the object
                        holder.setName(model.getName());
                        holder.setUser_status(model.getUser_status());
                        holder.setThumb_images(model.getUser_thumb_image());

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Gets the unique when selected

                                DatabaseReference getNameRef = FirebaseDatabase.getInstance().getReference().child("Users");

                                getNameRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String visit_profile_id = getRef(position).getKey();
                                        String name = dataSnapshot.child(visit_profile_id).child("name").getValue().toString();
                                        Intent individualChat = new Intent(getContext(),individualChatActivity.class);
                                        individualChat.putExtra("visit_profile_id",visit_profile_id);
                                        individualChat.putExtra("name",name);
                                        individualChat.putExtra("user_status",dataSnapshot.child(visit_profile_id).child("user_status").getValue().toString());
                                        individualChat.putExtra("user_thumb_image",dataSnapshot.child(visit_profile_id).child("user_thumb_image").getValue().toString());
                                        startActivity(individualChat);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });




                            }
                        });


                    }

                    @NonNull
                    @Override
                    public AllMessagesFragment.allMessagesDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_users_contacts_layout, parent, false);
                        return new allMessagesDisplayViewHolder(view1);
                    }
                };

        //Setting the recycler
        myChatList.setAdapter(firebaseRecyclerAdapter);

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


    //View Holder Class used for the Recyclers
    public static class allMessagesDisplayViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public allMessagesDisplayViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView name_TextView = (TextView) mView.findViewById(R.id.all_users_username);
            name_TextView.setText(name);
        }

        // ->Can be changed to previous message instead of status
        // ->Be seen as a type of preview
        public void setUser_status(String user_status) {
            TextView status = (TextView) mView.findViewById(R.id.all_users_status);
            status.setText(user_status);
        }

        public void setThumb_images(String user_thumb_image) {

            CircleImageView thumb_image = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);
            Picasso.get().load(user_thumb_image).placeholder(R.drawable.default_image_profile).into(thumb_image);

        }

    }


}
