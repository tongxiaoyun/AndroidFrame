package com.risenb.expandtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.risenb.expand.swipeback.base.SwipeBackUI;
import com.risenb.expandx.document.DocumentHandleDOC;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/7/5
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class DoctomentUI extends SwipeBackUI {
    private ListView listV = null;
    private List<File> list = null;
    private int a[] = {R.drawable.doc, R.drawable.dir, R.drawable.ppt, R.drawable.xls, R.drawable.pdf};
    private ArrayList<HashMap<String, Object>> recordItem;
    private File presentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctoment);
        listV = (ListView) findViewById(R.id.list);
        list_files();
    }

    private void list_files() {
        File path = android.os.Environment.getExternalStorageDirectory();
        presentFile = path;
        File[] file = path.listFiles();
        fill(file);
    }

    private void fill(File[] file) {
        SimpleAdapter adapter = null;
        recordItem = new ArrayList<HashMap<String, Object>>();
        list = new ArrayList<File>();
        for (File f : file)  {

            if (DocumentHandleDOC.getInstance().invalid(f) == 1) {
                list.add(f);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("picture", a[0]);
                map.put("name", f.getName());
                recordItem.add(map);
            }
            if (DocumentHandleDOC.getInstance().invalid(f) == 0) {
                list.add(f);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("picture", a[1]);
                map.put("name", f.getName());
                recordItem.add(map);
            }
            if (DocumentHandleDOC.getInstance().invalid(f) == 2) {
                list.add(f);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("picture", a[2]);
                map.put("name", f.getName());
                recordItem.add(map);
            }
            if (DocumentHandleDOC.getInstance().invalid(f) == 3) {
                list.add(f);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("picture", a[3]);
                map.put("name", f.getName());
                recordItem.add(map);
            }
            if (DocumentHandleDOC.getInstance().invalid(f) == 4) {
                list.add(f);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("picture", a[4]);
                map.put("name", f.getName());
                recordItem.add(map);
            }

        }


        adapter = new SimpleAdapter(this, recordItem, R.layout.item_doctoment, new String[]{"picture", "name"}, new int[]{R.id.picture, R.id.text});
        listV.setAdapter(adapter);
        //listV.setAdapter(adapter);
        listV.setOnItemClickListener(new Clicker());
    }

    private class Clicker implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            Intent i = new Intent();
            String nameStr = null;
            i.setClass(DoctomentUI.this, ParseUI.class);
            Bundle bundle = new Bundle();
            File file = list.get(arg2);
            presentFile = file;
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                fill(files);

            } else {
                nameStr = file.getAbsolutePath();
                bundle.putString("name", nameStr);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (presentFile.isDirectory()) {
                if (presentFile.equals(android.os.Environment.getExternalStorageDirectory())) {
                    this.finish();
                } else {
                    presentFile = presentFile.getParentFile();
                    File file = presentFile;
                    File[] files = file.listFiles();
                    fill(files);
                }
            }
            if (presentFile.isFile()) {
                if (presentFile.getParentFile().isDirectory()) {
                    presentFile = presentFile.getParentFile();
                    File file = presentFile;
                    File[] files = file.listFiles();
                    fill(files);
                }
            }
        }
        return false;
    }

}
