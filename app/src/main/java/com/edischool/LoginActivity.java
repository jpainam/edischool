package com.edischool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.edischool.utils.Constante;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;




public class LoginActivity extends AppCompatActivity{
    private static final int AUTHENTIFICATION_SUCCESS = 1;
    private static String TAG = "LoginActivity";
    private UserLoginTask mAuthTask = null;
    private FirebaseAuth mAuth = null;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    boolean verificationInProgress = false;
    AlertDialog dialog = null;
    Context context;
    private EditText mPhoneNumberView;
    private EditText mPasswordView;
    LinearLayout rootView;
    String phoneNumber;

    private static final int CONNEXION_TIMEOUT = 2;
    private static final int CONNEXION_FAILED = 3;
    private static final int AUTHENTIFICATION_FAILED = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mPhoneNumberView =  findViewById(R.id.phone_number);
        rootView = findViewById(R.id.rootView);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        context = getApplicationContext();

        if(dialog == null) {
            dialog = new SpotsDialog.Builder()
                    .setContext(LoginActivity.this).setCancelable(true)
                    .setMessage(getString(R.string.connecting))
                    .build();
        }
        TextView mEmailSignInButton =  findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            Log.e(TAG, "Manually setting drawableLeft");
            //Drawable drawable = AppCompatResources.getDrawable(context, R.drawable.ic_action_phone);
            //Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.ic_action_phone);
            //mPhoneNumberView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
            // Idem syntaxe
            //mPasswordView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_action_pass, 0, 0, 0);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(verificationInProgress){
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }
    }
    private void startPhoneNumberVerification(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
        verificationInProgress = true;
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
            String code = phoneAuthCredential.getSmsCode();
            // Sometimes the code is not detected automatically
            if(code != null){
                mPasswordView.setText(code);
                verifyVerificationCode(code);
            }else{
                Log.e(TAG, "Should not enter here - Let's try");
            }
        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verificationId, forceResendingToken);
            Log.d(TAG, "onCodeSent:" + verificationId);
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = forceResendingToken;
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Log.e(TAG, "Invalid request", e);
                Snackbar.make(rootView, "Invalid request", Snackbar.LENGTH_LONG).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Log.e(TAG, "The SMS quota for the project has been exceeded", e);
                Snackbar.make(rootView, "The SMS quota for the project has been exceeded", Snackbar.LENGTH_LONG).show();
            }
        }
    };
    private void verifyVerificationCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = task.getResult().getUser();
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                }else{
                    Log.e(TAG, "signInWithCredential:failure", task.getException());
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        mPasswordView.setError("Invalid code");
                        Snackbar.make(rootView, "Invalid code entered", Snackbar.LENGTH_LONG).show();
                    }else{
                        Snackbar.make(rootView, "Something is wrong, we will fix it soon", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationInProgress = savedInstanceState.getBoolean("verificationInProgress");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("verificationInProgress", verificationInProgress);
    }



    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mPhoneNumberView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phone_number = mPhoneNumberView.getText().toString();
        String password = mPasswordView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid phone number address.
        if (TextUtils.isEmpty(phone_number)) {
            mPhoneNumberView.setError(getString(R.string.error_field_required));
            focusView = mPhoneNumberView;
            cancel = true;
        } else if (!isPhoneNumberValid(phone_number)) {
            mPhoneNumberView.setError(getString(R.string.error_invalid_email));
            focusView = mPhoneNumberView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new UserLoginTask(phone_number, password);
            mAuthTask.execute((Void) null);
        }
    }


    private boolean isPhoneNumberValid(String phone_number) {
        //TODO: Replace this with your own logic
        return phone_number.length() > Constante.PHONE_NUMBER_LENGTH;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mPhone;
        private final String mPassword;
        private String mToken;
        private SharedPreferences pref;

        UserLoginTask(String email, String password) {
            mPhone = email;
            mPassword = password;
            pref = getApplicationContext().getSharedPreferences(
                    getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
            mToken = pref.getString(getString(R.string.firebase_token), "");
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String url = getString(R.string.login_url);
            OkHttpClient client =new OkHttpClient();
            Log.e(TAG, url);
            Log.i(TAG, mPhone + " " + mPassword);
            RequestBody body =new FormBody.Builder()
                    .add("phonenumber", mPhone)
                    .add("password", mPassword)
                    .add("token", mToken)
                    .build();
            Request newReq=new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            ResponseBody responseBody = null;
            try {
                Response response = client.newCall(newReq).execute();
                responseBody = response.body();
                String jsonData = responseBody.string();
                Log.e(TAG, jsonData);
                JSONObject data = new JSONObject(jsonData);
                boolean success = data.getBoolean("success");
                /**
                 * register the new account here and do something with the jsonData
                 */
                if (success) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(getString(R.string.phone_number), mPhone);
                    editor.apply();
                    return AUTHENTIFICATION_SUCCESS;
                } else {
                    return AUTHENTIFICATION_FAILED;
                }
            }catch (SocketTimeoutException ex){
                Log.e(TAG, ex.getMessage());
                ex.printStackTrace();
                return CONNEXION_TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
                return CONNEXION_FAILED;
            }finally {
                if(responseBody != null){
                    responseBody.close();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog != null) {
                dialog.show();
            }
        }

        @Override
        protected void onPostExecute(final Integer status) {
            mAuthTask = null;
            if (dialog != null) {
                dialog.dismiss();
            }
            if (status == AUTHENTIFICATION_SUCCESS) {

                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();
            } else if(status == AUTHENTIFICATION_FAILED){
                //Toast.makeText(context, getString(R.string.error_connexion), Toast.LENGTH_LONG).show();
                Snackbar.make(rootView, getString(R.string.authentification_failed), Snackbar.LENGTH_LONG).show();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }else if(status == CONNEXION_FAILED){
                Snackbar.make(rootView, getString(R.string.connexion_failed), Snackbar.LENGTH_LONG).show();
            }else if(status == CONNEXION_TIMEOUT){
                Snackbar.make(rootView, getString(R.string.connexion_timeout), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            dialog.dismiss();
        }
    }
}

