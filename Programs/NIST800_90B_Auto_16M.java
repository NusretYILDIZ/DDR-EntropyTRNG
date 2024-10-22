// Automation for NIST SP 800-90B.
// This code tests TRNG data that split into 16 million bit chunks.
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

public class NIST800_90B_Auto_16M
{
        public static void main(String[] args) throws IOException, InterruptedException
        {
                String[] files = {
                        "1RO_3INV_2DDR_8bit",
                        "1RO_5INV_2DDR_8bit",
                        "1RO_7INV_2DDR_8bit",
                        "1RO_9INV_2DDR_8bit",
                        "2RO_35INV_1DDR_8bit",
                        "2RO_37INV_1DDR_8bit",
                        "2RO_39INV_1DDR_8bit",
                        "2RO_57INV_1DDR_8bit",
                        "2RO_59INV_1DDR_8bit",
                        "3RO_357INV_8bit",
                        "3RO_359INV_8bit",
                        "3RO_379INV_8bit",
                        "3RO_579INV_8bit",
                        "3DDR_8bit",
                };
                
                int[] subfile_counts = { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 8 };
                
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./NIST_SP_800_90B_All_Results.txt")));
                
                for(int file = 0; file < files.length; file++)
                {
                        for(int subfile = 0; subfile < subfile_counts[file]; subfile++)
                        {
                                String command = "./ea_non_iid -l " + subfile + ",2000000 -v ./data_to_test/" + files[file] + ".txt -o ./data_to_test/NIST_SP_800_90B_Result_Of_" + files[file] + "_Subfile" + (subfile + 1) + ".json";
                                System.out.println("\n\n" + command + "\n");
                                pw.println("\n\n" + command + "\n");
                                Process process = Runtime.getRuntime().exec(command);
                                
                                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                                String line = "";
                                while((line = br.readLine()) != null)
                                {
                                        System.out.println(line);
                                        pw.println(line);
                                }
                                
                                br.close();
                                
                                process.waitFor();
                        }
                }
                
                pw.close();
        }
}
