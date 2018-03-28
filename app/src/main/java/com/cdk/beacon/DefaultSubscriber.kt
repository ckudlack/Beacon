package com.cdk.beacon

import android.util.Log
import rx.Subscriber

open class DefaultSubscriber<T> : Subscriber<T>() {
    override fun onNext(t: T) {
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        Log.e("Error", e?.message)
    }
}