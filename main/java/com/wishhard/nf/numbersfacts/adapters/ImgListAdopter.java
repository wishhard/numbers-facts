package com.wishhard.nf.numbersfacts.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wishhard.nf.numbersfacts.R;
import com.wishhard.nf.numbersfacts.ShareFactActivity;
import com.wishhard.nf.numbersfacts.pojos.ImgLinksPojo;

import java.util.List;

public class ImgListAdopter extends RecyclerView.Adapter<ImgListAdopter.ViewHolder> {


    private List<ImgLinksPojo> items;
    private Context mContext;
    private ViewHolder viewHolder;

    private String url;

    public ImgListAdopter(Context mContext, List<ImgLinksPojo> items) {
        this.items = items;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_layout,parent,false);
        viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
          final ImgLinksPojo item = items.get(position);

          Picasso.get().load(item.getThumbnailImgUrl()).placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder).into(holder.imageView);




            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!checkImageResource(mContext, (ImageView) v,R.drawable.img_placeholder)) {
                        url = item.getLargeImgUrl();

                        Intent i = new Intent(mContext,ShareFactActivity.class);
                        i.putExtra(ShareFactActivity.PIC_URL_KEY,url);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(i);
                    }
                }
            });
    }



    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static boolean checkImageResource(Context ctx, ImageView imageView,
                                             int imageResource) {
        boolean result = false;

        if (ctx != null && imageView != null && imageView.getDrawable() != null) {
            Drawable.ConstantState constantState;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                constantState = ctx.getResources()
                        .getDrawable(imageResource, ctx.getTheme())
                        .getConstantState();
            } else {
                constantState = ctx.getResources().getDrawable(imageResource)
                        .getConstantState();
            }

            if (imageView.getDrawable().getConstantState() == constantState) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_pic);

        }
    }
}
