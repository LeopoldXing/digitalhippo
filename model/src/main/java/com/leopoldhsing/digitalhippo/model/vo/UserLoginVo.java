package com.leopoldhsing.digitalhippo.model.vo;

import java.util.Objects;

public class UserLoginVo {
    private String email;
    private String password;

    public UserLoginVo() {
    }

    public UserLoginVo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserLoginVo{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLoginVo that = (UserLoginVo) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(password);
        return result;
    }
}
