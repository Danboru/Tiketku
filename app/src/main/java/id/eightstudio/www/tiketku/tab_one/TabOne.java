package id.eightstudio.www.tiketku.tab_one;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.eightstudio.www.tiketku.R;
import id.eightstudio.www.tiketku.activity.PencarianPenerbangan;
import id.eightstudio.www.tiketku.utils.CurrentDate;
import id.eightstudio.www.tiketku.utils.DataTemp;
import id.eightstudio.www.tiketku.utils.UriConfig;

public class TabOne extends Fragment {

    @BindView(R.id.spinnerBandaraAsal)
    Spinner spinnerBadaraAsal;
    @BindView(R.id.spinnerBandaraTujuan)
    Spinner spinnerBadaraTujuan;
    @BindView(R.id.btnCariRutePenerbangan)
    Button btnCariRutePenerbangan;
    @BindView(R.id.edtTanggalBerangkat)
    EditText edtTanggalBerangkat;
    @BindView(R.id.ibDatePicker)
    ImageButton ibDatePicker;

    ArrayList<HashMap<String, String>> jsonDataBandara = new ArrayList<>();
    HashMap<String, String> dataIdBandara = new HashMap<>();
    List<String> categories = new ArrayList<>();
    private static final String TAG = "TabOne";
    View view;

    private DatePickerDialog datePickerDialog;
    String dataKeberangkatan, bandaraAsal, bandaraTujuan;
    public static final String KEY_EXTRA = "id.eightstudio.www.tiketku.tab_one";

    public static TabOne newInstance() {
        return new TabOne();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_tab_one, container, false);
        ButterKnife.bind(this, view);

        //Generate data bandara
        generateDataBandara();
        //Set bandaraAsal
        originBandara(view);
        //Set bandaraTujuan
        destinationBandara(view);

        //Set dataKeberangkatan
        ibDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month_of_year, int day_of_month) {
                        NumberFormat numberFormat = new DecimalFormat("00");

                        dataKeberangkatan = year + "-" + numberFormat.format(month_of_year + 1) + "-" +
                                numberFormat.format(day_of_month);
                        edtTanggalBerangkat.setText(dataKeberangkatan);

                    }
                }, CurrentDate.currentYear, CurrentDate.currentMonth, CurrentDate.currentDay);
                datePickerDialog.show();
            }
        });

        //Cari Penerbangan Berdasarkan badara asal dan bandara tujuan
        btnCariRutePenerbangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHasilPencarian = new Intent(getContext(), PencarianPenerbangan.class);

                //Put data extra
                intentHasilPencarian.putExtra(KEY_EXTRA, dataKeberangkatan + "/" + bandaraAsal + "/" + bandaraTujuan);
                Log.d(TAG, "onClick: " + dataKeberangkatan + "/" + bandaraAsal + "/" + bandaraTujuan);
                startActivity(intentHasilPencarian);
            }
        });

        return view;
    }

    public void originBandara(View view) {

        ArrayAdapter<String> adapterOrigin = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, categories);
        adapterOrigin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBadaraAsal.setAdapter(adapterOrigin);

        // Spinner click listener
        spinnerBadaraAsal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View parent, int position, long l) {
                // On selecting a spinner item
                bandaraAsal = dataIdBandara.get(adapterView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void destinationBandara(View view) {

        ArrayAdapter<String> adapterDestination = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, categories);
        adapterDestination.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBadaraTujuan.setAdapter(adapterDestination);

        spinnerBadaraTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View parent, int position, long l) {
                // On selecting a spinner item
                bandaraTujuan = dataIdBandara.get(adapterView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void generateDataBandara() {
        getDataBandara("Tujuan");
        for (int i = 0; i < jsonDataBandara.size(); i++) {
            Log.d(TAG, "originBandara: " + jsonDataBandara.get(i).get("namaBandara"));

            //Menyimpan id bandara dengan key nama bandara
            dataIdBandara.put(jsonDataBandara.get(i).get("namaBandara"), jsonDataBandara.get(i).get("idBandara"));

            categories.add(jsonDataBandara.get(i).get("namaBandara"));
        }

    }

    public void getDataBandara(String response) {
        AndroidNetworking.post(UriConfig.host + "/672014113v120180401/bandara/list_bandara"+response+".php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonDataBandara.clear();

                        try {
                            if (response.optString("status").equals("true")) {
                                JSONArray jsonArray = response.optJSONArray("result");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject responses = jsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<>();

                                    map.put("idBerangkat", responses.optString("idBerangkat"));
                                    map.put("idBandara", responses.optString("idBandara"));
                                    map.put("namaBandara", responses.optString("namaBandara"));

                                    jsonDataBandara.add(map);
                                }

                                Log.d(TAG, "Main Data " + jsonDataBandara);

                            } else {
                                Snackbar.make(view, "Tidak Ada Data", Snackbar.LENGTH_SHORT).show();
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

}
