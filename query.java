import java.io.*;
import java.util.*;

//query class that handles the queries

public class query{
    private Map<String,Integer> typed;
    int count;
    File queryLoc;
    public TreeMap<String, HashSet<String>> queryPorter; 
    private Porter PorterAlgs; 
    BufferedWriter writer;

    query(){
        System.out.println("I need a location for the file");
        typed = new HashMap<String, Integer>();
        count = 0;
    }

    //With arguement passed in that is the location of output file
    query(File queryLocation){ 
        System.out.println("Query constructor called");
        queryLoc = queryLocation;
        typed = new HashMap<String, Integer>();
        count = 0;
    }

    public void somethingWasTyped(String typedQueries){
        // System.out.println(typedQueries);

        try{
            if (!typed.containsKey(typedQueries)){
                // throw new Error("This query has already been typed in");
        typed.put(typedQueries, count); // count as in what query number is this.
                count++;
            }
            } catch (Exception e){
                System.out.println(e);
            }
        
           

        }//somethingWasTyped


    
//writes to a file by virtue of string(query) passed in
    public void saveQueries(String toBSaved){
      
        try{
            // Set<String>  st = typed.keySet(); 
            writer = new BufferedWriter( new FileWriter(queryLoc,true)); 
        // for(String queries : st){
        //     System.out.println(queries);
        try{
            
            writer.write(toBSaved);
            writer.newLine();
            // writer.flush();
            
		}catch(IOException error){
            
			System.out.println(error);
        }
        writer.close();
    }//saveQueries



catch(IOException fwError){
    System.out.println(fwError);
}

}

//run the PA here to strip the queries
public void PorterAlgorithm(){
    queryPorter = new TreeMap<String, HashSet<String>>(); 
    PorterAlgs = new Porter();
    //  Set<Map.Entry<String, HashSet<Integer>>> everything = index.entrySet(); 
     for (String anyWord : typed.keySet()) { 
        try {
            
            String str = PorterAlgs.stripAffixes(anyWord); //calling PA here
            // System.out.println(str);
            if(!queryPorter.containsKey(str)){
                queryPorter.put(str, new HashSet<String>());
                
            }
            else{
                queryPorter.get(str).add(anyWord);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    
}
typed.clear(); //clearing of the map 
}//PorterAlgorithm
}//query