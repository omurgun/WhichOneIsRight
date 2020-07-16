package com.omurgun.whichoneisright.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omurgun.whichoneisright.Manager.HomeMenuActivity;
import com.omurgun.whichoneisright.Model.Question;
import com.omurgun.whichoneisright.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private static final long START_TIME_IN_MILLIS = 90000;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private boolean mTimerRunning;
    private CountDownTimer mBtnColorCountDownTimer;
    private long mBtnColorTimeLeftInMillis=500;
    private Button btnPause;
    private MediaPlayer player;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private SharedPreferences sharedPreferences;
    private Button btnLeft,btnRight;
    private Handler handler;
    private Runnable runnable;
    private int maxScore;
    private int level;
    private int delay;
    private int score;
    private int know;
    private int questionTrueKnow;
    private int questionFalseKnow;
    private String optionTrueAnswer;
    private TextView scoreText;
    private TextView timeText;
    private String backName;
    private TextView levelText;
    private MediaPlayer playerInGame;
    private int pauseMusicPosition;
    private ConstraintLayout layout;
    private ArrayList<Integer> seeQuestion;
    private ArrayList<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();
        sharedPreferences = this.getSharedPreferences("com.omurgun.whiconeisright", Context.MODE_PRIVATE);
        maxScore = sharedPreferences.getInt("maxScore", 0);
        backName = sharedPreferences.getString("back","back_default");
        setGameBack();
        translucentStatusBarFlag();
        countTime();
        game();
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               player.start();
                if (mTimerRunning) {
                    System.out.println("stop");
                    musicPause();
                    pauseTimer();
                }
                else {
                    System.out.println("start");
                    musicPlay();
                    startTimer();
                }

            }
        });
    }
    private void setGameBack() {
        if(!backName.isEmpty())
        {
            Drawable drawable;
            if(backName.equals("back_default"))
            {
                drawable = getResources().getDrawable(R.drawable.ripple_effect_raise_white);
                layout.setBackground(drawable);
            }
            else if(backName.equals("back_0"))
            {
                drawable = getResources().getDrawable(R.drawable.back);
                layout.setBackground(drawable);
            }
            else if(backName.equals("back_1"))
            {
                drawable = getResources().getDrawable(R.drawable.back1);
                layout.setBackground(drawable);
            }
            else if(backName.equals("back_2"))
            {
                drawable = getResources().getDrawable(R.drawable.back2);
                layout.setBackground(drawable);
            }
            else if(backName.equals("back_3"))
            {
                drawable = getResources().getDrawable(R.drawable.back3);
                layout.setBackground(drawable);
            }
            else if(backName.equals("back_4"))
            {
                drawable = getResources().getDrawable(R.drawable.back4);
                layout.setBackground(drawable);
            }
            else if(backName.equals("back_5"))
            {
                drawable = getResources().getDrawable(R.drawable.back5);
                layout.setBackground(drawable);
            }
            else if(backName.equals("back_6"))
            {
                drawable = getResources().getDrawable(R.drawable.back6);
                layout.setBackground(drawable);
            }
            else if(backName.equals("back_7"))
            {
                drawable = getResources().getDrawable(R.drawable.back7);
                layout.setBackground(drawable);
            }
            else if(backName.equals("back_8"))
            {
                drawable = getResources().getDrawable(R.drawable.back8);
                layout.setBackground(drawable);
            }
            else if(backName.equals("back_9"))
            {
                drawable = getResources().getDrawable(R.drawable.back9);
                layout.setBackground(drawable);
            }
        }
    }
    private void init() {
        questionTrueKnow=0;
        questionFalseKnow=0;
        maxScore = 0;
        know=0;
        level = 1;
        delay = 2100;
        score = 0;
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        timeText = findViewById(R.id.timeText);
        scoreText = findViewById(R.id.scoreText);
        levelText = findViewById(R.id.levelText);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnPause = findViewById(R.id.btnPause);
        player = MediaPlayer.create(GameActivity.this,R.raw.song);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        playerInGame = MediaPlayer.create(GameActivity.this,R.raw.songgame);
        layout = findViewById(R.id.layout_game);
        seeQuestion = new ArrayList<Integer>();
        addQuestions();

    }
    private void game() {
        Question question =changeQuestion();
        musicPlay();
        optionTrueAnswer = question.getOptionTrue();
        setButtonQuestion(question.getOptionA(),question.getOptionB());
        startTimer();

    }
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished <200)
                {
                    btnRight.setVisibility(View.INVISIBLE);
                    btnLeft.setVisibility(View.INVISIBLE);
                }
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                timeText.setText("Time is up");
                handler.removeCallbacks(runnable);
                openDialog();
            }
        }.start();
        mTimerRunning = true;
        Drawable drawable = getResources().getDrawable(R.drawable.pause);
        btnPause.setBackground(drawable);
        btnLeft.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.VISIBLE);
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        Drawable drawable = getResources().getDrawable(R.drawable.play);
        btnPause.setBackground(drawable);
        btnLeft.setVisibility(View.INVISIBLE);
        btnRight.setVisibility(View.INVISIBLE);
    }
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timeText.setText(timeLeftFormatted);
    }
    private void countTime() {

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,delay-(level*100));

            }
        };
        handler.post(runnable);
    }
    private void openDialog() {
        btnLeft.setVisibility(View.INVISIBLE);
        btnRight.setVisibility(View.INVISIBLE);
        saveMaxScore();
        AlertDialog.Builder alert = new AlertDialog.Builder(GameActivity.this);
        alert.setTitle("Restart?");
        alert.setMessage("Are you sure to restart game?\nTrue : "+questionTrueKnow+"\t\t\t"+"False : "+questionFalseKnow);
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(GameActivity.this, "MAX PUAN : "+score, Toast.LENGTH_SHORT).show();
                //restart
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(GameActivity.this, "MAX PUAN : "+score, Toast.LENGTH_SHORT).show();
                goLogin();
            }
        });
        alert.show();
    }
    private void addQuestions() {
        questions.add(new Question("Hiç bir","Hiçbir", "Hiçbir")); //B
        questions.add(new Question("Ardarda","Art arda", "Art arda")); //B
        questions.add(new Question("İnisiyatif","İnsiyatif", "İnisiyatif")); //A
        questions.add(new Question("Herhangi","Her hangi", "Herhangi"));  //A
        questions.add(new Question("Arasöz","Ara söz","Ara söz")); //B
        questions.add(new Question("Dersane","Dershane","Dershane")); //B
        questions.add(new Question("Amfi","Anfi","Amfi")); //A
        questions.add(new Question("Pek çok","Pekçok","Pek çok")); //A
        questions.add(new Question("Eşşek","Eşek","Eşek")); //B
        questions.add(new Question("Karnıbahar","Karnabahar","Karnabahar")); //B

        questions.add(new Question("Dinozor","Dinazor","Dinozor")); //A
        questions.add(new Question("Direkt","Direk","Direkt")); //A
        questions.add(new Question("Kapütülasyon","Kapitülasyon","Kapitülasyon")); //B
        questions.add(new Question("Kollektif","Kolektif","Kolektif")); //B
        questions.add(new Question("Kontör","Kontür","Kontör")); //A
        questions.add(new Question("Silahşor","Silahşör","Silahşor")); //A
        questions.add(new Question("Tesbit","Tespit","Tespit")); //B
        questions.add(new Question("Kavonoz","Kavanoz","Kavanoz")); //B
        questions.add(new Question("Parantez","Parentez","Parantez")); //A
        questions.add(new Question("Flüt","Fülüt","Flüt")); //A

        questions.add(new Question("Beysbol","Beyzbol","Beyzbol")); //B
        questions.add(new Question("Entellektüel","Entelektüel","Entelektüel")); //B
        questions.add(new Question("Komiser","Komser","Komiser")); //A
        questions.add(new Question("Aforoz","Afaroz","Aforoz")); //A
        questions.add(new Question("Izdırap","Istırap","Istırap")); //B
        questions.add(new Question("Fantazi","Fantezi","Fantezi")); //B
        questions.add(new Question("Espri","Espiri","Espri")); //A
        questions.add(new Question("Egzoz","Ekzoz","Egzoz")); //A
        questions.add(new Question("Pilaj","Plaj","Plaj")); //B
        questions.add(new Question("Traş","Tıraş","Tıraş")); //B

        questions.add(new Question("Şoför","Şöför","Şoför")); //A
        questions.add(new Question("Metot","Metod","Metot")); //A
        questions.add(new Question("Zatüre","Zatürre","Zatürre")); //B
        questions.add(new Question("Deynek","Değnek","Değnek")); //B
        questions.add(new Question("Sürpriz","Süpriz","Sürpriz")); //A
        questions.add(new Question("Poğaça","Poaça","Poğaça")); //A
        questions.add(new Question("Sandoviç","Sandviç","Sandviç")); //B
        questions.add(new Question("Antreman","Antrenman","Antrenman")); //B
        questions.add(new Question("Çember","Çenber","Çember")); //A
        questions.add(new Question("Makine","Makina","Makine")); //A

        questions.add(new Question("Matba","Matbaa","Matbaa")); //B
        questions.add(new Question("Orjinal","Orijinal","Orijinal")); //B
        questions.add(new Question("Kılavuz","Klavuz","Kılavuz")); //A
        questions.add(new Question("Röportaj","Ropörtaj","Röportaj")); //A
        questions.add(new Question("İnsiyatif","İnisiyatif","İnisiyatif")); //B
        questions.add(new Question("Mataryal","Materyal","Materyal")); //B
        questions.add(new Question("Yalnız","Yanlız","Yalnız")); //A
        questions.add(new Question("Kirpik","Kiprik","Kirpik")); //A
        questions.add(new Question("Kirbit","Kibrit","Kibrit")); //B
        questions.add(new Question("Pehriz","Perhiz","Perhiz")); //B

        questions.add(new Question("Psikolojik","Pisikoljk","Psikolojik")); //A
        questions.add(new Question("Öngörmek","Ön görmek","Öngörmek")); //A
        questions.add(new Question("Karma karışık","Karmakarışık","Karmakarışık")); //B
        questions.add(new Question("Rastgelmek","Rast gelmek","Rast gelmek")); //B
        questions.add(new Question("Ya da ","Yada","Ya da")); //A
        questions.add(new Question("Yanlış","Yalnış","Yanlış")); //A
        questions.add(new Question("Pekçok","Pek çok","Pek çok")); //B
        questions.add(new Question("Pekaz","Pek az","Pek az")); //B
        questions.add(new Question("Ara sıra","Arasıra","Ara sıra")); //A
        questions.add(new Question("Yanısıra","Yanı sıra","Yanısıra")); //A

        questions.add(new Question("Peşisıra","Peşi sıra","Peşi sıra")); //B
        questions.add(new Question("Ardısıra","Ardı sıra","Ardı sıra")); //B
        questions.add(new Question("Akşamüstü","Akşam üstü","Akşamüstü")); //A
        questions.add(new Question("Ayaküstü","Ayak üstü","Ayaküstü")); //A
        questions.add(new Question("Suç üstü","Suçüstü","Suçüstü")); //B
        questions.add(new Question("Vaz geçmek","Vazgeçmek","Vazgeçmek")); //B
        questions.add(new Question("Başvurmak","Baş vurmak","Başvurmak")); //A
        questions.add(new Question("Hak etmek","Haketmek","Hak etmek")); //A
        questions.add(new Question("Ve ya","Veya","Veya")); //B
        questions.add(new Question("Birgün","Bir gün","Bir gün")); //B

        //70
    }
    private int randNumber() {
        Random random = new Random();
        int i= random.nextInt(questions.size());
        return i;
    }
    private Boolean controlQuestion(int newQuestion) {
        boolean result = false;
        if(seeQuestion.size() < 1)
        {
            result = true;
        }
        else
        {
            if(seeQuestion.size()<70)
            {
                for(int j : seeQuestion)
                {
                    if(newQuestion != j)
                    {
                        result = true;
                        System.out.println("result "+ result);

                    }
                    else
                    {
                        result = false;
                        System.out.println("\t\t\t  YYYZZZHHH "+ result);
                        break;
                    }
                    System.out.println("newQuestion:"+newQuestion +"\t j :"+j);
                }
            }
            else
            {
                result = false;
            }

        }
        return result;
    }
    private Question changeQuestion() {
        int i;
        Question question = new Question("null","null","null");
        boolean result;
        boolean gameResult=true;
        System.out.println("\t\t RRRTTTYYY"+seeQuestion.size());
        while (gameResult)
        {
            i = randNumber();
            result = controlQuestion(i);
            if(result)
            {
                System.out.println("add : "+i);
                seeQuestion.add(i);
                question = questions.get(i);
                System.out.println("selected : "+"A : "+question.getOptionA()+"\tB : "+question.getOptionB()+"\tTrue : "+question.getOptionTrue());
                break;
            }
        }
        return question;
    }
    private void setButtonQuestion(String optionA ,String optionB) {
        btnLeft.setText(optionA);
        btnRight.setText(optionB);
        System.out.println("left : "+optionA+"\tright :"+optionB);
    }
    private void isTheAnswerCorrect(Button btnClick ,Button btnNotClick) {
        String btnAnswer = btnClick.getText().toString();
        System.out.println("clicked : "+btnAnswer +"\ttrue :"+optionTrueAnswer);
        Drawable drawableGreen = getResources().getDrawable(R.drawable.ripple_effect_raise_green);
        Drawable drawableRed = getResources().getDrawable(R.drawable.ripple_effect_raise_red);
        final Drawable drawableRight = getResources().getDrawable(R.drawable.ripple_effect_raise);
        final Drawable drawableLeft = getResources().getDrawable(R.drawable.ripple_effect_raise);

        mBtnColorCountDownTimer = new CountDownTimer(mBtnColorTimeLeftInMillis, 1000) {

            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {

                btnRight.setBackground(drawableRight);
                btnLeft.setBackground(drawableLeft);
                Question question =changeQuestion();
                optionTrueAnswer = question.getOptionTrue();
                setButtonQuestion(question.getOptionA(),question.getOptionB());
                btnRight.setClickable(true);
                btnLeft.setClickable(true);
            }

        }.start();
       if(btnAnswer.equals(optionTrueAnswer))
       {
           pauseTimer();
           btnClick.setBackground(drawableGreen);
           btnNotClick.setBackground(drawableRed);
           increaseScore();
           know++;
           questionTrueKnow++;
           mTimeLeftInMillis +=2000;
           startTimer();

       }
       else if(!btnAnswer.equals(optionTrueAnswer))
       {
           pauseTimer();
           btnClick.setBackground(drawableRed);
           btnNotClick.setBackground(drawableGreen);
           questionFalseKnow++;
           mTimeLeftInMillis -=3500;
           startTimer();
       }
       if(know==5) {
           increaseLevel();
           know = 0;
       }

        if(seeQuestion.size() == 69)
        {
            handler.removeCallbacks(runnable);
            pauseTimer();
            openDialog();
        }

    }
    @Override
    public void onClick(View v) {
       int id = v.getId();

       if(id == R.id.btnRight)
       {
           player.start();
           btnRight.setClickable(false);
           btnLeft.setClickable(false);
           isTheAnswerCorrect(btnRight,btnLeft);

       }
       else if(id ==R.id.btnLeft)
       {
           player.start();
           btnRight.setClickable(false);
           btnLeft.setClickable(false);
           isTheAnswerCorrect(btnLeft,btnRight);
       }
    }
    private void musicPlay() {
        if(playerInGame == null)
        {
            playerInGame.start();
        }
        else if(!playerInGame.isPlaying())
        {
            playerInGame.seekTo(pauseMusicPosition);
            playerInGame.start();
        }
    }
    private void musicPause() {
        if(playerInGame != null)
        {
            playerInGame.pause();
            pauseMusicPosition = playerInGame.getCurrentPosition();
        }
    }
    private void musicStop() {
        if(playerInGame != null)
        {
            playerInGame.stop();
            playerInGame = null;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        musicPause();
        pauseTimer();
    }
    private void translucentStatusBarFlag() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void saveMaxScore() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        String max;
        if (score > maxScore){
            maxScore = score;
            max = String.valueOf(maxScore);
            sharedPreferences.edit().putInt("maxScore", maxScore).apply();
            HashMap<String,Object> maxMap = new HashMap<>();
            maxMap.put("MaxScore",max);
            firebaseFirestore.collection("Users")
                    .document(uid).update(maxMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(GameActivity.this, "save..", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GameActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void goLogin() {
        Intent intentLogin = new Intent(GameActivity.this, HomeMenuActivity.class);
        musicStop();
        startActivity(intentLogin);
        finish();
    }
    private void increaseScore () {
        score = score +level*5;
        scoreText.setText("Score: " + score);
    }
    private void increaseLevel () {
        level++;
        levelText.setText("Level: " + level);
    }
}