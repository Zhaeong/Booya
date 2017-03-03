package io.github.zhaeong.booya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.zhaeong.booya.helperObjects.Post;
import io.github.zhaeong.booya.helperObjects.User;

public class CreatePostActivity extends AppCompatActivity {

    private EditText postName;
    private EditText postDesc;
    private Button createPost;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        postName = (EditText) findViewById(R.id.post_name);
        postDesc = (EditText) findViewById(R.id.post_description);
        createPost = (Button) findViewById(R.id.create_post);

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Creating Post", Toast.LENGTH_LONG).show();
                String sPostName = postName.getText().toString().trim();
                String sPostDesc = postDesc.getText().toString().trim();

                if (sPostName.isEmpty() || sPostDesc.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Put a name and description", Toast.LENGTH_LONG).show();
                }
                else {
                    createPost(sPostName, sPostDesc);
                }
            }
        });


    }

    protected void createPost(String sPostName, String sPostDesc)
    {

        SharedPreferences user_settings = getSharedPreferences(MainActivity.USER_PREFS_NAME, 0);
        String UserId = user_settings.getString("UserID", "No UID");
        String UserName = user_settings.getString("UserName", "No Name");


        String key = mDatabase.child("posts").push().getKey();
        Post newPost = new Post(key, UserName, sPostName, sPostDesc);
        mDatabase.child("posts").child(key).setValue(newPost);

        startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
        finish();

    }
}
