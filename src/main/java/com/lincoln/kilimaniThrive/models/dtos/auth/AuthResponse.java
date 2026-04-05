package com.lincoln.kilimaniThrive.models.dtos.auth;

import com.lincoln.kilimaniThrive.models.dtos.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private UserDTO user;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
}
