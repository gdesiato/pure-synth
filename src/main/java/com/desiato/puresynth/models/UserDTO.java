package com.desiato.puresynth.models;

public class UserDTO {
    private Long id;
    private String email;

    public UserDTO() {
    }

    public UserDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User toUser() {
        User user = new User();
        user.setId(this.id);
        user.setEmail(this.email);
        return user;
    }
}
