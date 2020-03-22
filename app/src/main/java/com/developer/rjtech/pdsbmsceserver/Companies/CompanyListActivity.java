package com.developer.rjtech.pdsbmsceserver.Companies;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.developer.rjtech.pdsbmsceserver.Common.Common;
import com.developer.rjtech.pdsbmsceserver.Interface.ItemClickListener;
import com.developer.rjtech.pdsbmsceserver.Models.CompanyCategory;
import com.developer.rjtech.pdsbmsceserver.Models.CompanyList;
import com.developer.rjtech.pdsbmsceserver.R;
import com.developer.rjtech.pdsbmsceserver.ViewHolder.CompanyListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;


public class CompanyListActivity extends AppCompatActivity {



    EditText editName,et_cgpa, et_role,et_visitdate, et_location;
    Spinner spin_duration, spin_work_time, spin_job_type,spin_eligible_department, spin_offers;
    Button btnselect, btnuplaod;
    CompanyCategory newcompany;
    Uri saveUrl;
    private final int PICK_IMAGE_REQUEST = 71;


    FirebaseStorage storage;
    StorageReference storageReference;

    FloatingActionButton fab;

    RelativeLayout roottLayout;

    TextView mItemSelected;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();








    //---------Menu ViewHolder--------
    FirebaseDatabase database;
    DatabaseReference clist;
    RecyclerView recycler_list;
    RecyclerView.LayoutManager layoutManager;
    TextView textFullName;
    FirebaseRecyclerAdapter<CompanyCategory, CompanyListViewHolder> adptor;
    String duration,worktime,job_type, department, offers;


