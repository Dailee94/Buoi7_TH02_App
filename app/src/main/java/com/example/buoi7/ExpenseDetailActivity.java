package com.example.buoi7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExpenseDetailActivity extends AppCompatActivity {

    private TextView tvDetailName, tvDetailAmount, tvDetailCategory, tvDetailDate, tvDetailNote, tvBackDetail;
    private Button btnBack, btnDelete;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailAmount = findViewById(R.id.tvDetailAmount);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailNote = findViewById(R.id.tvDetailNote);
        tvBackDetail = findViewById(R.id.tvBackDetail);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        if (intent != null) {
            Expense expense = (Expense) intent.getSerializableExtra("expense_data");
            position = intent.getIntExtra("expense_position", -1);

            if (expense != null) {
                tvDetailName.setText(expense.getName());
                tvDetailAmount.setText(Expense.formatCurrency(expense.getAmount()));
                tvDetailCategory.setText(expense.getCategory());
                tvDetailDate.setText(expense.getDate());

                if (expense.getNote() == null || expense.getNote().isEmpty()) {
                    tvDetailNote.setText("Không có ghi chú");
                } else {
                    tvDetailNote.setText(expense.getNote());
                }
            }
        }

        View.OnClickListener backListener = v -> finish();
        btnBack.setOnClickListener(backListener);
        tvBackDetail.setOnClickListener(backListener);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("delete_position", position);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(ExpenseDetailActivity.this, "Đã xóa khoản chi", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}