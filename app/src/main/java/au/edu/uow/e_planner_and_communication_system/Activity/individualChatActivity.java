package au.edu.uow.e_planner_and_communication_system.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.uow.e_planner_and_communication_system.Fragment.Messages;
import au.edu.uow.e_planner_and_communication_system.Fragment.MessagesAdpater;
import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class individualChatActivity extends AppCompatActivity {

    private String ID_user;
    private String name_of_user;
    private String user_status_string;
    private String thumb_image_url;

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

    private final List<Messages> messagesList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;

    private MessagesAdpater messagesAdpater;

    private static int gallery_pick =1;
    private StorageReference messageImageStorageRef;

    private ProgressDialog loadingBarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_chat);

        //Passed in
        ID_user =getIntent().getExtras().get("visit_profile_id").toString();
        name_of_user = getIntent().getExtras().get("name").toString();
        user_status_string = getIntent().getExtras().get("user_status").toString();
        thumb_image_url = getIntent().getExtras().get("user_thumb_image").toString();




        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();

        ChatToolBar = (Toolbar) findViewById(R.id.group_chatbar);
        setSupportActionBar(ChatToolBar);

        loadingBarImage = new ProgressDialog(this);

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
        sendMessgaeButton = (ImageButton) findViewById(R.id.individual_chat_send_message_button);
        selectImageToSend = (ImageButton) findViewById(R.id.individual_chat_select_image);
        inputtedMessae = (EditText) findViewById(R.id.individual_chat_message_input);

        nameTitle.setText(name_of_user);
        userLastSeen.setText(user_status_string);

        Picasso.get().load(thumb_image_url).placeholder(R.drawable.default_image_profile).into(profileImageChat);


        messageImageStorageRef = FirebaseStorage.getInstance().getReference().child("Messages_Pictures");


        messagesAdpater = new MessagesAdpater(messagesList);

        userMessagesList = (RecyclerView) findViewById(R.id.chat_activity_list);


        linearLayoutManager = new LinearLayoutManager(this);

        userMessagesList.setHasFixedSize(true);

        userMessagesList.setLayoutManager(linearLayoutManager);

        userMessagesList.setAdapter(messagesAdpater);

        FetchMessages();

        //Used for last seen
