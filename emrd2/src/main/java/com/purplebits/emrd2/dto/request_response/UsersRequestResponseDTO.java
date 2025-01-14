package com.purplebits.emrd2.dto.request_response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

public class UsersRequestResponseDTO {

    private Integer userId;
    private String username;
    private String email;
    @NotBlank
    private String fullName;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Status status;
    private String phoneNumber;

    public UsersRequestResponseDTO() {
        super();
    }

    public UsersRequestResponseDTO(UsersDTO usersDTO) {
        super();
        this.userId = usersDTO.getUserId();
        this.username = usersDTO.getUsername();
        this.email = usersDTO.getEmail();
        this.fullName = usersDTO.getFullName();
        this.status = usersDTO.getStatus();
        this.createdAt = usersDTO.getCreatedAt();
        this.updatedAt = usersDTO.getUpdatedAt();
        this.phoneNumber=usersDTO.getPhoneNumber();
    }

    @JsonIgnore
    public UsersDTO getUsersDTO() {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setUserId(userId);
        usersDTO.setUsername(username);
        usersDTO.setEmail(email);
        usersDTO.setFullName(fullName);
        usersDTO.setStatus(status);
        usersDTO.setCreatedAt(createdAt);
        usersDTO.setUpdatedAt(updatedAt);
        usersDTO.setPhoneNumber(phoneNumber);
        return usersDTO;
    }

    public static List<UsersRequestResponseDTO> getUsersRequestResponseDTO(List<UsersDTO> usersDTOs) {
        List<UsersRequestResponseDTO> res = new ArrayList<>();
        for (UsersDTO usersDTO : usersDTOs) {
            res.add(new UsersRequestResponseDTO(usersDTO));
        }
        return res;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
    public String toString() {
        return JsonUtils.createGson().toJson(this);
    }
}
