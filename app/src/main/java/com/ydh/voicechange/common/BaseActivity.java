package com.ydh.voicechange.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.ydh.voicechange.HisDialog;
import com.ydh.voicechange.R;
import com.ydh.voicechange.interfaces.HisDialogInterface;
import com.ydh.voicechange.permission.PermissionListener;
import com.ydh.voicechange.permission.PermissionSetting;
import com.ydh.voicechange.permission.PermissionUtils;
import com.ydh.voicechange.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yudonghui
 * @date 2018/1/16
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * 是否隐藏标题栏
     */
    private boolean isActionBar = true;
    /**
     * 是否禁止键盘弹出
     */
    private boolean isKeyboardUp = true;
    /**
     * 是否禁止横屏 手机
     */
    private boolean isScreenRotating = true;
    /**
     * 是否显示悬浮球
     */
    private boolean isFloatingDragger = false;
    /**
     * 是否更改状态栏颜色
     */
    private boolean isStatusBarColor = true;

    protected Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);//禁止截屏
        // AppManager.getAppManager().addActivity(this);//添加堆栈
        mContext = this;
        //隐藏标题栏
        if (isActionBar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        //禁止键盘自动弹出
        if (isKeyboardUp) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        //禁止横屏android:screenOrientation="portrait"
        isScreenRotating = !isTabletDevice();
        if (isScreenRotating) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 判断是否平板设备
     *
     * @return true:平板,false:手机
     */
    private boolean isTabletDevice() {
        return (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @SuppressLint("NewApi")
    @Override
    public void setContentView(int layoutResID) {

        super.setContentView(layoutResID);
    }

    public boolean dispatchTouchEvent(MotionEvent ev, boolean isTounch) {
        if (isTounch) {
            //监控触摸事件，点击文本框外部收起键盘
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (CommonUtil.isShouldHideInput(v, ev, 0)) {
                    CommonUtil.hideSoftInput(v.getWindowToken(), this);
                    onHideSoftInput();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return dispatchTouchEvent(ev, true);
    }

    /**
     *
     */
    public void onHideSoftInput() {


    }


    /**
     * --------------------------------设置是否隐藏标题栏-------------------------------------------
     *
     * @param isActionBar 默认隐藏
     */
    public void setActionBar(boolean isActionBar) {
        this.isActionBar = isActionBar;
    }

    /**
     * --------------------------------设置是否更改状态栏颜色---------------------------------------
     *
     * @param isStatusBarColor 默认状态栏颜色与标题颜色一致
     */
    public void setStatusBarColor(boolean isStatusBarColor) {
        this.isStatusBarColor = isStatusBarColor;
    }

    /**
     * --------------------------------设置是否禁止键盘弹出-----------------------------------------
     *
     * @param isKeyboardUp 默认显示
     */
    public void setKeyboardUp(boolean isKeyboardUp) {
        this.isKeyboardUp = isKeyboardUp;
    }

    /**
     * --------------------------------设置是否禁止横屏---------------------------------------------
     *
     * @param isScreenRotating 默认禁止横屏
     */
    public void setScreenRotating(boolean isScreenRotating) {
        this.isScreenRotating = isScreenRotating;
    }

    /**
     * --------------------------------设置是否显示悬浮球-------------------------------------------
     *
     * @param isFloatingDragger 默认隐藏悬浮球
     */
    public void setFloatingDragger(boolean isFloatingDragger) {
        this.isFloatingDragger = isFloatingDragger;
    }


    /**
     * 跳转页面
     *
     * @param clz 跳转的activity
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }

    /**
     * 携带数据的页面跳转
     *
     * @param clz    跳转的activity
     * @param bundle 数据
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 携带String的页面跳转(TAG)
     *
     * @param clz   跳转的activity
     * @param tag   标识
     * @param value 值
     */
    public void startActivity(Class<?> clz, String tag, String value) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (!TextUtils.isEmpty(value)) {
            intent.putExtra(tag, value);
        }
        startActivity(intent);
    }


    protected void dataResult(int num, TextView noData) {
        if (noData == null) return;
        if (num == 0) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }
    }

    public void startActivityForResult(Class<?> clz, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Class<?> clz, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        startActivityForResult(intent, requestCode);
    }


    //申请权限之后需要执行的方法。需要的时候再重写
    public void permissonExcute(int requestCode) {
    }

    //申请权限没有全部允许，让去设置界面自己去设置，点击了不
    public void permissonCancel() {

    }
    protected Dialog mLoadingDialog;

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.dialog_load, null);
            mLoadingDialog = new Dialog(this, R.style.LoadDialog);

            Window window = mLoadingDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            mLoadingDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            TextView mTextView = itemView.findViewById(R.id.tv_hint);
            mTextView.setVisibility(View.GONE);
            mLoadingDialog.setContentView(itemView);
        }
        if (!isFinishing())
            mLoadingDialog.show();
    }

    public void cancelLoadingDialog() {
        if (mLoadingDialog != null && !isFinishing())
            mLoadingDialog.dismiss();
    }
    private PermissionListener mlistener;

    /**
     * 权限申请
     *
     * @param permissions 待申请的权限集合
     * @param listener    申请结果监听事件
     */
    private void requestRunTimePermission(String[] permissions, PermissionListener listener) {
        mlistener = listener;
        Activity topActivity = this;
        if (topActivity == null) {
            return;
        }
        //用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        //遍历传递过来的权限集合
        for (String permission : permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(topActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission);
            }
        }

        //判断集合
        if (!permissionList.isEmpty()) {  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(topActivity, permissionList.toArray(new String[permissionList.size()]), perRequestCode);
        } else {  //为空，则已经全部授权
            listener.onGranted();
        }
    }

    public int perRequestCode;


    /**
     * 权限申请结果
     *
     * @param requestCode  请求码
     * @param permissions  所有的权限集合
     * @param grantResults 授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == perRequestCode) {
            if (grantResults.length > 0) {
                //被用户拒绝的权限集合
                List<String> deniedPermissions = new ArrayList<>();
                //用户通过的权限集合
                List<String> grantedPermissions = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    //获取授权结果，这是一个int类型的值
                    int grantResult = grantResults[i];

                    if (grantResult != PackageManager.PERMISSION_GRANTED) { //用户拒绝授权的权限
                        String permission = permissions[i];
                        deniedPermissions.add(permission);
                    } else {  //用户同意的权限
                        String permission = permissions[i];
                        grantedPermissions.add(permission);
                    }
                }

                if (deniedPermissions.isEmpty()) {  //全部申请成功
                    mlistener.onGranted();
                } else {  //部分权限申请成功
                    //回调授权成功的接口
                    mlistener.onDenied(deniedPermissions);
                    //回调授权失败的接口
                    mlistener.onGranted(grantedPermissions);
                }
            }
        }
    }


    public void requestPermission(final String[] persssions, int requestCode) {
        this.perRequestCode = requestCode;
        final PermissionSetting ps = new PermissionSetting(this);
        requestRunTimePermission(persssions
                , new PermissionListener() {
                    @Override
                    public void onGranted() {//全部申请成功的回调
                        permissonExcute(perRequestCode);
                    }

                    @Override
                    public void onGranted(List<String> grantedPermission) {//部分成功权限

                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {//部分未成功权限
                        String message = "我们需要以下权限，请在设置中为我们开启：" + "\n" + PermissionUtils.getName(deniedPermission);
                        new HisDialog.Builder().title("提示")
                                .message(message)
                                .cancel("不用了", new HisDialogInterface() {
                                    @Override
                                    public void callBack(View view) {
                                        ps.cancel();
                                        permissonCancel();
                                    }
                                })
                                .confirm("设置", new HisDialogInterface() {
                                    @Override
                                    public void callBack(View view) {
                                        ps.execute(6667);
                                    }
                                })
                                .build(BaseActivity.this);
                    }
                });
    }
}
