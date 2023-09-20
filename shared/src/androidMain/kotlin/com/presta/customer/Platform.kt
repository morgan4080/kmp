package com.presta.customer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.util.Base64
import android.util.Log
import android.webkit.CookieManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.mohamedrejeb.calf.ui.web.WebViewState
import kotlinx.coroutines.flow.MutableStateFlow
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays
import java.util.concurrent.Executor


enum class AuthenticationType {
    FINGERPRINT,
    FACIAL_RECOGNITION,
    IRIS
}
enum class SecurityLevel {
    NONE,
    SECRET,
    BIOMETRIC
}
data class LocalAuthenticationResult (
    val success: Boolean,
    val error: String?,
    val warning: String?
)


actual class Platform(
    private val context: Context
) {
    private val phoneUtil = PhoneNumberUtil.getInstance()
    private val currentActivity: AppCompatActivity = (context as AppCompatActivity)
    private val contactRequestCode = 421
    private var contactRequestCodeValue: Int? = null
    private var alpha2CodeValue: String? = null
    actual val otpCode = MutableStateFlow("")
    actual val resultFromContact: MutableStateFlow<Map<String, String>> = MutableStateFlow(emptyMap())
    actual fun showToast(text: String, duration: Durations) {
        Toast.makeText(
            context, text, when (duration) {
                Durations.SHORT -> Toast.LENGTH_SHORT
                Durations.LONG -> Toast.LENGTH_LONG
            }
        ).show()
    }
    actual fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        context.startActivity(intent)
    }
    actual  fun logErrorsToFirebase(Error: Exception){
        //Firebase.crashlytics.log(Error)
        FirebaseCrashlytics.getInstance().recordException(Error)
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

    private fun handleOnActivityResult(activity: Activity?, requestCode: Int?, country: String?, resultCode: Int, data: Intent?) {
        if (requestCode == contactRequestCode) {
            if (resultCode == Activity.RESULT_OK && data !== null) {
                var cursor: Cursor? = null

                try {
                    val contactUri: Uri? = data.data

                    val projection = arrayOf(
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    )

                    if (contactUri !== null) {
                        cursor = activity!!.contentResolver
                            .query(contactUri, projection, null, null, null)

                        var number: String? = ""

                        var name: String? = ""

                        if (cursor !== null && cursor.count > 0) {
                            if (cursor.moveToFirst()) {
                                val column =
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                number = cursor.getString(column)
                                val column0 =
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                                name = cursor.getString(column0)
                            }
                        }

                        val phoneNumber: Phonenumber.PhoneNumber = phoneUtil.parseAndKeepRawInput(number, country)

                        if (name !== null) {
                            resultFromContact.value = mapOf(name to "${phoneNumber.countryCode}${phoneNumber.nationalNumber}")
                        }

                    }


                } catch (e: Exception) {
                    resultFromContact.value = mapOf("CONTACT_PICKER_FAILED" to e.stackTraceToString())
                } finally {
                    if (cursor !== null) {
                        cursor.close()
                    }
                }
            }
        }


    }

    private val startForResult = currentActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

            if (intent != null) {
                println(intent.extras)

                handleOnActivityResult(currentActivity, contactRequestCodeValue, alpha2CodeValue, result.resultCode, intent)
            }
        }
    }

    actual fun getContact(contactRequestCode: Int, alpha2Code: String): MutableStateFlow<Map<String, String>> {
        return try {
            alpha2CodeValue = alpha2Code
            contactRequestCodeValue = contactRequestCode
            val contacts = Intent(Intent.ACTION_PICK).apply {
                setDataAndType( ContactsContract.Contacts.CONTENT_URI, ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE)
            }
            startForResult.apply {
                launch(contacts)
            }
            resultFromContact.value = mapOf("ACTIVITY_STARTED" to "TRUE")
            resultFromContact
        } catch (e: Exception) {
            e.printStackTrace()
            resultFromContact.value = mapOf("E_FAILED_TO_SHOW_PICKER" to e.stackTraceToString())
            resultFromContact
        }
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

    /*actual fun acceptWebViewCookies(webview: WebViewState) {
        CookieManager.getInstance().setAcceptThirdPartyCookies(webview.webView, true)
    }*/
}

