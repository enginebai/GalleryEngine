package com.enginebai.gallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.enginebai.gallery.R
import com.enginebai.gallery.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_media_select.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MediaSelectFragment : BaseFragment() {

    private val viewModel by sharedViewModel<GalleryViewModel>()
    private val mediaAdapter: MediaAdapter by lazy {
        MediaAdapter {
            viewModel.selectMedia(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_media_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with (list) {
            addItemDecoration(GridSpaceDecoration(context, R.dimen.media_margin))
            layoutManager = GridLayoutManager(list.context, 3)
            adapter = mediaAdapter
        }
        viewModel.loadAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .apply { addDisposable(this) }
        viewModel.currentAlbumItem
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                mediaAdapter.mediaList.addAll(it.mediaList)
                mediaAdapter.notifyDataSetChanged()
            }
            .subscribe()
            .apply { addDisposable(this) }
    }

    companion object {
        fun newInstance(): Fragment = MediaSelectFragment()
    }
}