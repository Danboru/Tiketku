package id.eightstudio.www.tiketku.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import id.eightstudio.www.tiketku.tab_one.TabOne;
import id.eightstudio.www.tiketku.tab_three.TabThree;
import id.eightstudio.www.tiketku.tab_two.TabTwo;

public class TabAdapterMain extends FragmentPagerAdapter {

    public TabAdapterMain(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                return TabOne.newInstance();
            }
            case 1: {
                return TabTwo.newInstance();
            }
            default: {
                return TabThree.newInstance();
            }
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return "Tab Satu";
            }
            case 1: {
                return "Tab Dua";
            }
            default: {
                return "Tab Tiga";
            }
        }
    }
}
