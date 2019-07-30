package uk.co.zac_h.traintime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uk.co.zac_h.traintime.ui.arrivals.ArrivalsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) supportFragmentManager.beginTransaction().replace(R.id.frame_fragments_container_main, ArrivalsFragment.newInstance()).commit()
    }
}
