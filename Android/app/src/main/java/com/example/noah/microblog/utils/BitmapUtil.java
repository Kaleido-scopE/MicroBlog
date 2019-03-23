package com.example.noah.microblog.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Base64;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.noah.microblog.form.ResponseForm;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class BitmapUtil {
    private static Map<String, Bitmap> avatarMap = new HashMap<>();

    public static Bitmap base64ToBitmap(String base64Str) {
        byte[] bytes = Base64.decode(base64Str, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    //将头像设置到指定ImageView
    public static void setAvatarTo(Activity activity, final String username, final ImageView toView) {
        //有缓存直接设置
        if (avatarMap.containsKey(username))
            toView.setImageBitmap(avatarMap.get(username));
        else
            HttpUtil.getUserAvatar(username, new CallbackAdapter(activity) {
                @Override
                public void onParseResponseForm(ResponseForm response) {
                    String base64Avatar = (String) response.getData();

                    //头像非空才设置并缓存
                    if (base64Avatar.length() > 0) {
                        Bitmap bitmap = base64ToBitmap(base64Avatar);
                        avatarMap.put(username, bitmap);
                        toView.setImageBitmap(bitmap);
                    }
                }
            });
    }

    //为指定的ImageView设置显示自适应
    public static void setAdjustImageView(Activity activity, ImageView imageView, Integer marginHorizontal) {
        //将DP转为PX
        Integer marginHorizontalPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginHorizontal, activity.getResources().getDisplayMetrics());

        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        Integer screenWidth = point.x - marginHorizontalPx;

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        imageView.setMaxWidth(screenWidth);
        imageView.setMaxHeight(screenWidth * 5);
    }
}
