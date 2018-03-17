package com.immigration.view.questions

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import android.widget.Toast
import com.immigration.R
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.view.questions.model.QuestionModel
import com.immigration.view.questions.model.Result
import com.immigration.view.subscription.ResultActivity
import kotlinx.android.synthetic.main.activity_questions.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class QuestionsActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var rideAdapter: QuestionAdapter? = null
    private var mAPIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private lateinit var listAdd:ArrayList<Result>

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var txt_count: TextView
        var isChecks: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_questions)

        listAdd= ArrayList()
        mAPIService = ApiUtils.apiService;
        pb = CustomProgressBar(this);
        pb.setCancelable(true)
        pb.show()

        txt_title.text = intent.getStringExtra("option")
        initView()

    }

    private fun initView() {
        recyclerView = findViewById(R.id.question_recy)
        txt_count = findViewById<TextView>(R.id.txt_count_ponts)
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        home_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        
        initJsonPareses("1","0","0")
    }


    
    
    fun initJsonPareses(a:String,b:String,c:String) {

        mAPIService!!.getQuestions(a,b,c).enqueue(object : Callback, retrofit2.Callback<QuestionModel> {

            override fun onResponse(call: Call<QuestionModel>?, response: Response<QuestionModel>?) {
                pb.dismiss()
                if (response!!.isSuccessful) {

                  val list = response.body()!!.result as ArrayList<Result>

                    listAdd.addAll(list)
                    if(!listAdd.isEmpty()) {

                        try {
                            rideAdapter = QuestionAdapter(this@QuestionsActivity, listAdd)
                            recyclerView!!.adapter = rideAdapter
                            rideAdapter!!.notifyDataSetChanged()

                        } catch (e: Exception) {
                        }
                    }else Toast.makeText(applicationContext,"Data empty",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<QuestionModel>?, t: Throwable?) {
                pb.dismiss()
                Utils.showToastSnackbar(this@QuestionsActivity,"Sorry!No internet available",Color.RED)

            }
        })


        btn_submit_qus.setOnClickListener {
            if (isChecks) {
                startActivity(Intent(this@QuestionsActivity, ResultActivity::class.java)
                        .putExtra("session_sub", "1")
                )
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            } else Utils.showToastSnackbar(this, getString(R.string.question_validation), Color.WHITE)

        }


    }

    override fun onStop() {
        super.onStop()
        isChecks=false
    }
}

