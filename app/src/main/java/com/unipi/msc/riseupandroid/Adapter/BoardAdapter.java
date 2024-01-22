package com.unipi.msc.riseupandroid.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.riseupandroid.Interface.OnBoardClick;
import com.unipi.msc.riseupandroid.Model.Board;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Tools.ImageUtils;
import com.unipi.msc.riseupandroid.Tools.UserUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {
    private final Activity a;
    private final List<Board> boardList;
    private final OnBoardClick onBoardClick;
    private final Long userId;
    public BoardAdapter(Activity a, List<Board> boardList, OnBoardClick onBoardClick) {
        this.a = a;
        this.boardList = boardList;
        this.onBoardClick = onBoardClick;
        this.userId = UserUtils.loadUser(a).getId();
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
        holder.bindData(a, Objects.equals(userId, boardList.get(position).getOwner().getId()), boardList.get(position));
    }
    @Override
    public int getItemCount() {
        return boardList.size();
    }
    public void addItem(Board board) {
        boardList.add(board);
        notifyItemInserted(boardList.size()-1);
    }
    public void clearData() {
        int size = boardList.size();
        if (size!=0){
            boardList.clear();
            notifyDataSetChanged();
        }
    }
    public void updateItem(int position, Board board) {
        boardList.set(position, board);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        boardList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class BoardViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewAddEmployees, imageView0, imageView1, imageView2, imageViewProfileAdmin;
        private TextView textViewTitle, textViewTasks, textViewEmployees;
        private OnBoardClick onBoardClick;
        private final View itemView;
        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            initViews();
            initListener();
        }
        private void initViews() {
            imageViewAddEmployees = itemView.findViewById(R.id.imageViewAddEmployees);
            imageView0 = itemView.findViewById(R.id.imageViewProfile0);
            imageView1 = itemView.findViewById(R.id.imageViewProfile1);
            imageView2 = itemView.findViewById(R.id.imageViewProfile2);
            imageViewProfileAdmin = itemView.findViewById(R.id.imageViewProfileAdmin);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewTasks = itemView.findViewById(R.id.textViewTotalTasks);
            textViewEmployees = itemView.findViewById(R.id.textViewTotalEmployees);

        }
        private void initListener() {
            itemView.setOnClickListener(view->onBoardClick.onClick(view,getAdapterPosition()));
            itemView.setOnLongClickListener(view->onBoardClick.onLongClick(view,getAdapterPosition()));
            imageViewAddEmployees.setOnClickListener(view-> onBoardClick.addEmployees(view, getAdapterPosition()));
        }
        public void setOnBoardClick(OnBoardClick onBoardClick) {
            this.onBoardClick = onBoardClick;
        }
        public void bindData(Activity a, boolean isOwner, Board board){
            imageViewAddEmployees.setVisibility(isOwner?View.VISIBLE:View.GONE);
            textViewTasks.setText(String.valueOf(board.getTotalTasks()));
            textViewEmployees.setText(String.valueOf(board.getUsers().size()));
            textViewTitle.setText(String.valueOf(board.getTitle()));
            try {
                ImageUtils.loadProfileToImageView(a,board.getUsers().get(0).getProfile(),imageView0);
                imageView0.setVisibility(View.VISIBLE);
            }catch (Exception ignore){
                imageView0.setVisibility(View.GONE);
            }
            try{
                ImageUtils.loadProfileToImageView(a,board.getUsers().get(1).getProfile(),imageView1);
                imageView1.setVisibility(View.VISIBLE);
            }catch (Exception ignore){
                imageView1.setVisibility(View.GONE);
            }
            try{
                ImageUtils.loadProfileToImageView(a,board.getUsers().get(2).getProfile(),imageView2);
                imageView2.setVisibility(View.VISIBLE);
            }catch (Exception ignore){
                imageView2.setVisibility(View.GONE);
            }
            ImageUtils.loadProfileToImageView(a,board.getOwner().getProfile(),imageViewProfileAdmin);
        }
    }
}