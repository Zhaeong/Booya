package io.github.zhaeong.booya.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.zhaeong.booya.R;
import io.github.zhaeong.booya.helperObjects.Post;
import io.github.zhaeong.booya.sqlDatabaseHelpers.customDBHelper;

import android.widget.CursorAdapter;

/**
 * Created by Owen on 2017-03-02.
 */

public class PostsAdapter extends CursorAdapter {

    public PostsAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.adapter_item_post, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView itemName = (TextView)view.findViewById(R.id.item_post);

        // Extract properties from cursor
        String taskName = cursor.getString(cursor.getColumnIndexOrThrow(customDBHelper.POSTS_COL_NAME));

        itemName.setText(taskName);
    }

}
