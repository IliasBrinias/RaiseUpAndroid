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
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Activity.TagActivity;
import com.unipi.msc.raiseupandroid.Adapter.NavTagAdapter;
import com.unipi.msc.raiseupandroid.Interface.OnNavTagClickListener;
import com.unipi.msc.raiseupandroid.Model.Tag;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.NameTag;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagFragment extends Fragment {
    private RecyclerView recyclerView;
    private NavTagAdapter adapter;
    private ImageButton imageButtonCreateTag;
    private RaiseUpAPI raiseUpAPI;
    private final List<Tag> tags = new ArrayList<>();
    private Toast t;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tag, container, false);
        initViews(v);
        initObjects();
        initListeners();
        return v;
    }

    @Override
    public void onResume() {
        if (adapter!=null) loadTags();
        super.onResume();
    }

    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(requireActivity()).create(RaiseUpAPI.class);
    }

    private void initListeners() {
        imageButtonCreateTag.setOnClickListener(v->startActivity(new Intent(requireActivity(),TagActivity.class)));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new NavTagAdapter(requireActivity(), tags, new OnNavTagClickListener() {

            @Override
            public void onEdit(View view, int position) {
                Intent intent = new Intent(requireActivity(), TagActivity.class);
                intent.putExtra(NameTag.TAG_ID, tags.get(position).getId());
                startActivity(intent);
            }
            @Override
            public void onDelete(View view, int position) {
                deleteTag(tags.get(position));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
        imageButtonCreateTag = v.findViewById(R.id.imageButtonCreateTag);
    }
    private void loadTags(){
        raiseUpAPI.getTags(UserUtils.loadBearerToken(requireActivity())).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(requireActivity(),response);
                    ActivityUtils.showToast(requireActivity(),t,msg);
                }else{
                    JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                    tags.clear();
                    for (int i=0;i<jsonArray.size();i++){
                        tags.add(Tag.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
                    }
                    adapter.setData(tags);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(),t);
            }
        });
    }
    private void deleteTag(Tag tag){
        raiseUpAPI.deleteTag(UserUtils.loadBearerToken(requireActivity()),tag.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(requireActivity(),response);
                    ActivityUtils.showToast(requireActivity(),t,msg);
                }else{
                    adapter.removeElement(tag);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(),t);
            }
        });
    }
}