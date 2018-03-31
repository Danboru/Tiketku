package id.eightstudio.www.tiketku.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import id.eightstudio.www.tiketku.R;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    ArrayList<HashMap<String, String>> dataTransaksi = new ArrayList<>();

    public DataAdapter(ArrayList<HashMap<String, String>> dataTransaksi) {
        this.dataTransaksi = dataTransaksi;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTester;
        public ViewHolder(View itemView) {
            super(itemView);
            //textViewTester = itemView.findViewById(R.id.txtTesterData);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_data, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textViewTester.setText(dataTransaksi.get(position).get("namaPesawat"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "" + dataTransaksi.get(position).get("namaUser"), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataTransaksi.size();
    }

}
