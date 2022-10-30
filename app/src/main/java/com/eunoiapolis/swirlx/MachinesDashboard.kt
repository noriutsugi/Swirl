package com.eunoiapolis.swirlx

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MachinesDashboard : AppCompatActivity(), java.io.Serializable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_machines_dashboard)

        // VAL
        val imgBtnProfile = findViewById<ImageButton>(R.id.imgBtnProfile)
        val imgBtnTxn = findViewById<ImageButton>(R.id.imgBtnTxn)


        val txtAvailM1 = findViewById<TextView>(R.id.txtAvailable)
        val txtAvailM2 = findViewById<TextView>(R.id.txtAvailable2)
        val txtAvailM3 = findViewById<TextView>(R.id.txtAvailable3)
        val txtAvailM4 = findViewById<TextView>(R.id.txtAvailable4)
        val txtBusyM1 = findViewById<TextView>(R.id.txtBusy)
        val txtBusyM2 = findViewById<TextView>(R.id.txtBusy2)
        val txtBusyM3 = findViewById<TextView>(R.id.txtBusy3)
        val txtBusyM4 = findViewById<TextView>(R.id.txtBusy4)
        val txtPowM1 = findViewById<TextView>(R.id.txtPowerOff)
        val txtPowM2 = findViewById<TextView>(R.id.txtPowerOff2)
        val txtPowM3 = findViewById<TextView>(R.id.txtPowerOff3)
        val txtPowM4 = findViewById<TextView>(R.id.txtPowerOff4)
        val txtMaiM1 = findViewById<TextView>(R.id.txtMaintenance)
        val txtMaiM2 = findViewById<TextView>(R.id.txtMaintenance2)
        val txtMaiM3 = findViewById<TextView>(R.id.txtMaintenance3)
        val txtMaiM4 = findViewById<TextView>(R.id.txtMaintenance4)


        val txtHOR1 = findViewById<TextView>(R.id.txtHOR)
        val txtHOR2 = findViewById<TextView>(R.id.txtHOR2)
        val txtHOR3 = findViewById<TextView>(R.id.txtHOR3)
        val txtHOR4 = findViewById<TextView>(R.id.txtHOR4)
        val btnSelM1 = findViewById<Button>(R.id.btnMachine1)
        val btnSelM2 = findViewById<Button>(R.id.btnMachine2)
        val btnSelM3 = findViewById<Button>(R.id.btnMachine3)
        val btnSelM4 = findViewById<Button>(R.id.btnMachine4)


