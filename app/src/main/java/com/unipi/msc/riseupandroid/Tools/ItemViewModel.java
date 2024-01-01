package com.unipi.msc.riseupandroid.Tools;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unipi.msc.riseupandroid.Model.User;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<String> keyword = new MutableLiveData<>();
    public void setUser(User user) {
        this.user.setValue(user);
    }
    public LiveData<User> getUser() {
        return user;
    }

    public void setKeyword(String keyword) {
        this.keyword.setValue(keyword);
    }
    public LiveData<String> getKeyword() {
        return keyword;
    }
}
