package com.ernest.reed

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RaiActivity : AppCompatActivity() {

    private val messages = mutableListOf<RaiMessage>()
    private lateinit var adapter: RaiAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var emptyState: View

    private val activeColor = android.graphics.Color.parseColor("#FFFFFF")
    private val inactiveColor = android.graphics.Color.parseColor("#888888")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rai)

        recycler = findViewById(R.id.raiRecycler)
        emptyState = findViewById(R.id.raiEmptyState)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = RaiAdapter(messages)
        recycler.adapter = adapter

        val input = findViewById<EditText>(R.id.raiInput)
        val sendButton = findViewById<TextView>(R.id.raiSendButton)

        sendButton.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text)
                input.setText("")
            }
        }

        findViewById<View>(R.id.raiAttachChip).setOnClickListener {
            Toast.makeText(this, "Attach — coming soon", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.raiBrowseChip).setOnClickListener {
            Toast.makeText(this, "Browse Context — coming soon", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.raiVoiceChip).setOnClickListener {
            Toast.makeText(this, "Voice — coming soon", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.raiAddButton).setOnClickListener {
            Toast.makeText(this, "Add — coming soon", Toast.LENGTH_SHORT).show()
        }

        setupBottomNav()
    }

    private fun sendMessage(text: String) {
        messages.add(RaiMessage(text, isUser = true))
        adapter.notifyItemInserted(messages.size - 1)

        messages.add(RaiMessage("Still learning that, I am still evolving.", isUser = false))
        adapter.notifyItemInserted(messages.size - 1)

        emptyState.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        recycler.scrollToPosition(messages.size - 1)
    }

    private fun setupBottomNav() {
        val navHome = findViewById<View>(R.id.navHome)
        val navReel = findViewById<View>(R.id.navReel)
        val navRai = findViewById<View>(R.id.navRai)
        val navMessage = findViewById<View>(R.id.navMessage)
        val navProfile = findViewById<View>(R.id.navProfile)

        navHome.setOnClickListener {
            val intent = android.content.Intent(this, MainActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        navReel.setOnClickListener {
            val intent = android.content.Intent(this, MainActivity::class.java)
            intent.putExtra("open_tab", "reel")
            intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        navRai.setOnClickListener {
            // already here
        }
        navMessage.setOnClickListener {
            Toast.makeText(this, "Message — coming soon", Toast.LENGTH_SHORT).show()
        }
        navProfile.setOnClickListener {
            val intent = android.content.Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        highlightNav(navRai)
    }

    private fun highlightNav(selected: View) {
        val items = listOf(
            findViewById<View>(R.id.navHome),
            findViewById<View>(R.id.navReel),
            findViewById<View>(R.id.navRai),
            findViewById<View>(R.id.navMessage),
            findViewById<View>(R.id.navProfile)
        )
        for (item in items) {
            val isSelected = item.id == selected.id
            val color = if (isSelected) activeColor else inactiveColor
            for (i in 0 until (item as android.view.ViewGroup).childCount) {
                val child = item.getChildAt(i)
                when (child) {
                    is ImageView -> child.setColorFilter(color)
                    is TextView -> child.setTextColor(color)
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}
