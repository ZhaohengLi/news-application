package com.java.lzhmzx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChannelActivity extends AppCompatActivity {

    ArrayList<String> arrayListAdded = new ArrayList<>();
    ArrayList<String> arrayListNotAdded = new ArrayList<>();
    RecyclerView recyclerViewAdded, recyclerViewNotAdded;
    ChannelRecyclerViewAdapter channelRecyclerViewAdapterAdded, channelRecyclerViewAdapterNotAdded;
    ChannelGridLayoutManager gridLayoutManagerAdded, gridLayoutManagerNotAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        setUpStatusBar();

        Button button = findViewById(R.id.button_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChannelActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        DataHelper.setSampleChannels(arrayListAdded);
        DataHelper.setSampleChannels(arrayListNotAdded);

        //data设定需先于view设定
        setUpRecyclerView();
    }

    private void setUpStatusBar(){
        getWindow().setStatusBarColor(getColor(R.color.colorBackground));
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(mode == Configuration.UI_MODE_NIGHT_NO)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void setUpRecyclerView() {
        recyclerViewAdded = findViewById(R.id.recycler_view_added);
        recyclerViewNotAdded = findViewById(R.id.recycler_view_not_added);

        gridLayoutManagerAdded = new ChannelGridLayoutManager(ChannelActivity.this, 5);
        gridLayoutManagerNotAdded = new ChannelGridLayoutManager(ChannelActivity.this, 5);

        channelRecyclerViewAdapterAdded = new ChannelRecyclerViewAdapter(ChannelActivity.this, arrayListAdded);
        channelRecyclerViewAdapterNotAdded = new ChannelRecyclerViewAdapter(ChannelActivity.this, arrayListNotAdded);

        recyclerViewAdded.setLayoutManager(gridLayoutManagerAdded);
        recyclerViewNotAdded.setLayoutManager(gridLayoutManagerNotAdded);

        recyclerViewAdded.setAdapter(channelRecyclerViewAdapterAdded);
        recyclerViewNotAdded.setAdapter(channelRecyclerViewAdapterNotAdded);
    }

}


class ChannelRecyclerViewAdapter extends RecyclerView.Adapter<ChannelRecyclerViewAdapter.ChannelViewHolder>{

    private Context context;
    private List<String> channelList;

    public ChannelRecyclerViewAdapter(Context context, List<String> channelList){
        this.context = context;
        this.channelList = channelList;
    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textView;
        public ChannelViewHolder(final View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.text_view_channel);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public ChannelRecyclerViewAdapter.ChannelViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.item_channel, viewGroup, false);
        ChannelViewHolder channelViewHolder = new ChannelViewHolder(view);
        return channelViewHolder;
    }

    @Override
    public void onBindViewHolder(ChannelRecyclerViewAdapter.ChannelViewHolder channelViewHolder, final int position){
        channelViewHolder.textView.setText(channelList.get(position));
        channelViewHolder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                removeData(position);
            }
        });
    }

    @Override
    public int getItemCount(){
        return channelList.size();
    }

    public void addData(int position, String channel){
        channelList.add(position, channel);
        notifyItemInserted(position);
    }

    public void addData(String channel){
        channelList.add(channel);
        notifyItemInserted(channelList.size());
    }

    public void removeData(int position){
        channelList.remove(position);
        notifyDataSetChanged();
    }

    public void swapData(List<String> channelList){
        this.channelList = channelList;
        notifyDataSetChanged();
    }

    public void clearData(){
        this.channelList.clear();
        notifyDataSetChanged();
    }

}


class ChannelGridLayoutManager extends GridLayoutManager {
    public ChannelGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }
    @Override
    public boolean canScrollVertically() {
        return false;
    }
}