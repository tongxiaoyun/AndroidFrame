package com.risenb.expandx.document;

import android.content.Context;

import java.io.File;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/7/5
 * 描    述：
 * 修订历史：解析基础类
 * ================================================
 */
public abstract class BaseDocumentHandle {


    /**
     * 解析后文件的存放路径
     */
    public static final String TEMP_FILL_DIRECTORY = "html";

    /**
     * 非文档
     */
    public static final int DOCUMENT_NO_DIRECTORY = 0;


    /**
     * doc 文档
     */
    public static final int DOCUMENT_DOC = 1;

    /**
     * ppt 文档
     */
    public static final int DOCUMENT_PPT = 2;

    /**
     * xls 文档
     */
    public static final int DOCUMENT_XLS = 3;

    /**
     * pdf 文档
     */
    public static final int DOCUMENT_PDF = 4;


    /**
     * 不可被解析的文档
     */
    public static final int DOCUMENT_NO_INCLUDE = 5;

    /**
     * 获取文件的类型
     */
    public int invalid(File f) {
        if (f.isDirectory()) {
            return DOCUMENT_NO_DIRECTORY;
        } else {
            String filename = f.getName().toLowerCase();
            if (filename.endsWith(".doc")) {
                return DOCUMENT_DOC;
            }
            if (filename.endsWith(".ppt")) {
                return DOCUMENT_PPT;
            }
            if (filename.endsWith(".xls")) {
                return DOCUMENT_XLS;
            }
            if (filename.endsWith(".pdf")) {
                return DOCUMENT_PDF;
            }
            return DOCUMENT_NO_INCLUDE;
        }
    }

    /**
     * 解析文件
     */
    public abstract String parserFile(String path, Context context);

    /**
     * 创建文件
     * */
    public abstract void makeFile();


    /**
     * 解析文字大小
     */
    protected int decideSize(int size) {

        if (size >= 1 && size <= 8) {
            return 1;
        }
        if (size >= 9 && size <= 11) {
            return 2;
        }
        if (size >= 12 && size <= 14) {
            return 3;
        }
        if (size >= 15 && size <= 19) {
            return 4;
        }
        if (size >= 20 && size <= 29) {
            return 5;
        }
        if (size >= 30 && size <= 39) {
            return 6;
        }
        if (size >= 40) {
            return 7;
        }
        return 3;
    }


    /**
     * 解析文字颜色
     */
    protected String decideColor(int a) {
        int color = a;
        switch (color) {
            case 1:
                return "#000000";
            case 2:
                return "#0000FF";
            case 3:
            case 4:
                return "#00FF00";
            case 5:
            case 6:
                return "#FF0000";
            case 7:
                return "#FFFF00";
            case 8:
                return "#FFFFFF";
            case 9:
                return "#CCCCCC";
            case 10:
            case 11:
                return "#00FF00";
            case 12:
                return "#080808";
            case 13:
            case 14:
                return "#FFFF00";
            case 15:
                return "#CCCCCC";
            case 16:
                return "#080808";
            default:
                return "#000000";
        }
    }

}
