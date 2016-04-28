package com.pili.pldroid.playerdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class VideoFileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mVideoListView;
    private VideoAdapter mVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_file);

        mVideoListView = (ListView) findViewById(R.id.FileListView);
        mVideoAdapter = new VideoAdapter(this);

        mVideoListView.setAdapter(mVideoAdapter);
        mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                Intent intent = new Intent();
                intent.putExtra("videoPath", mVideoAdapter.getVideoPath(position));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MediaStore.Video.Media.getContentUri("external"), null, null, null,
                "UPPER(" + MediaStore.Video.Media.DATA + ")");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mVideoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class VideoAdapter extends SimpleCursorAdapter {

        public VideoAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1, null,
                    new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA},
                    new int[]{android.R.id.text1, android.R.id.text2}, 0);
        }

        @Override
        public long getItemId(int position) {
            final Cursor cursor = getCursor();
            if (cursor == null || cursor.getCount() == 0 || position >= cursor.getCount()) {
                return 0;
            }
            cursor.moveToPosition(position);
            return cursor.getLong(0);
        }

        public String getVideoPath(int position) {
            final Cursor cursor = getCursor();
            if (cursor.getCount() == 0) {
                return "";
            }
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        }
    }

}
