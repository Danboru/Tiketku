package id.eightstudio.www.tiketku.tab_two;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.eightstudio.www.tiketku.R;
import id.eightstudio.www.tiketku.reservasi.History;
import id.eightstudio.www.tiketku.reservasi.InProgress;
import id.eightstudio.www.tiketku.tab_one.TabOne;

public class TabTwo extends Fragment {

    public static TabTwo newInstance() {
        return new TabTwo();
    }

    @BindView(R.id.bottomMenuReservasi)
    BottomNavigationView bottomNavigationViewReservasi;

    @Nullable @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_tab_two, container, false);
        ButterKnife.bind(this, view);

        bottomNavigationViewReservasi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragmentSelected = null;
                switch (item.getItemId()) {
                    case R.id.history : {
                        fragmentSelected = History.newInstance();
                        break;
                    }
                    default : {
                        fragmentSelected = InProgress.newInstance();
                        break;
                    }
                }

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutReservasi, fragmentSelected);
                fragmentTransaction.commit();
                return true;
            }
        });

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutReservasi, History.newInstance());
        fragmentTransaction.commit();

        return view;
    }
}
