package com.bachelor.appoint.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachelor.appoint.R
import com.bachelor.appoint.databinding.FragmentBusinessInformatioBinding
import com.bachelor.appoint.model.Business
import com.bachelor.appoint.utils.Constants
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private const val BUSINESS = "business"


class BusinessInformatioFragment : Fragment() {

    private var business: Business? = null
    private lateinit var binding: FragmentBusinessInformatioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            business = it.getParcelable(Constants.BUSINESS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (business != null) {
            Log.d("Inside fragment", business!!.name)
        }
        return inflater.inflate(R.layout.fragment_business_informatio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBusinessInformatioBinding.bind(view)
        if (business != null)
            setFragmentView(business!!)
        else
            setEmptyFragmentView()
    }

    private fun setEmptyFragmentView() {
        binding.tvCompanyName.text =
            "Swipe up for business details, swipe down for appointments list"
        binding.clBDetailsBody.visibility = View.GONE
    }

    private fun setFragmentView(currentBusiness: Business) {
        // set the name
        binding.tvCompanyName.text = currentBusiness.name

        // set the risk progressBar with an animation
        business?.let { //binding.statsProgressbar.setProgress(it.estimatedRisk, true)
            if (business!!.estimatedRisk < 33)
                ObjectAnimator.ofInt(binding.statsProgressbarLow, "progress", it.estimatedRisk)
                    .setDuration(400)
                    .start()
            else if (business!!.estimatedRisk < 66)
                ObjectAnimator.ofInt(binding.statsProgressbarMedium, "progress", it.estimatedRisk)
                    .setDuration(900)
                    .start()
            else
                ObjectAnimator.ofInt(binding.statsProgressbarHigh, "progress", it.estimatedRisk)
                    .setDuration(1200)
                    .start()
        }

        // set the number inside the pie chart
        binding.tvScore.text = business!!.estimatedRisk.toString()

        // set the business type
        binding.tvBiTypeVal.text = business!!.type

        // set the seats number
        binding.tvBiSeatsVal.text = business!!.seatsNumber.toString()

        // compute and round the average area for each user
        binding.tvBiUserAreaVal.text =
            ("%.1f".format((business!!.estimatedSurface.toFloat() / business!!.seatsNumber))) + " m²"

        // format and set the average time
        val time = LocalTime.parse(business!!.averageTimeSpent, DateTimeFormatter.ofPattern("H:mm"))
        val timeValue = time.hour.toString() + "hr " + time.minute.toString() + "min"
        binding.tvBiAvgTimeSpentVal.text = timeValue

        // set the address
        binding.tvBiAddressVal.text = business!!.location

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(business: Business) =
            BusinessInformatioFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUSINESS, business)
                }
            }
    }
}