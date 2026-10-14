package com.example.buoi7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etExpenseName, etExpenseAmount, etExpenseNote;
    private Spinner spCategory;
    private DatePicker datePicker;
    private Button btnSaveExpense;
    private TextView tvBackAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etExpenseName = findViewById(R.id.etExpenseName);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        etExpenseNote = findViewById(R.id.etExpenseNote);
        spCategory = findViewById(R.id.spCategory);
        datePicker = findViewById(R.id.datePicker);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        tvBackAdd = findViewById(R.id.tvBackAdd);

        String[] categories = {"Ăn uống", "Đi lại", "Học tập", "Giải trí", "Khác"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerAdapter);

        tvBackAdd.setOnClickListener(v -> finish());

        btnSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });
    }

    private void saveExpense() {
        String name = etExpenseName.getText().toString().trim();
        String amountStr = etExpenseAmount.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();
        String note = etExpenseNote.getText().toString().trim();

        if (name.isEmpty()) {
            etExpenseName.setError("Tên khoản chi không được để trống");
            etExpenseName.requestFocus();
            Toast.makeText(this, "Vui lòng nhập tên khoản chi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountStr.isEmpty()) {
            etExpenseAmount.setError("Số tiền không được để trống");
            etExpenseAmount.requestFocus();
            Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        long amount;
        try {
            amount = Long.parseLong(amountStr);
        } catch (NumberFormatException e) {
            etExpenseAmount.setError("Số tiền không hợp lệ");
            etExpenseAmount.requestFocus();
            return;
        }

        if (amount <= 0) {
            etExpenseAmount.setError("Số tiền phải lớn hơn 0");
            etExpenseAmount.requestFocus();
            Toast.makeText(this, "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

        Expense newExpense = new Expense(name, amount, category, date, note);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_expense", newExpense);
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Đã thêm khoản chi thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}