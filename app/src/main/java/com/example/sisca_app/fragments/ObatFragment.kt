package com.example.sisca_app.fragments

import android.content.Intent
import com.example.sisca_app.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sisca_app.Adapters.DataPenyakit
import com.example.sisca_app.Adapters.ObatAdapter
import com.example.sisca_app.Models.DetailPenyakit
import com.example.sisca_app.Models.diseasedata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class ObatFragment: Fragment(){
    private var list: ArrayList<DataPenyakit> = arrayListOf()
    private lateinit var rvobat: RecyclerView
    private var patientNickName: TextView? = null
    private var fAuth: FirebaseAuth? = null
    private var fStore: FirebaseFirestore? = null
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if(container==null) return null

        val view = inflater.inflate(R.layout.fragment_obat, container, false)
        patientNickName = view.findViewById<View>(R.id.patient_home_name) as TextView

        var rvobat = view.findViewById<View>(R.id.rv_obat)
        var obatName = view.findViewById<TextView>(R.id.tv_obat_name)
        list.addAll(diseasedata.listdata)
        showRecyclerList()

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        userId = fAuth!!.currentUser!!.uid
        val documentReference = fStore!!.collection("users").document(
            userId!!
        )
        documentReference.addSnapshotListener(requireActivity()) { value, error ->
            patientNickName!!.text = value!!.getString("nName")
        }
        val reference = FirebaseDatabase.getInstance().reference.child("Sensor")


        return view
    }

    private fun showSelected(data: DataPenyakit){
        Toast.makeText(requireActivity(), "Anda memilih  " + data.namapenyakit, Toast.LENGTH_SHORT).show()
    }

    private fun showRecyclerList() {
        rvobat.layoutManager = LinearLayoutManager(requireActivity())

        val obatAdapter = ObatAdapter(list)
        rvobat.adapter = obatAdapter

        obatAdapter.setOnItemClickCallback(object: ObatAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataPenyakit) {
                val diseaseParcel = DataPenyakit(
                    data.jenispengobatan,
                    data.namapenyakit
                )

//                val moveintent = Intent(this@HomeFragment.requireContext(), DetailPenyakit::class.java)
//                moveintent.putExtra(DetailPenyakit.EXTRA_DISEASE,diseaseParcel)
//                startActivity(moveintent)

                showSelected(data)
            }
        })
    }


}

