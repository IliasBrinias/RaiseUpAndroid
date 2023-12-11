package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.unipi.msc.raiseupandroid.Adapter.TagAdapter;
import com.unipi.msc.raiseupandroid.Adapter.TaskAdapter;
import com.unipi.msc.raiseupandroid.Model.Task;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.MockData;
import com.unipi.msc.raiseupandroid.Tools.NameTag;
import com.unipi.msc.raiseupandroid.Tools.RecyclerViewUtils;

public class TaskActivity extends AppCompatActivity {
    LinearLayout linearLayoutColumnChange;
    TextView textViewDaysToExpire, textViewColumnName,
             textViewFirstName, textViewTaskTitle, textViewDescription;
    RecyclerView recyclerViewTags, recyclerViewEmployees,
                 recyclerViewRecommendedEmployees;
    Task task;
    TagAdapter tagAdapter;

    ImageButton imageButtonExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initViews();
        task = getTask(getIntent().getLongExtra(NameTag.TASK_ID,0L));
        if (task!=null) loadData(task);
        initListeners();
    }

    private void loadData(Task task) {
        textViewTaskTitle.setText(task.getName());
        textViewColumnName.setText(task.getColumn().getName());
        textViewDescription.setText(task.getDsc());

        tagAdapter = new TagAdapter(this,task.getTags());
        recyclerViewTags.setLayoutManager(RecyclerViewUtils.getFlexLayout(this));
        recyclerViewTags.setAdapter(tagAdapter);

    }
    private void initListeners() {
        imageButtonExit.setOnClickListener(view->finish());
    }

    private Task getTask(Long taskId) {
        if (taskId == 0L) return null;
        return MockData.getTestTask();
    };

    private void initViews() {
        imageButtonExit = findViewById(R.id.imageButtonExit);
        linearLayoutColumnChange = findViewById(R.id.linearLayoutColumnChange);
        textViewDaysToExpire = findViewById(R.id.textViewDaysToExpire);
        textViewColumnName = findViewById(R.id.textViewColumnName);
        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewTaskTitle = findViewById(R.id.textViewTaskTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        recyclerViewTags = findViewById(R.id.recyclerViewTags);
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        recyclerViewRecommendedEmployees = findViewById(R.id.recyclerViewRecommendedEmployees);
    }
}