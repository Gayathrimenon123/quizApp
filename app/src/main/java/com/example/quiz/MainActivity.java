 package com.example.quiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

 public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ=1;
     public static final String EXTRA_CATEGORY_ID="extraCategoryID";
     public static final String EXTRA_CATEGORY_NAME="extraCategoryName";
    public static final String EXTRA_DIFFICULTY="extraDifficulty";

    public static final String SHARED_PREFS="sharedPrefs";
    public static final String KEY_HIGHSCORE="keyHighscore";

    private TextView textviewhighscore;
    private Spinner spinnerCategory;
    private Spinner spinnerDifficulty;

    private int highscore;

    Button start_quiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textviewhighscore=findViewById(R.id.text_high);
        spinnerCategory=findViewById(R.id.spinner_category);
        spinnerDifficulty=findViewById(R.id.spinner_difficulty);

        loadCategories();
        loadDifficultyLevels();
        loadHighscore();

        start_quiz=findViewById(R.id.b_start);
        start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startquiz();
            }
        });


    }
    private  void startquiz(){
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();
        String difficulty=spinnerDifficulty.getSelectedItem().toString();

        Intent intent=new Intent(MainActivity.this,quizActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID,categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME,categoryName);
        intent.putExtra(EXTRA_DIFFICULTY,difficulty);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }

     @Override
     protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if(requestCode==REQUEST_CODE_QUIZ){
             if(resultCode==RESULT_OK){
                 int score=data.getIntExtra(quizActivity.EXTRA_SCORE,0);
                 if(score>highscore){
                     updateHighscore(score);
                 }
             }
         }
     }

     private void loadCategories(){
        QuizDbHelper dbHelper=QuizDbHelper.getInstance(this);
        List<Category> categories=dbHelper.getAllCategories();

        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
     }

     private void loadDifficultyLevels(){
         String[] difficultyLevels=Question.getAllDifficultyLevels();

         ArrayAdapter<String> adapterDifficulty=new ArrayAdapter<>(this,
                 android.R.layout.simple_spinner_item,difficultyLevels);
         adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinnerDifficulty.setAdapter(adapterDifficulty);
     }

     private  void loadHighscore(){
        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        highscore=prefs.getInt(KEY_HIGHSCORE,0);
         textviewhighscore.setText("Highscore:" +highscore);
     }

     private void updateHighscore(int highscoreNew){
        highscore=highscoreNew;
         textviewhighscore.setText("Highscore:" +highscore);

         SharedPreferences prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
         SharedPreferences.Editor editor=prefs.edit();
         editor.putInt(KEY_HIGHSCORE,highscore);
         editor.apply();
     }
 }