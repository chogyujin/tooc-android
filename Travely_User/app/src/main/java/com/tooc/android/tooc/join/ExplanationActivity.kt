package com.tooc.android.tooc.join

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.tooc.android.tooc.R
import com.tooc.android.tooc.adapter.ExplanationPagerAdapter
import com.tooc.android.tooc.network.NetworkService
import kotlinx.android.synthetic.main.activity_explanation.*
import org.jetbrains.anko.toast

class ExplanationActivity : AppCompatActivity() {

    lateinit var networkService : NetworkService
    lateinit var explanationPagerAdapter : ExplanationPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explanation)

        init()
    }

    private fun init() {
        SharedPreferencesController.instance!!.setPrefData("Explanation", true)
        startExplanation()
    }

    private fun startExplanation(){
        explanationPagerAdapter = ExplanationPagerAdapter(supportFragmentManager)
        explanationPagerAdapter.addItem(Explanation1Fragment.newInstance("1"))
        explanationPagerAdapter.addItem(Explanation2Fragment.newInstance("2"))
        explanationPagerAdapter.addItem(Explanation3Fragment.newInstance("3"))
        explanationPagerAdapter.addItem(Explanation4Fragment.newInstance("4"))
        explanationPagerAdapter.addItem(WelcomeFragment.newInstance("5"))

        vp_explanation.offscreenPageLimit = 5
        indicator_explanation.setItemMargin(10)
        indicator_explanation.setAnimDuration(300)
        indicator_explanation.createDotPanel(explanationPagerAdapter.count, R.drawable.indicator_empty_circle, R.drawable.indicator_full_circle)

        vp_explanation.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                indicator_explanation.selectDot(position)
            }
        })
        vp_explanation.currentItem = 0
        vp_explanation.adapter = explanationPagerAdapter
    }
}

