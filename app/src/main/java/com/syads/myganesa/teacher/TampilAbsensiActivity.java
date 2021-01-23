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

public class TampilAbsensiActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;

    private String JSON_STRING;

    User user;

    String id_kelas,jampel,tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_absensi);

        init();

    }

    public void init(){
        Intent intent = getIntent();
        id_kelas = intent.getStringExtra(Config.CLASS_ID);
        jampel = intent.getStringExtra(Config.TAG_JAMPEL);

        user = PrefManager.getInstance(this).getUser();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        tanggal = sdf.format(new Date());

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();

    }

    private void showAbsensi(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String nis = jo.getString(Config.TAG_NIS);
                String nama = jo.getString(Config.TAG_NAMA);

                HashMap<String,String> listKelas = new HashMap<>();
                listKelas.put(Config.TAG_NIS, nis);
                listKelas.put(Config.TAG_NAMA, nama);
                list.add(listKelas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                com.syads.myganesa.teacher.TampilAbsensiActivity.this, list, R.layout.list_item_kelas,
                new String[]{Config.TAG_NIS, Config.TAG_NAMA},
                new int[]{R.id.nis, R.id.nama});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            final String kd_guru = user.getKd_guru();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(com.syads.myganesa.teacher.TampilAbsensiActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showAbsensi();
            }

            @Override
            protected String doInBackground(Void... params) {

                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> param = new HashMap<>();
                param.put("kd_guru", kd_guru);
                param.put("id_kelas", id_kelas);

                //returing the response
                String s = requestHandler.sendPostRequest(Config.URL_GET_CLASS, param);
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(this, TampilAbsensiActivity.class);
//        HashMap<String,String> map = (HashMap) parent.getItemAtPosition(position);
//        String classID = map.get(Config.TAG_KELAS);
//        intent.putExtra(Config.CLASS_ID,classID);
//        startActivity(intent);
    }

}