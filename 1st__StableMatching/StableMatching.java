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
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//--------------------------------------------------------------------------------------------------------------------------------
// Το πρόβλημα που έχουμε διαφέρει από οποιοδήποτε πρόβλημα ευσταθούς ταιριάσματος στο γεγονός πως μια σχολή μπορεί να έχει >1 θέσεις.
// Γι' αυτό, το σκεπτικό μου όταν έλυσα το πρόβλημα ήταν για κάθε επιπλέον θέση να δημιουργίσω μια "ιδεατή" σχολή, η οποία φυσικά
// μοιράζεται τις ίδιες προτιμήσεις με την "μητρική" σχολή. Με το σκεπτικό αυτό, ουσιαστικά μετέτρεψα το πρόβλημα σε απλό πρόβλημα
// Stable matching. 
//--------------------------------------------------------------------------------------------------------------------------------
// Όσον αφορά τις δομές δεδομένων που χρησιμοποίησα, έκανα εκτενή χρήση από HashMaps επειδή ναι μεν απαιτούν γραμμικό χρόνο για κατασκευή,
// αλλά προσφέρουν προσπέλαση/αναζήτηση ενός στοιχείου σε Ο(1). Επίσης χρησιμοποιώ και Queues στις δομές εκείνες που μόνο θέλω το πρώτο
// στοιχείο τους κάθε φορά και τίποτα παραπάνω. Επιτυγχάνω έτσι το ζητούμενο Ο(Μ*Ν).
//--------------------------------------------------------------------------------------------------------------------------------
// ΑΠΟΔΕΙΞΕΙΣ ΕΥΣΤΑΘΕΙΑΣ:
//  Α) "Ένας υποψήφιος φοιτητής προτιμάει άλλη σχολή Μ΄ από αυτήν που τον δέχθηκε,
//      ενώ και η Μ΄ προτιμάει αυτόν τον φοιτητή από τουλάχιστον ένα φοιτητή της."
//
//  Λόγω της συνθήκης στη γραμμή 324, οι φοιτητές στο τέλος της εκτέλεσης βρίσκονται στις κατα το δυνατόν βέλτιστες επιλογές τους. Συνεπώς,
//  η εν λόγω αστάθεια είναι αδύνατη να συμβεί καθώς η προτεραιότητα του φοιτητή αυτού θα έιναι υψηλότερη από του ήδη επιλεγμένου.
//
//  Β) "Μία σχολή προτιμάει ένα φοιτητή, που δεν τον δέχθηκε καμία σχολή, από τουλάχιστον ένα φοιτητή της."
//
//  Επειδή ο αλγόριθμος είναι εξαντλητικός, δέν πρόκειται να υπάρξει τέτοιας μορφής αστάθεια, καθώς άν υπάρχει ενας τέτοιος φοιτητής, σίγουρα
//  θα είχε ελεγθεί η προτεραιότητά του, γεγονός που μας οδηγεί σε άτοπο. Έτσι αποδεικνύεται πως δέν υπάρχει καμία αστάθεια.
//--------------------------------------------------------------------------------------------------------------------------------
// Παρακάτω περιγράφεται όλο το σκεπτικό και η πολυπλοκότητα βήμα προς βήμα, όπως απαιτεί η εκφώνηση.
//--------------------------------------------------------------------------------------------------------------------------------
public class StableMatching {
	
