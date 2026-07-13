package proyecto.picobotella.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import proyecto.picobotella.R

class RateRepository(private val context: Context) {

    fun createPlayStoreIntent(): Intent {
        val packageName = context.getString(R.string.nequi_package)

        return Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$packageName")
        ).apply {
            setPackage(context.getString(R.string.play_store_package))
        }
    }

    fun createWebFallbackIntent(): Intent {
        return Intent(
            Intent.ACTION_VIEW,
            Uri.parse(context.getString(R.string.play_store_url))
        )
    }
}
