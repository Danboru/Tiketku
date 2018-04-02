package id.eightstudio.www.tiketku;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.aboutApp : {
                showAbout(MainActivity.this);
            }
        }

        return true;
    }

    //Menampilkan detail hasil pencarian
    private void showAbout(Context context) {
        final Dialog dialog = new Dialog(context);

        //Set layout
        dialog.setContentView(R.layout.popup_about);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(true);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);


        dialog.show();
    }
}