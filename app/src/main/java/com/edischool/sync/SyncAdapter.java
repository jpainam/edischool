package com.edischool.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.edischool.R;
import com.edischool.absences.AbsenceDao;
import com.edischool.emplois.EmploiDao;
import com.edischool.manuelscolaires.ManuelscolaireDao;
import com.edischool.notes.NotesDao;
import com.edischool.pojo.Absence;
import com.edischool.pojo.Emploi;
import com.edischool.pojo.Manuelscolaire;
import com.edischool.pojo.Note;
import com.edischool.pojo.Student;
import com.edischool.pojo.Week;
import com.edischool.student.StudentDao;
import com.edischool.timetable.TimeTableDao;
import com.edischool.utils.Constante;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dmax.dialog.SpotsDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";
    AlertDialog dialog = null;
    ContentResolver contentResolver;


    public SyncAdapter(Context context, boolean autoInitialize) {

        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        contentResolver = context.getContentResolver();
        dialog = new SpotsDialog.Builder()
                .setContext(getContext()).setCancelable(true)
                .setMessage(getContext().getString(R.string.loading_message))
                .build();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(Context context, boolean autoInitialize,
                       boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        contentResolver = context.getContentResolver();
        dialog = new SpotsDialog.Builder()
                .setContext(getContext()).setCancelable(true)
                .setMessage(getContext().getString(R.string.loading_message))
                .build();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Intent intent = new Intent();
        intent.setAction(getContext().getString(R.string.SYNC_STATUS_ACTION));
        intent.putExtra(getContext().getString(R.string.sync_status), getContext().getString(R.string.sync_running));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);


        Log.i(TAG, "Beginning network synchronization");
        /**
         * Get the last sync time used in the database to return all data
         * create after last sync time. Fetch from the local database
         */

        String jsonData = donwloadData();
        if(jsonData != null) {
            Log.e(TAG, jsonData);
            updateLocalDatabase(jsonData);
            Log.i(TAG, "Streaming completed");
            /**
             * Send a broadcast to update list view
             */
        }

        intent = new Intent();
        intent.setAction(getContext().getString(R.string.SYNC_STATUS_ACTION));
        intent.putExtra(getContext().getString(R.string.sync_status), getContext().getString(R.string.sync_finished));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

    }



    public String donwloadData() {
        SharedPreferences pref = getContext().getSharedPreferences(
                getContext().getString(R.string.shared_preference_file),
                Context.MODE_PRIVATE);
        String phone_number = pref.getString(
                getContext().getString(R.string.phone_number), null);
        String lastSyncTime = pref.getString(
                getContext().getString(R.string.last_time_sync), "");
        String mToken = pref.getString(
                getContext().getString(R.string.firebase_token), "");

        String url = getContext().getString(R.string.sync_url);

        Log.w(TAG, mToken);
        Log.i(TAG, "Last time Sync " + lastSyncTime + " from " + url + " " + phone_number);
        OkHttpClient client = new OkHttpClient();
        RequestBody body =new FormBody.Builder()
                .add("phone_number", phone_number)
                .add("last_time_sync", lastSyncTime)
                .add("token", mToken)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response;
        ResponseBody responseBody = null;
        String jsonData = null;
        try {
            response = client.newCall(request).execute();
            responseBody = response.body();
            jsonData = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }finally {
            if(responseBody != null) {
                responseBody.close();
            }
        }

        return jsonData;
    }

    public void updateLocalDatabase(String jsonData) {
        if (jsonData != null) {
            if (jsonData != "404") {
                try {
                    JSONObject data = new JSONObject(jsonData);
                    boolean success = data.getBoolean("success");
                    if (success) {
                        Log.i(TAG, "Updating Local Database");
                        JSONArray studentData = data.getJSONArray("students");
                        syncStudent(studentData);
                        syncNotes(data.getJSONArray("notes"));
                        syncManuelscolaires(data.getJSONArray("manuels"));
                        syncAbsences(data.getJSONArray("absences"));
                        syncEmplois(data.getJSONArray("emplois"));
                        syncTimetable(data.getJSONArray("timetable"));
                    }
                    SharedPreferences pref = getContext().getSharedPreferences(
                            getContext().getString(R.string.shared_preference_file),
                            Context.MODE_PRIVATE);
                    /**
                     * Update last time sync
                     */
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentTime = sdf.format(new Date());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(getContext().getString(R.string.last_time_sync), currentTime);
                    editor.apply();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    Log.e(TAG, ex.getMessage());
                }
            }
        }
    }

    private void syncEmplois(JSONArray data) throws JSONException{
        EmploiDao dao = new EmploiDao(getContext());
        if(data.length() > 0){
            dao.emptyTable();
        }
        for(int i = 0; i < data.length(); i++){
            JSONObject item = data.getJSONObject(i);
            Emploi n = new Emploi();
            n.setIdemploi(item.getInt("id"));
            n.setIdstudent(item.getInt("id_student"));
            n.setHour(item.getString("hour"));
            n.setMatiere(item.getString("matiere"));
            n.setDay(item.getString("day"));
            dao.insert(n);
        }
    }
    private void syncNotes(JSONArray data) throws JSONException{
        NotesDao dao = new NotesDao(getContext());
        if(data.length() > 0){
            dao.emptyTable();
        }
        for(int i = 0; i < data.length(); i++){
            JSONObject item = data.getJSONObject(i);
            Note n = new Note();
            n.setIdnote(item.getInt("id"));
            n.setIdstudent(item.getInt("id_student"));
            n.setNote(item.getString("note"));
            n.setMatiere(item.getString("matiere"));
            n.setObservation(item.getString("observation"));
            n.setSequence(item.getString("sequence"));
            dao.insert(n);
        }
    }
    private void syncManuelscolaires(JSONArray data) throws JSONException{
        ManuelscolaireDao dao = new ManuelscolaireDao(getContext());
        if(data.length() > 0){
            dao.emptyTable();
        }
        for(int i = 0; i < data.length(); i++){
            JSONObject item = data.getJSONObject(i);
            Manuelscolaire m = new Manuelscolaire();
            m.setIdmanuel(item.getInt("id"));
            m.setTitre(item.getString("title"));
            m.setEditeurs(item.getString("editors"));
            m.setAuteurs(item.getString("authors"));
            m.setMatiere(item.getString("subject"));
            m.setIdstudent(item.getInt("id_student"));
            dao.insert(m);
        }
    }
    private void syncAbsences(JSONArray data) throws JSONException{
        AbsenceDao dao = new AbsenceDao(getContext());
        if(data.length() > 0){
            dao.emptyTable();
        }
        for(int i = 0; i < data.length(); i++){
            JSONObject item = data.getJSONObject(i);
            Absence a = new Absence();
            //a.setIdabsence(item.getInt("id"));
            //a.setIdstudent(item.getInt("id_student"));
            a.setStartHour(item.getString("start_hour"));
            a.setEndHour(item.getString("end_hour"));
            a.setDay(item.getString("day"));
            dao.insert(a);
        }
    }
    private void syncTimetable(JSONArray data) throws JSONException{
        TimeTableDao dao = new TimeTableDao(getContext());
        if(data.length() > 0){
            dao.emptyTable();
        }
        for(int i = 0; i < data.length(); i++){
            JSONObject item = data.getJSONObject(i);
            Week a = new Week();
            a.setId(item.getInt("id"));
            a.setSubject(item.getString("subject"));
            a.setFragment(item.getString("day"));
            a.setTeacher(item.getString("teacher"));
            a.setRoom(item.getString("room"));
            a.setFromTime(item.getString("fromtime"));
            a.setToTime(item.getString("totime"));
            a.setColor(item.getString("color"));
            dao.insertWeek(a);
        }
    }
    private void syncStudent(JSONArray data) throws JSONException {
        StudentDao dao = new StudentDao(getContext());
        if (data.length() > 0) {
            dao.emptyTable();
        }
        for (int i = 0; i < data.length(); i++) {
            Log.i(TAG, "Syncing Student");
            JSONObject item = data.getJSONObject(i);
            int id = Integer.parseInt(item.getString("id"));
            String firstname = item.getString("firstname");
            String lastname = item.getString("lastname");
            String sexe = item.getString("sex");
            String etablissement = item.getString("etablissement");
            String classe = item.getString("classe");
            String photo = item.getString("photo");
            if (photo == "" || photo == null) {
                if (sexe.equals("M")) {
                    photo = Constante.MALE_AVATAR;
                } else {
                    photo = Constante.FEMALE_AVATAR;
                }
            }
            final Student st = new Student(id, firstname, lastname, sexe,
                    classe, etablissement);
            final String path = Constante.SERVER + "eschool" + "/" + Constante.IMG_DIR
                    + "/" + photo;
            Log.i(TAG, path);
            st.setPhoto(path);
            dao.insert(st);
            /**
             * Download the student image
             */

            //String filename = path.substring(path.lastIndexOf("/")+  1);
        }
    }

    public static  void doManualSyncAsync(Context context){
        AccountManager accManager = AccountManager.get(context);
        Account account = accManager.getAccountsByType(Constante.ACCOUNT_TYPE)[0];
        if(account != null) {
            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            Log.i(TAG, "Start Sync Finish");
            ContentResolver.requestSync(account, Constante.AUTHORITY, settingsBundle);
            Log.i(TAG, "Manual Sync Finish");
        }
    }
}
