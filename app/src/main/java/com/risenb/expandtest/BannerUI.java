package com.risenb.expandtest;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.risenb.expand.banner.Banner;
import com.risenb.expand.banner.BannerAdapter;
import com.risenb.expand.floatwindow.ActivityBind;
import com.risenb.expand.swipeback.base.SwipeBackUI;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/3/10
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class BannerUI extends SwipeBackUI {


    private Banner mBanner;

    private List<BannerModel> mDatas = new ArrayList<>();
    private Banner mBanner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner);

        mBanner = (Banner) findViewById(R.id.id_banner);
        mBanner2 = (Banner) findViewById(R.id.id_banner_2);

        BannerAdapter adapter = new BannerAdapter<BannerModel>(mDatas) {
            @Override
            protected void bindTips(TextView tv, BannerModel bannerModel) {
                tv.setText(bannerModel.getTips());
            }

            @Override
            public void bindImage(ImageView imageView, BannerModel bannerModel) {
                Glide.with(BannerUI.this)
                        .load(bannerModel.getImageUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(imageView);
            }

        };

        BannerAdapter adapter2 = new BannerAdapter<BannerModel>(mDatas) {
            @Override
            protected void bindTips(TextView tv, BannerModel bannerModel) {
                tv.setText(bannerModel.getTips());
            }

            @Override
            public void bindImage(ImageView imageView, BannerModel bannerModel) {
                Glide.with(BannerUI.this)
                        .load(bannerModel.getImageUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(imageView);
            }

        };
        mBanner.setBannerAdapter(adapter);
        mBanner2.setBannerAdapter(adapter2);
        getData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityBind.getInstance().onResume(BannerUI.this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ActivityBind.getInstance().onStop(BannerUI.this);
    }



    private void getData() {
        mDatas.clear();
        BannerModel model = null;
        model = new BannerModel();
        model.setImageUrl("https://gma.alicdn.com/simba/img/TB1FS.AJpXXXXc_XpXXSutbFXXX.jpg_q50.jpg");
        model.setTips("这是页面1");
        mDatas.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/i3/TB1J9GqJXXXXXcZaXXXdIns_XXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面2");
        mDatas.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gma.alicdn.com/simba/img/TB1txffHVXXXXayXVXXSutbFXXX.jpg_q50.jpg");
        model.setTips("这是页面3");
        mDatas.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/TB1fW3ZJpXXXXb_XpXXXXXXXXXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面4");
        mDatas.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/i2/TB1ku8oMFXXXXciXpXXdIns_XXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面5");
        mDatas.add(model);
        mBanner.notifyDataHasChanged();
        mBanner2.notifyDataHasChanged();
    }


}
