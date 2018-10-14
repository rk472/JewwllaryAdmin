package com.smarttersstudio.jewellaryadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class AddItemActivity extends AppCompatActivity {
    private ImageView img;
    private EditText nameText,soldText;
    private boolean hasPic=false;
    private byte[] mbyte;
    private String gos,category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        img=findViewById(R.id.item_upload_image);
        nameText=findViewById(R.id.item_upload_name);
        soldText=findViewById(R.id.item_upload_sold);
        gos=getIntent().getStringExtra("gos");
        category=getIntent().getStringExtra("category");
    }

    public void upload(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File thumb_filePath = new File(resultUri.getPath());
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(10)
                            .compressToBitmap(thumb_filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                mbyte= byteArrayOutputStream.toByteArray();
                img.setImageBitmap(thumb_bitmap);
                hasPic=true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(AddItemActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void add(View view) {

        if(hasPic){
            final String name=nameText.getText().toString();
            if(TextUtils.isEmpty(name)){
                Toast.makeText(this, "You must fill the item name", Toast.LENGTH_SHORT).show();
            }else{
                final String sold=soldText.getText().toString();
                if(TextUtils.isEmpty(sold)){
                    Toast.makeText(this, "You must enter some sold number", Toast.LENGTH_SHORT).show();
                }else{
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Wait while We are updating the item..");
                    progressDialog.setTitle("Please Wait");
                    progressDialog.show();
                    final String key=FirebaseDatabase.getInstance().getReference().push().getKey();
                    StorageReference thumbFilePath=FirebaseStorage.getInstance().getReference().child(gos).child(key);
                    thumbFilePath.putBytes(mbyte).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                            if (thumb_task.isSuccessful()) {
                                final String downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                                Map<String,Object> m=new HashMap<>();
                                m.put("name",name);
                                m.put("sold",sold);
                                m.put("image",downloadUrl);
                                FirebaseDatabase.getInstance().getReference().child("items").child(gos).child(category).push().updateChildren(m).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddItemActivity.this, "Item Added successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddItemActivity.this, thumb_task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }else{
            Toast.makeText(this, "You must select a pic in order to add the item", Toast.LENGTH_SHORT).show();
        }
    }
}
