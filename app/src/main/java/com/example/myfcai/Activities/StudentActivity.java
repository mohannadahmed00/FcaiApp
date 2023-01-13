package com.example.myfcai.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfcai.Adapters.MyPagerAdapter;
import com.example.myfcai.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference databaseReference;
    String age;
    String email,dep,level;
    String imgGlobalURI;
    ImageView ivUserImg;
    String name;
    NavigationView navigationView;
    MyPagerAdapter myPagerAdapter;
    String phone;
    TabLayout tabLayout;
    TextView tvAge;
    TextView tvEmail;
    TextView tvPhone;
    TextView tvUsername,tvDep,tvLevel,tvGPA;
    ViewPager viewPager;
    View header;
    double gpa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //databaseReference= FirebaseDatabase.getInstance().getReference();

        imgGlobalURI = getIntent().getStringExtra("imgURI");
        name = getIntent().getStringExtra("name");
        age = getIntent().getStringExtra("age");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        dep = getIntent().getStringExtra("dep");
        level = getIntent().getStringExtra("level");
        gpa = getIntent().getDoubleExtra("gpa",0);




        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        ivUserImg = header.findViewById(R.id.nav_img);
        tvUsername = header.findViewById(R.id.nav_name);
        tvAge = header.findViewById(R.id.nav_age);
        tvEmail = header.findViewById(R.id.nav_email);
        tvPhone = header.findViewById(R.id.nav_tel);
        tvDep = header.findViewById(R.id.nav_dep);
        tvLevel = header.findViewById(R.id.nav_level);
        tvGPA = header.findViewById(R.id.nav_gpa);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),"s",dep);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        Glide.with(getApplicationContext()).load(imgGlobalURI).into(ivUserImg);
        tvUsername.setText(name);
        tvAge.setText(age);
        tvEmail.setText(email);
        tvPhone.setText(phone);
        tvDep.setText(dep);
        tvLevel.setText(level);
        tvGPA.setText(Double.toString(gpa));
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_change_password) {
            Intent intent=new Intent(StudentActivity.this,RegistrationActivity.class);
            intent.putExtra("dep", dep);
            intent.putExtra("level", level);
            intent.putExtra("gpa", gpa);
            intent.putExtra("name", name);
            startActivity(intent);
        }
        if (id == R.id.nav_about) {
            Toast.makeText(this, "Summer", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.nav_change_user_img) {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.nav_contact_us) {
            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.nav_logout) {
            logout();
        }
        return true;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(StudentActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}