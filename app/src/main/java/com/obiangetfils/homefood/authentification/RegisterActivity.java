package com.obiangetfils.homefood.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.controller.HomeActivity;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    private Button loginWith, register, login;
    private EditText emailEdt, passwordEdt, phoneEdt, usernameEdt;
    private LinearLayout linearLayoutRegisterForm;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("UserData");

        initView();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWith.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                loginWith.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                register.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                register.setTextColor(getResources().getColor(R.color.colorWhite));
                linearLayoutRegisterForm.setVisibility(View.VISIBLE);

                login.setText(R.string.SIGN_UP);
            }
        });

        loginWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginWith.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                loginWith.setTextColor(getResources().getColor(R.color.colorWhite));
                register.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                register.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                linearLayoutRegisterForm.setVisibility(View.GONE);
                login.setText(R.string.SIGN_IN);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SpotsDialog waitinDialog = (SpotsDialog) new SpotsDialog.Builder()
                        .setContext(RegisterActivity.this)
                        .setMessage("Connexion ou Inscription en cours")
                        .setCancelable(false)
                        .build();
                waitinDialog.show();

                String emailData = emailEdt.getText().toString();
                String passwordData = passwordEdt.getText().toString();
                String phoneData = phoneEdt.getText().toString();
                String usernameData = usernameEdt.getText().toString();

                if (linearLayoutRegisterForm.getVisibility() == View.VISIBLE) {
                    getAllData(usernameData, phoneData, emailData, passwordData, waitinDialog);

                } else {
                    getEmailAndPassword(emailData, passwordData, waitinDialog);
                }
            }
        });
    }

    private void getAllData(final String usernameData, final String phoneData, final String emailData, final String passwordData, final SpotsDialog waitinDialog) {

        mAuth.createUserWithEmailAndPassword(emailData, passwordData)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            saveData(usernameData, phoneData, emailData, passwordData, waitinDialog);

                        } else {
                            // If sign in fails, display a message to the user.
                            waitinDialog.dismiss();
                            Toasty.error(getApplicationContext(), R.string.authentification_failed,
                                    Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });

    }

    private void saveData(final String usernameData, final String phoneData, final String emailData, final String passwordData, final SpotsDialog waitinDialog) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userId = mAuth.getUid();
                if (!(dataSnapshot.child("Users").child(userId).exists())) {

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", usernameData);
                    userdataMap.put("phone", phoneData);
                    userdataMap.put("email", emailData);
                    userdataMap.put("password", passwordData);

                    myRef.child("Users").child(userId).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                waitinDialog.dismiss();
                                Toasty.success(RegisterActivity.this, R.string.account_creates_suuessfully,
                                        Toast.LENGTH_SHORT, true).show();
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                initEditText(usernameEdt, phoneEdt, emailEdt, passwordEdt);

                            } else {
                                // If sign in fails, display a message to the user.
                                waitinDialog.dismiss();
                                Toasty.error(getApplicationContext(), R.string.authentification_failed, Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                waitinDialog.dismiss();
            }
        });
    }

    private void getEmailAndPassword(String emailData, String passwordData, SpotsDialog waitinDialog) {

        if (!(TextUtils.isEmpty(emailData)) && !(TextUtils.isEmpty(passwordData))) {
            login(emailData, passwordData, waitinDialog);
        } else {
            waitinDialog.dismiss();
            Toasty.error(getApplicationContext(), R.string.all_field_mandatory,
                    Toast.LENGTH_SHORT, true).show();
        }

    }

    private void login(String emailData, String passwordData, final SpotsDialog waitinDialog) {
        mAuth.signInWithEmailAndPassword(emailData, passwordData)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            Intent homeIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                            homeIntent.putExtra("LOGIN_TYPE", "EmailPassword");
                            startActivity(homeIntent);

                            waitinDialog.dismiss();
                            Toasty.success(RegisterActivity.this, R.string.you_are_logined, Toast.LENGTH_SHORT, true).show();
                            initEditText(usernameEdt, phoneEdt, emailEdt, passwordEdt);

                        } else {
                            // If sign in fails, display a message to the user.
                            waitinDialog.dismiss();
                            Toasty.error(getApplicationContext(), R.string.authentification_failed, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void initEditText(EditText usernameEdt, EditText phoneEdt, EditText emailEdt, EditText passwordEdt) {

        usernameEdt.setText("");
        usernameEdt.setHint(getResources().getString(R.string.user_name));

        phoneEdt.setText("");
        phoneEdt.setHint(getResources().getString(R.string.user_phone));

        emailEdt.setText("");
        emailEdt.setHint(getResources().getString(R.string.user_email));

        passwordEdt.setText("");
        passwordEdt.setHint(getResources().getString(R.string.user_password));

    }

    private void initView() {

        loginWith = (Button) findViewById(R.id.loginWith);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        emailEdt = (EditText) findViewById(R.id.email);
        passwordEdt = (EditText) findViewById(R.id.password);
        phoneEdt = (EditText) findViewById(R.id.phone_number_id);
        usernameEdt = (EditText) findViewById(R.id.username_id);
        linearLayoutRegisterForm = (LinearLayout) findViewById(R.id.register_include);
        linearLayoutRegisterForm.setVisibility(View.GONE);
        login.setText(R.string.sign_in);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}