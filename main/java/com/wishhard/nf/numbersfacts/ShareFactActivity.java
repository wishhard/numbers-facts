package com.wishhard.nf.numbersfacts;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wishhard.nf.numbersfacts.adapters.PicAndTextMenuAdapter;
import com.wishhard.nf.numbersfacts.menupojo.PicAndTextPowerMenu;
import com.wishhard.nf.numbersfacts.pref.FactPref;
import com.wishhard.nf.numbersfacts.settings.SettingsActivity;

import java.io.File;
import java.io.FileOutputStream;


public class ShareFactActivity extends AppCompatActivity {

    private static final String FILE_NAME = "fact.jpg";

    private static final String[] VALUES_KEYS = {"tit","fact","uri"};

    public static final String PIC_URL_KEY = "com.wishhard_nf_pic_url";

    private ConstraintLayout mLayout;
    private FrameLayout top_BtnFrame,factImageFrame,loading;
    private AppCompatImageView settings_btn,share_btn;
    private TextView titleTv,factTv;
    private ImageView fimg;
    private ObjectAnimator anim;

    private String title,fact,url;

    private Context mContext;

    private FactPref pref;

    private Uri uu;

    private CustomPowerMenu customPowerMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = FactPref.getInstance(this);

        if(getIntent().getStringExtra(PIC_URL_KEY) != null) {
            url = getIntent().getStringExtra(PIC_URL_KEY);
            pref.setValue(VALUES_KEYS[2],url);
        } else {
            url = pref.getStringValue(VALUES_KEYS[2],"");
        }

        whichLayout();
        setFactScreenRotation();



        mContext = this;

        top_BtnFrame = findViewById(R.id.top_btns);
        settings_btn = findViewById(R.id.settings_btn);
        share_btn = findViewById(R.id.share_btn);
        titleTv = findViewById(R.id.fact_title);
        factTv =  findViewById(R.id.the_factTv);


       if(getIntent().getStringExtra(NumberFactActivity.FOR_SHARE_TITLE) != null) {
            title = getIntent().getStringExtra(NumberFactActivity.FOR_SHARE_TITLE);
            fact = getIntent().getStringExtra(NumberFactActivity.FOR_SHARE_FACT);

            pref.setValue(VALUES_KEYS[0],title);
            pref.setValue(VALUES_KEYS[1],fact);
        } else {
            title = pref.getStringValue(VALUES_KEYS[0],"");
            fact = pref.getStringValue(VALUES_KEYS[1],"");
        }


        titleTv.setText(title);
        factTv.setText(fact);


        anim = ObjectAnimator.ofFloat(top_BtnFrame, "alpha", 0.5f).setDuration(3000);



        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim.cancel();
                anim.end();
                anim.removeAllListeners();

