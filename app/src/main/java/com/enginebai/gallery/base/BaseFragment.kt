package com.enginebai.gallery.base

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment : Fragment() {
    private val disposables = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }
}