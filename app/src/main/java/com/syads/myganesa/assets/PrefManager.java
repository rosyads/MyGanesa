package com.syads.myganesa.assets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.syads.myganesa.MainActivity;

public class PrefManager {
    private static final String SHARED_PREF_NAME = "apippref";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_ROLE = "user_role";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAMA = "user_nama";
    //Siswa
    private static final String KEY_KELAS = "user_kelas";
    private static final String KEY_TGL_LAHIR = "user_tgl_lahir";
    private static final String KEY_AGAMA = "user_agama";
    private static final String KEY_ID_KELAS = "user_id_kelas";
    private static final String KEY_SALDO = "user_saldo";
    //Guru
    private static final String KEY_KD_GURU = "user_kd_guru";
    private static final String KEY_NAMA_MATPEL = "user_nama_matpel";

    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static PrefManager mInstance;
    private static Context mCtx;

    private PrefManager (Context context) {
        mCtx = context;
    }

    public static synchronized PrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefManager(context);
        }
        return mInstance;
    }
    public void setUserLogin(User user, String role) {
        switch (role) {
            case "101": { //Guru
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(KEY_ID, user.getId());
                editor.putString(KEY_ROLE, user.getRole());
                editor.putString(KEY_USERNAME, user.getUsername());
                editor.putString(KEY_NAMA, user.getNama());
                editor.putString(KEY_KD_GURU, user.getKd_guru());
                editor.putString(KEY_NAMA_MATPEL, user.getNama_matpel());
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.apply();
                break;
            }
            case "011": { //Siswa
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(KEY_ID, user.getId());
                editor.putString(KEY_ROLE, user.getRole());
                editor.putString(KEY_USERNAME, user.getUsername());
                editor.putString(KEY_NAMA, user.getNama());
                editor.putString(KEY_KELAS, user.getKelas());
                editor.putString(KEY_TGL_LAHIR, user.getTgl_lahir());
                editor.putString(KEY_AGAMA, user.getAgama());
                editor.putString(KEY_ID_KELAS, user.getId_kelas());
                editor.putInt(KEY_SALDO, user.getSaldo());
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.apply();
                break;
            }
            case "100": { //Wali Siswa
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(KEY_ID, user.getId());
                editor.putString(KEY_ROLE, user.getRole());
                editor.putString(KEY_USERNAME, user.getUsername());
                editor.putString(KEY_NAMA, user.getNama());
                editor.putString(KEY_KELAS, user.getKelas());
                editor.putString(KEY_TGL_LAHIR, user.getTgl_lahir());
                editor.putString(KEY_AGAMA, user.getAgama());
                editor.putString(KEY_ID_KELAS, user.getId_kelas());
                editor.putInt(KEY_SALDO, user.getSaldo());
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.apply();
                break;
            }
            case "010": { //Bendahara
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(KEY_ID, user.getId());
                editor.putString(KEY_ROLE, user.getRole());
                editor.putString(KEY_USERNAME, user.getUsername());
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.apply();
                break;
            }
        }
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        switch (sharedPreferences.getString(KEY_ROLE, null)) {
            case "011": //Siswa
                return new User(
                        sharedPreferences.getInt(KEY_ID, -1),
                        sharedPreferences.getString(KEY_USERNAME, null),
                        sharedPreferences.getString(KEY_ROLE, null),
                        sharedPreferences.getString(KEY_NAMA, null),
                        sharedPreferences.getString(KEY_KELAS, null),
                        sharedPreferences.getString(KEY_TGL_LAHIR, null),
                        sharedPreferences.getString(KEY_AGAMA, null),
                        sharedPreferences.getString(KEY_ID_KELAS, null),
                        sharedPreferences.getInt(KEY_SALDO,0)
                );
            case "100": //Wali Siswa
                return new User(
                        sharedPreferences.getInt(KEY_ID, -1),
                        sharedPreferences.getString(KEY_USERNAME, null),
                        sharedPreferences.getString(KEY_ROLE, null),
                        sharedPreferences.getString(KEY_NAMA, null),
                        sharedPreferences.getString(KEY_KELAS, null),
                        sharedPreferences.getString(KEY_TGL_LAHIR, null),
                        sharedPreferences.getString(KEY_AGAMA, null),
                        sharedPreferences.getString(KEY_ID_KELAS, null),
                        sharedPreferences.getInt(KEY_SALDO,0)
                );
            case "101": //Guru
                return new User(
                        sharedPreferences.getInt(KEY_ID, -1),
                        sharedPreferences.getString(KEY_USERNAME, null),
                        sharedPreferences.getString(KEY_ROLE, null),
                        sharedPreferences.getString(KEY_NAMA, null),
                        sharedPreferences.getString(KEY_KD_GURU, null),
                        sharedPreferences.getString(KEY_NAMA_MATPEL, null)
                );
            case "010": //Bendahara
                return new User(
                        sharedPreferences.getInt(KEY_ID, -1),
                        sharedPreferences.getString(KEY_USERNAME, null),
                        sharedPreferences.getString(KEY_ROLE, null)
                );
            default:
                return null;
        }
    }
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, MainActivity.class));
    }
}

