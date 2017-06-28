package com.risenb.expandtest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.risenb.expand.banner.Banner;
import com.risenb.expand.banner.BannerAdapter;
import com.risenb.expand.floatwindow.ActivityBind;
import com.risenb.expand.swipeback.base.SwipeBackUI;
import com.risenb.expand.xrecyclerview.XRecyclerView;

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
public class TestUI extends SwipeBackUI {


    private Banner mBanner;

    private List<BannerModel> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner);

        mBanner = (Banner) findViewById(R.id.id_banner);

        BannerAdapter adapter = new BannerAdapter<BannerModel>(mDatas) {
            @Override
            protected void bindTips(TextView tv, BannerModel bannerModel) {
                tv.setText(bannerModel.getTips());
            }

            @Override
            public void bindImage(ImageView imageView, BannerModel bannerModel) {
                Glide.with(TestUI.this)
                        .load(bannerModel.getImageUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(imageView);
            }

        };
        mBanner.setBannerAdapter(adapter);
        getData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityBind.getInstance().onResume(TestUI.this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ActivityBind.getInstance().onStop(TestUI.this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            ActivityBind.getInstance().funPress(TestUI.this);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityBind.getInstance().dismissFloat(TestUI.this);
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
    }



}
