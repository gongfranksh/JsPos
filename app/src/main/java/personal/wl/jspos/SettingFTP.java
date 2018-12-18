package personal.wl.jspos;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;

import personal.wl.jspos.method.SystemFtpInfo;
import personal.wl.jspos.update.utils.FTPToolkit;

import static personal.wl.jspos.method.SystemSettingConstant.TEST_OK_STATUS;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFTP extends Fragment {

    private SystemFtpInfo systemFtpInfo;
    private FTPClient mFtpClient;

    private EditText et_ftp_ip;
    private EditText et_ftp_account;
    private EditText et_ftp_pwd;
    private EditText et_ftp_path;
    private EditText et_ftp_josonfile;
    private EditText et_ftp_readme;

    private Button bt_needsave;
    private Button bt_test;

    public SettingFTP() {
        // Required empty public constructor
    }

    public static SettingFTP newInstance() {
        return new SettingFTP();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_ft, container, false);
        initViews(view);
        getValueFromConfig();
        saveConfig();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews(View view) {
        et_ftp_ip = view.findViewById(R.id.setting_ftp_ip);
        et_ftp_account = view.findViewById(R.id.setting_ftp_connect_user);
        et_ftp_pwd = view.findViewById(R.id.setting_ftp_connect_pwd);
        et_ftp_path = view.findViewById(R.id.setting_ftp_connect_path);
        et_ftp_josonfile = view.findViewById(R.id.setting_ftp_josonfile);
        et_ftp_readme = view.findViewById(R.id.setting_ftp_readme);
        bt_needsave = view.findViewById(R.id.bt_ftpNeedSave);
        bt_test = view.findViewById(R.id.bt_ftp_test);

        bt_needsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfig();
                Toast.makeText(getActivity(), "保存完毕!", Toast.LENGTH_LONG).show();
            }
        });

        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestConnectFTP().execute();
            }
        });

    }

    private void getValueFromConfig() {
        systemFtpInfo = new SystemFtpInfo(getActivity());
        et_ftp_ip.setText(systemFtpInfo.getFtp_ip_address());
        et_ftp_account.setText(systemFtpInfo.getFtp_iaccount());
        et_ftp_path.setText(systemFtpInfo.getFtp_path());
        et_ftp_pwd.setText(systemFtpInfo.getFtp_ipassword());
        et_ftp_josonfile.setText(systemFtpInfo.getFtp_joson_file());
        et_ftp_readme.setText(systemFtpInfo.getFtp_readme_file());


    }

    private void saveConfig() {
        systemFtpInfo.setFtp_iaccount(et_ftp_account.getText().toString());
        systemFtpInfo.setFtp_ip_address(et_ftp_ip.getText().toString());
        systemFtpInfo.setFtp_path(et_ftp_path.getText().toString());
        systemFtpInfo.setFtp_ipassword(et_ftp_pwd.getText().toString());
        systemFtpInfo.setFtp_joson_file(et_ftp_josonfile.getText().toString());
        systemFtpInfo.setFtp_readme_file(et_ftp_readme.getText().toString());
    }

    private String testFTPConnect() {
        try {
            mFtpClient = FTPToolkit
                    .makeFtpConnection(systemFtpInfo.getFtp_ip_address(), 21,
                            systemFtpInfo.getFtp_iaccount(), systemFtpInfo.getFtp_ipassword());
            return TEST_OK_STATUS;
        } catch (Exception e) {
            e.printStackTrace();
            return  e.toString();
        }

    }


    public class TestConnectFTP extends AsyncTask<String, Integer, String> {
        public TestConnectFTP() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            String flag = testFTPConnect();
            return flag;
        }


        @Override
        protected void onPostExecute(String o) {
            if (o.equals(TEST_OK_STATUS)) {
                Toast.makeText(getActivity(), "---测试成功！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), o, Toast.LENGTH_LONG).show();
            }
        }
    }
}
