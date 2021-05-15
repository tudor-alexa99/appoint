package com.bachelor.appoint.ui

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachelor.appoint.R
import com.bachelor.appoint.databinding.CardPlaceBinding
import com.bachelor.appoint.databinding.FragmentBusinessInformatioBinding
import com.bachelor.appoint.model.Business
import com.bachelor.appoint.utils.Constants

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val BUSINESS = "business_id"
private const val B_ID = "business_id"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BusinessInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BusinessInformatioFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var business: Business? = null
    private lateinit var binding: FragmentBusinessInformatioBinding
//    private var businessID: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            business = it.getParcelable(Constants.BUSINESS)
//            businessID = it.getString(B_ID)
//            param2 = it.getString(ARG_PARAM2)
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
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BusinessInformatioFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BusinessInformatioFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUSINESS, business)
//                    putString(B_ID, businessID)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}