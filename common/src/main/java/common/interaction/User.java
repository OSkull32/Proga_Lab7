package common.interaction;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private final String username;
    private final String password;
    private String token;

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

    public String getToken() {
        return token;
    }

    public User setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                '}';
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
