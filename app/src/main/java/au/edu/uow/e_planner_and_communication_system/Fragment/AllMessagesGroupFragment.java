package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import au.edu.uow.e_planner_and_communication_system.Activity.groupChatActivity;
import au.edu.uow.e_planner_and_communication_system.Activity.individualChatActivity;
import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class AllMessagesGroupFragment extends Fragment {
    private RecyclerView myChatList;

    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerOptions<allMessagesGroupDisplay> firebaseOptions;
    private FirebaseRecyclerAdapter<allMessagesGroupDisplay, AllMessagesGroupFragment.allMessagesGroupDisplayViewHolder> firebaseRecyclerAdapter;

    private String onlineUserID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_all_messages_group, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Recycled XML file ->> Design
        myChatList = (RecyclerView) view.findViewById(R.id.fragment_all_messages_list);
        myChatList.setHasFixedSize(true);
        myChatList.setLayoutManager(new LinearLayoutManager(getContext()));

        //Getting the current user's unique ID in the context of Firebase
        mAuth = FirebaseAuth.getInstance();
        onlineUserID = mAuth.getCurrentUser().getUid();

        //Current User Messages
        userReference = FirebaseDatabase.getInstance().getReference().child("Messengers_Groups-Linked").child(onlineUserID);

        //
        // ->A Database reference point based on the displayMessageUserID
        // ->It grabs the details of those that the current user has chatted with
        // ->Need to display the chat
        //




        //Options needed for the FirebaseRecylerAdpater
        firebaseOptions = new FirebaseRecyclerOptions.Builder<allMessagesGroupDisplay>().
                setQuery(userReference, allMessagesGroupDisplay.class).build();

        //Firebase Recycler Adapter
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<allMessagesGroupDisplay, allMessagesGroupDisplayViewHolder>(firebaseOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull allMessagesGroupDisplayViewHolder holder, final int position, @NonNull allMessagesGroupDisplay model) {
                        //Binding the object
                        holder.setName(model.getName());
                        //holder.setUser_status(model.getUser_status());
                        //holder.setThumb_images(model.getUser_thumb_image());

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Gets the unique when selected

                                DatabaseReference getNameRef = FirebaseDatabase.getInstance().getReference().child("Groups");

                                getNameRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String visit_profile_id = getRef(position).getKey();
                                        String id = dataSnapshot.child(visit_profile_id).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString();
                                        String name = dataSnapshot.child(visit_profile_id).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString();
                                        Intent groupChat = new Intent(getContext(),groupChatActivity.class);
                                        groupChat.putExtra("visit_profile_id",id);
                                        groupChat.putExtra("name",name);
                                        startActivity(groupChat);
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
                    public AllMessagesGroupFragment.allMessagesGroupDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_users_contacts_layout, parent, false);
                        return new AllMessagesGroupFragment.allMessagesGroupDisplayViewHolder(view1);
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
    public static class allMessagesGroupDisplayViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public allMessagesGroupDisplayViewHolder(View itemView) {
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
