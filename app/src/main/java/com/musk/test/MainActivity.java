package com.musk.test;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.musk.demo.R;
import com.musk.lib.ui.ProgressView;
import com.musk.lib.ui.MyDialog;

public class MainActivity extends AppCompatActivity {

    private int mTotalProgress = 100;
    private int mCurrentProgress = 0;
    //进度条
    private ProgressView mTasksView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTasksView = (ProgressView) findViewById(R.id.tasks_view);
        new Thread(new ProgressRunable()).start();
    }

    private void test(){
       MyDialog dialog= new MyDialog.Builder(this).create(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
        dialog.show();
    }
    class ProgressRunable implements Runnable {
        @Override
        public void run() {
            while (mCurrentProgress < mTotalProgress) {
                mCurrentProgress += 1;
                mTasksView.setProgress(mCurrentProgress);
                try {
                    Thread.sleep(90);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
