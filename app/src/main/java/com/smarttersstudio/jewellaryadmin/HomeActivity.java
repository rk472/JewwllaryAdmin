package com.smarttersstudio.jewellaryadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarttersstudio.jewellaryadmin.fragments.EnquiryFragment;
import com.smarttersstudio.jewellaryadmin.fragments.GalleryFragment;
import com.smarttersstudio.jewellaryadmin.fragments.HomeFragment;
import com.smarttersstudio.jewellaryadmin.fragments.ItemFragment;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
               DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment(),"home");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getSupportFragmentManager().findFragmentById(R.id.container).getTag().equals("home")){
                super.onBackPressed();
            }else{
                loadFragment(new HomeFragment(),"home");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String tag="others";
        Fragment f;
        if (id == R.id.nav_home) {
            f=new HomeFragment();
            tag="home";
        } else if (id == R.id.nav_gallery) {
            f=new GalleryFragment();
            tag="gallery";
        } else if (id == R.id.nav_enquiry) {
            f=new EnquiryFragment();
        }else{
            f=new ItemFragment();
        }
        loadFragment(f,tag);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void loadFragment(Fragment f,String tag){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,f,tag);
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(getSupportFragmentManager().findFragmentById(R.id.container).getTag().equals("gallery")) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Wait while We are updating the Picture..");
                    progressDialog.setTitle("Please Wait");
                    progressDialog.show();
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
                    final byte[] mbyte = byteArrayOutputStream.toByteArray();
                    final StorageReference thumbFilePath = FirebaseStorage.getInstance().getReference().child("gallery").child(id + ".jpg");
                    thumbFilePath.putBytes(mbyte).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                            if (thumb_task.isSuccessful()) {
                                final String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                                FirebaseDatabase.getInstance().getReference().child("gallery").push().child("image").setValue(thumb_downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            Toast.makeText(HomeActivity.this, "Profile Picture Updated Successfully..", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(HomeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(HomeActivity.this, thumb_task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
