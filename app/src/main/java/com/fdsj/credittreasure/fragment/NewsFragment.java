package com.fdsj.credittreasure.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.CollectionsInfoActivity;
import com.fdsj.credittreasure.activities.OperationInfoActivity;
import com.fdsj.credittreasure.adapter.TestNomalAdapter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import butterknife.BindView;

/**
 * <p>项目名称：MAJIA
 * <p>描   述： 消息
 * <p>当前版本： V1.0.0
 */

public class NewsFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.mRollViewPager)
    RollPagerView mRollViewPager;

    @BindView(R.id.relative_inform)
    RelativeLayout relativeInform;

    @BindView(R.id.relative_new_notice)
    RelativeLayout relativeNewNotice;

    @BindView(R.id.relative_gathering)
    RelativeLayout relativeGathering;

    @BindView(R.id.relative_operating)
    RelativeLayout relativeOperating;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        relativeInform.setOnClickListener(this);
        relativeNewNotice.setOnClickListener(this);
        relativeGathering.setOnClickListener(this);
        relativeOperating.setOnClickListener(this);
        TestNomalAdapter nomalAdapter = new TestNomalAdapter();
        mRollViewPager.setAdapter(nomalAdapter);
        mRollViewPager.setAnimationDurtion(2000);
        //        mRollViewPager.setHintView(new IconHintView(activity,R.mipmap.scancode,R.mipmap.account));
        mRollViewPager.setHintView(new ColorPointHintView(activity, Color.WHITE, Color.GRAY));
        //        mRollViewPager.setHintView(new TextHintView(activity));
        //        mRollViewPager.setHintView(null);//hide the indicator

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.relative_inform:
                break;
            case R.id.relative_new_notice:
                break;
            case R.id.relative_gathering://跳转收款详情
                intent.setClass(getActivity(), CollectionsInfoActivity.class);//收款界面
                startActivity(intent);
                break;
            case R.id.relative_operating:
                intent.setClass(getActivity(), OperationInfoActivity.class);//操作信息
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.findViewById(R.id.tv_right).setVisibility(View.GONE);
    }
}
