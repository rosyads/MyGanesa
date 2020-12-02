package com.syads.myganesa.treasurer;

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
import com.syads.myganesa.teacher.AbsenActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BayarActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView qrCodeScanner;
    String nis,kelas,bulan,tahun,type,jam,tgl,bulanBayar;
    int nominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_treasurer);

        init();

    }

    public void init(){
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
                        qrCodeScanner.setResultHandler(BayarActivity.this);
                        qrCodeScanner.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(BayarActivity.this, "Terima Permission ini untuk mengakses kamera.", Toast.LENGTH_SHORT).show();
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
        qrCodeScanner.resumeCameraPreview(BayarActivity.this);
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
        bulan = hasil[2];
        tahun = hasil[3];
        type = hasil[4];
        bulanBayar = hasil[5];
        nominal = Integer.parseInt(hasil[6]);

        SimpleDateFormat sdf = new SimpleDateFormat("E dd-MM-yyyy HH:mm");
        String currentDateandTime = sdf.format(new Date());
        jam = currentDateandTime.substring(15);
        tgl = currentDateandTime.substring(4,6);

        Intent intent = new Intent(BayarActivity.this, KonfirmasiBayarActivity.class);
        intent.putExtra("nis",nis);
        intent.putExtra("kelas",kelas);
        intent.putExtra("tahun",tahun);
        intent.putExtra("bulan",bulan);
        intent.putExtra("tanggal",tgl);
        intent.putExtra("jam",jam);
        intent.putExtra("type",type);
        intent.putExtra("bulanBayar", bulanBayar);
        intent.putExtra("nominal",nominal);
        startActivity(intent);

        qrCodeScanner.resumeCameraPreview(BayarActivity.this);

    }

}