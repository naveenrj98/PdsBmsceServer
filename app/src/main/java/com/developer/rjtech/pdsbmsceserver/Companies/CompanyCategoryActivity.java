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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.developer.rjtech.pdsbmsceserver.Common.Common;
import com.developer.rjtech.pdsbmsceserver.Interface.ItemClickListener;
import com.developer.rjtech.pdsbmsceserver.Models.CompanyCategory;
import com.developer.rjtech.pdsbmsceserver.Models.Year;
import com.developer.rjtech.pdsbmsceserver.R;
import com.developer.rjtech.pdsbmsceserver.ViewHolder.CompanyCategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompanyCategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //---------Menu ViewHolder--------
    FirebaseDatabase database;
    DatabaseReference ccategory;
    RecyclerView recycler_category;
    RecyclerView.LayoutManager layoutManager;
    TextView textFullName;
    FirebaseRecyclerAdapter<CompanyCategory, CompanyCategoryViewHolder> adptor;

    FirebaseStorage storage;
    StorageReference storageReference;

    FloatingActionButton fab;

    EditText editName;
    Button btnselect, btnuplaod;


    CompanyCategory newCategory;
    Uri saveUrl;
    private final int PICK_IMAGE_REQUEST = 71;
    DrawerLayout drawer;


    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_category);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();


            }
        });



        //Auth
        database = FirebaseDatabase.getInstance();
        ccategory = database.getReference("CompanyYear").child(Common.yearSelected)
        .child("details").child("CompanyCategory");


        recycler_category = findViewById(R.id.recycler_company_category);
        recycler_category.setHasFixedSize(true);


        recycler_category.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorgreen,
                R.color.color_option_menu,
                R.color.darkRed);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMenu();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadMenu();
            }
        });

        //Search
//        materialSearchBar = findViewById(R.id.searchBar);
//        materialSearchBar.setHint("Enter Your Company");
//        loadSuggest();
//        materialSearchBar.setLastSuggestions(suggestList);
//        materialSearchBar.setCardViewElevation(10);
//        materialSearchBar.addTextChangeListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                List<String> suggest = new ArrayList<>();
//                for (String search : suggestList) {
//
//                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
//
//                        suggest.add(search);
//                    }
//                    materialSearchBar.setLastSuggestions(suggest);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
//            @Override
//            public void onSearchStateChanged(boolean enabled) {
//                if (!enabled) {
//                    recycler_category.setAdapter(adptor);
//                }
//            }
//
//            @Override
//            public void onSearchConfirmed(CharSequence text) {
//
//                //starSearch(text);
//
//            }
//
//            @Override
//            public void onButtonClicked(int buttonCode) {
//
//            }
//        });
//


    }

//    private void starSearch(CharSequence text) {
//
//        searchadptor = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,
//                R.layout.menu_item,
//                MenuViewHolder.class,
//                category.orderByChild("Name").equalTo(text.toString())) {
//            @Override
//            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {
//
//                menuViewHolder.textMenuName.setText(category.getName());
//                Picasso.with(getApplicationContext()).load(category.getImage())
//                        .into(menuViewHolder.imageView);
//                final Category clickItem = category;
//                menuViewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//
//                        Toast.makeText(getApplicationContext(), "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), CompanyDetailsActivity.class);
//                        intent.putExtra("CategoryId", searchadptor.getRef(position).getKey());
//                        startActivity(intent);
//                    }
//                });
//
//            }
//        };
//        recycler_menu.setAdapter(searchadptor);
//
//    }

//    private void loadSuggest() {
//
//        ccategory.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//
//                    Category item = postSnapshot.getValue(Category.class);
//                    suggestList.add(item.getName());
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
    private void loadMenu() {

        FirebaseRecyclerOptions<CompanyCategory> options = new FirebaseRecyclerOptions.Builder<CompanyCategory>()
                .setQuery(ccategory, CompanyCategory.class)
                .build();

        adptor = new FirebaseRecyclerAdapter<CompanyCategory, CompanyCategoryViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CompanyCategoryViewHolder holder, int position, @NonNull CompanyCategory model) {

                Picasso.with(getApplicationContext()).load(model.getImage())
                        .into(holder.imageView);
                final CompanyCategory clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Toast.makeText(getApplicationContext(), "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), CompanyListActivity.class);
                        intent.putExtra("CategoryId", adptor.getRef(position).getKey());
                        Common.companyCategorySelected = adptor.getRef(position).getKey();
                        startActivity(intent);

                    }
                });


            }

            @NonNull
            @Override
            public CompanyCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.company_category_item, parent, false);
                return new CompanyCategoryViewHolder(itemView);
            }
        };
        adptor.startListening();

        recycler_category.setAdapter(adptor);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMenu();
    }

    @Override
    public void onStop() {
        super.onStop();
        adptor.stopListening();
    }
    private void showDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CompanyCategoryActivity.this);
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
                if(newCategory  !=null){

                    ccategory.child(editName.getText().toString()).setValue(newCategory);

                    Snackbar.make(drawer,"New CompanyYear"+newCategory.getName()+"was added SuccessFully",Snackbar.LENGTH_SHORT).show();

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

            final ProgressDialog progressDialog = new ProgressDialog(CompanyCategoryActivity.this);
            progressDialog.setMessage("Uploading........");
            progressDialog.show();


            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            progressDialog.dismiss();
                            Toast.makeText(CompanyCategoryActivity.this,"Uplaod Succesfull", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newCategory = new CompanyCategory(editName.getText().toString(), uri.toString());

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(CompanyCategoryActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

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


        ccategory.child(key).removeValue();
        Toast.makeText(CompanyCategoryActivity.this,"Item deleted RJ", Toast.LENGTH_SHORT).show();

    }

    private void showUpdateDialog(final String key, final CompanyCategory item) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CompanyCategoryActivity.this);
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
                ccategory.child(key).setValue(item);
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

    private void ChangeImage(final CompanyCategory item) {


        if (saveUrl != null) {

            final ProgressDialog progressDialog = new ProgressDialog(CompanyCategoryActivity.this);
            progressDialog.setMessage("Uploading........");
            progressDialog.show();


            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            progressDialog.dismiss();
                            Toast.makeText(CompanyCategoryActivity.this,"Uplaod Succesfull", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(CompanyCategoryActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

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
