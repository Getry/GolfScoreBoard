package nathanmiles.nathangoldleaderboardtechtest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    HttpURLConnection apiURLConnection;
    URL myURL;
    JSONObject json;
    StringBuilder result = new StringBuilder();
    //Stored the api url that are needed
    String leaderBoardUrl ="https://leaderboard-techtest.herokuapp.com/api/1/events/1000/leaderboard/" ;
    String playerUrl = "https://leaderboard-techtest.herokuapp.com/api/1/events/1000/player/" ;
    String playerNameUrl = "https://leaderboard-techtest.herokuapp.com/api/1/players/";
    String resultHold;
    TableLayout t1;
    JSONArray players;
    //arrays to track player information to pass
    ArrayList<Integer> playerIdHold;
    ArrayList<Integer> playerScoreHold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerIdHold = new ArrayList<Integer>();
        playerScoreHold = new ArrayList<Integer>();
        //create table to display the scoreboard
        t1 = (TableLayout) findViewById(R.id.main_table);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        //creation of the first tablerow which is the header
        TableRow tr_head = new TableRow(this);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5.0f));
        TextView pos = new TextView(this);
        pos.setText("POS");
        pos.setPadding(5, 5, 5, 5);
        pos.setTextColor(Color.WHITE);
        tr_head.addView(pos);
        TextView playTitle = new TextView(this);
        playTitle.setText("PLAYER");
        playTitle.setPadding(5, 5, 5, 5);
        playTitle.setTextColor(Color.WHITE);
        tr_head.addView(playTitle);
        TextView tot = new TextView(this);
        tot.setText("TOT");
        tot.setPadding(5, 5, 5, 5);
        tot.setTextColor(Color.WHITE);
        tr_head.addView(tot);
        TextView score = new TextView(this);
        score.setText("SCORE");
        score.setPadding(5, 5, 5, 5);
        score.setTextColor(Color.WHITE);
        tr_head.addView(score);
        TextView thru = new TextView(this);
        thru.setText("THRU");
        thru.setPadding(5, 5, 5, 5);
        thru.setTextColor(Color.WHITE);
        tr_head.addView(thru);
        t1.addView(tr_head);

        //Access the api to pull the scoreboard data and then
        //displays it using dynmanic tablerow building
        HttpGetRequest getEventRequest = new HttpGetRequest();
        HttpGetRequest getPersonRequest;
        try {
            resultHold = getEventRequest.execute(leaderBoardUrl).get();
            json = new JSONObject(resultHold);
            players = json.getJSONArray("data");

            for(int i = 0; i < players.length();i++){
                //parses json object to gather player information
                JSONObject playHold = players.getJSONObject(i);
                String rankHold = playHold.getString("rank");
                int playerId = playHold.getInt("player_id");
                int playerScore = playHold.getInt("score");
                int playerTotal = playHold.getInt("total");
                int playerThru = playHold.getInt("thru");
                playerIdHold.add(playerId);
                playerScoreHold.add(playerScore);
                getPersonRequest = new HttpGetRequest();
                String personHold = getPersonRequest.execute(playerNameUrl + playerId + "/").get();

                JSONObject personJson = new JSONObject(personHold);
                JSONObject playNameHold = personJson.getJSONObject("data");
                String fullName = playNameHold.getString("first_name") + " " + playNameHold.getString("last_name");

                //creates a new table row to add the player
                TableRow tr_new_row = new TableRow(this);
                tr_new_row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5.0f));
                tr_new_row.setId(i);
                TextView rankOfPlayer = new TextView(this);
                rankOfPlayer.setText(rankHold);
                rankOfPlayer.setPadding(5, 5, 5, 5);;
                tr_new_row.addView(rankOfPlayer);
                TextView nameOfPlayer = new TextView(this);
                nameOfPlayer.setText(fullName);
                nameOfPlayer.setPadding(5, 5, 5, 5);
                tr_new_row.addView(nameOfPlayer);
                TextView playerTot = new TextView(this);
                playerTot.setText(String.valueOf(playerTotal));
                playerTot.setPadding(5, 5, 5, 5);
                tr_new_row.addView(playerTot);
                TextView scoreOfPlayer = new TextView(this);
                if(playerScore == 0)
                    scoreOfPlayer.setText("EVEN");
                else
                    scoreOfPlayer.setText(String.valueOf(playerScore));
                scoreOfPlayer.setPadding(5, 5, 5, 5);
                tr_new_row.addView(scoreOfPlayer);
                TextView thruOfPlayer = new TextView(this);
                if(playerThru == 0)
                    thruOfPlayer.setText("-");
                else if(playerThru == 18)
                    thruOfPlayer.setText("F");
                else
                    thruOfPlayer.setText(String.valueOf(playerThru));
                thruOfPlayer.setPadding(5, 5, 5, 5);
                tr_new_row.addView(thruOfPlayer);
                tr_new_row.setOnClickListener(this);
                t1.addView(tr_new_row);

            }
        }  catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        int clicked_id = v.getId(); // here you get id for clicked TableRow
        //Grab the information for player from the api and the score and pass it to the player intent
        HttpGetRequest getPersonInfoRequest = new HttpGetRequest();
        String personResult;
        try{
            personResult = getPersonInfoRequest.execute(playerUrl + playerIdHold.get(clicked_id) +"/").get();
            Intent personIntent = new Intent(this,PlayerActivity.class);
            personIntent.putExtra("personInfo", personResult);
            String scoreTemp = String.valueOf(playerScoreHold.get(clicked_id));
            personIntent.putExtra("personScore", scoreTemp);
            startActivity(personIntent);
        }catch(Exception e){
            e.printStackTrace();
        }


    }


}
