package com.v_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.v_tracker.ui.Database.Database;

public class LoginActivity extends AppCompatActivity {

    public static boolean isLoggedIn = false;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    Database db = new Database();
    FirebaseAuth mFirebaseAuth;
    TextView text_email;
    TextView text_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();

        Button login_button = (Button) findViewById(R.id.login_button);
        text_email = (TextView) findViewById(R.id.inputEmail);
        text_password = (TextView) findViewById(R.id.inputPassword);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = text_email.getText().toString();
                String pwd = text_password.getText().toString();
                if (email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
                }
                else if (email.isEmpty()){
                    text_email.setError(getResources().getString(R.string.empty_email));
                    text_email.requestFocus();
                }
                else if (pwd.isEmpty()){
                    text_password.setError(getResources().getString(R.string.empty_password));
                    text_password.requestFocus();
                }
                else {
                    mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                isLoggedIn = true;
                                editor.putBoolean("IS_LOGGED", isLoggedIn);
                                editor.commit();
                                /*
                                Date currentTime = Calendar.getInstance().getTime();
                                db.addNewPosition(new Position(currentTime.toString(),0,0));
                                */
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            else{
                                Toast.makeText(LoginActivity.this,
                                        getResources().getString(R.string.login_unsuccesful),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        TextView no_login = (TextView) findViewById(R.id.no_register);
        no_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
