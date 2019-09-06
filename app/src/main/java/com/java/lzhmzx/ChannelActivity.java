package com.java.lzhmzx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChannelActivity extends AppCompatActivity {
    ArrayList<String> arrayListAdded;
    ArrayList<String> arrayListNotAdded;

    RecyclerView recyclerViewAdded, recyclerViewNotAdded;
    ChannelRecyclerViewAdapter channelRecyclerViewAdapterAdded, channelRecyclerViewAdapterNotAdded;
    ChannelGridLayoutManager gridLayoutManagerAdded, gridLayoutManagerNotAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        setUpStatusBar();
        setUpButton();
        setUpData();//data设定需先于view设定
        setUpRecyclerView();
    }

    public void setUpData(){
        arrayListAdded = DataHelper.getChannelArrayListAdded();
        arrayListNotAdded = DataHelper.getChannelArrayListNotAdded();
    }

    private void setUpStatusBar(){
        getWindow().setStatusBarColor(getColor(R.color.colorBackground));
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(mode == Configuration.UI_MODE_NIGHT_NO)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void setUpButton(){
        Button button = findViewById(R.id.button_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayListAdded.size() < 1) {
                    Snackbar.make(getWindow().getDecorView().findViewById(R.id.recycler_view_added), "至少选择一个频道", Snackbar.LENGTH_LONG).show();
                    return;
                }
                saveChannel();
            }
        });
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

        recyclerViewAdded.addOnItemTouchListener(new RecyclerViewClickListener(this, recyclerViewAdded,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        deleteChannel(position);
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {}
                }));

        recyclerViewNotAdded.addOnItemTouchListener(new RecyclerViewClickListener(this, recyclerViewNotAdded,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        addChannel(position);
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {}
                }));

        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(channelRecyclerViewAdapterAdded);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewAdded);
    }

    private void addChannel(int position){
        String channel = arrayListNotAdded.get(position);
        channelRecyclerViewAdapterNotAdded.removeData(position);
        channelRecyclerViewAdapterAdded.addData(channel);
    }

    private void deleteChannel(int position){
        String channel = arrayListAdded.get(position);
        channelRecyclerViewAdapterAdded.removeData(position);
        channelRecyclerViewAdapterNotAdded.addData(channel);
    }

    private void saveChannel(){
        DataHelper.setChannelArrayListAdded(arrayListAdded);
        DataHelper.setChannelArrayListNotAdded(arrayListNotAdded);
        Intent intent = new Intent(ChannelActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
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
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChannelRecyclerViewAdapter.ChannelViewHolder channelViewHolder, final int position){
        channelViewHolder.textView.setText(channelList.get(position));
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
        notifyItemRemoved(position);
    }

    public void dragData(int positionFrom, int positionTo){
        String channel = channelList.get(positionFrom);
        channelList.remove(positionFrom);
        channelList.add(positionTo,channel);
        notifyItemMoved(positionFrom, positionTo);
    }
}


class ChannelGridLayoutManager extends GridLayoutManager {
    //为了使GridLayout不滚动
    public ChannelGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }
    @Override
    public boolean canScrollVertically() {
        return false;
    }
}


class RecyclerViewClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    //内部接口，定义点击方法以及长按方法
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public RecyclerViewClickListener(Context context, final RecyclerView recyclerView,OnItemClickListener listener){
        mListener = listener;
        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener(){ //这里选择SimpleOnGestureListener实现类，可以根据需要选择重写的方法
                    //单击事件
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                        if(childView != null && mListener != null){
                            mListener.onItemClick(childView,recyclerView.getChildLayoutPosition(childView));
                            return true;
                        }
                        return false;
                    }
                    //长按事件
                    @Override
                    public void onLongPress(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                        if(childView != null && mListener != null){
                            mListener.onItemLongClick(childView,recyclerView.getChildLayoutPosition(childView));
                        }
                    }
                });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        //把事件交给GestureDetector处理
        if(mGestureDetector.onTouchEvent(e)){
            return true;
        }else
            return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}


class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private ChannelRecyclerViewAdapter channelRecyclerViewAdapter;

    ItemTouchHelperCallback(ChannelRecyclerViewAdapter channelRecyclerViewAdapter) {
        this.channelRecyclerViewAdapter = channelRecyclerViewAdapter;
    }
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        final int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        channelRecyclerViewAdapter.dragData(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}
}