package io.github.zhaeong.booya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.zhaeong.booya.adapters.PostsAdapter;
import io.github.zhaeong.booya.helperObjects.Post;
import io.github.zhaeong.booya.helperObjects.User;
import io.github.zhaeong.booya.sqlDatabaseHelpers.customDBHelper;


public class MainActivity extends AppCompatActivity {
    public static final String USER_PREFS_NAME = "UserPrefsFile";

    private ListView postListView;
    private ArrayList<Post> postsArrayList = new ArrayList<Post>();
    private PostsAdapter postsAdapter;

    //Network variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    //Device database variables
    public static customDBHelper myDeviceDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postListView = (ListView) findViewById(R.id.posts_list);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        myDeviceDatabase = new customDBHelper(this);

        postsAdapter = new PostsAdapter(this, myDeviceDatabase.getAllItemsInTable(customDBHelper.POSTS_TABLE_NAME));
        postListView.setAdapter(postsAdapter);

        mDatabase.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if(!myDeviceDatabase.doesPostExist(post.postID))
                    {
                        myDeviceDatabase.addPost(post);
                    }

                    postsAdapter.notifyDataSetChanged();
                }

                Log.d("MainActivity", "Value is: ");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("MainActivity", "Failed to read value.", error.toException());
            }
        });


    }


    private void logoutUser() {
        Toast.makeText(getApplicationContext(),"Logging Out", Toast.LENGTH_LONG).show();
        mAuth.signOut();
    }

    private void refreshList()
    {

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile_menuItem:
                return true;
            case R.id.create_new_menuItem:
                startActivity(new Intent(MainActivity.this, CreatePostActivity.class));
                finish();
                return true;
            case R.id.refresh_list:
                refreshList();
                return true;
            case R.id.logout:
                logoutUser();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
