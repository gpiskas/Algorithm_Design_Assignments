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
// �� �������� ��� ������ �������� ��� ����������� �������� ��������� ������������ ��� ������� ��� ��� ����� ������ �� ���� >1 ������.
// ��' ����, �� �������� ��� ���� ����� �� �������� ���� ��� ���� �������� ���� �� ����������� ��� "������" �����, � ����� ������
// ���������� ��� ����� ����������� �� ��� "�������" �����. �� �� �������� ����, ���������� ��������� �� �������� �� ���� ��������
// Stable matching. 
//--------------------------------------------------------------------------------------------------------------------------------
// ���� ����� ��� ����� ��������� ��� �������������, ����� ������ ����� ��� HashMaps ������ ��� ��� �������� �������� ����� ��� ���������,
// ���� ���������� ����������/��������� ���� ��������� �� �(1). ������ ����������� ��� Queues ���� ����� ������� ��� ���� ���� �� �����
// �������� ���� ���� ���� ��� ������ ��������. ���������� ���� �� ��������� �(�*�).
//--------------------------------------------------------------------------------------------------------------------------------
// ���������� ����������:
//  �) "���� ��������� �������� ��������� ���� ����� ̴ ��� ����� ��� ��� �������,
//      ��� ��� � ̴ ��������� ����� ��� ������� ��� ����������� ��� ������� ���."
//
//  ���� ��� �������� ��� ������ 324, �� �������� ��� ����� ��� ��������� ���������� ���� ���� �� ������� ��������� �������� ����. �������,
//  � �� ���� �������� ����� ������� �� ������ ����� � ������������� ��� ������� ����� �� ����� ��������� ��� ��� ��� �����������.
//
//  �) "��� ����� ��������� ��� �������, ��� ��� ��� ������� ����� �����, ��� ����������� ��� ������� ���."
//
//  ������ � ���������� ����� ������������, ��� ��������� �� ������� ������� ������ ��������, ����� �� ������� ���� ������� ��������, �������
//  �� ���� ������� � ������������� ���, ������� ��� ��� ������ �� �����. ���� ������������� ��� ��� ������� ����� ��������.
//--------------------------------------------------------------------------------------------------------------------------------
// �������� ������������ ��� �� �������� ��� � ������������� ���� ���� ����, ���� ������� � ��������.
//--------------------------------------------------------------------------------------------------------------------------------
public class StableMatching {
	
