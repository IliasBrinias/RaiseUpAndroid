package com.unipi.msc.raiseupandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.raiseupandroid.Interface.OnBoardClick;
import com.unipi.msc.raiseupandroid.Model.Board;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {
    Context c;
    List<Board> boardList;
    OnBoardClick onBoardClick;

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM");
    public BoardAdapter(Context c, List<Board> boardList, OnBoardClick onBoardClick) {
        this.c = c;
        this.boardList = boardList;
        this.onBoardClick = onBoardClick;
    }

    @NonNull
    @Override
    public BoardAdapter.BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_layout, parent, false);
        return new BoardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardAdapter.BoardViewHolder holder, int position) {
        holder.setOnBoardClick(onBoardClick);
        holder.bindData(c, boardList.get(position));
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public static class BoardViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView0, imageView1, imageView2;
        private TextView textViewTitle, textViewDueDate, textViewTasks, textViewEmployees;
        private OnBoardClick onBoardClick;
        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            itemView.setOnClickListener(view->onBoardClick.onClick(view,getAdapterPosition()));
        }

        public void setOnBoardClick(OnBoardClick onBoardClick) {
            this.onBoardClick = onBoardClick;
        }

        private void initViews(View view) {
            imageView0 = view.findViewById(R.id.imageViewProfile0);
            imageView1 = view.findViewById(R.id.imageViewProfile1);
            imageView2 = view.findViewById(R.id.imageViewProfile2);
            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewDueDate = view.findViewById(R.id.textViewDueDate);
            textViewTasks = view.findViewById(R.id.textViewTotalTasks);
            textViewEmployees = view.findViewById(R.id.textViewTotalEmployees);

        }

        public void bindData(Context c, Board board){
            textViewDueDate.setText(simpleDateFormat.format(board.getDueDate()));
            textViewTasks.setText(String.valueOf(board.getTasks()));
            textViewEmployees.setText(String.valueOf(board.getEmployees().size()));
            textViewTitle.setText(String.valueOf(board.getTitle()));
            try{
                ImageUtils.loadProfileToImageView(c,board.getEmployees().get(0),imageView0);
                ImageUtils.loadProfileToImageView(c,board.getEmployees().get(1),imageView1);
                ImageUtils.loadProfileToImageView(c,board.getEmployees().get(2),imageView2);
            }catch (Exception ignore){}
        }
    }
}
