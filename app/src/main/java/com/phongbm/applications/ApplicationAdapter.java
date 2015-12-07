package com.phongbm.applications;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ApplicationAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private PackageManager packageManager;
    private ArrayList<PackageInfo> packages;

    public ApplicationAdapter(Context context, ArrayList<PackageInfo> packages) {
        inflater = LayoutInflater.from(context);
        this.packageManager = context.getPackageManager();
        this.packages = packages;
    }

    @Override
    public int getCount() {
        return packages.size();
    }

    @Override
    public PackageInfo getItem(int position) {
        return packages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_application, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.img_icon);
            viewHolder.txtAppName = (TextView) convertView.findViewById(R.id.txt_app_name);
            viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PackageInfo packageInfo = this.getItem(position);
        Drawable icon = packageManager.getApplicationIcon(packageInfo.applicationInfo);
        viewHolder.imgIcon.setImageDrawable(icon);
        viewHolder.txtAppName.setText(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
        if (ApplicationManager.getInstance().isSystemPackage(packageInfo)) {
            viewHolder.txtStatus.setText("System application");
        } else {
            viewHolder.txtStatus.setText("Third-party application");
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imgIcon;
        TextView txtAppName;
        TextView txtStatus;
    }

    public void remove(int position) {
        packages.remove(position);
    }

}