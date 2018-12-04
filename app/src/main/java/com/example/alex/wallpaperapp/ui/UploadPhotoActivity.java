package com.example.alex.wallpaperapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alex.wallpaperapp.R;
import com.example.alex.wallpaperapp.model.Category;
import com.example.alex.wallpaperapp.model.UserImage;
import com.example.alex.wallpaperapp.model.WallPaperItem;
import com.example.alex.wallpaperapp.utils.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadPhotoActivity extends AppCompatActivity {
    private static final String TAG = "UploadPhotoActivity";

    @BindView(R.id.uplad_image)
    ImageView uploadImage;

    @BindView(R.id.upload_btn)
    Button btnUpload;

    @BindView(R.id.choose_btn)
    Button btnChoose;

    FirebaseStorage mStorage;
    StorageReference storageReference;

    private Uri filePath;

    String categoryIdSelected="";

    @BindView(R.id.spinner)
    MaterialSpinner spinner;

    Map<String, String> spinnerMap= new HashMap<>();

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        ButterKnife.bind(this);


        //firebase
        mStorage= FirebaseStorage.getInstance();
        storageReference= mStorage.getReference();

        currentUser =FirebaseAuth.getInstance().getCurrentUser();

//        loadCategoryToSpinner();


        //you can use the previous feature,, but for now i will use something defferint..
        /******************************************************/
        spinner.setVisibility(View.GONE);


        /**********************************************************/


    }

    @OnClick(R.id.choose_btn)
    public void chooseImage(){

        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Choose Picture .."),Common.CHOOSE_IMAGE_REQUEST_CODE);

    }


// i need this code for future ,,
//    @OnClick(R.id.upload_btn)
//    public void upload(){
//        if (categoryIdSelected.equals("CategoryKey")){
//            Toast.makeText(this, "please select Category ", Toast.LENGTH_SHORT).show();
//
//
//        }else if (categoryIdSelected.equals("")){
//            //this condition .. if the first time click on upload .. the categoryIdSelected will be empty
//            Toast.makeText(this, "please select Category E "+categoryIdSelected, Toast.LENGTH_SHORT).show();
//
//
//        }else {
//            if (filePath !=null){
//                final ProgressDialog  progressDialog= new ProgressDialog(this);
//                progressDialog.setTitle("Uploading..");
//                progressDialog.show();
//                String pathImage="images/" + UUID.randomUUID().toString();
//
//                StorageReference  reference= storageReference.child(pathImage);
//
//                reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        progressDialog.dismiss();
//                         String imgUrl=taskSnapshot.getDownloadUrl().toString();
//
//                        saveUrlToCategory(categoryIdSelected,imgUrl );
//
//                        //also insert into user database
//                        saveToOwnUserImage( imgUrl);
//                        finish();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(UploadPhotoActivity.this, "Error While uploading Image.."+e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//
//                        progressDialog.setMessage("Uploaded :  "+(int)progress + "%");
//                    }
//                });
//
//
//            }
//        }
//
//
//
//    }



    @OnClick(R.id.upload_btn)
    public void upload(){

            if (filePath !=null){
                final ProgressDialog  progressDialog= new ProgressDialog(this);
                progressDialog.setTitle("Uploading..");
                progressDialog.show();
                String pathImage="images/" + UUID.randomUUID().toString();

                StorageReference  reference= storageReference.child(pathImage);

                reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        progressDialog.dismiss();
                        String imgUrl=taskSnapshot.getDownloadUrl().toString();

                        saveToOwnUserImage( imgUrl);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadPhotoActivity.this, "Error While uploading Image.."+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        progressDialog.setMessage("Uploaded :  "+(int)progress + "%");
                    }
                });


            }




    }

    private void saveToOwnUserImage(final String imgUrl) {


        FirebaseDatabase.getInstance().getReference("UsersImages")
                .child(currentUser.getUid())
                .push()
                .setValue(new UserImage(imgUrl))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(UploadPhotoActivity.this, "Successfully saved in Your Image ..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUrlToCategory(String categoryIdSelected, String imageUrl) {


            FirebaseDatabase.getInstance().getReference(Common.REF_WALLPAPER).push().setValue(
                    new WallPaperItem(imageUrl,categoryIdSelected))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UploadPhotoActivity.this, "Success Upload Image ..", Toast.LENGTH_SHORT).show();
                        }
                    });

    }


    private void loadCategoryToSpinner() {

        //we will store key and name of the category .. so we need map !!
        FirebaseDatabase.getInstance().getReference(Common.REF_CATEGORY_BACKGROUND)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Category category = dataSnapshot1.getValue(Category.class);
                            String key= dataSnapshot1.getKey();
                            assert category != null;
                            spinnerMap.put(key,category.getName());
                        }

                        //we need defualt value in the spinner ...so i will add the 0 index some defult string..
                        //i will use with another array
                        Object [] valueArray = spinnerMap.values().toArray();//will get all values
                        ArrayList<Object> valueList = new ArrayList<>();
                        valueList.add(0,"Category");
                        valueList.addAll(Arrays.asList(valueArray));

                        spinner.setItems(valueList);
                        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                                Object [] keyArray = spinnerMap.keySet().toArray();//will get all keys
                                ArrayList<Object> keyList = new ArrayList<>();
                                keyList.add(0,"CategoryKey");
                                keyList.addAll(Arrays.asList(keyArray));
                                categoryIdSelected=keyList.get(position).toString();



                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==Common.CHOOSE_IMAGE_REQUEST_CODE){
            if (resultCode ==RESULT_OK){
                if (data !=null){
                    if (data.getData()!=null){
                        filePath= data.getData();
                        try {
                            //get bitmap to set to imageview
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                        uploadImage.setImageBitmap(bitmap);
                        btnUpload.setEnabled(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }
}
