package com.risenb.expandtest;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;


import com.risenb.expand.swipeback.base.SwipeBackUI;
import com.risenb.expandx.document.BaseDocumentHandle;
import com.risenb.expandx.document.DocumentHandleDOC;
import com.risenb.expandx.document.DocumentHandleXLS;

import java.io.File;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/7/5
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ParseUI extends SwipeBackUI {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parse);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String nameStr = bundle.getString("name");
        WebView view = (WebView) findViewById(R.id.show);

        view.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        view.getSettings().setSupportZoom(true);//支持放大网页功能
        view.getSettings().setBuiltInZoomControls(true);//支持缩小网页功能
        view.getSettings().setJavaScriptEnabled(true);//支持JAVA
        String htmlPath = null;
        switch (DocumentHandleDOC.getInstance().invalid(new File(nameStr))) {
            case BaseDocumentHandle.DOCUMENT_DOC:
                htmlPath = DocumentHandleDOC.getInstance().parserFile(nameStr, ParseUI.this);
                view.loadUrl("file://" + htmlPath);
                break;
            case BaseDocumentHandle.DOCUMENT_XLS:
                htmlPath = DocumentHandleXLS.getInstance().parserFile(nameStr, ParseUI.this);
                view.loadUrl("file://" + htmlPath);
                break;

        }


        System.out.println("htmlPath" + htmlPath);
    }
}
