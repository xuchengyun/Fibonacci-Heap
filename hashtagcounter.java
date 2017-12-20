import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
/**
 * Created by Chengyun on 11/8/16.
 */
public class hashtagcounter {
     public static void main(String[] args) {
   	     execute (args[0]);
     }
     
     /**
      *  read file content from file name 
      *  compute the hashtagcounter by fibonacciheap
      *  write output to output_file.txt
      *  @param fileName
      */
	 public static void execute(String fileName) {
    	 Hashtable<String, Node> hTable= new Hashtable<String, Node>();  //create a hashtable
    	 FibonacciHeap hHeap=new FibonacciHeap();                                         //create a FibHeap
         try {       	
        	 FileReader reader = new FileReader(fileName);                                //read from file
             BufferedReader bufferReader = new BufferedReader(reader);			      	  //read buffer
             FileWriter fileWriter = new FileWriter("output_file.txt");         	      //write to a file
             BufferedWriter bufferWriter = new BufferedWriter(fileWriter);			      //write buffer
             StringBuffer stringBuffer   = new StringBuffer();
             String str = null;
             // read file content from file
             while((str = bufferReader.readLine()) != null) {
        	   //split string
                  String[] temp = str.split(" ");             
                  String hashtag = temp[0];
               
                  if(hashtag.toLowerCase().equals("stop")) {
            	      break;
                  }
                  char x = hashtag.charAt(0);
                  //decide whether the first postion is #
                  if(x == '#') {
            	      int value = Integer.parseInt(temp[1]);
            	      //if key isn't in hashtable, do insert operation         	   
            	      if(!hTable.containsKey(hashtag)) {
                          Node node = new Node(value, hashtag);
            		      hTable.put(hashtag, node);         		  
            		      hHeap.insertNewNode(node);
            	      }
            	      //if key is in the hashtable,do update operation
            	      else { 
            		      //get node in FibHeap
            		      Node p = hTable.get(hashtag);
            		      //update value
            		      int newValue = p.key + value; 
            		      hHeap.increaseKey(p, newValue);
            		      //update hashtable
            		      hTable.remove(hashtag);
            		      hTable.put(hashtag, p);
            	      }
                  }

                  //if first position is a digit  
                  else {
            	      int numOfOut = Integer.parseInt(hashtag);             //get an integer
            	      int[] maxIndex = new int[numOfOut];                   //store max value
            	      String outHashtag = null;					 
            	      String[] hashtagArray = new String[numOfOut];		    //store max key
            	      //print most popular hashtags
            	      for(int i = 0; i < numOfOut; i++) {
            		      //get the node
            		      Node p = hHeap.getMaxNode();
            		      //get the hash tag
            		      outHashtag = p.hashtag;
                	      int max = hHeap.getMaxKey();
                          maxIndex[i] = max;
                          hashtagArray[i] = outHashtag;	  
                          //print hashtag
                          if(i < numOfOut - 1) {
                    	      bufferWriter.append(outHashtag.substring(1) + ",");
                          }
                          else { 
                		      bufferWriter.append(outHashtag.substring(1));
                          }
                          hHeap.removeMax();
                          hTable.remove(outHashtag);
            		   }
            	  
        		      bufferWriter.newLine();
        		      //insert the node back
            	      for(int i = 0; i < numOfOut; i++) {
            		      Node n = new Node(maxIndex[i], hashtagArray[i]);
            		      hTable.put(hashtagArray[i], n);
            		      hHeap.insertNewNode(n);     		   
            	      }
                 }
            }
    
         bufferReader.close();
         reader.close();         
         // write string to file       
         bufferWriter.write(stringBuffer.toString());       
         bufferWriter.close();
         fileWriter.close();
         }
         catch(FileNotFoundException e) {
             e.printStackTrace();
         }
         catch(IOException e) {
               e.printStackTrace();
         }
	 }
}

