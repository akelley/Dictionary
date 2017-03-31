import java.util.*; 

public class DictionaryApplication{
	public static void ToolsMenu(){
		System.out.println("====== Dictionary App Main Menu =======");     
		System.out.println("1. Select");
		System.out.println("2. Sort");
		System.out.println("3. Search");
		System.out.println("4. Spell-checker");
		System.out.println("5. Anagram-finder");
		System.out.println("6. Scrabble-helper");
		System.out.println("7. Create and test anagram dictionary");
		System.out.println("8. Current dictionary status");
		System.out.println("0. Exit");
		System.out.println("======================================");
		System.out.print("Command: ");
    }     
  
    public static void main(String args[]){
    	EasyIn easy = new EasyIn();
    	Dictionary dict = new Dictionary("C:\\Users\\atkelley\\workspace\\Project 1\\src\\english.txt"); 		
    	String[] wlist = new String[dict.getSize()];
		int[] map = new int[dict.getSize()];
    	long startTime;
		long endTime;
	    int command = -1;

		while(command != 0){		    
		    ToolsMenu();
		    command = easy.readInt();
	
		    switch(command){
		    	case 1: // Select option
		    		System.out.println("\nSelect dictionary:");
		    		System.out.println("1. English");
		    		System.out.println("2. French");
		    		System.out.println("3. Spanish");
		    		System.out.println("4. Short");
		    		System.out.print("\nSelection: ");
		    		int choice = easy.readInt();
		    		
		    		if(choice == 1){
		    			dict = new Dictionary("C:\\Users\\atkelley\\workspace\\Project 1\\src\\english.txt");
		    			System.out.println("\nSelected dictionary: " + dict.getName() + "\n");
		    			break;
		    		}
		    		
		    		else if(choice == 2){
		    			dict = new Dictionary("C:\\Users\\atkelley\\workspace\\Project 1\\src\\french.txt");
		    			System.out.println("\nSelected dictionary: " + dict.getName() + "\n");
		    			break;
		    		}
		    		
		    		else if(choice == 3){
		    			dict = new Dictionary("C:\\Users\\atkelley\\workspace\\Project 1\\src\\spanish.txt");
		    			System.out.println("\nSelected dictionary: " + dict.getName() + "\n");
		    			break;
		    		}
		    			
		    		else if(choice == 4){
		    			dict = new Dictionary("C:\\Users\\atkelley\\workspace\\Project 1\\src\\short.txt");
		    			System.out.println("Selected dictionary: " + dict.getName() + "\n");
		    			break;
		    		}
		    			
		    		else{
		    			System.out.println("Invalid selection.");
		    			break;
		    		}		
		    	
		    	case 2: // Sort option
				    System.out.print("Select sorting algorithm (1. bubbleSort, 2. selectionSort, 3. insertionSort): ");
				    int sort = easy.readInt(); 
				    
				    if(sort == 1){
				    	startTime = System.nanoTime();   
						dict.bubbleSort();
						endTime = System.nanoTime(); 
				    }

				    else if (sort == 2){
				    	startTime = System.nanoTime();   
						dict.selectionSort();
						endTime = System.nanoTime(); 
				    }
				    
				    else if (sort == 3){
				    	startTime = System.nanoTime();
				    	dict.insertionSort();
				    	endTime = System.nanoTime();
				    }
				    
				    else{
				    	System.out.println("Invalid selection!\n");
				    	break;
				    }
				    
				    dict.saveDictionary(dict.getName(), "sorted");  
				    System.out.println("Dictionary sorted in " + ((endTime - startTime) / 1000) + " ms and saved as \'" + dict.getName() + "\'\n");
				    
				    break;

				case  3: // Search option	     
				    System.out.print("How many words to search (1 <= x <= " + dict.getSize() + ")?: ");
				    int howMany = easy.readInt();
				    System.out.println();
				    
					for(int i = 0; i < howMany; i++){
						Random rand = new Random();
						int index = rand.nextInt(dict.getSize());
						
						if(dict.binarySearch(dict.getWord(index))){
							System.out.println("<<" + dict.getWord(index) + ">>" + " was found in " + dict.getStep() + " steps at index " + dict.getIndex() + ".");
						}
						else
							System.out.println("Error occurred!");
					}
					System.out.println();
				    break;
		    
				case 4: // Spell-checker option
				    System.out.print("Spell check a text file(1. English(sample), 2. French(sample), 3. Spanish(sample), 4. letter.txt): ");
				    int choice2 = easy.readInt();
				    
				    if(choice2 == 1)
				    	dict.spellCheckFile("english-sample.txt");
				    
				    else if(choice2 == 2)
				    	dict.spellCheckFile("french-sample.txt");
				    
				    else if(choice2 == 3)
				    	dict.spellCheckFile("spanish-sample.txt");
				    
				    else if(choice2 == 4)
				    	dict.spellCheckFile("letter.txt");
				    
				    else
				    	System.out.println("Invalid selection.");
				    
				    break;         
		     
				case 5: // Anagram-finder option
					if(dict.getStatus() == "Sorted"){
					    System.out.print("Enter a word: ");
					    String example = easy.readString();
					    dict.anagram(example);		  
					    break;
					}
					
					else if(dict.getStatus() == "Anagram"){
						System.out.print("Enter a word: ");
						String example = easy.readString();
						
						if(dict.anagramModified(example)){
						for(int j = 0; j < dict.getDuplicate(); j++)
							System.out.println(wlist[map[dict.getIndex()+j] - 2]);
						}
						if(dict.getDuplicate() > 0)
							System.out.print("Congrats! ");
						else
							System.out.print("Sorry! ");
						System.out.println("You found " + dict.getDuplicate() + " anagram(s).\n");
						
						break;
					}
				
					else{
			    		System.out.println("Please either sort or create an anagram dictionary before utilizing this option.\n");
			    		break;
					}
					
				case 6: // Scrabble-helper option
					int score = 0;
					
				    System.out.print("Enter series of letters: ");
				    String scrabword = easy.readString();
				    System.out.println();
				    int nbperm = dict.countWordComb(scrabword.toLowerCase());
				    System.out.print(nbperm + " letter combinations found. ");
				    
				    String[] swlist = dict.generateWordComb(scrabword.toLowerCase());
				    int[] winner = new int[swlist.length];

				    System.out.println("Words found in the sorted-anagram dictionary: ");

				    for(int i = 0; i < swlist.length; i++){
				    	if(dict.anagramModified(swlist[i]) && swlist[i].length() > 1)
				    		for(int j = 0; j < dict.getDuplicate(); j++){
				    			System.out.println("-- " + wlist[map[dict.getIndex()+j] - 2] + " (" + dict.scoreScrabble(swlist[i]) + ")");
							}
				    }

				    System.out.println();				    
				    break;
			    		
				case 7: // Create anagram dictionary option (NOTE: sorted dictionary must already be saved!)
				    if(dict.getStatus() != "Sorted"){
				    	System.out.println("Dictionary must first be sorted before selecting this option.\n");
				    	break;
				    }
					
					for(int i = 0; i < dict.getSize(); i++)   
				    	wlist[i] = dict.getWord(i);						// save sorted dictionary
				                                                                  
				    map = dict.createSortedAnagramDictionary();			// create sorted anagram dictionary and return mapping		            
				    dict.saveDictionary(dict.getName(), "anagram");
				    System.out.println("\nAnagram dictionary created and saved as file \'" + dict.getName() + "\'");	   
				    System.out.print("Testing: how many words should we randomly choose to map (1 <= x <= " + dict.getSize() + ")?: ");
				    int testWords = easy.readInt();
				    System.out.println();
				    
				    for(int i = 0; i < testWords; i++){
						Random rand = new Random();
						int indexAnagram = rand.nextInt(dict.getSize());		// grab random index number for Anagram dictionary
						String anagramExample = dict.getWord(indexAnagram);		// save it as a string
						System.out.println(dict.getWord(indexAnagram) + " ==> " +  wlist[map[indexAnagram] - 2]);
				    }
				    System.out.println();
				    break;
			           
				
				    
				case 8: // Current dictionary status option
				    System.out.println("Current dictionary: '" + dict.getName() + "'");
				    System.out.print("Number of entries: " + dict.getSize());
				    System.out.println("  Status: " + dict.getStatus() + "\n"); 
					break;
				
				case 0:{
					System.out.println("\nThank you for using the Dictionary Application. Goodbye!");
				    break;
				}
				
				default:
				    System.out.println("Invalid selection. Please try again.");
			}
		}  	
    }
}