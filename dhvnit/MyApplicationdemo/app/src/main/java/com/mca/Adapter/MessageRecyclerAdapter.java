package com.mca.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mca.Activity.ContactDetials;
import com.mca.Fragment.MessagesFragment;
import com.mca.Model.Message;
import com.mca.R;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class MessageRecyclerAdapter extends RealmRecyclerViewAdapter<Message, MessageRecyclerAdapter.MyViewHolder> {

    MessagesFragment mActivity;

    public MessageRecyclerAdapter(MessagesFragment activity,
                                  OrderedRealmCollection<Message> data) {
        super(data, true);
        this.mActivity = activity;
    }

    public void refreshData() {
        this.notifyDataSetChanged();
    }

    @Override
    public MessageRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new MessageRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MessageRecyclerAdapter.MyViewHolder holder, int position) {
        final Message obj = getData().get(position);

        holder.item_name.setText(obj.getDisplayMessage());

        Glide.with(mActivity.getActivity())
                .load(obj.getIcon())
                .apply(new RequestOptions()
                        .placeholder(null)
                        .error(null))
                .into(holder.image);

        if (obj.getId() != null) {
            holder.item_number.setText(obj.getId() + "");
        }

/*        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity.getActivity());
                dialogBuilder.setTitle("Edit Item");
                LayoutInflater inflater = activity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.edit_delete_layout, null);
                dialogBuilder.setView(dialogView);

                final EditText edt_name = dialogView.findViewById(R.id.edt_name);
                final EditText edt_number = dialogView.findViewById(R.id.edt_number);
                edt_name.setText(obj.getName());
                edt_number.setText(obj.getPhone());
                edt_name.setSelection(edt_name.getText().length());

                dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialogInterface.dismiss();
                        activity.editItem(obj.getId(), edt_name.getText().toString(), edt_number.getText().toString());
                    }
                });

                dialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        activity.deleteGroupData(obj.getId());
                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });*/

        Boolean read = obj.getReadStatus();
        if (!read) {
            holder.edit.setImageResource(R.mipmap.new_message);
        } else
            holder.edit.setVisibility(View.GONE);

        holder.ll_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivity.editMessage(obj.getId(), true, obj.getAccRej());

                Intent intent = new Intent(mActivity.getActivity(), ContactDetials.class);
                intent.putExtra("Name", "Message");
                intent.putExtra("name", obj.getDisplayMessage());
                intent.putExtra("option", obj.getOption());
                intent.putExtra("image", obj.getIcon());
                intent.putExtra("id", obj.getId());
                mActivity.startActivity(intent);
            }
        });

        Boolean accRej = obj.getAccRej();
        if (!accRej) {
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.editMessage(obj.getId(), obj.getReadStatus(), true);
                    holder.ll_acc_rej.setVisibility(View.GONE);
                }
            });

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.deleteMessageData(obj.getId());
                    holder.ll_acc_rej.setVisibility(View.GONE);
                }
            });
        } else
            holder.ll_acc_rej.setVisibility(View.GONE);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView item_name, item_number;
        private ImageView edit, accept, reject;
        private CircleImageView image;
        private LinearLayout ll_header, ll_acc_rej;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.iv_item);
            item_name = view.findViewById(R.id.item_name);
            item_number = view.findViewById(R.id.item_number);
            edit = view.findViewById(R.id.iv_edit);

            ll_acc_rej = view.findViewById(R.id.ll_acc_rej);
            ll_header = view.findViewById(R.id.ll_header);

            accept = view.findViewById(R.id.iv_accept);
            reject = view.findViewById(R.id.iv_reject);
        }
    }
}
