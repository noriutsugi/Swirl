package com.eunoiapolis.swirlx

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class BookedSlot : AppCompatActivity() {
    lateinit var tBuffready: countDown
    lateinit var tWasherTime: countDown
    lateinit var tDryReady: countDown
    lateinit var tDryerTime: countDown
    lateinit var tDone: countDown
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booked_slot)

        var machine = intent.extras?.getSerializable("machine_object") as? MachinesDashboard.Machine



        val btnWaterNow = findViewById<Button>(R.id.btnFillWater)
        val btnStartWash = findViewById<Button>(R.id.btnStartWash)
        val btnCancelWash = findViewById<Button>(R.id.btnCancelWash)
        val btnCancelDry = findViewById<Button>(R.id.btnCancelDry)
        val btnStartDry = findViewById<Button>(R.id.btnStartDry)
        val btnDoneNow = findViewById<Button>(R.id.btnDone)

        //one.cancel()

        fun addtimer() {
            //var machine = intent.extras?.getSerializable("machine_object") as? MachinesDashboard.Machine
            tBuffready = countDown(findViewById(R.id.txtWasherBuffer),  machine?.washer_start!! - machine?.timeNow()!!, "to put in clothes and fill water")
            tWasherTime = countDown(findViewById(R.id.txtWasherDone), machine?.washer_end!! - machine?.washer_start!!, "till washing is done")
            tDryReady = countDown(findViewById(R.id.txtDryerBuffer), machine?.dryer_start!! - machine?.washer_end!!, "to start the dryer")
            tDryerTime = countDown(findViewById(R.id.txtDryerDone), machine?.dryer_end!! - machine?.dryer_start!!, "till drying is done")
            tDone = countDown(findViewById(R.id.txtTimerDone), machine?.machine_end!! - machine?.dryer_end!!, "to take out washed clothes and close the slider")
            tBuffready.start()
            tWasherTime.start()
            tDryReady.start()
            tDryerTime.start()
            tDone.start()
        }
        fun removetimer() {
            tBuffready.cancel()
            tWasherTime.cancel()
            tDryReady.cancel()
            tDryerTime.cancel()
            tDone.cancel()
        }

        findViewById<TextView>(R.id.txtPayDone).text = "Started at " + machine?.timeReadable(machine.machine_start!!)
        findViewById<TextView>(R.id.txtWasherStart).text = "till " + machine?.timeReadable(machine.washer_end!!)
        findViewById<TextView>(R.id.txtDryerStart).text = "till " + machine?.timeReadable(machine.dryer_end!!)
        findViewById<TextView>(R.id.txtEnd).text = "till " + machine?.timeReadable(machine.machine_end!!)

        addtimer()

        findViewById<TextView>(R.id.txtWasherBuffer).visibility = View.VISIBLE
        findViewById<TextView>(R.id.txtWasherDone).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.txtDryerBuffer).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.txtDryerDone).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.txtTimerDone).visibility = View.INVISIBLE

        selThisMonoButton(btnWaterNow, btnStartWash, btnCancelWash, btnStartDry, btnCancelDry, btnDoneNow)

        btnWaterNow?.setOnClickListener() {
            findViewById<TextView>(R.id.txtWasherBuffer).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtWasherDone).visibility = View.VISIBLE
            findViewById<TextView>(R.id.txtDryerBuffer).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtDryerDone).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtTimerDone).visibility = View.INVISIBLE

            selThisDuoButton(btnStartWash, btnCancelWash, btnStartDry, btnCancelDry, btnDoneNow, btnWaterNow)

            machine?.updateMachineWashStart()
            machine?.startWater()
            removetimer()
            addtimer()
            findViewById<TextView>(R.id.txtPayDone).text = "Started at " + machine?.timeReadable(machine.machine_start!!)
            findViewById<TextView>(R.id.txtWasherStart).text = "till " + machine?.timeReadable(machine.washer_end!!)
            findViewById<TextView>(R.id.txtDryerStart).text = "till " + machine?.timeReadable(machine.dryer_end!!)
            findViewById<TextView>(R.id.txtEnd).text = "till " + machine?.timeReadable(machine.machine_end!!)
        }
        btnStartWash?.setOnClickListener() {
            machine?.startWash()
            selThisMonoButton(btnCancelWash, btnWaterNow, btnStartWash, btnStartDry, btnCancelDry, btnDoneNow)
        }
        btnCancelWash?.setOnClickListener() {

            findViewById<TextView>(R.id.txtWasherBuffer).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtWasherDone).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtDryerBuffer).visibility = View.VISIBLE
            findViewById<TextView>(R.id.txtDryerDone).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtTimerDone).visibility = View.INVISIBLE

            selThisDuoButton(btnStartDry, btnCancelDry, btnStartWash, btnCancelWash, btnDoneNow, btnWaterNow)

            machine?.updateMachineWashDone()
            machine?.cancelWash()
            removetimer()
            addtimer()
            findViewById<TextView>(R.id.txtPayDone).text = "Started at " + machine?.timeReadable(machine.machine_start!!)
            findViewById<TextView>(R.id.txtWasherStart).text = "till " + machine?.timeReadable(machine.washer_end!!)
            findViewById<TextView>(R.id.txtDryerStart).text = "till " + machine?.timeReadable(machine.dryer_end!!)
            findViewById<TextView>(R.id.txtEnd).text = "till " + machine?.timeReadable(machine.machine_end!!)
        }
        btnStartDry?.setOnClickListener() {

            findViewById<TextView>(R.id.txtWasherBuffer).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtWasherDone).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtDryerBuffer).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtDryerDone).visibility = View.VISIBLE
            findViewById<TextView>(R.id.txtTimerDone).visibility = View.INVISIBLE

            selThisMonoButton(btnCancelDry, btnCancelWash, btnWaterNow, btnStartWash, btnStartDry, btnDoneNow)

            machine?.updateMachineDryStart()
            machine?.startDry()
            removetimer()
            addtimer()
            findViewById<TextView>(R.id.txtPayDone).text = "Started at " + machine?.timeReadable(machine.machine_start!!)
            findViewById<TextView>(R.id.txtWasherStart).text = "till " + machine?.timeReadable(machine.washer_end!!)
            findViewById<TextView>(R.id.txtDryerStart).text = "till " + machine?.timeReadable(machine.dryer_end!!)
            findViewById<TextView>(R.id.txtEnd).text = "till " + machine?.timeReadable(machine.machine_end!!)
        }
        btnCancelDry?.setOnClickListener() {

            findViewById<TextView>(R.id.txtWasherBuffer).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtWasherDone).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtDryerBuffer).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtDryerDone).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtTimerDone).visibility = View.VISIBLE

            selThisMonoButton(btnDoneNow, btnCancelDry, btnCancelWash, btnWaterNow, btnStartWash, btnStartDry)

            machine?.updateMachineDryDone()
            machine?.cancelDry()
            removetimer()
            addtimer()
            findViewById<TextView>(R.id.txtPayDone).text = "Started at " + machine?.timeReadable(machine.machine_start!!)
            findViewById<TextView>(R.id.txtWasherStart).text = "till " + machine?.timeReadable(machine.washer_end!!)
            findViewById<TextView>(R.id.txtDryerStart).text = "till " + machine?.timeReadable(machine.dryer_end!!)
            findViewById<TextView>(R.id.txtEnd).text = "till " + machine?.timeReadable(machine.machine_end!!)
        }
        btnDoneNow?.setOnClickListener() {

            findViewById<TextView>(R.id.txtWasherBuffer).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtWasherDone).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtDryerBuffer).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtDryerDone).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.txtTimerDone).visibility = View.INVISIBLE

            machine?.updateMachineDone()
            machine?.Done()
            machine?.clean()
            removetimer()
            addtimer()
            findViewById<TextView>(R.id.txtPayDone).text = "Started at " + machine?.timeReadable(machine.machine_start!!)
            findViewById<TextView>(R.id.txtWasherStart).text = "till " + machine?.timeReadable(machine.washer_end!!)
            findViewById<TextView>(R.id.txtDryerStart).text = "till " + machine?.timeReadable(machine.dryer_end!!)
            findViewById<TextView>(R.id.txtEnd).text = "till " + machine?.timeReadable(machine.machine_end!!)
        }

    }




    class countDown(textView: TextView, end: Long, msg: String) {
        var timer = object : CountDownTimer(end, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                textView.text = "You have " + TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished).toString() + "m " + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - (60 * TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))).toString() + "s " + msg + "."
            }
            override fun onFinish() {
                //textView.setText("Time's finished!")
                textView.visibility = View.INVISIBLE
            }
        }
        fun start() {
            timer.start()
        }
        fun cancel () {
            timer.cancel()
        }
        fun timeReadable(milliSec: Long): String{
            val simple: DateFormat = SimpleDateFormat("HH:mm:ss")
            return simple.format(Date(milliSec))
        }
    }
    fun selThisMonoButton(button: Button, button2: Button, button3: Button, button4: Button, button5: Button, button6: Button) {
        val TRANS = 0.5f
        button.isClickable = true
        button.alpha = 1f

        button2.isClickable = false
        button3.isClickable = false
        button4.isClickable = false
        button5.isClickable = false
        button5.isClickable = false
        button6.isClickable = false
        button2.alpha = TRANS
        button3.alpha = TRANS
        button4.alpha = TRANS
        button5.alpha = TRANS
        button5.alpha = TRANS
        button6.alpha = TRANS
    }
    fun selThisDuoButton(button: Button, button2: Button, button3: Button, button4: Button, button5: Button, button6: Button) {
        val TRANS = 0.5f
        button.isClickable = true
        button.alpha = 1f
        button2.isClickable = true
        button2.alpha = 1f

        button3.isClickable = false
        button4.isClickable = false
        button5.isClickable = false
        button5.isClickable = false
        button6.isClickable = false
        button3.alpha = TRANS
        button4.alpha = TRANS
        button5.alpha = TRANS
        button5.alpha = TRANS
        button6.alpha = TRANS
    }
    override fun onBackPressed() {
        finishAffinity()
    }

}