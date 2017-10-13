package com.cdk.beacon.mvp.usecase

import android.support.annotation.CallSuper
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

abstract class UseCase {
    private var subscriptions: CompositeSubscription? = CompositeSubscription()

    fun <T> execute(observable: Observable<T>, subscriber: Subscriber<T>) {
        if (subscriptions == null || subscriptions?.isUnsubscribed == true) {
            subscriptions = CompositeSubscription()
        }

        val subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)

        subscriptions?.add(subscription)
    }

    @CallSuper
    fun clear() {
        subscriptions?.clear()
    }
}
