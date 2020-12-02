package com.syads.myganesa.treasurer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.syads.myganesa.R;
import com.syads.myganesa.assets.Config;
import com.syads.myganesa.assets.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class KonfirmasiTopUpActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    TextView tvNis, tvKelas, tvSaldo;
    EditText etSaldo;
    Button btnKonfirmasi, btnCancel;
    String nis, kelas, saldo;
    int  saldoAkhir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_top_up);

        init();

    }

    public void init(){

        tvNis = (TextView) findViewById(R.id.textViewNIS);
        tvKelas = (TextView) findViewById(R.id.textViewKelas);
        tvSaldo = (TextView) findViewById(R.id.textViewSaldo);
        etSaldo = (EditText) findViewById(R.id.editTextSaldo);

        btnKonfirmasi = (Button) findViewById(R.id.buttonTopUp);
        btnCancel = (Button) findViewById(R.id.buttonCancel);

        Intent intent = getIntent();
        nis = intent.getStringExtra("nis");
        kelas = intent.getStringExtra("kelas");
        saldo = intent.getStringExtra("saldo");

        tvNis.setText(nis);
        tvKelas.setText(kelas);
        tvSaldo.setText(saldo);

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tambah = etSaldo.getText().toString();
                saldoAkhir = Integer.parseInt(saldo) + Integer.parseInt(tambah);
                builder = new AlertDialog.Builder(KonfirmasiTopUpActivity.this);
                builder.setMessage(
                        "NIS        : "+nis+
                                "\n Saldo Asal     : "+saldo+
                                "\n Saldo Tambahan :" +tambah+
                                "\n Saldo Akhir    : "+String.valueOf(saldoAkhir))
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                topUpSiswa();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "Top Up Dibatalkan", Toast.LENGTH_LONG).show();
                            }
                        });
                AlertDialog alert = builder.create();

                alert.setTitle("Top Up Saldo Siswa");
                alert.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void topUpSiswa(){
        KonfirmasiTopUpActivity.topUpSiswa topUp = new KonfirmasiTopUpActivity.topUpSiswa(nis,kelas,saldoAkhir);
        topUp.execute();
    }

    class topUpSiswa extends AsyncTask<Void, Void, String> {
        ProgressBar progressBar;
        String nis, kelas, nominal;
        topUpSiswa(String nis, String kelas, int nominal) {
            this.nis = nis;
            this.kelas = kelas;
            this.nominal = String.valueOf(nominal);
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
                    finish();
                    startActivity(new Intent(getApplicationContext(), com.syads.myganesa.treasurer.ProfileActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            params.put("nis", nis);
            params.put("kelas", kelas);
            params.put("nominal", nominal);

            //returing the response
            return requestHandler.sendPostRequest(Config.URL_TOP_UP, params);
        }
    }

}