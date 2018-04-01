package id.eightstudio.www.tiketku.reservasi;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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

    @BindView(R.id.listTransaksi)
    ListView listViewTransaksiInProgress;

    public static InProgress newInstance() {
        return new InProgress();
    }

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
        SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext() , jsonDataGet, R.layout.model_data_inprogress,
                new String[] { "namaPesawat", "jamKeberangkatan", "jamTiba"},
                new int[] {R.id.txtNamaPesawatInProgress, R.id.txtJamBerangkat, R.id.txtJamTiba});

        listViewTransaksiInProgress.setAdapter(simpleAdapter);

        listViewTransaksiInProgress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showDetailTransaksi(position, view.getContext(), jsonDataGet);
            }
        });

    }

    //
    private void showDetailTransaksi(final int posisi, final Context context, ArrayList<HashMap<String, String>> jsonDataGet) {
        Dialog dialog = new Dialog(context);
        //Set layout
        dialog.setContentView(R.layout.popup_transaksi);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(true);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView txtNamaPesawat = dialog.findViewById(R.id.txtNamaPesawat);

        txtNamaPesawat.setText(jsonDataGet.get(posisi).get("namaUser"));

        dialog.show();
    }

}
