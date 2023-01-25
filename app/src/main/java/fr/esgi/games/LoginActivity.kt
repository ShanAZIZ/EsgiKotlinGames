package fr.esgi.games

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.login_btn)

        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.email_edit_text).text.toString()
            val password = findViewById<EditText>(R.id.password_edit_text).text.toString()
            // Check if not empty
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, R.string.login_empty_string_error_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signIn(email, password)

        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            // TODO Redirect
        }
    }

    private fun signIn(email :String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // TODO Redirect
                } else {
                    Toast.makeText(baseContext, R.string.login_failed_message,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }




}