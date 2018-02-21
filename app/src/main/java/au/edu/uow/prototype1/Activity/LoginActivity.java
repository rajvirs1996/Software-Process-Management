package au.edu.uow.prototype1.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import au.edu.uow.prototype1.MainActivity;
import au.edu.uow.prototype1.R;

/**
 * Created by Tony on 19/2/2018.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "UserInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String email = settings.getString("Email", "");
        String password = settings.getString("Password", "");

        // TODO Fix check logic
        if (!email.isEmpty() && !password.isEmpty()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
        }


        final Button button = findViewById(R.id.loginBtn);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();

                // TODO String Username = get it from fire base and store it to local

                EditText email = findViewById(R.id.emailText);
                EditText password = findViewById(R.id.passwordText);

                // Check for empty input for email and without @ sign
                boolean isEmailWrong = false;
                if (email.getText().toString().equals("") || email.getText().toString().indexOf('@') == -1) {
                    email.setError("Wrong email!");
                    email.requestFocus();
                    isEmailWrong = true;
                }
                if (password.getText().toString().equals("") && !isEmailWrong) {
                    password.setError("Wrong password!");
                    email.requestFocus();
                }

                // Write config
                if (email.getText() != null && !email.getText().toString().isEmpty()
                        && password.getText() != null && !password.getText().toString().isEmpty()) {

                    editor.putString("Email", email.getText().toString());
                    editor.putString("Password", password.getText().toString());

                    // Save the content after other write task is down, .commit() write it immediately
                    editor.apply();

                    // Go into main app
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //intent.putExtra("email", email.getText().toString());
                    //intent.putExtra("password", password.getText().toString());
                    startActivity(intent);
                }


            }
        });
    }
}
