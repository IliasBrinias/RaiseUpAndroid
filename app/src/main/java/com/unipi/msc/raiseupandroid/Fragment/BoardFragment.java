package com.unipi.msc.raiseupandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.unipi.msc.raiseupandroid.Activity.BoardActivity;
import com.unipi.msc.raiseupandroid.Activity.CreateBoardActivity;
import com.unipi.msc.raiseupandroid.Adapter.BoardAdapter;
import com.unipi.msc.raiseupandroid.Model.Board;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.NameTag;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {
    RecyclerView recyclerView;
    ImageButton imageButtonCreateBoard;
    BoardAdapter boardAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        initViews(view);
        initListeners();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        List<Board> boardList = new ArrayList<>();
        boardList.add(new Board(01L,"Vodafone",232436151235L,new ArrayList<>(),5L));
        boardList.add(new Board(02L,"HGI",232436151235L,new ArrayList<>(),20L));
        boardList.add(new Board(03L,"Cosmote",232436151235L,new ArrayList<>(),4L));
        boardList.add(new Board(04L,"SuperDry",232436151235L,new ArrayList<>(),2L));
        boardList.add(new Board(05L,"Lenovo",232436151235L,new ArrayList<>(),22L));
        boardList.add(new Board(06L,"IBM",232436151235L,new ArrayList<>(),15L));
        boardAdapter = new BoardAdapter(requireActivity(), boardList, (v, position) -> {
            Intent intent = new Intent(requireActivity(), BoardActivity.class);
            intent.putExtra(NameTag.BOARD_ID,boardList.get(position).getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(boardAdapter);
        return view;
    }

    private void initListeners() {
        imageButtonCreateBoard.setOnClickListener(view -> startActivity(new Intent(requireActivity(), CreateBoardActivity.class)));
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        imageButtonCreateBoard = view.findViewById(R.id.imageButtonCreateBoard);
    }
}