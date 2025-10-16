import java.util.*;
public class Main {


    
    public static int reportUniqueFaults(String[] faults) 
    {
        HashMap<String, Integer> map = new HashMap<>();
        for( String error: faults){
            map.put(error, 0);
        }
        return map.size();
    }
    public static void countFrequencyOfUniqueFaults(String[] faults)
    {
        HashMap<String, Integer> map = new HashMap<>();
        for( String error: faults){
            if(map.containsKey(error)){
                int base_value = map.get(error);
                base_value += 1;
                map.put(error, base_value);
            }
            else{
                map.put(error, 1);
            }      
        }
        System.out.println("-- Unique Fault Counts --");
        for(Map.Entry<String, Integer> entry: map.entrySet()){
            System.out.println("Fault: \'" + entry.getKey() +  "\' | Count: " + entry.getValue());
        }
    }
    
    public static void main(String[] args) 
    {
        String[] unixLogFaults = {
            "Segmentation Fault",
            "Kernel Panic",
            "Disk I/O Error",
            "Segmentation Fault",
            "Memory Allocation Failed",
            "Timeout: Resource Busy",
            "Kernel Panic",
            "Disk I/O Error",
            "Permission Denied",
            "Segmentation Fault",
            "Network Connection Reset",
            "Timeout: Resource Busy",
            "Memory Allocation Failed",
            "Kernel Panic",
            "Filesystem Corrupted",
            "Segmentation Fault",
            "Disk I/O Error",
            "Permission Denied",
            "Network Connection Reset",
            "Kernel Panic",
            "Filesystem Corrupted",
            "Timeout: Resource Busy",
            "Memory Allocation Failed",
            "Segmentation Fault",
            "Kernel Panic"
        };

        /// implement the two functions

        System.out.println(reportUniqueFaults(unixLogFaults) );
        countFrequencyOfUniqueFaults(unixLogFaults);

    }
    // ... rest of the code will go here
}