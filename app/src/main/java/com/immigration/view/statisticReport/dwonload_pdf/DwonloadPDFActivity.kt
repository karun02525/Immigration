package com.immigration.view.statisticReport.dwonload_pdf

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.immigration.R
import com.immigration.appdata.Constant.BASE_URL_Image
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.view.login.LoginActivity
import com.immigration.view.statisticReport.model.Model_Statistic
import com.immigration.view.statisticReport.model.Result
import kotlinx.android.synthetic.main.activity_statistic_report.*
import retrofit2.Call
import retrofit2.Response
import java.io.File

class DwonloadPDFActivity : AppCompatActivity(),AdapterPDF.Listener {


    private val TAG = DwonloadPDFActivity::class.java.name
    private var recy_statistic_report: RecyclerView? = null

    private var apiService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private var loginPreference: LoginPrefences? = null
    private lateinit var immigrationId: String
    private var snackbarMessage: String? = null

    private var adapterPdf: AdapterPDF? = null



    private var downloadManager: DownloadManager? = null
    private var refid: Long = 0
    private var Download_Uri: Uri? = null
    private var list = java.util.ArrayList<Long>()
    private lateinit var listPDF:ArrayList<Result>
    private lateinit var listStorage:ArrayList<String>
    private var file: File? = null
    private lateinit var pdfNameURL:String

    companion object {

        var NOTIFICATION_ID = 1
        private val filename = "sample"
        private val folderName = "/Download//Immigration/"
        private val folderNameSubpath = "/Immigration//"
    //    private val url = "http://rusenergyweek.com/upload/iblock/1b9/1b9cb0045fcda0e07be921ec922f5191.pdf"
        private val extenstion_filename = ".pdf"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_statistic_report)
        pdfNameURL=""
        immigrationId=intent.getStringExtra("key_immigrationId")

        apiService = ApiUtils.apiService
        titile_report.text=resources.getString(R.string.txt_pdf_list)
        listPDF =ArrayList()
        recy_statistic_report = findViewById(R.id.recy_statistic_report)
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recy_statistic_report!!.layoutManager = mLayoutManager
        recy_statistic_report!!.itemAnimator = DefaultItemAnimator()

        pb = CustomProgressBar(this);
        pb.setCancelable(false)
        pb.show()



        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))



        getPdfs(immigrationId)
        stati_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

    }




    private fun getPdfs(immigrationId: String) {
        listStorage= ArrayList()


        val path = Environment.getExternalStorageDirectory().path +folderName
        Log.d("Files", "Path: " + path)
        val directory = File(path)
        val files = directory.listFiles()
        Log.d("Files", "Size: " + files!!.size)
        for (i in files.indices) {
            Log.d("Files", "FileName:" + files[i].name)
            listStorage.add(files[i].name)
        }


        apiService!!.getPdfs(immigrationId)
                .enqueue(object :retrofit2.Callback<Model_Statistic> {
                    override fun onResponse(call: Call<Model_Statistic>?, response: Response<Model_Statistic>?) {
                        pb.dismiss()
                        val status = response!!.code()
                        when (status) {
                            200 -> {
                              //  Utils.showToast(applicationContext, response.body()!!.message)
                                if(response.isSuccessful) {

                                    listPDF = response.body()!!.result as ArrayList<Result>
                                    adapterPdf = AdapterPDF(this@DwonloadPDFActivity,listPDF,this@DwonloadPDFActivity)
                                    recy_statistic_report!!.adapter = adapterPdf

                                 //   for(m:Result in listPDF){
                                  //      m.ischeckDwonload=true
                                  //  }

                                }
                            }
                            401 -> {
                                Utils.showToast(applicationContext, Utils.errorHandlers(response))
                                Utils.invalidToken(this@DwonloadPDFActivity, loginPreference, LoginActivity())
                            }
                        }
                       /* snackbarMessage = Utils.responseStatuss(this@DwonloadPDFActivity, status, response)
                        if (snackbarMessage != null) {
                            Utils.showToastSnackbar(this@DwonloadPDFActivity, snackbarMessage!!, Color.WHITE)
                        }*/
                    }

                    override fun onFailure(call: Call<Model_Statistic>?, t: Throwable?) {
                        pb.dismiss()
                        Utils.log(TAG, "logout:  Throwable-:   $t")
                        Utils.showToastSnackbar(this@DwonloadPDFActivity, getString(R.string.no_internet).toString(), Color.RED)
                    }
                })


    }



     fun dwonloadPDF(url:String,postion:String,pdfName:String){
         pdfNameURL=pdfName
            file = File(Environment.getExternalStorageDirectory().path +folderName+filename+postion+ extenstion_filename)
            if (file!!.exists()) {
              //  Toast.makeText(applicationContext, "Already exists", Toast.LENGTH_SHORT).show()
                showPDF()
            } else {
                Download_Uri = Uri.parse(BASE_URL_Image+url)
                list.clear()
                val request = DownloadManager.Request(Download_Uri)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                request.setAllowedOverRoaming(false)
                request.setTitle("Immigration")
                request.setDescription(pdfName)
                request.setVisibleInDownloadsUi(true)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, folderNameSubpath+filename+postion+extenstion_filename)
                refid = downloadManager!!.enqueue(request)
                Log.e("OUT", "" + refid)
                list.add(refid)
            }

    }


     fun showPDF() {
        try {
          //  file = File(Environment.getExternalStorageDirectory().path + folderName + filename+ extenstion_filename)

            if (file!!.exists()) {
                val path = Uri.fromFile(file)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(path, "application/pdf")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Please download file..", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "deices not support", Toast.LENGTH_SHORT).show()
        }
    }

    //Dwonload PDF....................

    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Log.e("IN", "" + referenceId)
            list.remove(referenceId)
            if (list.isEmpty())
            {

                val intents = Intent(Intent.ACTION_VIEW)
                val pendingIntent: PendingIntent
                try {
                    val path = Uri.fromFile(file)
                    intents.setDataAndType(path, "application/pdf")
                    startActivity(intents)
                }catch (e:Exception){
                    Toast.makeText(applicationContext, "deices not support", Toast.LENGTH_SHORT).show()
                }
                pendingIntent = PendingIntent.getActivity(this@DwonloadPDFActivity, 0, intents, PendingIntent.FLAG_ONE_SHOT)
                val mNotifyBuilder = NotificationCompat.Builder(this@DwonloadPDFActivity, "")
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
                        .setContentText(pdfNameURL)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (NOTIFICATION_ID > 1073741824) {
                    NOTIFICATION_ID = 0
                }
                notificationManager.notify(NOTIFICATION_ID++, mNotifyBuilder.build())
            }
        }
    }


    override fun onItemClick(model: Result, pos:Int) {

     //   Toast.makeText(applicationContext,"KK"+model.details+" p"+ pos,Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onComplete)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission granted
        }
    }

}