	// ������ IOException ��� ������ ������ �� �� ������.
	public static void main(String[] args) throws IOException {

/////////////////////////////////////////////////////////////////////////////////////////////////////////	
// **���� 1: ���� ��� args[0] ��� ����� � �������, ������ ���������� ������ ��������� ��� ����������** //	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// ��������� ���� ������ ���������. �� ��� ����� ����������, � �� ����� ����������� ��� 1 � 2,
		// ���� �������������� �� '1' �� default value.
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
// **���� 2.1: ���������� ����������� ���������� ��� ��� �������� ��� ������������** //	
///////////////////////////////////////////////////////////////////////////////////////
		
		String dir = System.getProperty("user.dir"); // �� directory ��� ����������� ���� ��� ��������.
		String lineOfText; // ��������������� ��� �������� ��� prefs.txt.
		
		int M = 0; // � ������� ��� ������.
		int positions[]; // � ������� ��� ������ ��� �����.
		int N = 0; // � ������� ��� ��������.
		
		Queue<Integer> schools = new LinkedList<Integer>(); // ���� ��� ����������� ��� �� ID ��� ������, ������ ��� 1 ��� �.
		Queue<Integer> students = new LinkedList<Integer>(); // ���� ��� ����������� ��� �� ID ��� ������, ������ ��� 1 ��� �.
		LinkedList<Integer> schoolPrefs[] = null; // ����������� ��� ������ ��� ���������� �������� ��� ������ ������������ ������.
		LinkedList<Integer> schoolPrefsTemp[] = null; // ��������������� ��������� ���� ��� �������� ��� prefs.txt ��� ��� ������ �� >1 ������.
		LinkedList<Integer> studentPrefs[] = null; // ����������� ��� �������� ��� ���������� �������� ��� ������ ������������ ������.
		
		HashMap<Integer, Integer> matches = new HashMap<Integer, Integer>(); // ��� ������������� �� ������ �����.
		
/////////////////////////////////////////////////////////////////////////////////////////////
// **���� 2.2: ������� ��������� ��� �� prefs.txt ��� ���������� ��� �������� ����������** //	
/////////////////////////////////////////////////////////////////////////////////////////////
		
		// ������� ���������� �� ������� �� prefs.txt
		try {
			BufferedReader input = new BufferedReader(new FileReader(dir + "\\prefs.txt"));
			System.out.println("Input File Found! " + dir + "\\prefs.txt\n");
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
				
				// �������� ��� � ��� �.
				m.find();
				M = Integer.parseInt(m.group());
				m.find();
				N = Integer.parseInt(m.group());

				// ������ ������� �� � ��� �, �������� �� ��������������� �� Queues �� ��� ����������� ��� ��� ������,
				// ����� ��� ��� ������ positions[] �� ��� ������ ��� ���� �������������.
				schoolPrefsTemp = new LinkedList[M]; // �� �������� ���� ��� "�����������" ��� ��� ��� "�������" ������.
				studentPrefs = new LinkedList[N];
				positions = new int[M];
				
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// **���� 2.2.1: ������ ���� ��� ������ ��� prefs.txt ��� ���������� "�������" ������ ��� ���� �������� ����** //	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				// ������� ����� ����� ������� ��� �� ������� ��������.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// �������� ��� "������" M*N, ������� ��� schoolPrefsTemp ��� positions.
				int extraM = 0; // ��������� ����� "�������" ������ ������ �� ����������.
				for (int i = 0; i < M; i++) {
					schools.add(i+1); // �� ID �������� ��� �� 1 ��� �� �.
					schoolPrefsTemp[i] = new LinkedList<Integer>();
					m = p.matcher(lineOfText);
					while (m.find()) { // ��� ������� ��������-�����������, �� ��������� ��� queue.
						schoolPrefsTemp[i].add(Integer.parseInt(m.group()));
					}
					// ���� format ��� prefs.txt, �� ����� �������� ���� queue �������� ��� ������ ��� �������� ������.
					positions[i] = schoolPrefsTemp[i].poll(); // ��' ����, �� ���������.
					extraM = extraM + positions[i] - 1;
					lineOfText = input.readLine();
				}
				
				// �� schoolPrefs ���� �� ��������� �� �� �������� ��� schoolPrefsTemp ��� ��� "�������" ������.
				// ������ ������� � ��������� ��� temp...
				schoolPrefs = new LinkedList[M + extraM];
				for (int i = 0; i < M; i++) {
					schoolPrefs[i] = new LinkedList<Integer>();
					schoolPrefs[i].addAll(schoolPrefsTemp[i]);
				}
				
				// ...��� ������ ������������ ��� ����� ��� �� "�������" ������.
				int counter = 0; // ��������������� ���� ��������� ��� ������ ID ��� ��� "�������" ������.
				for (int i = 0; i < M; i++) {
					if(positions[i] > 1) {
						for(int j=1;j<positions[i];j++){
							
							// �� ID ���� "�������" ������ ����� ID_virtual(position_number) = ID_of_parent_school + (number_of_Schools+1)*position_number.
							//
							// ��:
							// ID_of_parent_school = 2   \
							// number_of_Schools = 3      }  ID_virtual(1) = 2 + (3+1)*1 = 6
							// position_number = 1       /
							//
							// ���� �� ����� ���� �� ��� ���� ID_virtual%(�+1) �� ������ �� ������� ������������ - �����.
							
							schools.add((i+1) + (M+1)*j);
							schoolPrefs[M + counter] = schoolPrefs[i];
							counter++;
						}
					}
				}
				
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// **���� 2.2.2: ������ ���� ��� �������� ��� �������� ��� ��� "�������" ����������� ������� �� �� ��������** //	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				// ������� ����� ����� ������� ��� �� ������� ��������.
				lineOfText = input.readLine();
				while (lineOfText.equals("")) {
					lineOfText = input.readLine();
				}
				
				// �������� ��� "������" �*�, ������� ��� studentPrefs.
				// ��������, �������� ��� "�������" ������ �� �����������.
				// � ����� ��� ����������� ��� ������� ������ ��������� �� ������ ����������,
				// ��� ��' ���� ������������� ������ ���� ��� ������� �����. ���� �� ���������� ��������� �����.
				for (int i = 0; i < N; i++) {
					students.add(i+1);
					studentPrefs[i] = new LinkedList<Integer>();
					m = p.matcher(lineOfText);
					while (m.find()) { // ��������������� � ���� ������� �� ������������.
						int pref = Integer.parseInt(m.group());
						studentPrefs[i].add(pref);
						
						if(positions[pref-1] > 1) {
							for(int j=1;j<positions[pref-1];j++){
								// �������� �� ���� ID ��� ����������� ������������.
								studentPrefs[i].add(pref + (M+1)*j);
							}
						}
					}
					lineOfText = input.readLine();
				}
			} catch (IOException e) {
				// ��������� ���������.
				System.out.println("Error reading " + dir + "\\prefs.txt");
			} finally {
				// �������� ��� ������� ��� �����.
				input.close();
			}
		} catch (FileNotFoundException e) {
			// ��������� ��� �� prefs.txt ������.
			System.out.println("Input File Missing!" + "\nPlace it in " + dir);
			System.out.println("Program will now exit. Press any key to continue...");
			System.in.read(); // ������� ��� ��������� ��� ������.
			System.exit(0);
		}

	
////////////////////////////////////////////////////////////////////
// **���� 3: �������� ��� ���������� ��� ��� �������� ���������** //	
////////////////////////////////////////////////////////////////////
		
