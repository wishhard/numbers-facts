package com.wishhard.nf.numbersfacts.tabfrags;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wishhard.nf.numbersfacts.NumberFactActivity;
import com.wishhard.nf.numbersfacts.R;

public class InfoFrag extends Fragment {

    RelativeLayout mLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mLayout = (RelativeLayout) inflater.inflate(R.layout.info_layout,container,false);

        TextView tv1 = mLayout.findViewById(R.id.thk_numb_api);
        tv1.setText(fromHtml(getString(R.string.numb_api_info)));
        tv1.setMovementMethod(LinkMovementMethod.getInstance());

        TextView tv2 = mLayout.findViewById(R.id.thk_pixalbay_api);
        tv2.setText(fromHtml(getString(R.string.pixal_bay_api_info)));
        tv2.setMovementMethod(LinkMovementMethod.getInstance());

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NumberFactActivity)getActivity()).showHideInfo(false);
            }
        });

        return mLayout;
    }

    @SuppressWarnings("deprecation")
    private  Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}
