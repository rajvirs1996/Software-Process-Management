package au.edu.uow.e_planner_and_communication_system.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import au.edu.uow.e_planner_and_communication_system.MainActivity;
import au.edu.uow.e_planner_and_communication_system.R;

public class SignUpActivity extends BasicActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDefaultDataReference;

    private EditText nameSignUp;
    private EditText emailSignUp;
    private EditText studentIDSignUp;
    private EditText passwordSignUp;
    private EditText confPasswordSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //View
        nameSignUp = findViewById(R.id.nameSignUp);
        emailSignUp = findViewById(R.id.emailSignUp);
        studentIDSignUp = findViewById(R.id.studentIDSignUp);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        confPasswordSignUp = findViewById(R.id.confPasswordText);

        //Button
        findViewById(R.id.signUp).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        //Initialise the Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.signUp) {
            signUp(emailSignUp.getText().toString(), nameSignUp.getText().toString(), studentIDSignUp.getText().toString());
        } else if (i == R.id.back) {
            finish();
            this.overridePendingTransition(R.anim.anim_slide_in_to_right, R.anim.anim_slide_out_to_right);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(R.anim.anim_slide_in_to_right, R.anim.anim_slide_out_to_right);
    }

    private void signUp(final String emailSignup, final String name, final String SID) {
        if (!validateForm()) {
            return;
        }

        // Show loading dialog
        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(emailSignUp.getText().toString(), passwordSignUp.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Capture UID
                            String currentUserID = mAuth.getCurrentUser().getUid();

                            //Creates a reference and stores the reference in the variable in which is the root->->
                            storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

                            storeUserDefaultDataReference.child("login_email").setValue(emailSignup);
                            storeUserDefaultDataReference.child("name").setValue(name);
                            storeUserDefaultDataReference.child("SID").setValue(SID);
                            storeUserDefaultDataReference.child("user_status").setValue("Online");
                            storeUserDefaultDataReference.child("user_image").setValue("default_profile");
                            storeUserDefaultDataReference.child("user_thumb_image").setValue("default_image");
                            storeUserDefaultDataReference.child("isStudent").setValue(true);
                            storeUserDefaultDataReference.child("isAdmin").setValue(false);
                            storeUserDefaultDataReference.child("isTeachingStaff").setValue(false);

                            DatabaseReference studentListRef = FirebaseDatabase.getInstance().getReference().child("Students").child(currentUserID);
                            //DatabaseReference adminListRef  = FirebaseDatabase.getInstance().getReference().child("Admins");
                            //DatabaseReference teacherListRef  = FirebaseDatabase.getInstance().getReference().child("Teachers");

                            studentListRef.child("name").setValue(name);
                            studentListRef.child("SID").setValue(SID);

                            //To be removed
                            /*
                            teacherListRef.child("name").setValue(name);
                            teacherListRef.child("ID").setValue(SID);
                            */


                            FirebaseUser user = mAuth.getCurrentUser();
                            setUpUserInfo(user);

                            // Hide loading dialog
                            hideProgressDialog();

                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            getApplicationContext().startActivity(intent);
                            overridePendingTransition(R.anim.anim_slide_in_to_right, R.anim.anim_slide_out_to_right);
                            finish();

                        } else {
                            // TODO Better exception handling
                            // If sign up fails, display a message to the user.
                            Log.w(this.getClass().getSimpleName(), "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "User already existed.",
                                    Toast.LENGTH_SHORT).show();

                            // Hide loading dialog
                            //hideProgressDialog();
                        }
                    }

                    private void setUpUserInfo(FirebaseUser user) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nameSignUp.getText().toString())
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(this.getClass().getSimpleName(), "User profile updated.");
                                        }
                                    }
                                });
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = nameSignUp.getText().toString();
        String email = emailSignUp.getText().toString();
        String password = passwordSignUp.getText().toString();
        String confPassword = confPasswordSignUp.getText().toString();
        String sid = studentIDSignUp.getText().toString();

        //<editor-fold desc="Logic">
        if (TextUtils.isEmpty(name)) {
            nameSignUp.setError("Please enter your name!");
            nameSignUp.requestFocus();
            valid = false;
        } else {
            nameSignUp.setError(null);
        }

        if ((TextUtils.isEmpty(email) || email.indexOf('@') == -1) && valid) {
            emailSignUp.setError("Please enter your email!");
            emailSignUp.requestFocus();
            valid = false;
        } else {
            emailSignUp.setError(null);
        }

        if (TextUtils.isEmpty(sid) && valid) {
            studentIDSignUp.setError("Please enter your student ID!");
            studentIDSignUp.requestFocus();
            valid = false;
        } else {
            studentIDSignUp.setError(null);
        }

        if (TextUtils.isEmpty(password) && valid) {
            passwordSignUp.setError("Please enter the password!");
            passwordSignUp.requestFocus();
            valid = false;
        } else {
            passwordSignUp.setError(null);
        }

        if (TextUtils.isEmpty(confPassword) && valid) {
            confPasswordSignUp.setError("Please enter the password again!");
            confPasswordSignUp.requestFocus();
            valid = false;
        } else {
            confPasswordSignUp.setError(null);
        }


        if ((!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confPassword)) && valid) {
            if (!TextUtils.equals(password, confPassword)) {
                passwordSignUp.setText("");
                confPasswordSignUp.setText("");
                //passwordSignUp.requestFocus();
                confPasswordSignUp.setError("Password does not match the confirm password");
                valid = false;
            } else {
                confPasswordSignUp.setError(null);
            }

            if (password.length() <= 5 && valid) {
                passwordSignUp.setText("");
                confPasswordSignUp.setText("");
                passwordSignUp.requestFocus();
                passwordSignUp.setError("Password length must be greater than 6 digit");
                valid = false;
            } else {
                passwordSignUp.setError(null);
            }
        }
        //</editor-fold>

        return valid;
    }
}
