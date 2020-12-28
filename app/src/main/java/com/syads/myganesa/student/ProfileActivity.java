package com.syads.myganesa.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.syads.myganesa.MainActivity;
import com.syads.myganesa.R;
import com.syads.myganesa.assets.Config;
import com.syads.myganesa.assets.RequestHandler;
import com.syads.myganesa.assets.User;
import com.syads.myganesa.assets.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewUsername, textViewRole;
    TextView textViewNama, textViewKelas, textViewTgl_lahir, textViewAgama, textViewSaldo;
    Runnable refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);

        init();

    }

    void init(){

        Handler handler = new Handler();

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewRole = findViewById(R.id.textViewRole);
        textViewNama = findViewById(R.id.textViewName);
        textViewKelas = findViewById(R.id.textViewKelas);
        textViewTgl_lahir = findViewById(R.id.textViewTglLahir);
        textViewAgama = findViewById(R.id.textViewAgama);
        textViewSaldo = findViewById(R.id.textViewSaldo);

        //getting the current user
        User user = PrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewUsername.setText(user.getUsername());
        textViewRole.setText(user.getRole());
        textViewNama.setText(user.getNama());
        textViewKelas.setText(user.getKelas());
        textViewTgl_lahir.setText(user.getTgl_lahir());
        textViewAgama.setText(user.getAgama());
        String saldo = String.format("Rp. %,d", user.getSaldo());
        textViewSaldo.setText(saldo);

        //when the user presses logout button calling the logout method
        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                PrefManager.getInstance(getApplicationContext()).logout();
            }
        });

        findViewById(R.id.btnJadwal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, TampilJadwal.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnAbsen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AbsenActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnBayarSpp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, BayarActivity.class);
                intent.putExtra("type","SPP");
                startActivity(intent);
            }
        });

        findViewById(R.id.btnHistoryBayar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HistoryBayarActivity.class);
                startActivity(intent);
            }
        });

//        findViewById(R.id.btnBayarUP).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProfileActivity.this, BayarActivity.class);
//                intent.putExtra("type","UP");
//                startActivity(intent);
//            }
//        });

        findViewById(R.id.btnTopUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, TopUpActivity.class);
                startActivity(intent);
            }
        });

        refresh = new Runnable() {
            @Override
            public void run() {
                ProfileActivity.UserRefresh userRefresh = new ProfileActivity.UserRefresh(user.getUsername());
                userRefresh.execute();

                handler.postDelayed(refresh,5000);
            }
        };
        handler.post(refresh);

    }

    class UserRefresh extends AsyncTask<Void, Void, String> {
        ProgressBar progressBar;
        String username, password,role;
        UserRefresh(String username) {
            this.username = username;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")) {

                    //getting the user from the response
                    JSONObject userJson = obj.getJSONObject("user");

                    if(userJson.getString("role").equals("011")){
                        //creating a new user object
                        User student = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("role"),
                                userJson.getString("nama"),
                                userJson.getString("kelas"),
                                userJson.getString("tgl_lahir"),
                                userJson.getString("agama"),
                                userJson.getString("id_kelas"),
                                userJson.getInt("saldo")
                        );
                        //storing the user in shared preferences
                        PrefManager.getInstance(getApplicationContext()).setUserLogin(student,"011");

                        textViewUsername.setText(userJson.getString("username"));
                        textViewRole.setText(userJson.getString("role"));
                        textViewNama.setText(userJson.getString("nama"));
                        textViewKelas.setText(userJson.getString("kelas"));
                        textViewTgl_lahir.setText(userJson.getString("tgl_lahir"));
                        textViewAgama.setText(userJson.getString("agama"));
                        int saldoSiswa = userJson.getInt("saldo");
                        String saldo = String.format("Rp. %,d", saldoSiswa);
                        textViewSaldo.setText(saldo);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);

            //returing the response
            return requestHandler.sendPostRequest(Config.URL_REFRESH, params);
        }
    }

}