package personal.wl.jspos.method;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import personal.wl.jspos.R;

import static personal.wl.jspos.method.PosHandleDB.CheckAccountPassword;

public class PosLogin {
    private View mPopupHeadViewy;//创建一个view
    private PopupWindow mHeadPopupclly;//PopupWindow
    private EditText userpass;
    private EditText useraccount;

    private TextView title;
    private Button textlogin, textlogout;//title,打折
    private String ADMINPASSWORD = "160023";
    private Context context;
    private View view;
    private PosTabInfo posTabInfo;

    View focusView = null;

    public PosLogin(Context context, View view) {
        this.context = context;
        this.view = view;
        posTabInfo = new PosTabInfo(context);

    }


//    public void ShowAccountLogin() {
//        CommonPopupWindow window = new CommonPopupWindow(context, R.layout.adminlogin, ViewGroup.LayoutParams.MATCH_PARENT, (int) (getScreenWidth() * 0.7))
//        {
//            @Override
//            public View getContentView() {
//                return super.getContentView();
//            }
//
//            @Override
//            public PopupWindow getPopupWindow() {
//                return super.getPopupWindow();
//            }
//
//            @Override
//            protected void initView() {
//
//            }
//
//            @Override
//            protected void initEvent() {
//
//            }
//
//            @Override
//            protected void initWindow() {
//                super.initWindow();
//                PopupWindow instance=getPopupWindow();
//                instance.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                    }
//                });
//            }
//        };
//
//    }

    public void ShowAccountLogin() {
        mPopupHeadViewy = View.inflate(context, R.layout.login, null);
        useraccount = mPopupHeadViewy.findViewById(R.id.login_account);
        userpass = mPopupHeadViewy.findViewById(R.id.login_password);

        textlogin = mPopupHeadViewy.findViewById(R.id.login_login);
        textlogout = mPopupHeadViewy.findViewById(R.id.login_logoff);
        mHeadPopupclly = new PopupWindow(mPopupHeadViewy, (int) (getScreenWidth() * 0.5), -2, true);
        // 在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mHeadPopupclly.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);


        mHeadPopupclly.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mHeadPopupclly.setBackgroundDrawable(new BitmapDrawable());
        mHeadPopupclly.setOutsideTouchable(true);
        mHeadPopupclly.showAtLocation(view, Gravity.CENTER, 0, 0);
//        mHeadPopupclly.showAsDropDown(view, 0, -(int) (getScreenHeight() * 0.6));
        textlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AttemptLogin()) {
                    Toast.makeText(context, "登陆成功！", Toast.LENGTH_LONG).show();
                    posTabInfo.setSalerid(useraccount.getText().toString());
                    mHeadPopupclly.dismiss();
                } else {
                    Toast.makeText(context, "密码错误", Toast.LENGTH_LONG).show();
                    focusView = userpass;
                }
            }
        });
        textlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeadPopupclly.dismiss();
                Toast.makeText(context, "取消", Toast.LENGTH_LONG).show();
            }
        });
    }


    public Boolean AttemptLogin() {

        String account = useraccount.getText().toString();
        String password = userpass.getText().toString();
        Drawable errorIcon = context.getResources().getDrawable(R.drawable.ic_error_black_24dp);

        if (TextUtils.isEmpty(account)) {
            Toast.makeText(context, "账号为空", Toast.LENGTH_LONG).show();
//            useraccount.setError("账号为空",errorIcon);
            focusView = useraccount;
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "密码为空", Toast.LENGTH_LONG).show();
            focusView = userpass;
            return false;
        }

        PosTabInfo posTabInfo = new PosTabInfo(context);
        HashMap<String, String> tmp_login = new HashMap<String, String>();
        tmp_login.put("branchid", posTabInfo.getBranchCode());
        tmp_login.put("accountid", account);
        tmp_login.put("password", password);

        return CheckAccountPassword(tmp_login);
    }


    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

}