//        txtAvailM1.visibility = View.GONE
//        txtAvailM2.visibility = View.GONE
//        txtAvailM3.visibility = View.GONE
//        txtAvailM4.visibility = View.GONE
//        txtBusyM1.visibility = View.GONE
//        txtBusyM2.visibility = View.GONE
//        txtBusyM3.visibility = View.GONE
//        txtBusyM4.visibility = View.GONE
//        txtPowM1.visibility = View.GONE
//        txtPowM2.visibility = View.GONE
//        txtPowM3.visibility = View.GONE
//        txtPowM4.visibility = View.GONE
//        txtMaiM1.visibility = View.GONE
//        txtMaiM2.visibility = View.GONE
//        txtMaiM3.visibility = View.GONE
//        txtMaiM4.visibility = View.GONE
//        btnSelM1.visibility = View.GONE
//        btnSelM2.visibility = View.GONE
//        btnSelM3.visibility = View.GONE
//        btnSelM4.visibility = View.GONE


        btnSelM1?.setOnClickListener() {
            proceedToTimer(Machine("Washing Machine 1", txtHOR1.text.toString(),"H4GF01", 1))
        }
        btnSelM2?.setOnClickListener() {
            proceedToTimer(Machine("Washing Machine 2", txtHOR1.text.toString(),"H4GF02", 1))
        }
        btnSelM3?.setOnClickListener() {
            proceedToTimer(Machine("Washing Machine 3", txtHOR1.text.toString(),"H4GF03", 1))
        }
        btnSelM4?.setOnClickListener() {
            proceedToTimer(Machine("Washing Machine 4", txtHOR1.text.toString(),"H4GF04", 1))
        }

        // BUTTONS
        imgBtnProfile?.setOnClickListener() {
            profileDashboard()
        }
        imgBtnTxn?.setOnClickListener() {
            transactionHistory()
        }

    }
    fun GET(Ref :String) :String{
        return ""
    }
    fun SET(Ref :String, Val :String) :Boolean {
        return true
    }
    fun profileDashboard() {
        val intent = Intent(this, ProfileDashboard::class.java)
        //intent.putExtra("the_mess", txtf.text.toString())
        startActivity(intent)
    }
    fun transactionHistory() {
        val intent = Intent(this, TransactionHistory::class.java)
        //intent.putExtra("the_mess", txtf.text.toString())
        startActivity(intent)
    }

    fun getMachine(machine: Machine, Mid: String) {
        var hidden: String? = null
        fun GET(Ref :String) :String?{
            Firebase.database.getReference(Ref).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    hidden = snapshot.getValue(String::class.java).toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(this@Machine, "Fail to get data.", Toast.LENGTH_SHORT).show()
                }
            })
            val tmp = hidden
            hidden = null
            return tmp
        }
        machine.machine_start = GET("/Cluster/$Mid/machine_start")?.toLong()
        machine.machine_end = GET("/Cluster/$Mid/machine_end")?.toLong()
        machine.washer_start = GET("/Cluster/$Mid/washer_start")?.toLong()
        machine.dryer_start = GET("/Cluster/$Mid/dryer_start")?.toLong()
        machine.washer_end = GET("/Cluster/$Mid/washer_end")?.toLong()
        machine.dryer_end = GET("/Cluster/$Mid/dryer_end")?.toLong()
    }


    public class Machine constructor(name: String, addr: String, id :String, status :Int): java.io.Serializable {
        val Name = name
        val Addr = addr
        var TxnID: String? = null
        val Id = id
            // <HC><L><MN> eg. H4GF01
        var Status = status
            // -2 -> No power
            // -1 -> In maintenance
            // 0 -> Connecting...
            // 1 -> Available
            // 2 -> Busy
            // 3 -> Booked!
        var User: String? = null
        var Amt: Int? = null
        var Wash: Int? = null
        var Dry: Int? = null

        var machine_start: Long? = null
        var machine_end: Long? = null
        var washer_start: Long? = null
        var washer_end: Long? = null
        var dryer_start: Long? = null
        var dryer_end: Long? = null
        var BUFFEWASH: Long? = 300000
        var BUFFERDRY: Long? = 300000
        val BUFFERDONE: Long? = 300000
        var wash_minutes: Long? = null
        var dry_minutes: Long? = null
        //
        var hidden: String? = null
        fun GET(Ref :String) :String?{
            Firebase.database.getReference(Ref).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    hidden = snapshot.getValue(String::class.java).toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(this@Machine, "Fail to get data.", Toast.LENGTH_SHORT).show()
                }
            })
            val tmp = hidden
            hidden = null
            return tmp
        }

        fun SetString(Ref:String, Val: String?) {
            Firebase.database.getReference(Ref).setValue(Val)
        }
        fun SetInt(Ref:String, Val: Long?) {
            Firebase.database.getReference(Ref).setValue(Val)
        }
        fun SetBool(Ref :String, Val :Boolean) {
            Firebase.database.getReference(Ref).setValue(Val)
        }

        fun startWash() {
            SetBool("/Cluster/$Id/wash_on", true)
        }
        fun startDry() {
            SetBool("/Cluster/$Id/dry_on", true)
        }
        fun cancelWash() {
            SetBool("/Cluster/$Id/wash_on", true)
        }
        fun cancelDry() {
            SetBool("/Cluster/$Id/dry_on", true)
        }
        fun startWater() {
            SetBool("/Cluster/$Id/water_on", true)
        }
        fun Done() {
            SetBool("/Cluster/$Id/done", true)
        }

        fun timeNow() :Long = System.currentTimeMillis()
        fun timeReadableFull(milliSec: Long): String{
            val simple: DateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z")
            return simple.format(Date(milliSec))
        }
        fun timeReadable(milliSec: Long): String{
            val simple: DateFormat = SimpleDateFormat("HH:mm")
            return simple.format(Date(milliSec))
        }
        fun setMachineOnce(tMWashing :Long, tMDrying :Long) {
            if (tMDrying == 0L) {
                BUFFERDRY = 0
            }
            machine_start = timeNow()
            washer_start = machine_start!! + BUFFEWASH!!
            washer_end = washer_start!! + (wash_minutes!! * 60 * 1000)
            dryer_start = washer_end!! + BUFFERDRY!!
            dryer_end = dryer_start!! + (dry_minutes!! * 60 * 1000)
            machine_end = dryer_end!! + BUFFERDONE!!


            SetString("/Cluster/$Id/user", User.toString())
            SetInt("/Cluster/$Id/machine_start", machine_start)
            SetInt("/Cluster/$Id/machine_end", machine_end)
            SetInt("/Cluster/$Id/washer_start", washer_start)
            SetInt("/Cluster/$Id/dryer_start", dryer_start)
            SetInt("/Cluster/$Id/washer_end", washer_end)
            SetInt("/Cluster/$Id/dryer_end", dryer_end)
        }

        fun updateMachineWashStart(tMWashing :Long = wash_minutes!!, tMDrying :Long = dry_minutes!!) {
            washer_start = timeNow()
            washer_end = washer_start!! + (wash_minutes!! * 60 * 1000)
            dryer_start = washer_end!! + BUFFERDRY!!
            dryer_end = dryer_start!! + (dry_minutes!! * 60 * 1000)
            machine_end = dryer_end!! + BUFFERDONE!!

            SetInt("/Cluster/$Id/machine_start", machine_start)
            SetInt("/Cluster/$Id/machine_end", machine_end)
            SetInt("/Cluster/$Id/washer_start", washer_start)
            SetInt("/Cluster/$Id/dryer_start", dryer_start)
            SetInt("/Cluster/$Id/washer_end", washer_end)
            SetInt("/Cluster/$Id/dryer_end", dryer_end)
        }

        fun updateMachineDryStart(tMWashing :Long = wash_minutes!!, tMDrying :Long = dry_minutes!!) {
            dryer_start = timeNow()
            dryer_end = dryer_start!! + (dry_minutes!! * 60 * 1000)
            machine_end = dryer_end!! + BUFFERDONE!!

            SetInt("/Cluster/$Id/machine_start", machine_start)
            SetInt("/Cluster/$Id/machine_end", machine_end)
            SetInt("/Cluster/$Id/washer_start", washer_start)
            SetInt("/Cluster/$Id/dryer_start", dryer_start)
            SetInt("/Cluster/$Id/washer_end", washer_end)
            SetInt("/Cluster/$Id/dryer_end", dryer_end)
        }

        fun updateMachineDryDone(tMWashing :Long = wash_minutes!!, tMDrying :Long = dry_minutes!!) {
            dryer_end = timeNow()
            machine_end = dryer_end!! + BUFFERDONE!!

            SetInt("/Cluster/$Id/machine_start", machine_start)
            SetInt("/Cluster/$Id/machine_end", machine_end)
            SetInt("/Cluster/$Id/washer_start", washer_start)
            SetInt("/Cluster/$Id/dryer_start", dryer_start)
            SetInt("/Cluster/$Id/washer_end", washer_end)
            SetInt("/Cluster/$Id/dryer_end", dryer_end)
        }

        fun updateMachineWashDone(tMWashing :Long = wash_minutes!!, tMDrying :Long = dry_minutes!!) {
            washer_end = timeNow()
            dryer_start = washer_end!! + BUFFERDRY!!
            dryer_end = dryer_start!! + (dry_minutes!! * 60 * 1000)
            machine_end = dryer_end!! + BUFFERDONE!!

            SetInt("/Cluster/$Id/machine_start", machine_start)
            SetInt("/Cluster/$Id/machine_end", machine_end)
            SetInt("/Cluster/$Id/washer_start", washer_start)
            SetInt("/Cluster/$Id/dryer_start", dryer_start)
            SetInt("/Cluster/$Id/washer_end", washer_end)
            SetInt("/Cluster/$Id/dryer_end", dryer_end)
        }

        fun updateMachineDone(tMWashing :Long = wash_minutes!!, tMDrying :Long = dry_minutes!!) {
            machine_end = timeNow()

            SetInt("/Cluster/$Id/machine_start", machine_start)
            SetInt("/Cluster/$Id/machine_end", machine_end)
            SetInt("/Cluster/$Id/washer_start", washer_start)
            SetInt("/Cluster/$Id/dryer_start", dryer_start)
            SetInt("/Cluster/$Id/washer_end", washer_end)
            SetInt("/Cluster/$Id/dryer_end", dryer_end)
        }
        fun clean() {
            SetString("/Cluster/$Id/", null)
        }
    }
    fun proceedToTimer(machine :Machine){
        machine.User = Firebase.auth.currentUser?.uid
        val intent = Intent(this, TimeSelection::class.java)
        intent.putExtra("machine_object", machine)
        startActivity(intent)
    }
}