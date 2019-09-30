package com.enginebai.gallery

import android.Manifest
import android.os.Bundle
import com.enginebai.gallery.base.BaseActivity
import com.enginebai.gallery.model.MimeType
import com.enginebai.gallery.ui.GalleryActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe {
                buttonOpenGallery.setOnClickListener {
                    GalleryActivity.Builder()
                        .choose(MimeType.IMAGE)
                        .forResult(this)
                }
            }.apply { addDisposable(this) }
    }
}
