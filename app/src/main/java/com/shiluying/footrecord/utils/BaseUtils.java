package com.shiluying.footrecord.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtils {
    public Bitmap setImgSize(Bitmap bm, int scale){
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float  fix = ((float) scale) / width;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale( fix,  fix);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        return newbm;
    }

    private SpannableString getBitmapMime(Activity activity, String path,String tagPath) {
        SpannableString ss = new SpannableString(tagPath);//这里使用加了<img>标签的图片路径

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        bitmap=setImgSize(bitmap,500);
        ImageSpan imageSpan = new ImageSpan(activity, bitmap);
        ss.setSpan(imageSpan, 0, tagPath.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;


    }

    public void insertImg(Activity activity, EditText content, String path){
        String tagPath = "<img src=\""+path+"\"/>";
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap=setImgSize(bitmap,500);
        if(bitmap != null){
            SpannableString ss = getBitmapMime(activity, path, tagPath);
            insertPhotoToEditText(content,ss);
            content.append("\n");

        }else{
            Toast.makeText(activity,"插入失败，无读写存储权限，请到权限中心开启",Toast.LENGTH_LONG).show();
        }
    }
    //将图片插入到EditText中
    private void insertPhotoToEditText(EditText content, SpannableString ss){
        Editable et = content.getText();
        int start = content.getSelectionStart();
        et.insert(start,ss);
        content.setText(et);
        content.setSelection(start+ss.length());
        content.setFocusableInTouchMode(true);
        content.setFocusable(true);
    }
    public SpannableString initContent(Activity activity, String input){
        Pattern p = Pattern.compile("\\<img src=\".*?\"\\/>");
        Matcher m = p.matcher(input);


        SpannableString spannable = new SpannableString(input);
        while(m.find()){
            String s = m.group();
            int start = m.start();
            int end = m.end();
            String path = s.replaceAll("\\<img src=\"|\"\\/>","").trim();
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                bitmap=setImgSize(bitmap,500);
                ImageSpan imageSpan = new ImageSpan(activity, bitmap);
                spannable.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return spannable;
    }
}
