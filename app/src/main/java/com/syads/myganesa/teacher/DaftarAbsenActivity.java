package com.syads.myganesa.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class DaftarAbsenActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;

    private String JSON_STRING;

    String hari;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_absen);

        //getting the current user
        user = PrefManager.getInstance(this).getUser();

        SimpleDateFormat sdf = new SimpleDateFormat("E dd-MM-yyyy");
        String currentDate = sdf.format(new Date());
        hari = currentDate.substring(0,3);
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

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();

    }

    private void showKelasAjarHariIni(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id_kelas = jo.getString(Config.CLASS_ID);
                String kelas = jo.getString(Config.TAG_KELAS);
                String jampel = jo.getString(Config.TAG_JAMPEL);

                HashMap<String,String> listKelas = new HashMap<>();
                listKelas.put(Config.CLASS_ID, id_kelas);
                listKelas.put(Config.TAG_KELAS, kelas);
                listKelas.put(Config.TAG_JAMPEL, jampel);
                list.add(listKelas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                com.syads.myganesa.teacher.DaftarAbsenActivity.this, list, R.layout.list_item_kelas_absensi,
                new String[]{Config.TAG_KELAS, Config.TAG_JAMPEL},
                new int[]{R.id.nama_kelas, R.id.jampel});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            final String kd_guru = user.getKd_guru();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(com.syads.myganesa.teacher.DaftarAbsenActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showKelasAjarHariIni();
            }

            @Override
            protected String doInBackground(Void... params) {

                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> param = new HashMap<>();
                param.put("kd_guru", kd_guru);
                param.put("hari", hari);

                //returing the response
                String s = requestHandler.sendPostRequest(Config.URL_GET_TODAYS_CLASS, param);
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, TampilAbsensiActivity.class);
        HashMap<String,String> map = (HashMap) parent.getItemAtPosition(position);
        String classID = map.get(Config.CLASS_ID);
        String jampel = map.get(Config.TAG_JAMPEL);
        intent.putExtra(Config.CLASS_ID,classID);
        intent.putExtra(Config.TAG_JAMPEL,jampel);
        startActivity(intent);
    }

}