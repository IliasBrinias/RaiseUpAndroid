package com.unipi.msc.raiseupandroid.Tools;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unipi.msc.raiseupandroid.Model.User;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<User> user = new MutableLiveData<>();
    public void setUser(User user) {
        this.user.setValue(user);
    }
    public LiveData<User> getUser() {
        return user;
    }
}