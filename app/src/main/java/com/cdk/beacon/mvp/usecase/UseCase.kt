package com.cdk.beacon.mvp.usecase

import androidx.annotation.CallSuper
import rx.Completable
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action0
import rx.functions.Action1
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

    protected fun execute(useCaseCompletable: Completable, onCompleteAction: Action0, errorAction: Action1<Throwable>) {
        if (subscriptions == null || subscriptions?.isUnsubscribed == true) {
            subscriptions = CompositeSubscription()
        }
        val subscription = useCaseCompletable.subscribeOn(Schedulers.io())
                .onErrorResumeNext { throwable -> Completable.error(throwable) }
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onCompleteAction, errorAction)
        subscriptions?.add(subscription)
    }

    @CallSuper
    fun clear() {
        subscriptions?.clear()
    }
}
