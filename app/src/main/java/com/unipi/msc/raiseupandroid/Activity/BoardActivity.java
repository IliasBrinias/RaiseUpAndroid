package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unipi.msc.raiseupandroid.Adapter.ColumnAdapter;
import com.unipi.msc.raiseupandroid.Model.Board;
import com.unipi.msc.raiseupandroid.Model.Column;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.NameTag;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {
    RecyclerView recyclerViewColumns;
    TextView textViewBoardTitle;
    ImageButton imageButtonExit;
    Board board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        initViews();
        board = getBoard(getIntent().getLongExtra(NameTag.BOARD_ID,0L));
        initListeners();
    }

    private Board getBoard(long boardId) {
        Board board = new Board(boardId, "Test Board", 0L,new ArrayList<>(),10L);
        textViewBoardTitle.setText(board.getTitle());
        return board;
    }

    private void initListeners() {
        imageButtonExit.setOnClickListener(v->finish());
        recyclerViewColumns.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewColumns.setHasFixedSize(true);
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column(0L,"Column0"));
        columnList.add(new Column(1L,"Column1"));
        columnList.add(new Column(2L,"Column2"));
        columnList.add(new Column(3L,"Column3"));
        ColumnAdapter columnAdapter = new ColumnAdapter(this,columnList);
        recyclerViewColumns.setAdapter(columnAdapter);
    }

    private void initViews() {
        recyclerViewColumns = findViewById(R.id.recyclerViewColumns);
        textViewBoardTitle = findViewById(R.id.textViewBoardTitle);
        imageButtonExit = findViewById(R.id.imageButtonExit);
    }
}