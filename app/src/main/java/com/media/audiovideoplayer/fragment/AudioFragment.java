package com.media.audiovideoplayer.fragment;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.media.audiovideoplayer.R;
import com.media.audiovideoplayer.adapter.AudioPlayerAdapter;
import com.media.audiovideoplayer.datamodel.AudioData;

import java.util.ArrayList;
import java.util.Locale;

import fastscroll.app.fastscrollalphabetindex.AlphabetIndexFastScrollRecyclerView;

public class AudioFragment extends Fragment {

    private AudioData audioModel;
    private ArrayList<AudioData> audioModels;
    private AlphabetIndexFastScrollRecyclerView musicRecyclerView;
    private AudioPlayerAdapter audioPlayerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        if (view != null) {
            musicRecyclerView = view.findViewById(R.id.music_list);
            musicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            ArrayList<AudioData> songsList = loadSongsFromInternalStorage();
            if (null != songsList) {
                if (!songsList.isEmpty())
                    audioPlayerAdapter = new AudioPlayerAdapter(songsList, getActivity(), getContext());
                else
                    Toast.makeText(getContext(), "No Music Files to be loaded", Toast.LENGTH_LONG).show();
                musicRecyclerView.setAdapter(audioPlayerAdapter);
                musicRecyclerView.setIndexBarVisibility(true);
            }
        }
        return view;
    }

    /**
     * Method used for loading songs from internal storage
     *
     * @return
     */

    private ArrayList<AudioData> loadSongsFromInternalStorage() {
        audioModels = new ArrayList<>();
        ContentResolver contentResolver = getContext().getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Albums.ALBUM_ID};

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        );
        int index = 0;
        while (cursor.moveToNext()) {
            audioModel = new AudioData(index,
                    cursor.getString(2),
                    cursor.getString(1),
                    cursor.getString(3),
                    cursor.getLong(5), cursor.getString(6));
            audioModels.add(audioModel);
            index++;
        }
        cursor.close();
        return audioModels;
    }
}