		// ��� ��� ��� ���� ��� ���������� � �������������.
		// ������ ��� ������� ������������� ������� �� //***
		
		// �� ������� �� ������������� ��� �� ��������� ��� args[0]=1,
		// ����� ��� ���� ��� ���������� ����� ������������ ��� ���� ������� �� � �� �.
		
		// ��� ����������� ��������� �� ����������� ��� ����������� �������� ��� ��� ���������� �����������,
		// ���� ����������� �� �������� ��� ��������. ������� ���� reference ��� ��� ��������� ���� ��� �����,
		// ������ �������� �������� ���� ������ ��� ������.
		// �� �� �������� references ���������� � ���������� Stable Matching.
		Queue<Integer> groupA= null;
		LinkedList<Integer>[] groupAPrefs= null;
		LinkedList<Integer>[] groupBPrefs = null;
		int groupAsize = 0; 
		int groupBsize = 0; 
		
		// �� �������� tags ����������� ��� format ��� output.txt.
		// ������� �� �� ��������� ��� ������, �������� �� ���������� references.
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
		//*** � �������� ������� ���������� �� �������� ����� �(1) ����� ����� ����������� ��� �������.
		
		// ������������ ��� HashMap matches �� 0 (������ ������ �������) ��� ��� �� �������.
		//*** ���������� �� �(�), ����� �� ����������� ���� ������� ��������� � �������� ������ � �����������.
		//*** � ��������������� ��� "matches" ������� �� �(1). �������� ������ ������ �(�) ��� �� ��������� ��� �����.
		for (int i=0;i<groupBPrefs[0].size();i++){
			matches.put(groupBPrefs[0].get(i), 0);
		}
		
		//��������� HashMap, ���� �� ���������� �� ����� O(1) �� ��� ����� ��� � ������ ����� ����������� ��� �� ���� - ������������ �� 0 (������ �������).
		//*** ������ �� ������������ ���� �� � �������� �� ����������������� ��� "isMatchedWith", ���� ��� �.
		//*** �������� ������ �(�).
		HashMap<Integer, Integer> isMatchedWith = new HashMap<Integer, Integer>();
		for (int i=0;i<groupAPrefs[0].size();i++){
			isMatchedWith.put(groupAPrefs[0].get(i), 0);
		}
		
		//��������� HashMap, ���� �� ��������� �� ����� O(1) ����������� ��� ������ ��� � ������. (��������������� ��� �������� ��� ����������� ������ ����.
		//*** �������� ������ �(�*�) ����� ����� �������������� � HashMaps �� � ��������.
		HashMap<Integer, Integer> groupBPrefsHash[] = new HashMap[groupBsize];
		for (int i=0;i<groupBsize;i++){
			groupBPrefsHash[i] = new HashMap<Integer, Integer>();
			int j=1;
			while(!groupBPrefs[i].isEmpty()){
				groupBPrefsHash[i].put(groupBPrefs[i].poll(),j);
				j++;
			}
		}
		
		// ������ ���������� ����������
		int aMemberA=0; // ����� � ������
		int aMemberB=0; // ����� � ������
		int aMemberA_pair=0; // ����� � ������, ����������� �� ��� � (��� ����������)
		
		// ��������� ��� ������ ��� �������� �����������.
		System.out.println("Running...");
		long timeStart= System.nanoTime();
		
