import java.io.*;
import java.util.*;

public class stopWord{
    private Map<Integer,File> sources;
    public HashMap<String, HashSet<Integer>> sWords;
    
    stopWord(String fileorFolder){
        System.out.println("You have chosen to give a stopwords document.");
        sources = new HashMap<Integer,File>();
        sWords = new HashMap<String, HashSet<Integer>>();
        File file = new File(fileorFolder);
        File[] fileNames = file.listFiles();
        constructstopWords(fileNames);
        System.out.println("Your stop words list have been constructed.");
    }

    //this is where the stopWords file/folder goes,and it's transferred into a HashMap
    //the reason why I have two hashMaps is just incase more than one stopWords list is provided
    public void constructstopWords(File [] fileorFold){
        int i =0;
        for(File fileName:fileorFold){
            // System.out.println("about to enter "+fileName.getName());
            try(BufferedReader file = new BufferedReader(new FileReader(fileName)))
            {
                // System.out.println("I successfully entered try " + fileName);
                sources.put(i,fileName);

                // System.out.println("passed put" + " i is " + i + "filename "+fileName);
                String ln; // holds the line read
                while( (ln = file.readLine()) !=null) { //while there are more lines
                    // System.out.println("entered while loop");
                    String[] words = ln.split("\\W+");  //delimiter of sorts to seperate into words
                        for(String word:words){ // for each loop to look at each word
                            // System.out.println("entered for loop");
                            word = word.toLowerCase();  //lowercase
                            if (!sWords.containsKey(word))  // if it doesn't contain the word
                            // System.out.println("entered if loop");
                                sWords.put(word, new HashSet<Integer>()); // set a hashset there.
                                // System.out.println("passed put thing");
                            sWords.get(word).add(i);
                            // System.out.println("passed get ");
                        }
                }
            } catch (IOException e){
                System.out.println("File "+fileName+" not found. Skip it");
            }
            i++;
            System.out.println("value of iterator i " + i);

        }

    }

    

    // stopWord(){
    //     System.out.println("You have chosen not to provide your own stopwords document. It will be made automatically ");
    // }
}//stopWord