package com.ummeeth.speedquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class activity_game extends AppCompatActivity {

    Runnable questionRunnable = null;
    Handler handler;
    private TextView textQuestionJ1;
    private TextView textQuestionJ2;
    private TextView timerJ1;
    private TextView timerJ2;
    private Button BTJ1;
    private Button BTJ2;
    private TextView ScoreJ1;
    private TextView ScoreJ2;


    ArrayList<Question> listeQuestion = new ArrayList<>();
    Random rand = new Random();
    int scoreJoueur1;
    int scoreJoueur2;
    int index;
    boolean derniereQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent activity_game = getIntent();

        QuestionManager qManager = new QuestionManager(this);

        TextView nomJoueur1 = findViewById(R.id.PseudoJ1);
        TextView nomJoueur2 = findViewById(R.id.PseudoJ2);
        textQuestionJ1 = findViewById(R.id.questionJ1);
        textQuestionJ2 = findViewById(R.id.questionJ2);
        timerJ1 = findViewById(R.id.timerJ1);
        timerJ2 = findViewById(R.id.timerJ2);
        BTJ1 = findViewById(R.id.bt_reponseJ1);
        BTJ2 = findViewById(R.id.bt_reponseJ2);
        ScoreJ1 = findViewById(R.id.scoreJ1);
        ScoreJ2 = findViewById(R.id.scoreJ2);
        listeQuestion.addAll(qManager.getQuestionList());

        nomJoueur1.setText(activity_game.getStringExtra("NomJoueur1"));
        nomJoueur2.setText(activity_game.getStringExtra("NomJoueur2"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        BTJ1.setOnClickListener(view -> {
            BTJ1.setEnabled(false);
            BTJ2.setEnabled(false);
            if (listeQuestion.get(index).getReponse() == 0) {
                scoreJoueur1 += 1;
            } else {
                scoreJoueur1 -= 1;
            }
            ScoreJ1.setText(String.valueOf(scoreJoueur1));
            listeQuestion.remove(index);

        });
        BTJ2.setOnClickListener(view -> {
            BTJ1.setEnabled(false);
            BTJ2.setEnabled(false);
            if (listeQuestion.get(index).getReponse() == 0) {
                scoreJoueur2 += 1;
            } else {
                scoreJoueur2 -= 1;
            }
            ScoreJ2.setText(String.valueOf(scoreJoueur2));
            listeQuestion.remove(index);

        });
        startCountDownTimer();
    }

    private void afficheQuestion() {
        index = rand.nextInt(listeQuestion.size());

        textQuestionJ1.setText(listeQuestion.get(index).getIntitule());
        textQuestionJ2.setText(listeQuestion.get(index).getIntitule());

    }

    private void initStartQuizz() {
        BTJ1.setEnabled(true);
        BTJ2.setEnabled(true);
        timerJ1.setText("");
        timerJ2.setText("");
    }

    private void startQuestionIterative() {
        handler = new Handler();
        questionRunnable = new Runnable() {
            @Override
            public void run() {
                initStartQuizz();
                afficheQuestion();

                if (listeQuestion.size() == 1) {
                    derniereQuestion = true;
                    handler.postDelayed(this,3500);
                } else {
                    handler.postDelayed(this,3500);
                }
                if (derniereQuestion) {
                    timerJ1.setText(R.string.fin);
                    timerJ2.setText(R.string.fin);
                    BTJ1.setEnabled(false);
                    BTJ2.setEnabled(false);
                    handler.removeCallbacks(questionRunnable);
                }
            }
        };
        handler.postDelayed(questionRunnable, 1000);
    }

    private void startCountDownTimer() {
        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
                String decompte = String.valueOf(millisUntilFinished / 1000);
                timerJ1.setText(decompte);
                timerJ2.setText(decompte);
            }

            public void onFinish() {
                timerJ1.setText(getText(R.string.timer));
                timerJ2.setText(getText(R.string.timer));
                startQuestionIterative();
            }

        }.start();
    }
}