package personal.wl.jspos.Config.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import personal.wl.jspos.Config.Beans.SystemDBInfo;
import personal.wl.jspos.R;

import static personal.wl.jspos.Config.Beans.SystemSettingConstant.TEST_OK_STATUS;

public class SettingDB extends Fragment {
    private SystemDBInfo systemDBInfo;
    private TextView tv_db_ip_address;
    private TextView tv_db_name;
    private TextView tv_db_account;
    private TextView tv_db_password;
    private Button bt_confirm;
    private Button bt_test;
    private String CONNSTR;
    private String DRIVER;



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
                Toast.makeText(getActivity(), "保存完毕！", Toast.LENGTH_LONG).show();
            }
        });

        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new  TestConnectMssql().execute();
            }
        });
    }


    protected void saveConfig() {
        systemDBInfo.set_Db_Account(tv_db_account.getText().toString());
        systemDBInfo.set_Db_Ip_Address(tv_db_ip_address.getText().toString());
        systemDBInfo.set_Db_Name(tv_db_name.getText().toString());
        systemDBInfo.set_Db_Password(tv_db_password.getText().toString());
    }


    private String testConnection() {
        try {
            DRIVER = "net.sourceforge.jtds.jdbc.Driver";
            CONNSTR = "jdbc:jtds:sqlserver://" + systemDBInfo.get_Db_IP_Address() + ":1433/"
                    + systemDBInfo.get_Db_Name() + ";useunicode=true;characterEncoding=UTF-8";

            // TODO Connect To MsSql Server
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            long start = System.currentTimeMillis();
            Connection con = DriverManager.getConnection(CONNSTR, systemDBInfo.get_Db_Account(), systemDBInfo.get_Db_Password());
            long end = System.currentTimeMillis() - start;
            return TEST_OK_STATUS;
        } catch (ClassNotFoundException e1) {
            // TODO ClassNotFoundException
            e1.printStackTrace();
            return e1.toString();
        } catch (SQLException e) {
            // TODO SQLExceptionError
            e.printStackTrace();
            return e.toString();
        }
    }


    public class TestConnectMssql extends AsyncTask<String, Integer, String> {
        public TestConnectMssql() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            String flag = testConnection();
            return flag;
        }


        @Override
        protected void onPostExecute(String o) {
            if (o.equals(TEST_OK_STATUS)){
                Toast.makeText(getActivity(),"---测试成功！",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(),o,Toast.LENGTH_LONG).show();
            }
        }
    }
}