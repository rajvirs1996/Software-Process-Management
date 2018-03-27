package au.edu.uow.e_planner_and_communication_system.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class viewProfileFragment extends DialogFragment {

    private Button messageButton;
    private Button addButton;
    private Button removeButton;
    private TextView name;
    private TextView studentID;
    private TextView email;
    private CircleImageView profilePic;
    private String ID;
    private String namePass;

    private DatabaseReference usersReference;


    public void setID(String ID){

        this.ID = ID;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_view_profile,null);

    }

    public void showDialog(View view)
    {
        String idToPass = ID;

        FragmentManager manager = getFragmentManager();
        addTo addToDialog = new addTo();
        //viewProfileDialog.setID(idToPass);
        addToDialog.show(manager,"MyDialog");


    }

    private void showDialogRemove(View view) {

        String idToPass = ID;

        FragmentManager manager = getFragmentManager();
        removeFrom  removeFromDialog = new removeFrom();
        //viewProfileDialog.setID(idToPass);
        removeFromDialog.show(manager,"MyDialog");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");



        final String visit_profile_id = ID;

        messageButton = (Button) view.findViewById(R.id.viewprofile_message);
        addButton = (Button) view.findViewById(R.id.viewprofile_add);
        removeButton = (Button) view.findViewById(R.id.viewprofile_remove);
        name = (TextView) view.findViewById(R.id.viewprofile_display_name);
        studentID = (TextView) view.findViewById(R.id.viewprofile_sid_text_view);
        email = (TextView) view.findViewById(R.id.viewprofile_email_text);
        profilePic = (CircleImageView) view.findViewById(R.id.viewprofile_image);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currUser = mAuth.getCurrentUser().getUid().toString();

       DatabaseReference checkStatusRef =  FirebaseDatabase.getInstance().getReference().child("Users").child(currUser);


        addButton.setVisibility(Button.INVISIBLE);
        removeButton.setVisibility(Button.INVISIBLE);


       checkStatusRef.child("isAdmin").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               String boolIsAdmin = dataSnapshot.getValue().toString();

               if (boolIsAdmin.equals("true") ){

                   addButton.setVisibility(Button.VISIBLE);
                   removeButton.setVisibility(Button.VISIBLE);

               } else{

                   addButton.setVisibility(Button.INVISIBLE);
                   removeButton.setVisibility(Button.INVISIBLE);

               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


       checkStatusRef.child("isTeachingStaff").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               String boolIsTeachingStaff = dataSnapshot.getValue().toString();

               if (boolIsTeachingStaff.equals("true") ){

                   addButton.setVisibility(Button.VISIBLE);
                   removeButton.setVisibility(Button.VISIBLE);

               } else{

                   addButton.setVisibility(Button.INVISIBLE);
                   removeButton.setVisibility(Button.INVISIBLE);

               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });





        usersReference.child(visit_profile_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String sName = dataSnapshot.child("name").getValue().toString();
                namePass=sName;
                String sid = dataSnapshot.child("SID").getValue().toString();
                String sEmail = dataSnapshot.child("login_email").getValue().toString();
                String sImage = dataSnapshot.child("user_image").getValue().toString();

                name.setText(sName);
                studentID.setText(sid);
                email.setText(sEmail);
                Picasso.get().load(sImage).placeholder(R.drawable.default_image_profile).into(profilePic);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent individualChat = new Intent(getContext(),individualChatActivity.class);
                individualChat.putExtra("visit_profile_id",visit_profile_id);
                individualChat.putExtra("name",namePass);
                startActivity(individualChat);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRemove(view);
            }
        });

    }


}