    String categoryId="";
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);

        //Auth
        database = FirebaseDatabase.getInstance();
        clist = database.getReference("CompanyYear").child(Common.yearSelected)
        .child("details").child("Companies");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        recycler_list = findViewById(R.id.recycler_company_list);
        recycler_list.setHasFixedSize(true);


        recycler_list.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));




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
//                    recycler_list.setAdapter(adptor);
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
        fab = findViewById(R.id.fabfl);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddFoodDialog();

            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorgreen,
                R.color.color_option_menu,
                R.color.darkRed);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getIntent() != null){
                    categoryId = getIntent().getStringExtra("CategoryId");

                }
                if (!categoryId.isEmpty() && categoryId != null) {
                    loadListCompany(categoryId);
                }

            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(getIntent() != null){
                    categoryId = getIntent().getStringExtra("CategoryId");

                }
                if (!categoryId.isEmpty() && categoryId != null) {
                    loadListCompany(categoryId);
                }

            }
        });


    }

    private void loadListCompany(String categoryId) {


        FirebaseRecyclerOptions<CompanyCategory> options = new FirebaseRecyclerOptions.Builder<CompanyCategory>()
                .setQuery(clist.orderByChild("ccID").equalTo(categoryId),CompanyCategory.class)
                .build();

        adptor = new FirebaseRecyclerAdapter<CompanyCategory, CompanyListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CompanyListViewHolder companyListViewHolder, int i, @NonNull CompanyCategory companyList) {

                Picasso.with(getApplicationContext()).load(companyList.getImage())
                        .into(companyListViewHolder.imageView);
                final CompanyCategory clickItem = companyList;
                companyListViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

//                        Toast.makeText(getApplicationContext(), "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), CompanyDetailsActivity.class);
////                        intent.putExtra("CategoryId", adptor.getRef(position).getKey());
//                        Common.companyCategorySelected = adptor.getRef(position).getKey();
//                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public CompanyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.company_list_item, parent, false);
                return new CompanyListViewHolder(itemView);
            }
        };


        adptor.startListening();

        recycler_list.setAdapter(adptor);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadListCompany(categoryId);
    }

    @Override
    public void onStop() {
        super.onStop();
        adptor.stopListening();
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

    private void showAddFoodDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CompanyListActivity.this);
        alertDialog.setTitle("Add New Company");
        alertDialog.setMessage("Please Fill Complete Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout, null);


        editName = add_menu_layout.findViewById(R.id.editName);
        et_cgpa = add_menu_layout.findViewById(R.id.et_cgpa);
        et_location = add_menu_layout.findViewById(R.id.et_job_location);
        et_role = add_menu_layout.findViewById(R.id.et_job_role);
        et_visitdate = add_menu_layout.findViewById(R.id.et_visitdate);


        spin_duration = add_menu_layout.findViewById(R.id.spin_duration);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.duration));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_duration.setAdapter(myAdapter);
        spin_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                duration = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin_work_time = add_menu_layout.findViewById(R.id.spin_worktime);
        ArrayAdapter<String> mw = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.work_time));
        mw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_work_time.setAdapter(mw);
        spin_work_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                worktime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin_offers = add_menu_layout.findViewById(R.id.spin_offers);
        ArrayAdapter<String> mo = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.offers));
        mo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_offers.setAdapter(mo);
        spin_offers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                offers = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spin_job_type = add_menu_layout.findViewById(R.id.spin_jobtype);
        ArrayAdapter<String> mj = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.job_type));
        mj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_job_type.setAdapter(mj);
        spin_job_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                job_type = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        mItemSelected =  add_menu_layout.findViewById(R.id.tv_elgible_department);

        listItems = getResources().getStringArray(R.array.select_eligible_department);
        checkedItems = new boolean[listItems.length];

        mItemSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getApplicationContext());
                mBuilder.setTitle("Days of Volunteer");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                            mItemSelected.setText("Select Days of Volunteer");
                        }
                    }
                });

                mBuilder.setCancelable(true);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        mItemSelected.setText(item);
                    }
                });

                mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        mItemSelected.setText("Select Days of Volunteer");
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });




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
                if(newcompany  !=null){

                    clist.child(editName.getText().toString()).setValue(newcompany);
                   // Snackbar.make(roottLayout,"New Food"+newfood.getName()+"was addded",Snackbar.LENGTH_SHORT).show();

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

            final ProgressDialog progressDialog = new ProgressDialog(CompanyListActivity.this);
            progressDialog.setMessage("Uploading........");
            progressDialog.show();


            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            progressDialog.dismiss();
                            Toast.makeText(CompanyListActivity.this,"Uplaod Succesfull",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newcompany = new CompanyCategory();
                                    newcompany.setName(editName.getText().toString());
                                    newcompany.setCgpa(et_cgpa.getText().toString());
                                    newcompany.setVisitdate(et_visitdate.getText().toString());
                                    newcompany.setJoblocation(et_location.getText().toString());
                                    newcompany.setRole(et_role.getText().toString());
//                                    newfood.setDescription(edtDescription.getText().toString());
//                                    newfood.setPrice(edtPrice.getText().toString());
//                                    newfood.setDiscount(edtDiscount.getText().toString());
//                                    newfood.setMenuId(categoryId);
                                    newcompany.setImage(uri.toString());

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(CompanyListActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

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
    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture of food"),PICK_IMAGE_REQUEST);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null && data != null) {

            saveUrl = data.getData();
            btnselect.setText("Image Selected");

        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {


        if(item.getTitle().equals(Common.UPDATE))
        {

            showUpdateFoodDialog(adptor.getRef(item.getOrder()).getKey(), adptor.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {


            deleteFood(adptor.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key) {

        clist.child(key).removeValue();
    }

    private void showUpdateFoodDialog(final String key, final CompanyCategory item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CompanyListActivity.this);
        alertDialog.setTitle("Edit Food");
        alertDialog.setMessage("Please Fill Complete Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout, null);


        editName = add_menu_layout.findViewById(R.id.editName);
        et_cgpa = add_menu_layout.findViewById(R.id.et_cgpa);
        et_location = add_menu_layout.findViewById(R.id.et_job_location);
        et_role = add_menu_layout.findViewById(R.id.et_job_role);
        et_visitdate = add_menu_layout.findViewById(R.id.et_visitdate);

        editName.setText(item.getName());
        et_role.setText(item.getRole());
        et_cgpa.setText(item.getCgpa());
        et_visitdate.setText(item.getVisitdate());
        et_location.setText(item.getJoblocation());

//        edtDiscount.setText(item.getDiscount());
//        edtDescription.setText(item.getDescription());
//        edtPrice.setText(item.getPrice());


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
                ChangeImage(item);
            }
        });


        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if(newcompany  !=null){

                    item.setName(editName.getText().toString());
//                    item.setDescription(edtDescription.getText().toString());
//                    item.setDiscount(edtDiscount.getText().toString());
//                    item.setPrice(edtPrice.getText().toString());


                    clist.child(key).setValue(item);
                 //   Snackbar.make(roottLayout,"Food "+item.getName()+"was addded",Snackbar.LENGTH_SHORT).show();

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

    private void ChangeImage(final CompanyCategory item) {


        if (saveUrl != null) {

            final ProgressDialog progressDialog = new ProgressDialog(CompanyListActivity.this);
            progressDialog.setMessage("Uploading........");
            progressDialog.show();


            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            progressDialog.dismiss();
                            Toast.makeText(CompanyListActivity.this,"Uplaod Succesfull",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(CompanyListActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

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
