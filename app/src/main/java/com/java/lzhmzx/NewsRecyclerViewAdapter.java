package com.java.lzhmzx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private Context context;
    private List<News> newsList;

    public NewsRecyclerViewAdapter(Context context, List<News> newsList){
        this.context = context;
        this.newsList = newsList;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView newPicture, markedAsRead;
        TextView newsTitle, newsDescription, newsTime;
        Button share, readMore;

        public NewsViewHolder(final View itemView){
            super(itemView);
            markedAsRead = itemView.findViewById(R.id.marked_as_read);
            cardView = itemView.findViewById(R.id.card_view);
            newPicture = itemView.findViewById(R.id.news_picture);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDescription = itemView.findViewById(R.id.news_description);
            share = itemView.findViewById(R.id.btn_share);
            readMore = itemView.findViewById(R.id.btn_more);
            newsTime = itemView.findViewById(R.id.text_view_time);

            newsTitle.setBackgroundColor(Color.argb(40,0,0,0));
        }
    }

    @Override
    public NewsRecyclerViewAdapter.NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, viewGroup, false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(final NewsRecyclerViewAdapter.NewsViewHolder newsViewHolder, final int position){
        if(newsList.get(position).getImageUrl().length()>1) {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                    //通过设置此方法的回调运行在子线程中，可以进行网络请求等一些耗时的操作
                    //比如请求网络拿到数据通过调用emitter.onNext(response);将请求的数据发送到下游
                    Bitmap bitmap = FileUtilities.readPicture(newsList.get(position).getNewsID()+".pic");
                    if (bitmap!=null){
                        System.out.println("From onBindViewHolder Get pic from file.");
                        emitter.onNext(bitmap);
                        emitter.onComplete();
                    }else{
                        bitmap = FileUtilities.getPictureFromURL(newsList.get(position).getImageUrl());
                        System.out.println("From onBindViewHolder Get pic from url.");
                        FileUtilities.savePicture(newsList.get(position).getNewsID()+".pic",bitmap);
                        emitter.onNext(bitmap);
                        emitter.onComplete();
                    }
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
                            newsViewHolder.newPicture.setImageBitmap(b);
                        }
                        @Override
                        public void onError(Throwable e) {}
                        @Override
                        public void onComplete() {}
                    });
        }else { newsViewHolder.newPicture.setImageResource(newsList.get(position).getPictureId()); }

        newsViewHolder.newsTitle.setText(newsList.get(position).getTitle());
        newsViewHolder.newsDescription.setText(newsList.get(position).getDescription());
        newsViewHolder.newsTime.setText(newsList.get(position).getTime());

        if(!newsList.get(position).getIsRead()) {
            newsViewHolder.markedAsRead.setVisibility(View.INVISIBLE);
        }else{
            newsViewHolder.markedAsRead.setVisibility(View.VISIBLE);
        }

        newsViewHolder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DataHelper.addToHistory(newsList.get(position).getNewsID());
                newsViewHolder.markedAsRead.setVisibility(View.VISIBLE);
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("News", newsList.get(position));
                context.startActivity(intent);
            }
        });

        newsViewHolder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataHelper.addToHistory(newsList.get(position).getNewsID());
                newsViewHolder.markedAsRead.setVisibility(View.VISIBLE);
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("News", newsList.get(position));
                context.startActivity(intent);
            }
        });
        newsViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享新闻 \""+newsList.get(position).getTitle()+"\"");
                intent.putExtra(Intent.EXTRA_TEXT, newsList.get(position).getDescription());
                context.startActivity(Intent.createChooser(intent,"新闻分享"));
            }
        });
    }

    @Override
    public int getItemCount(){
        return newsList.size();
    }

    public void addData(int position, News news){
        newsList.add(position, news);
        notifyItemInserted(position);
    }

    public void addData(News news){
        newsList.add(news);
        notifyItemInserted(newsList.size());
    }

    public void removeData(int position){
        newsList.remove(position);
        notifyItemRemoved(position);
    }

    public void swapData(List<News> newsList){
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    public void clearData(){
        this.newsList.clear();
        notifyDataSetChanged();
    }

}
