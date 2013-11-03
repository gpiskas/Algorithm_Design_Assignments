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
import java.util.HashMap;

public class Horspool {

	// Πετάει IOException εάν συμβεί σφάλμα με τα αρχεία.
	public static void main(String[] args) throws IOException {

//////////////////////////////////////////////////////////////////////////////////////////
//**Αρχικοποίηση: Δημιουργία απαραίτητων μεταβλητών για την εκτέλεση του προγράμματος** //	
//////////////////////////////////////////////////////////////////////////////////////////
		
		String dir = System.getProperty("user.dir"); // Το directory που βρισκόμαστε κατα την εκτέλεση.
		String lineOfText; // Χρησιμοποιείται στο διάβασμα του data.txt.
		
		// Το αλφάβητο του κειμένου σαν σταθερά.
		final char[] ALPHABET = {'A','B','C','D','E','F','0','1','2','3','4','5','6','7','8','9'};
		
		// Το πρότυπο και το κείμενο αποθηκευμένα σε strings.
		String pattern = new String();
		String text = new String();
		
		// Το μέγεθος του προτύπου και του κειμενου.
		int patternLen = 0;
		int textLen = 0;
		
		// Ο πίνακας ολίσθισης αποθηκευμένος σε δομή κατακερματισμού.
		HashMap<Character, Integer> olistish = new HashMap<Character, Integer>();
		
		// Λίστα που θα περιέχει όλες τις θέσεις που βρέθηκε το πρότυπο.
		ArrayList<Integer> hits = new ArrayList<Integer>();
		
/////////////////////////////////////////////////////////////////////////////////////////////
// **Βήμα 1: Διάβασμα του αρχείου data.txt και συμπλήρωση των μεταβλητών με τα δεδομένα.** //	
/////////////////////////////////////////////////////////////////////////////////////////////
		
		// Γίνεται προσπάθεια να ανόιξει το data.txt
		try {
			BufferedReader input = new BufferedReader(new FileReader(dir + "\\data8.txt"));
			System.out.println("Input File Found! " + dir + "\\data.txt\n");
			// Εφόσον ανοιχτεί επιτυχώς, γίνεται προσπάθεια ανάγνωσης.
			try {
				// Αγνόηση τυχόν κενών γραμμών έως τα πρώτα δεδομένα.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// Διάβασμα του προτύπου
				pattern = lineOfText;
				// Αντικατάσταση των λευκών χαρακτήρων (άν υπάρχουν) με το κενό χρησιμοποιώντας κανονική έκφραση.
				pattern=pattern.replaceAll("\\s", "");
				// Έυρεση του μήκους.
				patternLen = pattern.length();
				
				// Αγνόηση τυχόν κενών γραμμών έως τα πρώτα δεδομένα.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// Διάβασμα του κειμένου.
				// Χρήση StringBuilder ώστε να συνενώσουμε όλες τις γραμμές του κειμένου (άν υπάρχουν παραπάνω από μία) σε μία.
				StringBuilder textArea = new StringBuilder();
				while (lineOfText!=null) {
					textArea.append(lineOfText);
					lineOfText = input.readLine();
				}
				// Μετατροπή του StringBuilder σε String.
				text = textArea.toString();
				// Αντικατάσταση των λευκών χαρακτήρων (άν υπάρχουν) με το κενό χρησιμοποιώντας κανονική έκφραση.
				text=text.replaceAll("\\s", "");
				// Έυρεση του μήκους.
				textLen = text.length();
				
			} catch (IOException e) {
				// Περίπτωση σφάλματος.
				System.out.println("Error reading " + dir + "\\data.txt");
			} finally {
				// Κλείσιμο του αρχείου στο τέλος.
				input.close();
			}
		} catch (FileNotFoundException e) {
			// Περίπτωση που το points.txt λείπει.
			System.out.println("Input File Missing!" + "\nPlace it in " + dir);
			System.out.println("Program will now exit. Press Enter to continue...");
			System.in.read(); // Αναμονή για χαρακτήρα και έξοδος.
			System.exit(0);
		}		
		
////////////////////////////////////////////////////////////////////////////////
//**Βήμα 2: Κατασκευή του πίνακα ολίσθισης για κάθε χαρακτήρα του αλφάβητου** //	
////////////////////////////////////////////////////////////////////////////////

		// ΑΠΟ ΕΔΩ ΚΑΙ ΚΑΤΩ ΜΑΣ ΕΝΔΙΑΦΕΡΕΙ Η ΧΡΟΝΙΚΗ ΠΟΛΥΠΛΟΚΟΤΗΤΑ.
		// Σχόλια που αφορούν χρονική πολυπλοκότητα ξεκινάν με //$$$
		// Έστω:
		// n = μέγεθος του κειμένου.
		// m = μέγεθος του προτύπου.
		// k = μέγεθος του αλφάβητου.
		
		
		
		// Αρχικοποίηση όλων των θέσεων σε patternLen (μήκος του pattern).
		//$$$ Εκτελείται σε Ο(k).
		for (int i=0;i<ALPHABET.length;i++) {
			olistish.put(ALPHABET[i], patternLen);
		}
		
		// Εκχώρηση της σωστής τιμής για κάθε χαρακτήρα του pattern, σύμφωνα με τον αλγόριθμο Horspool.
		// Ξεκινώντας από το τέλος πρός την αρχή και αγνοώντας το δεξιότερο χαρακτήρα (εξού και το δεύτερο "-1")...
		//$$$ Εκτελείται σε Ο(m).
	    for (int i=(patternLen-1)-1;i>=0;i--) {
	    	char tempChar = pattern.charAt(i);
	    	// ...άν η τιμή δέν έχει μεταβληθεί, την αλλάζουμε στη θέση του δεξιότερου χαρακτήρα που βρίσκουμε.
	    	if (olistish.get(tempChar)==patternLen) {
	    		olistish.put(tempChar, (patternLen-i)-1);
	    	}
	    }
		
	    //$$$ Το βήμα 2 εκτελέστηκε σε συνολικα O(m)+O(k) = Ο(m+k) χρόνο.
	    // Όσον αφορα την ορθότητα, ο αλγόριθμος τελειώνει καθώς οι δείκτες των επαναλήψεων πλησιάζουν σταθερά την τερματική συνθήκη τους.
	    
/////////////////////////////////////////////////
//**Βήμα 3: Εκτέλεση του αλγορίθμου Horspool** //	
/////////////////////////////////////////////////
	    
	    // Μετρητής συγκρίσεων.
	    int comparisons=0;
	    // Δείκτης που δείχνει σε ποιά θέση του κειμένου βρίσκεται το τέλος του προτύπου.
	    int ptr = patternLen-1;
	    
	    // Όσο δέν έχουμε φτάσει το τέλος του κειμένου...
	    //$$$ Εκτελείται σε Ο(n-m) αλλά ισχύει πως n>>m άρα ουσιαστικά Ο(n).
	    while (ptr < textLen) {
	       // Βοηθητικός δείκτης για τη σύγκριση χαρακτήρα προς χαρακτήρα (Αρχικοποίηση σε -1 λόγω της δομής do-while).
	       int pos= -1;
	       System.out.println(text.charAt(ptr));
	       // Σύγκριση χαρακτήρων από τα δεξιά πρός τα αριστερά.
	       //$$$ Εκτελείται σε Ο(m).
	       do{
	    	   pos++;
	    	   // ’ν ο δείκτης ξεφύγει εκτός ορίων, βγαίνουμε από την επανάληψη χωρίς να προσθέσουμε σύγκριση γραμμάτων.
	    	   if (pos==patternLen) break;
	    	   comparisons++;
	       } while( text.charAt(ptr-pos)==pattern.charAt(patternLen-1-pos) );
	       
	       // Όταν βγούμε από το loop, άν ο δείκτης pos είναι ίσος με το μέγεθος του πρωτύπου,
	       // γνωρίζουμε πως έχουμε επιτυχία και η θέση αυτής προστίθεται στη λίστα.
	       if (pos==patternLen) {
	    	   hits.add(ptr-patternLen+2); // +2 ώστε να ξεκινάμε τη μέτρηση από το 1 και όχι από το 0.
	       }
	       // Η διαδικασία συνεχίζεται ολισθαίνοντας το πρότυπο δεξιά κατα τόσες μονάδες, όσο αντιστοιχούν στον χαρακτήρα που
	       // βρίσκεται στη θέση που δέιχνει ο ptr.
	       ptr += olistish.get( text.charAt(ptr) );
	    }
		
	    //$$$ Το βήμα 3 εκτελέστηκε σε συνολικα O(n)*O(m) = Ο(n*m) χρόνο στη χειρότερη των περιπτώσεων.
	    //$$$ Αποδεικνύεται όμως πως για τυχαίο κείμενο, επιτυγχάνεται πολυπλοκότητα της τάξης του Ο(n).
	    // Ο αλγόριθμος και πάλι τελειώνει καθώς στον δείκτη ptr προστίθενται θετικές τιμές και αναπόφευκτα θα φτάσει την τερματική συνθήκη.
	    
//////////////////////////////////////////////////////
//**Βήμα 4: Εκτύπωση των αποτελεσμάτων στην οθόνη** //	
//////////////////////////////////////////////////////
	    
	    // Εκτύπωση του πίνακα ολίσθησης.
	    System.out.println("*Πίνακας Ολίσθησης:");
	    for (int i=0;i<ALPHABET.length;i++) {
	    	char ch=ALPHABET[i];
	    	System.out.println(ch+" --> "+olistish.get(ch));
	    }
	    System.out.println(pattern
	    		);
	    // Εκτύπωση όλων των θέσεων που βρέθηκε το πρότυπο.
	    System.out.println("\n*Θέσεις που βρέθηκαν πρότυπα στο κείμενο (Το κείμενο ξεκινάει από τη θέση 1, όχι από 0):");
	    if (!hits.isEmpty()) System.out.println("Αριθμός επιτυχιών: "+ hits.size()
	    									  +"\nΘέσεις στο κείμενο: "+ hits);
	    else System.out.println("Δέν βρέθηκαν αποτελέσματα!");

	    // Εκτύπωση των συνολικών συγκρίσεων με τον αλγόριθμο Horspool.
	    System.out.println("\n*Συνολικές συγκρίσεις με αναζήτηση κατά Horspool:");
	    System.out.println(comparisons);
	    
	    // Εκτύπωση των συνολικών συγκρίσεων με τον αλγόριθμο ωμής βίας.
	    System.out.println("\n*Συνολικές συγκρίσεις με αναζήτηση Ωμής Βίας:");
	    // Εκτέλεση του αλγορίθμου για εξαγωγή των τιμών.
	    //$$$ Εκτελείται σε Ο(n*m).
	    int[] bfResults = bruteForce(pattern,text);
	    // Ελεγχος πως βρέθηκε ο ίδιος αριθμός αποτελεσμάτων (το οποίο πάντα θα αληθεύει..).	    
	    if (bfResults[0]==hits.size()) System.out.println(bfResults[1]);
	}
	
