package com.java.lzhmzx;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
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
                    Snackbar.make(view, "已取消收藏此新闻！", Snackbar.LENGTH_LONG).show();
                    operateFavorite();
                    fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_border));
                }else{
                    Snackbar.make(view, "已收藏此新闻！", Snackbar.LENGTH_LONG).show();
                    operateFavorite();
                    fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_favorite));
                }
            }
        });

    }

    public void setUpNewsDetail(){
        final ImageView newsPicture = findViewById(R.id.news_picture);
        TextView newsTitle = findViewById(R.id.news_title);
        final TextView newsDescription = findViewById(R.id.news_description);
        TextView newsTime = findViewById(R.id.news_time);
        TextView newsOrigin = findViewById(R.id.news_origin);
        VideoView videoView = NewsActivity.this.findViewById(R.id.news_video);

        if(news.getImageUrl().length()>1) {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                    //通过设置此方法的回调运行在子线程中，可以进行网络请求等一些耗时的操作
                    //比如请求网络拿到数据通过调用emitter.onNext(response);将请求的数据发送到下游
                    emitter.onNext(FileUtilities.getPictureFromURL(news.getImageUrl()));
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Bitmap>() {
                        //通过设置Observer运行在主线程，拿到网络请求的数据进行解析使用
                        @Override
                        public void onSubscribe(Disposable d) {}
                        @Override
                        public void onNext(Bitmap b) {
                            //在此接收上游异步获取的数据，比如网络请求过来的数据进行处理
                            newsPicture.setImageBitmap(b);
                        }
                        @Override
                        public void onError(Throwable e) {}
                        @Override
                        public void onComplete() {}
                    });
        }else { newsPicture.setImageResource(news.getPictureId()); }


        newsTitle.setText(news.getTitle());
        newsDescription.setText(news.getDescription());
        newsTime.setText("于 "+news.getTime());
        newsOrigin.setText("来自 "+news.getOrigin()+" 的报道");

        videoView.setMediaController(new MediaController(this));
        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/hehe2.mp4");
        videoView.start();
        videoView.requestFocus();
    }

    public void setUpButton(){
        final Button buttonShare = findViewById(R.id.btn_share);
        final Button buttonBlock = findViewById(R.id.btn_block);

        if(news.getIsBlocked()) buttonBlock.setText("已屏蔽类似的新闻");
        else buttonBlock.setText("屏蔽类似的新闻");

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpg");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享新闻 \""+news.getTitle()+"\"");
                intent.putExtra(Intent.EXTRA_TEXT, news.getDescription());
//                intent.putExtra(Intent.EXTRA_STREAM, )

                startActivity(Intent.createChooser(intent,"新闻分享"));
            }
        });
        buttonBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(news.getIsBlocked()){
                    Snackbar.make(view, "已取消屏蔽类似的新闻！", Snackbar.LENGTH_LONG).show();
                    operateBlock();
                    buttonBlock.setText("屏蔽类似的新闻");
                }else{
                    Snackbar.make(view, "已屏蔽类似的新闻！", Snackbar.LENGTH_LONG).show();
                    operateBlock();
                    buttonBlock.setText("已屏蔽类似的新闻");
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
}
