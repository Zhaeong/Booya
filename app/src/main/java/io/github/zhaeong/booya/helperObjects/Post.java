package io.github.zhaeong.booya.helperObjects;

/**
 * Created by Owen on 2017-03-02.
 */

public class Post {
    public String postAuthor;
    public String name;
    public String description;


    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Post(String postAuthor, String name, String description) {
        this.postAuthor = postAuthor;
        this.name = name;
        this.description = description;

    }
}
