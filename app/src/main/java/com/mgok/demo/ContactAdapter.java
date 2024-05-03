package com.mgok.demo;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private ContactOnClick contactOnClick;

    private ArrayList<Contact> contacts = new ArrayList<>();


    public ContactAdapter(ContactOnClick contactOnClick) {
        this.contactOnClick = contactOnClick;
    }


    @SuppressLint("NotifyDataSetChanged")
    void setData(ArrayList<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    void removeAt(int position) {
        this.contacts.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_layout, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        if (contact != null) {
            holder.tvId.setText(String.valueOf(contact.getId()));
            holder.tvName.setText(contact.getName());
            holder.tvPhone.setText(contact.getPhone());
            holder.itemView.setOnLongClickListener(v -> {
                contactOnClick.onLongClick(contact, v, position);
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvName, tvPhone;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvId = itemView.findViewById(R.id.tv_id);
        }
    }
}
