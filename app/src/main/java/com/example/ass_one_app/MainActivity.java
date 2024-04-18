package com.example.ass_one_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String NAME = "NAME";
    public static final String PASS = "PASS";
    public static final String FLAG = "FLAG";
    private boolean flag = false;
    private EditText edtName;
    private EditText edtPassword;
    private CheckBox chk;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    TextView totalQuestionsTextView;
    TextView questionTextView;
    Button choiceA, choiceB, choiceC;
    Button confirm;

    int counter=0;
    int totalQuestion = Results.question.length;
    int index = 0;
    String selectedAnswer = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supe    r.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the second activity
                startActivity(new Intent(MainActivity.this, ActivityTwo.class));
            }
        });

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the second activity
                startActivity(new Intent(MainActivity.this, ActivityThree.class));
            }
        });

        setupViews();
        setupSharedPrefs();
        checkPrefs();


        totalQuestionsTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        choiceA = findViewById(R.id.ans_A);
        choiceB = findViewById(R.id.ans_B);
        choiceC = findViewById(R.id.ans_C);
        confirm = findViewById(R.id.submit_btn);

        choiceA.setOnClickListener(this);
        choiceB.setOnClickListener(this);
        choiceC.setOnClickListener(this);
        confirm.setOnClickListener(this);

        totalQuestionsTextView.setText("Total questions : "+totalQuestion);

        loadNewQuestion();


    }

    private void checkPrefs() {
        flag = prefs.getBoolean(FLAG, false);

        if(flag){
            String name = prefs.getString(NAME, "");
            String password = prefs.getString(PASS, "");
            edtName.setText(name);
            edtPassword.setText(password);
            chk.setChecked(true);
        }
    }

    private void setupSharedPrefs() {
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void setupViews() {
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        chk = findViewById(R.id.chk);
    }

    public void btnLoginOnClick(View view) {
        String name = edtName.getText().toString();
        String password = edtPassword.getText().toString();

        if (chk.isChecked()) {
            if (!flag) {
                editor.putString(NAME, name);
                editor.putString(PASS, password);
                editor.putBoolean(FLAG, true);
                editor.commit();
            }

        }
        // authenticate the user

    }
    @Override
    public void onClick(View view) {

        choiceA.setBackgroundColor(Color.WHITE);
        choiceB.setBackgroundColor(Color.WHITE);
        choiceC.setBackgroundColor(Color.WHITE);

        Button clickedButton = (Button) view;
        if(clickedButton.getId()==R.id.submit_btn){
            if(selectedAnswer.equals(Results.correctAnswers[index])){
                counter++;
            }
            index++;
            loadNewQuestion();


        }else{
            //choices button clicked
            selectedAnswer  = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.MAGENTA);

        }

    }

    void loadNewQuestion(){

        if(index == totalQuestion ){
            finishQuiz();
            return;
        }

        questionTextView.setText(Results.question[index]);
        choiceA.setText(Results.choices[index][0]);
        choiceB.setText(Results.choices[index][1]);
        choiceC.setText(Results.choices[index][2]);

    }

    void finishQuiz(){
        String passStatus = "";
        if(counter > totalQuestion*0.60){
            passStatus = "Passed";
        }else{
            passStatus = "Failed";
        }

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Score is "+ counter+" out of "+ totalQuestion)
                .setPositiveButton("Restart",(dialogInterface, i) -> restartQuiz() )
                .setCancelable(false)
                .show();


    }

    void restartQuiz(){
        counter = 0;
        index =0;
        loadNewQuestion();
    }
}