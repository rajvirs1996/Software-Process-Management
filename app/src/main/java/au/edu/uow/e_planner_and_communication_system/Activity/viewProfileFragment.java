package au.edu.uow.e_planner_and_communication_system.Activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    private DatabaseReference usersReference;


    public void setID(String ID){

        this.ID = ID;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_view_profile,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");



        String visit_profile_id = ID;

        messageButton = (Button) view.findViewById(R.id.viewprofile_message);
        addButton = (Button) view.findViewById(R.id.viewprofile_add);
        removeButton = (Button) view.findViewById(R.id.viewprofile_remove);
        name = (TextView) view.findViewById(R.id.viewprofile_display_name);
        studentID = (TextView) view.findViewById(R.id.viewprofile_sid_text_view);
        email = (TextView) view.findViewById(R.id.viewprofile_email_text);
        profilePic = (CircleImageView) view.findViewById(R.id.viewprofile_image);

        usersReference.child(visit_profile_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String sName = dataSnapshot.child("name").getValue().toString();
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
    }
}
