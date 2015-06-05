package com.pili.pldroid.playerdemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.pili.pldroid.playerdemo.R;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView fileListView;

    private VideoAdapter adapter;

    private boolean changed = false;
    private EditText mInputUrlEditText;
    private Button mOKBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileListView = (ListView) findViewById(R.id.fileListView);
        adapter = new VideoAdapter(this);
        fileListView.setAdapter(adapter);
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                intent.putExtra("videoPath", adapter.getVideoPath(position));
                startActivity(intent);
            }
        });

        mInputUrlEditText = (EditText) findViewById(R.id.input_url);

        mOKBtn = (Button) findViewById(R.id.btn_ok);
        mOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mInputUrlEditText.getText().toString().trim();
                Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                intent.putExtra("videoPath", url);
                startActivity(intent);
            }
        });
        refreshUI();
        getSupportLoaderManager().initLoader(1, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void refreshUI() {
        if (changed) {
            fileListView.setVisibility(View.GONE);
            mInputUrlEditText.setVisibility(View.VISIBLE);
            mOKBtn.setVisibility(View.VISIBLE);
        } else {
            fileListView.setVisibility(View.VISIBLE);
            mInputUrlEditText.setVisibility(View.GONE);
            mOKBtn.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_change) {
            changed = !changed;
            refreshUI();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MediaStore.Video.Media.getContentUri("external"), null, null, null,
                "UPPER(" + MediaStore.Video.Media.DATA + ")");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    class VideoAdapter extends SimpleCursorAdapter {
        public VideoAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2, null,
                    new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA},
                    new int[]{android.R.id.text1, android.R.id.text2}, 0);
        }

        @Override
        public long getItemId(int position) {
            final Cursor cursor = getCursor();
            if (cursor.getCount() == 0 || position >= cursor.getCount()) {
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
