package common.interaction;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return username + ":"+ password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof User) {
            User userO = (User) o;
            return username.equals(userO.getUsername()) && password.equals(userO.getPassword());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return username.hashCode() + password.hashCode();
    }
}
