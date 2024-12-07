package com.huaranga.dayana.lab13

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huaranga.dayana.lab13.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos ViewBinding
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuramos el RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        // Obtenemos el directorio de medios externos
        val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val files = directory?.listFiles()

        // Verificamos si los archivos no son nulos y los revertimos
        if (files != null && files.isNotEmpty()) {
            adapter = GalleryAdapter(files)
            recyclerView.adapter = adapter
        } else {
            Toast.makeText(this, "No images found.", Toast.LENGTH_SHORT).show()
        }
    }
}