		//*** � �������� ������ while �� ���������� ����� �� �������� �� ������ �, ������ �� �(�).
		//*** � ������� groupA.isEmpty() �������� ���������� �� �(1) ��������� ���� �� ������� ������ ���� ����.
		while(!groupA.isEmpty()){ // ��� �� ������ � ��� ����� �����...
			//*** � ������ groupA.poll() ��� �������� ��� ������� �� ������ ���������� �� �(1) ������, ����� ����� �������� ��� ����� ��� �����.
			aMemberA = groupA.poll(); // ���� ��� ������� ��� ������ � 
			
			//*** � �������� ������ while �� ���������� ����� �� �������� ��� ����� ����������� ���� ������ �. ������� �� ����� �(�), ��� ��� �� �������� ��� ����� groupAPrefs.
			//*** � ������� groupAPrefs[(aMemberA%(groupAsize+1))-1].isEmpty() ���������� ��� ����� �� �(1), ���� ����� ������� (���������� ���� list_head==null).
			while(!groupAPrefs[(aMemberA%(groupAsize+1))-1].isEmpty()) { // ��� �� ����������� ��� ������ ��� �� ������ � ��� ���������...
				aMemberB = groupAPrefs[(aMemberA%(groupAsize+1))-1].poll(); // ���� ��� ������� ��� �� �������� //*** �(1) - �������� ��� �����.
				
				//*** �� �������� ������� (put & get) ����������� �� �(1) - �������� ����������� ��� �������� ��� ������ ���������������.
				if( isMatchedWith.get(aMemberB) == 0 ){ // �� �� ����� aMemberB ��� ���� �������...
					matches.put(aMemberA, aMemberB); // ����� �������!
					isMatchedWith.put(aMemberB, aMemberA); // �������� ��� ��������� ��� ��������� ����.
					break;
				}
				else {
					//*** �� isMatchedWith.get(aMemberB) ���������� �� �(1) ��� ��� ���� ���� �� �� ��������.
					aMemberA_pair = isMatchedWith.get(aMemberB);
					//*** ��� ��������� � ����� ����� ���� ��� ���������� - � �������� ��� ��� �����������. ��� �� ��� ����������� ��������� ��� HashMap �� ����� �(1).
					  if( groupBPrefsHash[(aMemberB%(groupBsize+1))-1].get(aMemberA%(groupAsize+1)) < groupBPrefsHash[(aMemberB%(groupBsize+1))-1].get(aMemberA_pair%(groupAsize+1)) ){
						// �� � aMemberA ���������� ��� �� ������� ������ �� ��� aMemberA_pair...
						//*** ��� �� �������� ����������� ��� ���� �� �(1) ���� ��� ��������� ��� Hashing.
						matches.put(aMemberA, aMemberB); // ����� �������!
						matches.put(aMemberA_pair, 0); // ���� �� aMemberA_pair ��������.
						isMatchedWith.put(aMemberB, aMemberA); // �������� ��� ��������� ��� ��������� ����.
						//*** � �������� ��������� �� ���� ������� �� �(1) ����� �� �������� ����� ������������ ��� �����.
						groupA.add(aMemberA_pair); // �������� ��� ��� �������� ���� ����.
						break;
					} // ������ � ��������� �����������...	
				}
			}
			// �������� ����������� ��������� ��� �� ������.
			System.out.println(tagA + aMemberA + tagB + matches.get(aMemberA)+".");
		}
		// ���� ����������� ��� ��������� ������.
		long timeFinish= System.nanoTime();
		long totalNSTime = timeFinish-timeStart;
		System.out.println("Finished! Execution time: "+ (totalNSTime/1000000.0)+" ms\n");
		
		// �������� �������������:
		//
		// ��� ��� �� ����� while, ����������� ��� ������������ �� ������ ��� �, ����������� �� �(�*�)
		// ���� ����� �������, � ������ ����� ������� �� ���� �(�*�) �����, ��� ���� �� ����� ������� ��� ���������� ��� ������������� ����� ����� �(1).
		// �������� ������ ������ 2 * �(�*�), ������ �(�*�)!
		
///////////////////////////////////////////////////////////////////
// **���� 4: ��������� ��� ������������� ��� ������ output.txt** //	
///////////////////////////////////////////////////////////////////
		
		// ���������� ��� output.txt ��� ���� directory �� �� ����������.
		BufferedWriter output = new BufferedWriter(new FileWriter(dir + "\\output.txt"));
		
		// ������� ���������� �������� �� �� ��������� format ��� ���������.
		try {
			for (Integer key : matches.keySet()) {
				if(matches.get(key)%(groupBsize+1)==0) {
					output.write(tagA + key%(groupAsize+1) + " was not selected.");
				} else {
					output.write(tagA + key%(groupAsize+1) + tagB + matches.get(key)%(groupBsize+1)+".");
				}
				output.newLine();
			}
			// �������� �������.
			System.out.println("Output.txt created successfully.");
		} catch (IOException e) {
			// ��������� ���������.
			System.out.println("Error writing " + dir + "\\output.txt");
		} finally {
			// �������� ��� ������� ��� �����.		
			output.close();
		}
 	}
}