package tarea3.perez_arias_cristian_pmdm_tarea_3.firebase;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;

public class FirebaseGoogleAuth {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001; // Solicitud de código para iniciar sesión
    private static final String TAG = "FirebaseConfig";

    public FirebaseGoogleAuth(Activity activity) {
        // Inicializa FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Configura Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1:620757350243:android:3dcd343f79382efeac8556") // Obtén este ID en tu consola de Firebase
                .requestEmail()
                .build();

        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(activity, gso);
    }

    // Método para iniciar el proceso de inicio de sesión con Google
    public void signInWithGoogle(Activity activity) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Método para manejar la respuesta de la actividad de inicio de sesión
    public void handleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Autenticación exitosa, autenticar con Firebase
            GoogleSignInAccount account = task.getResult();
            firebaseAuthWithGoogle(account);
        } catch (Exception e) {
            Log.w(TAG, "Google sign in failed", e);
        }
    }

    // Autenticar con Firebase usando el token de Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        // Utiliza el token de Google para autenticar con Firebase
        mAuth.signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // El usuario ha sido autenticado con Firebase
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, "signInWithCredential:success: " + user.getEmail());
                    } else {
                        // Si la autenticación falla
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    // Obtener el usuario actual
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // Método para cerrar sesión
    public void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
        Log.d(TAG, "Usuario ha cerrado sesión");
    }


}
