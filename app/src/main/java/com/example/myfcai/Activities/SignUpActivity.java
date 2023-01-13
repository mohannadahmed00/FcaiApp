package com.example.myfcai.Activities;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfcai.Class.Course;
import com.example.myfcai.Class.Loading;
import com.example.myfcai.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayAdapter adapter;
    ArrayList<String> types;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    UploadTask uploadTask;

    Spinner sType;
    ImageView ivUserImg;
    EditText etFirstName, etLastName, etAge, etPhone, etEmail, etPassword, etReTypePassword;
    Button btnSignUp, btnGoogle;
    TextView tvShowPassword, tvShowRePassword, tvSignIn;
    Loading loading;

    Uri userImgLocalUri;
    String userImgGlobalUri, type;
    public static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        loading = new Loading(SignUpActivity.this);

        ivUserImg = findViewById(R.id.iv_user_img_activity_sign_up);
        etFirstName = findViewById(R.id.et_first_name_activity_sign_up);
        etLastName = findViewById(R.id.et_last_name_activity_sign_up);
        sType = findViewById(R.id.s_type_activity_sign_up);
        etAge = findViewById(R.id.et_age_activity_sign_up);
        etPhone = findViewById(R.id.et_phone_activity_sign_up);
        etEmail = findViewById(R.id.et_email_activity_sign_up);
        etPassword = findViewById(R.id.et_password_activity_sign_up);
        etReTypePassword = findViewById(R.id.et_retype_password_activity_sign_up);
        tvShowPassword = findViewById(R.id.tv_show_password_activity_sign_up);
        tvShowRePassword = findViewById(R.id.tv_show_retype_password_activity_sign_up);
        btnSignUp = findViewById(R.id.btn_sign_up_activity_sign_up);
        btnGoogle = findViewById(R.id.btn_google_activity_sign_up);
        tvSignIn = findViewById(R.id.tv_sign_in_activity_sign_up);

        sType.setOnItemSelectedListener(this);
        types = new ArrayList<>();
        types.add("student");
        types.add("instructor");
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sType.setAdapter(adapter);

        ivUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        tvShowPassword.setVisibility(View.GONE);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPassword.getText().length() > 0) {
                    tvShowPassword.setVisibility(View.VISIBLE);
                } else {
                    tvShowPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword(etPassword, tvShowPassword);
            }
        });

        tvShowRePassword.setVisibility(View.GONE);
        etReTypePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etReTypePassword.getText().length() > 0) {
                    tvShowRePassword.setVisibility(View.VISIBLE);
                } else {
                    tvShowRePassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvShowRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword(etReTypePassword, tvShowRePassword);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                if (connected) {
                    createAccount();
                }else {
                    Toast.makeText(SignUpActivity.this, "please check your connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {/*
                ArrayList<Course> courses=new ArrayList<>();
                courses.add(new Course("C101","Introduction to Computer Science",0));
                courses.add(new Course("CS211","Algorithm Design and Analysis",0));
                for (Course course:courses){
                    FirebaseDatabase.getInstance().getReference().child("User Info").child("GUgRRu9z2DTTTNa3kVekmpv5Hmm2").child("courses").child(course.getCode()).setValue(course.getName());
                }*/
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                //onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            userImgLocalUri = data.getData();
            ivUserImg.setImageURI(userImgLocalUri);
        }
    }

    void createAccount() {
        final String fName, lName, age, email, phone, password, rePassword;
        fName = etFirstName.getText().toString().trim();
        lName = etLastName.getText().toString().trim();
        age = etAge.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        rePassword = etReTypePassword.getText().toString().trim();

        if ((!fName.equals("") && !lName.equals("") && !email.equals("") && !phone.equals("") && !password.equals("")) && (password.equals(rePassword) && (password.length() >= 8) && userImgLocalUri != null)) {
            loading.startLoading();
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        storageReference = FirebaseStorage.getInstance().getReference().child("User Image").child(System.currentTimeMillis() + "." + getFileExe(userImgLocalUri));
                        uploadTask = storageReference.putFile(userImgLocalUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {
                                if (!task.isComplete()) {
                                    throw task.getException();
                                }
                                return storageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    userImgGlobalUri = task.getResult().toString();
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    Map<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("first name", fName);
                                    hashMap.put("last name", lName);
                                    hashMap.put("type", type);
                                    hashMap.put("age", age);
                                    hashMap.put("email", email);
                                    hashMap.put("phone", phone);
                                    hashMap.put("user image", userImgGlobalUri);
                                    hashMap.put("dep", "G");
                                    hashMap.put("level", "1");
                                    hashMap.put("gpa", 0.0);

                                    databaseReference.setValue(hashMap);
                                    sendVerificationEmail();
                                }
                            }
                        });


                    } else {
                        loading.dismissLoading();
                        Toast.makeText(SignUpActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (userImgLocalUri == null) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8 && password.length() > 0) {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
        } else if (!etPassword.getText().toString().trim().equals(etReTypePassword.getText().toString().trim())) {
            Toast.makeText(this, "Password is not match", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter your information", Toast.LENGTH_SHORT).show();
        }
    }


    private void showPassword(EditText password, TextView show) {
        if (show.getText().equals("show")) {
            show.setText("hide");
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            show.setText("show");
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        password.setSelection(password.length());
    }

    private String getFileExe(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void sendVerificationEmail() {

        Toast.makeText(SignUpActivity.this, "Verification mail is sent", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        loading.dismissLoading();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (types.get(position).equals("instructor")){
            type="teacher";
        }else {
            type = types.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

 /*ArrayList<Course> courses = new ArrayList<>();
                courses.add(new Course("CS101","Introduction to Computer Science",3,"","e","1",false));
                courses.add(new Course("CS141","Programming Fundamentals",3,"CS101","e","1",false));
                courses.add(new Course("MA101","Mathematics I",3,"","e","1",false));
                courses.add(new Course("MA102","Mathematics II",3,"MA101","e","1",false));
                courses.add(new Course("PH101","Physics I",3,"","e","1",false));
                courses.add(new Course("EE101","Electronics",3,"","e","1",false));
                courses.add(new Course("EE102","Digital logic and design",2,"EE101","e","1",false));
                courses.add(new Course("HU111","English Language I",2,"","e","1",false));
                courses.add(new Course("HU112","English Language II",2,"HU111","e","1",false));
                courses.add(new Course("HU121","Social Context of Computing",1,"","e","1",false));
                courses.add(new Course("HU122","Intellectual Property",1,"","e","1",false));
                courses.add(new Course("HU132","Interpersonal Communication",2,"","e","1",false));
                courses.add(new Course("IS101","Foundations of Information Systems",3,"CS101","e","1",false));
                courses.add(new Course("HU133","Computing Economics",2,"","e","1",false));
                courses.add(new Course("HU141","Computer Law",2,"","e","1",false));
                courses.add(new Course("HU142","Privacy and Civil Liberties",1,"","e","1",false));
                courses.add(new Course("HU151","Hand Drawing",2,"","e","1",false));
                courses.add(new Course("HU152","History of Computing",2,"","e","1",false));
                courses.add(new Course("HU153","Islamic Culture",1,"","e","1",false));
                courses.add(new Course("HU154","Scientific Thinking",1,"","e","1",false));
                courses.add(new Course("HU131","Business Administration",2,"","e","1",false));
                courses.add(new Course("HU143","Computers and Ethics",1,"","e","1",false));
                courses.add(new Course("CS201","Discrete Structures",3,"MA102","e","1",false));
                courses.add(new Course("CS211","Data Structures and Algorithms",3,"CS241","e","1",false));
                courses.add(new Course("CS241","Object-Oriented Programming",3,"CS141","e","1",false));
                courses.add(new Course("IS212","Databases",3,"IS101","e","1",false));
                courses.add(new Course("IS221","Project Management ",2,"CS101","e","1",false));
                courses.add(new Course("IS231","Systems Analysis and Design",3,"CS101","e","1",false));
                courses.add(new Course("IT251","Data Communications",3,"CS101","e","1",false));
                courses.add(new Course("IT271","Web Programming",3,"CS141IT251","e","1",false));
                courses.add(new Course("MA201","Mathematics III ",3,"MA102","e","1",false));
                courses.add(new Course("MA202","Probability and Statistics",2,"MA102","e","1",false));
                courses.add(new Course("EE201","Digital Signal Processing",3,"MA201","e","1",false));
                courses.add(new Course("HU231","Organizational Behavior",2,"","e","1",false));
                courses.add(new Course("HU232","Technical Writing",2,"HU111","e","1",false));
                courses.add(new Course("HU233","Math communication",1,"","e","1",false));

                for (Course c : courses) {
                    HashMap<String, Object> course = new HashMap<>();
                    course.put("name", c.getName());
                    course.put("credit", c.getCredit());
                    course.put("pre", c.getPre());
                    course.put("type", c.getType());
                    course.put("status", c.getStatus());
                    HashMap<String,Object> table=new HashMap<>();
                    table.put("1st","");
                    table.put("2nd","");
                    FirebaseDatabase.getInstance().getReference().child("Department").child("G").child("course").child(c.getCode()).setValue(course);
                    FirebaseDatabase.getInstance().getReference().child("Department").child("G").child("table").setValue(table);

                }
                //FirebaseAuth.getInstance().signOut();*/
/*FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Verification mail is sent", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(SignUpActivity.this, "Verification error", Toast.LENGTH_SHORT).show();
                    databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                }
            }
        });*/
