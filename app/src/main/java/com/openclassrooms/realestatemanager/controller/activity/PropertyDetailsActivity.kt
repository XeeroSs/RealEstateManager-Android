package com.openclassrooms.realestatemanager.controller.activity

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertyImageRecyclerView
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_property_management.*
import kotlinx.android.synthetic.main.property_details_content.*

class PropertyDetailsActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_property_details
    private lateinit var propertyId: String
    private lateinit var mainViewModel: MainViewModel
    lateinit var adapter: PropertyImageRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureViewModel()
        getPropertyId()
    }

    private fun getPropertyId() {
        propertyId = intent.getStringExtra(PROPERTY_ID)
        getProperty()
    }

    private fun getProperty() {
        mainViewModel.getProperty(propertyId).observe(this, Observer { property ->
            if (property == null) finish()
            configureUI(property!!)
        })
    }

    private fun configureUI(property: PropertyModel) {
        fun textViewCapacity(textView: TextView, text: String) {
            textView.text = "${textView.text} $text"
        }
        textViewCapacity(textView_author_property, property.realEstateAgentProperty)
        textViewCapacity(textView_bathrooms_property, property.bathroomsNumberProperty.toString())
        textViewCapacity(textView_bedrooms_property, property.bedroomsNumberProperty.toString())
        textViewCapacity(textView_description_property_fragment, property.descriptionProperty)
        textViewCapacity(textView_entry_sale_property, Utils.todayDate)
        textViewCapacity(textView_price_property, property.priceDollarProperty.toString() + "$")
        textViewCapacity(textView_rooms_property, property.roomsNumberProperty.toString())
        textViewCapacity(textView_sale_date_property, property.saleDateProperty)
        textViewCapacity(textView_status_property, if (property.statusProperty) getString(R.string.available) else getString(R.string.not_available))
        textViewCapacity(textView_type_property, property.typeProperty)
        textViewCapacity(textView_surface_property, property.surfaceProperty.toString() + "m")

        //configureRecyclerView(property.photosPropertyJSON)
    }

    private fun configureRecyclerView(photosPropertyJSON: String) {
        recyclerView_photos_property_fragment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = PropertyImageRecyclerView(this, Utils.deserializeArrayList(photosPropertyJSON))
        recyclerView_photos_property_fragment.adapter = adapter
    }

    private fun configureViewModel() {
        val viewModelProvider = Injection.provideViewModelFactory(this)
        mainViewModel = ViewModelProviders.of(this, viewModelProvider).get(MainViewModel::class.java)
    }
}