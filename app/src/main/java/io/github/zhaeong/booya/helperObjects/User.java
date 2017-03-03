package io.github.zhaeong.booya.helperObjects;

/**
 * Created by Owen on 2017-02-28.
 */

public class User {
    public String userId;
    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

}
