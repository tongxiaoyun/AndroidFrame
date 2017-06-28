package com.risenb.expand.imagepick;

import com.risenb.expand.imagepick.picker.Load;
import com.risenb.expand.imagepick.picker.PhotoPreviewBuilder;
import com.risenb.expand.imagepick.picker.PickerTheme;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/5/27
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class PhotoPicker {

    public static final int REQUEST_SELECTED = 201;
    public static final int REQUEST_PREVIEW = 216;
    public static final String PARAMS_PICKER = "photo_picker_params";
    public static final String PATHS = "photo_picker_paths";

    /** Result data set，ArrayList&lt;String&gt;*/
    public static final String EXTRA_RESULT = "select_result";

    private static PhotoPicker mSingleton = null;
    private final PhotoPickerImageLoader pickerImageLoader;
    private final PickerTheme theme;

    public PhotoPicker(PhotoPickerImageLoader pickerImageLoader, PickerTheme theme) {
        this.pickerImageLoader = pickerImageLoader;
        this.theme = theme;
    }

    public static PhotoPicker init(PhotoPickerImageLoader imageLoader, PickerTheme theme) {
        if (mSingleton == null) {
            synchronized (PhotoPicker.class) {
                if (mSingleton == null) {
                    mSingleton = new Contractor(imageLoader, theme).build();
                }
            }
        }
        return mSingleton;
    }

    public PhotoPickerImageLoader getImageLoader() {
        return pickerImageLoader;
    }

    public static PhotoPicker getInstance() {
        return mSingleton;
    }

    private static class Contractor {
        private final PhotoPickerImageLoader imageLoader;
        private final PickerTheme theme;

        public Contractor (PhotoPickerImageLoader imageLoader, PickerTheme theme) {
            if (imageLoader == null) {
                throw  new IllegalArgumentException("PhotoPickerImageLoader must not be null.");
            }
            this.imageLoader = imageLoader;
            this.theme = theme;
        }

        public PhotoPicker build() {
            return new PhotoPicker(imageLoader, theme);
        }
    }

    public static Load load() {
        return new Load();
    }

    public static PhotoPreviewBuilder preview() {
        return new PhotoPreviewBuilder();
    }
}