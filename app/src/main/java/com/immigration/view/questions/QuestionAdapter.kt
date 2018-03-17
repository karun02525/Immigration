package com.immigration.view.questions

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.immigration.R
import com.immigration.view.questions.model.Result
import com.immigration.view.questions.spinner_model.Model_Sppiner
import com.immigration.view.questions.spinner_model.MyAdapter


class QuestionAdapter(val context: QuestionsActivity, val list: ArrayList<Result>) : RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() {
    var count =0
    var is_check_hide =0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_question_layout, parent, false)
        return MyViewHolder(itemView)
    }
    
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val res = list[position]
          
          holder.isCheck_hide.visibility=View.VISIBLE
          holder.txt_questation_num.text = "${position + 1}) ${res.question}"
          holder.lists = ArrayList<Model_Sppiner>()
          holder.lists.add(0, Model_Sppiner("", "Select...", ""))
          for (i in (res.answer)) {
             holder.lists.add(Model_Sppiner(i.answerId, i.answer, i.isCorrect))
          }
          holder.adp = MyAdapter(context, holder.lists)
          holder.sp_questions.adapter = holder.adp
      
      
          holder.sp_questions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                val answer_model: Model_Sppiner = holder.adp.getItem(position) as Model_Sppiner
             
             //   if (answer_model.isCorrect == "1") {
                   //count++
                   holder.isCheck_hide.visibility=View.VISIBLE
                   is_check_hide++
                 //  count += answer_model.point.toInt()
                   QuestionsActivity.isChecks = true
                   QuestionsActivity.txt_count.text = count.toString()
             //      Toast.makeText(context, "ID: " + answer_model.answerId + "\nName: " + answer_model.answer, Toast.LENGTH_SHORT).show()
                   context.initJsonPareses("1",res.questionId,answer_model.answerId)
               // }
             }
         
             override fun onNothingSelected(adapter: AdapterView<*>) {}
          }
       
    }
    
    override fun getItemCount(): Int {
        return list.size
    }
    
    
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var lists: ArrayList<Model_Sppiner>
        lateinit var adp: MyAdapter
        
        val isCheck_hide: RelativeLayout = view.findViewById(R.id.isCheck_hide)
        val txt_questation_num: TextView = view.findViewById(R.id.txt_questation_num)
        val sp_questions: Spinner = view.findViewById(R.id.sp_questions)
    }
    
}