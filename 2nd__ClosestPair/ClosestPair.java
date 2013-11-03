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
// �� �������� ��� ������� ��� ��������� ������� ����� ��� �������� ������������� ���������� ��� ����� ���������� �� ������ �� ���
// ����������� ������ ���� ����������� ����. ��������������� ��� ���, �� �������� ������� �� O(n^2), ���������� ���� ������ �� ���
// �� ��������. ������� ���� ������ - ������������ ���� ������ ���� - �� ���������� O(n*logn) ��������������� ������� ������� ���
// �������� �������� �� ���� ��� mergesort.
//--------------------------------------------------------------------------------------------------------------------------------
// �� �������� ��� ������� �������� ���' ����� �� ��� ��������� ��� ��������� mergesort, ���� ����������� ���� bottom-up ����� ���
// ��� ������������ ����� (������� ������������� �� �(n) ����).
// � mergesort ��� �������� ��������������� ��� �� ����������� ������ �� ������ �� ���� �, ��� �������� ����� ��������� �� ���� �.
// ��� �������� �������� � ���������� ���������� ��� ��������� �������, � ������ ������� �� ������ ������������ ������ �� ���� �
// ���� ��� �� ���� �.
//
//
public class ClosestPair {
	
	// �������� �������� ��� �� mergesort.
	private static final int SORT_BY_X=0;
	private static final int SORT_BY_Y=1;

