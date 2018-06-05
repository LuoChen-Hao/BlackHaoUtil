package com.balckhao.blackhaoutil.usbUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.balckhao.blackhaoutil.R;
import com.github.mjdev.libaums.fs.UsbFile;

import java.io.File;
import java.util.List;

/**
 * Author ： BlackHao
 * Time : 2018/5/10 14:53
 * Description : File/USBFile 列表 adapter
 * Logs :
 */
public class FileListAdapter<T> extends BaseAdapter {

    private List<T> list;

    private Context context;

    public FileListAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_usb_file_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        T t = list.get(position);
        if (t instanceof UsbFile) {
            UsbFile usbFile = (UsbFile) t;
            //设置文件ICON
            holder.icon.setImageResource(getResIdFromFileName(usbFile.isDirectory(),
                    usbFile.getName()));
            holder.tv.setText(usbFile.getName());
            if (!usbFile.isDirectory()) {
                holder.fileSizeTv.setText(getFileSize(usbFile.getLength()));
            } else {
                holder.fileSizeTv.setText("");
            }
        } else if (t instanceof File) {
            File file = (File) t;
            //设置文件ICON
            holder.icon.setImageResource(getResIdFromFileName(file.isDirectory(),
                    file.getName()));
            holder.tv.setText(file.getName());
            if (!file.isDirectory()) {
                holder.fileSizeTv.setText(getFileSize(file.length()));
            } else {
                holder.fileSizeTv.setText("");
            }
        }
        return convertView;
    }

    /**
     * 文件大小转换
     *
     * @param size 文件大小
     * @return 文件大小（B / KB / MB）
     */
    private String getFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return (size / 1024) + " kB";
        } else {
            return (size / 1024 / 1024) + " MB";
        }
    }

    /**
     * 通过文件名（路径）返回对应的 ICON 资源 ID
     *
     * @param isFolder 是否是文件夹
     * @param fileName 文件名（路径）
     * @return 对应的资源ID
     */
    private static int getResIdFromFileName(boolean isFolder, String fileName) {
        fileName = fileName.toLowerCase();
        if (isFolder) {
            //文件夹
            return R.drawable.folder;
        } else {
            if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                //图片文件
                return R.drawable.image;
            } else if (fileName.endsWith(".txt")) {
                //TXT 文件
                return R.drawable.file;
            } else if (fileName.endsWith(".pdf")) {
                //PDF 文件
                return R.drawable.pdf;
            } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                return R.drawable.xls;
            } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
                return R.drawable.ppt;
            } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
                return R.drawable.doc;
            } else if (fileName.endsWith(".mp4") || fileName.endsWith(".avi")) {
                //视频文件
                return R.drawable.video;
            } else {
                //未知文件
                return R.drawable.unkown_file;
            }
        }
    }

    private class ViewHolder {

        private TextView tv;
        private TextView fileSizeTv;
        private ImageView icon;

        ViewHolder(View view) {
            tv = (TextView) view.findViewById(R.id.file_name_tv);
            fileSizeTv = (TextView) view.findViewById(R.id.file_size_tv);
            icon = (ImageView) view.findViewById(R.id.file_icon_iv);
        }
    }
}
