import java.io.*;
import java.util.*;

public class Dictionary{
    // private variables related to the dictionary
    private int N;  //number of items
    private String name; // name of dictionary
    private String status; // status of dictionary (unsorted, sorted, sorted-anagram)
    private String[] words; //contains all the words of the dictionary
    
    // private variables related to the "binary" search for the dictionary
    private int searchIndex = 0;
    private int searchStepCounter = 1;
    private int searchDuplicate = 0;
    private int forwardDupes = 0;
    private int reverseDupes = 0;
    
    // private variables related to word permutations
    private int nbComb = 0;
    private String[] wordComb;
    private StringBuilder output = new StringBuilder();

    
    public Dictionary(String name){
    	this.name = name;
    	status = "Unsorted";
    	loadDictionary(name); 
    }   

    public void loadDictionary(String name){ 				// Load the dictionary from a file
		try {
		    File file = new File(name);
		    Scanner scanner = new Scanner(file);
		    this.name = name;
		    
		    N = scanner.nextInt(); 							// load size of the dictionary from first line in file   
		    words = new String[N];							// define String array "words", with size, N
		    scanner.nextLine(); // go to next line			// move to the next line in the file
		    int i = 0;
		    
		    while(scanner.hasNext() && i < N){				// scanning entries into the words arrray
		    	words[i] = scanner.nextLine();
		    	i++; 
		    }
		    scanner.close();
		} 
		
		catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
    }

      
    public void saveDictionary(String name, String state){ 				
    	this.name = new String(name.substring(0, name.length() - 4)); 	
    	this.name = this.name + "-" + state + ".txt";
    	
    	if(state == "sorted")
    		status = "Sorted";
    	
    	else if(state == "anagram")
    		status = "Anagram";
    	
    	try{
    		PrintWriter out = new PrintWriter(this.name);
    		out.println(N);
    		
    		for(int i = 0; i < N; i++){
    			out.println(words[i]);
    		}
    		out.close();
    	}
    	
    	catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    public void bubbleSort(){
    	int in, out;
		for(out = N - 1; out > 1; out--){
			for(in = 0; in < out; in++){
				if(words[in].compareTo(words[in + 1]) > 0) // if [in] is after [in + 1]
					swap(in, in + 1);
			}
		}
    }
    
    public void selectionSort(){
		int in, out, min;
		
		for(out = 0; out < N - 1; out++){
			min = out;
			for(in = out + 1; in < N; in++){
				if(words[min].compareTo(words[in]) > 0)
					min = in;
			}
			swap(out, min);
		}	
    }
    
    public void insertionSort(){
		int in, out;
		
		for(out = 1; out < N; out++){
			String temp = words[out];
			in = out;
			while(in > 0 && (words[in - 1].compareTo(temp)  > 0)){
				words[in] = words[in - 1];
				--in;
			}
			words[in] = temp;
		}
    }

    public void swap(int one, int two){
    	String temp = words[one];
		words[one] = words[two];
		words[two] = temp;
    }
    
    public boolean binarySearch(String searchKey){ 
    	int lowerBound = 0;										// lower bound starts at zero index
		int upperBound = N - 1;									// upper bound starts at highest index
		int curIn;												// current index (the cursor)
		searchStepCounter = 0;
		searchIndex = 0;
		searchKey = searchKey.replaceAll("[^a-zA-Z ]", "").toLowerCase(); 
		
		while(true){											// the main search loop
			curIn = (lowerBound + upperBound)/2;				// always begin at the midpoint of array
			
			if(words[curIn].equals(searchKey)){					// if searchKey is the midpoint value
				searchIndex = curIn;
				searchStepCounter++;
				return true;
			}
			
			else if (lowerBound > upperBound)					// You've searched the whole array and value not found
				return false;									// return the array size (out of bounds of the array)
			
			else{
				searchStepCounter++;
				searchIndex = curIn;
				
				if(words[curIn].compareTo(searchKey) < 0)		// if words entry < searchKey, not in lower half of array
					lowerBound = curIn + 1;						// reset lowerBound to next index after curIn
				else											// otherwise, value not in upper half
					upperBound = curIn - 1;						// reset upperBound to next index below curIn
			}	
		}
    } 
   
    public int[] createSortedAnagramDictionary(){ 				// return int array mapping between sorted and anagram dictionaries
    	int[] map = new int[N];
    	
    	for(int i = 0; i < N; i++){
    		words[i] = sortWord(words[i]);
    		map[i] = i + 2;
    	}
    	
		int in, out, min;
		
		for(out = 0; out < N - 1; out++){
			min = out;
			for(in = out + 1; in < N; in++){
				if(words[min].compareTo(words[in]) > 0){
					min = in;
				}	
			}
			String temp = words[out];
			int marker = map[out];
			words[out] = words[min];
			map[out] = map[min];
			words[min] = temp;
			map[min] = marker;
		}
		
		for(int i = 0; i < N; i++)
    		words[i] = sortWord(words[i]);

    	return(map);    
    }
   
    private String sortWord(String word){  						// sort the letters of the word using selectionSort 
    	char[] c = new char[word.length()];
    	c = word.toCharArray();
    	
    	int in, out, min;
		for(out = 0; out < word.length() - 1; out++){
			min = out;
			for(in = out + 1; in < word.length(); in++){
				if(c[min] > c[in])
					min = in;
			}
			char temp = c[out];
			c[out] = c[min];
			c[min] = temp;
		}
		
		String finished = new String(c);
    	return(finished); 
    }
  
    public void spellCheckFile(String fileName){ 				// load text file and identify all words 
		try {
			String prefix = new String("C:\\Users\\atkelley\\workspace\\Project 1\\src\\");
			prefix = prefix + fileName;
		    File file = new File(prefix);
		    Scanner scanner = new Scanner(file);
		    System.out.println();
		    
		    while(scanner.hasNextLine()){		
		    	String[] tokens = scanner.nextLine().split(" ");
		    	
		    	for(int i = 0; i < tokens.length; i++){
			    	if(!binarySearch(tokens[i]))
			    		tokens[i] = " <<" + tokens[i] + ">> ";
			    	else
			    		tokens[i] = " " + tokens[i] + " ";
			    }
	
		    	for(int i = 0; i < tokens.length; i++)
		    		System.out.print(tokens[i]);
		    	System.out.println();
		    }
		    scanner.close();
		    System.out.println();
		}
		
		catch (FileNotFoundException e){
		    e.printStackTrace();
		}
    }
  
    public void anagram(String word){ 									
		word = word.replaceAll("[^a-zA-Z ]", "").toLowerCase(); 
    	String nword = sortWord(word); 
    	int result = 0;
	
	    for(int i = 0; i < words.length; i++){
	    	if(words[i].length() == nword.length()){
	    		String temp = sortWord(words[i]);
		    	if(temp.compareTo(nword) == 0){
		    		System.out.println(words[i]);
		    		result++;
		    	}	
	    	}
	    }
	    	
	    if(result > 0)
	    	System.out.println("\nGreat! " + result + " anagram(s) found.\n");
	    
	    else
	    	System.out.println("\nSorry! " + result + " anagram(s) found.\n");
    }
    
    public boolean anagramModified(String word){
    	word = word.replaceAll("[^a-zA-Z ]", "").toLowerCase(); 
    	String nword = sortWord(word); 
    	searchDuplicate = 0;
    	forwardDupes = 0;
    	reverseDupes = 0;
    	int i = 0;
    	boolean bool = false;
    	
		if(binarySearch(nword)){ 							
			bool = true;
			searchDuplicate++;
			i = searchIndex;

			while(nword.compareTo(words[i+1]) == 0){
				searchDuplicate++;		
				i++;
			}
			
			i = searchIndex;
			while(nword.compareTo(words[i-1]) == 0){
				searchDuplicate++;
				searchIndex--;
				i--;
			}
		}		
		return(bool);
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////// Methods to count and generate Word permutation (recursive method)
    ///////////////////////////////////////////////////////////////////////////////////
    public int countWordComb(String word){ // return the number of combinations
    	nbComb = 0;
    	generateWordComb(word, 0, false );
    	return(nbComb);
    }
    
    public String[] generateWordComb(String word){ // return all the generated combinations
       	wordComb = new String[nbComb];
    	nbComb = 0;
    	generateWordComb(word, 0, true );
    	return(wordComb);
    }
     
    private void generateWordComb(String word, int start, boolean out){
        for(int i = start; i < word.length(); ++i ){
        	output.append(word.charAt(i) );
        	if(out) 
        		wordComb[nbComb] = output.toString();
			//System.out.println(output);
			nbComb++;
			
			if (i < word.length())
			    generateWordComb(word, i + 1, out);
			
			output.setLength( output.length() - 1 );
	    }
    }

    public int scoreScrabble(String word){ 
    	int count = 0;	
		char c;
		
		String nword = word.toLowerCase(); //transform to lowercases
		for(int i = 0; i < word.length(); i++){
		    c = nword.charAt(i);
		    
		    if ((c == 'e')||(c == 'a')||(c == 'i')||(c == 'n')||(c == 'r')||(c == 't')||(c == 'l')||(c == 's')||(c == 'u'))
		    	count++;
		    
		    if ((c == 'd')||(c == 'g'))
		    	count = count + 2;
		    
		    if ((c == 'b')||(c == 'c')||(c == 'm')||(c == 'p'))
		    	count = count + 3;
		    
		    if ((c=='f')||(c=='h')||(c=='v')||(c=='w')||(c=='y'))
		    	count = count + 4;
		    
		    if ((c=='k'))
		    	count = count + 5;
		    
		    if ((c=='j')||(c=='x'))
		    	count = count + 8;
		    
		    if ((c=='q')||(c=='z'))
		    	count = count + 10;
		}
		return count; // returns word score
    }
  
    public String getName(){ 
    	return(name); 
    }
  
    public int getSize(){ 
    	return(N); 
    }
  
    public String getStatus(){ 
    	return(status); 
    }
  
    public int getStep(){
    	return searchStepCounter;
    }
  
    public int getDuplicate(){
    	return searchDuplicate; 
    }
    
    public int getFwdDupes(){
    	return forwardDupes;
    }
    
    public int getRevDupes(){
    	return reverseDupes;
    }
  
    public int getIndex(){
    	return searchIndex; 
    }

    public String getWord(int i){
    	return words[i];
    }
}
    