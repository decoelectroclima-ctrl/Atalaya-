package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.AdrianaDatabase
import com.example.data.SoltarSettingsEntity
import com.example.ui.auth.AuthViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AuthViewModelTest {

    private lateinit var application: Application
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setup() = runBlocking {
        application = ApplicationProvider.getApplicationContext()

        val prefs = application.getSharedPreferences("atalaya_security_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().clear().commit()

        val db = AdrianaDatabase.getDatabase(application)
        db.soltarSettingsDao().saveSettings(
            SoltarSettingsEntity(
                id = 1,
                userName = "Viajero",
                pinHash = "",
                isLoggedIn = false
            )
        )
        authViewModel = AuthViewModel(application)
        ShadowLooper.idleMainLooper()
    }

    @Test
    fun testFormStateInputs() {
        authViewModel.openAuthDialog(mode = "REGISTER")
        assertTrue(authViewModel.uiState.value.isAuthDialogVisible)
        assertEquals("REGISTER", authViewModel.uiState.value.authDialogMode)

        authViewModel.setAuthName("Alejandro")
        assertEquals("Alejandro", authViewModel.uiState.value.authNameInput)

        authViewModel.setPin("1234")
        assertEquals("1234", authViewModel.uiState.value.pinInput)

        authViewModel.setConfirmPin("1234")
        assertEquals("1234", authViewModel.uiState.value.confirmPinInput)

        authViewModel.closeAuthDialog()
        assertFalse(authViewModel.uiState.value.isAuthDialogVisible)
    }

    @Test
    fun testRegisterPinMismatch() {
        authViewModel.openAuthDialog(mode = "REGISTER")
        authViewModel.setPin("1234")
        authViewModel.setConfirmPin("5678")

        var successResult = true
        var messageResult = ""
        authViewModel.registerPin { success, msg ->
            successResult = success
            messageResult = msg
        }
        ShadowLooper.idleMainLooper()

        assertFalse(successResult)
        assertEquals("Los PINs no coinciden. Vuelve a introducirlos.", messageResult)
    }

    @Test
    fun testRegisterPinIncompleteLength() {
        authViewModel.openAuthDialog(mode = "REGISTER")
        authViewModel.setPin("12")
        authViewModel.setConfirmPin("12")

        var successResult = true
        var messageResult = ""
        authViewModel.registerPin { success, msg ->
            successResult = success
            messageResult = msg
        }
        ShadowLooper.idleMainLooper()

        assertFalse(successResult)
        assertEquals("El PIN debe tener exactamente 4 dígitos numéricos.", messageResult)
    }
}
