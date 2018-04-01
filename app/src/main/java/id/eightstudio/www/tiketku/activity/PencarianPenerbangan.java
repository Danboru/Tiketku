package id.eightstudio.www.tiketku.activity;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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

    @BindView(R.id.toolbarMenuMain)
    Toolbar toolbarMain;
    @BindView(R.id.listHasilFilter)
    ListView listHasilFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian_penerbangan);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMain);

        dataSetStringExtra = getIntent().getStringExtra(TabOne.KEY_EXTRA);

        //Split data
        String[] temp = dataSetStringExtra.split("/");
        dataTanggalPencarian = temp[0];
        dataBandaraAsal = temp[1];
        dataBandaraTujuan = temp[2];
        Log.d(TAG, "onCreate: " + "Berangkat tanggal " + dataTanggalPencarian + " Dari " + dataBandaraAsal + " Ke " + dataBandaraTujuan);

        //Get All data
        getAllDataPenerbanganBySearch();

    }

    //GET Data dari database melalui JSON
    public void getAllDataPenerbanganBySearch() {
        AndroidNetworking.post(UriConfig.host + "/672014113v120180401/penerbangan/filter_penerbangan.php")
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("bandaraTujuan", "1")
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

                                Log.d(TAG, "Main Data " + jsonDataGet.get(0));
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


    private void Adapter() {
        SimpleAdapter simpleAdapter = new SimpleAdapter(PencarianPenerbangan.this, jsonDataGet, R.layout.model_data_penerbangan,
                new String[]{"idPenerbangan", "codePesawat", "idBerangkat", "idTujuan"},
                new int[]{R.id.txtIdPenerbangan, R.id.txtCodePesawat, R.id.txtBandaraAsal, R.id.txtBandaraTujuan});

        listHasilFilter.setAdapter(simpleAdapter);

        //Tampilkan Popup
        listHasilFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                showPencarianDetail(position, view.getContext());

            }
        });

    }

    private void showPencarianDetail(final int posisi, final Context context) {
        final Dialog dialog = new Dialog(context);

        //Set layout
        dialog.setContentView(R.layout.popup_hasil_pencarian);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(true);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button buttonBooking = dialog.findViewById(R.id.btnBookingPesawat);
        buttonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingPeesawat(jsonDataGet.get(posisi).get("idPenerbangan"));
            }
        });

        dialog.show();
    }

    public void bookingPeesawat(String idPenerbangan) {

        AndroidNetworking.post(UriConfig.host + "/672014113v120180401/transaksi/add_transaksi.php")
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("idPenerbangan", idPenerbangan)
                .addBodyParameter("idUser", "14")
                .addBodyParameter("jumlahTiket", "14")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.optString("response").equals("success")) {
                            Toast.makeText(PencarianPenerbangan.this, "Berhasil Booking", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(PencarianPenerbangan.this, "Gagal Booking", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + "Gagal Mengambil JSON");
                    }
                });

    }

}
