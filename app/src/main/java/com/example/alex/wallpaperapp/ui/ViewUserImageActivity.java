package com.example.alex.wallpaperapp.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alex.wallpaperapp.R;
import com.example.alex.wallpaperapp.utils.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewUserImageActivity extends AppCompatActivity {


    @BindView(R.id.fabUserDelete)
    com.github.clans.fab.FloatingActionButton fabUserDelete;

    FirebaseUser currentUser;
    @BindView(R.id.user_image_background)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_image);
        ButterKnife.bind(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        loadImage();

        fabUserDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
                finish();
            }
        });
    }

    private void loadImage() {
        Picasso.with(this).load(Common.userImage.getImageURL()).into(imageView);
    }

    private void deleteImage() {
        FirebaseDatabase.getInstance().getReference("UsersImages").child(currentUser.getUid())
                .child(Common.DELETE_KEY_IMAGE_USER).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //now delete also from storage

                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(Common.userImage.getImageURL());
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        Toast.makeText(ViewUserImageActivity.this, "The image was deleted ", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                         Toast.makeText(ViewUserImageActivity.this, "Can not deleted ..", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });



    }
}
