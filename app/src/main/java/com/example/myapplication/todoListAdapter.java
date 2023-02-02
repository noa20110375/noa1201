package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class todoListAdapter extends RecyclerView.Adapter<todoListAdapter.MyViewHolder> {

    private List<todoList> list;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
    //현재 연결은 데이터베이스에만 딱 연결해놓고
    //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.
    private DatabaseReference databaseReference = database.getReference();

    public todoListAdapter(List<todoList> listDto) {
        list = listDto;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private CheckBox chkContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chkContent = itemView.findViewById(R.id.chkContent);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        todoList todoList = list.get(position);
        holder.chkContent.setText(todoList.getContent());

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }
}
