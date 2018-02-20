package au.edu.uow.prototype1.Activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button button = findViewById(R.id.loginBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                SharedPreferences userInfoSetting = getSharedPreferences("LoginInfo", 0);
                SharedPreferences.Editor editor = userInfoSetting.edit();

                //String Username = userInfoSetting.getString("Username", ""); get it from firebase

                EditText email = findViewById(R.id.emailText);
                EditText password = findViewById(R.id.passwordText);

                if (email.getText() != null && !email.getText().toString().isEmpty()
                        && password.getText() != null && !password.getText().toString().isEmpty()) {

                    editor.putString("Email", email.getText().toString());
                    editor.putString("Password", password.getText().toString());

                    editor.apply();

                }


            }
        });
    }


}
