package com.developer.rjtech.pdsbmsceserver.Companies;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.rjtech.pdsbmsceserver.Common.Common;
import com.developer.rjtech.pdsbmsceserver.Interface.ItemClickListener;
import com.developer.rjtech.pdsbmsceserver.Models.Year;
import com.developer.rjtech.pdsbmsceserver.R;
import com.developer.rjtech.pdsbmsceserver.ViewHolder.YearViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class CompanyYearActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseDatabase database;
    DatabaseReference year;

    FirebaseStorage storage;
    StorageReference storageReference;

    TextView textFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Year, YearViewHolder> adptor;

    EditText editName;
    Button btnselect, btnuplaod;


    Year newYear;
    Uri saveUrl;
    private final int PICK_IMAGE_REQUEST = 71;
    DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();


            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //Auth
        database = FirebaseDatabase.getInstance();
        year = database.getReference("CompanyYear");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        View headerView = navigationView.getHeaderView(0);
        textFullName = headerView.findViewById(R.id.textFullName);

        textFullName.setText(Common.currentUser.getName());


        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);


        loadYear();


    }

    private void loadYear() {

        FirebaseRecyclerOptions<Year> options = new FirebaseRecyclerOptions.Builder<Year>()
                .setQuery(year, Year.class)
                .build();

        adptor = new FirebaseRecyclerAdapter<Year, YearViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull YearViewHolder holder, int position, @NonNull Year model) {

                holder.textMenuName.setText(model.getName());
                Picasso.with(getApplicationContext()).load(model.getImage())
                        .into(holder.imageView);
                final Year clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Toast.makeText(getApplicationContext(), "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), CompanyCategoryActivity.class);
//                        intent.putExtra("CategoryId", adptor.getRef(position).getKey());
                        Common.yearSelected = adptor.getRef(position).getKey();
                        startActivity(intent);


                    }
                });


            }

            @NonNull
            @Override
            public YearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item, parent, false);
                return new YearViewHolder(itemView);
            }
        };
        adptor.startListening();
            adptor.notifyDataSetChanged();
        recycler_menu.setAdapter(adptor);

    }



    private void showDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CompanyYearActivity.this);
        alertDialog.setTitle("Add New Company Year");
        alertDialog.setMessage("Please Fill Complete Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout, null);


        editName = add_menu_layout.findViewById(R.id.editName);
        btnselect = add_menu_layout.findViewById(R.id.btnselect);
        btnuplaod = add_menu_layout.findViewById(R.id.btnupload);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 chooseImage();
            }
        });

        btnuplaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    if(newYear  !=null){

                        year.child(editName.getText().toString()).setValue(newYear);

                        Snackbar.make(drawer,"New CompanyYear"+newYear.getName()+"was added SuccessFully",Snackbar.LENGTH_SHORT).show();

                    }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        alertDialog.show();


    }

    private void uploadImage() {


        if (saveUrl != null) {

            final ProgressDialog progressDialog = new ProgressDialog(CompanyYearActivity.this);
            progressDialog.setMessage("Uploading........");
            progressDialog.show();


            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            progressDialog.dismiss();
                            Toast.makeText(CompanyYearActivity.this,"Uplaod Succesfull", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newYear = new Year(editName.getText().toString(), uri.toString()," ");

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(CompanyYearActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uplaoded...."+progress+"%");

                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null && data != null) {

            saveUrl = data.getData();
            btnselect.setText("Image Selected");

        }
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture of food"),PICK_IMAGE_REQUEST);


    }

//    private void loadMenu(){
//
//
//
//
//        adptor = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
//            @Override
//            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
//
//                viewHolder.textMenuName.setText(model.getName());
//                Picasso.with(getBaseContext()).load(model.getImage())
//                        .into(viewHolder.imageView);
//                final Category clickItem = model;
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//
//
////                        Log.d("MenuViewHolder.this", "click on menu");
////
////                        Toast.makeText(CompanyYearActivity.this,""+clickItem.getName(), Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(CompanyYearActivity.this, FoodList.class);
////                        intent.putExtra("CategoryId",adptor.getRef(position).getKey());
////                        startActivity(intent);
//
//                    }
//                });
//
////                viewHolder.setItemClickListener(new ItemClickListener() {
////                    @Override
////                    public void onClick(View view, int position, boolean isLongClick) {
////
////                        Toast.makeText(CompanyYearActivity.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(CompanyYearActivity.this, FoodList.class);
////                        intent.putExtra("CategoryId",adptor.getRef(position).getKey());
////                        startActivity(intent);
////
////                    }
////                });
//            }
//        };
//        adptor.notifyDataSetChanged();
//        recycler_menu.setAdapter(adptor);
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the CompanyYearActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_orders) {
//            Intent intent = new Intent(CompanyYearActivity.this,OrderStatus.class);
//            startActivity(intent);




        } else if (id == R.id.nav_signout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        if(item.getTitle().equals(Common.UPDATE))
        {

            showUpdateDialog(adptor.getRef(item.getOrder()).getKey(), adptor.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {


            deleteCategory(adptor.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {




        year.child(key).removeValue();
        Toast.makeText(CompanyYearActivity.this,"Item deleted RJ", Toast.LENGTH_SHORT).show();

    }

    private void showUpdateDialog(final String key, final Year item) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CompanyYearActivity.this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("Please Fill Complete Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout, null);


        editName = add_menu_layout.findViewById(R.id.editName);
        btnselect = add_menu_layout.findViewById(R.id.btnselect);
        btnuplaod = add_menu_layout.findViewById(R.id.btnupload);

        editName.setText(item.getName());

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                chooseImage();
            }
        });

        btnuplaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeImage(item);
            }
        });


        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
              item.setName(editName.getText().toString());
                year.child(key).setValue(item);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        alertDialog.show();



    }

    private void ChangeImage(final Year item) {


        if (saveUrl != null) {

            final ProgressDialog progressDialog = new ProgressDialog(CompanyYearActivity.this);
            progressDialog.setMessage("Uploading........");
            progressDialog.show();


            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            progressDialog.dismiss();
                            Toast.makeText(CompanyYearActivity.this,"Uplaod Succesfull", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    item.setImage(uri.toString());

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(CompanyYearActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uplaoded...."+progress+"%");

                        }
                    });
        }
    }


}
