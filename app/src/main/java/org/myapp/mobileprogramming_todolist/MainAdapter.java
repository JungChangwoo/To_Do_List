package org.myapp.mobileprogramming_todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<MainData> todoList = null;
    private Context mainContext;
    private File fileDir;

    MainAdapter(Context context, ArrayList<MainData> list, File filesDir){
        mainContext = context;
        todoList = list;
        fileDir = filesDir;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_main_item, parent, false);
        MainAdapter.ViewHolder vh = new MainAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        String title = todoList.get(position).getTitle();
        String content = todoList.get(position).getContent();
        String startTime = todoList.get(position).getStartTime();
        String endTime = todoList.get(position).getEndTime();
        String toTime = "~";
        String category = "["+todoList.get(position).getCategory()+"]";

        holder.title.setText(title);
        holder.content.setText(content);
        holder.startTime.setText(startTime);
        holder.endTime.setText(endTime);
        holder.toTime.setText(toTime);
        holder.category.setText(category);
        if (holder.category.getText().toString().equals("[Study]")){
            holder.category.setTextColor(Color.parseColor("#980000"));
            holder.categoryImg.setImageResource(R.drawable.studyimg);
        }
        if (holder.category.getText().toString().equals("[Workout]")){
            holder.category.setTextColor(Color.parseColor("#FF007F"));
            holder.categoryImg.setImageResource(R.drawable.workoutimg);
        }
        if (holder.category.getText().toString().equals("[Daily Life]")){
            holder.category.setTextColor(Color.parseColor("#2F9D27"));
            holder.categoryImg.setImageResource(R.drawable.dailyimg);
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView startTime;
        TextView endTime;
        TextView toTime;
        TextView category;
        ImageView categoryImg;

        ViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.main_item_tv_title);
            content = itemView.findViewById(R.id.main_item_tv_content);
            startTime = itemView.findViewById(R.id.main_item_tv_startTime);
            endTime = itemView.findViewById(R.id.main_item_tv_endTime);
            toTime = itemView.findViewById(R.id.main_item_tv_toTime);
            category = itemView.findViewById(R.id.main_item_tv_category);
            categoryImg = itemView.findViewById(R.id.main_item_iv_categoryImg);

            View view = itemView;
            // 아이템 롱클릭시 수정가능하게 AddActivity 호출
            view.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(mainContext, AddActivity.class);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("content", content.getText().toString());
                    intent.putExtra("startTime", startTime.getText().toString());
                    intent.putExtra("endTime", endTime.getText().toString());
                    String intentCategory;
                    intentCategory = (category.getText().toString()).replace("]", "");
                    intentCategory = intentCategory.replace("[","");
                    intent.putExtra("category", intentCategory);
                    // position도 함께
                    intent.putExtra("position", getAdapterPosition());
                    ((Activity)mainContext).startActivityForResult(intent, 1);

                    return false;
                }
            });


            // 아이템 클릭시 삭제 다이어로그 생성
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    View dialogView = (View) View.inflate(mainContext, R.layout.dialog_todolist_delete, null);
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mainContext);
                    dialogBuilder.setView(dialogView);
                    AlertDialog alertDialog = dialogBuilder.create();

                    Button btn_yes = (Button) dialogView.findViewById(R.id.dialog_todolist_delete_btn_yes);
                    Button btn_no = (Button) dialogView.findViewById(R.id.dialog_todolist_delete_btn_no);

                    btn_yes.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            //삭제 버튼 누르면
                            try {
                                String removeTitle = title.getText().toString();
                                todoList.remove(getAdapterPosition());
                                File file = new File(fileDir,"todoList.txt");
                                FileWriter fw = new FileWriter(file, false);
                                for(MainData md : todoList){
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
                                Toast.makeText(mainContext, removeTitle+" 삭제되었습니다!", Toast.LENGTH_SHORT).show();
                                fw.close();
                                notifyDataSetChanged();
                                alertDialog.dismiss();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });
        }
    }

}
