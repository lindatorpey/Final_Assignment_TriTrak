package ie.wit.tritrack.user

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ie.wit.tritrack.R

class RegisterScreen : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var Register: Button
    var context: Context? = null
    lateinit var SinIn: Button
    lateinit var inputLoginEmail: EditText
    lateinit var inputLoginPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register_screen)

        context = this;

        firebaseAuth = FirebaseAuth.getInstance()

        initView()
        applyListeners()
    }

    private fun applyListeners() {
        SinIn.setOnClickListener {
            finish()
        }

        Register.setOnClickListener {
            val inputResultEmail = inputLoginEmail.text.toString().trim()
            val inputResultPassword = inputLoginPassword.text.toString().trim()

            if (inputResultEmail.isEmpty() || inputResultPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_LONG).show()
            } else {
                try {
                    firebaseAuth.createUserWithEmailAndPassword(inputResultEmail, inputResultPassword)
                        .addOnCompleteListener(this@RegisterScreen) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context, "Account create. Go back and login",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    context, "Error while creating account.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } catch (e: Exception) {
                    println(e)
                }
            }
        }
    }

    private fun initView() {
        SinIn = findViewById(R.id.SinIn)
        Register = findViewById(R.id.Register)
        inputLoginEmail = findViewById(R.id.inputLoginEmail)
        inputLoginPassword = findViewById(R.id.inputLoginPassword)
    }
}