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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HistoryAbsenActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;

    private String JSON_STRING;

    String hari;

    User user;

    TextView tvHari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_absen);

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

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String tanggal = jo.getString(Config.TAG_TANGGAL);
                String hari = jo.getString(Config.TAG_HARI);
                String jampel = jo.getString(Config.TAG_JAMPEL);
                String absen = jo.getString(Config.TAG_ABSEN);
                String matpel = jo.getString(Config.TAG_MATPEL);

                HashMap<String,String> historyAbsen = new HashMap<>();
                historyAbsen.put(Config.TAG_TANGGAL, tanggal);
                historyAbsen.put(Config.TAG_HARI, hari);
                historyAbsen.put(Config.TAG_JAMPEL,  jampel);
                historyAbsen.put(Config.TAG_ABSEN,  absen);
                historyAbsen.put(Config.TAG_MATPEL,  matpel);
                list.add(historyAbsen);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                HistoryAbsenActivity.this, list, R.layout.list_item_absensi_guardians,
                new String[]{Config.TAG_TANGGAL, Config.TAG_HARI
                        , Config.TAG_JAMPEL, Config.TAG_ABSEN, Config.TAG_MATPEL},
                new int[]{R.id.tvTanggal, R.id.tvHari, R.id.tvJampel, R.id.tvJamAbsen, R.id.tvMatpel});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            final String nis = user.getUsername();

            final SimpleDateFormat sdf = new SimpleDateFormat("E dd-MM-yyyy HH:mm");
            final String currentDateandTime = sdf.format(new Date());

            final String tgl = currentDateandTime.substring(4,6);
            final String bulan = currentDateandTime.substring(7,9);
            final String tahun = currentDateandTime.substring(10,14);

            final String tanggal = tahun+"-"+bulan+"-"+tgl;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(HistoryAbsenActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
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
                param.put("nis", nis);
                param.put("date", tanggal);

                //returing the response
                String s = requestHandler.sendPostRequest(Config.URL_GET_ABSENCE, param);
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