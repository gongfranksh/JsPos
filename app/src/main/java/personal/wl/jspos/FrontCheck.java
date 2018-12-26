package personal.wl.jspos;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import personal.wl.jspos.Front_check.Adapter.FrontCheckFragmentAdapter;

public class FrontCheck extends AppCompatActivity {
    private TabLayout tabLayout;
    private Context context;
    private List<Fragment> fragments;
    private String[] channels;
    private ViewPager viewPager;
    private OnHideKeyboardListener onHideKeyboardListener;
    private ArrayList<OnHideKeyboardListener> onTouchListeners = new ArrayList<OnHideKeyboardListener>(
            10);


    public interface OnHideKeyboardListener {
        public boolean hideKeyboard();
    }

    public void setOnHideKeyboardListener(OnHideKeyboardListener onHideKeyboardListener) {
        this.onHideKeyboardListener = onHideKeyboardListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_check);
        tabLayout = findViewById(R.id.front_check_tab_layout);
        viewPager = findViewById(R.id.front_check_viewpager);
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
        String[] channels = getResources().getStringArray(R.array.Front_check_Channel);
        this.channels = channels;
        for (int i = 0; i < channels.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(channels[i]);
            tabLayout.addTab(tab);
        }
        FrontCheckFragmentAdapter ffa = new FrontCheckFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(ffa);
//        viewPager.setCurrentItem(0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (OnHideKeyboardListener listener : onTouchListeners) {
            if (listener != null) {
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    public void registerOnHideKeyboardListener(OnHideKeyboardListener myOnTouchListener){
        onTouchListeners.add(myOnTouchListener);
    }
    public void unregisterOnHideKeyboardListener(OnHideKeyboardListener myOnTouchListener){
        onTouchListeners.remove(myOnTouchListener);
    }


}
