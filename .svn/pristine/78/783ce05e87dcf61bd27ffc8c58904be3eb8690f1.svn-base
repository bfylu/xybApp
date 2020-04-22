package com.utils.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.utils.R;

/**
 * Created by BXND on 2017-01-12.
 */

public class EmptyFragment extends Fragment {


    protected View rootView;
    private int rId = 0;
    private boolean status;

    public static EmptyFragment getEmptyFragment(int rId, boolean status) {
        EmptyFragment emptyFragment = new EmptyFragment();
        emptyFragment.status = status;
        emptyFragment.rId = rId;
        return emptyFragment;
    }

    /**
     * 获得布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.view_empty, container, false);
        }
        ViewGroup p = (ViewGroup) rootView.getParent();
        ImageView view = (ImageView) rootView.findViewById(R.id.userimgInfo);
        TextView textView = (TextView) rootView.findViewById(R.id.title_info);
        if (rId != 0) {
            view.setImageResource(rId);
        }
        if (status) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
        if (p != null) {
            p.removeView(rootView);
        }
        return rootView;
    }

}
