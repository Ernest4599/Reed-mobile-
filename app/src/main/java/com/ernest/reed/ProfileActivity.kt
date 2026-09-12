package com.ernest.reed

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {

    private lateinit var contentRecycler: RecyclerView
    private lateinit var emptyState: TextView
    private lateinit var tabPosts: TextView
    private lateinit var tabReels: TextView
    private lateinit var tabShares: TextView
    private lateinit var tabTags: TextView
    private lateinit var tabs: List<TextView>

    private val activeColor = android.graphics.Color.parseColor("#FFFFFF")
    private val inactiveColor = android.graphics.Color.parseColor("#888888")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val coverImage = findViewById<ImageView>(R.id.coverImage)
        val avatarImage = findViewById<ImageView>(R.id.avatarImage)

        val pickCover = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                coverImage.setImageURI(uri)
            }
        }

        val pickAvatar = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                avatarImage.setImageURI(uri)
            }
        }

        findViewById<LinearLayout>(R.id.editCoverButton).setOnClickListener {
            pickCover.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        avatarImage.setOnClickListener {
            pickAvatar.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        contentRecycler = findViewById(R.id.profileContentRecycler)
        emptyState = findViewById(R.id.profileEmptyState)

        tabPosts = findViewById(R.id.tabPosts)
        tabReels = findViewById(R.id.tabReels)
        tabShares = findViewById(R.id.tabShares)
        tabTags = findViewById(R.id.tabTags)
        tabs = listOf(tabPosts, tabReels, tabShares, tabTags)

        tabPosts.setOnClickListener { showPosts() }
        tabReels.setOnClickListener { showReels() }
        tabShares.setOnClickListener { showEmpty(tabShares) }
        tabTags.setOnClickListener { showEmpty(tabTags) }

        showPosts()
        setupBottomNav()
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
            android.widget.Toast.makeText(this, "RAI — coming soon", android.widget.Toast.LENGTH_SHORT).show()
        }
        navMessage.setOnClickListener {
            android.widget.Toast.makeText(this, "Message — coming soon", android.widget.Toast.LENGTH_SHORT).show()
        }
        navProfile.setOnClickListener {
            // already here
        }

        highlightNav(navProfile)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    private fun highlightNav(selected: View) {
        val navHome = findViewById<View>(R.id.navHome)
        val navReel = findViewById<View>(R.id.navReel)
        val navRai = findViewById<View>(R.id.navRai)
        val navMessage = findViewById<View>(R.id.navMessage)
        val navProfile = findViewById<View>(R.id.navProfile)
        val items = listOf(navHome, navReel, navRai, navMessage, navProfile)

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

    private fun selectTab(selected: TextView) {
        for (tab in tabs) {
            tab.setTextColor(if (tab == selected) activeColor else inactiveColor)
        }
    }

    private fun showPosts() {
        selectTab(tabPosts)
        emptyState.visibility = View.GONE
        contentRecycler.visibility = View.VISIBLE
        contentRecycler.layoutManager = LinearLayoutManager(this)
        contentRecycler.adapter = PostAdapter(AppData.posts.filter { it.username == AppData.currentUsername })
    }

    private fun showReels() {
        selectTab(tabReels)
        emptyState.visibility = View.GONE
        contentRecycler.visibility = View.VISIBLE
        contentRecycler.layoutManager = GridLayoutManager(this, 3)
        contentRecycler.adapter = ReelGridAdapter(AppData.reels)
    }

    private fun showEmpty(selected: TextView) {
        selectTab(selected)
        contentRecycler.visibility = View.GONE
        emptyState.visibility = View.VISIBLE
    }
}
