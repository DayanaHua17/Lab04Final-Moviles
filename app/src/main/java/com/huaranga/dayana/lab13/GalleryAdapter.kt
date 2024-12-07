package com.huaranga.dayana.lab13

// Importa las clases necesarias para manejar la vista y los archivos
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huaranga.dayana.lab13.databinding.ListItemImgBinding
import java.io.File

// El adaptador para el RecyclerView que manejará las imágenes
class GalleryAdapter(private val fileArray: Array<File>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    // Clase interna que representa la vista individual de cada elemento de la lista
    class ViewHolder(private val binding: ListItemImgBinding) : RecyclerView.ViewHolder(binding.root) {
        // Función para enlazar los datos (en este caso el archivo de imagen) con la vista
        fun bind(file: File) {
            // Usamos Glide (una librería para cargar imágenes) para cargar la imagen en el ImageView
            Glide.with(binding.root.context) // Obtiene el contexto de la vista
                .load(file) // Carga el archivo de imagen (que es de tipo File)
                .into(binding.localImg) // Carga la imagen en el ImageView 'localImg' del XML
        }
    }

    // Este método crea un ViewHolder para cada elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Infla el layout del item individual que será usado en el RecyclerView (ListItemImgBinding)
        val binding = ListItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding) // Devuelve el ViewHolder con el binding de la vista
    }

    // Este método vincula los datos a la vista correspondiente en el ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Pasa el archivo correspondiente al ViewHolder para que se cargue en la vista
        holder.bind(fileArray[position])
    }

    // Este método retorna el número de elementos en el arreglo de archivos
    override fun getItemCount(): Int {
        return fileArray.size // El número de archivos es el número de ítems que se mostrarán
    }
}
