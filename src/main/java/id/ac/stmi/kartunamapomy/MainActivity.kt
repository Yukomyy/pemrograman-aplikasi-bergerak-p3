package id.ac.stmi.kartunamapomy

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        findViewById<android.view.View>(R.id.buttonLihatKartu).setOnClickListener { bukaKartuNama() }
    }

    private fun bukaKartuNama() {
        val nama = findViewById<TextInputEditText>(R.id.inputNama).text.toString().trim()
        val email = findViewById<TextInputEditText>(R.id.inputEmail).text.toString().trim()
        val nomorHp = findViewById<TextInputEditText>(R.id.inputNomorHp).text.toString().trim()
        val url = findViewById<TextInputEditText>(R.id.inputUrl).text.toString().trim()
        val namaLayout = findViewById<TextInputLayout>(R.id.layoutNama)
        val emailLayout = findViewById<TextInputLayout>(R.id.layoutEmail)
        val nomorLayout = findViewById<TextInputLayout>(R.id.layoutNomorHp)
        val urlLayout = findViewById<TextInputLayout>(R.id.layoutUrl)
        listOf(namaLayout, emailLayout, nomorLayout, urlLayout).forEach { it.error = null }

        var valid = true
        if (nama.isBlank()) { namaLayout.error = getString(R.string.error_nama); valid = false }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { emailLayout.error = getString(R.string.error_email); valid = false }
        if (!nomorHp.matches(Regex("^[+]?[0-9][0-9\\s-]{7,14}$"))) { nomorLayout.error = getString(R.string.error_nomor); valid = false }
        if (url.isBlank() || !Patterns.WEB_URL.matcher(url).matches()) { urlLayout.error = getString(R.string.error_url); valid = false }
        if (!valid) return

        // Explicit Intent: tujuan ditentukan langsung ke ProfileActivity.
        val explicitIntent = Intent(this, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.EXTRA_NAMA, nama)
            putExtra(ProfileActivity.EXTRA_EMAIL, email)
            putExtra(ProfileActivity.EXTRA_NOMOR_HP, nomorHp)
            putExtra(ProfileActivity.EXTRA_URL, url)
        }
        startActivity(explicitIntent)
    }
}
