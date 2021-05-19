package com.bachelor.appoint

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bachelor.appoint.adapters.BusinessListAdapter
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityBusinessBinding
import com.bachelor.appoint.databinding.FragmentAddBusinessAlertBinding
import com.bachelor.appoint.helpers.StatisticsHelper
import com.bachelor.appoint.model.Business
import com.bachelor.appoint.ui.AppointmentsListFragment
import com.bachelor.appoint.ui.BusinessInformatioFragment
import com.bachelor.appoint.utils.Constants
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BusinessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusinessBinding
    private lateinit var businessAdapter: BusinessListAdapter
    private lateinit var businessList: ArrayList<Business>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind the activity to the view
        binding = ActivityBusinessBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        // Retrieve the list
        getBusinessList()

        // Add business button
        binding.btnAddBusiness.setOnClickListener {
            print("Button clicked")
            Log.d("Add business button clicked", "Business Activity")
            openAlertDialog()
        }

        itemTouchHelper.attachToRecyclerView(binding.rvBusiness)

    }


    override fun onResume() {
        super.onResume()
        getBusinessList()
    }

    // ADD

    fun openAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add new business")
        val alertBinding = FragmentAddBusinessAlertBinding.inflate(layoutInflater)
        val alertView = alertBinding.root
        builder.setView(alertView)

        // initialise the spinner
        val spinner = alertBinding.spBusinessTypes

        // Create the adaptor and set its values
        ArrayAdapter.createFromResource(
            this,
            R.array.business_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val name: String = alertBinding.etBusinessName.text.toString()
            val address: String = alertBinding.etBusinessAddress.text.toString()
            val phoneNumber: String = alertBinding.etPhone.text.toString()
            val type: String = spinner.selectedItem.toString()
            val estimatedSurface: Int = alertBinding.etEstimatedSurface.text.toString().toInt()
            val maxSeatsNumber: Int = alertBinding.etSeatsNumber.text.toString().toInt()
            val estimatedTime: String = alertBinding.etTimeSpent.text.toString()
            val openSpace: Boolean = alertBinding.swiOpenSpace.isChecked

            val estimatedRisk = StatisticsHelper().computeEstimatedRisk(
                type,
                estimatedSurface,
                maxSeatsNumber,
                estimatedTime,
                openSpace
            )

            FirestoreClass().addBusiness(
                this,
                name,
                address,
                phoneNumber,
                type,
                estimatedSurface,
                maxSeatsNumber,
                openSpace,
                estimatedRisk,
                estimatedTime
            )
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
            Toast.makeText(
                applicationContext,
                android.R.string.cancel, Toast.LENGTH_SHORT
            ).show()
        }

        builder.show()
    }

    fun addBusinessSuccess() {
        Toast.makeText(
            applicationContext,
            "Business added!", Toast.LENGTH_LONG
        ).show()
    }

    // READ

    private fun getBusinessList() {
        FirestoreClass().retrieveBusinesses(this)
    }

    fun successRetrieveBusinesses(businessList: ArrayList<Business>) {
        this.businessList = businessList

        // Set the recycler view as well
        businessAdapter = BusinessListAdapter(this, businessList)

        var recyclerView = binding.rvBusiness
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = businessAdapter

    }

    // Used for focusing on the list when scrolling vertically
    private fun scrollToPosition(direction: Int) {
        // direction: -1 -> left, 1 -> right

//        val eventId = "someId"
//        val position: Int = businessAdapter.getItemPosition(eventId)
//        if (position >= 0) {
//            binding.rvBusiness.scrollToPosition(position)
//        }

        if (direction == 1) {
            businessAdapter.nextPosition()
            binding.rvBusiness.scrollToPosition(businessAdapter.getCurrentPosition())
        }
        if (direction == -1) {
            businessAdapter.previousPosition()
            binding.rvBusiness.scrollToPosition(businessAdapter.getCurrentPosition())
        }
    }

    var itemTouchHelper = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder, target: ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                // move item in `fromPos` to `toPos` in adapter.
                return true // true if moved, false otherwise
            }

            // Swipe up on the business card to reload the appointments associated with it. An appointments list will reload and you will be able so swipe left or right on it `
            // 2 fragments. On swipe up, load the business information, on swipe down load the appointments list

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                Log.d("Direction", direction.toString())
                businessAdapter.notifyItemChanged(viewHolder.adapterPosition);

                // Select the current business from the card that was swiped on
                var business: Business = businessAdapter.getItem(viewHolder.adapterPosition)

                // Create a bundle to send the business param to the fragment
                val bundle = Bundle()
                bundle.putParcelable(Constants.BUSINESS, business)

                // used for switching between fragment in the same frame
                val manager = supportFragmentManager

                if (direction == 1) {   // on Swipe UP
                    // create the new fragment and send the parameters
                    val infoFragment = BusinessInformatioFragment()
                    infoFragment.arguments = bundle

                    binding.ellipse3.animate().alpha(0.0f)
                    binding.ellipse4.visibility  = View.VISIBLE
                    binding.ellipse4.animate().alpha(1.0f)
//                    binding.ellipse3.visibility  = View.GONE


                    // initiate the transaction that replaces the fragments
                    val transaction = manager.beginTransaction().setCustomAnimations(
                        android.R.animator.fade_in,
                        android.R.animator.fade_out
                    )
                    transaction.replace(R.id.fr_business_info, infoFragment)
                    transaction.commit()

                } else if (direction == 2) {    // on Swipe DOWN
                    // create the new fragment and send the parameters
                    val appListFragment = AppointmentsListFragment()
                    appListFragment.arguments = bundle

                    binding.ellipse4.animate().alpha(0.0f)
//                    binding.ellipse4.visibility  = View.GONE
                    binding.ellipse3.animate().alpha(1.0f)
                    binding.ellipse3.visibility  = View.VISIBLE


                    // initiate the transaction that replaces the fragments
                    val transaction = manager.beginTransaction().setCustomAnimations(
                        android.R.animator.fade_in,
                        android.R.animator.fade_out
                    )
                    transaction.replace(R.id.fr_business_info, appListFragment)
                    transaction.commit()
                }
            }
        })
}
