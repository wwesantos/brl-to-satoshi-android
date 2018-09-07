package co.javacafe.satoshirealconverter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.content.Context

abstract class BaseActivity : AppCompatActivity() {

    protected fun isNetworkAvailable():Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    protected fun verifyInternetConneciton():Boolean{
        return if(!isNetworkAvailable()){
            showErrorMessage("Check your Internet connection.")
            false
        }else{
            true
        }
    }

    protected fun showErrorMessage(message:String){
        AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("ok") { dialog, _ ->
                    dialog.cancel()
                }.show()
    }

    protected fun showSuccessMessage(message:String){
        AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("ok") { dialog, _ ->
                    dialog.cancel()
                }.show()
    }

    @Suppress("DEPRECATION")
    protected fun showProgress(message: String) :ProgressDialog{
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        progressDialog.show()
        return progressDialog
    }


}