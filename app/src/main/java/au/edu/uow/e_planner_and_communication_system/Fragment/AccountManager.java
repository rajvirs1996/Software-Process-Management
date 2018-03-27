package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Athens on 2018/02/27.
 */

public class AccountManager extends Fragment {

    private TextView accountManagerDisplayName;
    private TextView getAccountManagerDisplaystatus;
    private CircleImageView accountManagerDisplayImagae;
    private Button accountManagerChangeStatus;
    private Button accountManagerChangePassword;
    private Button accountManagerChangeImage;

    private final static int Gallery_pick = 1;
    private StorageReference storeProfileImageStorageReference;

    private DatabaseReference getUserDataReference;
    private FirebaseAuth mAuth;

    private Bitmap thumb_bitmap = null;

    private StorageReference thumb_image_ref;

    private ProgressDialog loadingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.accountmanager, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Get the uid
        mAuth = FirebaseAuth.getInstance();
        final String online_user_id = mAuth.getCurrentUser().getUid();
        getUserDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);
        storeProfileImageStorageReference = FirebaseStorage.getInstance().getReference().child("Profile_Images");

        //Storage References
        thumb_image_ref = FirebaseStorage.getInstance().getReference().child("thumb_images");

        accountManagerDisplayImagae = (CircleImageView) view.findViewById(R.id.account_management_image);
        accountManagerDisplayName = (TextView) view.findViewById(R.id.display_name);
        getAccountManagerDisplaystatus = (TextView) view.findViewById(R.id.status_display);
        accountManagerChangeStatus = (Button) view.findViewById(R.id.account_managment_button__status);
        accountManagerChangePassword = (Button) view.findViewById(R.id.account_managment_password_button);
        accountManagerChangeImage = (Button) view.findViewById(R.id.accountmanager_image_button);

        loadingBar = new ProgressDialog(getContext());

        getUserDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String image = dataSnapshot.child("user_image").getValue().toString();
                String thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();

                accountManagerDisplayName.setText(name);
                getAccountManagerDisplaystatus.setText(status);



                if (!image.equals("default_image")) {
                    //Load Profile Picture
                    Picasso.get().load(image).placeholder(R.drawable.default_image_profile).into(accountManagerDisplayImagae);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        accountManagerChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryFragment = new Intent();
                galleryFragment.setAction(Intent.ACTION_GET_CONTENT);
                galleryFragment.setType("image/");
                startActivityForResult(galleryFragment, Gallery_pick);
            }
        });



       DatabaseReference user_statusRef = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id)
               .child("user_status");
       user_statusRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               final String status = (String) dataSnapshot.getValue();

               if(status.equals("Offline")){

                   getAccountManagerDisplaystatus.setTextColor(Color.RED);

               } else if(status.equals("Online"))
               {
                   getAccountManagerDisplaystatus.setTextColor(Color.GREEN);
               }

               accountManagerChangeStatus.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if (status.equals("Online")){
                           FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id).child("user_status").setValue("Offline");
                           getAccountManagerDisplaystatus.setTextColor(Color.RED);
                       } else if (status.equals("Offline")){
                           FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id).child("user_status").setValue("Online");
                           getAccountManagerDisplaystatus.setTextColor(Color.GREEN);
                       }
                   }
               });

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_pick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();
            //Intent Code
            // CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(getActivity());

            //Fragment-based
            CropImage.activity().setAspectRatio(1, 1).start(getContext(), this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                loadingBar.setTitle("Updating Profile Picture");
                loadingBar.setMessage("Please wait while we are updating your selected profile image");
                loadingBar.show();
                Uri resultUri = result.getUri();

                File thumb_file_path = new File(resultUri.getPath());


                try {
                    thumb_bitmap = new Compressor(getActivity()).
                            setMaxHeight(200).setMaxWidth(200).
                            setQuality(50).
                            compressToBitmap(thumb_file_path);
                } catch (IOException e) {

                    e.printStackTrace();

                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                //Convert and compress
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();


                String user_id = mAuth.getCurrentUser().getUid();
                StorageReference filePath = storeProfileImageStorageReference.child(user_id + ".jpg");

                final StorageReference thumbfilePath = thumb_image_ref.child(user_id + ".jpg");


                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Saving your Profile Image", Toast.LENGTH_LONG).show();

                            final String downloadURL = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumbfilePath.putBytes(thumb_byte);

                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_download_url = thumb_task.getResult().getDownloadUrl().toString();

                                    if (task.isSuccessful()) {
                                        Map update_user_data = new HashMap();
                                        update_user_data.put("user_image", downloadURL);
                                        update_user_data.put("user_thumb_image", thumb_download_url);

                                        getUserDataReference.updateChildren(update_user_data).
                                                addOnCompleteListener(new OnCompleteListener<Void>() //Ensure success
                                                {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(getActivity(), "Image Profile Updates Successfully", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                });
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Error occurred!", Toast.LENGTH_LONG).show();

                            loadingBar.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
