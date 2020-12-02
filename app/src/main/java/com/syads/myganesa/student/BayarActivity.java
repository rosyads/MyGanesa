package com.syads.myganesa.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.syads.myganesa.R;
import com.syads.myganesa.assets.PrefManager;
import com.syads.myganesa.assets.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BayarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public final static int QRcodeWidth = 500;
    ImageView qrCode;
    Bitmap bitmap;
    String content, bulan, tahun, type, bulanBayar;
    int saldo;
    User user;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_student);
        init();
    }

    public void init(){
        Intent intent = getIntent();
        //getting the current user
        user = PrefManager.getInstance(this).getUser();

        saldo = user.getSaldo();

        type = intent.getStringExtra("type");

        SimpleDateFormat sdf = new SimpleDateFormat("E dd-MM-yyyy HH:mm");
        String currentDateandTime = sdf.format(new Date());
        bulan = currentDateandTime.substring(7,9);
        tahun = currentDateandTime.substring(10,14);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerBulan);

        spinner.setOnItemSelectedListener(this);

        List<String> bulan = new ArrayList<String>();
        bulan.add("Januari");
        bulan.add("Februari");
        bulan.add("Maret");
        bulan.add("April");
        bulan.add("Mei");
        bulan.add("Juni");
        bulan.add("Juli");
        bulan.add("Agustus");
        bulan.add("September");
        bulan.add("Oktober");
        bulan.add("November");
        bulan.add("Desember");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bulan);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        findViewById(R.id.btnConfirmBayar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saldo < 200000){
                    builder = new AlertDialog.Builder(BayarActivity.this);
                    builder.setMessage("Saldo tidak mencukupi! Silakan lakukan top up pada bendahara")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();

                    alert.setTitle("Pembayaran SPP");
                    alert.show();
                }else{
                    confirmBayar();
                }
            }
        });

    }

    public void confirmBayar(){
        qrCode = (ImageView) findViewById(R.id.imageViewQrCode);
        content = user.getUsername()+"/"+user.getKelas()+"/"+bulan+"/"+tahun+"/"+type+"/"+bulanBayar+"/200000";

        try {
            bitmap = TextToImageEncode(content);
            qrCode.setImageBitmap(bitmap);

            Button btnKembali = (Button) findViewById(R.id.btnKembali);
            btnKembali.setVisibility(View.VISIBLE);
            btnKembali.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        bulanBayar = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}