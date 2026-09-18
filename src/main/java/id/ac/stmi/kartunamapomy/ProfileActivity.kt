package id.ac.stmi.kartunamapomy

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profileRoot)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val nama = intent.getStringExtra(EXTRA_NAMA).orEmpty()
        val email = intent.getStringExtra(EXTRA_EMAIL).orEmpty()
        val nomorHp = intent.getStringExtra(EXTRA_NOMOR_HP).orEmpty()
        val url = intent.getStringExtra(EXTRA_URL).orEmpty()
        findViewById<TextView>(R.id.textInisial).text = nama.firstOrNull()?.uppercase() ?: "?"
        findViewById<TextView>(R.id.textNama).text = nama
        findViewById<TextView>(R.id.textEmail).text = email
        findViewById<TextView>(R.id.textNomorHp).text = nomorHp
        findViewById<TextView>(R.id.textUrl).text = url

        findViewById<android.view.View>(R.id.buttonWebsite).setOnClickListener {
            // Implicit Intent menuju browser.
            val implicitIntent = Intent(Intent.ACTION_VIEW, Uri.parse(normalisasiUrl(url)))
            jalankanIntent(implicitIntent)
        }
        findViewById<android.view.View>(R.id.buttonHubungi).setOnClickListener {
            // Implicit Intent menuju dialer; tidak langsung melakukan panggilan.
            val implicitIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(nomorHp)}"))
            jalankanIntent(implicitIntent)
        }
        findViewById<android.view.View>(R.id.buttonBagikan).setOnClickListener {
            val isi = getString(R.string.format_bagikan_kartu, nama, email, nomorHp, normalisasiUrl(url))
            // Implicit Intent untuk berbagi teks melalui aplikasi pilihan pengguna.
            val implicitIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subjek_bagikan, nama))
                putExtra(Intent.EXTRA_TEXT, isi)
            }
            jalankanIntent(Intent.createChooser(implicitIntent, getString(R.string.pilih_aplikasi_bagikan)))
        }
    }

    private fun jalankanIntent(intent: Intent) {
        try { startActivity(intent) }
        catch (_: ActivityNotFoundException) { Toast.makeText(this, R.string.aplikasi_tidak_ditemukan, Toast.LENGTH_SHORT).show() }
    }

    private fun normalisasiUrl(url: String) =
        if (url.startsWith("http://", true) || url.startsWith("https://", true)) url else "https://$url"

    companion object {
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_NOMOR_HP = "extra_nomor_hp"
        const val EXTRA_URL = "extra_url_portofolio"
    }
}
