// AIS31 Test Suite, modified to automate the tests.
// This code does the 100 million bit tests.

import java.io.*;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.text.DecimalFormat;
import java.lang.*;

class AIS31_Evaluator_console_linux
{
	static String[] rngDesigns = {
		"3DDR",
		"1RO-2DDR",
		"2RO-1DDR",
		"3RO",
	};
	
	static String[][] inverters = {
		{
		        "-",
		},
		{
		        "3",
		        "5",
		        "7",
		        "9",
		},
		{
		        "3-5",
		        "3-7",
		        "3-9",
		        "5-7",
		        "5-9",
		},
		{
		        "3-5-7",
		        "3-5-9",
		        "3-7-9",
		        "5-7-9",
		},
	};
	
	static String[][] fileNames = {
		{
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/3DDR",
		},
		{
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/1RO_3INV_2DDR",
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/1RO_5INV_2DDR",
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/1RO_7INV_2DDR",
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/1RO_9INV_2DDR",
		},
		{
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/2RO_35INV_1DDR",
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/2RO_37INV_1DDR",
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/2RO_39INV_1DDR",
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/2RO_57INV_1DDR",
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/2RO_59INV_1DDR",
		},
		{
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/3RO_357INV",
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/3RO_359INV",
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/3RO_379INV",
		        "./Source Codes/DDR-RAW-DATA/extracted_data/8bit/3RO_579INV",
		},
	};
	
	static int[][] subFileCounts = {
		{
		        7,
		},
		{
		        7,
		        7,
		        7,
		        7,
		},
		{
		        7,
		        7,
		        7,
		        7,
		        7,
		},
		{
		        7,
		        7,
		        7,
		        7,
		},
	};
	
    static final int elementzahl = 256*65536;
    static public byte[] BitFeldA = new byte[256*65536];
    static public byte[] BitFeldB = new byte[20000];
    static boolean geschwaetzig;
    static boolean byteformat;
    static byte testart;
    static boolean normalertest;
    static int fortschrittzahlt;
    static int bitbreite;
    static int bitzahl;
    static int c;
    static int letzterwert;
    static boolean zuwenigdaten = false;
    static boolean abbruch = false;
    static boolean skip = false;
    static boolean dateigewaehlt;
    static int letzteselement;
    static FileInputStream datei;
    static DataInputStream bitdatei;
    static String progname;
	static public String dateiname;
	static FileOutputStream logFile;
	static PrintWriter logWriter;
	
