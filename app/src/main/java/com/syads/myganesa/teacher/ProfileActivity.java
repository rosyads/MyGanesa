package com.syads.myganesa.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.syads.myganesa.R;
import com.syads.myganesa.assets.User;
import com.syads.myganesa.assets.PrefManager;
import com.syads.myganesa.student.TampilJadwal;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewUsername, textViewRole;
    TextView textViewNama, textViewKdGuru, textViewNamaMatpel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_teacher);

        init();
    }

    void init(){
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewRole = findViewById(R.id.textViewRole);
        textViewNama = findViewById(R.id.textViewName);
        textViewKdGuru = findViewById(R.id.textViewKdGuru);
        textViewNamaMatpel = findViewById(R.id.textViewNamaMatpel);

        //getting the current user
        User user = PrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewUsername.setText(user.getUsername());
        textViewRole.setText(user.getRole());
        textViewNama.setText(user.getNama());
        textViewKdGuru.setText(user.getKd_guru());
        textViewNamaMatpel.setText(user.getNama_matpel());

        //when the user presses logout button calling the logout method
        findViewById(R.id.buttonLogout).setOnClickListener(view -> {
            finish();
            PrefManager.getInstance(getApplicationContext()).logout();
        });

        findViewById(R.id.buttonAbsen).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AbsenActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.buttonDaftarAbsen).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, DaftarAbsenActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.buttonRekapAbsen).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, RekapAbsenActivity.class);
            startActivity(intent);
        });

    }

}