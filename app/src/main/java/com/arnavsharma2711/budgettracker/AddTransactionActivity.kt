package com.arnavsharma2711.budgettracker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        labelInput.addTextChangedListener{
            if(it!!.isNotEmpty())
                label_layout.error = null
        }
        amtInput.addTextChangedListener{
            if(it!!.isNotEmpty())
                amt_layout.error = null
        }
        addBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val amount = amtInput.text.toString().toDoubleOrNull()
            val description = descInput.text.toString()

            if(label.isEmpty())
                label_layout.error = "Please enter a valid label"
            else if(amount==null)
                amt_layout.error = "Please enter a valid amount"
            else {
                val transaction  =Transaction(0, label, amount, description)
                insert(transaction)
            }
        }

        closeBtn.setOnClickListener {
            finish()
        }
    }
    private fun insert(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }

}