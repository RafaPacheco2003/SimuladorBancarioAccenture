package com.simulador.financiero.entities;

import java.time.LocalDateTime;

public class UserResponse {


private Long id;
private String fullName;
private String email;
private String status;
private LocalDateTime createdAt;

public UserResponse(
        Long id,
        String fullName,
        String email,
        String status,
        LocalDateTime createdAt) {

    this.id = id;
    this.fullName = fullName;
    this.email = email;
    this.status = status;
    this.createdAt = createdAt;
}

public Long getId() {
    return id;
}

public String getFullName() {
    return fullName;
}

public String getEmail() {
    return email;
}

public String getStatus() {
    return status;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

}
