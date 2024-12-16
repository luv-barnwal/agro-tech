package com.luv.agrotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    public static boolean isValidEmail(CharSequence target){
        if (target == null){
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailEditText.getText().toString().isEmpty()){
                    emailEditText.setError("Enter an email address");
                } else if (!isValidEmail(emailEditText.getText().toString())){
                    emailEditText.setError("Enter a valid email address");
                } else if (usernameEditText.getText().toString().isEmpty()){
                    usernameEditText.setError("Enter your name");
                } else if (passwordEditText.getText().toString().isEmpty()){
                    passwordEditText.setError("Enter a password");
                } else if (passwordEditText.getText().toString().length() <= 8){
                    passwordEditText.setError("Password should be longer than 8 characters");
                } else {
                    mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                reload(user);
                            } else if (task.getException().toString().contains("There is no user record corresponding to this identifier.")){
                                mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(usernameEditText.getText().toString()).build();
                                        firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                    reload(firebaseUser);
                                                } else {
                                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                    reload(null);
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                reload(null);
                            }
                        }
                    });
                }
            }
        });
    }

    private void reload(FirebaseUser user) {
        if (user != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("name", user.getDisplayName());
//            intent.putExtra("email", user.getEmail());
            startActivity(intent);
        }
    }
}