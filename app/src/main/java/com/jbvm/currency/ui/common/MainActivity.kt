package com.jbvm.currency.ui.common

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jbvm.currency.R
import com.jbvm.currency.ui.symbols.SymbolsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SymbolsFragment.newInstance())
                    .commit()
        }
    }

    override fun onBackPressed() {
        Log.e("Fragments",supportFragmentManager.backStackEntryCount.toString())
        if(supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }
}