package com.eunoiapolis.swirlx

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // VAL
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)

        // INI VIEW
        findViewById<TextView>(R.id.txtWrongEmail).visibility = View.GONE

        var rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.fillAfter = true;
        findViewById<ImageView>(R.id.imageView3).startAnimation(rotation)

        var rotation2 = AnimationUtils.loadAnimation(this, R.anim.rotate2);
        rotation2.fillAfter = true;
        findViewById<ImageView>(R.id.imageView4).startAnimation(rotation2)

        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("936011744294-kkvv5rkj3fdkq9lc8q310a5oafcid43u.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()


        btnSignIn?.setOnClickListener() {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, Req_Code)
        }
    }
    fun loginSucess() {
        val intent = Intent(this, MachinesDashboard::class.java)
            //intent.putExtra("the_mess", txtf.text.toString())
        startActivity(intent)
    }
    fun wrongMailSelected() {
        Thread ( Runnable {
            runOnUiThread{
                findViewById<TextView>(R.id.txtWrongEmail).visibility = View.VISIBLE
            }
            Thread.sleep(3000)
            runOnUiThread{
                findViewById<TextView>(R.id.txtWrongEmail).visibility = View.GONE
            }
        } ).start()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            val loginErrMsg = "12500: "
            if (e.message == loginErrMsg) {
                wrongMailSelected()
            } else {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginSucess()
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(
                Intent(
                    this, MachinesDashboard
                    ::class.java
                )
            )
            finish()
        }
    }

}