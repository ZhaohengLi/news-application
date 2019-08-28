package com.java.lzhmzx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NewsViewHolder> {

    private Context context;
    private List<News> newsList;

    public RecyclerViewAdapter(Context context, List<News> newsList){
        //TODO 注意参数顺序
        this.context = context;
        this.newsList = newsList;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView newPicture;
        TextView newsTitle, newsDescription;
        Button share, readMore;

        public NewsViewHolder(final View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            newPicture = itemView.findViewById(R.id.news_picture);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDescription = itemView.findViewById(R.id.news_description);
            share = itemView.findViewById(R.id.btn_share);
            readMore = itemView.findViewById(R.id.btn_more);

            newsTitle.setBackgroundColor(Color.argb(30,0,0,0));
        }
    }

    @Override
    public RecyclerViewAdapter.NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, viewGroup, false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.NewsViewHolder newsViewHolder, final int position){
        newsViewHolder.newPicture.setImageResource(newsList.get(position).getPictureId());
        newsViewHolder.newsTitle.setText(newsList.get(position).getTitle());
        newsViewHolder.newsDescription.setText(newsList.get(position).getDescription());

        newsViewHolder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("News", newsList.get(position));
                context.startActivity(intent);
            }
        });

        newsViewHolder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, newsList.get(position).getDescription());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, newsList.get(position).getTitle()));
            }
        });
    }

    @Override
    public int getItemCount(){
        return newsList.size();
    }

}
