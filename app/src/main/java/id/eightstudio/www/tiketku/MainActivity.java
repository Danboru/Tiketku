package id.eightstudio.www.tiketku;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.eightstudio.www.tiketku.adapter.TabAdapterMain;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabLayoutMain)
    TabLayout tabLayoutMain;
    @BindView(R.id.viewPagerMain)
    ViewPager viewPagerMain;
    @BindView(R.id.toolbarMenuMain)
    Toolbar toolbarMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarMain);

        TabAdapterMain tabAdapterMain = new TabAdapterMain(getSupportFragmentManager());
        viewPagerMain.setAdapter(tabAdapterMain);
        tabLayoutMain.setupWithViewPager(viewPagerMain);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_main, menu);
        return true;
    }

}