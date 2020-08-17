package com.example.voting_rights_rys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Resources extends AppCompatActivity {
    SharedPreferences sp;
    private FrameLayout fragContainer;
    private HashMap <String, ArrayList<Object>> states = new HashMap<>();
    HashMap<String, String> AbbvToState = new HashMap<>();

    private String userState; // should be all lower case, get this from settings page
    // Opens links within app
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
    CustomTabsIntent customTabsIntent = builder.build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        //Initialize bottom nav bar and select "Resources"
        BottomNavigationView bottomNavView = findViewById(R.id.bot_nav);
        bottomNavView.setSelectedItemId(R.id.resources);

        //Initialize fragment container
        fragContainer = (FrameLayout) findViewById(R.id.fragment_container);

        // Parse Json data for how to register
        loadJSONFromAsset(); parseJSON();

        // Start a new activity when a nav bar item is selected
        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.elections:
                        startActivity(new Intent(getApplicationContext(), Elections.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.resources:
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        populateMap();
        // Get State from Settings Page
        sp = getSharedPreferences("USER_STATE", Context.MODE_PRIVATE);
        userState = sp.getString("state","");

        // If it's empty, default it to NY. Otherwise, change it to the user state.
        if(userState.equals("")) {userState = "new york";}
        else {userState = (AbbvToState.get(userState.toUpperCase())).toLowerCase();}
    }

    public void populateMap() {
        AbbvToState.put("AL","Alabama");
        AbbvToState.put("AK","Alaska");
        AbbvToState.put("AZ","Arizona");
        AbbvToState.put("AR","Arkansas");
        AbbvToState.put("CA", "California");
        AbbvToState.put("CO","Colorado");
        AbbvToState.put("CT","Connecticut");
        AbbvToState.put("DE","Delaware");
        AbbvToState.put("DC","District Of Columbia");
        AbbvToState.put("FL","Florida");
        AbbvToState.put("GA","Georgia");
        AbbvToState.put("HI","Hawaii");
        AbbvToState.put("ID","Idaho");
        AbbvToState.put("IL","Illinois");
        AbbvToState.put("IN","Indiana");
        AbbvToState.put("IA","Iowa");
        AbbvToState.put("KS","Kansas");
        AbbvToState.put("KY","Kentucky");
        AbbvToState.put("LA","Louisiana");
        AbbvToState.put("ME","Maine");
        AbbvToState.put("MD","Maryland");
        AbbvToState.put("MA","Massachusetts");
        AbbvToState.put("MI","Michigan");
        AbbvToState.put("MN","Minnesota");
        AbbvToState.put("MS","Mississippi");
        AbbvToState.put("MO","Missouri");
        AbbvToState.put("MT","Montana");
        AbbvToState.put("NE","Nebraska");
        AbbvToState.put("NV","Nevada");
        AbbvToState.put("NH","New Hampshire");
        AbbvToState.put("NJ","New Jersey");
        AbbvToState.put("NM","New Mexico");
        AbbvToState.put("NY","New York");
        AbbvToState.put("NC","North Carolina");
        AbbvToState.put("ND","North Dakota");
        AbbvToState.put("OH","Ohio");
        AbbvToState.put("OK","Oklahoma");
        AbbvToState.put("OR","Oregon");
        AbbvToState.put("PA","Pennsylvania");
        AbbvToState.put("RI","Rhode Island");
        AbbvToState.put("SC","South Carolina");
        AbbvToState.put("SD","South Dakota");
        AbbvToState.put("TN","Tennessee");
        AbbvToState.put("TX","Texas");
        AbbvToState.put("UT","Utah");
        AbbvToState.put("VT","Vermont");
        AbbvToState.put("VA","Virginia");
        AbbvToState.put("WA","Washington");
        AbbvToState.put("WV","West Virginia");
        AbbvToState.put("WI","Wisconsin");
        AbbvToState.put("WY","Wyoming");
    }

    //Called when button clicked
    public void validID(View view){
        /**Note: Still give option to see valid forms of id for other states as it will vary
         * using a drop down menu*/
        //Show the user the valid forms of ID for his/her state
        String url = ((String [])(states.get(userState).get(4)))[1];
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
    //Called when button clicked
    public void voterRegistrationStatus(View view){
        //Use an API to automatically check user's registration status if possible
        //Alternative: provide user with directions on how to check voter registration status
        String url = ((String [])(states.get(userState).get(4)))[0];
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
    //Called when button clicked
    public void absenteeBallot(View view){
        //Pull information from other sites and display in app using fragment (give credit to sources)
        String url = ((String [])(states.get(userState).get(4)))[2];
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
    //Called when button clicked
    public void earlyVoting(View view){
        //Pull information from other sites and display in app using fragment (give credit to sources)
        String url = ((String [])(states.get(userState).get(4)))[3];
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
    //Called when button clicked - Redirects to in app browser which displays website
    public void iSideWith(View view){
        //Website url
        String url = "https://www.isidewith.com/";
        //Create a builder to create the customTabsIntent
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
    //Called when button clicked
    public void howToRegisterToVote(View view){
        //Pull information from other sites and display in app using fragment (give credit to sources)
        openFragment();
    }

    // Opens Fragment
    public void openFragment() {
      RegisterFragment frag = RegisterFragment.newInstance();
      // get state info from map and pass it to fragment to display
       ArrayList <Object> stateInfo = states.get(userState);
       Bundle args = new Bundle();
       args.putString("stateName", (String)stateInfo.get(0));
       args.putString("abbv", (String)stateInfo.get(1));
       args.putStringArray("info", (String[]) stateInfo.get(2));
       args.putBoolean("regNeeded", (Boolean) stateInfo.get(3));
       frag.setArguments(args);

       FragmentManager fManager = getSupportFragmentManager();
       FragmentTransaction transaction = fManager.beginTransaction();
       transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
       transaction.addToBackStack(null);
       transaction.add(R.id.fragment_container, frag,"REGISTER_FRAGMENT").commit();
    }

    // loads JSON file and parses data for How to Register to vote page
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("state-info.JSON");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void parseJSON() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray arr = obj.getJSONArray("states");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject jObj = arr.getJSONObject(i);
                ArrayList<Object> stateInfo = new ArrayList<>();
                String [] rules;
                String [] links;

                // add state name
                stateInfo.add(jObj.getString("name"));

                // get state abbv
                stateInfo.add(jObj.getString("abbr"));

                // get state reqs
                JSONArray rulesArr = jObj.getJSONArray("rules");

                rules = new String [rulesArr.length()];
                for (int r = 0; r < rulesArr.length(); r++) {
                    rules[r] = rulesArr.getString(r);
                }
                stateInfo.add(rules);

                // get registration needed
                stateInfo.add(jObj.getBoolean("registration_needed"));

                // add links
                JSONArray linksArr = jObj.getJSONArray("links");
                links = new String [linksArr.length()];
                for (int l= 0; l < linksArr.length(); l++) {
                    links[l] = linksArr.getString(l);
                }
                stateInfo.add(links);

                // add to hashMap "state":info
                states.put(jObj.getString("name").toLowerCase(), stateInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
