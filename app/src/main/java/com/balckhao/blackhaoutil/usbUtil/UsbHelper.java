package com.balckhao.blackhaoutil.usbUtil;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;
import com.github.mjdev.libaums.partition.Partition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static com.balckhao.blackhaoutil.usbUtil.USBBroadCastReceiver.ACTION_USB_PERMISSION;


/**
 * Author ： BlackHao
 * Time : 2018/5/10 14:53
 * Description : USB 操作工具类
 * Logs :
 */

public class UsbHelper {

    //上下文对象
    private Context context;
    //USB 设备列表
    private UsbMassStorageDevice[] storageDevices;
    //USB 广播
    private USBBroadCastReceiver mUsbReceiver;
    //回调
    private USBBroadCastReceiver.UsbListener usbListener;
    //当前路径
    private UsbFile currentFolder = null;
    //TAG
    private static String TAG = "UsbHelper";

    public UsbHelper(Context context, USBBroadCastReceiver.UsbListener usbListener) {
        this.context = context;
        this.usbListener = usbListener;
        //注册广播
        registerReceiver();
    }

    /**
     * 注册 USB 监听广播
     */
    private void registerReceiver() {
        mUsbReceiver = new USBBroadCastReceiver();
        mUsbReceiver.setUsbListener(usbListener);
        //监听otg插入 拔出
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(mUsbReceiver, usbDeviceStateFilter);
        //注册监听自定义广播
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbReceiver, filter);
    }

    /**
     * 读取 USB设备列表
     *
     * @return USB设备列表
     */
    public UsbMassStorageDevice[] getDeviceList() {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        //获取存储设备
        storageDevices = UsbMassStorageDevice.getMassStorageDevices(context);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        //可能有几个 一般只有一个 因为大部分手机只有1个otg插口
        for (UsbMassStorageDevice device : storageDevices) {
//            Log.e(TAG, device.getUsbDevice().getDeviceName());
            //有就直接读取设备是否有权限
            if (!usbManager.hasPermission(device.getUsbDevice())) {
                //没有权限请求权限
                usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
            }
        }
        return storageDevices;
    }

    /**
     * 获取device 根目录文件
     *
     * @param device USB 存储设备
     * @return 设备根目录下文件列表
     */
    public ArrayList<UsbFile> readDevice(UsbMassStorageDevice device) {
        ArrayList<UsbFile> usbFiles = new ArrayList<>();
        try {
            //初始化
            device.init();
            //获取partition
            Partition partition = device.getPartitions().get(0);
            FileSystem currentFs = partition.getFileSystem();
            //获取根目录
            UsbFile root = currentFs.getRootDirectory();
            currentFolder = root;
            //将文件列表添加到ArrayList中
            Collections.addAll(usbFiles, root.listFiles());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usbFiles;
    }

    /**
     * 读取 USB 内文件夹下文件列表
     *
     * @param usbFolder usb文件夹
     * @return 文件列表
     */
    public ArrayList<UsbFile> getUsbFolderFileList(UsbFile usbFolder) {
        //更换当前目录
        currentFolder = usbFolder;
        ArrayList<UsbFile> usbFiles = new ArrayList<>();
        try {
            Collections.addAll(usbFiles, usbFolder.listFiles());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usbFiles;
    }


    /**
     * 复制文件到 USB
     *
     * @param targetFile       需要复制的文件
     * @param saveFolder       复制的目标文件夹
     * @param progressListener 下载进度回调
     * @return 复制结果
     */
    public boolean saveSDFileToUsb(File targetFile, UsbFile saveFolder, DownloadProgressListener progressListener) {
        boolean result;
        try {
            //USB文件是否存在
            boolean isExist = false;
            UsbFile saveFile = null;
            for (UsbFile usbFile : saveFolder.listFiles()) {
                if (usbFile.getName().equals(targetFile.getName())) {
                    isExist = true;
                    saveFile = usbFile;
                }
            }
            if (isExist) {
                //文件已存在，删除文件
                saveFile.delete();
            }
            //创建新文件
            saveFile = saveFolder.createFile(targetFile.getName());
            //开始写入
            FileInputStream fis = new FileInputStream(targetFile);//读取选择的文件的
            int avi = fis.available();
            UsbFileOutputStream uos = new UsbFileOutputStream(saveFile);
            int bytesRead;
            byte[] buffer = new byte[1024 * 8];
            int writeCount = 0;
            while ((bytesRead = fis.read(buffer)) != -1) {
                uos.write(buffer, 0, bytesRead);
                writeCount += bytesRead;
//                Log.e(TAG, "Progress : " + (writeCount * 100 / avi));
                if (progressListener != null) {
                    //回调下载进度
                    progressListener.downloadProgress(writeCount * 100 / avi);
                }
            }
            uos.flush();
            fis.close();
            uos.close();
            result = true;
        } catch (final Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 复制 USB文件到本地
     *
     * @param targetFile       需要复制的文件
     * @param savePath         复制的目标文件路径
     * @param progressListener 下载进度回调
     * @return 复制结果
     */
    public boolean saveUSbFileToLocal(UsbFile targetFile, String savePath,
                                      DownloadProgressListener progressListener) {
        boolean result;
        try {
            //开始写入
            UsbFileInputStream uis = new UsbFileInputStream(targetFile);//读取选择的文件的
            FileOutputStream fos = new FileOutputStream(savePath);
            //这里uis.available一直为0
//            int avi = uis.available();
            long avi = targetFile.getLength();
            int writeCount = 0;
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = uis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                writeCount += bytesRead;
//                Log.e(TAG, "Progress : write : " + writeCount + " All : " + avi);
                if (progressListener != null) {
                    //回调下载进度
                    progressListener.downloadProgress((int) (writeCount * 100 / avi));
                }
            }
            fos.flush();
            uis.close();
            fos.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 获取上层目录文件夹
     *
     * @return usbFile : 父目录文件 / null ：无父目录
     */
    public UsbFile getParentFolder() {
        if (currentFolder != null && !currentFolder.isRoot()) {
            return currentFolder.getParent();
        } else {
            return null;
        }
    }


    /**
     * 获取当前 USBFolder
     */
    public UsbFile getCurrentFolder() {
        return currentFolder;
    }

    /**
     * 退出 UsbHelper
     */
    public void finishUsbHelper() {
        context.unregisterReceiver(mUsbReceiver);
    }

    /**
     * 下载进度回调
     */
    public interface DownloadProgressListener {
        void downloadProgress(int progress);
    }
}
