package com.eunoiapolis.swirlx

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.eunoiapolis.swirlx.MachinesDashboard.Machine
import com.google.firebase.auth.FirebaseAuth
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

open class TimeSelection : AppCompatActivity(), PaymentResultListener {
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_selection)
        var machine = intent.extras?.getSerializable("machine_object") as? Machine
        //findViewById<TextView>(R.id.txtID).text = machine!!.Id

        //VAL
        findViewById<TextView>(R.id.txtHORName).text = machine!!.Addr
        findViewById<TextView>(R.id.txtMName).text = machine!!.Name

        findViewById<Button>(R.id.btnAddShirt)?.setOnClickListener() {
            dial(findViewById(R.id.txtShirtNumber), "max", 1, 25)
        }
        findViewById<Button>(R.id.btnRemShirt)?.setOnClickListener() {
            dial(findViewById(R.id.txtShirtNumber), "min", -1, 0)
        }
        findViewById<Button>(R.id.btnAddShort)?.setOnClickListener() {
            dial(findViewById(R.id.txtShortNumber), "max", 1, 25)
        }
        findViewById<Button>(R.id.btnRemShort)?.setOnClickListener() {
            dial(findViewById(R.id.txtShortNumber), "min", -1, 0)
        }
        findViewById<Button>(R.id.btnAddSheet)?.setOnClickListener() {
            dial(findViewById(R.id.txtSheetNumber), "max", 1, 25)
        }
        findViewById<Button>(R.id.btnRemSheet)?.setOnClickListener() {
            dial(findViewById(R.id.txtSheetNumber), "min", -1, 0)
        }
        findViewById<Button>(R.id.btnIncWash)?.setOnClickListener() {
            dial2(findViewById(R.id.txtWashMinutes), "max", 5, 40)
        }
        findViewById<Button>(R.id.btnDecWash)?.setOnClickListener() {
            dial2(findViewById(R.id.txtWashMinutes), "min", -5, 5)
        }
        findViewById<Button>(R.id.btnIncDry)?.setOnClickListener() {
            dial2(findViewById(R.id.txtDryMinutes), "max", 1, 5)
        }
        findViewById<Button>(R.id.btnDecDry)?.setOnClickListener() {
            dial2(findViewById(R.id.txtDryMinutes), "min", -1, 0)
        }
        findViewById<Button>(R.id.btnPay)?.setOnClickListener() {
            payment()
        }

    }
    fun payment() {
        //var machine = intent.extras?.getSerializable("machine_object") as? Machine
        val email = auth.currentUser?.email.toString()
        val amt = (patAmt2() * 100).toString()

        val activity: Activity = this
        val co = Checkout()
        co.setKeyID("rzp_test_adXtkXtgAKFjjP")
        try {
            val options = JSONObject()
            options.put("name","Swirl")
            options.put("description","Never worry about washing your favorate outfit again!")
            //You can omit the image option to fetch the image from dashboard
            //options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            //options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount",amt)//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email",email)
            //prefill.put("contact","9876543210")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    override fun onPaymentSuccess(s: String?) {
        var machine = intent.extras?.getSerializable("machine_object") as? Machine
        machine!!.Amt = patAmt2()
        machine!!.wash_minutes = findViewById<TextView>(R.id.txtWashMinutes).text.toString().toLong()
        machine!!.dry_minutes = findViewById<TextView>(R.id.txtDryMinutes).text.toString().toLong()
        machine!!.setMachineOnce(machine!!.wash_minutes!!, machine!!.dry_minutes!!)
        machine?.TxnID = s
        proceedToBookedSlot(machine!!)
        Toast.makeText(this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show()
    }
    override fun onPaymentError(p0: Int, s: String?) {
        Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
    }

    fun loadCalc(): Double = (findViewById<TextView>(R.id.txtShirtNumber).text.toString().toFloat()*0.25) + (findViewById<TextView>(R.id.txtShortNumber).text.toString().toFloat()*0.5) + (findViewById<TextView>(R.id.txtSheetNumber).text.toString().toFloat()*0.7)
    fun loadTime(): Int = (20*(1+(loadCalc()/10.1))).toInt()
    fun patAmt(): Int = (loadTime() + findViewById<TextView>(R.id.txtDryMinutes).text.toString().toInt()) * 2
    fun patAmt2(): Int = (findViewById<TextView>(R.id.txtWashMinutes).text.toString().toInt() + findViewById<TextView>(R.id.txtDryMinutes).text.toString().toInt()) * 2
    fun loadCheck(): Boolean {
        if (loadCalc() < 9.4) {
            return true
        }
        return false
    }
    fun dial(textView: TextView, type: String, inc: Int, till: Int) {
        if (type == "max"  && loadCheck()) {
            textView.text = if (textView.text.toString().toInt() < till)
                (textView.text.toString().toInt() + inc).toString() else till.toString()
            findViewById<TextView>(R.id.txtWashMinutes).text = loadTime().toString()
            findViewById<Button>(R.id.btnPay).text = "Proceed and pay Rs." + patAmt().toString()
        } else if (type == "min"){
            textView.text = if (textView.text.toString().toInt() > till)
                    (textView.text.toString().toInt() + inc).toString() else till.toString()
            findViewById<TextView>(R.id.txtWashMinutes).text = loadTime().toString()
            findViewById<Button>(R.id.btnPay).text = "Proceed and pay Rs." + patAmt().toString()
        }

    }
    fun dial2(textView: TextView, type: String, inc: Int, till: Int) {
        if (type == "max") {
            textView.text = if (textView.text.toString().toInt() < till)
                (textView.text.toString().toInt() + inc).toString() else till.toString()
            findViewById<Button>(R.id.btnPay).text = "Proceed and pay Rs." + patAmt2().toString()
        } else if (type == "min"){
            textView.text = if (textView.text.toString().toInt() > till)
                (textView.text.toString().toInt() + inc).toString() else till.toString()
            findViewById<Button>(R.id.btnPay).text = "Proceed and pay Rs." + patAmt2().toString()
        }
    }
    fun proceedToBookedSlot(machine :Machine){
        val intent = Intent(this, BookedSlot::class.java)
        intent.putExtra("machine_object", machine)
        startActivity(intent)
    }
}