package com.mgok.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.mgok.demo.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContactOnClick, View.OnClickListener {

    private ActivityMainBinding binding;
    private ContactAdapter adapter;
    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        ArrayList<Contact> list = database.getList();
        adapter.setData(list);
    }

    private void initView() {
        database = new MyDatabase(this);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.rcvContact.addItemDecoration(decoration);
        adapter = new ContactAdapter(this);
        binding.rcvContact.setAdapter(adapter);
        binding.fabAdd.setOnClickListener(this);
    }

    @Override
    public void onLongClick(Contact contact, View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_edit) {
                changeAdd(contact);
            } else if (id == R.id.menu_delete) {
                showDialogDelete(contact, position);
            }
            return true;
        });
        popupMenu.show();
    }

    private void showDialogDelete(Contact contact, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("You want to delete?");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("OK", (dialog, which) -> {
            long contactId = database.delete(contact.getId());
            if (contactId > 0) {
                adapter.removeAt(position);
            } else {
                Toast.makeText(this, "delete fail", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add) changeAdd(null);
    }


    private void changeAdd(Contact contact) {
        Intent intent = new Intent(this, AddActivity.class);
        if (contact != null) intent.putExtra("contact", contact);
        startActivity(intent);
    }
}