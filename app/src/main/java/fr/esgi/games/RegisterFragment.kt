package fr.esgi.games

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


// TODO: add credential validation and custom auth error message
class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loading = view.findViewById<View>(R.id.loading)
        val register = view.findViewById<Button>(R.id.register_btn)
        val username = view.findViewById<EditText>(R.id.username_edit_text)
        val email = view.findViewById<EditText>(R.id.email_edit_text)
        val password = view.findViewById<EditText>(R.id.password_edit_text)
        val confirmPassword = view.findViewById<EditText>(R.id.confirm_password_edit_text)

        auth = Firebase.auth
        navController = Navigation.findNavController(view)

        val registerButton = view.findViewById<Button>(R.id.register_btn)

        registerButton.setOnClickListener {
            if (
                email.text.toString().isEmpty() ||
                username.text.toString().isEmpty() ||
                password.text.toString().isEmpty())
            {
                Toast.makeText(context, R.string.register_empty_string_error_message, Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            if(!isPasswordSame(password.text.toString(), confirmPassword.text.toString())) {
                Toast.makeText(context, R.string.password_mismatch, Toast.LENGTH_SHORT).show()
                confirmPassword.text.clear()
                return@setOnClickListener
            }
            loading.visibility = View.VISIBLE
            register.visibility = View.INVISIBLE
            register(email, password, username, loading, registerButton)
        }
    }

    private fun register(email: EditText, password: EditText, username: EditText, loading : View, registerButton: Button) {
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    navController.navigate(R.id.action_registerFragment_to_homeFragment)
                    email.text.clear()
                    password.text.clear()
                    username.text.clear()
                } else {
                    Toast.makeText(
                        context, R.string.login_failed_message,
                        Toast.LENGTH_SHORT
                    ).show()
                    loading.visibility = View.INVISIBLE
                    registerButton.visibility = View.VISIBLE
                }
            }
    }

    private fun isPasswordSame(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}