package com.cdk.beacon.ui

interface BaseView {
    fun showLoading()

    fun hideLoading()

    fun showError(error: Throwable)
}