rootRef.child("Users").child(ID_user).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});



        sendMessgaeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessge();

            }
        });

        selectImageToSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/");
                startActivityForResult(galleryIntent, gallery_pick);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gallery_pick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            final String messenger_sender_ref = "Messages/" + messageSenderID + "/" + ID_user;
            final String messenger_receiver_ref =  "Messages/" + ID_user +  "/" + messageSenderID;


            DatabaseReference user_message_key = rootRef.child("Messages").child(messageSenderID).
                    child(ID_user).push();


            //---------------------------------------------------------------------------------------//

            //---------------------------------------------------------------------------------------//
            final String message_push_id = user_message_key.getKey();

            StorageReference filePath = messageImageStorageRef.child(message_push_id + ".jpg");
            filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                  if(task.isSuccessful())
                  {
                      loadingBarImage.setTitle("Sending Chat Image");
                      loadingBarImage.setMessage("Please wait :)");
                      loadingBarImage.show();

                      final String downloadURL = task.getResult().getDownloadUrl().toString();


                      Map messageTextBody = new HashMap();
                      //---------------------------------------------------------------------------------------//

                      //---------------------------------------------------------------------------------------//
                      messageTextBody.put("message",downloadURL);
                      messageTextBody.put("seen",false);
                      messageTextBody.put("type","image");
                      messageTextBody.put("time", ServerValue.TIMESTAMP);
                      messageTextBody.put("from",messageSenderID);
                      messageTextBody.put("to",ID_user);


                      Map messageBodyDetails = new HashMap();
                      messageBodyDetails.put(messenger_sender_ref + "/" + message_push_id, messageTextBody);
                      //Receiver
                      messageBodyDetails.put(messenger_receiver_ref+"/"+message_push_id,messageTextBody);

                      DatabaseReference usersConnected1 = FirebaseDatabase.getInstance().getReference().
                              child("Messengers-Linked").child(messageSenderID).child(ID_user);

                      DatabaseReference usersConnected2 = FirebaseDatabase.getInstance().getReference().
                              child("Messengers-Linked").child(ID_user).child(messageSenderID);

                      String name2 = mAuth.getCurrentUser().getDisplayName().toString();


                      String lastMessage = downloadURL.toString();

                      usersConnected1.child("name").setValue(name_of_user);
                      usersConnected1.child("last-message").setValue(lastMessage);
                      usersConnected1.child("time").setValue(ServerValue.TIMESTAMP);

                      usersConnected2.child("name").setValue(name2);
                      usersConnected2.child("last-message").setValue(lastMessage);
                      usersConnected2.child("time").setValue(ServerValue.TIMESTAMP);

                        //
                      rootRef.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                          @Override
                          public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                              if(databaseError!=null)
                              {
                                  Log.d("Chat_Log", databaseError.getMessage().toString());
                              }
                              inputtedMessae.setText("");
                                loadingBarImage.dismiss();
                          }
                      });



                      Toast.makeText(individualChatActivity.this,"Success!",Toast.LENGTH_SHORT).show();
                        loadingBarImage.dismiss();
                  } else
                      {
                          Toast.makeText(individualChatActivity.this,"Failure!", Toast.LENGTH_SHORT).show();
                            loadingBarImage.dismiss();
                      }


                }
            });
        }
    }

    private void FetchMessages()
    {
        rootRef.child("Messages").child(messageSenderID).child(ID_user)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {

                        Messages massages = dataSnapshot.getValue(Messages.class);

                        messagesList.add(massages);

                        messagesAdpater.notifyDataSetChanged();
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
        String messaeText = inputtedMessae.getText().toString();

        //Input Validation
        //Empty First
        if(TextUtils.isEmpty(messaeText)){
            //Empty message
        }
        //If not empty,
        else{

            String messenger_sender_ref = "Messages/" + messageSenderID + "/" + ID_user;
            String messenger_receiver_ref =  "Messages/" + ID_user +  "/" + messageSenderID;

            //Create unique message key
            //---------------------------------------------------------------------------------------//
            DatabaseReference user_message_key = rootRef.child("Messages").child(messageSenderID).
                    child(ID_user).push();


            //---------------------------------------------------------------------------------------//

            //---------------------------------------------------------------------------------------//
            String message_push_id = user_message_key.getKey();

            //---------------------------------------------------------------------------------------//

            //---------------------------------------------------------------------------------------//
            Map messageTextBody = new HashMap();
            //---------------------------------------------------------------------------------------//

            //---------------------------------------------------------------------------------------//
            messageTextBody.put("message",messaeText);
            messageTextBody.put("seen",false);
            messageTextBody.put("type","text");
            messageTextBody.put("time", ServerValue.TIMESTAMP);
            messageTextBody.put("from",messageSenderID);
            messageTextBody.put("to",ID_user);



            DatabaseReference usersConnected1 = FirebaseDatabase.getInstance().getReference().
                    child("Messengers-Linked").child(messageSenderID).child(ID_user);

            DatabaseReference usersConnected2 = FirebaseDatabase.getInstance().getReference().
                    child("Messengers-Linked").child(ID_user).child(messageSenderID);

            String name2 = mAuth.getCurrentUser().getDisplayName().toString();


            String lastMessage = messaeText;

            usersConnected1.child("name").setValue(name_of_user);
            usersConnected1.child("last-message").setValue(messaeText);
            usersConnected1.child("time").setValue(ServerValue.TIMESTAMP);

            usersConnected2.child("name").setValue(name2);
            usersConnected2.child("last-message").setValue(messaeText);
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
    }
}
