package com.huaranga.dayana.lab13

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huaranga.dayana.lab13.databinding.ActivityGalleryBinding

// Esta es la clase principal de la actividad que gestiona la galería de imágenes
class GalleryActivity : AppCompatActivity() {

    // Declaramos las variables que usaremos más tarde
    private lateinit var binding: ActivityGalleryBinding  // Para manejar la vista con ViewBinding
    private lateinit var recyclerView: RecyclerView       // El RecyclerView donde mostraremos las imágenes
    private lateinit var adapter: GalleryAdapter          // El adaptador que maneja los datos de las imágenes

    // Método onCreate se ejecuta cuando la actividad se inicia
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos ViewBinding para vincular la interfaz de usuario con el código
        binding = ActivityGalleryBinding.inflate(layoutInflater)  // Usamos el inflador de vistas para cargar el archivo XML de la interfaz
        setContentView(binding.root)  // Establecemos el layout de la actividad (la vista) a la raíz de nuestro binding

        // Configuramos el RecyclerView que se mostrará en la pantalla
        recyclerView = binding.recyclerView  // Tomamos el RecyclerView de la vista
        recyclerView.layoutManager = GridLayoutManager(this, 1)  // Usamos un GridLayoutManager (en este caso, con 1 columna)

        // Obtenemos el directorio de imágenes en el almacenamiento externo del dispositivo
        val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)  // Nos da la carpeta de imágenes
        val files = directory?.listFiles()  // Listamos los archivos (imágenes) dentro de ese directorio

        // Verificamos que la lista de archivos no esté vacía y que no sea nula
        if (files != null && files.isNotEmpty()) {
            adapter = GalleryAdapter(files)  // Si hay imágenes, creamos un adaptador para mostrarlas
            recyclerView.adapter = adapter  // Establecemos el adaptador en el RecyclerView para que lo muestre
        } else {
            // Si no hay imágenes o el directorio está vacío, mostramos un mensaje de error
            Toast.makeText(this, "No images found.", Toast.LENGTH_SHORT).show()
        }
    }
}
