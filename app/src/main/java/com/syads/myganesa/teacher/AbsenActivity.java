package com.syads.myganesa.teacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.syads.myganesa.R;
import com.syads.myganesa.assets.Config;
import com.syads.myganesa.assets.PrefManager;
import com.syads.myganesa.assets.RequestHandler;
import com.syads.myganesa.assets.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AbsenActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView qrCodeScanner;
    String nis,jam,hari,kd_guru,kelas;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_teacher);

        init();
    }

    public void init(){
        //getting the current user
        User user = PrefManager.getInstance(this).getUser();
        kd_guru = user.getKd_guru();

        qrCodeScanner = (ZXingScannerView) findViewById(R.id.qrCodeScanner);
        qrCodeScanner.startCamera();

        RequestPermission();

    }

    private void RequestPermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        qrCodeScanner.setResultHandler(AbsenActivity.this);
                        qrCodeScanner.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(AbsenActivity.this, "Terima Permission ini untuk mengakses kamera.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                    }

                })
                .check();
    }


    @Override
    protected void onResume() {
        super.onResume();
        qrCodeScanner.resumeCameraPreview(AbsenActivity.this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    @Override
    protected void onDestroy() {
        qrCodeScanner.stopCamera();
        super.onDestroy();
    }

    @Override
    public void handleResult(Result rawResult) {
        processRawResult(rawResult.getText());
    }

    private void processRawResult(String text) {
        String[] hasil = text.split("/");
        nis = hasil[0];
        kelas = hasil[1];

        SimpleDateFormat sdf = new SimpleDateFormat("E HH:mm");
        String currentDateandTime = sdf.format(new Date());
        hari = currentDateandTime.substring(0, 3);
        jam = currentDateandTime.substring(4);
        switch (hari) {
            case "Mon":
                hari = "Senin";
                break;
            case "Tue":
                hari = "Selasa";
                break;
            case "Wed":
                hari = "Rabu";
                break;
            case "Thu":
                hari = "Kamis";
                break;
            case "Fri":
                hari = "Jumat";
                break;
            case "Sat":
                hari = "Sabtu";
                break;
            case "Sun":
                hari = "Minggu";
                break;
        }

        builder = new AlertDialog.Builder(this);
        builder.setMessage("NIS: " + nis + " kelas: " + kelas + " kode guru: " + kd_guru + " hari: " + hari + " jam: " + jam)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        absenSiswa();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "Absen dibatalkan", Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alert = builder.create();

        alert.setTitle("Absensi Siswa");
        alert.show();

        qrCodeScanner.resumeCameraPreview(AbsenActivity.this);

    }

    private void absenSiswa(){
        AbsenSiswa absen = new AbsenSiswa(nis,kelas,kd_guru,hari,jam);
        absen.execute();
    }

    class AbsenSiswa extends AsyncTask<Void, Void, String> {
        ProgressBar progressBar;
        String nis, hari, jam, kd_guru, kelas;
        AbsenSiswa(String nis, String kelas, String kd_guru, String hari, String jam) {
            this.nis = nis;
            this.hari = hari;
            this.jam = jam;
            this.kd_guru = kd_guru;
            this.kelas = kelas;
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
                    startActivity(new Intent(getApplicationContext(), com.syads.myganesa.teacher.ProfileActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), "Absensi Gagal", Toast.LENGTH_SHORT).show();
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
            params.put("kd_guru", kd_guru);
            params.put("hari", hari);
            params.put("jam", jam);

            //returing the response
            return requestHandler.sendPostRequest(Config.URL_ABSEN, params);
        }
    }

}