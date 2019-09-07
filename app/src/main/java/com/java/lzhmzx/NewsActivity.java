package com.java.lzhmzx;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsActivity extends AppCompatActivity {

    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        news = getIntent().getParcelableExtra("News");

        setUpStatusBar();
        setUpNewsDetail();
        setUpFloatingActionButton();
        setUpButton();
    }

    private void setUpStatusBar(){
        getWindow().setStatusBarColor(getColor(R.color.colorBackground));
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(mode == Configuration.UI_MODE_NIGHT_NO)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    public void setUpFloatingActionButton(){
        final FloatingActionButton fabFavorite = findViewById(R.id.fab_favorite);

        if(news.getIsFavorite()) fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite));
        else fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_border));

        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(news.getIsFavorite()){
                    Snackbar.make(view, "已取消收藏此新闻！", Snackbar.LENGTH_SHORT).show();
                    operateFavorite();
                    fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_border));
                }else{
                    Snackbar.make(view, "已收藏此新闻！", Snackbar.LENGTH_SHORT).show();
                    operateFavorite();
                    fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite));
                }
            }
        });

    }

    public void setUpNewsDetail() {
        final ImageView newsPicture = findViewById(R.id.news_picture);
        TextView newsTitle = findViewById(R.id.news_title);
        final TextView newsDescription = findViewById(R.id.news_description);
        TextView newsTime = findViewById(R.id.news_time);
        TextView newsOrigin = findViewById(R.id.news_origin);
        VideoView videoView = NewsActivity.this.findViewById(R.id.news_video);

        if (news.getImageUrl().length() > 1) {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                    //通过设置此方法的回调运行在子线程中，可以进行网络请求等一些耗时的操作
                    //比如请求网络拿到数据通过调用emitter.onNext(response);将请求的数据发送到下游
                    Bitmap bitmap = FileUtilities.readPicture(news.getNewsID() + ".pic");
                    if (bitmap != null) {
                        System.out.println("In NewsActivity get pic from file.");
                        emitter.onNext(bitmap);
                        emitter.onComplete();
                    } else {
                        bitmap = FileUtilities.getPictureFromURL(news.getImageUrl());
                        System.out.println("In NewsActivity get pic from internet.");
                        FileUtilities.savePicture(news.getNewsID() + ".pic", bitmap);
                        emitter.onNext(bitmap);
                        emitter.onComplete();
                    }
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Bitmap>() {
                        //通过设置Observer运行在主线程，拿到网络请求的数据进行解析使用
                        @Override
                        public void onSubscribe(Disposable d) { }
                        @Override
                        public void onNext(Bitmap b) {
                            //在此接收上游异步获取的数据，比如网络请求过来的数据进行处理
                            newsPicture.setImageBitmap(b);
                        }
                        @Override
                        public void onError(Throwable e) { }
                        @Override
                        public void onComplete() { }
                    });
        } else { newsPicture.setImageResource(news.getPictureId()); }


        newsTitle.setText(news.getTitle());
        newsDescription.setText(news.getDescription());
        newsTime.setText("于 " + news.getTime());
        newsOrigin.setText("来自 " + news.getOrigin() + " 的报道");

        if (news.getVideoUrl().length()>1){
            System.out.println("Prepare to load video.");
            videoView.setMediaController(new MediaController(this));
            videoView.setVideoURI(Uri.parse(news.getVideoUrl()));
            videoView.start();
            videoView.requestFocus();
        }else{
        videoView.setVisibility(View.GONE);
        }

//        if (true){
//            System.out.println("Prepare to load video.");
//            videoView.setMediaController(new MediaController(this));
//            videoView.setVideoURI(Uri.parse("https://key003.ku6.com/movie/1af61f05352547bc8468a40ba2d29a1d.mp4"));
//            videoView.start();
//            videoView.requestFocus();
//        }

    }

    public void setUpButton(){
        final Button buttonShare = findViewById(R.id.btn_share);
        final Button buttonBlock = findViewById(R.id.btn_block);

        if(news.getIsBlocked()) buttonBlock.setText("已减少类似新闻的出现");
        else buttonBlock.setText("屏蔽类似的新闻");

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享新闻 \""+news.getTitle()+"\"");
                intent.putExtra(Intent.EXTRA_TEXT, news.getDescription());
                startActivity(Intent.createChooser(intent,"新闻分享"));
            }
        });
        buttonShare.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                if(checkPermission(NewsActivity.this)){
                    intent.putExtra(Intent.EXTRA_STREAM, getUri());
                    startActivity(Intent.createChooser(intent,"新闻图片分享"));
                }
                return false;
            }
        });
        buttonBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(news.getIsBlocked()){
                    Snackbar.make(view, "已取消屏蔽类似的新闻", Snackbar.LENGTH_SHORT).show();
                    operateBlock();
                    buttonBlock.setText("屏蔽类似的新闻");
                }else{
                    Snackbar.make(view, "感谢你的反馈 我们会减少类似新闻的出现", Snackbar.LENGTH_SHORT).show();
                    operateBlock();
                    buttonBlock.setText("已减少类似新闻的出现");
                }
            }
        });
    }

    public void operateFavorite(){
        DataHelper.changeFavorite(news.getNewsID());
        news.changeFavorite();
    }

    public void operateBlock(){
        DataHelper.changeBlock(news.getNewsID());
        news.changeBlock();
    }

    private static boolean checkPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] mPermissionList = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(context, mPermissionList, 1);
                return false;
            }
        }
        return true;
    }

    private Uri getUri(){
        Uri uri = null;
        if(!checkPermission(NewsActivity.this))
            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), news.getBitmap(), null, null));
        return uri;
    }
}
