package com.myfirstapp.testing.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myfirstapp.testing.R;
import com.myfirstapp.testing.model.Question;
import com.myfirstapp.testing.model.QuestionBank;
import com.myfirstapp.testing.model.User;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String RESULT_SCORE = "RESULT_SCORE";

    private static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    private static final String BUNDLE_STATE_QUESTION_COUNT = "BUNDLE_STATE_QUESTION_COUNT";
    private static final String BUNDLE_STATE_QUESTION_BANK = "BUNDLE_STATE_QUESTION_BANK";

    private static final int INITIAL_QUESTION_COUNT = 10;

    private TextView mTextViewQuestion;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;
    private String mName;

    private int mScore;
    private int mRemainingQuestionCount;
    private QuestionBank mQuestionBank;

    private boolean mEnableTouchEvents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        mEnableTouchEvents = true;

        mTextViewQuestion = findViewById(R.id.game_activity_textview_question);
        mAnswerButton1 = findViewById(R.id.game_activity_button1);
        mAnswerButton2 = findViewById(R.id.game_activity_button2);
        mAnswerButton3 = findViewById(R.id.game_activity_button3);
        mAnswerButton4 = findViewById(R.id.game_activity_button4);

        // Use the same listener for the four buttons.
        // The view id value will be used to distinguish the button triggered
        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mName = extras.getString("key");

        }

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION_COUNT);
            mQuestionBank = (QuestionBank) savedInstanceState.getSerializable(BUNDLE_STATE_QUESTION_BANK);
        } else {
            mScore = 0;
            mRemainingQuestionCount = INITIAL_QUESTION_COUNT;
            mQuestionBank = generateQuestions();
        }

        displayQuestion(mQuestionBank.getCurrentQuestion());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION_COUNT, mRemainingQuestionCount);
        outState.putSerializable(BUNDLE_STATE_QUESTION_BANK, mQuestionBank);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        int index;

        if (v == mAnswerButton1) {
            index = 0;
        } else if (v == mAnswerButton2) {
            index = 1;
        } else if (v == mAnswerButton3) {
            index = 2;
        } else if (v == mAnswerButton4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }

        if (index == mQuestionBank.getCurrentQuestion().getAnswerIndex()) {
            mScore++;
            mTextViewQuestion.setText("CORRECT !");
            if (index==0){
                mAnswerButton1.setBackgroundColor(Color.GREEN);
            }else if(index==1){
                mAnswerButton2.setBackgroundColor(Color.GREEN);
            }else if(index==2){
                mAnswerButton3.setBackgroundColor(Color.GREEN);
            }else if(index==3){
                mAnswerButton4.setBackgroundColor(Color.GREEN);
            }

        } else {
            mTextViewQuestion.setText("FAUX!");
            if (index==0){
                mAnswerButton1.setBackgroundColor(Color.RED);
            }else if(index==1){
                mAnswerButton2.setBackgroundColor(Color.RED);
            }else if(index==2){
                mAnswerButton3.setBackgroundColor(Color.RED);
            }else if(index==3){
                mAnswerButton4.setBackgroundColor(Color.RED);
            }
            if (mQuestionBank.getCurrentQuestion().getAnswerIndex()==0){
                mAnswerButton1.setBackgroundColor(Color.GREEN);
            }else if(mQuestionBank.getCurrentQuestion().getAnswerIndex()==1){
                mAnswerButton2.setBackgroundColor(Color.GREEN);
            }else if(mQuestionBank.getCurrentQuestion().getAnswerIndex()==2){
                mAnswerButton3.setBackgroundColor(Color.GREEN);
            }else if(mQuestionBank.getCurrentQuestion().getAnswerIndex()==3){
                mAnswerButton4.setBackgroundColor(Color.GREEN);
            }
        }

        mEnableTouchEvents = false;

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;

                mRemainingQuestionCount--;

                if (mRemainingQuestionCount <= 0) {
                    endGame();
                } else {
                    mAnswerButton1.setBackgroundColor(Color.parseColor("#322d31"));
                    mAnswerButton2.setBackgroundColor(Color.parseColor("#322d31"));
                    mAnswerButton3.setBackgroundColor(Color.parseColor("#322d31"));
                    mAnswerButton4.setBackgroundColor(Color.parseColor("#322d31"));
                    displayQuestion(mQuestionBank.getNextQuestion());
                }
            }
        }, 2_000);
    }

    private void displayQuestion(final Question question) {
        // Set the text for the question text view and the four buttons
        mTextViewQuestion.setText(question.getQuestion());
        mAnswerButton1.setText(question.getChoiceList().get(0));
        mAnswerButton2.setText(question.getChoiceList().get(1));
        mAnswerButton3.setText(question.getChoiceList().get(2));
        mAnswerButton4.setText(question.getChoiceList().get(3));
    }

    private void endGame() {
        // No question left, end the game
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(mScore<4){
            builder.setTitle("T'es nuuuuuul "+mName+" !")
                    .setMessage("Ton score: " + mScore+"/10")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra(RESULT_SCORE, mScore);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    })
                    .create()
                    .show();
        }else if (mScore<7) {
            builder.setTitle("Tu peux mieux faire "+mName+".")
                    .setMessage("Ton score: " + mScore+"/10")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra(RESULT_SCORE, mScore);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    })
                    .create()
                    .show();
        }else if(mScore<10){
            builder.setTitle("Bien jou?? "+mName+"!")
                    .setMessage("Ton score: " + mScore+"/10")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra(RESULT_SCORE, mScore);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    })
                    .create()
                    .show();
        }else{
            builder.setTitle("Tu es un g??nie "+mName+"!!!")
                    .setMessage("Ton score: " + mScore +"/10")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra(RESULT_SCORE, mScore);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private QuestionBank generateQuestions() {
        Question question1 = new Question(
                "Qui est le cr??ateur d'Apple ?",
                Arrays.asList("Steve Jobs", "Mark Zuckerberg", "Bill Gates", "Jeff Bezos"),
                0
        );
        Question question2 = new Question(
                "Quelle est la plan??te la plus proche du soleil ?",
                Arrays.asList("Jupiter", "Mars", "Mercure", "Venus"),
                2
        );
        Question question3 = new Question(
                "Combien de dents un adulte humain poss??de-t-il ?",
                Arrays.asList("30", "32", "34", "36"),
                1
        );
        Question question4 = new Question(
                "Quel c??l??bre scientifique a propos?? les 3 lois du mouvement ?",
                Arrays.asList("Albert Einstein", "Galil??e", "Nicolas Copernic", "Isaac Newton"),
                3
        );
        Question question5 = new Question(
                "Cette plan??te tourne le plus vite, effectuant une rotation en seulement 10 heures. De quelle plan??te s'agit-il?",
                Arrays.asList("Jupiter", "Neptune", "Mars", "Mercure"),
                0
        );
        Question question6 = new Question(
                "Combien de coeurs une pieuvre poss??de-t-elle ?",
                Arrays.asList("0", "3", "5", "18"),
                1
        );
        Question question7 = new Question(
                "Quel est l'??ge de l'univers ? (depuis le big bang)",
                Arrays.asList("6000 ans","4,6 milliards d'ann??es","13,7 milliards d'ann??es","l'univers est ??ternel"),
                2
        );
        Question question8 = new Question(
                "Quel est le num??ro atomique de l'??l??ment carbonne dans le tableau p??riodique des ??l??ments ?",
                Arrays.asList("1","6","12","24"),
                1
        );
        Question question9 = new Question(
                "Comment s'appelle la table sur laquelle le chimiste fait ses exp??riences ?",
                Arrays.asList("La paillasse","L'??tabli","Le comptoir","Le bureau"),
                0
        );
        Question question10 = new Question(
                "Quel grand physicien prix Nobel d??couvrit la radioactivit?? ?",
                Arrays.asList("Henri Becquerel","John Dalton","Fr??d??ric Joliot-Curie","Joseph Fourier"),
                0
        );
        Question question11 = new Question(
                "Quel est le 3??me ??tat de la mati??re ? Les deux premiers ??tant: solide, liquide.",
                Arrays.asList("Air","??mulsion","Gaz","Condens??"),
                2
        );
        Question question12 = new Question(
                "Comment s'appelle l'unit?? d'??nergie ?",
                Arrays.asList("La calorie","Le dalton","Le watt","Le joule"),
                3
        );
        Question question13 = new Question(
                "Qu'est-ce qu'un neutrino ?",
                Arrays.asList("Un atome neutre","Une particule ??l??mentaire","Un ??lectron libre","Un quark"),
                1
        );
        Question question14 = new Question(
                "Par quelle th??orie Einstein r??volutionna-t-il la physique ?",
                Arrays.asList("De l'??volution","Du complot","Des cordes","De la relativit?? g??n??rale"),
                3
        );
        Question question15 = new Question(
                "Il a fait des recherches sur des bact??ries mais il est c??l??bre pour son vaccin contre la rage.",
                Arrays.asList("Georges Cuvier","Louis Pasteur","Edward Jenner","Waldemar Haffkine"),
                1
        );
        Question question16 = new Question(
                "Il vivait 400 ans avant J-C et est consid??r?? comme ??tant le p??re de la m??decine.",
                Arrays.asList("Pythagore","Archim??de","Ptol??m??e","Hippocrate"),
                3
        );
        Question question17=new Question(
                "\"Rien ne se perd tout se transforme\", de qui vient cette c??l??bre phrase ?",
                Arrays.asList("Antoine de Lavoisier","Joseph Black","Georg-Ernest Stahl","Carl-Wilhelm Scheele"),
                0
        );
        Question question18=new Question(
                "Ce chimiste a travaill?? sur la classification p??riodique des ??l??ments.",
                Arrays.asList("Gregor Mendel","Ren?? Laennec","Dimitri Mendeleiev","Alfred Nobel"),
                2
        );
        Question question19=new Question(
                "Cet ??l??ve de Galil??e a fait des recherches sur la pression atmosph??rique et inventa le barom??tre.",
                Arrays.asList("Evangelista Torricelli","Giordano Bruno","Alessandro Volta","Thomas Edison"),
                0
        );
        Question question20=new Question(
                "Quel scientifique fran??ais a ??t?? ministre de la guerre sous la II??me R??publique ?",
                Arrays.asList("Ren?? Descartes","Fran??ois Arago","Denis Papin"," Alexis Bouvard"),
                1
        );
        Question question21=new Question(
                "Qui a donn?? son nom a l'unit?? d'intensit?? d'un courant ??l??ctrique ?",
                Arrays.asList("Charles Coulomb","James Watt","Thomas Edison","Andr?? Amp??re"),
                3
        );
        Question question22=new Question(
                "Il a perfectionn?? la lunette astronomique, invent?? le thermom??tre et ??tabli les lois du pendule. Cependant l'inquisition lui fait avouer qu'il est dans l'erreur et il sera emprisonn?? jusqu'?? sa mort.",
                Arrays.asList("Pierre-Simon Delaplace","Nicolas Copernic","Galil??e","Johannes Kepler"),
                2
        );
        Question question23=new Question(
                "Il a expliqu?? l'??volution par la s??l??ction naturelle des esp??ces",
                Arrays.asList("Jean-Baptiste de Lamarck","Charles Darwin","Georges de Buffon","John Gould"),
                1
        );
        Question question24=new Question(
                "Le bateau flotte car:",
                Arrays.asList("Son poids est plus grand que la pouss??e d'Archim??de","Son poids est plus petit que la pouss??e d'Archim??de","Son poids est ??gal ?? la pouss??e d'Archim??de","Il est attir?? par la Lune"),
                2
        );
        Question question25=new Question(
                "Quelle particularit?? a la date de d??c??s de Stephen Hawking ?",
                Arrays.asList("C'est le jour de sa propre naissance","C'est le jour dont l'existence des trous noirs a ??t?? prouv??e","C'est le jour de la naissance d'Albert Einstein","C'est le jour de la publication de \"la th??orie du rayonnement de Hawking\""),
                2
        );

        return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5, question6,question7,question8,question9,question10,question11,question12,question13,question14,question15,question16,question17,question18,question19,question20,question21,question22,question23,question24,question25));

    }
}
