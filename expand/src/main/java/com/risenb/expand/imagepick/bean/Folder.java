package com.risenb.expand.imagepick.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/5/27
 * 描    述： 文件夹
 * 修订历史：
 * ================================================
 */
public class Folder {
    public String name;
    public String path;
    public Image cover;
    public List<Image> images;

    @Override
    public boolean equals(Object o) {
        try {
            Folder other = (Folder) o;
            return TextUtils.equals(other.path, path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
