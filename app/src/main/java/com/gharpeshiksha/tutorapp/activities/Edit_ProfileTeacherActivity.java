package com.gharpeshiksha.tutorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.databinding.ActivityEditTeacherprofileBinding;

public class Edit_ProfileTeacherActivity extends AppCompatActivity {

    ActivityEditTeacherprofileBinding binding;
    private  static final int pic_id = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTeacherprofileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.openCamera.setOnClickListener(view1 -> {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, pic_id);
        });
}
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            binding.clickImage.setImageBitmap(photo);
        }
    }

}