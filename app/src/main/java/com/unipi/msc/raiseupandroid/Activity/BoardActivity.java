package com.unipi.msc.raiseupandroid.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Adapter.ColumnAdapter;
import com.unipi.msc.raiseupandroid.Interface.OnColumnTaskListener;
import com.unipi.msc.raiseupandroid.Interface.OnTaskClick;
import com.unipi.msc.raiseupandroid.Model.Board;
import com.unipi.msc.raiseupandroid.Model.Column;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.MockData;
import com.unipi.msc.raiseupandroid.Tools.NameTag;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardActivity extends AppCompatActivity implements View.OnDragListener {
    private RecyclerView recyclerViewColumns;
    private TextView textViewBoardTitle;
    private ImageButton imageButtonExit;
    private Board board;
    private LinearProgressIndicator linearProgressIndicator;
    private RaiseUpAPI raiseUpAPI;
    private ColumnAdapter columnAdapter;
    Toast t;
    Boolean isActive = false;
    boolean isDropped = false;
    private int lastLocationX = 0;
    float xDown = 0, yDown = 0;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        initViews();
        initObjects();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (columnAdapter!=null){
            getBoard(getIntent().getLongExtra(NameTag.BOARD_ID,0L));
        }
    }

    private void initObjects() {
        board = new Board();
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
        columnAdapter = new ColumnAdapter(this, board.getColumns(), new OnColumnTaskListener() {
            @Override
            public void onClick(View view, int columnPosition, int taskPosition) {
                Intent intent = new Intent(BoardActivity.this, TaskActivity.class);
                intent.putExtra(NameTag.TASK_ID, board.getColumns().get(columnPosition).getTasks().get(taskPosition).getId());
                BoardActivity.this.startActivity(intent);
            }
            @Override
            public void onDelete(View view, int columnPosition, int taskPosition) {

            }

            @Override
            public void onNextColumn(View view, int columnPosition, int taskPosition) {
                OnColumnTaskListener.super.onNextColumn(view, columnPosition, taskPosition);
            }

            @Override
            public void onPreviousColumn(View view, int columnPosition, int taskPosition) {
                OnColumnTaskListener.super.onPreviousColumn(view, columnPosition, taskPosition);
            }
        });
    }

    private void getBoard(long boardId) {
        if (boardId == 0L) return;
        raiseUpAPI.getBoard(UserUtils.loadBearerToken(this), boardId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(BoardActivity.this,response);
                    ActivityUtils.showToast(BoardActivity.this, t, msg);
                }else{
                    board = Board.buildBoardFromJson(response.body().get("data").getAsJsonObject());
                    loadData(board);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(BoardActivity.this, t);
            }
        });
    }

    private void loadData(Board board) {
        textViewBoardTitle.setText(board.getTitle());
        columnAdapter.setData(board.getColumns());
    }

    private void initListeners() {
        imageButtonExit.setOnClickListener(v->finish());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewColumns.setLayoutManager(linearLayoutManager);
        recyclerViewColumns.setHasFixedSize(true);
        List<Column> columnList = MockData.getTestColumns();
        recyclerViewColumns.setAdapter(columnAdapter);
        recyclerViewColumns.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager myLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                float scrollPosition = myLayoutManager.findFirstVisibleItemPosition() + 1;
                linearProgressIndicator.setProgress((int) ((scrollPosition/columnList.size())*100),true);
            }
        });
    }

    private void initViews() {
        recyclerViewColumns = findViewById(R.id.recyclerViewColumns);
        textViewBoardTitle = findViewById(R.id.textViewBoardTitle);
        imageButtonExit = findViewById(R.id.imageButtonExit);
        linearProgressIndicator = findViewById(R.id.linearProgressIndicator);
    }

    @Override
    public boolean onDrag(View v, DragEvent e) {
// Handle each of the expected events.
        switch(e.getAction()) {

            case DragEvent.ACTION_DRAG_STARTED:

                // Determine whether this View can accept the dragged data.
                if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                    // As an example, apply a blue color tint to the View to
                    // indicate that it can accept data.
                    v.setBackgroundColor(Color.BLUE);

                    // Invalidate the view to force a redraw in the new tint.
                    v.invalidate();

                    // Return true to indicate that the View can accept the dragged
                    // data.
                    return true;
                }

                // Return false to indicate that, during the current drag and drop
                // operation, this View doesn't receive events again until
                // ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:

                // Apply a green tint to the View.
                v.setBackgroundColor(Color.GREEN);

                // Invalidate the view to force a redraw in the new tint.
                v.invalidate();

                // Return true. The value is ignored.
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0];
                int translatedX = x - recyclerViewColumns.getScrollX();
                int threshold = 30;
                // make a scrolling up due the y has passed the threshold
                if (translatedX < threshold){
                    // make a scroll up by 30 px
                    recyclerViewColumns.scrollBy(-30, 0);
                }else if (translatedX + threshold > 500){
                    // make a scroll down by 30 px
                    recyclerViewColumns.scrollBy(30, 0);
                }
                // Ignore the event.
                return false;

            case DragEvent.ACTION_DRAG_EXITED:

                // Reset the color tint to blue.
                v.setBackgroundColor(Color.BLUE);

                // Invalidate the view to force a redraw in the new tint.
                v.invalidate();

                // Return true. The value is ignored.
                return true;

            case DragEvent.ACTION_DROP:

                // Get the item containing the dragged data.
                ClipData.Item item = e.getClipData().getItemAt(0);

                // Get the text data from the item.
                CharSequence dragData = item.getText();

                // Display a message containing the dragged data.
                Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_LONG).show();

                // Turn off color tints.
                ((ImageView)v).clearColorFilter();

                // Invalidate the view to force a redraw.
                v.invalidate();

                // Return true. DragEvent.getResult() returns true.
                return true;

            case DragEvent.ACTION_DRAG_ENDED:

                // Turn off color tinting.
                v.setBackgroundColor(Color.BLUE);

                // Invalidate the view to force a redraw.
                v.invalidate();

                // Do a getResult() and displays what happens.
                if (e.getResult()) {
                    Toast.makeText(this, "The drop was handled.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_LONG).show();
                }

                // Return true. The value is ignored.
                return true;

            // An unknown action type is received.
            default:
                break;
        }

        return false;
    }
}