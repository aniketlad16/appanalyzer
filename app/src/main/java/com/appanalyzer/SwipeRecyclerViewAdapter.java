package com.appanalyzer;

import android.content.Context;
import android.content.Intent;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;


import com.util.intermediate;

import java.util.List;

import de.onyxbits.droidentify.Result;

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {



     Context mContext;
     List<App> apps;

    public SwipeRecyclerViewAdapter(Context context, List<App> apps) {
        this.mContext = context;
        this.apps = apps;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_row, viewGroup, false);

        return new SimpleViewHolder(view);

    }



    private String getHumanReadableSize(long apkSize) {
        String humanReadableSize;
        if (apkSize < 1024) {
            humanReadableSize = String.format(
                    mContext.getString(R.string.app_size_b),
                    (double) apkSize
            );
        } else if (apkSize < Math.pow(1024, 2)) {
            humanReadableSize = String.format(
                    mContext.getString(R.string.app_size_kib),
                    (double) (apkSize / 1024)
            );
        } else if (apkSize < Math.pow(1024, 3)) {
            humanReadableSize = String.format(
                    mContext.getString(R.string.app_size_mib),
                    (double) (apkSize / Math.pow(1024, 2))
            );
        } else {
            humanReadableSize = String.format(
                    mContext.getString(R.string.app_size_gib),
                    (double) (apkSize / Math.pow(1024, 3))
            );
        }
        return humanReadableSize;
    }

    public static boolean  isAppPresent(String packageName,Context context) {


        try{
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 0 );
            return true;

        } catch( PackageManager.NameNotFoundException e ){

            return false;
        }

    }


    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int i) {
        final App item = apps.get(i);

        viewHolder.appName.setText(apps.get(i).getName());
        long apkSize = apps.get(i).getApkSize();
        viewHolder.apkSize.setText(getHumanReadableSize(apkSize));
        viewHolder.appIcon.setImageDrawable(apps.get(i).getIcon());


        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Left
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        // Drag From Right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));


        // Handling different events when swiping
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });




        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareApkIntent = new Intent(mContext,Result.class);
                shareApkIntent.putExtra("path", apps.get(i).getApkPath());

                mContext.startActivity(shareApkIntent);


            }
        });


        viewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Clicked on Map " + viewHolder.appName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Clicked on Share " + viewHolder.appName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.appName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri packageURI = Uri.parse("package:"+ apps.get(i).getPackName());
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                mContext.startActivity(uninstallIntent);


                System.out.println("*********************************");



                if(isAppPresent(apps.get(i).getPackName(),mContext)) {

                    mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                    apps.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, apps.size());
                    mItemManger.closeAllItems();


                    Toast.makeText(view.getContext(), "Deleted " + viewHolder.appName.getText().toString(), Toast.LENGTH_SHORT).show();

                }
                }
        });


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, i);

    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.app_row;
    }


//  ViewHolder Class

public static class SimpleViewHolder extends RecyclerView.ViewHolder {

    SwipeLayout swipeLayout;
    TextView appName;
    TextView apkSize;
    ImageView appIcon;
    TextView tvDelete;
    TextView tvEdit;
    TextView tvShare;
    ImageButton btnLocation;


    SimpleViewHolder(View itemView) {
        super(itemView);
        swipeLayout = (SwipeLayout) itemView.findViewById(R.id.app_row);
        appName = (TextView) itemView.findViewById(R.id.app_name);
        apkSize = (TextView) itemView.findViewById(R.id.apk_size);
        appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
        tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
        tvEdit = (TextView) itemView.findViewById(R.id.tvEdit);
        tvShare = (TextView) itemView.findViewById(R.id.tvShare);
        btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);

        // custom touch or button goes here


    }
}
}


