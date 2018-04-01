package id.eightstudio.www.tiketku.tab_one;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.eightstudio.www.tiketku.R;
import id.eightstudio.www.tiketku.activity.PencarianPenerbangan;
import id.eightstudio.www.tiketku.utils.CurrentDate;


public class TabOne extends Fragment {

    @BindView(R.id.spinnerBandaraAsal) Spinner spinnerBadaraAsal;
    @BindView(R.id.spinnerBandaraTujuan) Spinner spinnerBadaraTujuan;
    @BindView(R.id.btnCariRutePenerbangan) Button btnCariRutePenerbangan;
    @BindView(R.id.edtTanggalBerangkat) EditText edtTanggalBerangkat;
    @BindView(R.id.ibDatePicker)
    ImageButton ibDatePicker;

    private DatePickerDialog datePickerDialog;
    String dataKeberangkatan, bandaraAsal, bandaraTujuan;
    public static final String KEY_EXTRA = "id.eightstudio.www.tiketku.tab_one";

    public static TabOne newInstance() {
        return new TabOne();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_tab_one, container, false);
        ButterKnife.bind(this, view);

        //Set bandaraAsal
        originBadara(view);
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

                        dataKeberangkatan = year  + "-" + numberFormat.format(month_of_year + 1) + "-" +
                                numberFormat.format(day_of_month);
                        edtTanggalBerangkat.setText(dataKeberangkatan);

                    }
                }, CurrentDate.currentYear , CurrentDate.currentMonth, CurrentDate.currentDay);
                datePickerDialog.show();
            }
        });

        btnCariRutePenerbangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), dataKeberangkatan +
                        " : Dari " + bandaraAsal + " Ke " + bandaraTujuan , Toast.LENGTH_SHORT).show();

                Intent intentHasilPencarian = new Intent(getContext(), PencarianPenerbangan.class);

                //Put data extra
                intentHasilPencarian.putExtra(KEY_EXTRA, dataKeberangkatan + "/" + bandaraAsal + "/" + bandaraTujuan);
                startActivity(intentHasilPencarian);
            }
        });

        return view;
    }

    public void originBadara(View view) {

        //List Badara asal (Seharusnya datanya di ambil dari hasil select di table bandara)
        List<String> categories = new ArrayList<>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        ArrayAdapter<String> adapterOrigin = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, categories);
        adapterOrigin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBadaraAsal.setAdapter(adapterOrigin);

        // Spinner click listener
        spinnerBadaraAsal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View parent, int position, long l) {
                // On selecting a spinner item
                bandaraAsal = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void destinationBandara(View view) {

        //List Badara tujuan (Seharusnya datanya di ambil dari hasil select di table bandara)
        List<String> categories2 = new ArrayList<>();
        categories2.add("Automobile");
        categories2.add("Business Services");
        categories2.add("Computers");
        categories2.add("Education");

        ArrayAdapter<String> adapterDestination = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, categories2);
        adapterDestination.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBadaraTujuan.setAdapter(adapterDestination);

        spinnerBadaraTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View parent, int position, long l) {
                // On selecting a spinner item
                bandaraTujuan = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        }

}
