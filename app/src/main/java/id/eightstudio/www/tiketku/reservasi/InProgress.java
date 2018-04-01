package id.eightstudio.www.tiketku.reservasi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import id.eightstudio.www.tiketku.utils.UriConfig;

public class InProgress extends Fragment {

    private static final String TAG = "InProgress";
    ArrayList<HashMap<String, String>> jsonDataGet = new ArrayList<>();
    public static String transaksi_id, status, jumlah, keterangan, tanggal, tanggal2;

    public static InProgress newInstance() {
        return new InProgress();
    }

    @BindView(R.id.listTransaksi)
    ListView listViewTransaksiInProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_in_progress, container, false);
        ButterKnife.bind(this, view);

        getAllDataTransaksi("14", view);

        return view;
    }

    //GET Data dari Database melalui JSON
    public void getAllDataTransaksi(String idUser, final View view) {

        AndroidNetworking.post(UriConfig.host + "/672014113v120180401/transaksi/list_transaksi.php?idUser=" + idUser)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonDataGet.clear();

                        try {
                            if (response.optString("status").equals("true")) {
                                JSONArray jsonArray = response.optJSONArray("result");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject responses = jsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<>();

                                    map.put("idTransaksi", responses.optString("idTransaksi"));
                                    map.put("namaUser", responses.optString("namaUser"));
                                    map.put("idPenerbangan", responses.optString("idPenerbangan"));
                                    map.put("jumlahTiket", responses.optString("jumlahTiket"));
                                    map.put("jamKeberangkatan", responses.optString("jamKeberangkatan"));
                                    map.put("jamTiba", responses.optString("jamTiba"));
                                    map.put("namaPesawat", responses.optString("namaPesawat"));

                                    jsonDataGet.add(map);
                                }
                                Log.d(TAG, "Main Data " + jsonDataGet);

                                Adapter(view);

                            } else {
                                Toast.makeText(getContext(), "Data Tidak Di temukan", Toast.LENGTH_SHORT).show();
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

    //Adapter untuk data Transaksi In Progress
    private void Adapter(View view){
        SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext() , jsonDataGet, R.layout.model_data,
                new String[] { "idTransaksi", "namaUser", "jumlahTiket", "jamKeberangkatan", "jamTiba", "namaPesawat"},
                new int[] {R.id.text_transaksi_id, R.id.text_status, R.id.text_jumlah, R.id.text_keterangan,
                        R.id.text_tanggal, R.id.text_tanggal2});

        listViewTransaksiInProgress.setAdapter(simpleAdapter);
        listViewTransaksiInProgress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                transaksi_id    = ((TextView) view.findViewById(R.id.text_transaksi_id)).getText().toString();
                status          = ((TextView) view.findViewById(R.id.text_status)).getText().toString();
                jumlah          = ((TextView) view.findViewById(R.id.text_jumlah)).getText().toString();
                keterangan      = ((TextView) view.findViewById(R.id.text_keterangan)).getText().toString();
                tanggal         = ((TextView) view.findViewById(R.id.text_tanggal)).getText().toString();
                tanggal2        = ((TextView) view.findViewById(R.id.text_tanggal2)).getText().toString();

            }
        });

    }

}
