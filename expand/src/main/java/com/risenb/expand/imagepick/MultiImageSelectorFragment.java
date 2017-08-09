package com.risenb.expand.imagepick;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.risenb.expand.R;
import com.risenb.expand.imagepick.adapter.FolderAdapter;
import com.risenb.expand.imagepick.adapter.ImageAdapter;
import com.risenb.expand.imagepick.adapter.PhotoGridAdapter;
import com.risenb.expand.imagepick.bean.Folder;
import com.risenb.expand.imagepick.bean.Image;
import com.risenb.expand.imagepick.event.OnPhotoGridClickListener;
import com.risenb.expand.imagepick.event.PauseOnScrollListener;
import com.risenb.expand.imagepick.picker.PickerParams;
import com.risenb.expand.imagepick.picker.SelectMode;
import com.risenb.expand.imagepick.utils.GridSpacingItemDecoration;
import com.risenb.expand.imagepick.utils.ImageCaptureManager;
import com.risenb.expand.imagepick.utils.PhotoDirectoryLoader;
import com.risenb.expand.imagepick.utils.ScreenUtils;

import java.io.File;
import java.io.IOException;
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
public class MultiImageSelectorFragment extends Fragment implements OnPhotoGridClickListener, ImageAdapter.ClickImageBack {

    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 110;
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;

    // image result data set
    private ArrayList<String> resultList = new ArrayList<>();
    // folder result data set
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    private Callback mCallback;

    private FolderAdapter mFolderAdapter;

    private ListPopupWindow mFolderPopupWindow;

    private View mPopupAnchorView;
    private RecyclerView rv_photos;
    private PhotoGridAdapter photoGridAdapter;

    private boolean hasFolderGened = false;

    private PickerParams pickerParams;
    private ImageCaptureManager captureManager;
    private RecyclerView rvBottom;
    private TextView tv_bottom_review;
    private ImageAdapter<String> reviewAdapter;
    private TextView tv_bottom_ok;

