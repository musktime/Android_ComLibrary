package com.musk.lib.ui;

import com.musk.lib.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class MyDialog extends Dialog {

    public MyDialog(Context context) {
        super(context);
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String positiveButText;
        private String negativeButText;
        @SuppressWarnings("unused")
        private View contentView;
        private OnClickListener positiveClickListener;
        private OnClickListener negativeClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPoistiveButton(int PositiveTextId,
                                         OnClickListener positiveClickListener) {
            this.positiveButText = (String) context.getText(PositiveTextId);
            this.positiveClickListener = positiveClickListener;
            return this;
        }

        public Builder setNagetiveButton(int nagetiveTextId,
                                         OnClickListener nagetiveClickListener) {
            this.negativeButText = (String) context.getText(nagetiveTextId);
            this.negativeClickListener = nagetiveClickListener;
            return this;
        }

        public MyDialog create(OnDismissListener listener) {
            final MyDialog dialog = new MyDialog(context, R.style.Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_my_dialog, null);
            dialog.addContentView(view, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            Button positiveBut = (Button) view.findViewById(R.id.btn_confirm);
            Button nagetiveBut = (Button) view.findViewById(R.id.btn_cancel);
            if (positiveButText != null) {
                positiveBut.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        positiveClickListener.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            } else {
                positiveBut.setVisibility(View.GONE);
            }
            if (negativeButText != null) {
                nagetiveBut.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        negativeClickListener.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                        dialog.dismiss();
                    }
                });
            } else {
                nagetiveBut.setVisibility(View.GONE);
            }
            dialog.setContentView(view);
            dialog.setOnDismissListener(listener);
            return dialog;
        }
    }
}
