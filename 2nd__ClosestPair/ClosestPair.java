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

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//--------------------------------------------------------------------------------------------------------------------------------
// Το πρόβλημα της εύρεσης του εγγύτερου ζεύγους είναι ενα πρόβλημα υπολογιστικής γεωμετρίας στο οποίο καλούμαστε να βρούμε τα δύο
// κοντινότερα σημεία στον δισδιάστατο χώρο. Χρησιμοποιώντας ωμή βία, το πρόβλημα λύνεται σε O(n^2), ελέγχοντας κάθε σημείο με όλα
// τα υπόλοιπα. Υπάρχει όμως τρόπος - υλοποιημένος στην άσκηση αυτή - να επιτύχουμε O(n*logn) χρησιμοποιώντας τεχνική διαίρει και
// βασίλευε παρόμοια με αυτή της mergesort.
//--------------------------------------------------------------------------------------------------------------------------------
// Το σκεπτικό της άσκησης ξεκινάει κατ' αρχάς με μια υλοποίηση της κλασσικής mergesort, αλλά υλοποιημένη στην bottom-up μορφή της
// για εξοικονόμηση χώρου (είμαστε περιορισμένοι σε Ο(n) χώρο).
// Η mergesort στη συνέχεια χρησιμοποιείται για να ταξινομήσει αρχικά τα σημεία ώς προς Χ, και αργότερα όποτε χρειαστεί ώς προς Υ.
// Στη συνέχεια καλείται ο αλγόριθμος αναζήτησης του εγγύτερου ζεύγους, ο οποίος δέχεται τα αρχικά ταξινομημένα σημεία ώς πρός Χ
// αλλά και ώς πρός Υ.
//
//
public class ClosestPair {
	
	// Σταθερές χρήσιμες για τη mergesort.
	private static final int SORT_BY_X=0;
	private static final int SORT_BY_Y=1;

	// Πετάει IOException εάν συμβεί σφάλμα με τα αρχεία.
	public static void main(String[] args) throws IOException {

//////////////////////////////////////////////////////////////////////////////////////////
//**Αρχικοποίηση: Δημιουργία απαραίτητων μεταβλητών για την εκτέλεση του προγράμματος** //	
//////////////////////////////////////////////////////////////////////////////////////////

		// ΑΠΟ ΕΔΩ ΚΑΙ ΚΑΤΩ ΜΑΣ ΕΝΔΙΑΦΕΡΕΙ Η ΠΟΛΥΠΛΟΚΟΤΗΤΑ ΧΩΡΟΥ.
		// Σχόλια που αφορούν πολυπλοκότητα χώρου ξεκινάν με //###
		
		String dir = System.getProperty("user.dir"); // Το directory που βρισκόμαστε κατα την εκτέλεση.
		String lineOfText; // Χρησιμοποιείται στο διάβασμα του prefs.txt.
		
		//### Οι παρακάτω δύο λίστες θα καταλάβουν χώρο Ο(2*n) = O(n), δηλαδή είμαστε εντός ορίων.
		ArrayList<Point> pointsX = new ArrayList<Point>(); // Σημεία στο επίπεδο ταξινομημένα κατά Χ.
		ArrayList<Point> pointsY = new ArrayList<Point>(); // Σημεία στο επίπεδο ταξινομημένα κατά Υ.
		int n = 0; // Μέγεθος της εισόδου.
		
///////////////////////////////////////////////////////////////////////////////////////////////
// **Βήμα 1: Διάβασμα του αρχείου points.txt και συμπλήρωση των μεταβλητών με τα δεδομένα.** //	
///////////////////////////////////////////////////////////////////////////////////////////////
		
		// Γίνεται προσπάθεια να ανόιξει το points.txt
		try {
			BufferedReader input = new BufferedReader(new FileReader(dir + "\\points.txt"));
			System.out.println("Input File Found! " + dir + "\\points.txt\n");
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
				
				// Διάβασμα των σημείων.
				while (lineOfText!=null) {
					m = p.matcher(lineOfText);
					m.find();
					int x = Integer.parseInt( m.group() );
					m.find();
					int y = Integer.parseInt( m.group() );
					
					// Τα προσθέτω και στα 2 arrays καθώς παρακάτω ταξινομούνται διαφορετικά.
					pointsX.add(new Point(x,y));
					pointsY.add(new Point(x,y));
					lineOfText = input.readLine();
				}
				
			} catch (IOException e) {
				// Περίπτωση σφάλματος.
				System.out.println("Error reading " + dir + "\\points.txt");
			} finally {
				// Κλείσιμο του αρχείου στο τέλος.
				input.close();
			}
		} catch (FileNotFoundException e) {
			// Περίπτωση που το points.txt λείπει.
			System.out.println("Input File Missing!" + "\nPlace it in " + dir);
			System.out.println("Program will now exit. Press any key to continue...");
			System.in.read(); // Αναμονή για χαρακτήρα και έξοδος.
			System.exit(0);
		}
		
//////////////////////////////////////////////////////////////////
//**Βήμα 2: Ταξινόμηση των πινάκων ώς προς Χ και Υ αντίστοιχα** //	
//////////////////////////////////////////////////////////////////	

