package personal.wl.jspos;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import personal.wl.jspos.method.SystemHttpInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingUpgrade extends Fragment {
    private EditText et_http_web;
    private SystemHttpInfo systemHttpInfo;
    private Button bt_savehttp;
    private Button bu_testhttp;

    public SettingUpgrade() {
        // Required empty public constructor
    }

    public static SettingUpgrade newInstance() {
        return new SettingUpgrade();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_upgrade, container, false);
        initViews(view);
        getValueFromConfig();
        saveConfig();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getValueFromConfig() {
        systemHttpInfo = new SystemHttpInfo(getActivity());
        et_http_web.setText(systemHttpInfo.getURL());
    }

    private void initViews(View view) {
        et_http_web = view.findViewById(R.id.setting_http_web);
        bt_savehttp= view.findViewById(R.id.bt_upgradeNeedSave);
        bu_testhttp= view.findViewById(R.id.bt_http_test);

        bt_savehttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfig();
                Toast.makeText(getActivity(),"保存完毕！", Toast.LENGTH_LONG).show();
            }
        });

    }

    protected void saveConfig() {
        systemHttpInfo.setURL(et_http_web.getText().toString());
    }
}
