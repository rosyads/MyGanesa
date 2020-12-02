package com.syads.myganesa.student;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.syads.myganesa.R;
import com.syads.myganesa.assets.PrefManager;
import com.syads.myganesa.assets.User;

public class TopUpActivity extends AppCompatActivity {

    public final static int QRcodeWidth = 500;
    ImageView qrCode;
    Bitmap bitmap;
    String content;
    int saldo;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_student);

        init();

    }

    public void init(){
        //getting the current user
        user = PrefManager.getInstance(this).getUser();

        saldo = user.getSaldo();

        generateQR();

    }

    public void generateQR(){
        qrCode = (ImageView) findViewById(R.id.imageViewQrCode);
        content = user.getUsername()+"/"+user.getKelas()+"/"+saldo;

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