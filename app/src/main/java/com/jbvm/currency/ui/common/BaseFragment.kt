package com.jbvm.currency.ui.common

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.jbvm.currency.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseFragment : Fragment() {
    private val subscriptions = CompositeDisposable()

    fun subscribe(disposable: Disposable): Disposable {
        subscriptions.add(disposable)
        return disposable
    }

    override fun onStop() {
        super.onStop()
        subscriptions.clear()
    }

    fun loadFragment(fragment: Fragment){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.addToBackStack(fragment::class.simpleName)
        transaction?.add(R.id.container, fragment)
        transaction?.commit()
    }

    private fun popFragment(){
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    fun showError(error: String) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.dialog_error_title)
                setMessage(error)
                setPositiveButton(
                    R.string.dialog_positive_button
                ) { dialog, id ->
                    dialog.dismiss()
                    popFragment()
                }
            }
            builder.create()
        }

        alertDialog?.show()
    }
}