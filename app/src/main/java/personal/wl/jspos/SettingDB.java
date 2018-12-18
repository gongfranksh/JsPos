package personal.wl.jspos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import personal.wl.jspos.method.SystemDBInfo;

public class SettingDB extends Fragment {
    private SharedPreferences sPreferences;
    private String db_ip_address;
    private String db_name;
    private String db_account;
    private String db_password;
    private SystemDBInfo systemDBInfo;
    private TextView tv_db_ip_address;
    private TextView tv_db_name;
    private TextView tv_db_account;
    private TextView tv_db_password;
    private Button bt_confirm;
    private Button bt_test;


    public SettingDB() {
    }

    public static SettingDB newInstance() {

        return new SettingDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_db, container, false);
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
        systemDBInfo = new SystemDBInfo(getActivity());
        tv_db_account.setText(systemDBInfo.get_Db_Account());
        tv_db_ip_address.setText(systemDBInfo.get_Db_IP_Address());
        tv_db_name.setText(systemDBInfo.get_Db_Name());
        tv_db_password.setText(systemDBInfo.get_Db_Password());
    }

    private void initViews(View view) {
        tv_db_ip_address = view.findViewById(R.id.setting_db_ip);
        tv_db_name = view.findViewById(R.id.setting_db_connect_name);
        tv_db_account = view.findViewById(R.id.setting_db_connect_user);
        tv_db_password = view.findViewById(R.id.setting_db_connect_pwd);
        bt_confirm = view.findViewById(R.id.bt_dbNeedSave);
        bt_test = view.findViewById(R.id.setting_db_conect_test);

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfig();
                Toast.makeText(getActivity(),"保存完毕！",Toast.LENGTH_LONG).show();
            }
        });

        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"测试连接。。。。",Toast.LENGTH_LONG).show();

            }
        });
    }


    protected void saveConfig() {
        systemDBInfo.set_Db_Account(tv_db_account.getText().toString());
        systemDBInfo.set_Db_Ip_Address(tv_db_ip_address.getText().toString());
        systemDBInfo.set_Db_Name(tv_db_name.getText().toString());
        systemDBInfo.set_Db_Password(tv_db_password.getText().toString());
    }
}
