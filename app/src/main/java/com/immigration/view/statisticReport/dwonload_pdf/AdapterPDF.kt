package com.immigration.view.statisticReport.dwonload_pdf

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.immigration.R
import com.immigration.view.statisticReport.model.Result
import kotlinx.android.synthetic.main.adapter_pdf_dwonlaod_layout.view.*

class AdapterPDF(val context: DwonloadPDFActivity, val list:ArrayList<Result>, private val listener:Listener): RecyclerView.Adapter<AdapterPDF.ViewHolder>() {

    interface Listener{
        fun onItemClick(model: Result, pos: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AdapterPDF.ViewHolder {
        val v= LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_pdf_dwonlaod_layout,parent,false)
        return AdapterPDF.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AdapterPDF.ViewHolder, position: Int) {


         holder.bindItems(list[position],context,position,listener)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

      //  private val context:Context=itemView.context
        fun bindItems(model: Result,cotext:DwonloadPDFActivity,pos:Int,listener: Listener) {
            itemView.txt_title.text = model.details


        //  for(i in listStorage) {
           //   val ss = "sample$pos.pdf"
           //   if (ss == i) {
                //  itemView.btn_dwonload.setImageResource(R.drawable.ic_check_circle_show)
               //   itemView.btn_dwonload.setOnClickListener({
               //       cotext.showPDF()
               //   })
          //    } else {

                  itemView.btn_dwonload.setImageResource(R.drawable.ic_file_download)
                  itemView.btn_dwonload.setOnClickListener({
                      cotext.dwonloadPDF(model.pdf, pos.toString(), model.details)
                  })

             itemView.setOnClickListener {
                 listener.onItemClick(model,pos)
             }

      }
    }

}