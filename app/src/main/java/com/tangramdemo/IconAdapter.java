package com.tangramdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * @author liyongjian
 * @date 2019-08-09.
 * Description：
 */
public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {

    private List<String> mIconList;
    private Context mContext;
    private String url;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView iconName;
        ImageView icon;

        public ViewHolder(View view){
            super(view);
            iconName = (TextView) view.findViewById(R.id.icon_name);
            icon = view.findViewById(R.id.icon);
        }
    }

    public IconAdapter(List<String> iconList, Context context,int position){
        mIconList = iconList;
        mContext = context;
        switch (position){
                case 0:
                    url = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2959631642,3930733773&fm=26&gp=0.jpg";
                    break;
                case 1:
                    url = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3515300692,3175765611&fm=26&gp=0.jpg";
                    break;
                case 2:
                    url = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3076032243,4262568784&fm=26&gp=0.jpg";
                    break;
                case 3:
                    url = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2609201346,1547814986&fm=26&gp=0.jpg";
                    break;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_str,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String icon = mIconList.get(position);
        holder.iconName.setText(icon);
        Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.icon.setImageBitmap(resource);

                if (mOnGlideListener != null) {
                    mOnGlideListener.loadSuccess(resource);
                }

//                mBitmap = resource;

//                if (!hasLoadBitmap && noBitmapPosition == position){
//                    TablayoutActivity activity = (TablayoutActivity) getActivity();
//                    activity.setBgColor(false,position,position,0.0f);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIconList.size();
    }

    interface OnGlideListener {
        void loadSuccess(Bitmap bitmap);
    }

    private OnGlideListener mOnGlideListener;

    public void setOnGlideListener(OnGlideListener onGlideListener) {
        mOnGlideListener = onGlideListener;
    }
}