	// ������ IOException ��� ������ ������ �� �� ������.
	public static void main(String[] args) throws IOException {

//////////////////////////////////////////////////////////////////////////////////////////
//**������������: ���������� ����������� ���������� ��� ��� �������� ��� ������������** //	
//////////////////////////////////////////////////////////////////////////////////////////

		// ��� ��� ��� ���� ��� ���������� � ������������� �����.
		// ������ ��� ������� ������������� ����� ������� �� //###
		
		String dir = System.getProperty("user.dir"); // �� directory ��� ����������� ���� ��� ��������.
		String lineOfText; // ��������������� ��� �������� ��� prefs.txt.
		
		//### �� �������� ��� ������ �� ���������� ���� �(2*n) = O(n), ������ ������� ����� �����.
		ArrayList<Point> pointsX = new ArrayList<Point>(); // ������ ��� ������� ������������ ���� �.
		ArrayList<Point> pointsY = new ArrayList<Point>(); // ������ ��� ������� ������������ ���� �.
		int n = 0; // ������� ��� �������.
		
///////////////////////////////////////////////////////////////////////////////////////////////
// **���� 1: �������� ��� ������� points.txt ��� ���������� ��� ���������� �� �� ��������.** //	
///////////////////////////////////////////////////////////////////////////////////////////////
		
		// ������� ���������� �� ������� �� points.txt
		try {
			BufferedReader input = new BufferedReader(new FileReader(dir + "\\points.txt"));
			System.out.println("Input File Found! " + dir + "\\points.txt\n");
			// ������ �������� ��������, ������� ���������� ����������� ��� ����.
			try {
				// ������� ����� ����� ������� ��� �� ����� ��������.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// Pattern ��� matcher ��� ������������ ��� ������� ��� ����������� ��������� ���.
				Pattern p = Pattern.compile("-?\\d+");
				Matcher m = p.matcher(lineOfText);
				
				// �������� ��� �������.
				while (lineOfText!=null) {
					m = p.matcher(lineOfText);
					m.find();
					int x = Integer.parseInt( m.group() );
					m.find();
					int y = Integer.parseInt( m.group() );
					
					// �� �������� ��� ��� 2 arrays ����� �������� ������������� �����������.
					pointsX.add(new Point(x,y));
					pointsY.add(new Point(x,y));
					lineOfText = input.readLine();
				}
				
			} catch (IOException e) {
				// ��������� ���������.
				System.out.println("Error reading " + dir + "\\points.txt");
			} finally {
				// �������� ��� ������� ��� �����.
				input.close();
			}
		} catch (FileNotFoundException e) {
			// ��������� ��� �� points.txt ������.
			System.out.println("Input File Missing!" + "\nPlace it in " + dir);
			System.out.println("Program will now exit. Press any key to continue...");
			System.in.read(); // ������� ��� ��������� ��� ������.
			System.exit(0);
		}
		
//////////////////////////////////////////////////////////////////
//**���� 2: ���������� ��� ������� �� ���� � ��� � ����������** //	
//////////////////////////////////////////////////////////////////	

		// ��� ��� ��� ���� ��� ���������� � ������� �������������.
		// ������ ��� ������� ������� ������������� ������� �� //$$$
		
		// ������ � �������� ����� ��������, �������� �� �������� �� ���� � ��� � �� ������������� �������.
		//$$$ ���������� ��� ����� � mergesort �� ����� �(2*n*logn) = �(n*logn), ������ ������� ����� �����.
		//### ������� ���� �(n) ���� ���������� ���������.
		// ( �� �������� ���������� ��������� ��������� ��� ����������. )
		mergeSort(pointsX, SORT_BY_X);
		mergeSort(pointsY, SORT_BY_Y);
		
		// n ������� ��� �������.
		n = pointsX.size();
		
//////////////////////////////////////////////////////////////////////
//**���� 3: �������� ��� ���������� ������� ��� ��������� �������** //	
//////////////////////////////////////////////////////////////////////
		
		// � ��������� printClosestPair ������� �� ���������� ��� ������.
		if ( n == 1 ) {
			// ��������� ������� �� ���� ��� ������
			printClosestPair(pointsX.get(0),pointsX.get(0));			
		} else if ( n == 2 ) {
			// ��������� ������� �� ���� ��� ������
			printClosestPair(pointsX.get(0),pointsX.get(1));			
		} else {
			// ��������� �� n>2
			Point[] closest = closestPair(pointsX, pointsY, n);
			printClosestPair(closest[0],closest[1]);			
		}
	}
	
	
	// � �������� ��������� ��� mergesort ����� bottom-up, ������ ��� ����� ���������� ���� ������������ ��� ������� ����-
	// ����� ��� ����� ��� ������������� �����.
	// ������� ��� �������� ������ (int sydetagmenh) �� ����� ��������� �� ���� ���� ������������ �� ������������ �� ������.
	//$$$ ������� ������������� �(n*logn).
	//### ������������� ����� O(n).
	// ( ������������ ��������� ��������. )
	public static void mergeSort(ArrayList<Point> array, int sydetagmenh) {
		
		// ��� �������� =  ������������� �������.
		if(array.size() == 1) return;
		
		// ��� ��������, ������ �����������.
        if(array.size() == 2) {
        	// � ��������� sydetagmenh ��������� �� ���� �� �� ����� � ����������. ( SORT_BY_X � SORT_BY_�)
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
        // �� ���� (step) ��������� *2 ���� ����.
        int step = 1;
        int startL, startR;
        
        // ��� ���� ��� ��������� �� ������� ��� ������...
        while(step < array.size()) {
            startL = 0;
            startR = step;
            //$$$ �� ������������ while ����� ������� ������������� O(logn) ����� �� step ��������� �������� ��� �� ����������� �����
            //$$$ ��������� �� ����� �� �� ������� ��� �������.
            while(startR + step <= array.size()) {
            	// ����� ���� ���������� �� �� ������ step...
            	//$$$ � ������� merge ���������� �� O(n).
            	//### H ������� merge ������� �(n) ���� ����� ��������������� �� ���� ��������.
                merge(array, startL, startL + step, startR, startR + step, sydetagmenh); 
                startL = startR + step;
                startR = startL + step;
            }
            if(startR < array.size()) {
                merge(array, startL, startL + step, startR, array.size(), sydetagmenh);
            }
            step = step*2;
        }	
        //$$$ �������� ������������� ������: �(logn)*O(n) = �(n*logn)
        //### �������� ������������� �����: �(n)
	}

	
	// ��������� ���������-����� ��� mergesort.
	// ������ ���� ���������� ������ ���� �������� ������� ����������.
	//$$$ ������� ������������� �(n).
	//### ������������� ����� O(n).
	// ( ������������ ��������� ��������. )
    public static void merge(ArrayList<Point> array, int startL, int stopL, int startR, int stopR, int sydetagmenh) {
    	
    	// ������ ���������� ������ ��� �������� ��� ����� ������.
    	//### �� ������ ����� �������������� ���� � ��������� merge ����������, ��� ��� �� ������ O(nlogn) ���������� �����, ���� �(n).
		ArrayList<Point> right = new ArrayList<Point>();
		ArrayList<Point> left = new ArrayList<Point>();
		
		// ������� ��� �������� ������ �� ��� ����� ��� ���� ������������
		//$$$ ���������� �� O(n).
        for(int i = 0, k = startR; i < (stopR - startR); ++i, ++k) {
        	right.add(array.get(k));
        }
        //$$$ ���������� �� O(n).
        for(int i = 0, k = startL; i < (stopL - startL); ++i, ++k) {
        	left.add(array.get(k));
        }
        // ��������� "dummy" �������� ������� ���� ���������� ��������.
        right.add(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE));
        left.add(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        int m=0;
        int n=0;
        //$$$ ���������� �� O(n).
        for(int k = startL; k < stopR; ++k) {
        	// ������� �� ��� ���������� ������� - ������������, ������� � ���������� ��� 2 ��������� �� ���.
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
        //$$$ �������� ������������� ������: 3*�(n) = �(n)
        //### �������� ������������� �����: 2*O(n/2) = �(n)
    }
    
    
	// � ��������� ���� ������� �� �������� ������. ������� �� �������� �� ������ ������������ �� ���� ��� ��� ��� �������������,
    // �������� �� ����������� ������, ����� ��� �� ������� ��� �������.
	//$$$ ������� ������������� �(n*logn).
	//### ������������� ����� O(n).
	// ( ������������ ��������� ��������. )
	public static Point[] closestPair(ArrayList<Point> pointsX, ArrayList<Point> pointsY, int n) {
		
		// ��� ������������ �� ������ �������� ������.
		//### ���������� �(1) �����.
		Point[] closest = new Point[2];
		// ������� ����������� ��� n=1 ��� n=2.
		if (n==1){
			// ���������� ����������� ������ ��������...
			closest[0] = new Point(-Integer.MAX_VALUE,-Integer.MAX_VALUE);
			closest[1] = new Point(Integer.MAX_VALUE,Integer.MAX_VALUE);
			return closest;
		}
		if (n==2){
			closest[0] = new Point(pointsX.get(0));
			closest[1] = new Point(pointsX.get(1));
			return closest;
		}
		
		// �� �������� ���������� ������ ���������������� ��� �� ��������� �� ������ ��� ������� ��� ����.
		//### ��� ������� ���� 4*O(n). ������ ���� ��� ����������� ���� ���� ��������, ������� ���� ���������� � ���������,
		//### ���������� ��� ��� ���� ��� ������������� �� ArrayLists ��������������� ��� ������ clear(); (����� ����� ����������).
		//### ������ ������������� 4*�(n) = �(n) ��� ��� �(nlogn)!
		ArrayList<Point> xL = new ArrayList<Point>();
		ArrayList<Point> yL = new ArrayList<Point>();
		ArrayList<Point> xR = new ArrayList<Point>();
		ArrayList<Point> yR = new ArrayList<Point>();
		
		// �������� �� ��� ������������� ��� ������� ��� ������.
		int mesh = (n/2)-1;
	    Point midpoint = pointsX.get(mesh);
		// ������� ��� ������ ��� � �� �� ���������� ������ ��� ��� ������ ����� ��� �� ���� ������������.
		//$$$ ���������� �� �(n).
		for (int i=0;i<=mesh;i++){
			xL.add(pointsX.get(i));
		}
		//$$$ ���������� �� �(n).
		for (int i=mesh+1;i<=n-1;i++){
		    xR.add(pointsX.get(i));
		}
		// ������� ��� ������ ��� � �� �� ���������� ������ ��� ��� ������ ����� ��� �� ���� ������������ �� ���� �.
		// � ������� ������� �� �� ��������� ������ ���� �������� �������� ��������.
		//$$$ ���������� �� �(n).
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
				// ������ �������� �������� ��� �� pointsY, � yLine ��� �� �������� �� ����� ��� �������������.
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
		
		// �������� ��� ������� ���� �� ��� ������������� ����.
		yL.clear();
		yR.clear();
		xL.clear();
		xR.clear();
		yLine.clear();
		return closest;
	}	
	
	
	// � ��������� ���� ����� ������� �� ���������� ��� ������.
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


