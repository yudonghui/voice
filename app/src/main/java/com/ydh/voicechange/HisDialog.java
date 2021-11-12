package com.ydh.voicechange;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydh.voicechange.interfaces.HisDialogInterface;

public class HisDialog {
    /**
     * 正常情况
     */
    public final static int DIALOG_NORMAL = 1;
    /**
     * 内容是个编辑框
     */
    public final static int DIALOG_EDITE = 2;
    /**
     * 内容是个编辑框.只允许输入数字和小数点
     */
    public final static int DIALOG_EDITE_NUM = 3;

    public Dialog mHintDialog;
    private TextView mTvCancel;
    private TextView mTvConfirm;
    private TextView mMessage;
    private EditText mEditeText;
    private EditText mEditeNumber;
    private TextView mTitle;
    private LinearLayout mLlBtn;

    public HisDialog(Context mContext, Builder builder) {
        mHintDialog = new Dialog(mContext, R.style.HintDialog);
        mHintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(mContext, R.layout.dialog_his, null);
        mTitle = (TextView) view.findViewById(R.id.title);
        mMessage = (TextView) view.findViewById(R.id.message);
        mEditeText = view.findViewById(R.id.ettext);
        mEditeNumber = view.findViewById(R.id.etnumber);
        mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        mTvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        setView(builder);
        mHintDialog.setContentView(view);
        mHintDialog.show();
    }


    public void show() {
        if (mHintDialog != null) {
            mHintDialog.show();
        }
    }



    private void setView(final Builder builder) {
        mHintDialog.setCancelable(builder.isCancel);
        /**
         *标题
         */
        if (TextUtils.isEmpty(builder.title)) {
            mTitle.setVisibility(View.GONE);
        } else {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(builder.title);
        }
        mMessage.setGravity(builder.gravity);
        /**
         *根据不中间显示的内容进行划分
         * 正常显示文本
         * 编辑框
         * 编辑框只能输入数字和小数点
         */
        if (builder.type == DIALOG_NORMAL) {
            if (TextUtils.isEmpty(builder.message)) {
                mMessage.setVisibility(View.GONE);
            } else {
                mMessage.setVisibility(View.VISIBLE);
                mMessage.setText(builder.message);
            }
        } else if (builder.type == DIALOG_EDITE) {
            mEditeText.setVisibility(View.VISIBLE);
            mEditeText.setHint(builder.hint);
        } else if (builder.type == DIALOG_EDITE_NUM) {
            mEditeNumber.setVisibility(View.VISIBLE);
            mEditeNumber.setHint(builder.hint);
        }
        /**
         *取消按钮
         */
        if (TextUtils.isEmpty(builder.cancel)) {
            mTvCancel.setVisibility(View.GONE);
        } else {
            mTvCancel.setVisibility(View.VISIBLE);
            mTvCancel.setBackgroundResource(R.drawable.btn_gray_b_10);
            mTvCancel.setText(builder.cancel);
        }
        /**
         *中间显示的信息是否可复制
         */
        mMessage.setTextIsSelectable(builder.textIsSelectable);
        /**
         *确定按钮
         */
        if (TextUtils.isEmpty(builder.confirm)) {
            mTvConfirm.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(builder.cancel)) {
                mTvConfirm.setBackgroundResource(R.drawable.btn_green_r_l_10);
            } else {
                mTvConfirm.setBackgroundResource(R.drawable.shape_theme_b_10);
            }
            mTvConfirm.setVisibility(View.VISIBLE);
            mTvConfirm.setText(builder.confirm);
        }

        /**
         *相关点击事件
         */
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
                if (builder.mCancelListener != null)
                    builder.mCancelListener.callBack(v);
            }
        });
        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintDialog.dismiss();
                if (builder.mConfirmListener != null) {
                    if (builder.type == DIALOG_EDITE) {
                        builder.mConfirmListener.callBack(mEditeText);
                    } else if (builder.type == DIALOG_EDITE_NUM) {
                        builder.mConfirmListener.callBack(mEditeNumber);
                    } else {
                        builder.mConfirmListener.callBack(v);
                    }

                }
            }
        });
    }


    public static class Builder {
        private int type = DIALOG_NORMAL;//内容的类型
        private String title;
        /**
         * 正常中间是显示文本
         */
        private String message;
        /**
         * type是DIALOG_EDITE的时候中间是个编辑框，
         * 这个时候可以设置一个提示语
         */
        private String hint = "请输入内容";
        /**
         * 是否可以取消dialog。false是不可取消.默认可以取消
         */
        private boolean isCancel = true;
        private String confirm;
        private String cancel;
        private HisDialogInterface mConfirmListener;
        private HisDialogInterface mCancelListener;
        private String leftBtnText;
        private String middleBtnText;
        private String rightBtnText;
        private HisDialogInterface mLeftBtnListener;
        private HisDialogInterface mRightBtnListener;
        private HisDialogInterface mMiddleBtnListener;

        private int gravity = Gravity.CENTER_VERTICAL;//文本显示的时候文字的位置
        private boolean textIsSelectable = false;

        public Builder textIsSelectable(boolean textIsSelectable) {
            this.textIsSelectable = textIsSelectable;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder isCancel(boolean isCancel) {
            this.isCancel = isCancel;
            return this;
        }

        public Builder confirm(String confirm, HisDialogInterface mListener) {
            this.confirm = confirm;
            this.mConfirmListener = mListener;
            return this;
        }

        public Builder cancel(String cancel, HisDialogInterface mListener) {
            this.cancel = cancel;
            this.mCancelListener = mListener;
            return this;
        }

        public Builder rightBtn(String confirm, HisDialogInterface mListener) {
            this.rightBtnText = confirm;
            this.mRightBtnListener = mListener;
            return this;
        }

        public Builder rightBtn(String confirm) {
            this.rightBtnText = confirm;
            return this;
        }

        public Builder middleBtn(String confirm, HisDialogInterface mListener) {
            this.middleBtnText = confirm;
            this.mMiddleBtnListener = mListener;
            return this;
        }

        public Builder leftBtn(String confirm, HisDialogInterface mListener) {
            this.leftBtnText = confirm;
            this.mLeftBtnListener = mListener;
            return this;
        }

        public Builder confirm(String confirm) {
            this.confirm = confirm;
            return this;
        }

        public Builder cancel(String cancel) {
            this.cancel = cancel;
            return this;
        }

        public HisDialog build(Context mContext) {
            return new HisDialog(mContext, this);
        }
    }
}
