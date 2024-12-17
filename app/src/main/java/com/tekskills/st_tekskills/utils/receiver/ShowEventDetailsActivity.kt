package com.tekskills.st_tekskills.utils.receiver

import android.os.Bundle
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tekskills.st_tekskills.R
import com.tekskills.st_tekskills.data.db.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Created by RK
 */
class ShowEventDetailsActivity() : AppCompatActivity() {
    private var mToolbar: Toolbar? = null
    var expListView: ExpandableListView? = null
    var db: DatabaseHelper? = null
    var expandableListAdapter: ExpandableListAdapter? = null
    private var lastExpandedPosition = -1
    var eventsOnDate: TextView? = null
    var listDataHeader: List<String>? = null
    var listDataChild: HashMap<String, List<String>>? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_detail_activity)

        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        val bar: ActionBar? = supportActionBar
        bar!!.elevation = 0F
        bar!!.setDisplayHomeAsUpEnabled(true)


        val intent = intent
        Date = intent.getStringExtra("date")
        eventsOnDate = findViewById<View>(R.id.events_on_date) as TextView

        val items1 = Date!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val D1 = items1[0]
        val M1 = items1[1]
        val Y1 = items1[2]
        eventsOnDate!!.text = D1 + " " + M1.substring(0, 3) + " " + Y1
        expListView = findViewById<View>(R.id.events_list) as ExpandableListView
        prepareListData(Date)
        //expandableListAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView!!.setAdapter(expandableListAdapter)

        expListView!!.setOnGroupExpandListener(OnGroupExpandListener { groupPosition ->
            if ((lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition)
            ) {
                expListView!!.collapseGroup(lastExpandedPosition)
            }
            lastExpandedPosition = groupPosition
        })
    }

    private fun prepareListData(date: String?) {
        listDataHeader = ArrayList()
        listDataChild = HashMap()
        val myList: MutableList<String> = ArrayList()
        myList.add("One")
        try {
            val items1 = date!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val d1 = items1[0]
            val m1 = items1[1]
            val y1 = items1[2]
            val d = d1.toInt()
            val y = y1.toInt()
            val cal = Calendar.getInstance()
            try {
                cal.time = SimpleDateFormat("MMM").parse(m1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val monthInt = cal[Calendar.MONTH] + 1
            db = DatabaseHelper(this@ShowEventDetailsActivity)
            if (d < 10) {
                numberDate = "0$d-$monthInt-$y"
                listDataHeader = db!!.getRecepients("0$d-$monthInt-$y")
            } else {
                numberDate = "$d-$monthInt-$y"
                listDataHeader = db!!.getRecepients("$d-$monthInt-$y")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (listDataHeader != null) {
            for (i in listDataHeader!!.indices) {
                listDataChild!![listDataHeader!![i]] = myList
            }
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.home -> {
//                this.finish()
//                return true
//            }
//
//            else -> {}
//        }
//        return super.onOptionsItemSelected(item)
//    }

    companion object {
        var numberDate: String? = null
        var Date: String? = null
    }
}
