package id.eightstudio.www.tiketku.reservasi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.eightstudio.www.tiketku.R;

public class History extends Fragment {

    List<String> stringData = new ArrayList<>();

    public static History newInstance() {
        return new History();
    }

    @BindView(R.id.listDataHistory)
    ListView listViewHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_history, container, false);
        ButterKnife.bind(this, view);

        for (int i = 0; i < 5; i++) {
            stringData.add("History " + i);
        }

        ListAdapter adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, stringData);
        listViewHistory.setAdapter(adapter);

        return view;
    }

}