class AppSignatureHelper(
    context: Context
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

actual fun getPlatformName(): String = "Android"

class BiometricAuthenticator(
    private val context: AppContext
) {
    private var biometricPrompt: BiometricPrompt? = null
    private var isAuthenticating = false
    private val biometricManager = BiometricManager.from(context)
    private val packageManager = context.packageManager
    private val keyguardManager: KeyguardManager
        get() = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    fun hasHardwareAsync(): Boolean {
        val result =
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        return result != BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
    }

    fun supportedAuthenticationTypes(): List<AuthenticationType> {
        val result =
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        val results: MutableList<AuthenticationType> = ArrayList()
        if (result == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            return results
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (packageManager.hasSystemFeature("android.hardware.fingerprint")) {
                results.add(AuthenticationType.FINGERPRINT)
            }
        }

        if (Build.VERSION.SDK_INT >= 29) {
            if (packageManager.hasSystemFeature("android.hardware.biometrics.face")) {
                results.add(AuthenticationType.FACIAL_RECOGNITION)
            }
            if (packageManager.hasSystemFeature("android.hardware.biometrics.iris")) {
                results.add(AuthenticationType.IRIS)
            }
        }

        // check for face recognition support on some samsung devices
        if (packageManager.hasSystemFeature("com.samsung.android.bio.face") && !results.contains(
                AuthenticationType.FACIAL_RECOGNITION
            )
        ) {
            results.add(AuthenticationType.FACIAL_RECOGNITION)
        }

        return results
    }

    fun isEnrolledAsync(): Boolean {
        val result =
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        return result == BiometricManager.BIOMETRIC_SUCCESS
    }

    private val isDeviceSecure: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyguardManager.isDeviceSecure
        } else {
            keyguardManager.isKeyguardSecure
        }

    fun getEnrolledLevel(): SecurityLevel {
        var level = SecurityLevel.NONE
        if (isDeviceSecure) {
            level = SecurityLevel.SECRET
        }
        val result =
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        if (result == BiometricManager.BIOMETRIC_SUCCESS) {
            level = SecurityLevel.BIOMETRIC
        }
        return level
    }

    private var fragmentManager: FragmentManager? = null

    class ResolverFragment : Fragment() {
        private lateinit var executor: Executor
        private lateinit var biometricPrompt: BiometricPrompt
        private lateinit var promptInfo: BiometricPrompt.PromptInfo

        init {
            retainInstance = true
        }

        fun showBiometricPrompt(
            requestTitle: String,
            requestReason: String,
            failureButtonText: String,
            credentialAllowed: Boolean,
            callback: (Result<Boolean>) -> Unit
        ) {
            val context = requireContext()

            executor = ContextCompat.getMainExecutor(context)

            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    @SuppressLint("RestrictedApi")
                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                            errorCode == BiometricPrompt.ERROR_USER_CANCELED
                        ) {
                            callback.invoke(Result.success(false))
                        } else {
                            callback.invoke(Result.failure(Exception(errString.toString())))
                        }
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        callback.invoke(Result.success(true))
                    }
                }
            )

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(requestTitle)
                .setSubtitle(requestReason)
                .apply {
                    if (!credentialAllowed) {
                        this.setNegativeButtonText(failureButtonText)
                    }
                }
                .setDeviceCredentialAllowed(credentialAllowed)
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun getResolverFragment(): ResolverFragment {
        val fragmentManager: FragmentManager = fragmentManager
            ?: error("can't check biometry without active window")

        val currentFragment: Fragment? = fragmentManager
            .findFragmentByTag(BIOMETRY_RESOLVER_FRAGMENT_TAG)

        return if (currentFragment != null) {
            currentFragment as ResolverFragment
        } else {
            ResolverFragment().apply {
                fragmentManager
                    .beginTransaction()
                    .add(this, BIOMETRY_RESOLVER_FRAGMENT_TAG)
                    .commitNow()
            }
        }
    }

    suspend fun authenticate(options: Map<String?, Any?>): LocalAuthenticationResult {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return LocalAuthenticationResult(
                success = false,
                error = "E_NOT_SUPPORTED",
                warning = "Cannot display biometric prompt on android versions below 6.0"
            )
        }

        if (!keyguardManager.isDeviceSecure) {
            return LocalAuthenticationResult(
                success = false,
                error = "NOT_ENROLLED",
                warning = "KeyguardManager#isDeviceSecure() returned false"
            )
        }

        if (isAuthenticating) {
            LocalAuthenticationResult(success = false, error = "APP_CANCEL", warning = null)
        }

        val promptMessage = if (options.containsKey("promptMessage")) {
            options["promptMessage"] as String?
        } else {
            ""
        }

        val cancelLabel = if (options.containsKey("cancelLabel")) {
            options["cancelLabel"] as String?
        } else {
            ""
        }

        val disableDeviceFallback = if (options.containsKey("disableDeviceFallback")) {
            options["disableDeviceFallback"] as Boolean?
        } else {
            false
        }

        val requireConfirmation = options["requireConfirmation"] as? Boolean ?: true

        isAuthenticating = true

        // should prompt biometric resolver here


        return LocalAuthenticationResult(success = false, error = null, warning = null)
    }

    fun cancelAuthenticate() {
        biometricPrompt?.cancelAuthentication()
        isAuthenticating = false
    }

    companion object {
        private const val BIOMETRY_RESOLVER_FRAGMENT_TAG = "BiometricControllerResolver"
    }

    init {
        this.fragmentManager = (context as FragmentActivity).supportFragmentManager
    }


}


