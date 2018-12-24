package personal.wl.jspos.Front_check.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import personal.wl.jspos.Front_check.Fragments.FrontCheckDetailFragment;
import personal.wl.jspos.Front_check.Fragments.FrontCheckOperateFragment;
import personal.wl.jspos.Front_check.Fragments.FrontCheckSettingFragment;

public class FrontCheckFragmentAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private static final int FRONT_CHECK_OP = 0;
    private static final int FRONT_CHECK_DETAIL = 1;
    private static final int FRONT_CHECK_SETTING = 2;

    public FrontCheckFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {

        Bundle b = new Bundle();
        b.putInt("position", i);
        Fragment frag = null;
        switch (i) {
            case FRONT_CHECK_OP:
                frag = FrontCheckOperateFragment.newInstance();
                frag.setArguments(b);
                break;
            case FRONT_CHECK_DETAIL:
                frag = FrontCheckDetailFragment.newInstance();
                frag.setArguments(b);
                break;
            case FRONT_CHECK_SETTING:
                frag = FrontCheckSettingFragment.newInstance();
                frag.setArguments(b);
                break;
        }
        return frag;


    }

    @Override
    public int getCount()  {
        return mNumOfTabs;
    }
}
