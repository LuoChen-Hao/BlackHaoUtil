package com.balckhao.blackhaoutil.usbUtil;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.balckhao.blackhaoutil.R;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.UsbFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UsbTestActivity extends AppCompatActivity implements USBBroadCastReceiver.UsbListener {

    @Bind(R.id.local_backspace_iv)
    ImageButton localBackspaceIv;
    @Bind(R.id.local_file_lv)
    ListView localFileLv;
    @Bind(R.id.usb_backspace_iv)
    ImageButton usbBackspaceIv;
    @Bind(R.id.usb_file_lv)
    ListView usbFileLv;
    @Bind(R.id.show_progress_tv)
    TextView showProgressTv;

    //本地文件列表相关
    private ArrayList<File> localList;
    private FileListAdapter<File> localAdapter;
    private String localRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String localCurrentPath = "";
    //USB文件列表相关
    private ArrayList<UsbFile> usbList;
    private FileListAdapter<UsbFile> usbAdapter;
    private UsbHelper usbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_test);
        ButterKnife.bind(this);
        initLocalFile();
        initUsbFile();
    }

    /**
     * 初始化本地文件列表
     */
    private void initLocalFile() {
        localList = new ArrayList<>();
        Collections.addAll(localList, new File(localRootPath).listFiles());
        localCurrentPath = localRootPath;
        localAdapter = new FileListAdapter<>(this, localList);
        localFileLv.setAdapter(localAdapter);
        localFileLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openLocalFile(localList.get(position));
            }
        });
    }

    /**
     * 初始化 USB文件列表
     */
    private void initUsbFile() {
        usbHelper = new UsbHelper(this, this);
        usbList = new ArrayList<>();
        usbAdapter = new FileListAdapter<>(this, usbList);
        usbFileLv.setAdapter(usbAdapter);
        usbFileLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UsbFile file = usbList.get(position);
                openUsbFile(file);
            }
        });
        updateUsbFile(0);
    }

    /**
     * 打开本地 File
     *
     * @param file File
     */
    private void openLocalFile(File file) {
        if (file.isDirectory()) {
            //文件夹更新列表
            localList.clear();
            Collections.addAll(localList, file.listFiles());
            localAdapter.notifyDataSetChanged();
            localCurrentPath = file.getAbsolutePath();
        } else {
            //开启线程，将文件复制到本地
            copyLocalFile(file);
        }
    }

    /**
     * 更新 USB 文件列表
     */
    private void updateUsbFile(int position) {
        UsbMassStorageDevice[] usbMassStorageDevices = usbHelper.getDeviceList();
        if (usbMassStorageDevices.length > 0) {
            //存在USB
            usbList.clear();
            usbList.addAll(usbHelper.readDevice(usbMassStorageDevices[position]));
            usbAdapter.notifyDataSetChanged();
        } else {
            Log.e("UsbTestActivity", "No Usb Device");
            usbList.clear();
            usbAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 复制 USB 文件到本地
     *
     * @param file USB文件
     */
    private void copyLocalFile(final File file) {
        //复制到本地的文件路径
        new Thread(new Runnable() {
            @Override
            public void run() {
                //复制结果
                final boolean result = usbHelper.saveSDFileToUsb(file, usbHelper.getCurrentFolder(), new UsbHelper.DownloadProgressListener() {
                    @Override
                    public void downloadProgress(final int progress) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String text = "From Local : " + localCurrentPath
                                        + "\nTo Usb : " + usbHelper.getCurrentFolder().getName()
                                        + "\nProgress : " + progress;
                                showProgressTv.setText(text);
                            }
                        });
                    }
                });
                //主线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result) {
                            openUsbFile(usbHelper.getCurrentFolder());
                        } else {
                            Toast.makeText(UsbTestActivity.this, "复制失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 打开 USB File
     *
     * @param file USB File
     */
    private void openUsbFile(UsbFile file) {
        if (file.isDirectory()) {
            //文件夹更新列表
            usbList.clear();
            usbList.addAll(usbHelper.getUsbFolderFileList(file));
            usbAdapter.notifyDataSetChanged();
        } else {
            //开启线程，将文件复制到本地
            copyUSbFile(file);
        }
    }

    /**
     * 复制 USB 文件到本地
     *
     * @param file USB文件
     */
    private void copyUSbFile(final UsbFile file) {
        //复制到本地的文件路径
        final String filePath = localCurrentPath + File.separator + file.getName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //复制结果
                final boolean result = usbHelper.saveUSbFileToLocal(file, filePath, new UsbHelper.DownloadProgressListener() {
                    @Override
                    public void downloadProgress(final int progress) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String text = "From Usb " + usbHelper.getCurrentFolder().getName()
                                        + "\nTo Local " + localCurrentPath
                                        + "\n Progress : " + progress;
                                showProgressTv.setText(text);
                            }
                        });
                    }
                });
                //主线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result) {
                            openLocalFile(new File(localCurrentPath));
                        } else {
                            Toast.makeText(UsbTestActivity.this, "复制失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_backspace_iv:
                if (!localCurrentPath.equals(localRootPath)) {
                    //不是根目录，返回上一目录
                    openLocalFile(new File(localCurrentPath).getParentFile());
                }
                break;
            case R.id.usb_backspace_iv:
                if (usbHelper.getParentFolder() != null) {
                    //不是根目录，返回上一目录
                    openUsbFile(usbHelper.getParentFolder()
                    );
                }
                break;
        }
    }

    @Override
    public void insertUsb(UsbDevice device_add) {
        if (usbList.size() == 0) {
            updateUsbFile(0);
        }
    }

    @Override
    public void removeUsb(UsbDevice device_remove) {
        updateUsbFile(0);
    }

    @Override
    public void getReadUsbPermission(UsbDevice usbDevice) {

    }

    @Override
    public void failedReadUsb(UsbDevice usbDevice) {

    }

    @Override
    protected void onDestroy() {
        usbHelper.finishUsbHelper();
        super.onDestroy();
    }
}
