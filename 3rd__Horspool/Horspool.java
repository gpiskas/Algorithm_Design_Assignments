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

	// ������ IOException ��� ������ ������ �� �� ������.
	public static void main(String[] args) throws IOException {

//////////////////////////////////////////////////////////////////////////////////////////
//**������������: ���������� ����������� ���������� ��� ��� �������� ��� ������������** //	
//////////////////////////////////////////////////////////////////////////////////////////
		
		String dir = System.getProperty("user.dir"); // �� directory ��� ����������� ���� ��� ��������.
		String lineOfText; // ��������������� ��� �������� ��� data.txt.
		
		// �� �������� ��� �������� ��� �������.
		final char[] ALPHABET = {'A','B','C','D','E','F','0','1','2','3','4','5','6','7','8','9'};
		
		// �� ������� ��� �� ������� ������������ �� strings.
		String pattern = new String();
		String text = new String();
		
		// �� ������� ��� �������� ��� ��� ��������.
		int patternLen = 0;
		int textLen = 0;
		
		// � ������� ��������� ������������� �� ���� ���������������.
		HashMap<Character, Integer> olistish = new HashMap<Character, Integer>();
		
		// ����� ��� �� �������� ���� ��� ������ ��� ������� �� �������.
		ArrayList<Integer> hits = new ArrayList<Integer>();
		
/////////////////////////////////////////////////////////////////////////////////////////////
// **���� 1: �������� ��� ������� data.txt ��� ���������� ��� ���������� �� �� ��������.** //	
/////////////////////////////////////////////////////////////////////////////////////////////
		
		// ������� ���������� �� ������� �� data.txt
		try {
			BufferedReader input = new BufferedReader(new FileReader(dir + "\\data8.txt"));
			System.out.println("Input File Found! " + dir + "\\data.txt\n");
			// ������ �������� ��������, ������� ���������� ���������.
			try {
				// ������� ����� ����� ������� ��� �� ����� ��������.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// �������� ��� ��������
				pattern = lineOfText;
				// ������������� ��� ������ ���������� (�� ��������) �� �� ���� ��������������� �������� �������.
				pattern=pattern.replaceAll("\\s", "");
				// ������ ��� ������.
				patternLen = pattern.length();
				
				// ������� ����� ����� ������� ��� �� ����� ��������.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// �������� ��� ��������.
				// ����� StringBuilder ���� �� ����������� ���� ��� ������� ��� �������� (�� �������� �������� ��� ���) �� ���.
				StringBuilder textArea = new StringBuilder();
				while (lineOfText!=null) {
					textArea.append(lineOfText);
					lineOfText = input.readLine();
				}
				// ��������� ��� StringBuilder �� String.
				text = textArea.toString();
				// ������������� ��� ������ ���������� (�� ��������) �� �� ���� ��������������� �������� �������.
				text=text.replaceAll("\\s", "");
				// ������ ��� ������.
				textLen = text.length();
				
			} catch (IOException e) {
				// ��������� ���������.
				System.out.println("Error reading " + dir + "\\data.txt");
			} finally {
				// �������� ��� ������� ��� �����.
				input.close();
			}
		} catch (FileNotFoundException e) {
			// ��������� ��� �� points.txt ������.
			System.out.println("Input File Missing!" + "\nPlace it in " + dir);
			System.out.println("Program will now exit. Press Enter to continue...");
			System.in.read(); // ������� ��� ��������� ��� ������.
			System.exit(0);
		}		
		
////////////////////////////////////////////////////////////////////////////////
//**���� 2: ��������� ��� ������ ��������� ��� ���� ��������� ��� ���������** //	
////////////////////////////////////////////////////////////////////////////////

		// ��� ��� ��� ���� ��� ���������� � ������� �������������.
		// ������ ��� ������� ������� ������������� ������� �� //$$$
		// ����:
		// n = ������� ��� ��������.
		// m = ������� ��� ��������.
		// k = ������� ��� ���������.
		
		
		
		// ������������ ���� ��� ������ �� patternLen (����� ��� pattern).
		//$$$ ���������� �� �(k).
		for (int i=0;i<ALPHABET.length;i++) {
			olistish.put(ALPHABET[i], patternLen);
		}
		
		// �������� ��� ������ ����� ��� ���� ��������� ��� pattern, ������� �� ��� ��������� Horspool.
		// ���������� ��� �� ����� ���� ��� ���� ��� ��������� �� ��������� ��������� (���� ��� �� ������� "-1")...
		//$$$ ���������� �� �(m).
	    for (int i=(patternLen-1)-1;i>=0;i--) {
	    	char tempChar = pattern.charAt(i);
	    	// ...�� � ���� ��� ���� ����������, ��� ��������� ��� ���� ��� ���������� ��������� ��� ���������.
	    	if (olistish.get(tempChar)==patternLen) {
	    		olistish.put(tempChar, (patternLen-i)-1);
	    	}
	    }
		
	    //$$$ �� ���� 2 ����������� �� �������� O(m)+O(k) = �(m+k) �����.
	    // ���� ����� ��� ��������, � ���������� ��������� ����� �� ������� ��� ����������� ���������� ������� ��� ��������� ������� ����.
	    
/////////////////////////////////////////////////
//**���� 3: �������� ��� ���������� Horspool** //	
/////////////////////////////////////////////////
	    
	    // �������� ����������.
	    int comparisons=0;
	    // ������� ��� ������� �� ���� ���� ��� �������� ��������� �� ����� ��� ��������.
	    int ptr = patternLen-1;
	    
	    // ��� ��� ������ ������ �� ����� ��� ��������...
	    //$$$ ���������� �� �(n-m) ���� ������ ��� n>>m ��� ���������� �(n).
	    while (ptr < textLen) {
	       // ���������� ������� ��� �� �������� ��������� ���� ��������� (������������ �� -1 ���� ��� ����� do-while).
	       int pos= -1;
	       System.out.println(text.charAt(ptr));
	       // �������� ���������� ��� �� ����� ���� �� ��������.
	       //$$$ ���������� �� �(m).
	       do{
	    	   pos++;
	    	   // �� � ������� ������� ����� �����, ��������� ��� ��� ��������� ����� �� ����������� �������� ���������.
	    	   if (pos==patternLen) break;
	    	   comparisons++;
	       } while( text.charAt(ptr-pos)==pattern.charAt(patternLen-1-pos) );
	       
	       // ���� ������ ��� �� loop, �� � ������� pos ����� ���� �� �� ������� ��� ��������,
	       // ���������� ��� ������ �������� ��� � ���� ����� ����������� ��� �����.
	       if (pos==patternLen) {
	    	   hits.add(ptr-patternLen+2); // +2 ���� �� �������� �� ������� ��� �� 1 ��� ��� ��� �� 0.
	       }
	       // � ���������� ����������� ������������� �� ������� ����� ���� ����� �������, ��� ������������ ���� ��������� ���
	       // ��������� ��� ���� ��� ������� � ptr.
	       ptr += olistish.get( text.charAt(ptr) );
	    }
		
	    //$$$ �� ���� 3 ����������� �� �������� O(n)*O(m) = �(n*m) ����� ��� ��������� ��� �����������.
	    //$$$ ������������� ���� ��� ��� ������ �������, ������������� ������������� ��� ����� ��� �(n).
	    // � ���������� ��� ���� ��������� ����� ���� ������ ptr ������������ ������� ����� ��� ����������� �� ������ ��� ��������� �������.
	    
//////////////////////////////////////////////////////
//**���� 4: �������� ��� ������������� ���� �����** //	
//////////////////////////////////////////////////////
	    
	    // �������� ��� ������ ���������.
	    System.out.println("*������� ���������:");
	    for (int i=0;i<ALPHABET.length;i++) {
	    	char ch=ALPHABET[i];
	    	System.out.println(ch+" --> "+olistish.get(ch));
	    }
	    System.out.println(pattern
	    		);
	    // �������� ���� ��� ������ ��� ������� �� �������.
	    System.out.println("\n*������ ��� �������� ������� ��� ������� (�� ������� �������� ��� �� ���� 1, ��� ��� 0):");
	    if (!hits.isEmpty()) System.out.println("������� ���������: "+ hits.size()
	    									  +"\n������ ��� �������: "+ hits);
	    else System.out.println("��� �������� ������������!");

	    // �������� ��� ��������� ���������� �� ��� ��������� Horspool.
	    System.out.println("\n*��������� ���������� �� ��������� ���� Horspool:");
	    System.out.println(comparisons);
	    
	    // �������� ��� ��������� ���������� �� ��� ��������� ���� ����.
	    System.out.println("\n*��������� ���������� �� ��������� ���� ����:");
	    // �������� ��� ���������� ��� ������� ��� �����.
	    //$$$ ���������� �� �(n*m).
	    int[] bfResults = bruteForce(pattern,text);
	    // ������� ��� ������� � ����� ������� ������������� (�� ����� ����� �� ��������..).	    
	    if (bfResults[0]==hits.size()) System.out.println(bfResults[1]);
	}
	
	// ���������� �������� �������� �� �� ������ ��� ���� ����.
	// ��������������� ��� ��� ��������� �������� ��� ������ � ��������.
	// ������� �� ������� ��� �� ������� ��� ��������, ��� ���������� ��� ������ ��� ���������,
	// ��� ��� ��������� ���������� ��� ������.
	//$$$ ���������� �� �(n*m).
	private static int[] bruteForce(String pattern, String text){
		// results = {number of hits,comparisons}
		int[] results= new int[2];
		results[0]=0;
		results[1]=0;
		// � ptr ������� ��� ����������� ��� �������
		int ptr=0;
		int textLen=text.length();
		int patternLen=pattern.length();
		
		// ��� ��� ������ ������ �� �������� ������ ������� ��� �������� �� ����������...
		//$$$ ���������� �� �(n).
		while ( ptr <= textLen - patternLen ) {
			// � i ������� ��� ����������� ��� ������� (������ �������� ��� ��� ptr)
			int i=0;
			results[1]++;
			// ��������� �� ����� ������ �� ���������� ���� ��� ��������..
			//$$$ ���������� �� �(m).
			while (i<patternLen && pattern.charAt(i)==text.charAt(ptr+i)) {
				// �� ������� ���� ������, ����������� ��� ���������� ���� �������. ������ ��������� �������� ��� ����������� ��������.
				if (i==patternLen-1) {
					results[0]++;
					break;
				} else {
					results[1]++;
					i++;
				}
			}
			// ������ ����������� ��� ��������� ��������.
			ptr++;
		}
		return results;
		//$$$ �������� ���������� �� �(n*m).
	}
}