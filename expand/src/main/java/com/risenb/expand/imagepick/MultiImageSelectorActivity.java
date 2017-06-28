package com.risenb.expand.imagepick;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.risenb.expand.R;
import com.risenb.expand.imagepick.picker.PickerParams;
import com.risenb.expand.imagepick.picker.SelectMode;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/5/27
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MultiImageSelectorActivity extends AppCompatActivity
        implements MultiImageSelectorFragment.Callback{

    // Default image size
    private static final int DEFAULT_IMAGE_SIZE = 9;

    private ArrayList<String> resultList = new ArrayList<>();
    private int mDefaultCount = DEFAULT_IMAGE_SIZE;
    private MenuItem menuItemDone;
    private static final int menuItemDoneId = Menu.FIRST + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photopicker_activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#21282C"));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        PickerParams pickerParams = (PickerParams) intent.getSerializableExtra(PhotoPicker.PARAMS_PICKER);

        mDefaultCount = pickerParams.maxPickSize;

        if(pickerParams.mode == SelectMode.MULTI) {

            if(pickerParams.selectedPaths != null){
                resultList = pickerParams.selectedPaths;
            }
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, MultiImageSelectorFragment.newInstance(pickerParams))
                .commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;

            case menuItemDoneId:
                if(resultList != null && resultList.size() > 0) {
                    // Notify success
                    Intent data = new Intent();
                    data.putStringArrayListExtra(PhotoPicker.EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                }else{
                    setResult(RESULT_CANCELED);
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onImagePathsChange(ArrayList<String> paths) {
        resultList = paths;
        updateDoneText(resultList);
    }

    /**
     * Update done button by select image data
     * @param resultList selected image data
     */
    public void updateDoneText(ArrayList<String> resultList){

        if(menuItemDone == null || resultList == null){
            return;
        }

        menuItemDone.setVisible(resultList.size() > 0);
        menuItemDone.setTitle(getString(R.string.action_button_string,
                getString(R.string.action_done), resultList.size(), mDefaultCount));
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(PhotoPicker.EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if(!resultList.contains(path)) {
            resultList.add(path);
        }
        updateDoneText(resultList);
    }

    @Override
    public void onImageUnselected(String path) {
        if(resultList.contains(path)){
            resultList.remove(path);
        }
        updateDoneText(resultList);
    }

    @Override
    public void onCameraShot(String filePath) {
        if(filePath != null) {
            Intent data = new Intent();
            resultList.add(filePath);
            data.putStringArrayListExtra(PhotoPicker.EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuItemDone = menu.add(Menu.NONE, menuItemDoneId, 0, "Finish");
        menuItemDone.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menuItemDone.setVisible(false);
        updateDoneText(resultList);;
        return true;
    }

    @Override
    protected void onDestroy() {
        if (PhotoPicker.getInstance() != null) {
            PhotoPicker.getInstance().getImageLoader().clearMemoryCache();
        }
        super.onDestroy();
    }
}