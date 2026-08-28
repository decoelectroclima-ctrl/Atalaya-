package com.example.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BillingManager(
    private val context: Context
) : PurchasesUpdatedListener, BillingClientStateListener {

    private val TAG = "BillingManager"
    private val scope = CoroutineScope(Dispatchers.IO)

    private val billingClient = BillingClient.newBuilder(context.applicationContext)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    private val _isPremium = MutableStateFlow(false)
    val isPremium = _isPremium.asStateFlow()

    private val _premiumProductDetails = MutableStateFlow<ProductDetails?>(null)
    val premiumProductDetails = _premiumProductDetails.asStateFlow()

    private val _billingStatusMessage = MutableStateFlow<String?>(null)
    val billingStatusMessage = _billingStatusMessage.asStateFlow()

    init {
        startBillingConnection()
    }

    private fun startBillingConnection() {
        try {
            billingClient.startConnection(this)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting BillingClient connection", e)
        }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            Log.d(TAG, "BillingClient connected successfully")
            queryProductDetails()
            queryExistingPurchases()
        } else {
            Log.w(TAG, "Billing setup finished with code: ${billingResult.responseCode} - ${billingResult.debugMessage}")
        }
    }

    private fun queryProductDetails() {
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("premium_subscription")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("atalaya_pro_monthly")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            )
            .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                _premiumProductDetails.update { productDetailsList[0] }
                Log.d(TAG, "Loaded product details: ${productDetailsList[0].name}")
            } else {
                Log.d(TAG, "Product details query response: ${billingResult.responseCode}")
            }
        }
    }

    fun queryExistingPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchasesList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                var hasActiveSubscription = false
                for (purchase in purchasesList) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        hasActiveSubscription = true
                        if (!purchase.isAcknowledged) {
                            acknowledgePurchase(purchase)
                        }
                    }
                }
                _isPremium.update { hasActiveSubscription }
                Log.d(TAG, "Active subscriptions query: hasActive=$hasActiveSubscription (${purchasesList.size} found)")
            }
        }
    }

    override fun onBillingServiceDisconnected() {
        Log.w(TAG, "Billing service disconnected. Reconnecting...")
        startBillingConnection()
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "User canceled the purchase flow")
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                Log.d(TAG, "Item already owned, granting access")
                _isPremium.update { true }
            }
            else -> {
                Log.w(TAG, "Purchase failed with code: ${billingResult.responseCode} - ${billingResult.debugMessage}")
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            _isPremium.update { true }
            if (!purchase.isAcknowledged) {
                acknowledgePurchase(purchase)
            }
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            Log.d(TAG, "Purchase is pending confirmation from Google Play")
            _billingStatusMessage.update { "Tu suscripción está pendiente de confirmación bancaria." }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        scope.launch {
            billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Purchase acknowledged successfully: ${purchase.orderId}")
                } else {
                    Log.e(TAG, "Failed to acknowledge purchase: ${billingResult.debugMessage}")
                }
            }
        }
    }

    fun launchBillingFlow(activity: Activity, productDetails: ProductDetails) {
        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: ""
        
        val productDetailsParamsList = if (offerToken.isNotEmpty()) {
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )
        } else {
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
            )
        }

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(activity, flowParams)
    }

    fun restorePurchases(onComplete: (Boolean, String) -> Unit) {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchasesList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val activePurchases = purchasesList.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
                if (activePurchases.isNotEmpty()) {
                    for (purchase in activePurchases) {
                        if (!purchase.isAcknowledged) {
                            acknowledgePurchase(purchase)
                        }
                    }
                    _isPremium.update { true }
                    onComplete(true, "¡Suscripción restaurada con éxito!")
                } else {
                    onComplete(false, "No se encontraron suscripciones activas vinculadas a tu cuenta de Google Play.")
                }
            } else {
                onComplete(false, "Error al conectar con Google Play (${billingResult.debugMessage})")
            }
        }
    }
}
