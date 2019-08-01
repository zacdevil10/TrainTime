package uk.co.zac_h.traintime

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import uk.co.zac_h.traintime.ui.arrivals.ArrivalsFragment
import uk.co.zac_h.traintime.ui.lines.TrainLineFragment
import uk.co.zac_h.traintime.utils.LineColorHelper

class MainActivity : AppCompatActivity(), FragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) supportFragmentManager.beginTransaction().replace(R.id.frame_fragments_container_main, ArrivalsFragment.newInstance()).commit()

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                window.apply {
                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark)
                }
            }
        }
    }

    override fun swapFragment(lineName: String, stationName: String) {
        supportFragmentManager.beginTransaction().add(R.id.frame_fragments_container_main, TrainLineFragment.newInstance(lineName, stationName)).addToBackStack("").commit()
    }
}
