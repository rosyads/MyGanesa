package com.syads.myganesa.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.syads.myganesa.R;
import com.syads.myganesa.assets.User;
import com.syads.myganesa.assets.PrefManager;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewUsername, textViewRole;
    TextView textViewNama, textViewKelas, textViewTgl_lahir, textViewAgama, textViewSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);

        init();

    }

    void init(){
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
    }

}