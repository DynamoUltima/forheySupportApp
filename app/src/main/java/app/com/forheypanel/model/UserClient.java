package app.com.forheypanel.model;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;


public class UserClient extends AppCompatActivity {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
