package com.nkle.poet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //adding navigation drawer to the main activity
        var drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout);
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close)
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //adding trending cards recycler view demo data
        //doit: wrap images with other layout and give padding to it to give the red colors
        var trendingCards = mutableListOf(
                TrendingCard("imagePath","Jane Doe"),
                TrendingCard("imagePath","John Doe"),
                TrendingCard("imagePath","Kidus Yoseph"),
                TrendingCard("imagePath","Natnael Abay"),
                TrendingCard("imagePath","Liyu Doe"),
        )

        var mainTrendingRecyclerView : RecyclerView = findViewById(R.id.rvTrendingCards);
        var mainTrendingCardAdapter = TrendingCardAdapter(trendingCards);
        mainTrendingRecyclerView.adapter = mainTrendingCardAdapter;
        mainTrendingRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //adding post_cards with swipable view demo data
        var posts = mutableListOf(
                Post("James Carter Brown","Roots in everyone heart","Content"),
                Post("James Carter Brown","Roots in everyone heart","Content"),
                Post("James Carter Brown","Roots in everyone heart","Content")
        )

        val postCards : ViewPager2 = findViewById(R.id.post_cards)
        val mainPostSwipableAdapter = PostCardAdapter(posts)
        postCards.adapter = mainPostSwipableAdapter


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}