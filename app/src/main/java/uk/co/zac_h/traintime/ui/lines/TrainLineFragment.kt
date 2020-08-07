package uk.co.zac_h.traintime.ui.lines

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.train_line_fragment.*
import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.StopPointModel
import uk.co.zac_h.traintime.ui.lines.adapter.StopPointsAdapter
import uk.co.zac_h.traintime.utils.LatLng
import uk.co.zac_h.traintime.utils.LineColorHelper
import uk.co.zac_h.traintime.utils.LocationUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class TrainLineFragment : Fragment(), ITrainLine.View {

    private lateinit var presenter: ITrainLine.Presenter

    private lateinit var stopPointsAdapter: StopPointsAdapter

    private var lineName: String? = null
    private var stationName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            lineName = getString("lineName")
            stationName = getString("stationName")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.train_line_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = TrainLinePresenterImpl(this, TrainLineInteractorImpl())

        activity?.actionBar?.apply {
            title = lineName ?: "Train Time"
            setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor(
                        LineColorHelper.getColour(
                            arguments?.getString(
                                "lineName"
                            ) ?: "default"
                        )
                    )
                )
            )
        }

        activity?.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor(
                LineColorHelper.getColour(
                    arguments?.getString("lineName") ?: "default"
                )
            )
        }

        val currentLocation = LocationUtils.getLocation(context)

        lineName?.let {
            presenter.getLineStopPoints(it.replace(" & ", "-"), currentLocation)
        }
    }

    override fun setLinePointsAdapter(lineStopPoints: ArrayList<StopPointModel>) {
        stopPointsAdapter = StopPointsAdapter(context, stationName ?: "Unknown station", lineStopPoints)

        line_stop_points_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = stopPointsAdapter
        }
    }

    override fun updateLinePointsAdapter() {
        stopPointsAdapter.notifyDataSetChanged()
    }

}