                Intent i = new Intent(ShareFactActivity.this,SettingsActivity.class);
                startActivity(i);
            }
        });

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim.cancel();
                anim.end();
                anim.removeAllListeners();
                saveInInernalStorage();
                shareFactImage();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10 && data != null) {
            url = data.getData().toString();

                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    setMainLayoutClickable(false);
                    setFactImageFrameClickable(false);
                    showHideLoading(true);
                    Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.fact_img_placeholder)
                            .into(fimg, new Callback() {
                                @Override
                                public void onSuccess() {
                                    showHideLoading(false);
                                    setFactImageFrameClickable(true);
                                    setMainLayoutClickable(true);
                                    pref.setValue(VALUES_KEYS[2],url);
                                }

                                @Override
                                public void onError(Exception e) {
                                    showHideLoading(false);
                                    setFactImageFrameClickable(true);
                                    setMainLayoutClickable(true);
                                }
                            });
                } else {
                    Dexter.withActivity(this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                setMainLayoutClickable(false);
                                setFactImageFrameClickable(false);
                                showHideLoading(true);
                                Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.fact_img_placeholder)
                                        .into(fimg, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                showHideLoading(false);
                                                setFactImageFrameClickable(true);
                                                setMainLayoutClickable(true);
                                                pref.setValue(VALUES_KEYS[2],url);
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                showHideLoading(false);
                                                setFactImageFrameClickable(true);
                                                setMainLayoutClickable(true);
                                            }
                                        });
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                openPremissionDialog();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                }
        }
    }

    private void setFactScreenRotation() {
        if(!prefValue(SettingsActivity.FACT_SCREEN_ORIENTATION_KEY)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void whichLayout() {
        if(!prefValue(SettingsActivity.SHOW_FACT_WITH_IMG_KEY)) {
            setContentView(R.layout.fact_wid_out_picture);
            mLayout = findViewById(R.id.fact_without_pic);
            factImageFrame = null;
            loading = null;
            fimg = null;
            url = "";

            setMainLayoutClickable(true);

        } else {
            setContentView(R.layout.fact_with_picture);
            mLayout = findViewById(R.id.fact_with_pic);
            factImageFrame = findViewById(R.id.fact_cont);
            fimg = findViewById(R.id.fact_image);
            loading = findViewById(R.id.loading_frame);

            setMainLayoutClickable(true);

            setFactImageFrameClickable(true);

            if(url.isEmpty()) {
                Picasso.get().load(R.drawable.fact_img_placeholder).placeholder(R.drawable.fact_img_placeholder)
                        .into(fimg);
            } else {
                setMainLayoutClickable(false);
                setFactImageFrameClickable(false);
                showHideLoading(true);
                Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.fact_img_placeholder)
                        .into(fimg, new Callback() {
                            @Override
                            public void onSuccess() {
                                showHideLoading(false);
                                setFactImageFrameClickable(true);
                                setMainLayoutClickable(true);
                            }

                            @Override
                            public void onError(Exception e) {
                                showHideLoading(false);
                                setFactImageFrameClickable(true);
                                setMainLayoutClickable(true);
                            }
                        });
            }

        }
    }

    private void setFactImageFrameClickable(boolean c) {
        if(c) {
            factImageFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPowerMenu();
                    customPowerMenu.showAsAnchorCenter(v);
                }
            });
        } else {
            factImageFrame.setOnClickListener(null);
        }
    }

    private void setMainLayoutClickable(boolean c) {

        if(c) {
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!anim.isStarted() && !anim.isRunning()) {
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                top_BtnFrame.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationStart(Animator animation) {
                                top_BtnFrame.setVisibility(View.VISIBLE);
                            }
                        });
                        anim.start();
                    }
                }
            });

        } else {
            mLayout.setOnClickListener(null);
        }
    }

    private void setPowerMenu() {
        customPowerMenu = new CustomPowerMenu.Builder<>(ShareFactActivity.this,new PicAndTextMenuAdapter())
                .addItem(new PicAndTextPowerMenu(AppCompatResources.getDrawable(ShareFactActivity.this,R.drawable.pixbay)
                        ,"Search On Pixalbay"))
                .setMenuRadius(0)
                .setMenuShadow(0)
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PicAndTextPowerMenu>() {
                    @Override
                    public void onItemClick(int position, PicAndTextPowerMenu item) {
                        customPowerMenu.dismiss();
                        if(position == 0) {
                            Intent i = new Intent(ShareFactActivity.this,PixelBayActivity.class);
                            startActivity(i);
                        } else if(position ==  1) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), 10);
                        }
                    }
                })
                .addItem(new PicAndTextPowerMenu(AppCompatResources.getDrawable(ShareFactActivity.this,R.drawable.storage),
                        "Device Storage"))
                .setMenuRadius(0)
                .setMenuShadow(0)
                .build();
    }

    private void showHideLoading(boolean show) {
        if(show) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    private boolean prefValue(String prepKey) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getBoolean(prepKey,false);
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private void shareFactImage() {
        File f = new File(this.getCacheDir().getAbsolutePath()+"/",FILE_NAME);
        final Uri path = FileProvider.getUriForFile(this,getString(R.string.file_provider_auth),f);

        try {

            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, path);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setType("image/jpg");
            startActivity(Intent.createChooser(shareIntent, "Share fact"));
        } catch (Exception e) {

        }
    }


    private void openPremissionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ShareFactActivity.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app requires permission to access device storage to load the image that you have chosen. Grant this in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 420);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void saveInInernalStorage() {
        Bitmap b = getBitmapFromView(mLayout);
        File dir = new File(this.getCacheDir().getAbsolutePath()+"/"+FILE_NAME);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(String.valueOf(dir));
            b.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception e) {

        }
    }
}
