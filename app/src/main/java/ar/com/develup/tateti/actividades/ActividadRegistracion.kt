package ar.com.develup.tateti.actividades

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ar.com.develup.tateti.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.actividad_registracion.*

class ActividadRegistracion : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_registracion)
        registrar.setOnClickListener { registrarse() }
    }

    fun registrarse() {
        val passwordIngresada = password.text.toString()
        val confirmarPasswordIngresada = confirmarPassword.text.toString()
        val email = email.text.toString()

        if (email.isEmpty()) {
            // Si no completo el email, muestro mensaje de error
            Snackbar.make(rootView, "Email requerido", Snackbar.LENGTH_SHORT).show()
        } else if (passwordIngresada == confirmarPasswordIngresada) {
            // Si completo el email y las contraseñas coinciden, registramos el usuario en Firebase
            registrarUsuarioEnFirebase(email, passwordIngresada)
        } else {
            // No coinciden las contraseñas, mostramos mensaje de error
            Snackbar.make(rootView, "Las contraseñas no coinciden", Snackbar.LENGTH_SHORT).show()
        }
    }
    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {

        }
    }


    private fun registrarUsuarioEnFirebase(email: String, passwordIngresada: String) {
        // TODO-05-AUTHENTICATION
        // Crear el usuario con el email y passwordIngresada
        // Ademas, registrar en CompleteListener el listener registracionCompletaListener definido mas abajo
        auth.createUserWithEmailAndPassword(email, password.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }

    }

    private fun updateUI(user: FirebaseUser?) {
        TODO("Not yet implemented")
    }

    private val registracionCompletaListener: OnCompleteListener<AuthResult?> = OnCompleteListener { task ->
        if (task.isSuccessful) {
            // Si se registro OK, muestro mensaje y envio mail de verificacion
            Snackbar.make(rootView, "Registro exitoso", Snackbar.LENGTH_SHORT).show()
            enviarEmailDeVerificacion()
        } else if (task.exception is FirebaseAuthUserCollisionException) {
            // Si el usuario ya existe, mostramos error
            Snackbar.make(rootView, "El usuario ya existe", Snackbar.LENGTH_SHORT).show()
        } else {
            // Por cualquier otro error, mostramos un mensaje de error
            Snackbar.make(rootView, "El registro fallo: " + task.exception, Snackbar.LENGTH_LONG).show()
        }
   }

    private fun enviarEmailDeVerificacion() {
        // TODO-05-AUTHENTICATION
        // Enviar mail de verificacion al usuario currentUser
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }
}