	static int[] pass_count = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	static double[] sum_of_results = { 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // 6a, 6b, 7a1, 7a2, 7b1, 7b2, 7b3, 7b4, 8
	static double[] avg_of_results = { 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // 6a, 6b, 7a1, 7a2, 7b1, 7b2, 7b3, 7b4, 8

	public static void main(String[] arguments) throws IOException
	{
		geschwaetzig = false;
		byteformat = false;
		normalertest = true;
		bitbreite = 24;
		dateigewaehlt = false;
		
		logFile = new FileOutputStream("./AIS31_100M_Bit_Test_Results.txt");
		logWriter = new PrintWriter(logFile, true);
		
		for(int rngDesign = 0; rngDesign < rngDesigns.length; rngDesign++)
		{
			for(int inverter = 0; inverter < inverters[rngDesign].length; inverter++)
			{
				PrintLog("\n\n" + rngDesigns[rngDesign] + " " + inverters[rngDesign][inverter] + " INV TRNG\n\n");

				String filename = fileNames[rngDesign][inverter];
					
				for(int subfile = 1; subfile <= subFileCounts[rngDesign][inverter]; subfile++)
				{
					for(int i = 0; i < pass_count.length; i++) pass_count[i] = 0;
				
					dateiname = filename + ".txt_8bit_subbits" + subfile + ".txt";
					testart = 0; // T0
					letzterwert = 0;
					letzteselement = -1;
					
					StartTests(rngDesign, inverter, subfile);
					PrintLog(GetRNGName(rngDesign, inverter, subfile) + " | T0: " + pass_count[0] + "/1" +
		                                                                             " | T1: " + pass_count[1] + "/257" +
		                                                                             " | T2: " + pass_count[2] + "/257" +
								                             " | T3: " + pass_count[3] + "/257" +
								                             " | T4: " + pass_count[4] + "/257" +
								                             " | T5: " + pass_count[5] + "/257" +
								                             " | T6a: " + pass_count[6] + "/1" +
		                                                                             " | T6b: " + pass_count[7] + "/1" +
								                             " | T7a: " + pass_count[8] + "/1" +
								                             " | T7b: " + pass_count[9] + "/1" +
								                             " | T8: " + pass_count[10] + "/1" +
								                             "\n");
					PrintLog("\n");
				}

                                for(int i = 0; i < sum_of_results.length; i++) avg_of_results[i] = sum_of_results[i] / (double)subFileCounts[rngDesign][inverter];
		
		                PrintLog(GetRNGName(rngDesign, inverter) + " | T0: " + pass_count[0] + "/1" +
		                                                           " | T1: " + pass_count[1] + "/257" +
		                                                           " | T2: " + pass_count[2] + "/257" +
				               	                             " | T3: " + pass_count[3] + "/257" +
						                             " | T4: " + pass_count[4] + "/257" +
						                             " | T5: " + pass_count[5] + "/257" +
				        	                             "\n | T6a: " + pass_count[6] + "/1, mean |P(1) - 0.5|: " + avg_of_results[0] +
		                                                           " | T6b: " + pass_count[7] + "/1, mean |P(01) - P(11)|: " + avg_of_results[1] +
				        	                             "\n | T7a: " + pass_count[8] + "/1, mean T[0]: " + avg_of_results[2] + ", T[1]: " + avg_of_results[3] +
						                             " | T7b: " + pass_count[9] + "/1, mean T[00]: " + avg_of_results[4] + ", T[01]: " + avg_of_results[5] + ", T[10]: " + avg_of_results[6] + ", T[11]: " + avg_of_results[7] +
				               	                             "\n | T8: " + pass_count[10] + "/1, mean: " + avg_of_results[8] +
						                             "\n");

                                for(int i = 0; i < sum_of_results.length; i++) 
                                {
                                        avg_of_results[i] = 0.0;
                                        sum_of_results[i] = 0.0;
                                }
			}
		}
		
		logWriter.close();
		logFile.close();
    }
	
	public static void PrintLog(String str, boolean printToScreen)
	{
		if(printToScreen) System.out.print(str);
		logWriter.print(str);
	}
	
	public static void PrintLog(String str)
	{
		System.out.print(str);
		logWriter.print(str);
	}
	
	public static String GetRNGName(int rngDesign, int inverter, int subfile)
	{
		return rngDesigns[rngDesign] + " " + inverters[rngDesign][inverter] + " INV TRNG - subfile " + subfile;
	}
	
	public static String GetRNGName(int rngDesign, int inverter)
	{
		return rngDesigns[rngDesign] + " " + inverters[rngDesign][inverter] + " INV TRNG";
	}
	
	public static void StartTests(int rngDesign, int inverter, int subfile) throws IOException
	{
		testart = 0; // T0
		boolean test0err = false;
		
		if(!einlesen(BitFeldA))
		{
			PrintLog(GetRNGName(rngDesign, inverter, subfile) + " - file error for test 0.\n");
			return;
		}
		
		if(!test0(c, bitbreite))
		{
			test0err = true;
		}
		pass_count[0]++;
		
		dateiname += "_rest";
		testart = 1; // T1-T5
		
		if(!einlesen(BitFeldA))
		{
			PrintLog(GetRNGName(rngDesign, inverter, subfile) + " - T0: " + pass_count[0] + "/1" + ", file error for tests 1-5.\n");
			return;
		}
		
		boolean test1err = false;
		boolean test2err = false;
		boolean test3err = false;
		boolean test4err = false;
		boolean test5err = false;
		boolean error = false;
		
		for (int durchlauf = 0; durchlauf < 257; durchlauf++)
		{
                    neuesfeldb(BitFeldA,BitFeldB,durchlauf);
                    if (test1()) {
                        //System.out.println("Test T1 bestanden.");
		        pass_count[1]++;
                    }
                    else {
                        //System.out.println("Test T1 (Monobittest);  Kriterium P1.i(ii) nicht bestanden.");
                        test1err = true;
                    }
                    
                    if (test2()) {
                        //System.out.println("Test T2 bestanden.");
		        pass_count[2]++;
                    }
                    else {
                        //System.out.println("Test T2 (Pokertest);  Kriterium P1.i(ii) nicht bestanden.");
                        test2err = true;
                    }
                    
                    if (test3()) {
                        //System.out.println("Test T3 bestanden.");
		        pass_count[3]++;
                    }
                    else {
                        //System.out.println("Test T3 (Runtest);  Kriterium P1.i(ii) nicht bestanden.");
                        test3err = true;
                    }
                    
                    if (test4()) {
                        //System.out.println("Test T4 bestanden.");
		        pass_count[4]++;
                    }
                    else {
                        //System.out.println("Test T4 (Long Runtest);  Kriterium P1.i(ii) nicht bestanden.");
                        test4err = true;
                    }
                    
                    if (test5()) {
                        //System.out.println("Test T5 bestanden.");
		        pass_count[5]++;
                    }
                    else {
                        //System.out.println("Test T5 (Autokorrelationstest);  Kriterium P1.i(ii) nicht bestanden.");
                        test5err = true;
                    }
                    if (test1err || test2err || test3err || test4err || test5err){
                        //System.out.println("Durchlauf " + (durchlauf+1) + " nicht bestanden.");
                        error = true;
                    }
                    else {
                        //System.out.println("Durchlauf " + (durchlauf+1) + " bestanden.");
                    }
                }
		
		dateiname += "_rest";
		testart = 2; // T6-T8
		
		boolean test6a_err = false;
		boolean test6b_err = false;
		boolean test7a_err = false;
		boolean test7b_err = false;
		boolean test8_err = false;
		
		if(!einlesen(BitFeldA))
		{
			PrintLog(GetRNGName(rngDesign, inverter, subfile) + " - T0: " + pass_count[0] + "/1" +
			                                                     ", T1: " + pass_count[1] + "/257" +
			                                                     ", T2: " + pass_count[2] + "/257" +
					                                     ", T3: " + pass_count[3] + "/257" +
					                                     ", T4: " + pass_count[4] + "/257" +
					                                     ", T5: " + pass_count[5] + "/257" +
					                                     ", file error for tests 6-8.\n");
			return;
		}
		
		if(!test6a())
		{
			test6a_err = true;
		}
		else
		        pass_count[6]++;

		if(!test6b())
		{
			test6b_err = true;
		}
		else
		        pass_count[7]++;

		if(!test7a())
		{
			test7a_err = true;
		}
		else
		        pass_count[8]++;

		if(!test7b())
		{
			test7b_err = true;
		}
		else
		        pass_count[9]++;

		if(!test8())
		{
			test8_err = true;
		}
		else
		        pass_count[10]++;
	}
    
    public static boolean einlesen(byte[] BitFeldA) {
        
        //String dateiname = evaluator.dateieingabe.getText();
        boolean eof = false;
        int anzahl = 0;
        boolean fehler = false;
        if (testart == 0){
            c = teilenaufrunden(48,bitbreite);
            bitzahl = bitbreite * c * 65536;
        }
        if (testart == 1){
            bitzahl = teilenaufrunden(257,(bitbreite + 1)) * ((teilenaufrunden(20000, bitbreite)) * bitbreite + (bitbreite * 20000));
        }
        if (testart == 2){
            bitzahl = 7200000;
        }
        try {
            datei = new FileInputStream(dateiname);
            if (!byteformat){
                int[] tempfeld = new int[teilenaufrunden(bitzahl,8) + 1];
                bitdatei = new DataInputStream(datei);
                //evaluator.kommentar("Kopiere BitStream-Datei in RAM ...",false,2);
                while ((!eof) && (anzahl < (teilenaufrunden(bitzahl,8)))){
                    try {
                        tempfeld[anzahl] = bitdatei.readUnsignedByte();
                    } catch (EOFException e2){
                        eof = true;
                        //evaluator.kommentar("Dateifehler: " + e2.toString(), true,2);
                        
                    }
                    anzahl++;
                }
                anzahl = anzahl * 8;
                for (int i = 0; i < BitFeldA.length; i++){
                    BitFeldA[i] = 0;
                }
                //evaluator.kommentar("Konvertiere Dateidaten in ByteStream ...",false,2);
                for (int i = 0; i < (teilenaufrunden(anzahl,8)); i++){
                    if (tempfeld[i] > 127){
                        BitFeldA[(i*8)+0] = 1;
                        tempfeld[i] = tempfeld[i] - 128;
                    }
                    if (tempfeld[i] > 63){
                        BitFeldA[(i*8)+1] = 1;
                        tempfeld[i] = tempfeld[i] - 64;
                    }
                    if (tempfeld[i] > 31){
                        BitFeldA[(i*8)+2] = 1;
                        tempfeld[i] = tempfeld[i] - 32;
                    }
                    if (tempfeld[i] > 15){
                        BitFeldA[(i*8)+3] = 1;
                        tempfeld[i] = tempfeld[i] - 16;
                    }
                    if (tempfeld[i] > 7){
                        BitFeldA[(i*8)+4] = 1;
                        tempfeld[i] = tempfeld[i] - 8;
                    }
                    if (tempfeld[i] > 3){
                        BitFeldA[(i*8)+5] = 1;
                        tempfeld[i] = tempfeld[i] - 4;
                    }
                    if (tempfeld[i] > 1){
                        BitFeldA[(i*8)+6] = 1;
                        tempfeld[i] = tempfeld[i] - 2;
                    }
                    if (tempfeld[i] > 0){
                        BitFeldA[(i*8)+7] = 1;
                    }
                }
            }
            else{
                //evaluator.kommentar("Kopiere ByteStream in RAM ...",false,2);
                anzahl = datei.read(BitFeldA, 0, bitzahl);
            }
        } catch (IOException e) {
            if (!dateigewaehlt){
                //evaluator.kommentar("Dateifehler: Keine Datei ausgew�hlt!",true,2);
                return false;
            }
            else{
                //evaluator.kommentar("Dateifehler: " + e.toString(), true,2);
                return false;
            }
        }
        try {
            int filepos = 0;
            //evaluator.setzefortschritt(21);
            //evaluator.kommentar("Schreibe Restdatei: " + dateiname + "_rest",true,2);
            FileInputStream datei = new FileInputStream(dateiname);
            FileOutputStream datei2 = new FileOutputStream(dateiname + "_rest");
            if (byteformat){
                eof = false;
                while (!eof){
                    filepos++;
                    int elementtemp;
                    elementtemp = datei.read();
                    if (elementtemp == -1){
                        eof = true;
                    } else {
                        if (filepos > teilenaufrunden(bitzahl,8)){
                            datei2.write(elementtemp);
                        }
                    }
                }
            } else {
                eof = false;
                while (!eof){
                    filepos++;
                    int elementtemp;
                    elementtemp = datei.read();
                    if (elementtemp == -1){
                        eof = true;
                    } else {
                        if (filepos > (bitzahl/8)){
                            datei2.write(elementtemp);
                        }
                    }
                }
            }
            datei.close();
            datei2.close();
        } catch (IOException e) {
            //evaluator.kommentar("Dateifehler: " + e.toString(), true,2);
        }
        if (anzahl < bitzahl) {
            //evaluator.kommentar("Dateifehler: Datei zu klein. Mindestgr��e " + bitzahl + " Bits. Gr��e: " + anzahl + " Bits.", true,2);
            return false;
        }
        //evaluator.kommentar(anzahl + " Elemente in RAM kopiert.",false,2);
        if (byteformat){
            //evaluator.kommentar("�berpr�fe Daten ...",false,2);
            for (int i = 0; i<bitzahl; i++){
                if ((BitFeldA[i] != 1) & (BitFeldA[i] != 0)){
                    //evaluator.kommentar("Element Nr. " + (i) + " ungleich \"0\" oder \"1\", da \"" + BitFeldA[i] + "\" - Datenpr�fung nicht bestanden.",true,2);
                    return false;
                }
            }
        }
        return true;
    }
    
    
    
    static void neuesfeldb(byte[] BitFeldA, byte[] BitFeldB, int durchlauf){
        int grundelement = 0;
        
        if (durchlauf % (bitbreite+1) == 0){
            //evaluator.kommentar("Teste im Block.",false,1);
            for (int i = 0; i < 20000; i++){
                BitFeldB[i] = BitFeldA[i + letzteselement + 1];
            }
            letzteselement += teilenaufrunden(20000,bitbreite)*bitbreite;
        }
        else{
            grundelement = (durchlauf % (bitbreite + 1)) + letzteselement;
            for(int i= 0; i<20000; i++) {
                BitFeldB[i] = BitFeldA[grundelement + (i * bitbreite)];
            }
            if (durchlauf % (bitbreite+1) == bitbreite) {
                letzteselement += 20000*bitbreite;
            }
            
        }
    }
    
    static int teilenaufrunden(int a, int b){
        if ((a % b) == 0){
            return (int)(a/b);
        }
        else {
            return (((int)(a/b)) + 1);
        }
    }
    
    static boolean test0(int c, int bitbreite){
        int i,j,einsen;
        boolean ok;
        long u0= (long)0;
        long u1= (long)1;
        long[] WFeld = new long[65536];
        for(i=0; i<65536; i++){
            WFeld[i]=u0;
            for(j=0; j<48; j++) WFeld[i]+=(u1<<j)*BitFeldA[(c*bitbreite)*i+j];
        }
        Arrays.sort(WFeld);
        ok=true;
        for(i=1; i<65536; i++) {
            if (WFeld[i-1]==WFeld[i]) {
                ok=false;
            }
        }
        return ok;
    }
    
    
    
    static boolean test1(){
        int i, einsen, mg=20000, ptest=0;
        boolean ok;
        einsen=0;
        for(i=0; i<mg; i++) {
            einsen+=BitFeldB[i];
        }
        if ((einsen>9654) && (einsen<10346)) ok=true;
        else ok=false;
        //System.out.println("Anzahl Einsen: " + einsen);
        //System.out.println("Zul�ssiger Bereich: [9655; 10345]");
        return ok;
    }
    
    
    
    static boolean test2(){
        int i,j,index,ptest;
        int[] Hfg = new int[16];
        boolean ok;
        double testgroesse;
        
        for(i=0; i<16; i++) Hfg[i]=0;
        for(i=0; i<5000; i++) {
            index=0;
            for(j=0; j<4; j++) index+=(1<<j)*BitFeldB[4*i+j];
            Hfg[index]++;
        }
        testgroesse=0.0;
        for(i=0; i<16; i++) testgroesse+=Hfg[i]*Hfg[i];
        testgroesse=testgroesse*(16.0/5000.0)-5000.0;
        if ((testgroesse>1.03) && (testgroesse<57.4)) ok=true;
        else ok=false;
        if (geschwaetzig){
            //System.out.println("Testgroesse = " + testgroesse);
        }
        return ok;
    }
    
    
    
    static boolean test3(){
        int i,j,run,ptest=0;
        int[] Run0Feld = new int[7];
        int[] Run1Feld = new int[7];
        boolean ok;
        int UGrenze[]={0,2267,1079,502,223,90,90};
        int OGrenze[]={0,2733,1421,748,402,223,223};
        for(i=0; i<7; i++) Run0Feld[i]=Run1Feld[i]=0;
        run=1;
        for(i=1; i<20000; i++) {
            if (BitFeldB[i-1]==BitFeldB[i]) run++;
            else {
                if (run>6) run=6;
                if (BitFeldB[i-1]==1)
                    Run1Feld[run]++;
                else Run0Feld[run]++;
                run=1;
            }
        }
        if (run>6) run=6;
        if (BitFeldB[i-1]==1)
            Run1Feld[run]++;
        else Run0Feld[run]++;
        ok=true;
        for(i=1; i<=6; i++){
            if ( (Run0Feld[i]>=UGrenze[i]) && (Run0Feld[i]<=OGrenze[i]) ){
                //System.out.println("0-Runs[" + i + "] = " + Run0Feld[i] + "; zul�ssiges Intervall: [" + UGrenze[i] + "; " + OGrenze[i] + "]");
            }
            else {
                ok=false;
                //System.out.println("FEHLER: 0-Runs[" + i + "] = " + Run0Feld[i] + "; zul�ssiges Intervall: [" + UGrenze[i] + "; " + OGrenze[i] + "]");
            }
            if ( (Run1Feld[i]>=UGrenze[i]) && (Run1Feld[i]<=OGrenze[i]) ){
                //System.out.println("1-Runs[" + i + "] = " + Run1Feld[i] + "; zul�ssiges Intervall: [" + UGrenze[i] + "; " + OGrenze[i] + "]");
            }
            else {
                ok=false;
                //System.out.println("FEHLER: 1-Runs[" + i + "] = " + Run1Feld[i] + "; zul�ssiges Intervall: [" + UGrenze[i] + "; " + OGrenze[i] + "]");
            }
        }
        return ok;
    }
    
    
    
    static boolean test4(){
        int i,run,ptest=0;
        boolean ok;
        
        run=1;ok=true;
        
        for(i=1; i<20000; i++) {
            if (BitFeldB[i-1]==BitFeldB[i]) {
                run++;
                if (run>=34) {
                    ok=false;
                    //System.out.println("Long Run aufgetreten (Wert: " + BitFeldB[i] + "). Erste Bitposition: " + (i-33) + ".");
                }
            }
            else run=1;
        }
        return ok;
    }
    
    
    
    static boolean test5(){
        int i,j,k,tau,Z_tau,max,maxindex,ptest=0;
        boolean ok;
        int[] ShiftFeld = new int[5000];
        int[] MaxKorrFeld = new int[5000];
        
        for(tau=1; tau<=5000; tau++) {
            Z_tau=0;
            for(i=0; i<5000; i++) Z_tau+=(BitFeldB[i]^BitFeldB[i+tau]);
            ShiftFeld[tau-1]=Z_tau;
        }
        
        max=0;
        
        for(tau=0; tau<5000; tau++) {
            if (Math.abs(ShiftFeld[tau]-2500)>max){
                max=Math.abs(ShiftFeld[tau]-2500);
            }
        }
        
        j=0;
        for(tau=0; tau<5000; tau++) {
            if (Math.abs(ShiftFeld[tau]-2500)==max){
                MaxKorrFeld[j]=tau;
                j++;
            }
        }
        
        //System.out.println("Maximale Z_tau-Abweichung von 2500: " + max);
        //System.out.println("Aufgetreten f�r Shifts: ");
        for(k=0; k<j; k++){
            //System.out.println("Shift: " + (MaxKorrFeld[k]+1));
        }
        tau=MaxKorrFeld[0];
        Z_tau=0;
        for(i=10000; i<15000; i++){
            Z_tau+=(BitFeldB[i]^BitFeldB[i+tau+1]);
        }
        tau++;
        //System.out.println("Nochmaliger Autokorrelationstest mit Shift: " + tau + " auf Bits 10.000 bis 14.999");
        //System.out.println("Z_" + tau + " = " + Z_tau);
        if (( Z_tau > 2326) && ( Z_tau< 2674))
            ok=true;
        else
            ok=false;
        return ok;
    }
    
    
    static boolean test6a(){
        boolean ok;
        double[] prob = new double[1];
        double absdiff = 0.0;
        int groesse=100000;
        int einsen=0;
        for(int i=0; i<groesse ; i++) einsen+=BitFeldA[i];
        prob[0]=(double) einsen / (double) groesse;
        absdiff=Math.abs(prob[0]-0.5);
        PrintLog("T6a: |P(1) - 0.5| = " + absdiff + (absdiff<0.025 ? " < 0.025" : " >= 0.025") + "\n");
        sum_of_results[0] += absdiff;
        if ((absdiff<0.025)) ok = true;  
        else ok = false;
        letzterwert = groesse;
        //System.out.println("|P(1) - 0.5| = " + absdiff);
        //System.out.println("Letztes Element: " + letzterwert);
        return ok;
    }
    
    
    static boolean test6b(){
        int groesse=100000;
        int[] counter = {0,0};
        int einsen[] = {0,0};
        int voll[] = {0,0};
        int i=0;
        double[] prob = new double[2];
        double absdiff = 0.0;
        while ((voll[0]+voll[1]<2) && ((letzterwert+2*i+1)<bitzahl)){
            if(voll[BitFeldA[letzterwert+2*i]] == 1);
            else {
                counter[BitFeldA[letzterwert+2*i]]+=1;
                if (counter[BitFeldA[letzterwert+2*i]]==groesse) voll[BitFeldA[letzterwert+2*i]]=1;
                einsen[BitFeldA[letzterwert+2*i]]+=BitFeldA[letzterwert+2*i+1];
            }
            i++;
        }  
        letzterwert += 2*i;
        if (voll[0]+voll[1]<2){
            //System.out.println("Inputdatei zu klein....Kriterium P2.i)(vii.b) konnte nicht geprueft werden.");
            zuwenigdaten = true;
            return false;
        }
        else {
            
            for(i=0; i<2; i++) prob[i]=(double)einsen[i]/groesse;
            absdiff=Math.abs(prob[0]-prob[1]);
            //System.out.println("p(01) = " + prob[0]);
            //System.out.println("p(11) = " + prob[1]);
            //System.out.println("|p_(01) - p_(11)| = " + absdiff);
            //System.out.println("Letztes Element: " + letzterwert);
            PrintLog("T6b: |P(01) - P(11)| = " + absdiff + (absdiff<0.02 ? " < 0.02" : " >= 0.02") + "\n");
            sum_of_results[1] += absdiff;
            if (absdiff<0.02) return true;  
            else return false;
        } 
    }
    
    static boolean test7a(){
        int groesse=100000;
        int hilf;
        int[] voll = {0,0,0,0};
        int[] counter = {0,0,0,0};
        int[] einsen = {0,0,0,0};
        int[] nullen = {0,0,0,0};
        double sum = 0;
        int i=0;
        while ((voll[0]+voll[1]+voll[2]+voll[3]<4) && ((letzterwert+3*i)<bitzahl)){
            hilf=2*BitFeldA[letzterwert+3*i+1]+BitFeldA[letzterwert+3*i];
            if(voll[hilf] == 1);
            else {
                counter[hilf]+=1;
                if (counter[hilf]==groesse) voll[hilf]=1;
                einsen[hilf]+=BitFeldA[letzterwert+3*i+2];
            }
            i++;
        }  
        letzterwert += 3*i;
        if (voll[0]+voll[1]+voll[2]+voll[3]<4){
            //System.out.println("Inputdatei zu klein....Kriterium P2.i)(vii.c) konnte nicht geprueft werden");
            zuwenigdaten = true;
            return false;
        }
        else{
            for(i=0; i<4; i++) nullen[i]=groesse-einsen[i];
            boolean ok = true;
            for(i=0; i<2; i++) {
                sum=((double)((nullen[2*i]-nullen[2*i+1])*(nullen[2*i]-nullen[2*i+1]))/(nullen[2*i]+nullen[2*i+1])
                + (double)((einsen[2*i]-einsen[2*i+1])*(einsen[2*i]-einsen[2*i+1]))/(einsen[2*i]+einsen[2*i+1]));
                PrintLog("T7a: Sum = " + sum + (sum>15.13 ? " > 15.13" : " <= 15.13") + "\n");
                sum_of_results[2 + i] += sum;
                if(sum>15.13) ok = false;
                //System.out.println("Testgr��e[" + i + "] = " + sum);
            }
            //System.out.println("Letztes Element: " + letzterwert);
            return ok;
        }
    }

    
    static boolean test7b(){
        boolean ok;
        int hilf;
        int[] voll = {0,0,0,0,0,0,0,0};
        int[] counter = {0,0,0,0,0,0,0,0};
        int[] einsen = {0,0,0,0,0,0,0,0};
        int[] nullen = {0,0,0,0,0,0,0,0};
        double sum = 0;
        int groesse=100000;
        int i=0;
        while ((voll[0]+voll[1]+voll[2]+voll[3]+voll[4]+voll[5]+voll[6]+voll[7]<8) && ((letzterwert+4*i)<bitzahl)){
            hilf=4*BitFeldA[letzterwert+4*i+2]+2*BitFeldA[letzterwert+4*i+1]+BitFeldA[letzterwert+4*i];
            if(voll[hilf] == 1);
            else {
                counter[hilf]+=1;
                if (counter[hilf]==groesse) voll[hilf]=1;
                einsen[hilf]+=BitFeldA[letzterwert+4*i+3];
            }
            i++;
        }  
        letzterwert += 4*i;
        if (voll[0]+voll[1]+voll[2]+voll[3]+voll[4]+voll[5]+voll[6]+voll[7]<8){
            //System.out.println("Inputdatei zu klein....Kriterium P2.i)(vii.d) konnte nicht geprueft werden");
            zuwenigdaten = true;
            ok = false;
        }
        else{
            for(i=0; i<8; i++) nullen[i]=groesse-einsen[i];
            ok=true;
            for(i=0; i<4; i++) {
                sum=(double)((nullen[2*i]-nullen[2*i+1])*(nullen[2*i]-nullen[2*i+1]))/(nullen[2*i]+nullen[2*i+1])
                + (double)((einsen[2*i]-einsen[2*i+1])*(einsen[2*i]-einsen[2*i+1]))/(einsen[2*i]+einsen[2*i+1]);
                PrintLog("T7b: Sum = " + sum + (sum>15.13 ? " > 15.13" : " <= 15.13") + "\n");
                sum_of_results[4 + i] += sum;
                if(sum>15.13) ok=false;
                //System.out.println("Testgr��e[" + i + "] = " + sum);
            }
        }
        //System.out.println("Letztes Element: " + letzterwert);
        return ok;
    }
    
    
    
    static boolean test8(){
        
        final int L = 8;
        final int Q = 2560; 
        final int K = 256000; 
        int hilf = 0;
        int counter = 0;
        int[] letzteposition = new int[256];
        for (int i = 0; i<256; i++){
            letzteposition[i] = -1;
        }
        int[] W = new int[Q+K];  
        double TG;
        double[] G = new double[K+Q+1];
        int abstand;
        
        G[1]=0.0;
        for(int i=1; i<=K+Q-1; i++) G[i+1]=G[i]+1.0/(i);
        for(int i=1; i<=K+Q; i++) G[i]/=Math.log(2.0);
        for (int i = 0; i<Q; i++){
            hilf = 0;
            for (int j = 0; j<8; j++){
                hilf+= BitFeldA[letzterwert+8*i+j]<<j;
            }
            letzteposition[hilf] = i;
        }
        TG=0.0;
        for(int i=Q; i<K+Q; i++){  
            hilf = 0;
            for (int j = 0; j<8; j++){
                hilf+= BitFeldA[letzterwert+8*i+j]<<j;
            }
            abstand = i - letzteposition[hilf];
            letzteposition[hilf] = i;
            TG+=G[abstand];
        } 
        TG/=(double)K;
        //System.out.println("Testgr��e = " + TG);
        letzterwert += 8*(2560+256000);
        //System.out.println("Letztes Element: " + letzterwert);
        PrintLog("T8: H1 = " + TG + (TG>7.976 ? " > 7.976" : " <= 7.976") + "\n");
        sum_of_results[8] += TG;
        if (TG>7.976) return true;  
        else return false;
    }
}


