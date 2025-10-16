import java.util.*;
public class Main {

    private class Song {
    String title;
    String artist;
    String genre;
    
    public static Song(String title, String artist, String genre) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
    }
    
    @Override
    public String toString() {
        return "\"" + title + "\" by " + artist;
    }
    }
    
    /// implement task 1 here
    public static List<String> getTop5Songs(Song[] listeningHistory)
    {
        HashMap<String, Integer> songCount = new HashMap<>();
        for(Song song : listeningHistory){
            if(songCount.containsKey(song.title)){
                int count = songCount.get(song.title);
                count+=1;
                songCount.put(song.title, count);
            }
            else{
                songCount.put(song.title, 1);
            }
        }
        List<String> top5Songs = new ArrayList<>();
        for(int i=0; i<5; i++){
            String maxSong = "";
            int maxCount = 0;
            for(Map.Entry<String, Integer> entry : songCount.entrySet()){
                if(entry.getValue() > maxCount){
                    maxCount = entry.getValue();
                    maxSong = entry.getKey();
                }
            }
            top5Songs.add(maxSong);
            songCount.remove(maxSong);   
        }
        return top5Songs;
    }

    /// implement task 2 here
    public static String checkObsession(Song[] listeningHistory, String artistName)
    {

        int totalLength = listeningHistory.length;
        HashMap<String, Integer> artistCount = new HashMap<>();
        for(Song song : listeningHistory){
            if(artistCount.containsKey(song.artist)){
                int count = artistCount.get(song.artist);
                count+=1;
                artistCount.put(song.artist, count);
            }
            else{
                artistCount.put(song.artist, 1);
            }
        }

        int percentage = (artistCount.get(artistName) / totalLength);
        String isObsessed = "";

        if(percentage > 0.30){
            isObsessed = "Artist is obsessed";
            return isObsessed;
        }
        else if(percentage <= 0.30 && percentage > 0){
            isObsessed = "Artist is not obsessed";
            return isObsessed;
        }
        else{
            isObsessed = "Artist not in history at all (0 plays)";
            return isObsessed;
        }
        

    }

    


    

    public static void main(String[] args) 
    {

        Song[] listeningHistory = {
            new Song("Cruel Summer", "Taylor Swift", "Pop"),
            new Song("Flowers", "Miley Cyrus", "Pop"),
            new Song("Die For You", "The Weeknd", "R&B"),
            new Song("Flowers", "Miley Cyrus", "Pop"),
            new Song("Creepin'", "Metro Boomin", "Hip-Hop"),
            new Song("As It Was", "Harry Styles", "Pop"),
            new Song("Cruel Summer", "Taylor Swift", "Pop"),
            new Song("Die For You", "The Weeknd", "R&B"),
            new Song("Paint The Town Red", "Doja Cat", "Hip-Hop"),
            new Song("Snooze", "SZA", "R&B"),
            new Song("Flowers", "Miley Cyrus", "Pop"),
            new Song("Cruel Summer", "Taylor Swift", "Pop"),
            new Song("Vampire", "Olivia Rodrigo", "Pop"),
            new Song("Die For You", "The Weeknd", "R&B"),
            new Song("Greedy", "Tate McRae", "Pop"),
            new Song("Paint The Town Red", "Doja Cat", "Hip-Hop"),
            new Song("Snooze", "SZA", "R&B"),
            new Song("Lavender Haze", "Taylor Swift", "Pop"),
            new Song("Cupid", "FIFTY FIFTY", "K-Pop")
        };

        /// implement the two functions
        List<String> top5Songs = getTop5Songs(listeningHistory);
        System.out.println("Top 5 Songs:");
        for(String song : top5Songs){
            System.out.println(song);
        }

        String obsessionStatus = checkObsession(listeningHistory, "Taylor Swift");
        System.out.println("Obsession Status:");
        System.out.println(obsessionStatus);
    }

}