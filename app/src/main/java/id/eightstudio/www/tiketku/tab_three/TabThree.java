package id.eightstudio.www.tiketku.tab_three;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import id.eightstudio.www.tiketku.utils.DataTemp;
import id.eightstudio.www.tiketku.utils.UriConfig;

public class TabThree extends Fragment implements View.OnClickListener {

    ArrayList<HashMap<String, String>> jsonDataUser = new ArrayList<>();
    private static final String TAG = "TabThree";

    public static TabThree newInstance() {
        return new TabThree();
    }

    @BindView(R.id.btnUpdateDataUser)
    Button btnUpdateDataUser;
    @BindView(R.id.edtNamaUser)
    EditText edtNamaUser;
    @BindView(R.id.edtAlamatUser)
    EditText edtAlamatUser;
    @BindView(R.id.edtEmailUser)
    EditText edtEmailUser;
    @BindView(R.id.edtTelpUser)
    EditText edtTelpUser;
    @BindView(R.id.txtSaldo)
    TextView txtSaldo;
    @BindView(R.id.txtIdUseer)
    TextView txtIdUseer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_tab_three, container, false);
        ButterKnife.bind(this, view);
        getDataUser("danangpriabada@gmail.com", view);

        btnUpdateDataUser.setOnClickListener(this);

        return view;
    }

    public void getDataUser(String emailUser, final View view) {
        AndroidNetworking.post(UriConfig.host + "/672014113v120180401/user/info.php?emailUser=" + emailUser)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonDataUser.clear();

                        try {
                            if (response.optString("status").equals("true")) {
                                JSONArray jsonArray = response.optJSONArray("result");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject responses = jsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<>();

                                    map.put("idUser", responses.optString("idUser"));
                                    map.put("namaUser", responses.optString("namaUser"));
                                    map.put("alamatUser", responses.optString("alamatUser"));
                                    map.put("saldoUser", responses.optString("saldoUser"));
                                    map.put("emailUser", responses.optString("emailUser"));
                                    map.put("telpUser", responses.optString("telpUser"));

                                    jsonDataUser.add(map);
                                }

                                DataTemp.idCurrentUser = jsonDataUser.get(0).get("idUser");
                                setDataUser();
                                Log.d(TAG, "Main Data " + jsonDataUser);

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

    public void setDataUser() {
        edtNamaUser.setText(jsonDataUser.get(0).get("namaUser"));
        edtAlamatUser.setText(jsonDataUser.get(0).get("alamatUser"));
        edtTelpUser.setText(jsonDataUser.get(0).get("telpUser"));
        edtTelpUser.setText(jsonDataUser.get(0).get("telpUser"));
        edtEmailUser.setText(jsonDataUser.get(0).get("emailUser"));
        txtSaldo.setText(jsonDataUser.get(0).get("saldoUser"));
        txtIdUseer.setText("ID : " + jsonDataUser.get(0).get("idUser"));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdateDataUser: {
                Log.d(TAG, "onClick: " + "Update CLicked");
                updateDataUser(view);
                break;
            }
        }
    }

    public void updateDataUser(final View view) {
        AndroidNetworking.post(UriConfig.host + "/672014113v120180401/user/update.php")
                .addBodyParameter("idUser", jsonDataUser.get(0).get("idUser"))
                .addBodyParameter("namaUser", edtNamaUser.getText().toString())
                .addBodyParameter("alamatUser", edtAlamatUser.getText().toString())
                .addBodyParameter("saldoUser", jsonDataUser.get(0).get("saldoUser"))
                .addBodyParameter("emailUser", edtEmailUser.getText().toString())
                .addBodyParameter("telpUser", edtTelpUser.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
            public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.d("response", response.toString());

                        if (response.optString("response").equals("success")) {
                            Snackbar.make(view, "Berhasil Di Simpan", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onResponse: " + response.optString("response"));
                            Snackbar.make(view, "Gagal Di Simpan", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }

}