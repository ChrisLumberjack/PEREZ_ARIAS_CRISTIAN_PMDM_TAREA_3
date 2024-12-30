package tarea3.perez_arias_cristian_pmdm_tarea_3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 9001  // Request Code for Google Sign-In

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_login)

        val appCheck = FirebaseAppCheck.getInstance()
        appCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())

        // Inicialización de Firebase
        auth = FirebaseAuth.getInstance()

        // Login button (Correo y Contraseña)
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {

            val email = findViewById<EditText>(R.id.usernameEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()

            if (!email.isEmpty() && !password.isEmpty()) {

                // Iniciar sesión con correo y contraseña
                loginUser(email, password)
            }else{
                Toast.makeText(baseContext, "Ingresa email y contraseña.", Toast.LENGTH_SHORT).show()
            }
        }

        // Google Login button
        val googleLoginButton = findViewById<MaterialButton>(R.id.googleLoginButton)
        googleLoginButton.setOnClickListener {
            signInWithGoogle()
        }

        // Register button (Registro de usuario con correo y contraseña)
        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Función para iniciar sesión con correo y contraseña
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Proceder a la siguiente pantalla
                    Toast.makeText(baseContext, "Authentication Success.", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()  // Cerrar la pantalla de login
                } else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }



    // Función para iniciar sesión con Google
    private fun signInWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("620757350243-qhpiosrsmdrh1h16c2accoikclt5k9ho.apps.googleusercontent.com")  // Reemplaza con tu ID de cliente de Google
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Manejar el resultado de la autenticación con Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                // Usar el ID token de Google para la autenticación con Firebase
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("Google Sign-In", "signInResult:failed code=" + e.statusCode)
                Toast.makeText(this, "Google sign-in failed: " + e.statusCode, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Autenticación con Firebase usando el ID de Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "Google Authentication Success.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Redirigir a MainActivity después de la autenticación exitosa
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()  // Cerrar LoginActivity para que no aparezca en el stack
                } else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
