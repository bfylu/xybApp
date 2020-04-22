package com.utils.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utils.R;
import com.utils.view.ProgressWheel;

/**
 * Created by BXND on 2017-01-12.
 */

public class ProgressFragment extends Fragment {
    protected View rootView;
    private ProgressWheel progressWheel;
    int color;

    public static ProgressFragment getProgressFragment(int color) {
        ProgressFragment progressFragment = new ProgressFragment();
        progressFragment.color = color;
        return progressFragment;
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
            rootView = inflater.inflate(R.layout.view_progress, container, false);
        }
        ViewGroup p = (ViewGroup) rootView.getParent();
        if (p != null) {
            p.removeView(rootView);
        }
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
        if (color != 0) {
            progressWheel.setBarColor(color);
        }
        return rootView;
    }
}
