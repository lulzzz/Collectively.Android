package com.noordwind.apps.collectively.presentation.views.toast

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import com.muddzdev.styleabletoastlibrary.StyleableToast
import com.noordwind.apps.collectively.R


class ToastManager(val context: Context, message: String, duration: Int) {

    private val toast = StyleableToast(context, message, duration)

    fun networkError() : ToastManager {
        toast.setBackgroundColor(Color.parseColor("#ff5a5f"))
        toast.setTextColor(Color.WHITE)


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            toast.setIcon(R.drawable.ic_signal_wifi_off_white_24dp)
        } else {
            toast.setIcon(R.drawable.ic_signal_wifi_off_pre_lollipop)
        }

        toast.setMaxAlpha()

        return this
    }

    fun error() : ToastManager {
        toast.setBackgroundColor(Color.parseColor("#ff5a5f"))
        toast.setTextColor(Color.WHITE)
        toast.setMaxAlpha()
        return this
    }

    fun progress(): ToastManager {
        toast.setTextColor(Color.WHITE)
        toast.setBackgroundColor(ContextCompat.getColor(context, R.color.red_dark_2))

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            toast.setIcon(R.drawable.ic_autorenew_24dp)
        } else {
            toast.setIcon(R.drawable.ic_autorenew_pre_lollipop)
        }

        toast.spinIcon()
        toast.setMaxAlpha()
        return this
    }

    fun show() : ToastManager {
        toast.show()
        return this
    }

    fun success(): ToastManager {
        toast.setBackgroundColor(Color.parseColor("#4CAF50"))

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            toast.setIcon(R.drawable.ic_check_white_24dp)
        } else {
            toast.setIcon(R.drawable.ic_check_white_pre_lollipop)
        }

        toast.setTextColor(Color.WHITE)
        toast.setMaxAlpha()

        return this
    }

    fun hide() {
        toast.cancel()
    }

}

