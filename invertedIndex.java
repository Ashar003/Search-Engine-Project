import java.io.*;
import java.util.*;


public class invertedIndex {
    public Map<Integer,File> sources; // doc number, doc
    private HashMap<String, HashSet<Integer>> index; // word to document#
    private File[] fileNames; 
    private stopWord list; 
    private Porter PorterAlgs;
    private TreeMap<String, HashSet<String>> basePorter;   //where stripped words of index are
    private StringBuilder str; 
    private Set<HashSet<Integer>> returnedDocuments;  //query returned documents
    // private HashMap<File,Integer> nowReturned;
    private query Querying;
    public HashSet<Integer> answer; //goes to displayDocuments

    float recall;
    float precision;
    

    invertedIndex(String dirFile, stopWord words, query querying){
        System.out.println("InvertedIndex constructor called");
        // System.out.println("files exist here " + dirFile);
        // if(!(stopWordStatus)){ System.out.println("Please upload a stop word list");} // done in searchGui
        Querying = querying;

        sources = new HashMap<Integer,File>();
        index = new HashMap<String, HashSet<Integer>>();

        File file = new File(dirFile);
        fileNames = file.listFiles();

        list = words;

        constructIndex(fileNames);
        stopWordStrip();
        PorterAlgorithm();     
    }//constructor



    public void constructIndex(File [] files){
        int i =0; // bound the document by a number 
        for(File fileName:files){
            // System.out.println("about to enter "+fileName.getName());
            try(BufferedReader file = new BufferedReader(new FileReader(fileName))){
                // System.out.println("I successfully entered try " + fileName);
                sources.put(i,fileName); // i is the number
                // System.out.println("passed put" + " i is " + i + "filename "+fileName);
                String ln;
                while( (ln = file.readLine()) !=null) {
                    // System.out.println("entered while loop");
                    String[] words = ln.split("\\W+"); //delimiter
                    for(String word:words){
                        // System.out.println("entered for loop");
                        word = word.toLowerCase(); // lowercase
                        if (!index.containsKey(word)) // if the index does not contain the word then create a new hashset
                        // System.out.println("entered if loop");
                            index.put(word, new HashSet<Integer>());  //put word to a hashset, which contains the documents number.
                            // System.out.println("passed put thing");
                        index.get(word).add(i); // the else
                        // System.out.println("passed get ");
                    }
                }
                i++; // document number for sources
            } catch (IOException e){
                System.out.println("File "+fileName+" issue. Skipping it");
            }
           
            // System.out.println("value of iterator i " + i);
        }
        System.out.println("done constructing inverted index");
    }//constructIndex

    public void stopWordStrip(){
        Set<String> stripWords = list.sWords.keySet(); 
        // Set<Map.Entry<String, HashSet<Integer>>> everything = index.entrySet(); 

        for(String stripW : stripWords){
            // System.out.println("current stripW "+ stripW);
            if(index.containsKey(stripW)){
                try{
                // System.out.println(index.get(stripW) + " eviscerated");
                index.get(stripW).clear();
            }catch(Exception e){
                System.out.println("Problem removing value");
            }
    }
        }
        System.out.println("done stripping inverted index");    
}//stopWordStrip

public void PorterAlgorithm(){
    basePorter = new TreeMap<String, HashSet<String>>(); 
    PorterAlgs = new Porter();
    //  Set<Map.Entry<String, HashSet<Integer>>> everything = index.entrySet(); 
     for (String anyWord : index.keySet()) {
        try {
            String str = PorterAlgs.stripAffixes(anyWord);
            // System.out.println(str);
                if(!basePorter.containsKey(str)){            //is it part of the TreeMap already?
                    basePorter.put(str, new HashSet<String>()); // if it's not
                }
                else{
                    basePorter.get(str).add(anyWord); //if it is 
                }
        }//try
        catch (Exception ex) {
            // ex.printStackTrace();
        }//catch
    
}//for
System.out.println("Porter's Algorithm has done its magic");
}//porterAlgorithm

public String crossExamine(){
    str = new StringBuilder();
    // System.out.println("i am in crossexamine");
    for(String similarWords : Querying.queryPorter.keySet()){
        // System.out.println("i am in for each");
       if(basePorter.containsKey(similarWords)){  
        // System.out.println(basePorter.get(similarWords));
           str.append(similarWords + " "); 
           //this holds the query 
            
       }

    }
    Querying.queryPorter.clear(); //to prevent queries from intersecting
    return str.toString();
}



public HashSet<Integer> findDocuments(){
     returnedDocuments = new HashSet<HashSet<Integer>>(); 
        
    //  int count = 0;

    String holyString = crossExamine();
    // System.out.println("holystring" + holyString.length());
    String[] arr = holyString.split(" ");    


    for ( String ss : arr) {
        // System.out.println(ss);
for(String holy : index.keySet()){
    
       
    try{
    if(PorterAlgs.stripAffixes(holy).equals(ss)){

        returnedDocuments.add(index.get(holy));
        answer = index.get(holy);
        


    }
}catch(Exception e){
    // System.out.println(e);
}
}
}
// nowReturned = new HashMap<File,Integer>(); 
// Map.Entry<HashSet<Integer>, Integer> set = returnedDocuments.entrySet();
// TreeSet<HashSet<Integer>> sortedSet = new TreeSet<HashSet<Integer>>(returnedDocuments);
//         Iterator<HashSet<Integer>> value = sortedSet.iterator();
// for(HashSet<Integer> documents : sortedSet.) {
//     while(value.hasNext()){
//          if(sources.containsKey(value.next())){
//              nowReturned.put(sources.get(value.next()), count++);

//     }
// }

PrecisionRecall();


return answer;
}

public void PrecisionRecall(){
    // System.out.println(answer.size() + " documents were captured");

    // /Precision  =  Total number of documents retrieved that are relevant/Total number of documents that are retrieved.
    //Recall  =  Total number of documents retrieved that are relevant/Total number of relevant documents in the database.

    // tp = 20; // true positives ; should be 20 because each query was stripped of 20 documents from Google?
	// fp = answer.size() - tp; // false positives
    // fn = answer.size() - tp; // false
        float tp = answer.size();
    precision = 20 / (tp + 20);  //tp /(tp + fp)
    
    recall = 20 / (answer.size());                  //tp / (tp + fn);
    // System.out.println(precision + " " + recall);
    Querying.saveQueries("The precision is" + String.valueOf(Math.abs(precision))+ " The recall is "+ String.valueOf(Math.abs(recall)));

    
}



}//invertedIndex