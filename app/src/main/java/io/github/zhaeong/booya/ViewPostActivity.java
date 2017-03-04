package io.github.zhaeong.booya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.github.zhaeong.booya.helperObjects.Post;
import io.github.zhaeong.booya.sqlDatabaseHelpers.customDBHelper;

public class ViewPostActivity extends AppCompatActivity {

    public static final String POST_ID = "InternalPostId";

    //Device database variables
    public customDBHelper myDeviceDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        myDeviceDatabase = customDBHelper.getInstance(this);

        Intent intent = getIntent();
        Long postID_internal = intent.getLongExtra(POST_ID, -1);
        Post curPost = myDeviceDatabase.getPost(postID_internal);

        TextView postNameTextView = (TextView) findViewById(R.id.post_name);
        TextView postDescTextView = (TextView) findViewById(R.id.post_description);
        TextView postAuthorTextView = (TextView) findViewById(R.id.post_author);

        postNameTextView.setText(curPost.name);
        postDescTextView.setText(curPost.description);
        postAuthorTextView.setText(curPost.postAuthor);


    }
}
