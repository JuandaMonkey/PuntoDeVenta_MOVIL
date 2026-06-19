package com.example.pv_movil.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pv_movil.R
import core.dtos.cliente.ClienteDTO

class ClienteAdapter(
    private var clientes: List<ClienteDTO>,
    private val onItemClick: (ClienteDTO) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvClave: TextView = view.findViewById(R.id.tvClave)
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.tvClave.text = cliente.clave.toString().padStart(4, '0')
        holder.tvNombre.text = cliente.nombreCompleto
        
        holder.itemView.setOnClickListener { onItemClick(cliente) }
    }

    override fun getItemCount() = clientes.size

    fun updateList(newList: List<ClienteDTO>) {
        clientes = newList
        notifyDataSetChanged()
    }
}
