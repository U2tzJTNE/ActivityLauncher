package com.u2tzjtne.activitylauncher

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var etPackageName: EditText
    private lateinit var etActivityName: EditText
    private lateinit var etExtra: EditText
    private lateinit var btnStart: Button
    private var mPackageName = ""
    private var mActivityName = ""
    private var mExtra = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initEvent()
    }

    private fun initView() {
        etPackageName = findViewById(R.id.et_package_name)
        etActivityName = findViewById(R.id.et_activity_name)
        etExtra = findViewById(R.id.et_extra)
        btnStart = findViewById(R.id.btn_start)
    }

    private fun initEvent() {
        btnStart.setOnClickListener { startApp() }
        etPackageName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mPackageName = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        etActivityName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mActivityName = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        etExtra.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mExtra = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun startApp() {
        if (check()) {
            var intent = Intent()
            if (mActivityName.isNotEmpty()) {
                val cn = ComponentName(mPackageName, mActivityName)
                intent.component = cn
            } else {
                val packageManager = packageManager
                intent = packageManager.getLaunchIntentForPackage(mPackageName)!!
            }
            //添加参数
            if (jsonArray.length() > 0) {
                for (i in 0 until jsonArray.length()) {
                    try {
                        val obj: JSONObject = jsonArray.get(i) as JSONObject
                        val key = obj.getString("key")
                        when (obj.get("value")) {
                            is String -> {
                                intent.putExtra(key, obj.getString("value"))
                            }
                            is Int -> {
                                intent.putExtra(key, obj.getInt("value"))
                            }
                            is Boolean -> {
                                intent.putExtra(key, obj.getBoolean("value"))
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this, "请检查参数格式", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            try {
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Activity未找到", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var jsonArray: JSONArray = JSONArray()
    private fun parseExtra() {
        jsonArray = try {
            JSONArray(mExtra)
        } catch (e: Exception) {
            e.printStackTrace()
            JSONArray()
        }
    }

    private fun check(): Boolean {
        if (mPackageName.isEmpty()) {
            Toast.makeText(this, "请检查包名", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!checkPackInfo(mPackageName)) {
            Toast.makeText(this, "应用未安装", Toast.LENGTH_SHORT).show()
            return false
        }
        if (mExtra.isNotEmpty()) {
            parseExtra()
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        etPackageName.setText(SPUtils.getString("data_package_name", ""))
        etActivityName.setText(SPUtils.getString("data_activity_name", ""))
        etExtra.setText(SPUtils.getString("data_extra", ""))
    }

    override fun onStop() {
        super.onStop()
        SPUtils.putString("data_package_name", mPackageName)
        SPUtils.putString("data_activity_name", mActivityName)
        SPUtils.putString("data_extra", mExtra)
    }

    /**
     * 检查包是否存在
     *
     * @param packageName
     * @return
     */
    private fun checkPackInfo(packageName: String): Boolean {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packageInfo != null
    }
}