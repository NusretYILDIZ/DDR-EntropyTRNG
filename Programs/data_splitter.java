// This code splits the test data into chunks that AIS31 can handle.
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

public class data_splitter
{
	//public static final int MAX_BIT_COUNT = 5220000;  // That was the number in the README of AIS31.
	//public static final int MAX_BIT_COUNT = 10000000;
	public static final int MAX_BIT_COUNT = 16000000;  // Minimum bit count to do all AIS31 tests without an error.
	
	public static void main(String[] args)
	{
		if(args.length < 1)
		{
			System.err.println("\nUSAGE: java data_splitter.java [filepath1] [filepath2] [filepath3]...\n");
			System.exit(-1);
		}
		
		for(int file = 0; file < args.length; file++)
		{
			String filepath = args[file];
			
			try
			{
				long filesize = Files.size(Paths.get(filepath + "_converted_8bit.txt"));
				long progress = 0;
				long bits_written = 0;
				int file_index = 1;
				
				System.out.println("\nFile path: " + filepath + "_converted_8bit.txt");
				System.out.println("File size: " + filesize + " bytes\n");
				
				FileInputStream fis = new FileInputStream(filepath + "_converted_8bit.txt");
				
				FileOutputStream fos_1bit = new FileOutputStream(filepath + "_1bit_subbits" + file_index + ".txt");
				FileOutputStream fos_8bit = new FileOutputStream(filepath + "_8bit_subbits" + file_index + ".txt");
					
				while(progress < filesize)
				{
                                        int bit_byte = fis.read();
					progress++;
					
					if(bit_byte == -1)
					{
						fos_1bit.close();
						fos_8bit.close();
						break;
					}
					
					byte[] bits = Arrays.copyOf(GetBitsFromByte((byte)0x9f), 8);
					fos_1bit.write(bits);
					fos_8bit.write(bit_byte);
					
					bits_written += 8;
					
					System.out.print("\rSplitting (" + progress + " bytes done, current file index is " + file_index + ", " + progress * 100 / filesize + "%)...");
					
					if(bits_written >= MAX_BIT_COUNT)
					{
						fos_1bit.close();
						fos_8bit.close();
						
						file_index++;
						bits_written = 0;
						
						fos_1bit = new FileOutputStream(filepath + "_1bit_subbits" + file_index + ".txt");
						fos_8bit = new FileOutputStream(filepath + "_8bit_subbits" + file_index + ".txt");
					}
					
					if(progress >= filesize)
					{
						fos_1bit.close();
						fos_8bit.close();
						break;
					}
				}
				
				fis.close();
				System.out.println(" Completed.");
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
		}
	}
	
	public static byte[] GetBitsFromByte(byte b)
	{
		byte[] bits = new byte[8];
		
		for(int i = 0; i < 8; i++)
		{
			if((b & 0x80) > 0) bits[i] = 1;
			else bits[i] = 0;
			
			b <<= 1;
		}
		
		return bits;
	}
	
	public static byte Create8BitValue(char high_nibble, char low_nibble)
	{
		byte val = (byte)Integer.parseInt("" + high_nibble, 16);
		val *= 16;
		val += (byte)Integer.parseInt("" + low_nibble, 16);
		
		return val;
	}
	
	public static byte[] CreateArrayFor1Bit(char high_nibble, char low_nibble)
	{
		byte[] array = new byte[8];
		
		switch(high_nibble)
		{
		case '0':
			array[0] = 0;
			array[1] = 0;
			array[2] = 0;
			array[3] = 0;
			break;
			
		case '1':
			array[0] = 0;
			array[1] = 0;
			array[2] = 0;
			array[3] = 1;
			break;
			
		case '2':
			array[0] = 0;
			array[1] = 0;
			array[2] = 1;
			array[3] = 0;
			break;
			
		case '3':
			array[0] = 0;
			array[1] = 0;
			array[2] = 1;
			array[3] = 1;
			break;
			
		case '4':
			array[0] = 0;
			array[1] = 1;
			array[2] = 0;
			array[3] = 0;
			break;
			
		case '5':
			array[0] = 0;
			array[1] = 1;
			array[2] = 0;
			array[3] = 1;
			break;
			
		case '6':
			array[0] = 0;
			array[1] = 1;
			array[2] = 1;
			array[3] = 0;
			break;
			
		case '7':
			array[0] = 0;
			array[1] = 1;
			array[2] = 1;
			array[3] = 1;
			break;
			
		case '8':
			array[0] = 1;
			array[1] = 0;
			array[2] = 0;
			array[3] = 0;
			break;
			
		case '9':
			array[0] = 1;
			array[1] = 0;
			array[2] = 0;
			array[3] = 1;
			break;
			
		case 'a':
			array[0] = 1;
			array[1] = 0;
			array[2] = 1;
			array[3] = 0;
			break;
			
		case 'b':
			array[0] = 1;
			array[1] = 0;
			array[2] = 1;
			array[3] = 1;
			break;
			
		case 'c':
			array[0] = 1;
			array[1] = 1;
			array[2] = 0;
			array[3] = 0;
			break;
			
		case 'd':
			array[0] = 1;
			array[1] = 1;
			array[2] = 0;
			array[3] = 1;
			break;
			
		case 'e':
			array[0] = 1;
			array[1] = 1;
			array[2] = 1;
			array[3] = 0;
			break;
			
		case 'f':
			array[0] = 1;
			array[1] = 1;
			array[2] = 1;
			array[3] = 1;
			break;
		}
		
		switch(low_nibble)
		{
		case '0':
			array[0 + 4] = 0;
			array[1 + 4] = 0;
			array[2 + 4] = 0;
			array[3 + 4] = 0;
			break;
			
		case '1':
			array[0 + 4] = 0;
			array[1 + 4] = 0;
			array[2 + 4] = 0;
			array[3 + 4] = 1;
			break;
			
		case '2':
			array[0 + 4] = 0;
			array[1 + 4] = 0;
			array[2 + 4] = 1;
			array[3 + 4] = 0;
			break;
			
		case '3':
			array[0 + 4] = 0;
			array[1 + 4] = 0;
			array[2 + 4] = 1;
			array[3 + 4] = 1;
			break;
			
		case '4':
			array[0 + 4] = 0;
			array[1 + 4] = 1;
			array[2 + 4] = 0;
			array[3 + 4] = 0;
			break;
			
		case '5':
			array[0 + 4] = 0;
			array[1 + 4] = 1;
			array[2 + 4] = 0;
			array[3 + 4] = 1;
			break;
			
		case '6':
			array[0 + 4] = 0;
			array[1 + 4] = 1;
			array[2 + 4] = 1;
			array[3 + 4] = 0;
			break;
			
		case '7':
			array[0 + 4] = 0;
			array[1 + 4] = 1;
			array[2 + 4] = 1;
			array[3 + 4] = 1;
			break;
			
		case '8':
			array[0 + 4] = 1;
			array[1 + 4] = 0;
			array[2 + 4] = 0;
			array[3 + 4] = 0;
			break;
			
		case '9':
			array[0 + 4] = 1;
			array[1 + 4] = 0;
			array[2 + 4] = 0;
			array[3 + 4] = 1;
			break;
			
		case 'a':
			array[0 + 4] = 1;
			array[1 + 4] = 0;
			array[2 + 4] = 1;
			array[3 + 4] = 0;
			break;
			
		case 'b':
			array[0 + 4] = 1;
			array[1 + 4] = 0;
			array[2 + 4] = 1;
			array[3 + 4] = 1;
			break;
			
		case 'c':
			array[0 + 4] = 1;
			array[1 + 4] = 1;
			array[2 + 4] = 0;
			array[3 + 4] = 0;
			break;
			
		case 'd':
			array[0 + 4] = 1;
			array[1 + 4] = 1;
			array[2 + 4] = 0;
			array[3 + 4] = 1;
			break;
			
		case 'e':
			array[0 + 4] = 1;
			array[1 + 4] = 1;
			array[2 + 4] = 1;
			array[3 + 4] = 0;
			break;
			
		case 'f':
			array[0 + 4] = 1;
			array[1 + 4] = 1;
			array[2 + 4] = 1;
			array[3 + 4] = 1;
			break;
		}
		
		return array;
	}
}
