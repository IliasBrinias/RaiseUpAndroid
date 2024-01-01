package com.unipi.msc.riseupandroid.Retrofit.Request;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditUserRequest {
    private MultipartBody.Part multipartFile;
    private RequestBody password;
    private RequestBody firstName;
    private RequestBody lastName;

    public void setMultipartFile(MultipartBody.Part multipartFile) {
        this.multipartFile = multipartFile;
    }

    public MultipartBody.Part getMultipartFile() {
        return multipartFile;
    }

    public RequestBody getPassword() {
        return password;
    }

    public void setPassword(RequestBody password) {
        this.password = password;
    }

    public RequestBody getFirstName() {
        return firstName;
    }

    public void setFirstName(RequestBody firstName) {
        this.firstName = firstName;
    }

    public RequestBody getLastName() {
        return lastName;
    }

    public void setLastName(RequestBody lastName) {
        this.lastName = lastName;
    }
}
