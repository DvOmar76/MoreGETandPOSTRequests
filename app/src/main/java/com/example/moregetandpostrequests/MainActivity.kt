package com.example.moregetandpostrequests

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    val apiInterface = APIClient().getClient()?.create(ApiInterface::class.java)
    lateinit var progressDialog: ProgressDialog
    val names = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnPost.setOnClickListener {
            val obUser1 = DataItem(edName1.text.toString())
            val obUser2 = DataItem(edName2.text.toString())
            val obUser3 = DataItem(edName3.text.toString())
            post(obUser1)
            post(obUser2)
            post(obUser3)


        }
        btnGetNames.setOnClickListener {
            getNames()
        }


    }

    fun post(obUser: DataItem) {
        funProgressDialog()
        if (apiInterface != null) {
            apiInterface.addUserToApi(obUser).enqueue(object : Callback<DataItem?> {
                override fun onResponse(call: Call<DataItem?>, response: Response<DataItem?>) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "users added", Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(call: Call<DataItem?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()

                }
            })
        }
        progressDialog.dismiss()

    }

    fun getNames() {
        funProgressDialog()
        if (apiInterface != null) {
            apiInterface.getUser()?.enqueue(object : Callback<Array<DataItem>?> {
                override fun onResponse(
                    call: Call<Array<DataItem>?>,
                    response: Response<Array<DataItem>?>
                ) {
                    progressDialog.dismiss()
                    val users=response.body()!!
                    for (user in users) {
                        names.add(user.name)
                    }
                    RV.adapter = RVAdapter(names)
                    RV.layoutManager = LinearLayoutManager(this@MainActivity)

                }


                override fun onFailure(call: Call<Array<DataItem>?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }


    fun funProgressDialog() {
        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()
    }
}