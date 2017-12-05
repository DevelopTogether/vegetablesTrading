package com.vegetablestrading.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.vegetablestrading.R;
import com.vegetablestrading.activity.MineMode.ActivateUserActivity;
import com.vegetablestrading.bean.MyApply;
import com.vegetablestrading.bean.TransportRecord;

import java.util.regex.Pattern;

/**
 * Created by ${王sir} on 2017/11/7.
 * application
 */

public class PublicUtils {

    public static int app_width = 0;
    public static int app_height = 0;
    public static TransportRecord transportRecord;//配送记录点击的实体类
    public static MyApply myApply;//申请详情实体类
    private static String USERINFO = "USER_INFO";//用户信息标识，用于存储sp
    private static String ACTIVATED = "ACTIVATED";//用户激活状态，用于存储sp


    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][3758]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 规则：必须同时包含数字和字母（大写或小写）
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
        boolean isUpperCase = false;
        if (str == null || TextUtils.isEmpty(str)) {
            return false;
        } else {
            if (str.length() > 5 && str.length() < 13) {
                for (int i = 0; i < str.length(); i++) {
                    if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                        isDigit = true;
                    } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                        isLowerCase = true;
                    } else if (Character.isUpperCase(str.charAt(i))) {
                        isUpperCase = true;
                    }
                }
            } else {
                return false;
            }


        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = (isUpperCase || isLowerCase) && isDigit && str.matches(regex);
        return isRight;
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    public static boolean isConnected(Context context) {
        boolean isOk = true;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetInfo != null && !wifiNetInfo.isConnectedOrConnecting()) {
                if (mobNetInfo != null && !mobNetInfo.isConnectedOrConnecting()) {
                    NetworkInfo info = connectivityManager
                            .getActiveNetworkInfo();
                    if (info == null) {
                        isOk = false;
                    }
                }
            }
            mobNetInfo = null;
            wifiNetInfo = null;
            connectivityManager = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOk;
    }

    /**
     * 设置状态栏的背景色
     *
     * @param context
     * @param color
     */
    public static void setStatusBarBg(Activity context, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = context.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            context.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 描述：dip转换为px.
     *
     * @param context the context
     * @return px值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 查询激活状态
     *
     * @param context
     * @return
     */
    public static boolean getStatusOfActivated(Context context) {
        SharedPreferencesHelper sph = new SharedPreferencesHelper(context, USERINFO);
        return sph.getBoolean(ACTIVATED, true);
    }

    /**
     * 提醒用户激活
     * @param context
     */
    private static Dialog warnActivate(final Context context) {

        View v = LayoutInflater.from(context).inflate(R.layout.warn_activate_layout
                , null);
        final Dialog dialog_toWarn = new Dialog(context, R.style.DialogStyle);
        dialog_toWarn.setCanceledOnTouchOutside(false);
        dialog_toWarn.setCancelable(false);
        Window window = dialog_toWarn.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//        lp.width = dip2px(this, 300); // 宽度
//        lp.height = dip2px(this, 160); // 高度
        // lp.alpha = 0.7f; // 透明度
        window.setAttributes(lp);
        window.setContentView(v);
        dialog_toWarn.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        dialog_toWarn.dismiss();
                    }
                }
                return false;
            }
        });
        final TextView warn_activate_tv = (TextView) v.findViewById(R.id.warn_activate_tv);
        final TextView activate_tv = (TextView) v.findViewById(R.id.activate_tv);
        warn_activate_tv.setText(R.string.warn_activated_status);
        activate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_toWarn.dismiss();
                context.startActivity(new Intent(context, ActivateUserActivity.class));
            }
        });
        return dialog_toWarn;
    }

    /**
     * 提醒用户激活的dialog
     * @param context
     */
    public static void warnActivateDialog(Context context){
        Dialog dialog =warnActivate(context);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 更改EditText中hint字体的大小
     * @param hintContent
     * @return
     */
    public static void getEdittextHint(EditText et ,String hintContent){
        SpannableString spanString = new SpannableString(hintContent);
//        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
//        spanString.setSpan(span, 2, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        AbsoluteSizeSpan spanSize = new AbsoluteSizeSpan(14, true);//true代表dp单位生效
        spanString.setSpan(spanSize, 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        et.setHint(new SpannedString(spanString));
    }
}