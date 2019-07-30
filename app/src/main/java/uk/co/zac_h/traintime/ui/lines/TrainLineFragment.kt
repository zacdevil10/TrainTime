package uk.co.zac_h.traintime.ui.lines

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.utils.LineColorHelper

class TrainLineFragment : Fragment() {

    companion object {
        fun newInstance(lineName: String, stationName: String) = TrainLineFragment().apply {
            arguments = Bundle().apply {
                putString("lineName", lineName)
                putString("stationName", stationName)
            }
        }
    }

    private lateinit var viewModel: TrainLineViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.train_line_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Toolbar>(R.id.line_toolbar).apply {
            title = arguments?.getString("lineName") ?: "Train Time"
            setBackgroundColor(Color.parseColor(LineColorHelper.getColour(arguments?.getString("lineName") ?: "default")))
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TrainLineViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
