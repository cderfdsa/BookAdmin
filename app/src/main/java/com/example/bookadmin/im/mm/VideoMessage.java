package com.example.bookadmin.im.mm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.R;
import com.example.bookadmin.activity.VideoActivity;
import com.example.bookadmin.im.chat.ChatAdapter;
import com.example.bookadmin.tools.utils.MediaUtil;
import com.example.bookadmin.tools.utils.SDCardUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSnapshot;
import com.tencent.imsdk.TIMVideo;
import com.tencent.imsdk.TIMVideoElem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 小视频消息数据
 */
public class VideoMessage extends Message {

    private static final String TAG = "VideoMessage";


    public VideoMessage(TIMMessage message) {
        this.message = message;
    }

    public VideoMessage(String fileName) {
        message = new TIMMessage();
        TIMVideoElem elem = new TIMVideoElem();
        elem.setVideoPath(SDCardUtils.getCacheFilePath(fileName));
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(SDCardUtils.getCacheFilePath(fileName), MediaStore.Images.Thumbnails.MINI_KIND);
        elem.setSnapshotPath(SDCardUtils.createFile(thumb, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())));
        TIMSnapshot snapshot = new TIMSnapshot();
        snapshot.setType("PNG");
        snapshot.setHeight(thumb.getHeight());
        snapshot.setWidth(thumb.getWidth());
        TIMVideo video = new TIMVideo();
        video.setType("MP4");
        video.setDuaration(MediaUtil.getInstance().getDuration(SDCardUtils.getCacheFilePath(fileName)));
        elem.setSnapshot(snapshot);
        elem.setVideo(video);
        message.addElement(elem);
    }

    public VideoMessage(String filePath, String coverPath, long duration) {
        message = new TIMMessage();
        TIMVideoElem elem = new TIMVideoElem();
        elem.setVideoPath(filePath);
        elem.setSnapshotPath(coverPath);
        TIMSnapshot snapshot = new TIMSnapshot();
        File file = new File(coverPath);
        int height = 0, width = 0;
        if (file.exists()) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(coverPath, options);
            height = options.outHeight;
            width = options.outWidth;
        }
        snapshot.setType("PNG");
        snapshot.setHeight(height);
        snapshot.setWidth(width);
        TIMVideo video = new TIMVideo();
        video.setType("MP4");
        video.setDuaration(duration);
        elem.setSnapshot(snapshot);
        elem.setVideo(video);
        message.addElement(elem);
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(final ChatAdapter.ViewHolder viewHolder, final Context context) {
        clearView(viewHolder);
        final TIMVideoElem e = (TIMVideoElem) message.getElement(0);
        switch (message.status()) {
            case Sending:
                showSnapshot(viewHolder, BitmapFactory.decodeFile(e.getSnapshotPath(), new BitmapFactory.Options()), context);
                break;
            case SendSucc:

                final TIMSnapshot snapshot = e.getSnapshotInfo();
                if (SDCardUtils.isCacheFileExist(snapshot.getUuid())) {
                    showSnapshot(viewHolder, BitmapFactory.decodeFile(SDCardUtils.getCacheFilePath(snapshot.getUuid()), new BitmapFactory.Options()), context);
                } else {
                    snapshot.getImage(SDCardUtils.getCacheFilePath(snapshot.getUuid()), new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Log.e(TAG, "get snapshot failed. code: " + i + " errmsg: " + s);
                        }

                        @Override
                        public void onSuccess() {
                            showSnapshot(viewHolder, BitmapFactory.decodeFile(SDCardUtils.getCacheFilePath(snapshot.getUuid()), new BitmapFactory.Options()), context);
                        }
                    });
                }
                final String fileName = e.getVideoInfo().getUuid();
                if (!SDCardUtils.isCacheFileExist(fileName)) {
                    e.getVideoInfo().getVideo(SDCardUtils.getCacheFilePath(fileName), new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Log.e(TAG, "get video failed. code: " + i + " errmsg: " + s);
                        }

                        @Override
                        public void onSuccess() {
                            setVideoEvent(viewHolder, fileName, context);
                        }
                    });
                } else {
                    setVideoEvent(viewHolder, fileName, context);
                }
                break;
        }
        showStatus(viewHolder);
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        return BookApplication.getInstance().getString(R.string.summary_video);
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }


    /**
     * 显示缩略图
     */
    private void showSnapshot(final ChatAdapter.ViewHolder viewHolder, final Bitmap bitmap, Context context) {
        if (bitmap == null) return;
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmap);
        getBubbleView(viewHolder).addView(imageView);
    }

    private void showVideo(String path, Context context) {
        if (context == null) return;
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }

    private void setVideoEvent(final ChatAdapter.ViewHolder viewHolder, final String fileName, final Context context) {
        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideo(SDCardUtils.getCacheFilePath(fileName), context);
            }
        });
    }
}