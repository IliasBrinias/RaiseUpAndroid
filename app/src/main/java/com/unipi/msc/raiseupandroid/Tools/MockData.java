package com.unipi.msc.raiseupandroid.Tools;

import com.unipi.msc.raiseupandroid.Model.Column;
import com.unipi.msc.raiseupandroid.Model.User;
import com.unipi.msc.raiseupandroid.Model.Tag;
import com.unipi.msc.raiseupandroid.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class MockData {
    public static List<User> getTestEmployees(){
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L,"Ilias", "Brinias","sdfasdf", 50));
        userList.add(new User(2L,"Ilias1", "Brinias1","sdfasdf", 20));
        userList.add(new User(3L,"Ilias2", "Brinias2","sdfasdfasd", -20));
        userList.add(new User(4L,"Ilias3", "Brinias3","sdfasdfasd", -40));
        userList.add(new User(5L,"Ilias4", "Brinias4","sdfasdfasd", 70));
        userList.add(new User(6L,"Ilias6", "Brinias6","sdfasdfasd", 20));
        userList.add(new User(7L,"Ilias7", "Brinias7","sdfasdfasd", -10));
        return userList;
    }
    public static List<Tag> getTestTag() {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1L,"SD024","#5e180d"));
        tags.add(new Tag(2L,"FI04424","#ad4838"));
        tags.add(new Tag(3L,"MM124","#38ad3e"));
        tags.add(new Tag(4L,"MM12234","#8fba2c"));
        tags.add(new Tag(5L,"CO24","#ba752c"));
        return tags;
    }
    public static List<Column> getTestColumns() {
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column(0L,"Column0"));
        columnList.add(new Column(1L,"Column1"));
        columnList.add(new Column(2L,"Column2"));
        columnList.add(new Column(3L,"Column3"));
        return columnList;
    }
    public static Column getTestColumn() {
        return new Column(1L,"Column1");
    }
    public static Task getTestTask() {
        return new Task(1L,"Test Task", "Test Description", 0L, MockData.getTestColumn(),MockData.getTestEmployees(),MockData.getTestTag());
    }
    public static List<Task> getTestTasks() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1L,"Test Task", "Test Description", 0L, MockData.getTestColumn(),MockData.getTestEmployees(),MockData.getTestTag()));
        taskList.add(new Task(2L,"Test Tas2", "Test Description2", 0L, MockData.getTestColumn(),MockData.getTestEmployees(),MockData.getTestTag()));
        taskList.add(new Task(3L,"Test Task3", "Test Description3", 0L, MockData.getTestColumn(),MockData.getTestEmployees(),MockData.getTestTag()));
        taskList.add(new Task(4L,"Test Task4", "Test Description4", 0L, MockData.getTestColumn(),MockData.getTestEmployees(),MockData.getTestTag()));
        taskList.add(new Task(5L,"Test Task5", "Test Description5", 0L, MockData.getTestColumn(),MockData.getTestEmployees(),MockData.getTestTag()));
        taskList.add(new Task(6L,"Test Task6", "Test Description6", 0L, MockData.getTestColumn(),MockData.getTestEmployees(),MockData.getTestTag()));
        return taskList;
    }

}
