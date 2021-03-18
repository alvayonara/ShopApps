package com.alvayonara.shopapps.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            setup()
        }
    }

    abstract fun loadInjector()
    abstract fun setup()

    protected fun showToast(message: String) =
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()

    protected open fun setupFormValidation() {}
    protected open fun setupView() {}
    protected open fun subscribeViewModel() {}

    protected fun <T> LiveData<T>.onLiveDataResult(action: (T) -> Unit) {
        observe(this@BaseDialogFragment) { data ->
            data?.let(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}