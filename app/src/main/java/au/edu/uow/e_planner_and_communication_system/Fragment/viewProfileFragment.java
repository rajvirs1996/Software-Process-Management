package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class viewProfileFragment extends AppCompatActivity {

    private Button messageButton;
    private Button addButton;
    private Button removeButton;
    private TextView name;
    private TextView studentID;
    private TextView email;
    private CircleImageView profilePic;

    private DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");



        String visit_profile_id = getIntent().getExtras().get("visit_profile_id").toString();

        messageButton = (Button) findViewById(R.id.viewprofile_message);
        addButton = (Button) findViewById(R.id.viewprofile_add);
        removeButton = (Button) findViewById(R.id.viewprofile_remove);
        name = (TextView) findViewById(R.id.viewprofile_display_name);
        studentID = (TextView) findViewById(R.id.viewprofile_sid_text_view);
        email = (TextView) findViewById(R.id.viewprofile_email_text);
        profilePic = (CircleImageView) findViewById(R.id.viewprofile_image);

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
