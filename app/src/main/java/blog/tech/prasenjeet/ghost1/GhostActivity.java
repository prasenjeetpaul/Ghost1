
package blog.tech.prasenjeet.ghost1;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static blog.tech.prasenjeet.ghost1.R.id.ghostText;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView ghostWord ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        //Code Implemented
        try
        {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);

        } catch (IOException e)
        {
            Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG).show();
        }

        ghostWord = (TextView) findViewById(ghostText);
        findViewById(R.id.challengeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                challengeButtonHandler();

            }
        });

        findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(v);
            }
        });

        onStart(null);
    }

    private void challengeButtonHandler()
    {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again

        String ghostCurrentWord = ghostWord.getText().toString();

        if(ghostCurrentWord.length()>=4 && dictionary.isWord(ghostCurrentWord))
        {
            label.setText("User Won!!!");
        }
        else
        {
            String newWord = dictionary.getAnyWordStartingWith(ghostCurrentWord);
            Log.d("Ghost", "Word starting with " + ghostText + "is " + newWord);
            if(newWord == null)
            {
                label.setText("User Won!!");
            }
            else
            {
                label.setText("Computer Won!! \nPossible words: "+newWord);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu_ghost; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view)
    {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn()
    {


        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // do something
                TextView label = (TextView) findViewById(R.id.gameStatus);
                // Do computer turn stuff then make it the user's turn again

                String ghostCurrentWord = ghostWord.getText().toString();

                if(ghostCurrentWord.length()>=4 && dictionary.isWord(ghostCurrentWord))
                {
                    label.setText("Computer Won!!!");
                }
                else
                {
                    String newWord = dictionary.getAnyWordStartingWith(ghostCurrentWord);
                    Log.d("Ghost", "Word starting with " + ghostText + "is " + newWord);
                    if(newWord == null)
                    {
                        label.setText("Computer Won!!");
                    }
                    else
                    {
                        ghostWord.append(newWord.charAt(ghostCurrentWord.length())+"");
                        userTurn = true;
                        label.setText(USER_TURN);
                    }
                }
            }
        }, 1500);





    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        //Code Implemented
        char keyPressed = (char) event.getUnicodeChar();
        //Log.d("Key Pressed: ", keyPressed+"hola "+keyCode+" "+event);

        if(Character.isLetter(keyPressed))
        {
            ghostWord.append(keyPressed+"");
            //ghostWord.setText(ghostWord.getText()+""+keyPressed);
            TextView label = (TextView) findViewById(R.id.gameStatus);
            label.setText(COMPUTER_TURN);
            computerTurn();
            return true;
        }




        return super.onKeyUp(keyCode, event);
    }
}
