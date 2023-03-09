package com.example.galeriaplus

import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galeriaplus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var picsAdapter: PicsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (permissions()) {
            val picturesList=loadAllPictures()
            Toast.makeText(this, picturesList.size.toString(), Toast.LENGTH_SHORT).show()
            picsAdapter = PicsAdapter(this, picturesList.toList())
            binding.rvImages.adapter=picsAdapter
            binding.rvImages.layoutManager=GridLayoutManager(this, 3)
        } else {
            askForPermission()
        }
    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            100)
    }

    private fun permissions(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_GRANTED)
    }

    private fun loadAllPictures() :List<PicModel> {

        var tempList= mutableListOf<PicModel>()

        val uri = when {
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q-> {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else->{
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        }

        val projection =
            arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )

        contentResolver.query(uri, projection, null, null, null)
            .use {cursor->
                cursor?.let {
                    while (cursor.moveToNext()){
                       val picId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                       val displaName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, picId)
                        val model = PicModel(picId,displaName,uri)
                        tempList.add(model)
                    }
                }

            }
        return tempList
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==100){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                loadAllPictures()
            }
        } else {
            askForPermission()
        }
    }
}