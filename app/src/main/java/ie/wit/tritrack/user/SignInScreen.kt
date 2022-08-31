package ie.wit.tritrack.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ie.wit.tritrack.main.MainActivity
import ie.wit.tritrack.R

class SignInScreen : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var Register: Button
    var context: Context? = null
    lateinit var SinIn: Button
    lateinit var inputLoginEmail: EditText
    lateinit var inputLoginPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login_screen)

        context = this;

        firebaseAuth = FirebaseAuth.getInstance()

        initView()
        applyListeners()
    }

    private fun applyListeners() {
        Register.setOnClickListener {
            startActivity(Intent(context, RegisterScreen::class.java))
        }

        SinIn.setOnClickListener {
            val inputResultEmail = inputLoginEmail.text.toString().trim()
            val inputResultPassword = inputLoginPassword.text.toString().trim()

            if (inputResultEmail.isEmpty() || inputResultPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_LONG).show()
            } else {
                try {
                    firebaseAuth.signInWithEmailAndPassword(inputResultEmail, inputResultPassword)
                        .addOnCompleteListener(this@SignInScreen) { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(context, MainActivity::class.java))
                            } else {
                                Toast.makeText(
                                    context, "Wrong credentials.",
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