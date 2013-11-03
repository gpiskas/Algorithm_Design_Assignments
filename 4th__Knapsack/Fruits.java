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
	
	// ������ IOException ��� ������ ������ �� �� ������.
	public static void main(String[] args) throws IOException {	
		
///////////////////////////////////////////////////////////////////////////////////////////
//**������������: ���������� ����������� ���������� ��� ��� �������� ��� ������������** //	
///////////////////////////////////////////////////////////////////////////////////////////
		
		// ��������� ����� ��� �������������� ��� ������.
		// �������� ��� ����� ��� �� ����� ��� ������� (name), ��� ��� �� �������������� ��� �� ��������� (vitAmount)
		// ��� ��� ��� ��� �������� ��� ���� ��� ��������� ��� ����� (needAmount). �� needAmount �������������� �� 0
		// ��� ��� �� ������ ���� ��������������, ����� � �������� ��� �� ����������� ����������� ���� �� ����� ��� �����������,
		// ���� ��� ������ ��� ��������� ��� ��� ��������� ��������� ���������������.
		class Fruit {
			
			private String name;
			private int vitAmount;
			private int needAmount;
			
			// ������������� - ������� ����� ��� ��������������.
			public Fruit(String fName,int fVits) {
				name = fName;
				vitAmount = fVits;	
				needAmount = 0;	
			}

			// � toString �� ��������� ��� ��������� � �������� ���� �������.
			// ����������: Name: A|Vits: 25|Needed: 0
			@Override
			public String toString() {
				return "Name: "+this.name+"|Vits: "+this.vitAmount+"|Needed: "+this.needAmount;
			}	
		}
		
		String dir = System.getProperty("user.dir"); // �� directory ��� ����������� ���� ��� ��������.
		String lineOfText; // ��������������� ��� �������� ��� fruits.txt.
		
		// � ����� �� �� ������.
		ArrayList<Fruit> fruits = new ArrayList<Fruit>(); 
		// � �������� �������� ��� ���������.
		int targetVits = 0; 
		
///////////////////////////////////////////////////////////////////////////////////////////////
// **���� 1: �������� ��� ������� fruits.txt ��� ���������� ��� ���������� �� �� ��������.** //	
///////////////////////////////////////////////////////////////////////////////////////////////
		
		// ������� ���������� �� ������� �� fruits.txt
		try {
			BufferedReader input = new BufferedReader(new FileReader(dir + "\\fruits.txt"));
			System.out.println("Input File Found! " + dir + "\\fruits.txt\n");
			// ������ �������� ��������, ������� ���������� ����������� ��� ����.
			try {
				// ������� ����� ����� ������� ��� �� ����� ��������.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// �������� ����� ��� ��������� totalVits.
				targetVits = Integer.parseInt(lineOfText);
				
				// ������� ����� ����� ������� ��� �� ������.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// Pattern ��� matcher ��� ������������ ��� ������� ��� ���������.
				// ��� �����, ��� ��� �� ����� (frName) ��� ��� ��� ��� ������ ��� ��������� (frVits).
				Pattern frName_p = Pattern.compile("[a-zA-Z]+");
				Matcher frName_m = frName_p.matcher(lineOfText);
				
				Pattern frVits_p = Pattern.compile("\\d+");
				Matcher frVits_m = frVits_p.matcher(lineOfText);

				// �������� ��� ������� ��� �� ����� ��� �������.
				while (lineOfText!=null) {

					// ������ ��� �������� �� ��� ����� �������� �������.
					frName_m = frName_p.matcher(lineOfText);
					frName_m.find();
					String name = frName_m.group();
					
					// ������ ��� ��������������� �� ��� ������� �������� �������.
					frVits_m = frVits_p.matcher(lineOfText);
					frVits_m.find();
					int vitAmount = Integer.parseInt( frVits_m.group() );		
			
					// ��� ������ ����� ��� input, ������ Exception ��� �� ��������� ������������.
					if (name==null || vitAmount<=0) {
						throw new Exception();
					}
				
					// ��������� ���� ���� ������� �� �� �������� �������������� ��� ���������� ��� fruits.
					Fruit aFruit = new Fruit(name, vitAmount);
					fruits.add(aFruit);
					
					// �������� ��� �������� �� ����� �������.
					lineOfText = input.readLine();
					while (lineOfText!=null && lineOfText.equals("")) {
						lineOfText = input.readLine();
					}
				}
				
			} catch (IOException e) {
				// ��������� ���������.
				System.out.println("Error reading " + dir + "\\fruits.txt");
			} catch (Exception e) {
				// ��������� ��������� ��� ��������.
				System.out.println("Input file contains invalid data.");
				System.out.println("| Make sure fruit names are not missing and consist of [a-zA-Z] characters.");
				System.out.println("| Make sure fruit vitamin amounts are not missing and are positive non-zero integers.");
				System.out.println("Program will now exit.");
				System.exit(0);
			} finally {
				// �������� ��� ������� ��� �����.
				input.close();
			}
		} catch (FileNotFoundException e) {
			// ��������� ��� �� fruits.txt ������.
			System.out.println("Input File Missing!");
			System.out.println("| Place it in " + dir);
			System.out.println("Program will now exit.");
			System.exit(0);
		}

///////////////////////////////////////////////////////////////////////////////////
// **���� 2: �������� ��� ���������� ������� ��� �����������-��������� �������** //	
///////////////////////////////////////////////////////////////////////////////////
		
		// �� ���� ��� ���� ������ �� ����� ��� �������� ���� ���� ���� � ����� ������ (table[0][i] = inf)  ��� � ����� ����� (table[i][0] = 0)  �� ����� �� ������� ����� ��� ������ ���.
		final int FR_CNT = fruits.size()+1; // fruitCount
		final int VI_CNT = targetVits+1; // vitaminCount
		
		// ��� ������ ���� (INF) � ����� ��������������� ��� �� ������������ �����������.
		final int INF = 2*targetVits;
		// ��������� ��������� ��� ��������������� ���� �������� ���������. �� ���� ���� �������� �� ����������� ����������.
		int tempFruitAmount;
		
		// � ������������ ������� ��� �� ��������.
		// �� ������ ��������������� �� ������, �� ������� ��������������� ��� ���������.
		// �� ��������� �������� ����� ��� 0 ��� targetVits (� ������ ���).
		// �� ������ �������� ����� ��� 0 ��� fruitCount. "�����������" ��� ������� ��� ��� �� �� ����� ����.
		// � ����� ��� ������� ��� ��� ����������, ��� ��' ���� ��� �� �����������. ������������ � ��, �� ������� ����� ���� �� ������ ��� �� ��� ��� ������� � ����� ������ ��� �� ��������.
		// �� �������� ������� ������������ ������ �� �� ����� ��� ������� ��� arrayList fruits ����� ��� (����� �������� ��� �� 0).
		// ����������� ��� ������� ��� ����������� ����� ��� ��� ������ (table[i][0] ��� table[0][i]). ����� ���������������� ��� ��� ������� �����, ���� �� ��������� � ����������.
		int[][] table = new int[FR_CNT][VI_CNT];

		// ������������ ��� ������ ������ �� 0.
		// ���� ����� ��� ������ ��� �� ������, ������������ 0 ��� ���� ��� �� ������������ 0 ��������� ���������.
	    for (int i = 0; i < FR_CNT; i++) {
	        table[i][0] = 0;
	    }
	    
	    // ������������ ��� ������ ������� �� inf.
	    // ���� ����� ��������� �� ������ "0" �� �������������� 0, ������������ ������ ��� ���� ��� ����������� ���� ��������� ��� ���������� �� ������������.
	    for (int j = 0; j < VI_CNT; j++) {
	        table[0][j] = INF;
	    }

	    // � ���������� ������� ��� ��������������� ����� � ����:
	    //               
	    //                  / min{ 1 + table[i][j-Vits(i-1)] , table[i-1][j] } ���� j >= Vits(i-1)
	    //    table[i,j] = |
	    //                  \ table[i-1][j]                                    ���� j < Vits(i-1)
	    // 
	    // ���� Vits(i-1) ����� � �������������� �� ��������� ��� ������� �� ������ i-1 ���� ��� arrayList fruits.

	    // ������� ����������� ��� ������ ��� �������� ���� �� ����� ��� ��� ���� ���� �� ����. ������������ � ����� ������ ��� �����, ����� ����� �� ������� ��� �����.
	    // � ���������� ����� ���������� �� �(FR_CNT-1)*�(VI_CNT-1) = �(FR_CNT*VI_CNT). ������ ��������� ����� �� ������� ��� loops ���������� ���� ��������� �������.
	    for (int fruitIndex = 1; fruitIndex < FR_CNT; fruitIndex++) {
	    	
	        for (int currentVits = 1; currentVits < VI_CNT; currentVits++) {
	        	
	            if (currentVits >= fruits.get(fruitIndex-1).vitAmount) {
	            	// ��� ������ ��� ����������� ��������.
		        	tempFruitAmount = table[fruitIndex][currentVits - fruits.get(fruitIndex-1).vitAmount];
		        	table[fruitIndex][currentVits] = Math.min(1 + tempFruitAmount, table[fruitIndex-1][currentVits]);
	            } else {
	            	// ���� ������ ��� ����������� ��������.
	            	table[fruitIndex][currentVits] = table[fruitIndex-1][currentVits];
	            }
	        }
	    }

//////////////////////////////////////////////////////////////////////////////////////////
//**���� 3: ������� ��� ������������� ������� ��� ����������� ��� ��� �������� ������** //	
//////////////////////////////////////////////////////////////////////////////////////////  
	    
	    // ���������� ������� ��� ������ �������� ���� ���� ����� ����� ��� ������, ������ ���� ����.
	    // �� ������� ����, ���������������� ��� �� ������ ���� ������� ������ �����������.
	    int i = fruits.size();
	    int j = targetVits;
	    
	    // �� �� ���� ��� ����� ����� ��� �� INF, ���� ��� ������� ���� ��� ��������, ������ �� ������ ������ ��������� ��� �������� �� ���������
	    // ������� �� ���� ��� ��������� ��� �������. ����������� ���� ������ ���������.
	    if (table[i][j]==INF) {
	    	System.out.println("No solution!");
	    } else {
	    	// ����������� ����������� ��� ������ ��� ��������� �������.
	    	System.out.println("Smallest amount of fruits needed: " + table[i][j]);
	    	
	    	// ��� ��������, ��������� ���� ������� ������ ����� ���� ��� ��������� �� ����� needAmount �������.
	    	// �������� �� ����� ����� ��� ������, ����� ���� ������ ����������.
	    	while (i>0 && j>0) {
	    		// ���� ������, �� ����������� ��� table[i][j], ��� �� ��� ���� ��� (table[i-1][j]) ����� ����������,
	    		//�������� ��� ������ ������� ��� ������ �� ����� ����������� ���� ������ ����...
	    		if (table[i][j]<table[i-1][j]) {
	    			// ��������� ���� �� needAmount ��� ������������� �������. �������, � ������� ��� arrayList fruits ����� i-1
	    			// ��� ��� i, ����� ��� ����������������� �� "��������" ������ ��� ������ table.
	    			fruits.get(i-1).needAmount++;
	    			// �������� ��������� ����� ������ ��������, ���� � �������������� �� ��������� ��� ������� �����.
	    			j = j - fruits.get(i-1).vitAmount;
	    		
	    		// �� ����������� ���������, ����������� "���� �� ����" (i--) ���� ��� ����, ����� ��� ��������������� .
	    		} else {
	    			i--;
	    		}
	    	}
	    	
	    	// ���� �� ������, ����������� �� ������ ������ �� needAmount ���������� ��� 0.
	    	for (int k=0;k<fruits.size();k++) {
	    		int amount = fruits.get(k).needAmount;
	    		if (amount>0) {
	    			System.out.println("| "+amount+" of "+fruits.get(k).name+".");
	    		}
	    	}
	    }
	}
}