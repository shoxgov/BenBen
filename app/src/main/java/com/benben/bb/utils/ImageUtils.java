package com.benben.bb.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * @Description Image工具类
 * @Author 一花一世界
 */
public class ImageUtils {
    public static final float DISPLAY_WIDTH = 200;
    public static final float DISPLAY_HEIGHT = 200;

    /**
     * @Description 根据InputStream获取图片实际的宽度和高度
     */
    public static ImageSize getImageSize(InputStream imageStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    public static class ImageSize {
        int width;
        int height;

        public ImageSize() {

        }

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "ImageSize{" +
                    "width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize) {
        // 源图片的宽度
        int width = srcSize.width;
        int height = srcSize.height;
        int inSampleSize = 1;

        int reqWidth = targetSize.width;
        int reqHeight = targetSize.height;

        if (width > reqWidth && height > reqHeight) {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    /**
     * @Description 根据ImageView获适当的压缩的宽和高
     */
    public static ImageSize getImageViewSize(View view) {

        ImageSize imageSize = new ImageSize();
        imageSize.width = getExpectWidth(view);
        imageSize.height = getExpectHeight(view);

        return imageSize;
    }

    /**
     * @Description 根据view获得期望的高度
     */
    private static int getExpectHeight(View view) {

        int height = 0;
        if (view == null) return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = view.getWidth(); // 获得实际的宽度
        }
        if (height <= 0 && params != null) {
            height = params.height; // 获得布局文件中的声明的宽度
        }

        if (height <= 0) {
            height = getImageViewFieldValue(view, "mMaxHeight");// 获得设置的最大的宽度
        }

        //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
        if (height <= 0) {
            DisplayMetrics displayMetrics = view.getContext().getResources()
                    .getDisplayMetrics();
            height = displayMetrics.heightPixels;
        }

        return height;
    }

    /**
     * @Description 根据view获得期望的宽度
     */
    private static int getExpectWidth(View view) {
        int width = 0;
        if (view == null) return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = view.getWidth(); // 获得实际的宽度
        }
        if (width <= 0 && params != null) {
            width = params.width; // 获得布局文件中的声明的宽度
        }

        if (width <= 0) {
            width = getImageViewFieldValue(view, "mMaxWidth");// 获得设置的最大的宽度
        }
        //如果宽度还是没有获取到，使用屏幕的宽度
        if (width <= 0) {
            DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
            width = displayMetrics.widthPixels;
        }

        return width;
    }

    /**
     * @Description 通过反射获取imageview的某个属性值
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 从path中获取图片信息
     *
     * @param path
     * @return
     */
    public static Bitmap decodeBitmap(String path) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        //inJustDecodeBounds
        //If set to true, the decoder will return null (no bitmap), but the out…
        op.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
        //获取比例大小
        int wRatio = (int) Math.ceil(op.outWidth / DISPLAY_WIDTH);
        int hRatio = (int) Math.ceil(op.outHeight / DISPLAY_HEIGHT);
        //如果超出指定大小，则缩小相应的比例
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }
}
