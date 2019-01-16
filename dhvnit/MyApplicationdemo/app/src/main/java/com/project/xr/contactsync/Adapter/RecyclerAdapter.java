package com.project.xr.contactsync.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.xr.contactsync.Activity.ContactDetials;
import com.project.xr.contactsync.Activity.ContactListActivity;
import com.project.xr.contactsync.Model.Item;
import com.project.xr.contactsync.R;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RecyclerAdapter extends RealmRecyclerViewAdapter<Item, RecyclerAdapter.MyViewHolder> {

    private final ContactListActivity activity;

    public RecyclerAdapter(ContactListActivity activity,
                           OrderedRealmCollection<Item> data) {
        super(data, true);
        this.activity = activity;
    }

    public void refreshData() {
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Item obj = getData().get(position);

        holder.item_name.setText(obj.getName());

        Glide.with(activity)
                .load(obj.getImage())
                .apply(new RequestOptions()
                        .placeholder(null)
                        .error(null))
                .into(holder.image);

        if (obj.getPhone() != null) {
            holder.item_number.setText(obj.getPhone() + "");
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
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
                        activity.deleteItem(obj.getId());
                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.ll_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ContactDetials.class);
                intent.putExtra("name", obj.getName());
                intent.putExtra("phone", obj.getPhone());
                intent.putExtra("image", obj.getImage());
                intent.putExtra("id", obj.getId());
                activity.startActivity(intent);
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView item_name, item_number;
        private ImageView edit;
        private CircleImageView image;
        private LinearLayout ll_header;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.iv_item);
            item_name = view.findViewById(R.id.item_name);
            item_number = view.findViewById(R.id.item_number);
            edit = view.findViewById(R.id.iv_edit);
            ll_header = view.findViewById(R.id.ll_header);
        }
    }
}