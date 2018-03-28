package au.edu.uow.e_planner_and_communication_system.Activity;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.uow.e_planner_and_communication_system.Fragment.Messages;
import au.edu.uow.e_planner_and_communication_system.Fragment.MessagesGroup;
import au.edu.uow.e_planner_and_communication_system.Fragment.MessagesGroupAdpater;
import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class groupChatActivity extends AppCompatActivity {
    private String group_ID;
    private String group_Name;
    private String name_of_user;

    private Toolbar ChatToolBar;

    private TextView nameTitle;
    private TextView userLastSeen;
    private CircleImageView profileImageChat;

    private ImageButton sendMessgaeButton;
    private ImageButton selectImageToSend;
    private EditText inputtedMessae;

    private DatabaseReference rootRef;

    private FirebaseAuth mAuth;
    private String messageSenderID;

    private RecyclerView userMessagesList;

    private final List<MessagesGroup> messagesGroupsList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;

    private MessagesGroupAdpater messagesGroupAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        //Passed in
        group_ID =getIntent().getExtras().get("GID").toString();
        group_Name = getIntent().getExtras().get("groupName").toString();
        name_of_user = getIntent().getExtras().get("messengersName").toString();

        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();

        ChatToolBar = (Toolbar) findViewById(R.id.group_chatbar);
        setSupportActionBar(ChatToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar,null);
        //Connect Action bar to custom view
        actionBar.setCustomView(action_bar_view);


        //Casting
        nameTitle = (TextView) findViewById(R.id.custom_user_name);
        userLastSeen = (TextView) findViewById(R.id.custom_last_seen);
        profileImageChat = (CircleImageView) findViewById(R.id.custom_user_profile);
        sendMessgaeButton = (ImageButton) findViewById(R.id.group_chat_send_message_button);
        selectImageToSend = (ImageButton) findViewById(R.id.group_chat_select_image);
        inputtedMessae = (EditText) findViewById(R.id.group_chat_message_input);

        nameTitle.setText(group_Name);

        messagesGroupAdpater = new MessagesGroupAdpater(messagesGroupsList);
        userMessagesList = (RecyclerView) findViewById(R.id.chat_activity_list);


        linearLayoutManager = new LinearLayoutManager(this);

        userMessagesList.setHasFixedSize(true);

        userMessagesList.setLayoutManager(linearLayoutManager);

        userMessagesList.setAdapter(messagesGroupAdpater);

        FetchMessages();

        //Used for last seen
        /*rootRef.child("Users").child(ID_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        */

        sendMessgaeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessge();

            }
        });

    }

    private void FetchMessages()
    {
        rootRef.child("Group_Messages").child(messageSenderID).child(group_ID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {

                        MessagesGroup massagesGroup = dataSnapshot.getValue(MessagesGroup.class);

                        messagesGroupsList.add(massagesGroup);

                        messagesGroupAdpater.notifyDataSetChanged();
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

    private void sendMessge() {
        final String messageText = inputtedMessae.getText().toString();

        //Input Validation
        //Empty First
        if(TextUtils.isEmpty(messageText)){
            //Empty message
        }
        //If not empty,
        else{


            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(messageSenderID);



            ref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String messenger_sender_ref = "Messages/" + messageSenderID + "/" + group_ID;
                    String messenger_receiver_ref =  "Messages/" + group_ID +  "/" + messageSenderID;

                    //Create unique message key
                    //---------------------------------------------------------------------------------------//
                    DatabaseReference user_message_key = rootRef.child("Group_Messages").child(messageSenderID).
                            child(group_ID).push();


                    //---------------------------------------------------------------------------------------//

                    //---------------------------------------------------------------------------------------//
                    String message_push_id = user_message_key.getKey();

                    //---------------------------------------------------------------------------------------//

                    //---------------------------------------------------------------------------------------//

                    //---------------------------------------------------------------------------------------//

                    //---------------------------------------------------------------------------------------//

                    String messageToPass = messageText;

                    Map messageTextBody = new HashMap();
                    messageTextBody.put("message",messageToPass);
                    messageTextBody.put("seen",false);
                    messageTextBody.put("type","text");
                    messageTextBody.put("time", ServerValue.TIMESTAMP);
                    messageTextBody.put("from",messageSenderID);
                    messageTextBody.put("GID",group_ID);
                    messageTextBody.put("messengersName",dataSnapshot.child("name").getValue());



                    DatabaseReference usersConnected1 = FirebaseDatabase.getInstance().getReference().
                            child("Messengers_Groups-Linked").child(messageSenderID).child(group_ID);

                    DatabaseReference usersConnected2 = FirebaseDatabase.getInstance().getReference().
                            child("Messengers_Groups-Linked").child(group_ID).child(messageSenderID);

                    String name2 = mAuth.getCurrentUser().getDisplayName().toString();


                    String lastMessage = messageToPass;

                    usersConnected1.child("name").setValue(group_Name);
                    usersConnected1.child("last-message").setValue(messageToPass);
                    usersConnected1.child("time").setValue(ServerValue.TIMESTAMP);

                    usersConnected2.child("name").setValue(name2);
                    usersConnected2.child("last-message").setValue(messageToPass);
                    usersConnected2.child("time").setValue(ServerValue.TIMESTAMP);

                    //---------------------------------------------------------------------------------------//

                    //---------------------------------------------------------------------------------------//
                    //Sender
                    Map messageBodyDetails = new HashMap();
                    messageBodyDetails.put(messenger_sender_ref + "/" + message_push_id, messageTextBody);
                    //Receiver
                    messageBodyDetails.put(messenger_receiver_ref+"/"+message_push_id,messageTextBody);

                    //Store into database
                    rootRef.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
                        {
                            if(databaseError!=null)
                            {
                                Log.d("Chat_Log",databaseError.getMessage().toString());
                            }

                            //Clear
                            inputtedMessae.setText("");
                        }
                    });


                    //---------------------------------------------------------------------------------------//

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }
}
