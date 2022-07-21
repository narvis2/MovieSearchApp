package com.example.moviesearchapp.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * @author choi young jun
 * FlowObserver -> lifecycle 이 onStart가 되면 구독 시작, onStop이 되면 구독 취소
 */
class FlowObserverInStop<T>(
    lifecycleOwner: LifecycleOwner,
    private val flow: Flow<T>,
    private val collector: suspend (T) -> Unit
) {

    private var job: Job? = null

    init {
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    job = source.lifecycleScope.launch {
                        flow.collect {
                            collector(it)
                        }
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    job?.cancel()
                    job = null
                }
                else -> {}
            }
        })
    }
}

inline fun <reified T> Flow<T>.observeOnLifecycleStop(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) = FlowObserverInStop(lifecycleOwner, this, collector)

// .onEach{ } 사용할때 사용
inline fun <reified T> Flow<T>.observeInLifecycleStop(
    lifecycleOwner: LifecycleOwner
) = FlowObserverInStop(lifecycleOwner, this) {}

/**
 * @author choi young jun
 * FlowObserver -> lifecycle 이 onStart 가 되면 구독 시작, ON_DESTROY 가 되면 구독 취소
 * 앱위에 다른 구성 요소가 들어가 이전 Fragment 의 Lifecycle 이 onStop()이 되면 옵저빙을 하지 않아
 * 그럴 경우에 FlowObserverInStop 사용
 * 즉, 해당 View 의 Lifecycle 이 onStop()인 상태에서도 Observing 을 가능하게 하기위해 사용.
 */
class FlowObserverInDestroy<T>(
    lifecycleOwner: LifecycleOwner,
    private val flow: Flow<T>,
    private val collector: suspend (T) -> Unit
) {
    private var job: Job? = null
    init {
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    job = source.lifecycleScope.launch {
                        flow.collect {
                            collector(it)
                        }
                    }
                }
                Lifecycle.Event.ON_DESTROY -> {
                    job?.cancel()
                    job = null
                }
                else -> {}
            }
        })
    }
}

inline fun <reified T> Flow<T>.observeOnLifecycleDestroy(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) = FlowObserverInDestroy(lifecycleOwner, this, collector)

inline fun <reified T> Flow<T>.observeInLifecycleDestroy(
    lifecycleOwner: LifecycleOwner
) = FlowObserverInDestroy(lifecycleOwner, this) {}