		// ΑΠΟ ΕΔΩ ΚΑΙ ΚΑΤΩ ΜΑΣ ΕΝΔΙΑΦΕΡΕΙ Η ΧΡΟΝΙΚΗ ΠΟΛΥΠΛΟΚΟΤΗΤΑ.
		// Σχόλια που αφορούν χρονική πολυπλοκότητα ξεκινάν με //$$$
		
		// Εφόσον η ανάγνωση γίνει επιτυχώς, ταξινομώ τα στοιχεία ώς προς Χ και Υ σε διαφορετικούς πίνακες.
		//$$$ Εκτελείται δύο φορές η mergesort σε χρόνο Ο(2*n*logn) = Ο(n*logn), δηλαδή είμαστε εντός ορίων.
		//### Απαιτεί χώρο Ο(n) στην διαδικασία συνένωσης.
		// ( Τα παραπάνω εξηγούνται αναλυτικά εσωτερικά της συνάρτησης. )
		mergeSort(pointsX, SORT_BY_X);
		mergeSort(pointsY, SORT_BY_Y);
		
		// n μέγεθος της εισόδου.
		n = pointsX.size();
		
//////////////////////////////////////////////////////////////////////
//**Βήμα 3: Εκτέλεση του αλγορίθμου εύρεσης του εγγύτερου ζεύγους** //	
//////////////////////////////////////////////////////////////////////
		
