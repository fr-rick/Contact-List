package com.example.contactlist

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlist.model.Contact
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private val mainItems = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        contactAdapter = ContactAdapter(mainItems) {id ->
            showOptionsDialog(id)
        }
        recyclerView.adapter = contactAdapter

        val fabAdd: FloatingActionButton = findViewById(R.id.fab_add)
        fabAdd.setOnClickListener {
            val intent = Intent(this, FormContact::class.java)
            startActivity(intent)
        }

        Thread {
            val app = application as App
            val dao = app.db.contactDao()
            val response = dao.getAllContacts()


            runOnUiThread {
                mainItems.addAll(response)
                contactAdapter.notifyDataSetChanged()
            }

        }.start()


    }

    private fun showOptionsDialog(id: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
        alertDialogBuilder.setTitle("Opções")
        alertDialogBuilder.setMessage(R.string.options_message)
        alertDialogBuilder.setPositiveButton("Editar") { dialog, which ->
            editContact(id)
        }
        alertDialogBuilder.setNegativeButton("Excluir") { dialog, which ->
            deleteContact(id)
        }
        alertDialogBuilder.setNeutralButton("Cancelar", null)
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }

    private fun editContact(id: Int) {
        val intent = Intent(this@MainActivity, FormContact::class.java)
        intent.putExtra("contact_id", id)
        startActivity(intent)
    }

    private fun deleteContact(id: Int) {

        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
        alertDialogBuilder.setTitle("Excluir Contato")
        alertDialogBuilder.setMessage("Tem certeza de que deseja excluir este contato?")
        alertDialogBuilder.setPositiveButton("Sim") { dialog, which ->
            Thread {
                val app = application as App
                val dao = app.db.contactDao()
                val contactToDelete = dao.getContactById(id)
                if (contactToDelete != null) {
                    dao.deleteContact(contactToDelete)
                    runOnUiThread {
                        val position = mainItems.indexOfFirst { it.id == id }
                        mainItems.removeAt(position)
                        contactAdapter.notifyItemRemoved(position)
                        Toast.makeText(this@MainActivity, "Contato excluído com sucesso", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Contato não encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
        alertDialogBuilder.setNegativeButton("Cancelar", null)
        val dialog = alertDialogBuilder.create()
        dialog.show()

    }

    private inner class ContactAdapter(
        private val mainItems: List<Contact>,
        private val onLongClickListener: (Int) -> Unit,
    ) :
        RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

        inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(currentMainItem: Contact) {
                val item = itemView
                val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
                val emailTextView: TextView = itemView.findViewById(R.id.textViewEmail)
                val phoneTextView: TextView = itemView.findViewById(R.id.textViewPhone)

                nameTextView.text = currentMainItem.name
                emailTextView.text = currentMainItem.email
                phoneTextView.text = currentMainItem.phone

                item.setOnLongClickListener {
                    onLongClickListener.invoke(currentMainItem.id)
                    true
                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return ContactViewHolder(view)
        }

        override fun getItemCount(): Int {
            return mainItems.size
        }

        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val currentContact = mainItems[position]
            holder.bind(currentContact)
        }
    }
}