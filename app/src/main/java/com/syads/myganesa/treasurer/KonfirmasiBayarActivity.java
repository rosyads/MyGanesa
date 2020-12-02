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

public class KonfirmasiBayarActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    TextView tvNis, tvKelas, tvTanggal, tvJam, tvType, tvBulanBayar, tvDana;
    Button btnKonfirmasi, btnCancel;
    String nis,kelas,bulan,tahun,type,jam,tgl,bulanBayar;
    int nominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_bayar);

        init();

    }

    public void init(){

        tvNis = (TextView) findViewById(R.id.textViewNIS);
        tvKelas = (TextView) findViewById(R.id.textViewKelas);
        tvTanggal = (TextView) findViewById(R.id.textViewTanggal);
        tvJam = (TextView) findViewById(R.id.textViewJam);
        tvType = (TextView) findViewById(R.id.textViewAlokasi);
        tvBulanBayar = (TextView) findViewById(R.id.textViewBulanBayar);
        tvDana = (TextView) findViewById(R.id.textViewDana);

        btnKonfirmasi = (Button) findViewById(R.id.buttonBayar);
        btnCancel = (Button) findViewById(R.id.buttonCancel);

        Intent intent = getIntent();
        nis = intent.getStringExtra("nis");
        kelas = intent.getStringExtra("kelas");
        tahun = intent.getStringExtra("tahun");
        bulan = intent.getStringExtra("bulan");
        tgl = intent.getStringExtra("tanggal");
        jam = intent.getStringExtra("jam");
        type = intent.getStringExtra("type");
        bulanBayar = intent.getStringExtra("bulanBayar");
        nominal = intent.getIntExtra("nominal", 0);

        String tanggal = tgl+"-"+bulan+"-"+tahun;

        tvNis.setText(nis);
        tvKelas.setText(kelas);
        tvTanggal.setText(tanggal);
        tvJam.setText(jam);
        tvType.setText(type);
        tvBulanBayar.setText(bulanBayar);
        String dana = String.valueOf(nominal);
        tvDana.setText(dana);

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(KonfirmasiBayarActivity.this);
                builder.setMessage(
                            "NIS        : "+nis+
                            "\n Pembayaran : "+type+
                            "\n Bulan      :" +bulan+
                            "\n Nominal    : "+nominal)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                pembayaranSiswa();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "Pembayaran Dibatalkan", Toast.LENGTH_LONG).show();
                            }
                        });
                AlertDialog alert = builder.create();

                alert.setTitle("Pembayaran "+type);
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

    private void pembayaranSiswa(){
        KonfirmasiBayarActivity.pembayaranSiswa bayar = new KonfirmasiBayarActivity.pembayaranSiswa(nis,kelas,tahun,bulan,tgl,jam,type,nominal,bulanBayar);
        bayar.execute();
    }

    class pembayaranSiswa extends AsyncTask<Void, Void, String> {
        ProgressBar progressBar;
        String nis, kelas, tahun, bulan, tgl, jam, type, nominal,bulanBayar;
        pembayaranSiswa(String nis, String kelas, String tahun, String bulan, String tanggal, String jam, String type, int nominal, String bulanBayar) {
            this.nis = nis;
            this.kelas = kelas;
            this.tahun = tahun;
            this.bulan = bulan;
            this.tgl = tanggal;
            this.jam = jam;
            this.type = type;
            this.nominal = String.valueOf(nominal);
            this.bulanBayar = bulanBayar;
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
            params.put("tahun", tahun);
            params.put("bulan", bulan);
            params.put("tanggal", tgl);
            params.put("jam", jam);
            params.put("type", type);
            params.put("bulanBayar", bulanBayar);
            params.put("nominal", nominal);

            //returing the response
            return requestHandler.sendPostRequest(Config.URL_BAYAR, params);
        }
    }

}