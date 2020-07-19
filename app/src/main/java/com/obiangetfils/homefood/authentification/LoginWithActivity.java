package com.obiangetfils.homefood.authentification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.obiangetfils.homefood.R;
import com.obiangetfils.homefood.controller.HomeActivity;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;


public class LoginWithActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button emailBtn;
    private SignInButton googleBtn;
    private TextView createAccount;
    private FirebaseAuth mAuth;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    private Context mContext;

    private CallbackManager mCallbackManager;
    private SpotsDialog waitinDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with);

        createAccount = (TextView) findViewById(R.id.create_account);
        //facebookBtn = (LoginButton) findViewById(R.id.facebook);
        googleBtn = (SignInButton) findViewById(R.id.google);
        emailBtn = (Button) findViewById(R.id.email_login);
        mContext = this;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        TextView textView = (TextView) googleBtn.getChildAt(0);
        textView.setText("Se connecter avec Google");
        textView.setTextColor(getResources().getColor(R.color.colorWhite));
        textView.setBackground(getResources().getDrawable(R.drawable.google_button));
        textView.setTextSize(18);

        /**Init Facebook **/

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook);
        // mBinding.buttonFacebookLogin

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                waitingDialog(mContext, "Connexion avec facebook en cours");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        /** end facebook login  **/

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginWithActivity.this, RegisterActivity.class));
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginWithActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        waitinDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            waitingDialog(LoginWithActivity.this, "Connexion avec Google en cours");
            handleSignInResult(result);
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            waitinDialog.dismiss();
            goToHomeActivity("Google");
            Toasty.success(LoginWithActivity.this, R.string.google_login_successfully, Toast.LENGTH_SHORT, true).show();
        } else {
            // If sign in fails, display a message to the user.
            waitinDialog.dismiss();
            Toasty.error(getApplicationContext(), R.string.authentification_failed, Toast.LENGTH_SHORT, true).show();
        }
    }

    private void goToHomeActivity(String loginType) {
        Intent homeIntent = new Intent(LoginWithActivity.this, HomeActivity.class);
        homeIntent.putExtra("LOGIN_TYPE", loginType);
        startActivity(homeIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        goToHomeActivity("Facebook");
        Toasty.success(LoginWithActivity.this, R.string.facebook_login_successfully, Toast.LENGTH_SHORT, true).show();
        finish();
    }


    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            waitinDialog.dismiss();
                            Toast.makeText(LoginWithActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void waitingDialog(Context context, String message) {

        waitinDialog = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(context)
                .setMessage(message)
                .setCancelable(false)
                .build();
        waitinDialog.show();

    }

}