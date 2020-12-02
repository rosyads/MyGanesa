package com.syads.myganesa.treasurer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.syads.myganesa.R;
import com.syads.myganesa.assets.PrefManager;
import com.syads.myganesa.assets.User;
import com.syads.myganesa.teacher.AbsenActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewUsername, textViewRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_treasurer);

        init();
    }

    void init(){
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewRole = findViewById(R.id.textViewRole);

        //getting the current user
        User user = PrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewUsername.setText(user.getUsername());
        textViewRole.setText(user.getRole());

        //when the user presses logout button calling the logout method
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                PrefManager.getInstance(getApplicationContext()).logout();
            }
        });

        findViewById(R.id.buttonBayar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.syads.myganesa.treasurer.ProfileActivity.this,
                        com.syads.myganesa.treasurer.BayarActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonTopUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.syads.myganesa.treasurer.ProfileActivity.this,
                        com.syads.myganesa.treasurer.TopUpActivity.class);
                startActivity(intent);
            }
        });
    }
}