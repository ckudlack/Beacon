package com.cdk.beacon.mvp

interface BaseView {
    fun showLoading()

    fun hideLoading()

    fun showError(error: Throwable)
}
