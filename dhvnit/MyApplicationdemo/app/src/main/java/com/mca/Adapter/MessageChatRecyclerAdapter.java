package com.mca.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mca.Activity.MessageChatActivity;
import com.mca.Model.MessageDetails;
import com.mca.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class MessageChatRecyclerAdapter extends RealmRecyclerViewAdapter<MessageDetails, MessageChatRecyclerAdapter.MyViewHolder> {

    MessageChatActivity mActivity;

    public MessageChatRecyclerAdapter(MessageChatActivity activity,
                                      OrderedRealmCollection<MessageDetails> data) {
        super(data, true);
        this.mActivity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MessageDetails obj = getData().get(position);

        holder.msg.setText(obj.getMessage());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView msg;

        public MyViewHolder(View view) {
            super(view);
            msg = view.findViewById(R.id.tv_receive_msg);
        }
    }
}
