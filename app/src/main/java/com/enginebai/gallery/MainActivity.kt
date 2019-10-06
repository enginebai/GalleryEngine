package com.enginebai.gallery

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.enginebai.gallery.base.BaseActivity
import com.enginebai.gallery.model.MimeType
import com.enginebai.gallery.ui.GalleryEngine
import com.enginebai.gallery.ui.REQUEST_SELECT_MEDIA
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe {
                buttonSingleSelection.setOnClickListener {
                    GalleryEngine.Builder()
                        .choose(MimeType.IMAGE)
                        .forResult(this)
                }
                buttonMultipleSelection.setOnClickListener {
                    GalleryEngine.Builder()
                        .multiple(true)
                        .maxSelect(5)
                        .forResult(this)
                }
            }.apply { addDisposable(this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_MEDIA && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, GalleryEngine.getSelectMediaPaths(data).toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
