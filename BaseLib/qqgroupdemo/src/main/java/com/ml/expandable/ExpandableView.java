package com.ml.expandable;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ml.expandable.AppExpandableRecyleViewBaseAdapter;
import com.ml.expandable.ExpandableAdapter;


/**
 * Created by admin on 2017-04-21.
 */

public class ExpandableView extends FrameLayout {

    public ExpandableView(Context context) {
        super(context);
        initView();
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ExpandableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    RecyclerView recyclerView;
    View topView;
    int topHeight;
    private int mCurrentPosition = 0;
    ExpandableAdapter adapter;
    boolean isNotDO;
    AppExpandableRecyleViewBaseAdapter.ViewHolder viewHolder;
    private void initView() {
        recyclerView = new RecyclerView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(layoutParams);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                isNotDO= RecyclerView.SCROLL_STATE_SETTLING==newState;
                topHeight = topView.getMeasuredHeight();
                setTOPView(linearLayoutManager);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View nextView=linearLayoutManager.findViewByPosition(mCurrentPosition+1);
                if(nextView==null)return;



                if(nextView.getTop()<=topHeight){
                    topView.setTranslationY(-(topHeight - nextView.getTop()));
                }else {
                    topView.setTranslationY(0);
                }
                if (mCurrentPosition != linearLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    topView.setY(0);


                }
                {
                    AppExpandableRecyleViewBaseAdapter.ViewHolder viewHolder = adapter.getGroupViewHolder(topView);
                    adapter.initGroupWidget(viewHolder, topView);
                   final ViewGroup viewGroup= (ViewGroup) linearLayoutManager.findViewByPosition(mCurrentPosition);
                    boolean isOpen=false;
                    if(viewGroup.getChildCount()<=1){

                    }else {
                        isOpen=viewGroup.getChildAt(1).getVisibility()==VISIBLE?true:false;
                       /* final boolean  isOpenFlag=isOpen;
                        topView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewGroup.getChildAt(1).setVisibility(isOpenFlag?GONE:VISIBLE);
                            }
                        });*/
                    }
                    adapter.bindGroupView(viewHolder, adapter.getGroup(mCurrentPosition),isOpen);
                }

                View firstVisableView=linearLayoutManager.findViewByPosition(mCurrentPosition);
                if(firstVisableView==null){
                    return;
                }
                ViewGroup viewGroup= (ViewGroup) firstVisableView;
                if(viewGroup.getChildCount()<=1)return;
                int  visibility= viewGroup.getChildAt(1).getVisibility();
                if(visibility==VISIBLE)
                topView.setVisibility(visibility);

            }
        });
        addView(recyclerView);
    }

    private void  setTOPView(LinearLayoutManager linearLayoutManager){
        View nextView=linearLayoutManager.findViewByPosition(mCurrentPosition+1);
        if(nextView==null)return;

        if(nextView.getTop()<=topHeight){
            topView.setTranslationY(-(topHeight - nextView.getTop()));
        }else {
            topView.setTranslationY(0);
        }
        View first=linearLayoutManager.findViewByPosition(mCurrentPosition);
        if(first.getY()==0){
            topView.setVisibility(GONE);
        }


    }


    public void setAdapter(ExpandableAdapter adapter) {

        recyclerView.setAdapter(adapter);
        this.adapter = adapter;
        topView = LayoutInflater.from(getContext()).inflate(adapter.getGroupLayout(), null);
        if (adapter.getItemCount() != 0) {
            AppExpandableRecyleViewBaseAdapter.ViewHolder viewHolder = adapter.getGroupViewHolder(topView);
            adapter.initGroupWidget(viewHolder, topView);
            adapter.bindGroupView(viewHolder, adapter.getGroup(0),false);
        }
        //topView.setBackgroundColor(Color.RED);
        topView.setVisibility(GONE);

        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(topView,layoutParams);
    }
}
