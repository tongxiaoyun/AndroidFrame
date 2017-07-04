# AndroidFrame
安卓拓展框架
1.banner 包为流行式轮播图 具体用法如下：
        
        <com.risenb.expand.banner.Banner
        android:id="@+id/id_banner"
        android:layout_width="match_parent"
        android:layout_height="180dp"
        app:banner_pageChangeDuration="300"
        app:banner_pointAutoPlayInterval="3"
        app:banner_pointContainerBackground="#33000000"
        app:banner_pointContainerLeftRightPadding="10dp"
        app:banner_pointGravity="right"
        app:banner_pointLeftRightMargin="3dp"
        app:banner_point_color_false="#fff"
        app:banner_point_color_true="#f00"
        app:banner_tipTextColor="#FFFFFF"
        app:banner_tipTextHeight="40dp"
        app:banner_tipTextSize="12sp" />
        
     
        banner_pointContainerBackground       <!-- 指示点容器背景 -->
        banner_point_color_true               <!-- 指示点选中颜色 -->
        banner_point_color_false              <!-- 指示点未选中颜色 -->
        banner_pointContainerLeftRightPadding <!-- 指示点容器左右内间距 -->
        banner_tipTextHeight                  <!-- 指示点上下外间距 -->
        banner_pointLeftRightMargin           <!-- 指示点左右外间距 -->
        banner_pointGravity                   <!-- 指示点的位置 -->
        banner_pointAutoPlayInterval          <!-- 自动轮播的时间间隔 -->
        banner_pageChangeDuration             <!-- 页码切换过程的时间长度 -->
        banner_tipTextColor                   <!-- 提示文案的文字颜色 -->
        banner_tipTextSize                    <!-- 提示文案的文字大小 -->
        
        java 代码如下：
        
         mBanner = (Banner) findViewById(R.id.id_banner);
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
        mBanner.setBannerAdapter(adapter);
        mBanner.notifyDataHasChanged();
        
        
   2.floatwindow 为桌面窗口包 具体用法如下：
   
   java 代码：
   2.1  Application中初始化布局 布局为你要显示的窗口布局
         initView（） 为第一次初始化布局
         reStart（）为再次启动时对布局的初始
         stop（）为暂停时执行的方法
         destory（） 为销毁服务时的方法


         m.getInstance().setFloatView(LayoutInflater.from(this).inflate(R.layout.float_window, null));
              m.getInstance().setCallback(new LayoutInitCallback() {
                  @Override
                  public void initView(View v, String path) {
                      VideoView vv = (VideoView) v.findViewById(R.id.vv);
                      vv.setVideoURI(Uri.parse(path));
                      vv.start();
                  }

                  @Override
                  public void reStart(View v, String path) {
                      VideoView vv = (VideoView) v.findViewById(R.id.vv);
                      vv.setVideoURI(Uri.parse(path));
                      vv.start();
                  }

                  @Override
                  public void stop(View v) {
                      VideoView vv = (VideoView) v.findViewById(R.id.vv);
                      vv.pause();

                  }

                  @Override
                  public void destory(View v) {

                  }
              });
      
 2.2 对activity生命周期的监听： 最好写在baseUI中
        
            //  弹窗生命周期
            @Override
            protected void onResume() {
                super.onResume();
                ActivityBind.getInstance().onResume(MainUI.this);
            }

            //  弹窗生命周期
              @Override
              protected void onStop() {
                  super.onStop();
                  ActivityBind.getInstance().onStop(MainUI.this);
              }

              @Override
              public boolean onKeyDown(int keyCode, KeyEvent event) {
                  if (keyCode == KeyEvent.KEYCODE_MENU) {
                      ActivityBind.getInstance().funPress(MainUI.this);
                      return false;
                  } else {
                      return super.onKeyDown(keyCode, event);
                  }
              }

              @Override
              public void onBackPressed() {
                  super.onBackPressed();
                  ActivityBind.getInstance().dismissFloat(MainUI.this);
              }
              
 2.3 初始化播放路径：
 
        public class FloatWindowUI extends SwipeBackUI {
        public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

        private LinearLayout ll;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.test);
            ActivityBind.getInstance().dismissFloat(FloatWindowUI.this);


            VideoView vv = (VideoView) findViewById(R.id.vv);
            vv.setVideoURI(Uri.parse("http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4"));
            vv.start();

        }

        @Override
        protected void onResume() {
            super.onResume();
            ActivityBind.getInstance().onResume(FloatWindowUI.this);
        }


        @Override
        protected void onStop() {
            super.onStop();
            ActivityBind.getInstance().onStop(FloatWindowUI.this);
        }


        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askForPermission();
                } else {
                    ActivityBind.getInstance().showFloat(FloatWindowUI.this, "http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4");
                    finish();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MENU) {
                ActivityBind.getInstance().funPress(FloatWindowUI.this);
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();

        }

        /**
         * 请求用户给予悬浮窗的权限
         */

        @TargetApi(Build.VERSION_CODES.M)
        public void askForPermission() {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(FloatWindowUI.this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {
                ActivityBind.getInstance().showFloat(FloatWindowUI.this, "http://video.jiecao.fm                                        /8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4");
                finish();

            }
        }

        /**
         * 用户返回
         *
         * @param requestCode
         * @param resultCode
         * @param data
         */
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(FloatWindowUI.this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FloatWindowUI.this, "权限授予成功！", Toast.LENGTH_SHORT).show();
                    //启动FxService
                    ActivityBind.getInstance().showFloat(FloatWindowUI.this, "http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4");

                }
                finish();

            }
        }


    }

2.imagepick 包为图片选择 具体用法如下：

   2.1 图片选择
        
        PhotoPicker.init(new GlideImageLoader(), null);
            Load load = PhotoPicker.load()
                    .showCamera(true)
                    .gridColumns(3);
            courseFile.remove(0);
            load.multi().maxPickSize(20).selectedPaths(courseFile).start(this);
            
   2.2 图片预览
          
          PhotoPicker.init(new GlideImageLoader(), null);
          courseFile.remove(0);
          PhotoPicker.preview()
                    .paths(courseFile)
                    .currentItem(position)
                    .start(getActivity());
            
   2.3 重写onActivityResult()方法
   
            if (requestCode == PhotoPicker.REQUEST_SELECTED) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> mSelectPath = data.getStringArrayListExtra(PhotoPicker.EXTRA_RESULT);
                courseFile.clear();
                courseFile.add("");
                courseFile.addAll(mSelectPath);
                courseFileRecyclerAdapter.setImg(courseFile);
                courseFileRecyclerAdapter.notifyDataSetChanged();

              }
            }
           if (requestCode == PhotoPicker.REQUEST_PREVIEW) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> paths = data.getStringArrayListExtra(PhotoPicker.PATHS);
                courseFile.clear();
                courseFile.add("");
                courseFile.addAll(paths);
                courseFileRecyclerAdapter.setImg(courseFile);
                courseFileRecyclerAdapter.notifyDataSetChanged();
                    }
                }
 
