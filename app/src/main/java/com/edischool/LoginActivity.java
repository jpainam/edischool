package com.edischool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import android.graphics.drawable.Drawable;
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

import com.edischool.utils.Constante;

import org.json.JSONObject;


import java.net.SocketTimeoutException;

import dmax.dialog.SpotsDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    private static final int AUTHENTIFICATION_SUCCESS = 1;
    private static String TAG = "LoginActivity";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    AlertDialog dialog = null;
    Context context;
    // UI references.
    private EditText mPhoneNumberView;
    private EditText mPasswordView;
    LinearLayout rootView;
    private static final int CONNEXION_TIMEOUT = 2;
    private static final int CONNEXION_FAILED = 3;
    private static final int AUTHENTIFICATION_FAILED = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Set up the login form.
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



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
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

