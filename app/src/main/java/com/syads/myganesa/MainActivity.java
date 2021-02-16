package com.syads.myganesa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.syads.myganesa.assets.Config;
import com.syads.myganesa.assets.PrefManager;
import com.syads.myganesa.assets.RequestHandler;
import com.syads.myganesa.assets.User;
import com.syads.myganesa.teacher.ProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText etUsername,etPassword;
    RadioGroup roleGroup;
    RadioButton roleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    void init(){
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        roleGroup = (RadioGroup) findViewById(R.id.radiogroup);

        findViewById(R.id.radioBendahara).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

        findViewById(R.id.radioGuru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        findViewById(R.id.radioSiswa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        findViewById(R.id.radioWaliSiswa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        PrefManager prefManager = PrefManager.getInstance(MainActivity.this);
        if(prefManager.isLoggedIn()) {
            String role = prefManager.getUser().getRole();
            if(role.equals("011")){
                Intent intent = new Intent(MainActivity.this, com.syads.myganesa.student.ProfileActivity.class);
                startActivity(intent);
            }else if(role.equals("101")) {
                Intent intent = new Intent(MainActivity.this, com.syads.myganesa.teacher.ProfileActivity.class);
                startActivity(intent);
            }else if(role.equals("010")) {
                Intent intent = new Intent(MainActivity.this, com.syads.myganesa.treasurer.ProfileActivity.class);
                startActivity(intent);
            }else if(role.equals("100")) {
                Intent intent = new Intent(MainActivity.this, com.syads.myganesa.guardians.ProfileActivity.class);
                startActivity(intent);
            }
        }

    }

    private void userLogin() {
        //first getting the values
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        int radioId = roleGroup.getCheckedRadioButtonId();
        roleButton = findViewById(radioId);
        final String role = roleButton.getText().toString();
        //validating inputs
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Please enter username");
            etUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter password");
            etPassword.requestFocus();
            return;
        }
        //if everything is fine
        UserLogin ul = new UserLogin(username,password,role);
        ul.execute();
    }
    class UserLogin extends AsyncTask<Void, Void, String> {
        ProgressBar progressBar;
        String username, password,role;
        UserLogin(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
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
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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
                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), com.syads.myganesa.student.ProfileActivity.class));

                    }else if(userJson.getString("role").equals("101")){
                        User teacher = new User(userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("role"),
                                userJson.getString("nama"),
                                userJson.getString("kd_guru"),
                                userJson.getString("nama_matpel")
                        );
                        //storing the user in shared preferences
                        PrefManager.getInstance(getApplicationContext()).setUserLogin(teacher, "101");
                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), com.syads.myganesa.teacher.ProfileActivity.class));
                    }else if(userJson.getString("role").equals("010")){
                        User treasurer = new User(userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("role")
                        );
                        //storing the user in shared preferences
                        PrefManager.getInstance(getApplicationContext()).setUserLogin(treasurer, "010");
                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), com.syads.myganesa.treasurer.ProfileActivity.class));
                    }else if(userJson.getString("role").equals("100")){
                        //creating a new user object
                        User guardians = new User(
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
                        PrefManager.getInstance(getApplicationContext()).setUserLogin(guardians,"100");
                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), com.syads.myganesa.guardians.ProfileActivity.class));

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
            params.put("password", password);
            params.put("role", role);

            //returing the response
            return requestHandler.sendPostRequest(Config.URL_LOGIN, params);
        }
    }

}