import java.util.*;

public class Main {
   public static void main(String[] args) {

      HashMap <String, List<String>> homeTowns = new HashMap<>();
      // key as your name
      // value as your hometown name
      homeTowns.put("Nafi" , Arrays.asList("De Pere"));
      homeTowns.put("Calvin" , Arrays.asList("Navarino"));

      List<String> homeTownNames =  Arrays.asList("Seymour", "Green Bay", "Shawano", "De Pere", "West De Pere", "Navarino");
      
      homeTowns.put("Locations", homeTownNames);
      
      System.out.println("size of hash table : " + homeTowns.size());

      //Create a new HashMap that will keep track of the number of people from a city.
      HashMap<String, Integer> cityCount = new HashMap<>();
      for (String key: homeTownNames) {
         if (cityCount.containsKey(key)) {
            cityCount.put(key, cityCount.get(key) + 1);
         } else {
            cityCount.put(key, 1);
         }
      }

      for (String key : homeTowns.keySet()) {
         System.out.println("Key: " + key + " Value: " + homeTowns.get(key) );
      }
      for (String city : cityCount.keySet()) {
         System.out.println("City: " + city + " Count: " + cityCount.get(city));
      }

      System.out.println("homeTowns.entrySet(): " + homeTowns.entrySet());

         //Print the mapHasCode
      for (Map.Entry<String, List<String>> entry : homeTowns.entrySet()) {
         System.out.println("Entry: " + entry);
         System.out.println(entry.getKey() + ": " + entry.getValue());
      }

      //hashcode
      System.out.println("homeTowns.hashCode(): " + homeTowns.hashCode());
   }

}