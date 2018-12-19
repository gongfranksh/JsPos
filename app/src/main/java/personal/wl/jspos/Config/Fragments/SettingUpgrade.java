package personal.wl.jspos.Config.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;
import personal.wl.jspos.Config.Beans.SystemFtpInfo;
import personal.wl.jspos.Config.Beans.SystemHttpInfo;
import personal.wl.jspos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingUpgrade extends Fragment {
    private  String VERSION_URL ;
    private EditText et_http_web;
    private SystemHttpInfo systemHttpInfo;
    private SystemFtpInfo  systemFtpInfo;
    private Button bt_savehttp;
    private Button bt_testhttp;

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
        bt_testhttp= view.findViewById(R.id.bt_http_test);
        bt_testhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestHttpConnection();
            }
        });

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



    public void TestHttpConnection() {
        systemFtpInfo= new SystemFtpInfo(getActivity());

        VERSION_URL = systemHttpInfo.getURL() + systemFtpInfo.getFtp_joson_file();
        //下载output.json版本信息文件
        OkHttpUtils
                .get()
                .url(VERSION_URL)
                .build()//
                .execute(new FileCallBack(getActivity().getFilesDir().getPath(), systemFtpInfo.getFtp_joson_file())//
                {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(getActivity(), "失败" + e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(Call call, File file) {
                        Toast.makeText(getActivity(), "测试成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void inProgress(float v, long l) {
                    }
                });
    }
}
