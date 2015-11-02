package com.wesleyreisz.mymusic;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.wesleyreisz.mymusic.fragment.ListFragment;
import com.wesleyreisz.mymusic.fragment.MyListFragment;
import com.wesleyreisz.mymusic.fragment.NewListActivityFragment;
import com.wesleyreisz.mymusic.fragment.SongFragment;
import com.wesleyreisz.mymusic.model.Song;
import com.wesleyreisz.mymusic.service.MockMusicService;

import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements
        NewListActivityFragment.OnItemChange,
        SongFragment.OnReloadClick {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        //test it
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewListActivityFragment listFragment = new NewListActivityFragment();
        fragmentTransaction.replace(R.id.fragmentContainer, listFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void ItemClicked(int changeToSongPosition, String songTitle) {
        Toast toast = Toast.makeText(this, "Position: " + changeToSongPosition, Toast.LENGTH_SHORT);
        toast.show();

        Song song = new MockMusicService().findOne(songTitle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SongFragment songFragment = new SongFragment();
        songFragment.setSong(song);

        if(findViewById(R.id.fragmentContainerRight)!=null){
            fragmentTransaction.replace(R.id.fragmentContainerRight, songFragment);
        }else{
            fragmentTransaction.replace(R.id.fragmentContainer, songFragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void reload() {
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NewListActivityFragment listFragment = new NewListActivityFragment();
        fragmentTransaction.replace(R.id.fragmentContainer, listFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_new){
            addSong();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //replace this with a fragment
    private void addSong() {
        ParseObject songObject = new ParseObject("Song");
        songObject.put("songTitle","My Song");
        songObject.put("artistTitle","The Artist is");
        songObject.put("album","Album Name");
        songObject.put("date",new Date());
        songObject.saveInBackground();
    }

}
