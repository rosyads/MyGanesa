package com.syads.myganesa.teacher;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RekapAbsenActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;

    private String JSON_STRING;

    String hari;

    User user;

    TextView tvHari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap_absen);

        //getting the current user
        user = PrefManager.getInstance(this).getUser();

        tvHari = (TextView) findViewById(R.id.tvHari);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();
    }

    private void showHistoryAbsen(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            String kls = null;

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String kelas = jo.getString(Config.TAG_KELAS);
                if(kls == null){
                    kls = jo.getString(Config.TAG_KELAS);
                }else if(kls.equals(kelas)){
                    kelas = null;
                }else{
                    kls = kelas;
                }
                String tanggal = jo.getString(Config.TAG_TANGGAL);
                String nama = jo.getString(Config.TAG_NAMA);

                HashMap<String,String> historyAbsen = new HashMap<>();
                historyAbsen.put(Config.TAG_TANGGAL, tanggal);
                historyAbsen.put(Config.TAG_NAMA, nama);
                historyAbsen.put(Config.TAG_KELAS,  kelas);
                historyAbsen.put(Config.TAG_HARI,  "✅");
                list.add(historyAbsen);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                RekapAbsenActivity.this, list, R.layout.list_item_absensi_teacher,
                new String[]{Config.TAG_TANGGAL, Config.TAG_NAMA
                        , Config.TAG_KELAS, Config.TAG_HARI},
                new int[]{R.id.tvTanggal, R.id.tvNama, R.id.tvKelas, R.id.tvKehadiran});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            final String kd_guru = user.getKd_guru();

            final SimpleDateFormat sdf = new SimpleDateFormat("E dd-MM-yyyy HH:mm");
            final String currentDateandTime = sdf.format(new Date());

            final String tgl = currentDateandTime.substring(4,6);
            final String bulan = currentDateandTime.substring(7,9);
            final String tahun = currentDateandTime.substring(10,14);

            //            final String tanggal = tahun+"-"+bulan+"-"+tgl;
            final String tanggal = tahun+"-"+bulan;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RekapAbsenActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showHistoryAbsen();
//                Toast.makeText(HistoryBayarActivity.this, nis, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... params) {

                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> param = new HashMap<>();
                param.put("kd_guru", kd_guru);
                param.put("date", tanggal);

                //returing the response
                return requestHandler.sendPostRequest(Config.URL_GET_ABSENCE_TEACHER, param);
//                return s;

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