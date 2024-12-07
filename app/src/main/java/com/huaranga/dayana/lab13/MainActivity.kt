package com.huaranga.dayana.lab13

// Importa las clases necesarias para gestionar la cámara, los permisos, la UI, etc.
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.huaranga.dayana.lab13.databinding.ActivityMainBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// Clase principal de la actividad de la cámara
class MainActivity : AppCompatActivity() {

    // Variables de instancia para manejar la UI y la cámara
    private lateinit var binding: ActivityMainBinding // Binding para el layout de la actividad
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider> // Para obtener el proveedor de la cámara
    private lateinit var cameraSelector: CameraSelector // Para elegir la cámara (delantera o trasera)
    private var imageCapture: ImageCapture? = null // Para capturar las fotos
    private lateinit var imgCaptureExecutor: ExecutorService // Executor para realizar la captura en un hilo separado

    // Este bloque registra un resultado para solicitar permisos de cámara
    private val cameraPermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
        if (permissionGranted) {
            startCamera() // Si el permiso es otorgado, inicializamos la cámara
        } else {
            // Si el permiso es denegado, mostramos un mensaje al usuario
            Snackbar.make(binding.root, "The camera permission is necessary", Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    // El método onCreate se ejecuta cuando la actividad es creada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el binding y establece la vista de la actividad
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el proveedor de la cámara y el selector para la cámara trasera
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor() // Crea un executor para manejar la captura de fotos en un hilo distinto

        // Solicita permiso para acceder a la cámara
        cameraPermissionResult.launch(android.Manifest.permission.CAMERA)

        // Configura el botón de captura de fotos
        binding.imgCaptureBtn.setOnClickListener {
            takePhoto() // Llama a la función para tomar una foto
        }

        // Configura el botón para cambiar entre la cámara delantera y trasera
        binding.switchBtn.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA // Cambia a la cámara delantera
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA // Cambia a la cámara trasera
            }
            startCamera() // Reinicia la cámara con la nueva selección
        }

        // Configura el botón para abrir la galería de imágenes
        binding.galleryBtn.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java) // Crea un intent para abrir la actividad de galería
            startActivity(intent) // Inicia la actividad de la galería
        }
    }

    // Método para iniciar la cámara
    private fun startCamera() {
        // Crea un objeto Preview para mostrar la vista previa de la cámara
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.preview.surfaceProvider) // Configura el proveedor de superficie para la vista previa
        }

        // Agrega un listener para iniciar la cámara
        cameraProviderFuture.addListener({
            // Obtiene el proveedor de la cámara
            val cameraProvider = cameraProviderFuture.get()
            // Crea el objeto para capturar imágenes
            imageCapture = ImageCapture.Builder().build()

            try {
                // Desvincula cualquier uso anterior de la cámara
                cameraProvider.unbindAll()
                // Vinca la cámara, la vista previa y la captura de imágenes al ciclo de vida de la actividad
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                // Si hubo un error al vincular la cámara, se muestra un mensaje en los logs
                Log.d(TAG, "Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(this)) // Ejecuta el listener en el hilo principal
    }

    // Método para tomar una foto
    private fun takePhoto() {
        imageCapture?.let { capture ->
            // Crea un nombre de archivo para la imagen
            val fileName = "JPEG_${System.currentTimeMillis()}.jpg"
            // Crea un archivo en el directorio de imágenes de la aplicación
            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build() // Define las opciones de salida

            // Toma la foto
            capture.takePicture(
                outputFileOptions, // Las opciones de salida
                imgCaptureExecutor, // El executor para la tarea en segundo plano
                object : ImageCapture.OnImageSavedCallback {
                    // Si la imagen es guardada con éxito
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        // Muestra un mensaje en los logs con la ruta del archivo
                        Log.i(TAG, "The image has been saved in ${file.absolutePath}")
                        // Escanea el archivo para hacerlo visible en la galería de medios
                        MediaScannerConnection.scanFile(this@MainActivity, arrayOf(file.absolutePath), null, null)
                    }

                    // Si ocurre un error al capturar la foto
                    override fun onError(exception: ImageCaptureException) {
                        // Muestra un mensaje de error al usuario
                        Toast.makeText(binding.root.context, "Error taking photo", Toast.LENGTH_LONG).show()
                        // Muestra detalles del error en los logs
                        Log.d(TAG, "Error taking photo: $exception")
                    }
                }
            )
        }
    }

    // Una constante para los mensajes de log
    companion object {
        val TAG = "MainActivity"
    }
}
