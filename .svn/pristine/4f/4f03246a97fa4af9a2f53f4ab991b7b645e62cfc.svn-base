package cn.payadd.majia.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.payadd.majia.util.ImageUtil;

/**
 * Created by zz on 2016/10/9.
 */
public class ZzImageBox extends RecyclerView {

    private int mMaxLine;
    private int mImageSize;
    private int mPadding;
    private int mLeftMargin;
    private int mRightMargin;
    private int mDefaultPicId = -1;
    private int mDeletePicId = -1;
    private int mAddPicId = -1;
    private boolean mDeletable;
    private static final boolean DEFAULT_DELETABLE = true;



    private static final int DEFAULT_MAX_LINE = 1;
    private static final int DEFAULT_IMAGE_SIZE = 4;
    private static final int DEFAULT_IMAGE_PADDING = 5;

    private List<ImageEntity> mDatas;
    private MyAdapter mAdapter;

    private OnImageClickListener mClickListener;

    private Comparator comparator ;

    public ZzImageBox(Context context) {
        super(context);
        init(context, null);
    }

    public ZzImageBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZzImageBox(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZzImageBox);
        mLeftMargin = a.getDimensionPixelSize(R.styleable.ZzImageBox_zib_left_margin, 0);
        mRightMargin = a.getDimensionPixelSize(R.styleable.ZzImageBox_zib_right_margin, 0);
        mMaxLine = a.getInteger(R.styleable.ZzImageBox_zib_max_line, DEFAULT_MAX_LINE);
        mPadding = a.getDimensionPixelSize(R.styleable.ZzImageBox_zib_img_padding, DEFAULT_IMAGE_PADDING);
        mDefaultPicId = a.getResourceId(R.styleable.ZzImageBox_zib_img_default, -1);
        mDeletePicId = a.getResourceId(R.styleable.ZzImageBox_zib_img_delete, -1);
        mAddPicId = a.getResourceId(R.styleable.ZzImageBox_zib_img_add, -1);
        mDeletable = a.getBoolean(R.styleable.ZzImageBox_zib_img_deletable, DEFAULT_DELETABLE);
        a.recycle();

