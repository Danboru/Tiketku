package id.eightstudio.www.tiketku.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.eightstudio.www.tiketku.R;
import id.eightstudio.www.tiketku.tab_one.TabOne;
import id.eightstudio.www.tiketku.utils.UriConfig;

public class PencarianPenerbangan extends AppCompatActivity {

    private static final String TAG = "PencarianPenerbangan";
    private String dataTanggalPencarian, dataBandaraAsal, dataBandaraTujuan, dataSetStringExtra;

    ArrayList<HashMap<String, String>> jsonDataGet = new ArrayList<>();

    @BindView(R.id.listHasilFilter)
    ListView listHasilFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian_penerbangan);
        ButterKnife.bind(this);

        dataSetStringExtra = getIntent().getStringExtra(TabOne.KEY_EXTRA);

        //Split data
        String[] temp = dataSetStringExtra.split("/");
        dataTanggalPencarian = temp[0];
        dataBandaraAsal = temp[1];
        dataBandaraTujuan = temp[2];
        Log.d(TAG, "onCreate: " + "Berangkat tanggal " + dataTanggalPencarian + " Dari " + dataBandaraAsal + " Ke " + dataBandaraTujuan );

        //Get All data
        getAllDataPenerbanganBySearch();

    }

    //GET Data dari Database melalui JSON
    public void getAllDataPenerbanganBySearch() {

        AndroidNetworking.post(UriConfig.host + "/672014113v120180401/penerbangan/filter_penerbangan.php")
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("bandaraTujuan","1")
                .addBodyParameter("bandaraAsal", "3")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.optString("status").equals("true")) {
                                JSONArray jsonArray = response.optJSONArray("result");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject responses = jsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<>();

                                    map.put("idPenerbangan", responses.optString("idPenerbangan"));
                                    map.put("codePesawat", responses.optString("codePesawat"));
                                    map.put("idTujuan", responses.optString("idTujuan"));
                                    map.put("idBerangkat", responses.optString("idBerangkat"));
                                    map.put("harga", responses.optString("harga"));
                                    map.put("tanggalPenerbangan", responses.optString("tanggalPenerbangan"));
                                    map.put("jamBerangkat", responses.optString("jamBerangkat"));
                                    map.put("jamTiba", responses.optString("jamTiba"));

                                    jsonDataGet.add(map);
                                }
                                Log.d(TAG, "Main Data " + jsonDataGet);

                                Adapter();

                            } else {
                                Toast.makeText(PencarianPenerbangan.this, "Data Tidak Di temukan", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + "Gagal Mengambil JSON");
                    }
                });
    }


    private void Adapter(){
        SimpleAdapter simpleAdapter = new SimpleAdapter(PencarianPenerbangan.this, jsonDataGet, R.layout.model_data,
                new String[] { "idPenerbangan", "codePesawat", "idTujuan", "idBerangkat", "harga", "tanggalPenerbangan"},
                new int[] {R.id.text_transaksi_id, R.id.text_status, R.id.text_jumlah, R.id.text_keterangan,
                        R.id.text_tanggal, R.id.text_tanggal2});

        listHasilFilter.setAdapter(simpleAdapter);

    }

}
