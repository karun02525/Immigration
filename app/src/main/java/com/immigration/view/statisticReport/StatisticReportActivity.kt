package com.immigration.view.statisticReport

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.immigration.R
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

class StatisticReportActivity : AppCompatActivity() {

    private val TAG = StatisticReportActivity::class.java.name
    private var recy_statistic_report: RecyclerView? = null

    private var apiService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private var loginPreference: LoginPrefences? = null
    private lateinit var accessToken: String
    private var snackbarMessage: String? = null

    private var adapterStatisticReport: AdapterStatisticReport? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_statistic_report)



        apiService = ApiUtils.apiService
        loginPreference = LoginPrefences.getInstance()
        accessToken = loginPreference!!.getAccessToken(LoginPrefences.getInstance().getLoginPreferences(this))

        titile_report.text=resources.getString(R.string.txt_stattistics_report)
        recy_statistic_report = findViewById(R.id.recy_statistic_report)
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recy_statistic_report!!.layoutManager = mLayoutManager
        recy_statistic_report!!.itemAnimator = DefaultItemAnimator()

        pb = CustomProgressBar(this);
        pb.setCancelable(false)
        pb.show()

        statisticReport(accessToken)

        stati_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

    }




    private fun statisticReport(accessToken: String) {

        apiService!!.getStatisticReport(accessToken)
                .enqueue(object :retrofit2.Callback<Model_Statistic> {
                    override fun onResponse(call: Call<Model_Statistic>?, response: Response<Model_Statistic>?) {
                        pb.dismiss()
                        val status = response!!.code()
                        when (status) {
                            200 -> {
                             //   Utils.showToast(applicationContext, response.body()!!.message)
                                if(response.isSuccessful) {

                                    val list = response.body()!!.result
                                    adapterStatisticReport = AdapterStatisticReport(list as ArrayList<Result>)
                                    recy_statistic_report!!.adapter = adapterStatisticReport

                                }
                            }
                            401 -> {
                                Utils.showToast(applicationContext, Utils.errorHandlers(response))
                                Utils.invalidToken(this@StatisticReportActivity, loginPreference, LoginActivity())
                            }
                        }
                       /* snackbarMessage = Utils.responseStatuss(this@StatisticReportActivity, status, response)
                        if (snackbarMessage != null) {
                            Utils.showToastSnackbar(this@StatisticReportActivity, snackbarMessage!!, Color.WHITE)
                        }*/
                    }

                    override fun onFailure(call: Call<Model_Statistic>?, t: Throwable?) {
                        pb.dismiss()
                        Utils.log(TAG, "logout:  Throwable-:   $t")
                        Utils.showToastSnackbar(this@StatisticReportActivity, getString(R.string.no_internet).toString(), Color.RED)
                    }
                })
    }


}
