package com.syads.myganesa.treasurer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.syads.myganesa.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class TopUpActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView qrCodeScanner;
    String nis,kelas,saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_treasurer);

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
                        qrCodeScanner.setResultHandler(TopUpActivity.this);
                        qrCodeScanner.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(TopUpActivity.this, "Terima Permission ini untuk mengakses kamera.", Toast.LENGTH_SHORT).show();
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
        qrCodeScanner.resumeCameraPreview(TopUpActivity.this);
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
        saldo = hasil[2];

        Intent intent = new Intent(TopUpActivity.this, KonfirmasiTopUpActivity.class);
        intent.putExtra("nis",nis);
        intent.putExtra("kelas",kelas);
        intent.putExtra("saldo",saldo);
        startActivity(intent);

        qrCodeScanner.resumeCameraPreview(TopUpActivity.this);

    }

}