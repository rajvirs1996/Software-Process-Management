package au.edu.uow.e_planner_and_communication_system.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDefaultDataReference;

    private EditText nameSignUp;
    private EditText emailSignUp;
    private EditText passwordSignUp;
    private EditText confPasswordSignUp;
    private EditText studentIDSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //View
        nameSignUp = findViewById(R.id.nameSignUp);
        emailSignUp = findViewById(R.id.emailSignUp);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        confPasswordSignUp = findViewById(R.id.confPasswordText);
        studentIDSignUp = findViewById(R.id.studentIDSignUp);

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


    private void signUp(final String emailSignup, final String name, final String SID) {
        if (!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(emailSignUp.getText().toString(), passwordSignUp.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Capture UID
                            String currentUserID = mAuth.getCurrentUser().getUid();

                            //Creates a reference and stores the reference in the variable in which is the root->->
                            storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

                            storeUserDefaultDataReference.child("login_email").setValue(emailSignup);
                            storeUserDefaultDataReference.child("name").setValue(name);
                            storeUserDefaultDataReference.child("SID").setValue(SID);
                            storeUserDefaultDataReference.child("user_status").setValue("Online");
                            storeUserDefaultDataReference.child("user_image").setValue("default_profile");
                            storeUserDefaultDataReference.child("user_thumb_image").setValue("default_image").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        setUpUserInfo(user);
//                            updateUI(user);
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        getApplicationContext().startActivity(intent);
                                        overridePendingTransition(R.anim.anim_slide_in_to_right, R.anim.anim_slide_out_to_right);
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(this.getClass().getSimpleName(), "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "User already existed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                        // ...
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

        if (!TextUtils.equals(password, confPassword) && valid) {
            passwordSignUp.setError("Password does not match the confirm password");
            passwordSignUp.setText("");
            confPasswordSignUp.setText("");
            passwordSignUp.requestFocus();
            valid = false;
        } else {
            passwordSignUp.setError(null);
        }
        //</editor-fold>

        return valid;
    }
}
