package com.example.buoi7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalAmount;
    private ListView lvExpenses;
    private Button btnAddExpense;

    private ArrayList<Expense> expenseList;
    private ExpenseAdapter adapter;

    private final ActivityResultLauncher<Intent> addExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Expense newExpense = (Expense) result.getData().getSerializableExtra("new_expense");
                    if (newExpense != null) {
                        expenseList.add(newExpense);
                        adapter.notifyDataSetChanged();
                        updateTotalAmount();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> detailExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    int deletePos = result.getData().getIntExtra("delete_position", -1);
                    if (deletePos >= 0 && deletePos < expenseList.size()) {
                        expenseList.remove(deletePos);
                        adapter.notifyDataSetChanged();
                        updateTotalAmount();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        lvExpenses = findViewById(R.id.lvExpenses);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        expenseList = new ArrayList<>();
        expenseList.add(new Expense("Ăn sáng", 30000, "Ăn uống", "12/10/2026", "Bánh mì + Cà phê sáng"));
        expenseList.add(new Expense("Đổ xăng", 50000, "Đi lại", "12/10/2026", "Đổ xăng RON 95"));
        expenseList.add(new Expense("Mua sách", 120000, "Học tập", "13/10/2026", "Sách Lập trình Android"));

        adapter = new ExpenseAdapter(this, expenseList);
        lvExpenses.setAdapter(adapter);

        updateTotalAmount();

        lvExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense selectedExpense = expenseList.get(position);
                Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
                intent.putExtra("expense_data", selectedExpense);
                intent.putExtra("expense_position", position);
                detailExpenseLauncher.launch(intent);
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                addExpenseLauncher.launch(intent);
            }
        });
    }

    private void updateTotalAmount() {
        long total = 0;
        for (Expense item : expenseList) {
            total += item.getAmount();
        }
        tvTotalAmount.setText(Expense.formatCurrency(total));
    }
}