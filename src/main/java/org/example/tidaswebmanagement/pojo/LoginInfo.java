package org.example.tidaswebmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    private Integer id;
    private String username;
    private String name;
    private String token;
    private String role;
    private String refreshToken;

    public LoginInfo(Integer id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }
}