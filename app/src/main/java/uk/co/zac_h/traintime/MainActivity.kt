package uk.co.zac_h.traintime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uk.co.zac_h.traintime.ui.arrivals.ArrivalsFragment
import uk.co.zac_h.traintime.ui.lines.TrainLineFragment

class MainActivity : AppCompatActivity(), FragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) supportFragmentManager.beginTransaction().replace(R.id.frame_fragments_container_main, ArrivalsFragment.newInstance()).commit()
    }

    override fun swapFragment(lineName: String, stationName: String) {
        supportFragmentManager.beginTransaction().add(R.id.frame_fragments_container_main, TrainLineFragment.newInstance(lineName, stationName)).addToBackStack("").commit()
    }
}
