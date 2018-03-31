package id.eightstudio.www.tiketku.tab_three;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.eightstudio.www.tiketku.R;
import id.eightstudio.www.tiketku.tab_one.TabOne;

public class TabThree extends Fragment {

    public static TabThree newInstance() {
        return new TabThree();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_tab_three, container, false);

        return view;
    }
}
