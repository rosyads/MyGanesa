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

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryBayarActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;

    private String JSON_STRING;

    String hari;

    User user;

    TextView tvHari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_bayar_guardians);

        //getting the current user
        user = PrefManager.getInstance(this).getUser();

        tvHari = (TextView) findViewById(R.id.tvHari);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();

    }

    private void showHistoryBayar(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String bulan = jo.getString(Config.TAG_BULAN);
                String tgl_bayar = jo.getString(Config.TAG_TANGGAL_BAYAR);
                String nominal = jo.getString(Config.TAG_NOMINAL);

                HashMap<String,String> historyBayar = new HashMap<>();
                historyBayar.put(Config.TAG_BULAN, "Bulan: "+ bulan);
                historyBayar.put(Config.TAG_TANGGAL_BAYAR, "Tanggal: "+ tgl_bayar);
                historyBayar.put(Config.TAG_NOMINAL, "Nominal: "+ nominal);
                list.add(historyBayar);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                HistoryBayarActivity.this, list, R.layout.list_item_pembayaran_student,
                new String[]{ Config.TAG_BULAN, Config.TAG_TANGGAL_BAYAR
                        , Config.TAG_NOMINAL},
                new int[]{R.id.bulan, R.id.tanggal_bayar, R.id.nominal});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            final String nis = user.getUsername();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(HistoryBayarActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showHistoryBayar();
//                Toast.makeText(HistoryBayarActivity.this, nis, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... params) {

                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> param = new HashMap<>();
                param.put("nis", nis);

                //returing the response
                String s = requestHandler.sendPostRequest(Config.URL_GET_PAID, param);
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