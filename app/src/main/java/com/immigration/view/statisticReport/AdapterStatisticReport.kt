package com.immigration.view.statisticReport

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.immigration.R
import com.immigration.view.statisticReport.dwonload_pdf.DwonloadPDFActivity
import com.immigration.view.statisticReport.model.Result
import kotlinx.android.synthetic.main.adapter_statistic_report_layout.view.*


class AdapterStatisticReport(val list:ArrayList<Result>): RecyclerView.Adapter<AdapterStatisticReport.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AdapterStatisticReport.ViewHolder {
        val v=LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_statistic_report_layout,parent,false)
        return AdapterStatisticReport.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AdapterStatisticReport.ViewHolder, position: Int) {
         holder.bindItems(list[position])
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         private val context:Context=itemView.context
          fun bindItems(model:Result){

              //"createdAt": "2018-02-21 00:13:35"
              val data_=model.createdAt;
              val date_split=data_.split(" ")
              val dateParse=date_split[0]
              val showDay=dateParse.split("-")
                itemView.txt_only_date.text=showDay[2]
                itemView.txt_date.text=showDay[1]+"/"+showDay[0]

              itemView.txt_title.text=model.name
              itemView.txt_points.text="${model.points} Points"
             // itemView.txt_description.text=model.descrption
              itemView.card_click.setOnClickListener({
                  context.startActivity(Intent(context,DwonloadPDFActivity::class.java)
                          .putExtra("key_immigrationId",model.immigrationId))

              })

            /*  val check=model.descrption
              if(check.contains(".pdf")){
                  itemView.rl_document_type_bg.setBackgroundColor(Color.RED)
                  itemView.txt_document_type.text="PDF"
                  itemView.txt_description.text=model.descrption
              }else if(check.contains(".docx")){
                  itemView.rl_document_type_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_doc_color))
                  itemView.txt_document_type.text="DOCX"
                  itemView.txt_description.text=model.descrption
              }else if(check.contains(".jpg")){
                  itemView.rl_document_type_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_png_color))
                  itemView.txt_document_type.text="JPEG"
                  itemView.txt_description.text=model.descrption
              }else if(check.contains(".png")){
                  itemView.rl_document_type_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_jpg_color))
                  itemView.txt_document_type.text="PNG"
                  itemView.txt_description.text=model.descrption
              }*/


          }

    }


}

