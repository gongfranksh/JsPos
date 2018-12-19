package personal.wl.jspos;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import personal.wl.jspos.Config.Adapter.SettingFragmentAdapter;

public class ConnectSettingActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private Context context;
    private List<Fragment> fragments;
    private String[] channels;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_setting);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.setting_tab_layout);
        viewPager = findViewById(R.id.viewpager);
        this.tabLayout = tabLayout;
        this.context = ConnectSettingActivity.this;
        load_tabitem();
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void load_tabitem() {
        String[] channels = getResources().getStringArray(R.array.Setting_Channel);
        this.channels = channels;
        for (int i = 0; i < channels.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(channels[i]);
            tabLayout.addTab(tab);
        }

        SettingFragmentAdapter sfa = new SettingFragmentAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(sfa);
        viewPager.setCurrentItem(0);
    }
}
