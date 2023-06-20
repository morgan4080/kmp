package com.presta.customer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.flow.MutableStateFlow
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays


actual class Platform actual constructor(
    private val context: AppContext
) {

    actual val platformName: String = "Android"

    actual val otpCode = MutableStateFlow("")
    actual fun showToast(text: String, duration: Durations) {
        Toast.makeText(
            context, text, when (duration) {
                Durations.SHORT -> Toast.LENGTH_SHORT
                Durations.LONG -> Toast.LENGTH_LONG
            }
        ).show()
    }
    actual fun startSmsRetriever() {
        val client = SmsRetriever.getClient(context)
        client.startSmsRetriever()
    }

    private val smsBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val data = intent.extras
                if (data != null) {
                    val status = data[SmsRetriever.EXTRA_STATUS] as Status

                    when (status.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            val appMessage = data[SmsRetriever.EXTRA_SMS_MESSAGE] as String
                            StringBuilder().apply {
                                append(appMessage)
                                toString().also { otp ->
                                    otpCode.value = otp.substringAfterLast("is:").replace(": ", "").trim().substring(0, 4)
                                }
                            }
                        }
                        CommonStatusCodes.TIMEOUT -> {
                            Log.d("App SMS", "TimeOut")
                        }
                    }
                }
            }
        }
    }

    actual fun getAppSignatures(): String {
        val helper = AppSignatureHelper(context)
        val hash = mutableStateOf("")
        val signatures = helper.getAppSignatures()
        for (signature in signatures) {
            hash.value = signature
        }
        return hash.value
    }

    init {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        context.registerReceiver(
            smsBroadcastReceiver,
            intentFilter,
            SmsRetriever.SEND_PERMISSION,
            null
        )
    }
}

class AppSignatureHelper(
    context: AppContext
): ContextWrapper(context) {
    private val tag = AppSignatureHelper::class.java.simpleName
    private val hashType = "SHA-256"
    private val numHashedBytes = 9
    private val numBase64Char = 11

    fun getAppSignatures(): ArrayList<String> {
        val appCodes = ArrayList<String>()
        try {
            // Get all package signatures for the current package
            val packageName = packageName
            val packageManager = packageManager
            val signatures: Array<Signature> = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            ).signatures

            // For each signature create a compatible hash
            for (signature in signatures) {
                val hash = hash(packageName, signature.toCharsString())
                if (hash != null) {
                    appCodes.add(String.format("%s", hash))
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(tag, "Unable to find package to obtain hash.", e)
        }
        return appCodes
    }

    private fun hash(packageName: String, signature: String): String? {
        val appInfo = "$packageName $signature"
        try {
            val messageDigest = MessageDigest.getInstance(hashType)
            messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
            var hashSignature = messageDigest.digest()

            // truncated into numHashedBytes
            hashSignature = Arrays.copyOfRange(hashSignature, 0, numHashedBytes)
            // encode into Base64
            var base64Hash: String =
                Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
            base64Hash = base64Hash.substring(0, numBase64Char)
            Log.d(tag, String.format("pkg: %s -- hash: %s", packageName, base64Hash))
            return base64Hash
        } catch (e: NoSuchAlgorithmException) {
            Log.e(tag, "hash:NoSuchAlgorithm", e)
        }
        return null
    }
}