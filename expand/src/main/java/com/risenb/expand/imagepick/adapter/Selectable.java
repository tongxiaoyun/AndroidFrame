package com.risenb.expand.imagepick.adapter;

import com.risenb.expand.imagepick.bean.Image;

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
public interface Selectable {


    /**
     * Indicates if the item at position position is selected
     *
     * @param image Photo of the item to check
     * @return true if the item is selected, false otherwise
     */
    boolean isSelected(Image image);

    /**
     * Toggle the selection status of the item at a given position
     *
     * @param image Photo of the item to toggle the selection status for
     */
    void toggleSelection(Image image);

    /**
     * Clear the selection status for all items
     */
    void clearSelection();

    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    int getSelectedItemCount();

    /**
     * Indicates the list of selected photos
     *
     * @return List of selected photos
     */
    List<Image> getSelectedPhotos();

}