package com.example.chatclient.activities.recyclerviewadapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatclient.activities.Messages;
import com.example.chatclient.R;
import com.example.chatclient.roomdatabase.entites.ConversationWithUsers;
import com.example.chatclient.roomdatabase.entites.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private List<ConversationWithUsers> conversations;
    private Context context;

    public ConversationAdapter(Context context, List<ConversationWithUsers> conversations){
        this.context=context;
        this.conversations=conversations;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view=(TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_view,parent,false);
        ConversationAdapter.ConversationViewHolder viewholder=new ConversationAdapter.ConversationViewHolder(view);
        viewholder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMsgsr=new Intent(context, Messages.class);
                startMsgsr.putExtra("convId",viewholder.conv.conversation.convId);
                context.startActivity(startMsgsr);
            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        String name="";
        holder.conv=conversations.get(position);
        if(holder.conv.conversation.name.equals("")) {
            for(User u:holder.conv.users)
                name=name+u.name+", ";
            name=name.substring(0,name.length()-2);
        }else{
            name= holder.conv.conversation.name;
        }
        holder.textView.setText(name);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ConversationWithUsers conv;

        public ConversationViewHolder(@NonNull TextView itemView) {
            super(itemView);
            textView=itemView;
        }
    }
}
