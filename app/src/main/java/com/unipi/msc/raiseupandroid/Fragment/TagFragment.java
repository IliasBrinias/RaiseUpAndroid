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

import com.unipi.msc.raiseupandroid.Activity.TagActivity;
import com.unipi.msc.raiseupandroid.Adapter.NavTagAdapter;
import com.unipi.msc.raiseupandroid.Interface.OnNavTagClickListener;
import com.unipi.msc.raiseupandroid.Model.Tag;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.CustomBottomSheet;
import com.unipi.msc.raiseupandroid.Tools.NameTag;

import java.util.ArrayList;
import java.util.List;

public class TagFragment extends Fragment {
    RecyclerView recyclerView;
    NavTagAdapter adapter;
    ImageButton imageButtonCreateTag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tag, container, false);
        initViews(v);
        initListeners();
        return v;
    }

    private void initListeners() {
        imageButtonCreateTag.setOnClickListener(v->startActivity(new Intent(requireActivity(),TagActivity.class)));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag(1L,"Test0","#80FFFFFF"));
        tagList.add(new Tag(2L,"Test1","#80F2FFFF"));
        tagList.add(new Tag(3L,"Test2","#80FF2FFF"));
        tagList.add(new Tag(4L,"Test3","#80FFF2FF"));
        tagList.add(new Tag(5L,"Test4","#80FFFF2F"));
        tagList.add(new Tag(6L,"Test5","#80FFFFF2"));
        adapter = new NavTagAdapter(requireActivity(), tagList, new OnNavTagClickListener() {

            @Override
            public void onEdit(View view, int position) {
                Intent intent = new Intent(requireActivity(), TagActivity.class);
                intent.putExtra(NameTag.TAG_ID, tagList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
        imageButtonCreateTag = v.findViewById(R.id.imageButtonCreateTag);
    }
}