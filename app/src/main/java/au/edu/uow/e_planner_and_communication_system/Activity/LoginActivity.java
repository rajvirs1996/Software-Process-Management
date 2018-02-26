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

import au.edu.uow.e_planner_and_communication_system.MainActivity;
import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by Tony on 19/2/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //private static final String PREFS_NAME = "UserInfo";
    private String username;
    private FirebaseAuth mAuth;
    private EditText emailText;
    private EditText passwordText;
    //private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //View
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        //Button
        findViewById(R.id.loginBtn).setOnClickListener(this);
        findViewById(R.id.signUpBtn).setOnClickListener(this);

        //Initialise the Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Restore preferences
        //SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        //String email = settings.getString("Email", "");
        //String password = settings.getString("Password", "");

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }


        // Code here executes on main thread after user presses loginBtn
//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences.Editor editor = settings.edit();


        // Write config
//        if (emailText.getText() != null && !emailText.getText().toString().isEmpty()
//                && passwordText.getText() != null && !passwordText.getText().toString().isEmpty()) {
//
//            editor.putString("Email", emailText.getText().toString());
//            editor.putString("Password", passwordText.getText().toString());
//
//            // Save the content after other write task is down, .commit() write it immediately
//            editor.apply();
//
//            // Go into main app
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            //intent.putExtra("emailText", emailText.getText().toString());
//            //intent.putExtra("passwordText", passwordText.getText().toString());
//            startActivity(intent);
    }


    private void signIn() {
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Go into main app
                            // TODO remove bundle and let main act get info by itself
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("Username", user != null ? user.getDisplayName() : user.getEmail());
//                            bundle.putString("Email", user.getEmail());
//                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(this.getClass().getSimpleName(), "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {

        // Check for empty input for emailText and without @ sign
        boolean valid = true;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (TextUtils.isEmpty(email) || email.indexOf('@') == -1) {
            emailText.setError("Wrong email!");
            emailText.requestFocus();
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (TextUtils.isEmpty(password) && !valid) {
            passwordText.setError("Wrong password");
            passwordText.requestFocus();
        } else {
            passwordText.setError(null);
        }
        //Fix check logic
//        if (Text) {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra("emailText", emailText);
//            intent.putExtra("passwordText", passwordText);
//            startActivity(intent);
//        }
        return valid;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.loginBtn) {
            signIn();
        } else if (i == R.id.signUpBtn) {
            signUp();
        }
    }

    private void signUp() {
        //TODO Finish sign up
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("Email", emailText.getText().toString());
//        bundle.putString("Password", passwordText.getText().toString());
//        intent.putExtras(bundle);
        startActivity(intent);
    }
}
