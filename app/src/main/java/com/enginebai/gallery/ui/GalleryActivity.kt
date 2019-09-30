package com.enginebai.gallery.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.enginebai.gallery.R
import com.enginebai.gallery.base.BaseActivity
import com.enginebai.gallery.model.AlbumSetting
import com.enginebai.gallery.model.MimeType
import org.koin.androidx.viewmodel.ext.android.viewModel

const val REQUEST_SELECT_MEDIA = 88
private const val KEY_ALBUM_SETTING = "albumSetting"

class GalleryActivity : BaseActivity() {

    private val viewModel by viewModel<GalleryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        viewModel.setting = intent.getSerializableExtra(KEY_ALBUM_SETTING) as AlbumSetting

        supportFragmentManager.beginTransaction()
            .add(
                R.id.mediaContainer,
                MediaSelectFragment.newInstance()
            )
            .commit()
    }

    class Builder {
        private val setting = AlbumSetting()

        fun choose(mimeType: MimeType = MimeType.ALL): Builder {
            setting.mimeType = mimeType
            return this
        }

        fun multiple(multipleSelect: Boolean = true): Builder {
            setting.multipleSelection = multipleSelect
            return this
        }

        fun maxSelect(max: Int): Builder {
            check(max > 0) { "The maximum selection should be greater than 0."}
            setting.maxSelection = max
            return this
        }

        fun imageMaxSize(max: Long): Builder {
            setting.imageMaxSize = max
            return this
        }

        fun videoMaxSecond(max: Int): Builder {
            setting.videoMaxSecond = max
            return this
        }

        fun videoMinSecond(min: Int): Builder {
            setting.videoMinSecond = min
            return this
        }

        fun forResult(activity: Activity, requestCode: Int = REQUEST_SELECT_MEDIA) {
            activity.startActivityForResult(getIntent(activity), requestCode)
        }

        fun forResult(fragment: Fragment, requestCode: Int = REQUEST_SELECT_MEDIA) {
            fragment.context?.run {
                fragment.startActivityForResult(getIntent(this), requestCode)
            }
        }

        private fun getIntent(context: Context) = Intent(context, GalleryActivity::class.java).apply {
            putExtra(KEY_ALBUM_SETTING, setting)
        }
    }
}
