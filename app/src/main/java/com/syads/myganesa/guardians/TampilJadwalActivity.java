package com.syads.myganesa.guardians;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.syads.myganesa.R;
import com.syads.myganesa.assets.Config;
import com.syads.myganesa.assets.PrefManager;
import com.syads.myganesa.assets.RequestHandler;
import com.syads.myganesa.assets.User;
import com.syads.myganesa.student.TampilJadwal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TampilJadwalActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;

    private String JSON_STRING;

    String hari;

    User user;

    TextView tvHari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_jadwal_guardians);

        //getting the current user
        user = PrefManager.getInstance(this).getUser();

        tvHari = (TextView) findViewById(R.id.tvHari);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();
    }

    private void showJadwal(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String jampel = jo.getString(Config.TAG_JAMPEL);
                String kd_guru = jo.getString(Config.TAG_KD_GURU);
                String nama = jo.getString(Config.TAG_NAMA);
                String nama_matpel = jo.getString(Config.TAG_NAMA_MATPEL);

                HashMap<String,String> jadwal = new HashMap<>();
                jadwal.put(Config.TAG_JAMPEL,jampel);
                jadwal.put(Config.TAG_KD_GURU,kd_guru);
                jadwal.put(Config.TAG_NAMA,nama);
                jadwal.put(Config.TAG_NAMA_MATPEL,nama_matpel);
                list.add(jadwal);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                TampilJadwalActivity.this, list, R.layout.list_item_jadwal,
                new String[]{ Config.TAG_JAMPEL, Config.TAG_KD_GURU
                        , Config.TAG_NAMA, Config.TAG_NAMA_MATPEL},
                new int[]{R.id.jampel, R.id.kd_guru, R.id.nama, R.id.nama_matpel});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            final String kelas = user.getId_kelas();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilJadwalActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showJadwal();
                tvHari.setText(hari);
            }

            @Override
            protected String doInBackground(Void... params) {

                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                SimpleDateFormat sdf = new SimpleDateFormat("E HH:mm");
                String currentDateandTime = sdf.format(new Date());
                hari = currentDateandTime.substring(0, 3);
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

                //creating request parameters
                HashMap<String, String> param = new HashMap<>();
                param.put("kelas", kelas);
                param.put("hari", hari);

                //returing the response
                String s = requestHandler.sendPostRequest(Config.URL_GET_ALL, param);
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(this, TampilPegawai.class);
//        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
//        String empId = map.get(konfigurasi.TAG_HARI).toString();
//        intent.putExtra(konfigurasi.EMP_ID,empId);
//        startActivity(intent);
    }
}