        initData(context);
    }


    private void initData(Context context) {
        mDatas = new ArrayList<>();
        setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(context));
        setPadding(mLeftMargin, 0, mRightMargin, 0);
        mAdapter = new MyAdapter(context, mDatas,mDefaultPicId,mDeletable, mPadding, mLeftMargin, mRightMargin, mMaxLine, mClickListener);
        setAdapter(mAdapter);

        comparator = new Comparator<ImageEntity>() {

            @Override
            public int compare(ImageEntity o1, ImageEntity o2) {
                String tag1 = o1.getTag();
                String tag2 = o2.getTag();
                if(TextUtils.isEmpty(tag1)){
                    return 1;
                }
                if(TextUtils.isEmpty(tag2)){
                    return -1;
                }
                return tag1.compareTo(tag2);
            }
        };
        mAdapter.notifyDataSetChanged();
    }

    public void setOnImageClickListener(OnImageClickListener mClickListener) {
        this.mClickListener = mClickListener;
        mAdapter.listener = mClickListener;
    }


    public void setmDefaultPicId(int mDefaultPicId) {
        this.mDefaultPicId = mDefaultPicId;
        mAdapter.defaultPic = mDefaultPicId;
        mAdapter.notifyDataSetChanged();
    }




    public void setmDeletable(boolean mDeletable) {
        this.mDeletable = mDeletable;
        mAdapter.deletable = mDeletable;
        mAdapter.notifyDataSetChanged();
    }

    public void setmDatas(List<ImageEntity> mDatas) {
        this.mDatas = mDatas;
        mAdapter.setmDatas(mDatas);
        mAdapter.notifyDataSetChanged();
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public void addView() {
        if (mDatas != null) {
            if (mDatas.size() < mMaxLine) {
                mAdapter.lastOne = false;
                ImageEntity entity = new ImageEntity();
                entity.setPicFilePath(null);
                entity.setAdd(false);

                this.mDatas.add(this.mDatas.size(), entity);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void addImage(int position,String imagePath,String tag) {

        boolean flag = true;//false为不需要添加空布局，true为需要添加空布局
        if (mDatas != null) {
            if (position >= 0 && position <= mDatas.size() - 1) {
                //如果下标中有布局，替换
                mAdapter.lastOne = true;
                this.mDatas.get(position).setAdd(true);
                this.mDatas.get(position).setTag(tag);
                this.mDatas.get(position).setPicFilePath(imagePath);
                mAdapter.notifyDataSetChanged();
            }else{
                addImage(imagePath,tag);
            }
        }

    }
    public void addImage(String imagePath,String tag) {
        int index = 0;
        boolean flag = true;//false为不需要添加空布局，true为需要添加空布局
        if (mDatas != null && mDatas.size() < mMaxLine) {
            //如果有剩余的空布局则不添加，并记录下标
            for(int i = 0;i< mDatas.size();i++){
                ImageEntity entity = mDatas.get(i);
                if(TextUtils.isEmpty(entity.getPicFilePath())){
                    index = i;
                    flag =false;
                    break;
                }
            }
            if(flag){
                addView();
            }
        }
            if (mDatas != null && mDatas.size() > 0 && mDatas.size() <= mMaxLine) {

                for (ImageEntity entity : mDatas) {
                    if (TextUtils.isEmpty(entity.getPicFilePath())) {
                        entity.setAdd(true);
                        entity.setTag(tag);
                        entity.setPicFilePath(imagePath);
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

    }

    public void removeImage(int position) {
        if (mDatas != null) {
            mDatas.remove(position);
        }
        mAdapter.notifyDataSetChanged();
    }

    public boolean isDataEmpty() {
        boolean flag = true;//false不为空，true为空
        if (mDatas != null) {
          for (ImageEntity entity :mDatas){
            if(!TextUtils.isEmpty(entity.getPicFilePath())){
                flag = false;
                break;
            }
          }
        }
        return flag;
    }

    private static class MyAdapter extends Adapter<ViewHolder> {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<ImageEntity> mDatas;
        private int defaultPic;
        private boolean deletable;
        private int picWidth;
        private int padding;
        private int maxLine;
        private int imageSize;
        private int leftMargin;
        private int rightMargin;
        private boolean lastOne;
        private OnImageClickListener listener;

        public MyAdapter(Context context, List<ImageEntity> mDatas,int defaultPic, boolean deletable, int padding, int leftMargin, int rightMargin, int maxLine, OnImageClickListener listener) {
            mInflater = LayoutInflater.from(context);
            this.mContext = context;
            this.mDatas = mDatas;
            ImageEntity entity = new ImageEntity();
            entity.setAdd(true);
            this.mDatas.add(entity);
            this.defaultPic = defaultPic;
            this.deletable = deletable;
            this.padding = padding;
            this.maxLine = maxLine;
            this.leftMargin = leftMargin;
            this.rightMargin = rightMargin;
            this.lastOne = false;
            this.listener = listener;
            this.picWidth = getScreenWidth(context) - leftMargin - rightMargin;
        }

        public void setmDatas(List<ImageEntity> mDatas) {
            this.mDatas = mDatas;
            if (mDatas != null && mDatas.size() < maxLine) {
                ImageEntity entity = new ImageEntity();
                entity.setAdd(true);
                this.mDatas.add(entity);
            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.zz_image_box_item, parent, false);
            TextView textView = (TextView) itemView.findViewById(R.id.lab_upload);
            textView.setText("点击上传");
//            itemView.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picWidth));
//          ImageView iv = (ImageView) itemView.findViewById(R.id.iv_voucher);
//           iv.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picWidth));
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ImageEntity entity = mDatas.get(position);
            if(TextUtils.isEmpty(entity.getPicFilePath())){
                holder.uploadPic.setText("点击上传");
            }
            int adapterPos = holder.getAdapterPosition();
            //第一张图片:图片只有第一张时：没有删除按钮,显示添加按钮，否则，显示删除按钮，不显示添加按钮
            //最后一张图片：没有添加按钮
            if (mDatas.get(holder.getAdapterPosition()).getPicFilePath() != null && mDatas.get(holder.getAdapterPosition()).getPicFilePath().length() != 0) {

                int width = 200;
                int height = 150;
                File file = new File(mDatas.get(holder.getAdapterPosition()).getPicFilePath());
                if(file.isFile()&& file.exists()){
                    holder.ivPic.setImageBitmap(ImageUtil.zoom(file,width,height));
                }

            } else {
                holder.ivPic.setImageResource(defaultPic == -1 ? R.mipmap.ticket : defaultPic);
            }
            if(adapterPos == 0 ){
                if(mDatas.size() <= 1){
                    holder.tvDelete.setVisibility(GONE);
                    holder.tvCreate.setVisibility(VISIBLE);
                }else{
                    holder.tvDelete.setVisibility(VISIBLE);
                    holder.tvCreate.setVisibility(GONE);
                }
            }else if(adapterPos == maxLine - 1) {
                holder.tvCreate.setVisibility(GONE);
            }else{
                holder.tvDelete.setVisibility(VISIBLE);
                holder.tvCreate.setVisibility(VISIBLE);
                if(adapterPos < mDatas.size() - 1){
                    holder.tvDelete.setVisibility(VISIBLE);
                    holder.tvCreate.setVisibility(GONE);
                }

            }
            holder.tvCreate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddClick();
                }
            });

            holder.tvDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(position,holder.uploadPic,mDatas.get(position));
                }
            });
            holder.uploadPic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageClick(position,holder.ivPic,holder.uploadPic,mDatas.get(position));
                }
            });
            holder.rootView.setPadding(padding, padding, padding, padding);
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }
    }

    public interface OnImageClickListener {
        void onImageClick(int position,ImageView ivPic,TextView uploadPic,ImageEntity entity);

        void onDeleteClick(int position,TextView uploadInfo,ImageEntity entity);

        void onAddClick();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private View rootView;
        public TextView tvCreate;
        public TextView tvDelete;
        public ImageView ivPic;
        public TextView uploadPic;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivPic = (ImageView) itemView.findViewById(R.id.iv_voucher);
            tvCreate = (TextView) itemView.findViewById(R.id.tvCreate);
            tvDelete = (TextView) itemView.findViewById(R.id.tvDel);
            uploadPic = (TextView) itemView.findViewById(R.id.lab_upload);
        }
    }

    public static class ImageEntity {
        private String picFilePath;
        private boolean isAdd;
        private String tag;

        public boolean isAdd() {
            return isAdd;
        }

        public void setAdd(boolean add) {
            isAdd = add;
        }

        public void setPicFilePath(String picFilePath) {
            this.picFilePath = picFilePath;
        }

        public String getPicFilePath() {
            return picFilePath;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    /**
     * return all paths of images.
     *
     * @return paths of images
     */
    public List<String> getAllImages() {
        List<String> allImages = new ArrayList<>();
        if (mDatas != null) {
            for (ImageEntity mData : mDatas) {
                if (!mData.isAdd) {
                    allImages.add(mData.getPicFilePath());
                }
            }
        }
        return allImages;
    }

    /**
     * remove all images.
     */
    public void removeAllImages() {
        if (mDatas != null) {
            mDatas.clear();
            ImageEntity entity = new ImageEntity();
            entity.setAdd(true);
            mDatas.add(entity);
            mAdapter.lastOne = false;
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * return the image path of position.
     *
     * @param position position.
     * @return image path.
     */
    public String getImagePathAt(int position) {
        if (mDatas != null && mDatas.size() > position) {
            return mDatas.get(position).getPicFilePath();
        }
        return null;
    }
    public List<ImageEntity> getmDatas() {
        return mDatas;
    }

}
