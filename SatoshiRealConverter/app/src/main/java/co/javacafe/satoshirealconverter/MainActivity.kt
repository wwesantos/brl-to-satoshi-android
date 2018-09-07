package co.javacafe.satoshirealconverter


import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class MainActivity : BaseActivity() {

    private var exchangeRateBrlToBtc = BigDecimal("10000")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disbleInputs()

        if (!verifyInternetConneciton()){
            enableSyncBtn()
        }else{
            LoadExchangeRate().execute()
        }

        txtBRL.setOnKeyListener( View.OnKeyListener{ v, _, event ->
            if (event.action == KeyEvent.ACTION_UP){
                val text = (v as EditText).text.toString()
                updateValues(text, ValueType.BRL)
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })

        txtBTC.setOnKeyListener( View.OnKeyListener{ v, _, event ->
            if (event.action == KeyEvent.ACTION_UP){
                val text = (v as EditText).text.toString()
                updateValues(text, ValueType.BTC)
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })

        txtMilliBTC.setOnKeyListener( View.OnKeyListener{ v, _, event ->
            if (event.action == KeyEvent.ACTION_UP){
                val text = (v as EditText).text.toString()
                updateValues(text, ValueType.MILLI_BTC)
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })

        txtMicroBTC.setOnKeyListener( View.OnKeyListener{ v, _, event ->
            if (event.action == KeyEvent.ACTION_UP){
                val text = (v as EditText).text.toString()
                updateValues(text, ValueType.MICRO_BTC)
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })

        txtSatoshiBTC.setOnKeyListener( View.OnKeyListener{ v, _, event ->
            if (event.action == KeyEvent.ACTION_UP){
                val text = (v as EditText).text.toString()
                updateValues(text, ValueType.SATOSHI_BTC)
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })

        btnSync.setOnClickListener{ _ ->
            disbleInputs()
            LoadExchangeRate().execute()
        }

        bottomLayout.setOnTouchListener(View.OnTouchListener { _, _ ->
            hideKeyboard(this)
            return@OnTouchListener true
        })

        labelsLayout.setOnTouchListener(View.OnTouchListener { _, _ ->
            hideKeyboard(this)
            return@OnTouchListener true
        })

    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun updateValues(value: String, type: ValueType){

        if(value.isBlank()){
            txtBRL.setText("")
            txtBTC.setText("")
            txtMicroBTC.setText("")
            txtMicroBTC.setText("")
            txtSatoshiBTC.setText("")
            return
        }

        val currentBtcValue:BigDecimal = when(type){
            ValueType.BRL -> BigDecimal(value).divide(exchangeRateBrlToBtc, 30, RoundingMode.HALF_UP)
            ValueType.BTC -> BigDecimal(value)
            ValueType.MILLI_BTC -> BigDecimal(value).divide(MILLI_BTC, 30, RoundingMode.HALF_UP)
            ValueType.MICRO_BTC -> BigDecimal(value).divide(MICRO_BTC, 30, RoundingMode.HALF_UP)
            ValueType.SATOSHI_BTC -> BigDecimal(value).divide(SATOSHI_BTC, 30, RoundingMode.HALF_UP)
        }

        if(type != ValueType.BRL){
            val text = getFormatedAndTrimmed (currentBtcValue.times(exchangeRateBrlToBtc), "0.00")
            txtBRL.setText(text)
        }
        if(type != ValueType.BTC){
            val text = getFormatedAndTrimmed(currentBtcValue,"0.############")
            txtBTC.setText(text)
        }
        if(type != ValueType.MILLI_BTC){
            val text = getFormatedAndTrimmed(currentBtcValue.times(MILLI_BTC),"0.############")
            txtMilliBTC.setText(text)
        }
        if(type != ValueType.MICRO_BTC){
            val text = getFormatedAndTrimmed(currentBtcValue.times(MICRO_BTC),"0.############")
            txtMicroBTC.setText(text)
        }
        if(type != ValueType.SATOSHI_BTC){
            val text = getFormatedAndTrimmed(currentBtcValue.times(SATOSHI_BTC),"0.############")
            txtSatoshiBTC.setText(text)
        }
    }

    private fun getFormatedAndTrimmed(value: BigDecimal, pattern :String): String{
        val text = DecimalFormat(pattern).format(value)
        return if (text.length < 18){
            text
        }else{
            text.substring(0, 18)
        }
    }

    private fun enableInputs(){
        txtBRL.isEnabled = true
        txtBTC.isEnabled = true
        txtMicroBTC.isEnabled = true
        txtMilliBTC.isEnabled = true
        txtSatoshiBTC.isEnabled = true
        btnSync.isEnabled = true
    }

    private fun enableSyncBtn(){
        btnSync.isEnabled = true
    }

    private fun disbleInputs(){
        txtBRL.isEnabled = false
        txtBTC.isEnabled = false
        txtMicroBTC.isEnabled = false
        txtMilliBTC.isEnabled = false
        txtSatoshiBTC.isEnabled = false
        btnSync.isEnabled = false
    }



    private inner class LoadExchangeRate: AsyncTask<Void, Void, AsyncTaskResult<String>> (){
        private var progressDialog: ProgressDialog? = null
        override fun onPreExecute(){
            progressDialog = showProgress("Loading exchange rate")
        }

        override fun doInBackground(vararg params: Void?): AsyncTaskResult<String>? {
            try{
                return AsyncTaskResult(loadExhangeRateAPI())
            }catch (e: Exception){
                return AsyncTaskResult(e)
            }
        }

        override fun onPostExecute(result: AsyncTaskResult<String>?) {
            progressDialog?.dismiss()
            if(result?.error != null){
                showErrorMessage(result.error.localizedMessage)
                enableSyncBtn()
            }else{
                exchangeRateBrlToBtc = BigDecimal(result?.result)
                lblExchangeRate.text = "BRL/BTC: " + getFormatedAndTrimmed(exchangeRateBrlToBtc,"0.############")
                lblLastUpdate.text = "Last Update: " + DATE_FORMAT.format(Calendar.getInstance().time)
                enableInputs()
            }
        }
    }

}
