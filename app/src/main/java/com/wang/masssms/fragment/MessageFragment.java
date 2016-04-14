package com.wang.masssms.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.wang.masssms.R;
import com.wang.masssms.activity.MainActivity;

/**
 * Created by 58 on 2016/3/9.
 */
public class MessageFragment extends BaseFragment implements SwipeMenuListView.OnMenuItemClickListener{

    private final int MENU_MARK_POSITION=0;
    private final int MENU_DELETE_POSITION=1;

    //侧滑组件
    private SwipeMenuListView mSwipeMenuListView;
    //swipemenu 创建类
    SwipeMenuCreator mCreater=new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem openItem = new SwipeMenuItem(mContext);
            openItem.setBackground(new ColorDrawable(Color.rgb(0xc9, 0xc9, 0xce)));
            openItem.setWidth(90);
            openItem.setTitle("标记");
            openItem.setTitleSize(18);
            openItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(openItem);
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                   mContext);
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(90);
            // set a icon
            deleteItem.setIcon(R.drawable.icon_message_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_message,null);
        mSwipeMenuListView= (SwipeMenuListView) view.findViewById(R.id.message_swipeMenuListView);
        mSwipeMenuListView.setMenuCreator(mCreater);
        mSwipeMenuListView.setOnMenuItemClickListener(this);
        //向左划
        mSwipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        return view;
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index){
            case MENU_MARK_POSITION:

                Toast.makeText(mContext,"MenuMark click!",Toast.LENGTH_SHORT).show();
                break;
            case MENU_DELETE_POSITION:

                Toast.makeText(mContext,"MenuDelete click!",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
