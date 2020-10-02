package com.example.chatclient.activities.recyclerviewadapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chatclient.ChatApplication;
import com.example.chatclient.R;
import com.example.chatclient.roomdatabase.entites.ConversationWithUsers;
import com.example.chatclient.roomdatabase.entites.Message;
import com.example.chatclient.roomdatabase.entites.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<ConversationWithUsers> conv;
    private List<Message> messages;
    private Context context;
    private String name;
    private int id;

    public MessageAdapter(Context context,List<ConversationWithUsers> conv,List<Message> messages){
        this.messages=messages;
        this.context=context;
        this.conv=conv;
        id=((ChatApplication)context.getApplicationContext()).getAuthManager().getId();
        name=((ChatApplication)context.getApplicationContext()).getAuthManager().getName();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout view=(LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_view,parent,false);
        MessageAdapter.MessageViewHolder viewholder=new MessageAdapter.MessageViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message msg=messages.get(position);
        String user="";
        for(User u:conv.get(0).users)
            if(u.userId==msg.userCreator)
                user=u.name;
        holder.user.setText(user);
        holder.message.setText(msg.msg);
        if(msg.userCreator==id){
            holder.layout.setGravity(Gravity.RIGHT);
            holder.user.setGravity(Gravity.RIGHT);
            holder.message.setGravity(Gravity.RIGHT);
            holder.user.setVisibility(View.GONE);
        }else{
            holder.layout.setGravity(Gravity.LEFT);
            holder.user.setGravity(Gravity.LEFT);
            holder.message.setGravity(Gravity.LEFT);
            holder.user.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends  RecyclerView.ViewHolder{
        public LinearLayout layout;
        public TextView user;
        public TextView message;

        public MessageViewHolder(@NonNull LinearLayout itemView) {
            super(itemView);
            layout=itemView;
            user=(TextView)itemView.findViewById(R.id.user);
            message=(TextView)itemView.findViewById(R.id.msg);
        }
    }

}
