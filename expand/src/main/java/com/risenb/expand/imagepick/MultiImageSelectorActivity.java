package com.risenb.expand.imagepick;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.risenb.expand.R;
import com.risenb.expand.imagepick.adapter.FolderAdapter;
import com.risenb.expand.imagepick.bean.Folder;
import com.risenb.expand.imagepick.picker.PickerParams;
import com.risenb.expand.imagepick.picker.SelectMode;
import com.risenb.expand.imagepick.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

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
        implements MultiImageSelectorFragment.Callback {

    // Default image size
    private static final int DEFAULT_IMAGE_SIZE = 9;

    private ListPopupWindow mFolderPopupWindow;

    private FolderAdapter mFolderAdapter;
    private ArrayList<String> resultList = new ArrayList<>();
    private int mDefaultCount = DEFAULT_IMAGE_SIZE;
    private MenuItem menuItemDone;
    private static final int menuItemDoneId = Menu.FIRST + 1;
    private MultiImageSelectorFragment multiImageSelectorFragment;
    private TextView title;
    private RelativeLayout rl_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.photopicker_activity_main);
        RelativeLayout back = (RelativeLayout) findViewById(R.id.back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    finish();
                }
            });
        }
        title = (TextView) findViewById(R.id.title);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);

        if (title != null) {
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mFolderPopupWindow == null) {
                        createPopupFolderList();
                    }

                    if (mFolderPopupWindow.isShowing()) {
                        mFolderPopupWindow.dismiss();
                    } else {
                        mFolderPopupWindow.show();
                        int index = mFolderAdapter.getSelectIndex();
                        index = index == 0 ? index : index - 1;
                        mFolderPopupWindow.getListView().setSelection(index);
                    }
                }
            });
        }
        RelativeLayout v_top = (RelativeLayout) findViewById(R.id.v_top);
        if (v_top != null) {
            ViewGroup.MarginLayoutParams topParams = (ViewGroup.MarginLayoutParams) v_top.getLayoutParams();
            topParams.height = ScreenUtils.getStatusHeight(this);
            v_top.setLayoutParams(topParams);
        }

        TextView v_bottom = (TextView) findViewById(R.id.v_bottom);
        if (v_bottom != null) {
            ViewGroup.MarginLayoutParams bottomParams = (ViewGroup.MarginLayoutParams) v_bottom.getLayoutParams();
            bottomParams.height = ScreenUtils.getBottomStatusHeight(this);
            v_bottom.setLayoutParams(bottomParams);
        }
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        PickerParams pickerParams = (PickerParams) intent.getSerializableExtra(PhotoPicker.PARAMS_PICKER);

        mDefaultCount = pickerParams.maxPickSize;

        if (pickerParams.mode == SelectMode.MULTI) {

            if (pickerParams.selectedPaths != null) {
                resultList = pickerParams.selectedPaths;
            }
        }
        multiImageSelectorFragment = MultiImageSelectorFragment.newInstance(pickerParams);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, multiImageSelectorFragment)
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
                if (resultList != null && resultList.size() > 0) {
                // Notify success
                Intent data = new Intent();
                data.putStringArrayListExtra(PhotoPicker.EXTRA_RESULT, resultList);
                setResult(RESULT_OK, data);
            } else {
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

    @Override
    public void fileloadFinish(List<Folder> folders) {
        mFolderAdapter = new FolderAdapter(this);
        mFolderAdapter.setData(folders);
    }

    /**
     * Update done button by select image data
     *
     * @param resultList selected image data
     */
    public void updateDoneText(ArrayList<String> resultList) {

        if (menuItemDone == null || resultList == null) {
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
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
        updateDoneText(resultList);
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
        }
        updateDoneText(resultList);
    }

    @Override
    public void onCameraShot(String filePath) {
        if (filePath != null) {
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
        updateDoneText(resultList);
        ;
        return true;
    }

    @Override
    protected void onDestroy() {
        if (PhotoPicker.getInstance() != null) {
            PhotoPicker.getInstance().getImageLoader().clearMemoryCache();
        }
        super.onDestroy();
    }


    /**
     * Create popup ListView
     */
    private void createPopupFolderList() {
        Point point = ScreenUtils.getScreenSize(this);
        int width = point.x;

        // default
        int height = ListPopupWindow.WRAP_CONTENT;
        if (mFolderAdapter != null) {
            int folderHeight = mFolderAdapter.getCount() * mFolderAdapter.getItemHeight();
            if (folderHeight >= point.y) {
                height = (int) (point.y * 0.72f);
            }
        }

        mFolderPopupWindow = new ListPopupWindow(this);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height);
        mFolderPopupWindow.setAnchorView(rl_title);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mFolderAdapter.setSelectIndex(i);

                final int index = i;
                final AdapterView v = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        multiImageSelectorFragment.listImage(index, v);
                        mFolderPopupWindow.dismiss();
                        if (index == 0) {
                            title.setText(R.string.folder_all);
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            title.setText(folder.name);
                        }

                    }
                }, 100);

            }
        });
    }
}