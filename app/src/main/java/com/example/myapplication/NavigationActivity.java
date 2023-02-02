package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ActivityNavigationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
    //현재 연결은 데이터베이스에만 딱 연결해놓고
    //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseUser user;


    String date;
    private String uid;
    private List<todoList> listDto = new ArrayList<>();
    private List<String> contents = new ArrayList<>(); // 할일을 담을 List
    //네비게이션 메뉴 추가부분
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationBinding binding;
   
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //네비게이션 메뉴 추가부분
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigation.toolbar);
        //toolbar title 제거
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                //페이지 이동
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //네비게이션  추가부분



        CalendarView calendarView = findViewById(R.id.calendarView);
        Button btn_save = findViewById(R.id.save_Btn);
        EditText editText = findViewById(R.id.contextEditText);

        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NavigationActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(linearLayoutManager);

        todoListAdapter adapter = new todoListAdapter(listDto);

        editText.setVisibility(View.VISIBLE);
        btn_save.setVisibility(View.VISIBLE);

        // 다른 날짜를 클릭하면
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                date = year + "-" + (month + 1) + "-" + dayOfMonth;
            }
        });

        // 파이어베이스에서 데이터 가져옴
        //옵저버 패턴 --> 변화가 있으면 클라이언트에 알려준다.
        database.getReference().child("todoList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //변화된 값이 DataSnapshot 으로 넘어온다.
                //데이터가 쌓이기 때문에  clear()
                listDto.clear();

                for(DataSnapshot ds : snapshot.getChildren()) {
                    todoList  todolistDto = ds.getValue(todoList.class);
                    listDto.add(todolistDto);

                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(todolistDto.getUid())) {
//                        String content = todolistDto.getContent();
//                        contents.add(content);
                    }
                }
                recyclerView1.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        // 날짜 기본값(클릭을 하지 않으면 오늘날짜가 들어감)
        Calendar cal = Calendar.getInstance();
        Date nowDate = cal.getTime();
        SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-M-d");
        date = dataformat.format(nowDate);


        // 저장 버튼을 클릭하면
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    uid = user.getUid();
                }
                addList(editText.getText().toString(), date, 0, uid);
            }
        });
    }

    //EditText에 있는 값을 파이어베이스 Realtime database로 넘기는 함수
    public void addList(String content, String date, int chkId, String uid){
        todoList todoList = new todoList(content, date, chkId, uid);
        databaseReference.child("todoList").push().setValue(todoList);
    }

    public void getList(){

    }


    //네비게이션  추가부분
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);

        return true;
    }
    //네비게이션  추가부분
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}