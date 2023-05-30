
package com.example.customermanagement
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.database.Cursor

class MainActivity : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAddCustomer = findViewById<Button>(R.id.btnAdd)
        btnAddCustomer.setOnClickListener {
            val db = DBHelper(this, null)
            val etID = findViewById<EditText>(R.id.etID)
            val etName = findViewById<EditText>(R.id.etName)
            val etEmail = findViewById<EditText>(R.id.etEmail)
            val etMobile = findViewById<EditText>(R.id.etMobile)

            val id = etID.text.toString().toInt()
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val mobile = etMobile.text.toString()

            db.addCustomer(id, name, email, mobile)

            Toast.makeText(this, name + " added to database", Toast.LENGTH_SHORT).show()
            etName.text.clear()
            etEmail.text.clear()
            etMobile.text.clear()
        }

        val btnShowCustomers = findViewById<Button>(R.id.btnShow)
        btnShowCustomers.setOnClickListener {
            val db = DBHelper(this, null)
            val userlist = db.getAllCustomers()
            val tvIdContent = findViewById<TextView>(R.id.tvIdContent)
            val tvNameContent = findViewById<TextView>(R.id.tvNameContent)
            val tvEmailContent = findViewById<TextView>(R.id.tvEmailContent)
            val tvMobileContent = findViewById<TextView>(R.id.tvMobileContent)

            tvIdContent.text = ""
            tvNameContent.text = ""
            tvEmailContent.text = ""
            tvMobileContent.text = ""

            userlist.forEach{
                tvIdContent.append("${it.getIdText()}\n")
                tvNameContent.append("${it.getNameText()}\n")
                tvEmailContent.append("${it.getEmailText()}\n")
                tvMobileContent.append("${it.getMobileText()}\n")
            }
        }

        val btnDeleteCustomers = findViewById<Button>(R.id.btnDelete)
        btnDeleteCustomers.setOnClickListener {
            val db = DBHelper(this, null)
            val id = findViewById<EditText>(R.id.etID).text.toString()
            val rows = db.deleteCustomer(id)
            Toast.makeText(this,
                when (rows) {
                    0 -> "Nothing deleted"
                    1 -> "1 customer deleted"
                    else -> "" // shouldn't happen
                },
                Toast.LENGTH_LONG).show()
        }

//        val btnSearch = findViewById<Button>(R.id.btnSearch)
//        btnSearch.setOnClickListener {
//            val db = DBHelper(this, null)
//            val etName = findViewById<EditText>(R.id.etName)
//            val name = etName.text.toString()
//
//            val user = db.searchUserByName(name)
//
//            if (user != null) {
//                val tvIdContent = findViewById<TextView>(R.id.tvIdContent)
//                val tvNameContent = findViewById<TextView>(R.id.tvNameContent)
//                val tvEmailContent = findViewById<TextView>(R.id.tvEmailContent)
//                val tvMobileContent = findViewById<TextView>(R.id.tvMobileContent)
//
//                tvIdContent.text = user.getIdText()
//                tvNameContent.text = user.getNameText()
//                tvEmailContent.text = user.getEmailText()
//                tvMobileContent.text = user.getMobileText()
//            } else {
//                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
//            }
//        }

        val btnSearch = findViewById<Button>(R.id.btnSearch)
        btnSearch.setOnClickListener {
            val db = DBHelper(this, null)
            val etName = findViewById<EditText>(R.id.etName)
            val name = etName.text.toString()

            val cursor = db.searchByName(name)

            val tvId = findViewById<TextView>(R.id.tvIdContent)
            val tvName = findViewById<TextView>(R.id.tvNameContent)
            val tvEmail = findViewById<TextView>(R.id.tvEmailContent)
            val tvMobile = findViewById<TextView>(R.id.tvMobileContent)

            if (cursor != null && cursor.moveToFirst()) {
                tvId.text = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ID)) + "\n"
                tvName.text = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME)) + "\n"
                tvEmail.text = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.EMAIL)) + "\n"
                tvMobile.text = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MOBILE)) + "\n"

                while (cursor.moveToNext()) {
                    tvId.append(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ID)) + "\n")
                    tvName.append(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME)) + "\n")
                    tvEmail.append(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.EMAIL)) + "\n")
                    tvMobile.append(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.MOBILE)) + "\n")
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }

            cursor?.close()
        }



        val btnUpdateCustomer = findViewById<Button>(R.id.btnUpdate)
        btnUpdateCustomer.setOnClickListener {
            val db = DBHelper(this, null)
            val name = findViewById<EditText>(R.id.etName).text.toString()
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val mobile = findViewById<EditText>(R.id.etMobile).text.toString()
            val rows = db.updateCustomer(name, email, mobile)
            Toast.makeText(this, "$rows customers updated", Toast.LENGTH_LONG).show()
        }
    }
}