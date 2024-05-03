package com.mgok.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mgok.demo.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddBinding binding;
    private Contact contact;

    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        database = new MyDatabase(this);
        contact = (Contact) getIntent().getSerializableExtra("contact");
        if (contact != null) {
            binding.edtId.setText(String.valueOf(contact.getId()));
            binding.btnAdd.setText("UPDATE");
            binding.edtId.setEnabled(false);
        } else {
            contact = new Contact();
        }
        binding.btnAdd.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) finish();
        else if (id == R.id.btn_add) addContact();
    }

    private void addContact() {
        contact.setName(binding.edtName.getText().toString().trim());
        if (contact.getName().isEmpty()) {
            Toast.makeText(this, "name null", Toast.LENGTH_SHORT).show();
            return;
        }
        contact.setPhone(binding.edtPhone.getText().toString().trim());
        if (contact.getPhone().isEmpty()) {
            Toast.makeText(this, "phone null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contact.getId() != null) {
            long result = database.update(contact);
            if (result > 0) {
                Toast.makeText(this, "update success", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "update fail", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                int id = Integer.parseInt(binding.edtId.getText().toString().trim());
                contact.setId(id);
                long result = database.insert(contact);
                if (result > 0) {
                    Toast.makeText(this, "add success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(this, "add fail", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "id invalid", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}