package nathanmiles.nathangoldleaderboardtechtest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlayerActivity extends AppCompatActivity {
    TableLayout t1;
    String playerInfo;
    String playerScore;
    String courseURL = "https://leaderboard-techtest.herokuapp.com/api/1/courses/9000/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        //pull the player JSON block data and score from main intent
        playerInfo = intent.getStringExtra("personInfo");
        playerScore = intent.getStringExtra("personScore");
        //Setting up the tablelayout to show scorecard properly
        t1 = (TableLayout) findViewById(R.id.player_table);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );

        //The table row header. Its the first thing to appear on top
        TableRow tr_head = new TableRow(this);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5.0f));
        TextView hole = new TextView(this);
        hole.setText("HOLE");
        hole.setPadding(5, 5, 5, 5);
        hole.setTextColor(Color.WHITE);
        tr_head.addView(hole);
        for(int i = 1; i < 10; i ++){
            TextView holeNum = new TextView(this);
            holeNum.setText(String.valueOf(i));
            holeNum.setPadding(5, 5, 5, 5);
            holeNum.setTextColor(Color.WHITE);
            tr_head.addView(holeNum);
        }
        TextView frontNine = new TextView(this);
        frontNine.setText("Front 9");
        frontNine.setPadding(5, 5, 5, 5);
        frontNine.setTextColor(Color.WHITE);
        tr_head.addView(frontNine);

        //added some space text views to properly format
        TextView spacehold1 = new TextView(this);
        spacehold1.setText("");
        spacehold1.setPadding(5, 5, 5, 5);
        spacehold1.setTextColor(Color.WHITE);
        tr_head.addView(spacehold1);
        TextView spacehold2 = new TextView(this);
        spacehold2.setText("");
        spacehold2.setPadding(5, 5, 5, 5);
        spacehold2.setTextColor(Color.WHITE);
        tr_head.addView(spacehold2);
        t1.addView(tr_head);
        TextView sp1 = new TextView(this);
        sp1.setText("");
        sp1.setPadding(5, 5, 5, 5);
        TextView sp2 = new TextView(this);
        sp2.setText("");
        sp2.setPadding(5, 5, 5, 5);

        //First row pulls data from courses api and gets the par information and displays the
        //first nine holes, the total par number for the front nine
        TableRow tr_row1 = new TableRow(this);
        tr_row1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5.0f));
        TextView par = new TextView(this);
        par.setText("PAR");
        par.setPadding(5, 5, 5, 5);
        par.setTextColor(Color.WHITE);
        par.setBackgroundColor(Color.GRAY);
        tr_row1.addView(par);

        HttpGetRequest getCourseRequest = new HttpGetRequest();
        try{
            String courseResult = getCourseRequest.execute(courseURL).get();
            JSONObject json = new JSONObject(courseResult);
            json = json.getJSONObject("data");
            JSONArray holes = json.getJSONArray("holes");
            int totalPar = 0;
            for(int j = 0; j < 9; j ++){
                JSONObject holesHold = holes.getJSONObject(j);
                TextView parNum = new TextView(this);
                totalPar += holesHold.getInt("par");
                parNum.setText(holesHold.getString("par"));
                parNum.setPadding(5, 5, 5, 5);
                tr_row1.addView(parNum);

            }
            TextView parTotal = new TextView(this);
            parTotal.setText(String.valueOf(totalPar));
            parTotal.setPadding(5, 5, 5, 5);
            tr_row1.addView(parTotal);
            tr_row1.addView(sp1);
            tr_row1.addView(sp2);

            t1.addView(tr_row1);

            //Row 2 this has the player information that shows his scoares for the first 9 holes and the total
            TableRow tr_row2 = new TableRow(this);
            tr_row2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5.0f));
            TextView score = new TextView(this);
            score.setText("SCORE");
            score.setPadding(5, 5, 5, 5);
            score.setTextColor(Color.WHITE);
            score.setBackgroundColor(Color.GRAY);
            tr_row2.addView(score);
            JSONObject json2 = new JSONObject(playerInfo);
            json2 = json2.getJSONObject("data");
            JSONArray scoreHoles = json2.getJSONArray("holes");
            int frontn = 0;
            for(int k = 0; k < 12; k++ ){
                if(k < scoreHoles.length() && k < 9){
                    TextView scoreNum = new TextView(this);
                    frontn += scoreHoles.getInt(k);
                    scoreNum.setText(scoreHoles.getString(k));
                    scoreNum.setPadding(5, 5, 5, 5);
                    tr_row2.addView(scoreNum);
                }else if(k == 9){
                    TextView scoreTot = new TextView(this);
                    scoreTot.setText(String.valueOf(frontn));
                    scoreTot.setPadding(5, 5, 5, 5);
                    tr_row2.addView(scoreTot);
                }
                else{
                    TextView empty = new TextView(this);
                    empty.setText("");
                    empty.setPadding(5, 5, 5, 5);
                    tr_row2.addView(empty);
                }
            }
            t1.addView(tr_row2);

            //Second header Seperates the scorecard to tack everything
            //has nine holes, back nine total and score
            TableRow tr_head2 = new TableRow(this);
            tr_head2.setBackgroundColor(Color.GRAY);
            tr_head2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5.0f));
            TextView hole2 = new TextView(this);
            hole2.setText("HOLE");
            hole2.setPadding(5, 5, 5, 5);
            hole2.setTextColor(Color.WHITE);
            tr_head2.addView(hole2);
            for(int a = 10; a < 19; a ++){
                TextView holeNum2 = new TextView(this);
                holeNum2.setText(String.valueOf(a));
                holeNum2.setPadding(5, 5, 5, 5);
                holeNum2.setTextColor(Color.WHITE);
                tr_head2.addView(holeNum2);
            }
            TextView backnine = new TextView(this);
            backnine.setText("Back 9");
            backnine.setPadding(5, 5, 5, 5);
            backnine.setTextColor(Color.WHITE);
            tr_head2.addView(backnine);
            TextView total = new TextView(this);
            total.setText("TOTAL");
            total.setPadding(5, 5, 5, 5);
            total.setTextColor(Color.WHITE);
            tr_head2.addView(total);
            TextView scoretot = new TextView(this);
            scoretot.setText("SCORE");
            scoretot.setPadding(5, 5, 5, 5);
            scoretot.setTextColor(Color.WHITE);
            tr_head2.addView(scoretot);
            t1.addView(tr_head2);

            //row 3 is the course par values and total par
            TableRow tr_row3 = new TableRow(this);
            tr_row3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5.0f));
            TextView par2 = new TextView(this);
            par2.setText("PAR");
            par2.setPadding(5, 5, 5, 5);
            par2.setTextColor(Color.WHITE);
            par2.setBackgroundColor(Color.GRAY);
            tr_row3.addView(par2);
            int totalBackPar = 0;
            for(int b = 9; b < holes.length(); b ++){
                JSONObject backholesHold = holes.getJSONObject(b);
                TextView backparNum = new TextView(this);
                totalBackPar += backholesHold.getInt("par");
                backparNum.setText(backholesHold.getString("par"));
                backparNum.setPadding(5, 5, 5, 5);
                tr_row3.addView(backparNum);


            }
            TextView backParTotal = new TextView(this);
            backParTotal.setText(String.valueOf(totalBackPar));
            backParTotal.setPadding(5, 5, 5, 5);
            tr_row3.addView(backParTotal);
            TextView completeTotal = new TextView(this);
            completeTotal.setText(String.valueOf(totalBackPar + totalPar));
            completeTotal.setPadding(5, 5, 5, 5);
            tr_row3.addView(completeTotal);
            TextView empty2 = new TextView(this);
            empty2.setText("");
            empty2.setPadding(5, 5, 5, 5);
            tr_row3.addView(empty2);
            t1.addView(tr_row3);

            //row 4 is the players last 9 holes and total and score
            TableRow tr_row4 = new TableRow(this);
            tr_row4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5.0f));
            TextView score2 = new TextView(this);
            score2.setText("SCORE");
            score2.setPadding(5, 5, 5, 5);
            score2.setTextColor(Color.WHITE);
            score2.setBackgroundColor(Color.GRAY);
            tr_row4.addView(score2);
            int backint = 0;
            for(int c = 9; c < holes.length(); c++ ){
                if(c < scoreHoles.length()) {
                    TextView scoreNumBack = new TextView(this);
                    backint += scoreHoles.getInt(c);
                    scoreNumBack.setText(scoreHoles.getString(c));
                    scoreNumBack.setPadding(5, 5, 5, 5);
                    tr_row4.addView(scoreNumBack);

                }else{
                    TextView empty3 = new TextView(this);
                    empty3.setText("");
                    empty3.setPadding(5, 5, 5, 5);
                    tr_row4.addView(empty3);
                }
            }
            TextView backScoretotal = new TextView(this);
            backScoretotal.setText(String.valueOf(backint));
            backScoretotal.setPadding(5, 5, 5, 5);
            tr_row4.addView(backScoretotal);
            TextView backCompTotal = new TextView(this);
            backCompTotal.setText(String.valueOf(backint + frontn));
            backCompTotal.setPadding(5, 5, 5, 5);
            tr_row4.addView(backCompTotal);
            TextView endscore = new TextView(this);
            endscore.setText(String.valueOf(playerScore));
            endscore.setPadding(5, 5, 5, 5);
            tr_row4.addView(endscore);
            t1.addView(tr_row4);

        }catch (Exception e){
            e.printStackTrace();

        }

    }

}