    public static MultiImageSelectorFragment newInstance(PickerParams params) {
        MultiImageSelectorFragment fragment = new MultiImageSelectorFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PhotoPicker.PARAMS_PICKER, params);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == getArguments() || null == getArguments().getSerializable(PhotoPicker.PARAMS_PICKER)) {
            throw new IllegalArgumentException("The Fragment Arguments must contain the PhotoPicker.PARMAS attributes");
        }

        pickerParams = (PickerParams) getArguments().getSerializable(PhotoPicker.PARAMS_PICKER);
        captureManager = new ImageCaptureManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photopicker_fragment_multi_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_bottom_review = (TextView) view.findViewById(R.id.tv_bottom_review);
        mPopupAnchorView = view.findViewById(R.id.footer);
        rvBottom = (RecyclerView) view.findViewById(R.id.rv_bottom);
        tv_bottom_ok = (TextView) view.findViewById(R.id.tv_bottom_ok);

        tv_bottom_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultList != null && resultList.size() > 0) {
                    // Notify success
                    Intent data = new Intent();
                    data.putStringArrayListExtra(PhotoPicker.EXTRA_RESULT, resultList);
                    getActivity().setResult(RESULT_OK, data);
                } else {
                    getActivity().setResult(RESULT_CANCELED);
                }
                getActivity().finish();
            }
        });

        if (pickerParams.mode == SelectMode.MULTI) {
            ArrayList<String> tmp = pickerParams.selectedPaths;
            if (tmp != null && tmp.size() > 0) {
                resultList = tmp;
            }
            refreshPreviewButtonState(resultList);
        }


        mFolderAdapter = new FolderAdapter(getActivity());

        photoGridAdapter = new PhotoGridAdapter(getActivity(), pickerParams.showCamera,
                pickerParams.gridColumns);
        photoGridAdapter.showSelectIndicator(pickerParams.mode == SelectMode.MULTI);
        photoGridAdapter.setOnPhotoGridClickListener(this);

        rv_photos = (RecyclerView) view.findViewById(R.id.rv_photos);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), pickerParams.gridColumns * 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 || position == 1 ? pickerParams.gridColumns : 2;
            }
        });
        rv_photos.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space_size);
        rv_photos.addItemDecoration(new GridSpacingItemDecoration(pickerParams.gridColumns, spacingInPixels, false));
        rv_photos.setAdapter(photoGridAdapter);
        rv_photos.setHasFixedSize(true);
        rv_photos.setItemAnimator(new DefaultItemAnimator());
        if (PhotoPicker.getInstance() != null) {
            rv_photos.addOnScrollListener(new PauseOnScrollListener(PhotoPicker.getInstance().getImageLoader()));
        }
    }

    /**
     * Create popup ListView
     */
    private void createPopupFolderList() {
        Point point = ScreenUtils.getScreenSize(getActivity());
        int width = point.x;

        // default
        int height = ListPopupWindow.WRAP_CONTENT;
        if (mFolderAdapter != null) {
            int folderHeight = mFolderAdapter.getCount() * mFolderAdapter.getItemHeight();
            if (folderHeight >= point.y) {
                height = (int) (point.y * 0.72f);
            }
        }

        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height);
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
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
                        mFolderPopupWindow.dismiss();

                        if (index == 0) {
                            getActivity().getSupportLoaderManager().restartLoader(0, null, mLoaderCallback);
                            photoGridAdapter.setShowCamera(pickerParams.showCamera);
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                photoGridAdapter.setData(folder.images);
                                if (resultList != null && resultList.size() > 0) {
                                    photoGridAdapter.setDefaultSelected(resultList);
                                }
                            }
                            photoGridAdapter.setShowCamera(false);
                        }

                        rv_photos.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
    }

    @Override
    public void onCameraClick() {
        showCameraAction();
    }

    @Override
    public void onPhotoClick(Image image, int position) {
        selectImageFromGrid(image, position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // load image data
        getActivity().getSupportLoaderManager().initLoader(0, null, mLoaderCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    captureManager.galleryAddPic();

                    if (mCallback != null) {
                        mCallback.onCameraShot(captureManager.getCurrentPhotoPath());
                    }
                    break;

                case PhotoPicker.REQUEST_PREVIEW:
                    resultList = data.getStringArrayListExtra(PhotoPicker.PATHS);
                    photoGridAdapter.clearSelection();
                    photoGridAdapter.setDefaultSelected(resultList);
                    photoGridAdapter.notifyDataSetChanged();
                    refreshPreviewButtonState(resultList);
                    if (mCallback != null) {
                        mCallback.onImagePathsChange(resultList);
                    }
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Open camera
     */
    private void showCameraAction() {

        if (pickerParams.mode == SelectMode.MULTI) {
            if (resultList.size() >= pickerParams.maxPickSize) {
                Toast.makeText(getActivity(), R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_rationale_write_storage),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            try {
                Intent intent = captureManager.dispatchTakePictureIntent();
                startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.start_camera_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.permission_dialog_cancel, null)
                    .create().show();
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_WRITE_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCameraAction();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * notify callback
     *
     * @param image image data
     */
    private void selectImageFromGrid(Image image, int position) {
        if (image != null) {
            if (pickerParams.mode == SelectMode.MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                    if (mCallback != null) {
                        mCallback.onImageUnselected(image.path);
                    }
                } else {
                    if (pickerParams.maxPickSize == resultList.size()) {
                        Toast.makeText(getActivity(), R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    resultList.add(image.path);
                    if (mCallback != null) {
                        mCallback.onImageSelected(image.path);
                    }
                }
                photoGridAdapter.select(image, position);
                refreshPreviewButtonState(resultList);
            } else if (pickerParams.mode == SelectMode.SINGLE) {
                if (mCallback != null) {
                    mCallback.onSingleImageSelected(image.path);
                }
            }
        }
    }

    private void refreshPreviewButtonState(ArrayList<String> resultList) {
        if (reviewAdapter == null) {
            reviewAdapter = new ImageAdapter<>();
            reviewAdapter.setActivity(getActivity());
            reviewAdapter.setClickImageBack(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rvBottom.setLayoutManager(layoutManager);
            rvBottom.setAdapter(reviewAdapter);
        }
        reviewAdapter.setList(resultList);
        tv_bottom_review.setText("已添加" + resultList.size() + "张照片");
        if (resultList.size() > 0) {
            mPopupAnchorView.setVisibility(View.VISIBLE);
        } else {
            mPopupAnchorView.setVisibility(View.GONE);
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(getActivity(), pickerParams.filter);
        }

        private boolean fileExist(String path) {
            if (!TextUtils.isEmpty(path)) {
                return new File(path).exists();
            }
            return false;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                if (data.getCount() > 0) {
                    List<Image> images = new ArrayList<>();
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        String name = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED));

                        Image image = null;
                        if (fileExist(path) && !TextUtils.isEmpty(name)) {
                            image = new Image(path, name, dateTime);
                            images.add(image);
                        }
                        if (!hasFolderGened && fileExist(path)) {
                            // get all folder data
                            File folderFile = new File(path).getParentFile();
                            if (folderFile != null && folderFile.exists()) {
                                String fp = folderFile.getAbsolutePath();
                                Folder f = getFolderByPath(fp);
                                if (f == null) {
                                    Folder folder = new Folder();
                                    folder.name = folderFile.getName();
                                    folder.path = fp;
                                    folder.cover = image;
                                    List<Image> imageList = new ArrayList<>();
                                    imageList.add(image);
                                    folder.images = imageList;
                                    mResultFolder.add(folder);
                                } else {
                                    f.images.add(image);
                                }
                            }
                        }

                    } while (data.moveToNext());

                    photoGridAdapter.setData(images);
                    if (resultList != null && resultList.size() > 0) {
                        photoGridAdapter.setDefaultSelected(resultList);
                    }
                    if (!hasFolderGened) {
                        mCallback.fileloadFinish(mResultFolder);
                        hasFolderGened = true;

                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private Folder getFolderByPath(String path) {
        if (mResultFolder != null) {
            for (Folder folder : mResultFolder) {
                if (TextUtils.equals(folder.path, path)) {
                    return folder;
                }
            }
        }
        return null;
    }


    public void listImage(int index, AdapterView<?> v) {
        if (index == 0) {
            getActivity().getSupportLoaderManager().restartLoader(0, null, mLoaderCallback);
            photoGridAdapter.setShowCamera(pickerParams.showCamera);
        } else {
            Folder folder = (Folder) v.getAdapter().getItem(index);
            if (null != folder) {
                photoGridAdapter.setData(folder.images);
                if (resultList != null && resultList.size() > 0) {
                    photoGridAdapter.setDefaultSelected(resultList);
                }
            }
            photoGridAdapter.setShowCamera(pickerParams.showCamera);
        }

        rv_photos.smoothScrollToPosition(0);
    }

    @Override
    public void delete(int position) {
        resultList.remove(position);
        photoGridAdapter.notifyDataSetChanged();
        reviewAdapter.notifyDataSetChanged();
        refreshPreviewButtonState(resultList);
    }

    @Override
    public void review(int position) {
        PhotoPicker.preview()
                .paths(resultList)
                .currentItem(position)
                .start(this);
    }

    /**
     * Callback for host activity
     */
    public interface Callback {
        void onSingleImageSelected(String path);

        void onImageSelected(String path);

        void onImageUnselected(String path);

        void onCameraShot(String filePath);

        void onImagePathsChange(ArrayList<String> paths);

        void fileloadFinish(List<Folder> folders);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

}
