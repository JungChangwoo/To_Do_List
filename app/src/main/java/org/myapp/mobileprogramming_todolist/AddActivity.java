package org.myapp.mobileprogramming_todolist;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AddActivity extends AppCompatActivity {
    TextView et_title;
    TextView et_content;
    EditText et_startTime;
    EditText et_endTime;
    EditText et_category;
    Button btn_startTime;
    Button btn_endTime;
    Button btn_category;
    Button btn_ok;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_title = (TextView) findViewById(R.id.add_et_title);
        et_content = (TextView) findViewById(R.id.add_et_content);
        et_startTime = (EditText) findViewById(R.id.add_et_startTime);
        et_endTime = (EditText) findViewById(R.id.add_et_endTime);
        et_category = (EditText) findViewById(R.id.add_et_category);
        btn_startTime = (Button) findViewById(R.id.add_btn_select_startTime);
        btn_endTime = (Button) findViewById(R.id.add_btn_select_endTime);
        btn_category = (Button) findViewById(R.id.add_btn_select_category);
        btn_ok = (Button) findViewById(R.id.add_btn_ok);

        Intent getIntent = getIntent();
        et_title.setText(getIntent.getStringExtra("title"));
        et_content.setText(getIntent.getStringExtra("content"));
        et_startTime.setText(getIntent.getStringExtra("startTime"));
        et_endTime.setText(getIntent.getStringExtra("endTime"));
        et_category.setText(getIntent.getStringExtra("category"));
        position = getIntent.getIntExtra("position",-1);

        btn_startTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String customMinute = ""+minute;
                        if(minute <10){
                            customMinute = "0"+minute;
                        }
                        String customeHour = ""+hourOfDay;
                        if(hourOfDay < 10){
                            customeHour = "0"+hourOfDay;
                        }
                        et_startTime.setText(customeHour + ":" + customMinute);
                    }
                }, 0, 0, false);

                timePickerDialog.setTitle("시작 시간 설정");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

        btn_endTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String customMinute = ""+minute;
                        if(minute <10){
                            customMinute = "0"+minute;
                        }
                        String customeHour = ""+hourOfDay;
                        if(hourOfDay < 10){
                            customeHour = "0"+hourOfDay;
                        }
                        et_endTime.setText(customeHour + ":" + customMinute);
                    }
                }, 0, 0, false);

                timePickerDialog.setTitle("마침 시간 설정");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

        btn_category.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                getMenuInflater().inflate(R.menu.category, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.study :
                            case R.id.workout :
                            case R.id.dailylife :
                                et_category.setText(item.getTitle());
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    String title = et_title.getText().toString();
                    String content = et_content.getText().toString();
                    String startTime = et_startTime.getText().toString();
                    String endTime = et_endTime.getText().toString();
                    String category = et_category.getText().toString();

                    File file = new File(getFilesDir(),"todoList.txt");
                    FileWriter fw = new FileWriter(file, true);

                    fw.write(title);
                    fw.write("\n");
                    fw.write(content);
                    fw.write("\n");
                    fw.write(startTime);
                    fw.write("\n");
                    fw.write(endTime);
                    fw.write("\n");
                    fw.write(category);
                    fw.write("\n");

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("position", position);
                    setResult(RESULT_OK, intent);
                    fw.close();
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("dddddddddddd", "#############");
                }
            }
        });

    }
}