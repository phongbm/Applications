package com.phongbm.applications;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SystemAppFragment extends Fragment {
    private ApplicationAdapter applicationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationAdapter = new ApplicationAdapter(this.getActivity(), ApplicationManager.getInstance().getSystemApps());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_app, container, false);
        ListView listViewSystemApp = (ListView) view.findViewById(R.id.list_view_system_app);
        listViewSystemApp.setAdapter(applicationAdapter);
        return view;
    }

}