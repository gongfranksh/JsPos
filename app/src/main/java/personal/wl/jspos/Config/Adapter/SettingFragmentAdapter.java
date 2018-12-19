package personal.wl.jspos.Config.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import personal.wl.jspos.Config.Fragments.SettingDB;
import personal.wl.jspos.Config.Fragments.SettingFTP;
import personal.wl.jspos.Config.Fragments.SettingUpgrade;

public class SettingFragmentAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private static final int SETTING_DB = 0;
    private static final int SETTING_UPGRADE = 1;
    private static final int SETTING_FTP = 2;


    public SettingFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putInt("position", position);
        Fragment frag = null;
        switch (position) {
            case SETTING_DB:
                frag = SettingDB.newInstance();
                frag.setArguments(b);
                break;
            case SETTING_UPGRADE:
                frag = SettingUpgrade.newInstance();
                frag.setArguments(b);
                break;
            case SETTING_FTP:
                frag = SettingFTP.newInstance();
                frag.setArguments(b);
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
