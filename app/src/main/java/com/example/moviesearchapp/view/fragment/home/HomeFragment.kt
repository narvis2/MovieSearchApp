package com.example.moviesearchapp.view.fragment.home

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.MovieInfoModel
import com.example.moviesearchapp.R
import com.example.moviesearchapp.databinding.FragmentHomeBinding
import com.example.moviesearchapp.base.BaseFragment
import com.example.moviesearchapp.base.MyLoadStateAdapter
import com.example.moviesearchapp.utils.KeyboardUtils
import com.example.moviesearchapp.utils.observeInLifecycleStop
import com.example.moviesearchapp.utils.observeOnLifecycleStop
import com.example.moviesearchapp.view.activity.web.WebViewActivity
import com.example.moviesearchapp.view.fragment.home.adapter.MovieInfoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    R.layout.fragment_home
), View.OnFocusChangeListener, TextWatcher {
    override val viewModel: HomeViewModel by viewModels()

    private val movieInfoAdapter by lazy {
        MovieInfoAdapter()
    }

    private lateinit var scrollLayoutManager: LinearLayoutManager
    private var scrollReset = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val lastVisibleItemPosition = scrollLayoutManager.findFirstVisibleItemPosition()

            // 스크롤 시 Scroll Top Button 을 보여줍니다.
            if (lastVisibleItemPosition >= 1) {
                binding.topButton.show()
            } else {
                binding.topButton.hide()
            }
        }
    }

    private val keyboardObserver = object : KeyboardUtils.SoftKeyboardToggleListener {
        override fun onToggleSoftKeyboard(isVisible: Boolean, height: Int) {
            // keyboard 가 보이지 않을 때 searchEt 포커스를 해제합니다.
            if (!isVisible) {
                binding.searchEt.let {
                    if (it.isFocusable) {
                        it.clearFocus()
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        KeyboardUtils.addKeyboardToggleListener(requireActivity(), keyboardObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = this.viewModel
        binding.focusListener = this
        binding.textListener = this
        binding.noResultLayout.noResultText = resources.getString(R.string.str_no_search)

        binding.listView.apply {
            scrollLayoutManager = layoutManager as LinearLayoutManager

            adapter = movieInfoAdapter.withLoadStateHeaderAndFooter(
                header = MyLoadStateAdapter { movieInfoAdapter.retry() },
                footer = MyLoadStateAdapter { movieInfoAdapter.retry() }
            )

            addOnScrollListener(scrollListener)
        }

        viewModel.getMovieList.observeOnLifecycleStop(viewLifecycleOwner) {
            movieInfoAdapter.submitData(it)
        }

        movieInfoAdapter.loadStateFlow.observeOnLifecycleStop(viewLifecycleOwner) {
            viewModel.loadingType.value = when {
                // 초기 load 또는 새로고침이 실패하면 -> ERROR
                it.source.refresh is LoadState.Error && movieInfoAdapter.itemCount == 0 -> {
                    if (binding.refreshView.isRefreshing) {
                        binding.refreshView.isRefreshing = false
                    }

                    MovieLoadingType.ERROR
                }

                // List 가 비어있는 경우 -> EMPTY
                it.source.refresh is LoadState.NotLoading && movieInfoAdapter.itemCount == 0 -> {
                    if (binding.refreshView.isRefreshing) {
                        binding.refreshView.isRefreshing = false
                    }

                    MovieLoadingType.EMPTY
                }

                // Local Db 또는 Remote 에서 새로 고침이 성공한 경우 -> VIEW
                it.source.refresh is LoadState.NotLoading -> {
                    if (binding.listRefreshView.isRefreshing) {
                        binding.listRefreshView.isRefreshing = false
                    }

                    if (scrollReset) {
                        scrollReset = false
                        binding.listView.stopScroll()
                        scrollLayoutManager.scrollToPositionWithOffset(0, 0)
                    }

                    MovieLoadingType.VIEW
                }

                else -> MovieLoadingType.VIEW
            }
        }

        movieInfoAdapter.setOnItemClickListener(object : MovieInfoAdapter.OnItemClickListener {
            override fun onClick(item: MovieInfoModel, position: Int) {
                startActivity(
                    WebViewActivity.getInAppBrowserIntent(
                    activity = requireActivity(),
                    url = item.link,
                    pageTitle = "영화 상세 정보",
                    showTitle = true
                ))
            }
        })

        // IME_ACTION_SEARCH : 키보드 search 모양 클릭 시 Listener 등록
        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.onSearchClick()
                true
            } else {
                false
            }
        }

        initViewModelCallback()
    }

    private fun initViewModelCallback() = with (viewModel) {
        scrollTop.onEach {
            binding.listView.stopScroll()
            scrollLayoutManager.scrollToPositionWithOffset(0, 0)
        }.observeInLifecycleStop(viewLifecycleOwner)

        refresh.onEach {
            if (binding.refreshView.isRefreshing) {
                binding.refreshView.isRefreshing = false
            }

            scrollReset = true
            movieInfoAdapter.refresh()
            onScrollTop()
        }.observeInLifecycleStop(viewLifecycleOwner)

        search.onEach {
            onRefreshList()
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.searchEt -> {
                    viewModel.searchFocusView.value = hasFocus
                }
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        viewModel.notBlankContents.value = if (!s.isNullOrEmpty()) {
            viewModel.searchFocusView.value = true
            true
        } else {
            false
        }
    }

    override fun onDetach() {
        KeyboardUtils.removeKeyboardToggleListener(keyboardObserver)
        super.onDetach()
    }
}