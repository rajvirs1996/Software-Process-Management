package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.app.Notification;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Manish on 20/03/2018.
 */

public class MessagesAdpater extends  RecyclerView.Adapter<MessagesAdpater.MessageViewHolder> {

    private List<Messages> userMessagesList;

    public MessagesAdpater (List<Messages> userMessagesList)
    {
        this.userMessagesList = userMessagesList;

    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View V = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_layout_of_users,parent,false);

    return new MessageViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position)
    {
        Messages message = userMessagesList.get(position);

        holder.messageTextView.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView messageTextView;
        public CircleImageView userProfileImage;

        public MessageViewHolder(View view)
        {
            super(view);

            messageTextView = (TextView) view.findViewById(R.id.messages_chat_show);
            userProfileImage = (CircleImageView) view.findViewById(R.id.messages_profile_picture);
        }
    }
}
