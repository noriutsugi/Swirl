package com.eunoiapolis.swirlx

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileDashboard : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_dashboard)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("936011744294-kkvv5rkj3fdkq9lc8q310a5oafcid43u.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        findViewById<TextView>(R.id.txtpfname).text = auth.currentUser?.displayName.toString()
        findViewById<TextView>(R.id.txtpfemail).text = auth.currentUser?.email.toString()
        findViewById<TextView>(R.id.txtpfhor).text = "Vivekananda Hostel"
        val btnSignOut = findViewById<Button>(R.id.btnSignOut)

        btnSignOut?.setOnClickListener() {
            logout()
        }

    }
    fun logout() {
        mGoogleSignInClient.signOut().addOnCompleteListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}