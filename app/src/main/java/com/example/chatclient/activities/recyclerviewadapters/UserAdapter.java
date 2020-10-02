package com.example.chatclient.activities.recyclerviewadapters;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatclient.R;
import com.example.chatclient.roomdatabase.entites.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users;
    private List<User> selected;

    public UserAdapter(List<User> users){
        this.users=users;
        selected=new ArrayList<>();
    }

    public void itemClick(View view, User user){
        TextView textView=(TextView)view;
        if(selected.contains(user)){
            selected.remove(user);
            textView.setTypeface(null, Typeface.NORMAL);
            Log.println(Log.INFO,"user_deselected",user.name);
            return;
        }else{
            selected.add(user);
            textView.setTypeface(null, Typeface.BOLD);
            Log.println(Log.INFO,"user_selected",user.name);
            return;
        }
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view=(TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.user_text_view,parent,false);
        UserViewHolder viewholder=new UserViewHolder(view);
        viewholder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick(viewholder.textView,viewholder.user);
                Log.println(Log.INFO,"user_clicked",viewholder.user.name);
            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.textView.setText(users.get(position).name);
        holder.user=users.get(position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public User user;
        public UserViewHolder(@NonNull TextView itemView) {
            super(itemView);
            textView=itemView;
        }
        
    }

    public List<User> getSelected() {
        return selected;
    }
}
