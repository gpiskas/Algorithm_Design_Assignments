/*
*  Algorithm Design Assignments
*  Copyright (C) 2013  George Piskas
*
*  This program is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License along
*  with this program; if not, write to the Free Software Foundation, Inc.,
*  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
*  Contact: geopiskas@gmail.com
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fruits {
	
	// Πετάει IOException εάν συμβεί σφάλμα με τα αρχεία.
	public static void main(String[] args) throws IOException {	
		
///////////////////////////////////////////////////////////////////////////////////////////
//**Αρχικοποίηση: Δημιουργία απαραίτητων μεταβλητών για την εκτέλεση του προγράμματος** //	
///////////////////////////////////////////////////////////////////////////////////////////
		
		// Εσωτερική κλάση που αντιπροσωπεύει ενα φρούτο.
		// Διαθέτει ενα πεδίο για το όνομα του φρούτου (name), ενα για τη περιεκτικότητα του σε βιταμίνες (vitAmount)
		// και ένα για την ποσότητα που ίσως μας χρειαστεί στο τέλος (needAmount). Το needAmount αρχικοποιείται σε 0
		// για όλα τα φρούτα όταν δημιουργούνται, καθώς η ποσότητα που θα χρειαστούμε καθορίζεται προς το τέλος του προβλήματος,
		// μέσω του πίνακα που προκύπτει απο τον αλγόριθμο δυναμικού προγραμματισμού.
		class Fruit {
			
			private String name;
			private int vitAmount;
			private int needAmount;
			
			// Κατασκευαστής - δέχεται όνομα και περιεκτικότητα.
			public Fruit(String fName,int fVits) {
				name = fName;
				vitAmount = fVits;	
				needAmount = 0;	
			}

			// Η toString σε περίπτωση που χρειαστεί η εκτύπωση ενός φρούτου.
			// Παράδειγμα: Name: A|Vits: 25|Needed: 0
			@Override
			public String toString() {
				return "Name: "+this.name+"|Vits: "+this.vitAmount+"|Needed: "+this.needAmount;
			}	
		}
		
		String dir = System.getProperty("user.dir"); // Το directory που βρισκόμαστε κατα την εκτέλεση.
		String lineOfText; // Χρησιμοποιείται στο διάβασμα του fruits.txt.
		
		// Η λίστα με τα φρούτα.
		ArrayList<Fruit> fruits = new ArrayList<Fruit>(); 
		// Η συνολική βιταμίνη που απαιτούμε.
		int targetVits = 0; 
		
///////////////////////////////////////////////////////////////////////////////////////////////
// **Βήμα 1: Διάβασμα του αρχείου fruits.txt και συμπλήρωση των μεταβλητών με τα δεδομένα.** //	
///////////////////////////////////////////////////////////////////////////////////////////////
		
		// Γίνεται προσπάθεια να ανόιξει το fruits.txt
		try {
			BufferedReader input = new BufferedReader(new FileReader(dir + "\\fruits.txt"));
			System.out.println("Input File Found! " + dir + "\\fruits.txt\n");
			// Εφόσον ανοιχτεί επιτυχώς, γίνεται προσπάθεια διαβάσματος από αυτό.
			try {
				// Αγνόηση τυχόν κενών γραμμών έως τα πρώτα δεδομένα.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// Εκχώρηση τιμής στη μεταβλητή totalVits.
				targetVits = Integer.parseInt(lineOfText);
				
				// Αγνόηση τυχόν κενών γραμμών έως τα φρούτα.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// Pattern και matcher που διευκολύνουν την εξόρυξη των δεδομένων.
				// Δύο ειδών, ένα για το όνομα (frName) και ένα για τον αριθμό των βιταμινών (frVits).
				Pattern frName_p = Pattern.compile("[a-zA-Z]+");
				Matcher frName_m = frName_p.matcher(lineOfText);
				
				Pattern frVits_p = Pattern.compile("\\d+");
				Matcher frVits_m = frVits_p.matcher(lineOfText);

				// Διάβασμα των φρούτων έως το τέλος του αρχείου.
				while (lineOfText!=null) {

					// Έυρεση του ονόματος με την πρώτη κανονική έκφραση.
					frName_m = frName_p.matcher(lineOfText);
					frName_m.find();
					String name = frName_m.group();
					
					// Έυρεση της περιεκτικότητας με την δεύτερη κανονική έκφραση.
					frVits_m = frVits_p.matcher(lineOfText);
					frVits_m.find();
					int vitAmount = Integer.parseInt( frVits_m.group() );		
			
					// Εάν βρεθεί λάθος στο input, πετάμε Exception και το πρόγραμμα τερματίζεται.
					if (name==null || vitAmount<=0) {
						throw new Exception();
					}
				
					// Κατασκευή ενός νέου φρούτου με τα παραπάνω χαρακτηρηστικά και αποθήκευση στο fruits.
					Fruit aFruit = new Fruit(name, vitAmount);
					fruits.add(aFruit);
					
					// Διάβασμα της επόμενης μή κενής γραμμής.
					lineOfText = input.readLine();
					while (lineOfText!=null && lineOfText.equals("")) {
						lineOfText = input.readLine();
					}
				}
				
			} catch (IOException e) {
				// Περίπτωση σφάλματος.
				System.out.println("Error reading " + dir + "\\fruits.txt");
			} catch (Exception e) {
				// Περίπτωση σφάλματος στα δεδομένα.
				System.out.println("Input file contains invalid data.");
				System.out.println("| Make sure fruit names are not missing and consist of [a-zA-Z] characters.");
				System.out.println("| Make sure fruit vitamin amounts are not missing and are positive non-zero integers.");
				System.out.println("Program will now exit.");
				System.exit(0);
			} finally {
				// Κλείσιμο του αρχείου στο τέλος.
				input.close();
			}
		} catch (FileNotFoundException e) {
			// Περίπτωση που το fruits.txt λείπει.
			System.out.println("Input File Missing!");
			System.out.println("| Place it in " + dir);
			System.out.println("Program will now exit.");
			System.exit(0);
		}

///////////////////////////////////////////////////////////////////////////////////
// **Βήμα 2: Εκτέλεση του αλγορίθμου εύρεσης των απαραίτητων-ελάχιστων φρούτων** //	
///////////////////////////////////////////////////////////////////////////////////
		
		// Τα όρια του νέου πίνακα θα έχουν μια επιπλέον θέση έτσι ώστε η πρώτη γραμμή (table[0][i] = inf)  και η πρώτη στήλη (table[i][0] = 0)  να είναι οι αρχικές τιμές του πίνακά μας.
		final int FR_CNT = fruits.size()+1; // fruitCount
		final int VI_CNT = targetVits+1; // vitaminCount
		
		// Μια μεγάλη τιμή (INF) η οποία χρησιμοποιείται για να αποκλείονται καταστάσεις.
		final int INF = 2*targetVits;
		// Βοηθητική μεταβλητή που χρησιμοποιείται στον παρακάτω αλγόριθμο. Με βάση αυτή γίνονται οι απαραίτητες συγκρίσεις.
		int tempFruitAmount;
		
		// Ο δισδιάστατος πίνακας που θα προκύψει.
		// Οι στήλες αντιπροσωπεύουν τα φρόυτα, οι γραμμές αντιπροσωπεύουν τις βιταμίνες.
		// Οι βιταμίνες παίρνουν τιμές απο 0 έως targetVits (ο στόχος μας).
		// Τα φρούτα παίρνουν τιμές από 0 έως fruitCount. "Μαρκάρονται" σαν δείκτες και όχι με το όνομά τους.
		// Η σειρά των φρούτων δέν μας ενδιαφέρει, και γι' αυτό δέν τα ταξινομούμε. Ταξινομημένα ή μή, άν υπάρχει σωστή λύση θα βρεθεί καί με τις δύο εκδοχές η οποία μπορεί και να διαφέρει.
		// Οι παραπάνω δείκτες αντιστοιχούν δηλαδή με τη σειρά των φρούτων στο arrayList fruits μείον ένα (γιατί ξεκινάμε από το 0).
		// Παρατηρούμε πως υπαρχει μια πλεονάζουσα στήλη και μια γραμμή (table[i][0] και table[0][i]). Αυτές χρησιμοποιούνται για τις αρχικές τιμές, ώστε να ξεκινήσει η διαδικασία.
		int[][] table = new int[FR_CNT][VI_CNT];

		// Αρχικοποίηση της πρώτης στήλης σε 0.
		// Αυτό γιατί όσα φρούτα και να έχουμε, χρειαζόμαστε 0 από αυτά για να εκπληρώσουμε 0 συνολικές βιταμίνες.
	    for (int i = 0; i < FR_CNT; i++) {
	        table[i][0] = 0;
	    }
	    
	    // Αρχικοποίηση της πρώτης γραμμής σε inf.
	    // Αυτό γιατί θεωρώντας το φρούτο "0" με περιεκτικότητα 0, χρειαζόμαστε άπειρα από αυτά για οποιοδήποτε ποσό βιταμίνης που καλούμαστε να εκπληρώσουμε.
	    for (int j = 0; j < VI_CNT; j++) {
	        table[0][j] = INF;
	    }

	    // Η αναδρομική εξίσωση που χρησιμοποιείται είναι η εξής:
	    //               
	    //                  / min{ 1 + table[i][j-Vits(i-1)] , table[i-1][j] } όταν j >= Vits(i-1)
	    //    table[i,j] = |
	    //                  \ table[i-1][j]                                    όταν j < Vits(i-1)
	    // 
	    // Όπου Vits(i-1) είναι η περιεκτικότητα σε βιταμίνες του φρούτου με δείκτη i-1 μέσα στο arrayList fruits.

	    // Γίνεται επεξεργασία των κελιών από αριστερά προς τα δεξιά και από πάνω προς τα κάτω. Παραλείπεται η πρώτη γραμμή και στήλη, καθώς είναι οι αρχικές μας τιμές.
	    // Ο αλγόριθμος αυτός εκτελείται σε Ο(FR_CNT-1)*Ο(VI_CNT-1) = Ο(FR_CNT*VI_CNT). Επίσης τελειώνει καθώς οι δείκτες των loops συγκλίνουν στην τερματική συνθήκη.
	    for (int fruitIndex = 1; fruitIndex < FR_CNT; fruitIndex++) {
	    	
	        for (int currentVits = 1; currentVits < VI_CNT; currentVits++) {
	        	
	            if (currentVits >= fruits.get(fruitIndex-1).vitAmount) {
	            	// ’νω σκέλος της αναδρομικής εξίσωσης.
		        	tempFruitAmount = table[fruitIndex][currentVits - fruits.get(fruitIndex-1).vitAmount];
		        	table[fruitIndex][currentVits] = Math.min(1 + tempFruitAmount, table[fruitIndex-1][currentVits]);
	            } else {
	            	// Κάτω σκέλος της αναδρομικής εξίσωσης.
	            	table[fruitIndex][currentVits] = table[fruitIndex-1][currentVits];
	            }
	        }
	    }

//////////////////////////////////////////////////////////////////////////////////////////
//**Βήμα 3: Εξαγωγή των συγγεκριμένων φρούτων που επιλέχτηκαν από τον παραπάνω πίνακα** //	
//////////////////////////////////////////////////////////////////////////////////////////  
	    
	    // Βοηθητικοί δείκτες που αρχικά δείχνουν στην κάτω δεξιά γωνία του πίνακα, δηλαδή στην λύση.
	    // ’ν υπάρχει λύση, χρησιμοποιούνται για να βρούμε ποιά ακριβώς φρούτα επιλέχθηκαν.
	    int i = fruits.size();
	    int j = targetVits;
	    
	    // ’ν το κελί της λύσης είναι ίσο με INF, τότε δέν υπάρχει λύση στο πρόβλημα, δηλαδή με κανένα πιθανό συνδυασμό δέν μπορούμε να πετύχουμε
	    // ακριβώς το ποσό της βιταμίνης που θέλουμε. Εκτυπώνουμε έτσι μήνυμα αποτυχίας.
	    if (table[i][j]==INF) {
	    	System.out.println("No solution!");
	    } else {
	    	// Διαφορετικά εκτυπώνουμε τον αριθμό των ελάχιστων φρούτων.
	    	System.out.println("Smallest amount of fruits needed: " + table[i][j]);
	    	
	    	// Στη συνέχεια, βρίσκουμε ποιά ακριβώς φρούτα είναι αυτά και αυξάνουμε το πεδίο needAmount ανάλογα.
	    	// Αγνοούμε τη πρώτη στήλη και γραμμή, καθώς ήταν καθαρά βοηθητικές.
	    	while (i>0 && j>0) {
	    		// Στον πίνακα, άν βρισκόμαστε στο table[i][j], και το από πάνω του (table[i-1][j]) είναι μεγαλύτερο,
	    		//σημαίνει πως πήραμε σίγουρα ενα φρούτο το οποίο αντιστοιχεί στην γραμμή αυτή...
	    		if (table[i][j]<table[i-1][j]) {
	    			// Αυξάνουμε έτσι το needAmount του συγγεκριμένου φρούτου. Προσοχή, ο δείκτης στο arrayList fruits είναι i-1
	    			// και όχι i, καθώς δέν συμπεριλαμβάνεται το "μηδενικό" φρούτο του πίνακα table.
	    			fruits.get(i-1).needAmount++;
	    			// Επιπλέον προχωράμε τόσες θέσεις αριστερά, όσες η περιεκτικότητα σε βιταμίνες του φρούτου αυτόυ.
	    			j = j - fruits.get(i-1).vitAmount;
	    		
	    		// Σε διαφορετική περίπτωση, ανεβαίνουμε "προς τα πάνω" (i--) κατα μία θέση, καθώς δέν χρησιμοποιήσαμε .
	    		} else {
	    			i--;
	    		}
	    	}
	    	
	    	// Αφού τα βρούμε, εκτυπώνουμε τα φρούτα εκείνα με needAmount μεγαλύτερο του 0.
	    	for (int k=0;k<fruits.size();k++) {
	    		int amount = fruits.get(k).needAmount;
	    		if (amount>0) {
	    			System.out.println("| "+amount+" of "+fruits.get(k).name+".");
	    		}
	    	}
	    }
	}
}