package com.example.contactlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.contactlist.model.Contact
import com.example.contactlist.model.ContactDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormContact : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_contact)

        editName = findViewById(R.id.editTextName)
        editEmail = findViewById(R.id.editTextEmail)
        editPhone = findViewById(R.id.editTextPhone)

        val contactId = intent.getIntExtra("contact_id", -1)
        if (contactId != -1) {
            loadContactForEdit(contactId)
        }

        val btnCancel: Button = findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }

        val btnSave: Button = findViewById(R.id.btnSave)
        btnSave.setOnClickListener {

            if (!validate()) {
                Toast.makeText(
                    this, R.string.fields_message,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val phone = editPhone.text.toString()

            val app = application as App
            val dao = app.db.contactDao()

            Thread {
                if (contactId != -1) {
                    dao.updateContact(Contact(id = contactId, name = name, email = email, phone = phone))
                } else {
                    dao.insertContact(Contact(name = name, email = email, phone = phone))
                }
                runOnUiThread {
                    startActivity(Intent(this@FormContact, MainActivity::class.java))
                    finish()
                }
            }.start()

        }


    }

    private fun loadContactForEdit(contactId: Int) {
        Thread {
            val app = application as App
            val dao = app.db.contactDao()
            val contact = dao.getContactById(contactId)

            runOnUiThread {
                if (contact != null) {
                    editName.setText(contact.name)
                    editEmail.setText(contact.email)
                    editPhone.setText(contact.phone)
                } else {
                    Toast.makeText(this@FormContact, "Contato não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun validate(): Boolean {
        return (editName.text.toString().isNotEmpty()
                && editEmail.text.toString().isNotEmpty()
                && editPhone.text.toString().isNotEmpty())
    }

}