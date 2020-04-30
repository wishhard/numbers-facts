package com.wishhard.nf.numbersfacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skydoves.powermenu.MenuBaseAdapter;
import com.wishhard.nf.numbersfacts.R;
import com.wishhard.nf.numbersfacts.menupojo.PicAndTextPowerMenu;

public class PicAndTextMenuAdapter extends MenuBaseAdapter<PicAndTextPowerMenu> {

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.txt_wid_pic_menu_item_layout, viewGroup, false);
        }

        PicAndTextPowerMenu item = (PicAndTextPowerMenu) getItem(index);
        final ImageView icon = view.findViewById(R.id.icon_img);
        icon.setImageDrawable(item.getIcon());
        final TextView titleTv = view.findViewById(R.id.text_menu_tv);
        titleTv.setText(item.getTitle());

        return super.getView(index, view, viewGroup);
    }
}
