package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Athens on 2018/02/27.
 */

public class AccountManager extends Fragment {

    private CircleImageView accountManagerDisplayImagae;
    private TextView accountManagerDisplayName;
    private TextView getAccountManagerDisplaystatus;
    private Button accountManagerChangeStatus;
    private Button accountManagerChangePassword;

    private DatabaseReference getUserDataReference;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.accountmanager, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        //Get the uid
        mAuth = FirebaseAuth.getInstance();
        String online_user_id = mAuth.getCurrentUser().getUid();

        getUserDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);


        accountManagerDisplayImagae = (CircleImageView) view.findViewById(R.id.account_management_image);
        accountManagerDisplayName = (TextView) view.findViewById(R.id.display_name);
        getAccountManagerDisplaystatus = (TextView) view.findViewById(R.id.status_display);
        accountManagerChangeStatus = (Button) view.findViewById(R.id.account_managment_button__status);
        accountManagerChangePassword = (Button) view.findViewById(R.id.account_managment_password_button);

        getUserDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String image = dataSnapshot.child("user_image").getValue().toString();
                String thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();

                accountManagerDisplayName.setText(name);
                getAccountManagerDisplaystatus.setText(status);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