	// Πετάει IOException εάν συμβεί σφάλμα με τα αρχεία.
	public static void main(String[] args) throws IOException {

/////////////////////////////////////////////////////////////////////////////////////////////////////////	
// **Βήμα 1: Λήψη του args[0] που έδωσε ο χρήστης, δηλαδή καθορισμός τρόπου εκτέλεσης του αλγορίθμου** //	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// Ελέγχεται κάθε δυνατή περίπτωση. ’ν δέν δοθεί παράμετρος, η άν είναι διαφορετική απο 1 ή 2,
		// τότε χρησιμοποιούμε το '1' ώς default value.
		if (args.length == 0){
			args = new String[1];
			args[0] = "1";
			System.out.println("No argument given. Default Argument Loaded... (1)");
		} else if (args[0].equals("1") || args[0].equals("2")){
			System.out.println("Argument ("+args[0]+") given!");
		} else {
			args[0] = "1";
			System.out.println("Invalid argument given. Default Argument Loaded... (1)");
		}
		
///////////////////////////////////////////////////////////////////////////////////////	
// **Βήμα 2.1: Δημιουργία απαραίτητων μεταβλητών για την εκτέλεση του προγράμματος** //	
///////////////////////////////////////////////////////////////////////////////////////
		
		String dir = System.getProperty("user.dir"); // Το directory που βρισκόμαστε κατα την εκτέλεση.
		String lineOfText; // Χρησιμοποιείται στο διάβασμα του prefs.txt.
		
		int M = 0; // Ο αριθμός των σχολών.
		int positions[]; // Ο αριθμός των θέσεων ανά σχολή.
		int N = 0; // Ο αριθμός των φοιτητών.
		
		Queue<Integer> schools = new LinkedList<Integer>(); // Ουρά που αποτελείται από τα ID των σχολών, δηλαδή από 1 έως Μ.
		Queue<Integer> students = new LinkedList<Integer>(); // Ουρά που αποτελείται από τα ID των σχολών, δηλαδή από 1 έως Ν.
		LinkedList<Integer> schoolPrefs[] = null; // Προτιμήσεις των σχολών στο αντίστοιχο στοιχείο του πίνακα συνδεδεμένων λιστών.
		LinkedList<Integer> schoolPrefsTemp[] = null; // Χρησιμοποιείται βοηθητικά μόνο στο διάβασμα του prefs.txt για τις σχολές με >1 θέσεις.
		LinkedList<Integer> studentPrefs[] = null; // Προτιμήσεις των φοιτητών στο αντίστοιχο στοιχείο του πίνακα συνδεδεμένων λιστών.
		
		HashMap<Integer, Integer> matches = new HashMap<Integer, Integer>(); // Εδώ αποθηκεύονται τα τελικά ζεύγη.
		
/////////////////////////////////////////////////////////////////////////////////////////////
// **Βήμα 2.2: Εξώρυξη δεδομένων από το prefs.txt και συμπλήρωση των παραπάνω μεταβλητών** //	
/////////////////////////////////////////////////////////////////////////////////////////////
		
		// Γίνεται προσπάθεια να ανόιξει το prefs.txt
		try {
			BufferedReader input = new BufferedReader(new FileReader(dir + "\\prefs.txt"));
			System.out.println("Input File Found! " + dir + "\\prefs.txt\n");
			// Εφόσον ανοιχτεί επιτυχώς, γίνεται προσπάθεια διαβάσματος από αυτό.
			try {
				// Αγνόηση τυχόν κενών γραμμών έως τα πρώτα δεδομένα.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// Pattern και matcher που διευκολύνουν την εξόρυξη των αριθμητικών δεδομένων μας.
				Pattern p = Pattern.compile("-?\\d+");
				Matcher m = p.matcher(lineOfText);
				
				// Διάβασμα των Μ και Ν.
				m.find();
				M = Integer.parseInt(m.group());
				m.find();
				N = Integer.parseInt(m.group());

				// Εφόσον βρήκαμε τα Μ και Ν, μπορούμε να αρχικοποιήσουμε τα Queues με τις προτιμήσεις των δύο γκρούπ,
				// καθώς και τον πίνακα positions[] με τις θέσεις του κάθε πανεπιστημίου.
				schoolPrefsTemp = new LinkedList[M]; // Θα περιέχει μόνο τις "πραγματικές" και όχι τις "ιδεατές" σχολές.
				studentPrefs = new LinkedList[N];
				positions = new int[M];
				
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// **Βήμα 2.2.1: Εύρεση όλων των σχολών στο prefs.txt και δημιουργία "ιδεατών" σχολών για κάθε επιπλέον θέση** //	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				// Αγνόηση τυχόν κενών γραμμών έως τα επόμενα δεδομένα.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// Διάβασμα του "πίνακα" M*N, γέμισμα των schoolPrefsTemp και positions.
				int extraM = 0; // Καταμετρά πόσες "ιδεατές" σχολές πρέπει να προστεθούν.
				for (int i = 0; i < M; i++) {
					schools.add(i+1); // Τα ID ξεκινάνε από το 1 έως το Μ.
					schoolPrefsTemp[i] = new LinkedList<Integer>();
					m = p.matcher(lineOfText);
					while (m.find()) { // Όσο βρίσκει αριθμούς-προτιμήσεις, τα τοποθετεί στο queue.
						schoolPrefsTemp[i].add(Integer.parseInt(m.group()));
					}
					// Λόγω format του prefs.txt, το πρώτο στοιχείο κάθε queue περιέχει τις θέσεις της εκάστοτε σχολής.
					positions[i] = schoolPrefsTemp[i].poll(); // Γι' αυτό, το αφαιρούμε.
					extraM = extraM + positions[i] - 1;
					lineOfText = input.readLine();
				}
				
				// Το schoolPrefs τώρα θα επεκταθέι με τα στοιχεία του schoolPrefsTemp ΣΥΝ τις "ιδεατές" σχολές.
				// Αρχικά γίνεται η αντιγραφή των temp...
				schoolPrefs = new LinkedList[M + extraM];
				for (int i = 0; i < M; i++) {
					schoolPrefs[i] = new LinkedList<Integer>();
					schoolPrefs[i].addAll(schoolPrefsTemp[i]);
				}
				
				// ...και ύστερα προστίθονται στο τέλος και οι "ιδεατές" σχολές.
				int counter = 0; // Χρησιμοποιείται στον καθορισμό του σωστού ID για τις "ιδεατές" σχολές.
				for (int i = 0; i < M; i++) {
					if(positions[i] > 1) {
						for(int j=1;j<positions[i];j++){
							
							// Το ID κάθε "ιδεατής" σχολής είναι ID_virtual(position_number) = ID_of_parent_school + (number_of_Schools+1)*position_number.
							//
							// ΠΧ:
							// ID_of_parent_school = 2   \
							// number_of_Schools = 3      }  ID_virtual(1) = 2 + (3+1)*1 = 6
							// position_number = 1       /
							//
							// Αυτό το έκανα ώστε με ενα απλό ID_virtual%(Μ+1) να παίρνω το αληθινό πανεπιστήμιο - γονέα.
							
							schools.add((i+1) + (M+1)*j);
							schoolPrefs[M + counter] = schoolPrefs[i];
							counter++;
						}
					}
				}
				
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// **Βήμα 2.2.2: Έυρεση όλων των φοιτητών και πρόσθεση και των "ιδεατών" προτιμήσεων σύμφωνα με τα παραπάνω** //	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				// Αγνόηση τυχόν κενών γραμμών έως τα επόμενα δεδομένα.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// Διάβασμα του "πίνακα" Ν*Μ, γέμισμα του studentPrefs.
				// Επιπλέον, προσθήκη των "ιδεατών" σχολών ώς προτιμήσεις.
				// η σειρά των προτιμήσεων των ιδεατών σχολών επηρεάζει το τελικό αποτέλεσμα,
				// και γι' αυτό τοποθετούνται αμέσως μετά την αληθινή σχολή. Έτσι το αποτέλεσμα παραμένει σωστό.
				for (int i = 0; i < N; i++) {
					students.add(i+1);
					studentPrefs[i] = new LinkedList<Integer>();
					m = p.matcher(lineOfText);
					while (m.find()) { // Χρησιμοποιείται η ίδια τεχνική με προηγουμένως.
						int pref = Integer.parseInt(m.group());
						studentPrefs[i].add(pref);
						
						if(positions[pref-1] > 1) {
							for(int j=1;j<positions[pref-1];j++){
								// Προσθέτω το ίδιο ID που δημιούργησα προηγουμένως.
								studentPrefs[i].add(pref + (M+1)*j);
							}
						}
					}
					lineOfText = input.readLine();
				}
			} catch (IOException e) {
				// Περίπτωση σφάλματος.
				System.out.println("Error reading " + dir + "\\prefs.txt");
			} finally {
				// Κλείσιμο του αρχείου στο τέλος.
				input.close();
			}
		} catch (FileNotFoundException e) {
			// Περίπτωση που το prefs.txt λείπει.
			System.out.println("Input File Missing!" + "\nPlace it in " + dir);
			System.out.println("Program will now exit. Press any key to continue...");
			System.in.read(); // Αναμονή για χαρακτήρα και έξοδος.
			System.exit(0);
		}

	
////////////////////////////////////////////////////////////////////
// **Βήμα 3: Εκτέλεση του αλγορίθμου για την παραγωγή ζευγαριών** //	
////////////////////////////////////////////////////////////////////
		
		// ΑΠΟ ΕΔΩ ΚΑΙ ΚΑΤΩ ΜΑΣ ΕΝΔΙΑΦΕΡΕΙ Η ΠΟΛΥΠΛΟΚΟΤΗΤΑ.
		// Σχόλια που αφορούν πολυπλοκότητα ξεκινάν με //***
		
		// Θα αναλύσω τη πολυπλοκότητα για τη περίπτωση που args[0]=1,
		// καθώς και στις δύο παραλλαγές είναι πανομοιότυπη και απλά αλλάζει το Μ με Ν.
		
		// Δέν χρησιμοποιώ απευθείας τα αντικείμενα που δημιούργισα παραπάνω για την αποθήκευση προτιμήσεων,
		// αλλά χρησιμοποιώ τα παρακάτω για ευελιξία. Υπάρχει απλό reference και όχι αντιγραφή όλων των τιμών,
		// δηλαδή μηδενική επίπτωση στην άυξηση της μνήμης.
		// Με τα παρακάτω references εκτελείται ο αλγόριθμος Stable Matching.
		Queue<Integer> groupA= null;
		LinkedList<Integer>[] groupAPrefs= null;
		LinkedList<Integer>[] groupBPrefs = null;
		int groupAsize = 0; 
		int groupBsize = 0; 
		
		// Τα παρακάτω tags χρησιμεύουν στο format του output.txt.
		// Ανάλογα με τη παράμετρο του χρήστη, γίνονται τα αντίστοιχα references.
		String tagA, tagB;
		if(args[0].equals("1")){
			tagA = "School ";
			tagB = " accepts student ";

			groupA = schools;
			
			groupAPrefs = schoolPrefs;
			groupBPrefs= studentPrefs;
			
			groupAsize = M;
			groupBsize = N;
		} else {
			tagA = "Student ";
			tagB = " goes to school ";
			
			groupA = students;
			
			groupAPrefs = studentPrefs;
			groupBPrefs= schoolPrefs;
			
			groupAsize = N;
			groupBsize = M;
		}
		//*** Ο παραπάνω κώδικας εκτελείται σε συνολικό χρόνο Ο(1) καθώς είναι ανεξάρτητος της εισόδου.
		
		// Αρχικοποίηση του HashMap matches σε 0 (δηλαδή κανένα ζευγάρι) για όλα τα κλειδιά.
		//*** Εκτελείται σε Ο(Μ), καθώς οι προτιμήσεις ενός μέλουςΒ περιέχουν Μ στοιχεία δηλαδή Μ επαναλήψεις.
		//*** Ο κατακερματισμός στο "matches" γίνεται σε Ο(1). Συνολικά δηλαδή έχουμε Ο(Μ) για τη κατασκευή της δομής.
		for (int i=0;i<groupBPrefs[0].size();i++){
			matches.put(groupBPrefs[0].get(i), 0);
		}
		
		//Βοηθητικό HashMap, ώστε να γνωρίζουμε σε χρόνο O(1) άν ένα μέλος του Β γκρούπ είναι ζευγαρωμένο και με ποιό - αρχικοποίηση σε 0 (κανένα ζευγάρι).
		//*** Ομοίως με προηγουμένως αλλά με Ν στοιχέια να κατακερματίζονται στο "isMatchedWith", αντί για Μ.
		//*** Συνολικά έχουμε Ο(Ν).
		HashMap<Integer, Integer> isMatchedWith = new HashMap<Integer, Integer>();
		for (int i=0;i<groupAPrefs[0].size();i++){
			isMatchedWith.put(groupAPrefs[0].get(i), 0);
		}
		
		//Βοηθητικό HashMap, ώστε να ανακτούμε σε χρόνο O(1) προτιμήσεις των ατόμων του Β γκρούπ. (χρησιμοποιείται στη σύγκριση δύο προτιμήσεων μεταξύ τους.
		//*** Συνολικά έχουμε Ο(Ν*Μ) χρόνο καθώς κατασκευάζουμε Ν HashMaps με Μ στοιχεία.
		HashMap<Integer, Integer> groupBPrefsHash[] = new HashMap[groupBsize];
		for (int i=0;i<groupBsize;i++){
			groupBPrefsHash[i] = new HashMap<Integer, Integer>();
			int j=1;
			while(!groupBPrefs[i].isEmpty()){
				groupBPrefsHash[i].put(groupBPrefs[i].poll(),j);
				j++;
			}
		}
		
		// Δήλωση βοηθητικών μεταβλητών
		int aMemberA=0; // Μέλος Α γκρούπ
		int aMemberB=0; // Μέλος Β γκρούπ
		int aMemberA_pair=0; // Μέλος Α γκρούπ, ζευγαρωμένο με ενα Β (για συγκρίσεις)
		
		// Ενημέρωση του χρήστη και εκκίνηση χρονόμετρου.
		System.out.println("Running...");
		long timeStart= System.nanoTime();
		
		//*** Ο παρακάτω βρόχος while θα εκτελεστεί μέχρι να αδειάσει το γκρούπ Α, δηλαδή σε Ο(Μ).
		//*** Ο έλεγχος groupA.isEmpty() προφανώς εκτελείται σε Ο(1) βλέποντας απλά άν υπάρχει κεφαλή στην ουρά.
		while(!groupA.isEmpty()){ // Όσο το γκρούπ Α δέν είναι άδειο...
			//*** Η εντολή groupA.poll() που λαμβάνει και αφαιρεί τη κεφαλή εκτελείται σε Ο(1) επίσης, καθώς είναι ιδιότητα της δομής της ουράς.
			aMemberA = groupA.poll(); // Λήψη της κεφαλής του γκρούπ Α 
			
			//*** Ο παρακάτω βρόχος while θα εκτελεστεί μέχρι να αδειάσει μια λίστα προτιμήσεων ενός μέλους Α. Γίνεται σε χρόνο Ο(Ν), όσα και τα στοιχεία στη λίστα groupAPrefs.
			//*** Ο έλεγχος groupAPrefs[(aMemberA%(groupAsize+1))-1].isEmpty() εκτελείται και αυτός σε Ο(1), λόγω απλού ελέγχου (ουσιαστικά απλά list_head==null).
			while(!groupAPrefs[(aMemberA%(groupAsize+1))-1].isEmpty()) { // Όσο οι προτιμήσεις του μέλους από το γκρούπ Α δέν τελείωσαν...
				aMemberB = groupAPrefs[(aMemberA%(groupAsize+1))-1].poll(); // Λήψη της κεφαλής από το παραπάνω //*** Ο(1) - ιδιότητα της ουράς.
				
				//*** Οι παρακάτω μέθοδοι (put & get) εκτελούνται σε Ο(1) - ιδιότητα προσπέλασης και εγγραφής του πίνακα κατακερματισμού.
				if( isMatchedWith.get(aMemberB) == 0 ){ // ’ν το μέλος aMemberB δέν έχει ζευγάρι...
					matches.put(aMemberA, aMemberB); // Έγινε ζευγάρι!
					isMatchedWith.put(aMemberB, aMemberA); // Σημέιωση του ζευγαριού στη βοηθητική δομή.
					break;
				}
				else {
					//*** Το isMatchedWith.get(aMemberB) εκτελείται σε Ο(1) για τον ίδιο λόγο με τα παραπάνω.
					aMemberA_pair = isMatchedWith.get(aMemberB);
					//*** Εδώ βρίσκεται η κύρια πράξη όλου του αλγορίθμου - η σύγκριση των δύο προτιμήσεων. Και οι δύο προτιμήσεις εξάγονται από HashMap σε χρόνο Ο(1).
					  if( groupBPrefsHash[(aMemberB%(groupBsize+1))-1].get(aMemberA%(groupAsize+1)) < groupBPrefsHash[(aMemberB%(groupBsize+1))-1].get(aMemberA_pair%(groupAsize+1)) ){
						// ’ν ο aMemberA προτιμάται από το υπάρχων ζεύγος με τον aMemberA_pair...
						//*** Όλα τα παρακάτω εκτελούνται και πάλι σε Ο(1) λόγω της ιδιότητας του Hashing.
						matches.put(aMemberA, aMemberB); // Έγινε ζευγάρι!
						matches.put(aMemberA_pair, 0); // Θέσε το aMemberA_pair ελεύθερο.
						isMatchedWith.put(aMemberB, aMemberA); // Σημέιωση του ζευγαριού στη βοηθητική δομή.
						//*** Η προσθήκη στοιχείου σε ουρά γίνεται σε Ο(1) καθώς το στοιχείο απλώς τοποθετείται στο τέλος.
						groupA.add(aMemberA_pair); // Πρόσθεσε τον νέο ελεύθερο στην ουρά.
						break;
					} // Αλλιώς η αναζήτηση συνεχίζεται...	
				}
			}
			// Εκτύπωση πληροφοριών εκτέλεσης για το χρήστη.
			System.out.println(tagA + aMemberA + tagB + matches.get(aMemberA)+".");
		}
		// Λήξη χρονόμετρου και ενημέρωση χρήστη.
		long timeFinish= System.nanoTime();
		long totalNSTime = timeFinish-timeStart;
		System.out.println("Finished! Execution time: "+ (totalNSTime/1000000.0)+" ms\n");
		
		// ΣΥΝΟΛΙΚΗ ΠΟΛΥΠΛΟΚΟΤΗΤΑ:
		//
		// Έξω από το πρώτο while, αθροίζοντας και απλοποιώντας το σύνολο των Ο, καταλήγουμε σε Ο(Μ*Ν)
		// Μέσα στους βρόχους, η βασική πράξη γίνεται το πολύ Ο(Ν*Μ) φορές, ενώ όλες οι άλλες εντολές δέν επηρεάζουν την πολυπλοκότητα καθώς είναι Ο(1).
		// Συνολικά λοιπόν έχουμε 2 * Ο(Μ*Ν), δηλαδή Ο(Μ*Ν)!
		
///////////////////////////////////////////////////////////////////
// **Βήμα 4: Καταγραφή των αποτελεσμάτων στο αρχείο output.txt** //	
///////////////////////////////////////////////////////////////////
		
		// Δημιουργία του output.txt στο ίδιο directory με το εκτελέσιμο.
		BufferedWriter output = new BufferedWriter(new FileWriter(dir + "\\output.txt"));
		
		// Γίνεται προσπάθεια εγγραφής με το ζητούμενο format της εκφώνησης.
		try {
			for (Integer key : matches.keySet()) {
				if(matches.get(key)%(groupBsize+1)==0) {
					output.write(tagA + key%(groupAsize+1) + " was not selected.");
				} else {
					output.write(tagA + key%(groupAsize+1) + tagB + matches.get(key)%(groupBsize+1)+".");
				}
				output.newLine();
			}
			// επιτυχής εγγραφή.
			System.out.println("Output.txt created successfully.");
		} catch (IOException e) {
			// περίπτωση σφάλματος.
			System.out.println("Error writing " + dir + "\\output.txt");
		} finally {
			// κλείσιμο του αρχείου στο τέλος.		
			output.close();
		}
 	}
}