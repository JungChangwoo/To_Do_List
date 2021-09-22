package org.myapp.mobileprogramming_todolist;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btn_add;

    ArrayList<MainData> dataList;

    MainAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();

        try {
            File file = new File(getFilesDir(), "todoList.txt");
            Scanner sc = new Scanner(file);
            while (readTodoList(sc)) {

            }
            sc.close();
            dataList.sort(new StartTimeSorter());
            // 정렬된 대로 파일에 다시 써주기
            FileWriter fw = new FileWriter(file, false);
            writeTodoList(fw);
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("@@@@@@@@@@@@@@@@", "no file");
        } catch (IOException e) {
            e.printStackTrace();
        }


        recyclerView = findViewById(R.id.activity_main_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MainAdapter(this, dataList, getFilesDir());
        recyclerView.setAdapter(adapter);

        btn_add = (Button) findViewById(R.id.toolbar_main_btn_add);

        // 추가버튼 누르면
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivityIfNeeded(intent, 0);
            }
        });
    }

    public Boolean readTodoList(Scanner sc) {
        if (sc.hasNext()) {
            MainData md = new MainData();
            md.setTitle(sc.nextLine());
            md.setContent(sc.nextLine());
            md.setStartTime(sc.nextLine());
            md.setEndTime(sc.nextLine());
            md.setCategory(sc.nextLine());
            dataList.add(md);
            return true;
        }
        return false;
    }

    public void writeTodoList(FileWriter fw) throws IOException {
        for (MainData md : dataList) {
            fw.write(md.getTitle());
            fw.write("\n");
            fw.write(md.getContent());
            fw.write("\n");
            fw.write(md.getStartTime());
            fw.write("\n");
            fw.write(md.getEndTime());
            fw.write("\n");
            fw.write(md.getCategory());
            fw.write("\n");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("title");
                try {
                    File file = new File(getFilesDir(), "todoList.txt");
                    Scanner sc = new Scanner(file);
                    dataList.clear();
                    while (readTodoList(sc)) {

                    }
                    sc.close();
                    dataList.sort(new StartTimeSorter());
                    // 정렬된 대로 파일에 다시 써주기
                    FileWriter fw = new FileWriter(file, false);
                    writeTodoList(fw);
                    fw.close();
                    Toast.makeText(this, "[" + title + "]" + " 성공적으로 추가되었습니다!", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d("@@@@@@@@@@@@@@@@", "no file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("title");
                int position = data.getIntExtra("position", -1);

                try {
                    File file = new File(getFilesDir(), "todoList.txt");
                    // dataList 읽기 / dataList에 맨 뒤에 추가해준 것 저장
                    Scanner sc = new Scanner(file);
                    dataList.clear();
                    while (readTodoList(sc)) {

                    }
                    sc.close();
                    // dataList에 선택된 position 삭제 // 다시 dataList 읽기
                    // dataList에서 선택된 position지우기
                    dataList.remove(position);
                    // startTime으로 정렬하기
                    dataList.sort(new StartTimeSorter());
                    // dataList에 삭제된 dataList버전으로 다시 써주기
                    FileWriter fw = new FileWriter(file, false);
                    writeTodoList(fw);
                    fw.close();
                    //다시 dataList 읽기
                    Scanner sc2 = new Scanner(file);
                    dataList.clear();
                    while (readTodoList(sc2)) {

                    }
                    sc2.close();
                    Toast.makeText(this, "[" + title + "]" + " 성공적으로 수정되었습니다!", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d("@@@@@@@@@@@@@@@@", "no file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}