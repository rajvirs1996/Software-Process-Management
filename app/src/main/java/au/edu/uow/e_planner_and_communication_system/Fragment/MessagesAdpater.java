package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.app.Notification;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Manish on 20/03/2018.
 */

public class MessagesAdpater extends  RecyclerView.Adapter<MessagesAdpater.MessageViewHolder> {

    private List<Messages> userMessagesList;

    private FirebaseAuth mAuth;

    private DatabaseReference usersDatabaseReference;

    public MessagesAdpater (List<Messages> userMessagesList)
    {
        this.userMessagesList = userMessagesList;

    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View V = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_layout_of_users,parent,false);

        mAuth = FirebaseAuth.getInstance();

    return new MessageViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position)
    {
        String message_sender_id = mAuth.getCurrentUser().getUid();

        Messages message = userMessagesList.get(position);

        String fromUserID = message.getFrom();

        String fromMessagetype = message.getType();

        usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);
        usersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String userName = dataSnapshot.child("name").getValue().toString();
                String userImage = dataSnapshot.child("user_thumb_image").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(fromMessagetype.equals("text")){

            holder.messagePicture.setVisibility(View.INVISIBLE);

            if(fromUserID.equals(message_sender_id))
            {
                holder.messageTextView.setBackgroundResource(R.drawable.message_text_background_two);
                holder.messageTextView.setTextColor(Color.BLACK);
                holder.messageTextView.setGravity(Gravity.RIGHT);
            }

            else
            {
                holder.messageTextView.setBackgroundResource(R.drawable.message_text_background);
                holder.messageTextView.setTextColor(Color.BLACK);
                holder.messageTextView.setGravity(Gravity.LEFT);

            }

            holder.messageTextView.setText(message.getMessage());
        } else
            {
                holder.messageTextView.setVisibility(View.INVISIBLE);
                holder.messageTextView.setPadding(0,0,0,0);

                if(fromUserID.equals(message_sender_id))
                {
                    holder.messageTextView.setGravity(Gravity.RIGHT);
                    holder.messagePicture.setForegroundGravity(Gravity.RIGHT);
                    Picasso.get().load(message.getMessage()).placeholder(R.drawable.default_image_profile).into(holder.messagePicture);
                } else
                    {
                        holder.messageTextView.setGravity(Gravity.LEFT);
                        holder.messagePicture.setForegroundGravity(Gravity.LEFT);
                        Picasso.get().load(message.getMessage()).placeholder(R.drawable.default_image_profile).into(holder.messagePicture);
                    }

                //holder.messagePicture.set

            }




    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView messageTextView;
        public TextView userNameTextView;
        public CircleImageView userProfileImage;
        public ImageView messagePicture;

        public MessageViewHolder(View view)
        {
            super(view);

            messageTextView = (TextView) view.findViewById(R.id.messages_chat_show);
            messagePicture = (ImageView) view.findViewById(R.id.message_imagge_view);
            // userProfileImage = (CircleImageView) view.findViewById(R.id.messages_profile_picture);
        }
    }
}
