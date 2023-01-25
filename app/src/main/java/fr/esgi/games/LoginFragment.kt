package fr.esgi.games

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO add on start redirect
class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = Firebase.auth
        navController = Navigation.findNavController(view)

        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<Button>(R.id.login_btn)
        val registerButton = view.findViewById<Button>(R.id.register_btn)
        val forgotPassword = view.findViewById<TextView>(R.id.password_forgot)

        // Login button on click
        loginButton.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.email_edit_text).text.toString()
            val password = view.findViewById<EditText>(R.id.password_edit_text).text.toString()
            // Check if not empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, R.string.login_empty_string_error_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
           signIn(email, password)
        }

        // Go to register button on click
        registerButton.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        forgotPassword.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun signIn(email :String, password: String) {
        println(email)
        println(password)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    navController.navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Toast.makeText(context, R.string.login_failed_message,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}