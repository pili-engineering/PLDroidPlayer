package com.qiniu.droid.niuplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.qiniu.droid.niuplayer.MainActivity;
import com.qiniu.droid.niuplayer.R;

public class LiveVideoListFragment extends Fragment implements FragmentLifecycle, View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_live_video_list, container, false);

        RecyclerView videoList = root.findViewById(R.id.video_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        videoList.setLayoutManager(layoutManager);
        videoList.setHasFixedSize(true);
        LiveVideoListAdapter adapter = new LiveVideoListAdapter(getActivity());
        videoList.setAdapter(adapter);

        ImageButton imageButton = root.findViewById(R.id.path_image);
        imageButton.setOnClickListener(this);
        imageButton = root.findViewById(R.id.scan_image);
        imageButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.path_image:
                ((MainActivity) getActivity()).showPathDialog();
                break;
            case R.id.scan_image:
                ((MainActivity) getActivity()).scanQrcode();
                break;
        }
    }

    @Override
    public void onFragmentPause() {
    }

    @Override
    public void onFragmentResume() {
    }

    @Override
    public void onActivityPause() {
    }

    @Override
    public void onActivityResume() {
    }

    @Override
    public void onActivityDestroy() {
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }
}
