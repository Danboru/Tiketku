package id.eightstudio.www.tiketku;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.eightstudio.www.tiketku.adapter.TabAdapterMain;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabLayoutMain) TabLayout tabLayoutMain;
    @BindView(R.id.viewPagerMain) ViewPager viewPagerMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        TabAdapterMain tabAdapterMain = new TabAdapterMain(getSupportFragmentManager());
        viewPagerMain.setAdapter(tabAdapterMain);
        tabLayoutMain.setupWithViewPager(viewPagerMain);

    }

}