package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Manish on 28/03/2018.
 */

public class MessagesGroupAdpater extends RecyclerView.Adapter<MessagesGroupAdpater.MessagesGroupViewHolder> {

    private List<MessagesGroup> userGroupsMessagesList;

    private FirebaseAuth mAuth;

    public MessagesGroupAdpater (List<MessagesGroup> userMessagesList)
    {
        this.userGroupsMessagesList = userGroupsMessagesList;

    }

    public MessagesGroupAdpater.MessagesGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View V = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_messages_layout_of_users,parent,false);

        mAuth = FirebaseAuth.getInstance();

        return new MessagesGroupAdpater.MessagesGroupViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesGroupAdpater.MessagesGroupViewHolder holder, int position)
    {
        String message_sender_id = mAuth.getCurrentUser().getUid();

        MessagesGroup messagesGroup = userGroupsMessagesList.get(position);

        String fromUserID = messagesGroup.getFrom();

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

        holder.messageTextView.setText(messagesGroup.getMessage());
    }

    @Override
    public int getItemCount() {
        return userGroupsMessagesList.size();
    }

    public class MessagesGroupViewHolder extends RecyclerView.ViewHolder
    {
        public TextView messageTextView;
        public TextView userNameTextView;
        public CircleImageView userProfileImage;

        public MessagesGroupViewHolder(View view)
        {
            super(view);

            messageTextView = (TextView) view.findViewById(R.id.group_messages_chat_show);
            userNameTextView = (TextView) view.findViewById(R.id.group_messages_user_name_display);

            // userProfileImage = (CircleImageView) view.findViewById(R.id.messages_profile_picture);
        }
    }

}