	// Αλγόριθμος ταύτισης πρωτύπου με τη μέθοδο της ωμής βίας.
	// Χρησιμοποιείται για την τελευταία εκτύπωση που ζητάει η εκφώνηση.
	// Δέχεται το πρότυπο και το κείμενο σάν ορίσματα, και επιστρέφει τον αριθμό των ταυτίσεων,
	// και τις συνολικές συγκρίσεις που έγιναν.
	//$$$ Εκτελείται σε Ο(n*m).
	private static int[] bruteForce(String pattern, String text){
		// results = {number of hits,comparisons}
		int[] results= new int[2];
		results[0]=0;
		results[1]=0;
		// Ο ptr δέιχνει που βρισκόμαστε στο κείμενο
		int ptr=0;
		int textLen=text.length();
		int patternLen=pattern.length();
		
		// Όσο δέν έχουμε φτάσει το ελάχιστο δυνατό μέγεθος που μπορούμε να ελέγγσουμε...
		//$$$ Εκτελείται σε Ο(n).
		while ( ptr <= textLen - patternLen ) {
			// Ο i δέιχνει που βρισκόμαστε στο πρότυπο (δηλαδή διαφέρει από τον ptr)
			int i=0;
			results[1]++;
			// Ελέγχουμε αν είναι όμοιοι οι χαρακτήρες όλου του προτύπου..
			//$$$ Εκτελείται σε Ο(m).
			while (i<patternLen && pattern.charAt(i)==text.charAt(ptr+i)) {
				// ’ν βρεθούν όλοι όμοιοι, προσθέτουμε ενα αποτέλεσμα στον μετρητή. Αλλιώς προχωράμε παρακάτω και προσθέτουμε σύγκριση.
				if (i==patternLen-1) {
					results[0]++;
					break;
				} else {
					results[1]++;
					i++;
				}
			}
			// Αλλιώς συνεχίζουμε την αναζήτηση παρακάτω.
			ptr++;
		}
		return results;
		//$$$ Συνολικά εκτελείται σε Ο(n*m).
	}
}