package com.aditya.placesinfo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // List the available PlacesLists
        val listView = ListView(this).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it.adapter = PlacesListAdapter(this, PlacesList.values())
            it.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
                val PlacesList = parent.adapter.getItem(position) as? PlacesList
                PlacesList?.let {
                    startActivity(Intent(this, PlacesList.activity))
                }
            }
        }
        setContentView(listView)
    }

    private class PlacesListAdapter(context: Context, PlacesLists: Array<PlacesList>) :
        ArrayAdapter<PlacesList>(context, R.layout.item_demo, PlacesLists) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val PlacesListView = (convertView as? PlacesListItemView) ?: PlacesListItemView(context)
            return PlacesListView.also {
                val PlacesList = getItem(position)
                it.title.setText(PlacesList?.title ?: 0)
                it.description.setText(PlacesList?.description ?: 0)
            }
        }
    }

    private class PlacesListItemView(context: Context) : LinearLayout(context) {

        val title: TextView by lazy { findViewById(R.id.textViewTitle) }

        val description: TextView by lazy { findViewById(R.id.textViewDescription) }

        init {
            LayoutInflater.from(context)
                .inflate(R.layout.item_demo, this)
        }
    }
}