		// Η συνάρτηση printClosestPair τυπώνει το αποτέλεσμα στο χρήστη.
		if ( n == 1 ) {
			// Περίπτωση εισόδου με μόνο ένα σημείο
			printClosestPair(pointsX.get(0),pointsX.get(0));			
		} else if ( n == 2 ) {
			// Περίπτωση εισόδου με μόνο δύο σημεία
			printClosestPair(pointsX.get(0),pointsX.get(1));			
		} else {
			// Περίπτωση με n>2
			Point[] closest = closestPair(pointsX, pointsY, n);
			printClosestPair(closest[0],closest[1]);			
		}
	}
	
	
	// Η παρακάτω υλοποίηση της mergesort είναι bottom-up, δηλαδή δέν είναι αναδρομική αλλά επαναληπτική για αποφυγή ξεπέ-
	// ρασης του ορίου στη πολυπλοκότητα χώρου.
	// Δέχεται ενα επιπλέον όρισμα (int sydetagmenh) το οποίο καθορίζει ώς προς ποιά συντεταγμένη θα ταξινομηθούν τα σημεία.
	//$$$ Χρονική πολυπλοκότητα Ο(n*logn).
	//### Πολυπλοκότητα χώρου O(n).
	// ( Επεξηγούνται αναλυτικά παρακάτω. )
	public static void mergeSort(ArrayList<Point> array, int sydetagmenh) {
		
		// Ένα στοιχείο =  ταξινομημένος πίνακας.
		if(array.size() == 1) return;
		
		// Δύο στοιχεία, εύρεση μεγαλύτερου.
        if(array.size() == 2) {
        	// Η μεταβλητή sydetagmenh καθορίζει ώς προς τί να γίνει η ταξινόμηση. ( SORT_BY_X ή SORT_BY_Υ)
    	    if(sydetagmenh==SORT_BY_X){	
    	 	    if (array.get(0).x>array.get(1).x){
    	 	    	Point temp = new Point(array.get(0));
    	 	    	array.set(0, array.get(1));
    	 	    	array.set(1, temp);
    	 	    }
    	    } else if(sydetagmenh==SORT_BY_Y){
    	    	if (array.get(0).y>array.get(1).y){
    	    		Point temp = new Point(array.get(0));
    	    		array.set(0, array.get(1));
    	    		array.set(1, temp);
    	    	}
    	    }
    	    return;
        }
        // Το βήμα (step) αυξάνεται *2 κάθε φορά.
        int step = 1;
        int startL, startR;
        
        // Όσο βήμα δέν ξεπερνάει το μέγεθος του πίνακα...
        while(step < array.size()) {
            startL = 0;
            startR = step;
            //$$$ Το συγγεκριμένο while δίνει χρονική πολυπλοκότητα O(logn) καθώς το step αυξάνεται εκθετικά και οι επαναλήψεις είναι
            //$$$ ελάχιστες σε σχέση με το μέγεθος της εισόδου.
            while(startR + step <= array.size()) {
            	// Ένωσε τους υποπίνακες με το τρέχων step...
            	//$$$ Η μέθοδος merge εκτελείται σε O(n).
            	//### H μέθοδος merge απαιτεί Ο(n) χώρο καθώς απελευθερώνεται σε κάθε εκτέλεση.
                merge(array, startL, startL + step, startR, startR + step, sydetagmenh); 
                startL = startR + step;
                startR = startL + step;
            }
            if(startR < array.size()) {
                merge(array, startL, startL + step, startR, array.size(), sydetagmenh);
            }
            step = step*2;
        }	
        //$$$ Συνολική πολυπλοκότητα χρόνου: Ο(logn)*O(n) = Ο(n*logn)
        //### Συνολική πολυπλοκότητα χώρου: Ο(n)
	}

	
	// Βοηθητική συνάρτηση-μέρος της mergesort.
	// Ενώνει τους υποπίνακες μεταξύ τους τηρώντας αύξουσα ταξινόμηση.
	//$$$ Χρονική πολυπλοκότητα Ο(n).
	//### Πολυπλοκότητα χώρου O(n).
	// ( Επεξηγούνται αναλυτικά παρακάτω. )
    public static void merge(ArrayList<Point> array, int startL, int stopL, int startR, int stopR, int sydetagmenh) {
    	
    	// Δήλωση βοηθητικών λιστών για αριστερό και δεξιό πίνακα.
    	//### Οι λίστες αυτές καταστρέφονται όταν η συνάρτηση merge επιστρέψει, άρα δέν θα έχουμε O(nlogn) απαιτήσεις χώρου, αλλά Ο(n).
		ArrayList<Point> right = new ArrayList<Point>();
		ArrayList<Point> left = new ArrayList<Point>();
		
		// Γέμισμα των παραπάνω λιστών με τις τιμές που τους αντιστοιχούν
		//$$$ Εκτελείται σε O(n).
        for(int i = 0, k = startR; i < (stopR - startR); ++i, ++k) {
        	right.add(array.get(k));
        }
        //$$$ Εκτελείται σε O(n).
        for(int i = 0, k = startL; i < (stopL - startL); ++i, ++k) {
        	left.add(array.get(k));
        }
        // Βοηθητικά "dummy" στοιχεία χρήσιμα στην ταξινόμηση παρακάτω.
        right.add(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE));
        left.add(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        int m=0;
        int n=0;
        //$$$ Εκτελείται σε O(n).
        for(int k = startL; k < stopR; ++k) {
        	// Ανάλογα με την επιλεγμένη σταθερά - συντεταγμένη, γίνεται ο συνδυασμός των 2 κομματιών σε ένα.
        	if(sydetagmenh==SORT_BY_X){
        		if(left.get(m).x <= right.get(n).x) {
        			array.set(k, left.get(m));
        			m++;
        		} else {
        			array.set(k, right.get(n));
        			n++;
        		}
        	} else if(sydetagmenh==SORT_BY_Y){
        		if(left.get(m).y <= right.get(n).y) {
        			array.set(k, left.get(m));
        			m++;
        		} else {
        			array.set(k, right.get(n));
        			n++;
        		}
        	}
        }	
        //$$$ Συνολική πολυπλοκότητα χρόνου: 3*Ο(n) = Ο(n)
        //### Συνολική πολυπλοκότητα χώρου: 2*O(n/2) = Ο(n)
    }
    
    
	// Η συνάρτηση αυτή βρίσκει το εγγύτερο ζεύγος. Δέχεται ώς ορίσματα τα σημεία ταξινομημένα ώς προς και τις δύο συντεταγμένες,
    // προφανώς σε διαφορετικό πίνακα, καθώς και το μέγεθος της εισόδου.
	//$$$ Χρονική πολυπλοκότητα Ο(n*logn).
	//### Πολυπλοκότητα χώρου O(n).
	// ( Επεξηγούνται αναλυτικά παρακάτω. )
	public static Point[] closestPair(ArrayList<Point> pointsX, ArrayList<Point> pointsY, int n) {
		
		// Εδώ αποθηκεύεται το τρέχον εγγύτερο ζεύγος.
		//### Δεσμεύεται Ο(1) χώρος.
		Point[] closest = new Point[2];
		// Οριακές περιπτώσεις για n=1 και n=2.
		if (n==1){
			// Ουσιαστικά προσομοιώνω άπειρη απόσταση...
			closest[0] = new Point(-Integer.MAX_VALUE,-Integer.MAX_VALUE);
			closest[1] = new Point(Integer.MAX_VALUE,Integer.MAX_VALUE);
			return closest;
		}
		if (n==2){
			closest[0] = new Point(pointsX.get(0));
			closest[1] = new Point(pointsX.get(1));
			return closest;
		}
		
		// Οι παρακάτω βοηθητικές λίστες χρησιμοποιούνται για να χωρίσουμε το σύνολο των σημείων στη μέση.
		//### Εδώ δεσμεύω χώρο 4*O(n). Παρόλο όμως που βρισκόμαστε μέσα στην αναδρομή, ακριβώς πρίν επιστρέψει η συνάρτηση,
		//### αποδεσμεύω όλο τον χώρο που καταλαμβάνουν τα ArrayLists χρησιμοποιώντας την εντολή clear(); (Βλέπε τέλος συναρτησης).
		//### Τελική πολυπλοκότητα 4*Ο(n) = Ο(n) και όχι Ο(nlogn)!
		ArrayList<Point> xL = new ArrayList<Point>();
		ArrayList<Point> yL = new ArrayList<Point>();
		ArrayList<Point> xR = new ArrayList<Point>();
		ArrayList<Point> yR = new ArrayList<Point>();
		
		// Διαίρεση σε δύο υποπροβλήματα και γέμισμα των λιστών.
		int mesh = (n/2)-1;
	    Point midpoint = pointsX.get(mesh);
		// Γέμισμα των λιστών των Χ με τα αντίστοιχα σημεία από την μεγάλη λίστα που τα έχει ταξινομημένα.
		//$$$ Εκτελείται σε Ο(n).
		for (int i=0;i<=mesh;i++){
			xL.add(pointsX.get(i));
		}
		//$$$ Εκτελείται σε Ο(n).
		for (int i=mesh+1;i<=n-1;i++){
		    xR.add(pointsX.get(i));
		}
		// Γέμισμα των λιστών των Υ με τα αντίστοιχα σημεία από την μεγάλη λίστα που τα έχει ταξινομημένα ώς προς Υ.
		// Ο έλεγχος γίνεται με το ενδιάμεσο σημείο όπως προφανώς φαίνεται παρακάτω.
		//$$$ Εκτελείται σε Ο(n).
		for (int i=0;i<pointsY.size();i++){
			if(pointsY.get(i).x <= midpoint.x){
				yL.add(pointsY.get(i));
			} else {
			    yR.add(pointsY.get(i));
			}
		}
		
	    //conquer
		Point[] closestL = new Point[2];
		closest = closestPair(xL,yL,n/2);
	    closestL[0] = new Point(closest[0]);
	    closestL[1] = new Point(closest[1]);
	    double distL = closestL[0].distance(closestL[1]);

		Point[] closestR = new Point[2];
		closest = closestPair(xR,yR,n/2);
	    closestR[0] = new Point(closest[0]);
	    closestR[1] = new Point(closest[1]);
	    double distR = closestR[0].distance(closestR[1]);
	    
	    //combine
	    double minDist = 0;
	    if(distL<distR){
	    	minDist = distL;
	    	closest = closestL;
	    } else {
	    	minDist = distR;
	    	closest = closestR;
	    }
	    
		ArrayList<Point> yLine = new ArrayList<Point>();
		for (int i=0;i<pointsY.size();i++){
			if(Math.abs(pointsY.get(i).x-midpoint.x)<minDist) {
				// Επειδή προσθέτω στοιχεία από το pointsY, ο yLine που θα προκύψει θα είναι ήδη ταξινομημένος.
				yLine.add(pointsY.get(i));
			}
		}
		for(int i=0;i<=yLine.size()-2;i++) {
			int k = i+1;
			
			while (k<=yLine.size()-1 && Math.abs(yLine.get(k).y-yLine.get(i).y)<minDist){
				double tempDist = yLine.get(k).distance(yLine.get(i));
			    if(tempDist<minDist){
			    	minDist = tempDist;
					closest[0] = new Point(yLine.get(i));
					closest[1] = new Point(yLine.get(k));
			    }
			    k++;
			}
		}
		
		// ’δειασμα των πινάκων ώστε να μήν καταλαμβάνουν χώρο.
		yL.clear();
		yR.clear();
		xL.clear();
		xR.clear();
		yLine.clear();
		return closest;
	}	
	
	
	// Η συνάρτηση αυτή απλώς τυπώνει το αποτέλεσμα στο χρήστη.
	public static void printClosestPair(Point pointA, Point pointB){
		if(pointA.equals(pointB)){
			System.out.println("|=========== ERROR ===========");
			System.out.println("| Either only a single point was given\n| or the input containts double entries!");
			System.out.println("|=============================");
		} else {
			System.out.println("|========== SUCCESS ==========");
			System.out.println("| Closest Pair: ("+pointA.x+","+pointA.y+") <-> ("+pointB.x+","+pointB.y+")");
			System.out.println("| Distance: "+pointA.distance(pointB));
			System.out.println("|=============================");
		}
	}
	
}


