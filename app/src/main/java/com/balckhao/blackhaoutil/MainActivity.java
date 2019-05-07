package com.balckhao.blackhaoutil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.balckhao.blackhaoutil.base.test.TestActivity;
import com.balckhao.blackhaoutil.mvp.test.MvpTestActivity;
import com.balckhao.blackhaoutil.usbUtil.UsbTestActivity;
import com.blackhao.utillibrary.file.FileUtil;
import com.blackhao.utillibrary.log.LogHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private File temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.usb_test:
                Intent usbIntent = new Intent(this, UsbTestActivity.class);
                startActivity(usbIntent);
                break;
            case R.id.log_base_test:
                Intent logIntent = new Intent(this, TestActivity.class);
                startActivity(logIntent);
                break;
            case R.id.mvp_base_test:
                startActivity(new Intent(this, MvpTestActivity.class));
                break;
            case R.id.create_temp:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String text = "123456789qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq" +
                                "qwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww" +
                                "ertyuioplkjhgfdsazxcvbbnmnmpoiweqasdjashfasjc nzxkcnahfdasd,as nfasdnask";
                        try {
                            temp = FileUtil.getTempFile(MainActivity.this, "aaaa.txt");
                            FileOutputStream fos = new FileOutputStream(temp);
                            fos.write(text.getBytes());
                            fos.flush();
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogHelper.getInstance().e(e.toString());
                        }
                    }
                }).start();
                break;
            case R.id.read_temp:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileInputStream fis = new FileInputStream(
                                    temp);
                            byte[] data = new byte[fis.available()];
                            fis.read(data);
                            fis.close();
                            String text = new String(data, "utf-8");
                            LogHelper.getInstance().e(text);
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogHelper.getInstance().e(e.toString());
                        }
                    }
                }).start();
                break;
            case R.id.copy_folder:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //未授权，提起权限申请
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                        FileUtil.copyFolder(root + "/QChat", root + "/QChatTest");
                        LogHelper.getInstance().e("copy finish ");
                    }
                }).start();
                break;
            case R.id.del_folder:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                        FileUtil.delFileBeforeTime(root + "/test", System.currentTimeMillis());
                        LogHelper.getInstance().e("delete finish ");
                    }
                }).start();
                break;
            case R.id.rename:
                String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                FileUtil.modifyFileName(root + "/test", "QChatRename");
                break;
        }
    }
}
