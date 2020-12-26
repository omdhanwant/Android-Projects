package com.omdhanwant.permissionsbackgroundtasksmedia.MediaPlayer;

public class Song {
    public  String Path;
    public  String song_name;
    public  String album_name;
    public  String artist_name;
    public  Song(String Path,String song_name,String album_name,
                     String artist_name){
        this.Path=Path;
        this.song_name=song_name;
        this.album_name=album_name;
        this.artist_name=artist_name;
    }
}
