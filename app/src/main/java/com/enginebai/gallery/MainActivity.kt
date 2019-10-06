package com.enginebai.gallery

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.enginebai.gallery.base.BaseActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe {
                buttonSingleSelection.setOnClickListener {
                    com.enginebai.gallery.ui.GalleryEngine.Builder()
                        .choose(com.enginebai.gallery.model.MimeType.IMAGE)
                        .imageMaxSize(1024 * 1024 * 20)
                        .forResult(this)
                }
                buttonMultipleSelection.setOnClickListener {
                    com.enginebai.gallery.ui.GalleryEngine.Builder()
                        .choose(com.enginebai.gallery.model.MimeType.ALL)
                        .multiple(true)
                        .maxSelect(5)
                        .imageMaxSize(1024)
                        .videoMaxSecond(60)
                        .videoMinSecond(3)
                        .forResult(this, com.enginebai.gallery.ui.REQUEST_SELECT_MEDIA)
                }
            }.apply { addDisposable(this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == com.enginebai.gallery.ui.REQUEST_SELECT_MEDIA && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, data?.getStringArrayListExtra(com.enginebai.gallery.model.KEY_MEDIA_LIST)?.toString() ?: "null", Toast.LENGTH_SHORT).show()
        }
    }
}
