package fr.esgi.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        navController = Navigation.findNavController(view)

        val email = view.findViewById<EditText>(R.id.email_edit_text)
        val passwordResetButton = view.findViewById<Button>(R.id.password_forgot_btn)
        val loading = view.findViewById<View>(R.id.loading)

        passwordResetButton.setOnClickListener {
            if (email.text.toString().isEmpty()) {
                Toast.makeText(context, R.string.forgot_password_empty_email, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            passwordResetButton.visibility= View.INVISIBLE
            loading.visibility = View.VISIBLE
            auth.sendPasswordResetEmail(email.text.toString())
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                        email.text.clear()
                        return@addOnCompleteListener
                    }
                    passwordResetButton.visibility= View.VISIBLE
                    loading.visibility = View.INVISIBLE
                    Toast.makeText(context, R.string.forgot_error_message, Toast.LENGTH_SHORT).show()
                }
        }
    }


}