package com.unipi.msc.riseupandroid.Retrofit.Request;

import java.io.File;

import okhttp3.MediaType;
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
    public void setMultipartFile(File file) {
        this.multipartFile = MultipartBody.Part.createFormData(
                "multipartFile",
                file.getName(),
                RequestBody.create(MediaType.parse("image/*"), file));
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
