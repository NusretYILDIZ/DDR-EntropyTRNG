// This code converts contents of specified files from human readable hex format to binary format (8 bits in 1 byte).
/*
        MIT License

        Copyright (c) 2024 Nusret YILDIZ

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in all
        copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
        SOFTWARE.
*/

import java.io.*;
import java.util.*;
import java.nio.file.*;

public class data_converter
{
	public static void main(String[] arguments)
	{
		if(arguments.length < 1)
		{
			System.err.println("\nUSAGE: java data_converter.java [filepath1] [filepath2] [filepath3]...\n");
			System.exit(-1);
		}
		
		for(int file = 0; file < arguments.length; file++)
		{
			String filepath = arguments[file];
			
			try
			{
				FileInputStream fis = new FileInputStream(filepath);
				FileOutputStream fos_1bit = new FileOutputStream(filepath + "_converted_1bit.txt");
				FileOutputStream fos_8bit = new FileOutputStream(filepath + "_converted_8bit.txt");
				
				int h_byte = 0, l_byte = 0;
				long progress = 0;
				long filesize = Files.size(Paths.get(filepath));
				
				System.out.println("\nFile path: " + filepath);
				System.out.println("File size: " + filesize + " bytes\n");
				
				while((h_byte = fis.read()) != -1 && (l_byte = fis.read()) != -1)
				{
					char h_char = (char)h_byte;
					char l_char = (char)l_byte;
					
					if(h_char == '\n' || h_char == '\r')
					{
					        h_char = l_char;
					        l_byte = fis.read();
					        
					        if(l_byte == -1) break;
					        l_char = (char)l_byte;
					}
					if(l_char == '\n' || l_char == '\r')
					{
					        l_byte = fis.read();
					        
					        if(l_byte == -1) break;
					        l_char = (char)l_byte;
					}
					
					fos_1bit.write(CreateStringFor1Bit(h_char, l_char).getBytes());
					fos_8bit.write(Create8BitValue(h_char, l_char));
					
					progress += 2;
					System.out.print("\rConverting (" + progress + " bytes done, " + (long)((float)progress * 100.0f / (float)filesize) + "%)...");
				}
				
				fis.close();
				fos_1bit.close();
				fos_8bit.close();
				
				System.out.println(" Completed.\n");
			}
			catch(IOException e)
			{
				System.err.println(e);
			}
		}
	}
	
	public static byte Create8BitValue(char high_byte, char low_byte)
	{
		byte val = (byte)Integer.parseInt("" + high_byte, 16);
		val *= 16;
		val += (byte)Integer.parseInt("" + low_byte, 16);
		
		return val;
	}
	
	public static String CreateStringFor1Bit(char high_byte, char low_byte)
	{
		String str = new String();
		
		switch(high_byte)
		{
		case '0':
			str = "0000";
			break;
			
		case '1':
			str = "0001";
			break;
			
		case '2':
			str = "0010";
			break;
			
		case '3':
			str = "0011";
			break;
			
		case '4':
			str = "0100";
			break;
			
		case '5':
			str = "0101";
			break;
			
		case '6':
			str = "0110";
			break;
			
		case '7':
			str = "0111";
			break;
			
		case '8':
			str = "1000";
			break;
			
		case '9':
			str = "1001";
			break;
			
		case 'a':
			str = "1010";
			break;
			
		case 'b':
			str = "1011";
			break;
			
		case 'c':
			str = "1100";
			break;
			
		case 'd':
			str = "1101";
			break;
			
		case 'e':
			str = "1110";
			break;
			
		case 'f':
			str = "1111";
			break;
		}
		
		switch(low_byte)
		{
		case '0':
			str += "0000";
			break;
			
		case '1':
			str += "0001";
			break;
			
		case '2':
			str += "0010";
			break;
			
		case '3':
			str += "0011";
			break;
			
		case '4':
			str += "0100";
			break;
			
		case '5':
			str += "0101";
			break;
			
		case '6':
			str += "0110";
			break;
			
		case '7':
			str += "0111";
			break;
			
		case '8':
			str += "1000";
			break;
			
		case '9':
			str += "1001";
			break;
			
		case 'a':
			str += "1010";
			break;
			
		case 'b':
			str += "1011";
			break;
			
		case 'c':
			str += "1100";
			break;
			
		case 'd':
			str += "1101";
			break;
			
		case 'e':
			str += "1110";
			break;
			
		case 'f':
			str += "1111";
			break;
		}
		
		return str;
	}
}
