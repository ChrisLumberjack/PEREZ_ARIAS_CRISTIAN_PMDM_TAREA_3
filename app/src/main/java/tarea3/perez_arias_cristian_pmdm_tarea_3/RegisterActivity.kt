package tarea3.perez_arias_cristian_pmdm_tarea_3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicialización de Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Referencia a los campos de la UI
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val registerButton = findViewById<Button>(R.id.registerButtonRegister)

        // Acción al hacer click en el botón de registro
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            // Validación de los campos
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar a la función para registrar el usuario
                registerUser(email, password)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        // Crear un nuevo usuario con correo y contraseña
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Usuario registrado exitosamente
                    val user = auth.currentUser
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

                    // Navegar a la MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()  // Finaliza la actividad de registro
                } else {
                    // Si hay un error durante el registro
                    Toast.makeText(this, "Error de registro. Intenta de nuevo.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
