package com.ernest.reed

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.appbar.AppBarLayout

class MainActivity : AppCompatActivity() {

    private lateinit var postAdapter: PostAdapter
    private lateinit var reelAdapter: ReelAdapter
    private lateinit var feedRecycler: RecyclerView
    private lateinit var appBar: AppBarLayout
    private lateinit var appBarParent: android.view.ViewGroup
    private var snapHelper: SnapHelper? = null
    private var appBarIndex: Int = 0



    private enum class Tab { HOME, REEL }
    private var currentTab = Tab.HOME

    private lateinit var navItems: List<View>
    private val activeColor = "#FFFFFF".toColorInt()
    private val inactiveColor = "#888888".toColorInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appBar = findViewById(R.id.appBar)
        appBarParent = appBar.parent as android.view.ViewGroup
        appBarIndex = appBarParent.indexOfChild(appBar)

        val moments = listOf(
            Moment("Your moment"),
            Moment("Motivation"),
            Moment("Business"),
            Moment("Tech"),
            Moment("Lifestyle"),
            Moment("Mindset")
        )
        val momentsRecycler = findViewById<RecyclerView>(R.id.momentsRecycler)
        momentsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        momentsRecycler.adapter = MomentAdapter(moments)
        momentsRecycler.isNestedScrollingEnabled = false

        feedRecycler = findViewById(R.id.feedRecycler)
        feedRecycler.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(AppData.posts)
        reelAdapter = ReelAdapter(AppData.reels)
        feedRecycler.adapter = postAdapter

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val mimeType = contentResolver.getType(uri) ?: ""
                if (mimeType.startsWith("video")) {
                    AppData.reels.add(0, Reel("you", "now", "", uri))
                    reelAdapter.setActive(uri)
                    switchTab(Tab.REEL)
                } else {
                    AppData.posts.add(0, Post("you", "now", "", 0, 0, 0, uri))
                    postAdapter.notifyItemInserted(0)
                    switchTab(Tab.HOME)
                }
                feedRecycler.scrollToPosition(0)
            }
        }

        findViewById<TextView>(R.id.addButton).setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        findViewById<TextView>(R.id.reelAddButton).setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        setupBottomNav()

        if (intent.getStringExtra("open_tab") == "reel") {
            switchTab(Tab.REEL)
        }
    }

    private fun switchTab(tab: Tab) {
        currentTab = tab
        when (tab) {
            Tab.HOME -> {
                feedRecycler.adapter = postAdapter
                if (appBar.parent == null) {
                    appBarParent.addView(appBar, appBarIndex)
                }
                appBar.setExpanded(true, false)
                snapHelper?.attachToRecyclerView(null)
                findViewById<View>(R.id.reelAddButton).visibility = View.GONE
                selectNav(findViewById(R.id.navHome))
            }
            Tab.REEL -> {
                feedRecycler.adapter = reelAdapter
                if (appBar.parent != null) {
                    appBarIndex = appBarParent.indexOfChild(appBar)
                    appBarParent.removeView(appBar)
                }
                if (snapHelper == null) {
                    snapHelper = PagerSnapHelper()
                }
                snapHelper?.attachToRecyclerView(feedRecycler)
                findViewById<View>(R.id.reelAddButton).visibility = View.VISIBLE
                selectNav(findViewById(R.id.navReel))
            }
        }
    }

    private fun setupBottomNav() {
        val navHome = findViewById<View>(R.id.navHome)
        val navReel = findViewById<View>(R.id.navReel)
        val navRai = findViewById<View>(R.id.navRai)
        val navMessage = findViewById<View>(R.id.navMessage)
        val navProfile = findViewById<View>(R.id.navProfile)

        navItems = listOf(navHome, navReel, navRai, navMessage, navProfile)

        navHome.setOnClickListener { switchTab(Tab.HOME) }
        navReel.setOnClickListener { switchTab(Tab.REEL) }
        navRai.setOnClickListener {
            startActivity(android.content.Intent(this, RaiActivity::class.java))
            overridePendingTransition(0, 0)
        }
        navMessage.setOnClickListener {
            selectNav(navMessage)
            Toast.makeText(this, "Message — coming soon", Toast.LENGTH_SHORT).show()
        }
        navProfile.setOnClickListener {
            startActivity(android.content.Intent(this, ProfileActivity::class.java))
            overridePendingTransition(0, 0)
        }

        selectNav(navHome)
    }

    private fun selectNav(selected: View) {
        for (item in navItems) {
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
}

private fun String.toColorInt(): Int = android.graphics.Color.parseColor(this)
