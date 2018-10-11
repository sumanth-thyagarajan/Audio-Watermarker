
package javaapplication125;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * converting r g b layers into binary string and to perform xor operation and conversion to String
 * input 3 layered image, output dna encoded String
 * addition process will be performed only on r g b not on alpha, alpha+ addition ( r, g, b )
 * extract alpha from the decrypted data, and perform subtraction process on (r2,g2,b2)
 * combine the colors and retrieve the original image
 * @author sumanth
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.xml.bind.DatatypeConverter;

public class ProjectRGB {

    /**
     * @param args the command line arguments
     */
    int ausize_1,noff_1,rseed_1;
    int asom_1[];
    File Strtoimage_;
    File embeddedaudio_1;
    File stringtoimage_1;
    File ISO_1;
    byte[] cipher_1;
    byte[] retrieved_ciphermsg_1;
    SecretKeySpec secKey_1;
    String watermarkmsg_1;
    String decryptedwatermarkmsg_1;
    String String_alpha_red_green_blue=""; //contains resultant string of argb
    int width; //width of the image
    int height; // height of the image
    int count=0;
    File input ; // for reading files
    BufferedImage image,Result_image; //reading imgae file
    
    /*Constructor for reading the image file and converting it into DNA added data*/
    public ProjectRGB(String str) throws IOException
    {
        input = new File(str); // returns object file of str to input
        this.image = ImageIO.read(input); // image reading
         try
       {
            String Binary_alpha=""; // for converting color into binary data
            String Binary_red="";
            String Binary_green="";
            String Binary_blue="";
            String Binary_alpha_full=""; // contains full binary data of the color
            String Binary_red_full="";
            String Binary_green_full="";
            String Binary_blue_full="";
            width = image.getWidth(); 
            height = image.getHeight();
            
            for(int i=0;i<height;i++)
            {
                for(int j=0;j<width;j++)
                {
                    count++;
                    int pixel; //returns color data od particular pixel
                    Color c= new Color(pixel=image.getRGB(i, j));
                    int alpha,red,blue,green; //for getting colors from range 0 to 255
                    Binary_alpha = Integer.toBinaryString(alpha=c.getAlpha()); //color is represnted as binary data
                    Binary_red = Integer.toBinaryString(red=c.getRed());
                    Binary_green = Integer.toBinaryString(green=c.getGreen());
                    Binary_blue = Integer.toBinaryString(blue=c.getBlue());
                    /*8 bit binary color convertion process*/
                    int R8bit=8-Integer.toBinaryString(red).length();
                    for(int q=0;q<R8bit;q++)
                    {
                        Binary_red="0"+Binary_red;
                    }
                    int B8bit=8-Integer.toBinaryString(blue).length();
                    for(int q=0;q<B8bit;q++)
                    {
                        Binary_blue="0"+Binary_blue;
                    }
                    int G8bit=8-Integer.toBinaryString(green).length();
                    for(int q=0;q<G8bit;q++)
                    {
                        Binary_green="0"+Binary_green;
                    }
                    int A8bit=8-Integer.toBinaryString(alpha).length();
                    for(int q=0;q<A8bit;q++)
                    {
                        Binary_alpha="0"+Binary_alpha;
                    }
                    /*holds full binary data of the colors*/
                    Binary_alpha_full+=Binary_alpha;
                    Binary_red_full+=Binary_red;
                    Binary_green_full+=Binary_green;
                    Binary_blue_full+=Binary_blue;
                }
            }
            System.out.println(count);
            /*DNA encoding process*/
            Binary_alpha_full=DNA_encoding(Binary_alpha_full);
            Binary_red_full=DNA_encoding(Binary_red_full);
            Binary_green_full=DNA_encoding(Binary_green_full);
            Binary_blue_full=DNA_encoding(Binary_blue_full);
            /*DNA addition process*/
            String a1=DNA_addition(Binary_alpha_full, Binary_red_full);
            String r1=DNA_addition(Binary_red_full, Binary_green_full);
            String g1=DNA_addition(Binary_green_full, Binary_blue_full);
            String b1=DNA_addition(g1, Binary_blue_full);
            /*Total string DNA ARGB data is at String_alpha_red_green_blue variable*/
            //System.out.println("a "+a1.length()+" r "+r1.length()+" g "+g1.length()+" b "+b1.length()+"count"+count);
            String_alpha_red_green_blue=a1+r1+g1+b1;
       }
       catch(Exception e){}
    }
    
    /*this function is used for converting dna encoded Data into binary data */
    File String_to_image(String str) throws IOException
    {
         /*str holds total string data and length/4 is done to retrieve each a r g b DNA data respectively*/
        int length=str.length()/4; 
        String a1=str.substring(0, length);
        String result=""; //holds totoal binary data of ARGB
        String r1=str.substring(length, length*2);
        String g1=str.substring(length*2, length*3);
        String b1=str.substring(length*3, length*4);
        result=a1+r1+g1+b1;
        /*convertion of DNA encoded data into binary data*/
        result=DNA_decoding(result);
        File ImageRetrieval_string = ImageRetrieval_string(result);
        return ImageRetrieval_string;
    }
    
    /*String to image retrieve is part of convertion of dna processed image into original image
      this function performs DNA subtraction operation and converts into binary data
    */
    String String_to_image_retrieve(String str) throws IOException
    {
        /*length holds the size of individual color*/
        int length=str.length()/4;
        String a1=str.substring(0, length);
        String result=""; //used for holding dna subtracted data
        String r1=str.substring(length, length*2);
        String g1=str.substring(length*2, length*3);
        String b1=str.substring(length*3, length*4);
        b1=DNA_subtraction(b1, g1);
        g1=DNA_subtraction(g1, b1);
        r1=DNA_subtraction(r1, g1);
        a1=DNA_subtraction(a1, r1);
        result=a1+r1+g1+b1;
        /*convertion of DNA encoded data into binary data*/
        result=DNA_decoding(result);
        return result;
    }
    
    /*conversion of binary data into DNA encoded data*/
    String DNA_encoding(String str)
    {
        String Replaced_string="";
        for(int i=0;i<str.length()-1;)
        {
            String str1 = "";
            str1+=Character.toString(str.charAt(i));
            str1+=Character.toString(str.charAt(i+1));
            if("00".equals(str1))
            {
                Replaced_string+="A";
            }
            else if("01".equals(str1))
            {
                Replaced_string+="C";
            }
            else if("10".equals(str1))
            {
                Replaced_string+="G";
            }
            else if("11".equals(str1))
            {
                Replaced_string+="T";
            }
            else 
            {
                Replaced_string+=str1;
            }
            if(str.length()-1%2==0)
            {
            i+=2;    
            }
            else 
            {
                if(i==str.length()-3)
                {
                 Replaced_string+=str.charAt(i+2);
                }
                i+=2;
            }
        }
        return Replaced_string;
    }
    
    /*Dna addtion is done for 2 colors where c=a+b*/
    String DNA_addition(String str1,String str2)
    {
        String New_color = "";
        for(int i=0;i<str1.length();i++)
        {
            if(str1.charAt(i)=='A')
            {
                if(str2.charAt(i)=='A')
                {
                    New_color+='A';
                }
                else if(str2.charAt(i)=='T')
                {
                    New_color+='T';
                }
                else if(str2.charAt(i)=='G')
                {
                    New_color+='G';
                }
                else if(str2.charAt(i)=='C')
                {
                    New_color+='C';
                }
            }
            else if(str1.charAt(i)=='T')
            {
                if(str2.charAt(i)=='A')
                {
                    New_color+='T';
                }
                else if(str2.charAt(i)=='T')
                {
                    New_color+='G';
                }
                else if(str2.charAt(i)=='G')
                {
                    New_color+='C';
                }
                else if(str2.charAt(i)=='C')
                {
                    New_color+='A';
                }
            }
            else if(str1.charAt(i)=='C')
            {
                if(str2.charAt(i)=='A')
                {
                    New_color+='C';
                }
                else if(str2.charAt(i)=='T')
                {
                    New_color+='A';
                }
                else if(str2.charAt(i)=='G')
                {
                    New_color+='T';
                }
                else if(str2.charAt(i)=='C')
                {
                    New_color+='G';
                }
            }
            else if(str1.charAt(i)=='G')
            {
                if(str2.charAt(i)=='A')
                {
                    New_color+='G';
                }
                else if(str2.charAt(i)=='T')
                {
                    New_color+='C';
                }
                else if(str2.charAt(i)=='G')
                {
                    New_color+='A';
                }
                else if(str2.charAt(i)=='C')
                {
                    New_color+='T';
                }
            }
            else
            {
                New_color+=Character.toString(str1.charAt(i))+Character.toString(str2.charAt(i));
            }
        }
        return New_color;
    }
    
    /*converting of DNA data back to binary data*/
    String DNA_decoding(String str)
    {
        String Replacement="";
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i)=='A')
            {
                Replacement+="00";
            }
            else if(str.charAt(i)=='T')
            {
                Replacement+="11";
            }
            else if(str.charAt(i)=='C')
            {
                Replacement+="01";
            }
            else if(str.charAt(i)=='G')
            {
                Replacement+="10";
            }
            else
            {
                Replacement+=Character.toString(str.charAt(i));
            }
        }
        return Replacement;
    }
    
    /*Dna subtraction performed*/
    String DNA_subtraction(String str1,String str2)
    {
        String Replacement="";
        for(int i=0;i<str1.length();i++)
        {
            if(str1.charAt(i)=='A')
            {
                if(str2.charAt(i)=='A')
                {
                    Replacement+="A";
                }
                else if(str2.charAt(i)=='T')
                {
                    Replacement+="C";
                }
                else if(str2.charAt(i)=='C')
                {
                    Replacement+="T";
                }
                else if(str2.charAt(i)=='G')
                {
                    Replacement+="G";
                }
            }
            else if(str1.charAt(i)=='T')
            {
                if(str2.charAt(i)=='A')
                {
                    Replacement+="T";
                }
                else if(str2.charAt(i)=='T')
                {
                    Replacement+="A";
                }
                else if(str2.charAt(i)=='C')
                {
                    Replacement+="G";
                }
                else if(str2.charAt(i)=='G')
                {
                    Replacement+="C";
                }
            }
            else if(str1.charAt(i)=='C')
            {
                if(str2.charAt(i)=='A')
                {
                    Replacement+="C";
                }
                else if(str2.charAt(i)=='T')
                {
                    Replacement+="G";
                }
                else if(str2.charAt(i)=='C')
                {
                    Replacement+="A";
                }
                else if(str2.charAt(i)=='G')
                {
                    Replacement+="T";
                }
            }
            else if(str1.charAt(i)=='G')
            {
                if(str2.charAt(i)=='A')
                {
                    Replacement+="G";
                }
                else if(str2.charAt(i)=='T')
                {
                    Replacement+="T";
                }
                else if(str2.charAt(i)=='C')
                {
                    Replacement+="C";
                }
                else if(str2.charAt(i)=='G')
                {
                    Replacement+="A";
                }
            }
            else 
            {
               // i++;
                Replacement+=Character.toString(str2.charAt(i));
            }
        }
        return Replacement;
    }
    
    /* This funcrion converts binary data into image by setting pixels with rgb value*/
    File ImageRetrieval_string(String str) throws IOException
    {
        Result_image=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // creating image of h and w
        int leng=str.length()/4; // Length of particular color length/4 means there are 4 colors of leng/4 
        int r = leng, g = 2*leng,b = 3*leng;
        /*Retrival of ARGB values from binary data, length /8 is used because length is in binary format */
        int Alpha_value[]= new int[count]; 
        int Red_value[]= new int[count];
        int Green_value[]= new int[count];
        int Blue_value[]= new int[count]; 
        //int c=0;
        for(int i=0,j=0;i<leng;i+=8,j++)
        {
            String alpha = str.substring(i,i+8);
            String red = str.substring(r+i,r+i+8);
            String green = str.substring(g+i,g+i+8);
            String blue = str.substring(b+i,b+i+8);
         /*retrieval of argb values and setting it into bytte data values array*/
            int alpha_Decimal_value=Integer.parseInt(alpha,2);
            Alpha_value[j]=alpha_Decimal_value;
            int red_Decimal_value=Integer.parseInt(red,2);
            Red_value[j]=red_Decimal_value;
            int green_Decimal_value=Integer.parseInt(green,2);
            Green_value[j]=green_Decimal_value;
            int blue_Decimal_value=Integer.parseInt(blue,2);
            Blue_value[j]=blue_Decimal_value;
        }
        int i=0;
        //height=image.getHeight();
        System.out.println("height"+height);
        //width=image.getWidth();
        System.out.println("Width "+width);
        for(int h=0;h<height;h++)
        {
            for(int w=0;w<width;w++)
            {
                //System.out.println((i+1)+" A "+Alpha_value[i]+" R "+Red_value[i]+" G "+Green_value[i]+" B "+Blue_value[i]);
        
                int pixel=((Alpha_value[i])<<24) | ((Red_value[i])<<16) | ((Green_value[i])<<8) | (Blue_value[i]);
                i++;
                /*used for setting argb values to DNA processe image*/
                Result_image.setRGB(h, w, pixel);
            }
        }
        File f=null;
        try
        {
            /*writing dna processed image into file named DNA_processed_image.png*/
            f = new File("/home/sumanth/ProjectOutputs/DNA_processed_image.png");
            f.createNewFile();
            ImageIO.write(Result_image,"png", f);
        }
        catch(Exception e){}
        return f;
    }
    
    /*Image retieval string Original converts the DNA processed imgae back to original image by calling
      String to imgge to retrieve original for binary conversion
    */
    File ImageRetrieval_string_Original(String str) throws IOException
    {
        input = new File(str);
        this.image = ImageIO.read(input);
        /*String to convert values into binary data*/
        String Binary_alpha="";
        String Binary_red="";
        String Binary_green="";
        String Binary_blue="";
        /*Holds the whole binary 8bit data of the argb values*/
        String Binary_alpha_full="";
        String Binary_red_full="";
        String Binary_green_full="";
        String Binary_blue_full="";
        /*height and width of the image*/
        width = image.getWidth();
        height = image.getHeight();
        /*retrieveing ARGB 8 bit values*/
        for(int i=0;i<height;i++)
        {
            for(int j=0;j<width;j++)
            {
                count++;
                int pixel;
                /*color is used to get a r g b values*/
                Color c= new Color(pixel=image.getRGB(i, j));
                int alpha,red,blue,green;
                Binary_alpha = Integer.toBinaryString(alpha=c.getAlpha());
                Binary_red = Integer.toBinaryString(red=c.getRed());
                Binary_green = Integer.toBinaryString(green=c.getGreen());
                Binary_blue = Integer.toBinaryString(blue=c.getBlue());
                /*8bit binary data conversion*/
                int R8bit=8-Integer.toBinaryString(red).length();
                for(int q=0;q<R8bit;q++)
                {
                    Binary_red="0"+Binary_red;
                }
                int B8bit=8-Integer.toBinaryString(blue).length();
                for(int q=0;q<B8bit;q++)
                {
                    Binary_blue="0"+Binary_blue;
                }
                int G8bit=8-Integer.toBinaryString(green).length();
                for(int q=0;q<G8bit;q++)
                {
                    Binary_green="0"+Binary_green;
                }
                int A8bit=8-Integer.toBinaryString(alpha).length();
                for(int q=0;q<A8bit;q++)
                {
                    Binary_alpha="0"+Binary_alpha;
                }
                Binary_alpha_full+=Binary_alpha;
                Binary_red_full+=Binary_red;
                Binary_green_full+=Binary_green;
                Binary_blue_full+=Binary_blue;
            }
        }
        /*DNA encoding process*/
        Binary_alpha_full=DNA_encoding(Binary_alpha_full);
        Binary_red_full=DNA_encoding(Binary_red_full);
        Binary_green_full=DNA_encoding(Binary_green_full);
        Binary_blue_full=DNA_encoding(Binary_blue_full);
        /*full DNA data*/
        String st=Binary_alpha_full+Binary_red_full+Binary_green_full+Binary_blue_full; 
        /*calls function to perform DNA subtaction process*/
        str=String_to_image_retrieve(st);
        
        Result_image=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int leng=str.length()/4; //length of the binary data/4 for finding the size of each color
        int r = leng, g = 2*leng,b = 3*leng;
        /*array of a r g b retrieved values*/
        int Alpha_value[]=new int[count]; //leng/8
        int Red_value[]=new int[count];
        int Green_value[]=new int[count];
        int Blue_value[]=new int[count]; 
        for(int i=0,j=0;i<leng;i+=8,j++)
        {
            String alpha = str.substring(i,i+8);
            String red = str.substring(r+i,r+i+8);
            String green = str.substring(g+i,g+i+8);
            String blue = str.substring(b+i,b+i+8);
            int alpha_Decimal_value=Integer.parseInt(alpha,2);
            Alpha_value[j]=alpha_Decimal_value;
            int red_Decimal_value=Integer.parseInt(red,2);
            Red_value[j]=red_Decimal_value;
            int green_Decimal_value=Integer.parseInt(green,2);
            Green_value[j]=green_Decimal_value;
            int blue_Decimal_value=Integer.parseInt(blue,2);
            Blue_value[j]=blue_Decimal_value;
        }
        
        int i=0;
        //height=image.getHeight();
        System.out.println("height"+height);
        //width=image.getWidth();
        System.out.println("Width "+width);
        /*setting pixel color for the retrieved image and creating file for retrieve data from subtration process*/
        for(int h=0;h<height;h++)
        {
            for(int w=0;w<width;w++)
            {
                int pixel=((Alpha_value[i])<<24) | ((Red_value[i])<<16) | ((Green_value[i])<<8) | (Blue_value[i]);
                i++;
                Result_image.setRGB(h, w, pixel);
            }
        }
        File f=null;
        try
        {
            f = new File("/home/sumanth/ProjectOutputs/Original_Retrieved_from_DNA_subtraction.png");
            f.createNewFile();
            ImageIO.write(Result_image,"png", f);
        }
        catch(Exception e){}
        return f;
        
    }
    
    public static String imagetostring(String fp) throws Exception 
    {
        String sb=null;
        File f = new File(fp).getCanonicalFile();
        FileInputStream imgipt=new FileInputStream(fp);
        byte imgs[]=new byte[(int)f.length()];
        System.out.println("testing size"+imgs.length+" "+imgs.length*8);
        imgipt.read(imgs);
        sb=Base64.getEncoder().encodeToString(imgs);
        return sb;
    }

    public static SecretKey getSecretEncryptionKey() throws Exception
    {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
    }
    
    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception
    {
	// AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }
    
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception
    {
	// AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }
    
    private static String  bytesToHex(byte[] hash)
    {
        return DatatypeConverter.printHexBinary(hash);
    }
     
    public static File stringtoimage(String decimage,String ar) throws Exception
    {
        byte[] imgby=Base64.getDecoder().decode(decimage);
        FileOutputStream ffo=new FileOutputStream(ar);
        ffo.write(imgby);
        File fs=new File(ar).getCanonicalFile();
        if(fs.exists())
        {
            System.out.println("image recovered check the file at"+ar);
        }
        return fs;
    }
     
    public static byte[] aesencrypt(String watermarkmsg,SecretKey secKey) throws Exception
    {
        byte[] cipherText = encryptText(watermarkmsg, secKey);
        return cipherText;
    }
     
    public static File lsbembed(byte d[],String audiofilepath,String embedded_audiofilepath,int guwa[],int ss) throws FileNotFoundException, IOException 
    {
        InputStream in;
        OutputStream out;
        in = new FileInputStream(new File(audiofilepath));
        //AudioStream audio = new AudioStream(in);
       
        ByteArrayOutputStream Bout = new ByteArrayOutputStream();
        ByteArrayOutputStream Bkout = new ByteArrayOutputStream();
        int read;
        byte[] buff = new byte[1024];
        while ((read = in.read(buff)) > 0)
        {
            Bout.write(buff, 0, read);
        }
        Bout.flush();
        byte[] audioBytes = Bout.toByteArray();
        byte[] abd = Bkout.toByteArray();
        System.out.println(" Range"+audioBytes.length+"  "+"ultra range"+new File(audiofilepath).length());
        byte bisc[]=d;
        int offset=ss;
        System.out.println("<>"+guwa[0]);
        int cdr=0;
        File embeddedaudio = null;
        int ok=0;          
        System.out.println(bisc.length+"string s[0] bit format: "+bisc[0]+" "+Integer.toBinaryString(bisc[0]));
        byte sk;
        if(bisc.length+offset>audioBytes.length/2) 
        { 
            System.out.println("secret text too long to hide");
            cdr++; 
        }
        else if(cdr==0)  
        {
            for(int ip=0;ip<bisc.length;++ip)
            {
                int add=bisc[ip];
                for(int gh=7;gh>=0;--gh,ok=ok+1)
                {
                    int b=(add >>> gh) & 1;
                    audioBytes[guwa[ok]]=(byte)((audioBytes[guwa[ok]] & 0XFE)|b);
                    // System.out.println("embedding in "+ok+"th position");
                }
            }
            embeddedaudio=new File(embedded_audiofilepath).getCanonicalFile();}
            try
            { 
                    if(embeddedaudio==null)
                    { 
                        System.out.println("embedding done sucessfully and embedded audio is created at "+embeddedaudio.getCanonicalPath());
                        throw new Exception("offset size large da");
                    }
                    else
                    {
                            FileOutputStream f51 = new FileOutputStream(embeddedaudio);
                            f51.write(audioBytes);
                            System.out.println();
                    }
            }
            catch(Exception e)
            {
                System.out.println("file not created because of "+e);
                System.exit(0);
            }
       return embeddedaudio;
    }
    
    public static byte[] lsbrecovery(String fr,int strlength,int disp[],int ds) throws Exception
    {
        InputStream in;
        OutputStream out;
        in = new FileInputStream(new File(fr));
        //AudioStream audio = new AudioStream(in);
       
        ByteArrayOutputStream Bout = new ByteArrayOutputStream();
        ByteArrayOutputStream Bkout = new ByteArrayOutputStream();
        int read;
        byte[] buff = new byte[1024];
        while ((read = in.read(buff)) > 0)
        {
            Bout.write(buff, 0, read);
        }
        Bout.flush();
        byte[] audioBytes = Bout.toByteArray();
        System.out.println(" Range"+audioBytes.length+"  ");
        // int offset =ds;
        //        int ar[]=new int[68000];
        System.out.println("[][][]"+disp[0]);
        int cdr=0,ok=0; 
        int bigbp=disp[disp.length-1];
        byte[] result = null;
        if(bigbp>audioBytes.length)
        {
            System.out.println("secret text too long to hide");
            cdr++;
        }
        else if(cdr==0)
        {
            result = new byte[strlength];   //byte length is the length of the message in bytes
            for(int b=0; b<result.length; ++b )
            {
                for(int i=0; i<8; ++i,ok=ok+1)
                {
                    result[b] = (byte)((result[b] << 1) | (audioBytes[disp[ok]] & 1));
                }
            }
        System.out.println(ok+">>"+disp[ok-1]+" "+result.length);
        }   
    return result;
    }
    
   public static SecretKeySpec genKey(String d) throws Exception
   {
        byte[] keyBytes=d.getBytes();
        SecretKeySpec key=new SecretKeySpec(keyBytes,"AES");
        return key;
    }
        
    public static String aesdecrypt(byte[] retrieved_ciphermsg,SecretKey secKey) throws Exception
    {
        String decryptedwatermarkmsg = decryptText(retrieved_ciphermsg, secKey);
        return decryptedwatermarkmsg;
    }

    public static int[] randomarray(int noffset,int iseed,int ausize1)
    {
        int ny=noffset;
        ArrayList<Integer> numbers=new ArrayList();
        for(int go=ny,ol=0;go<ausize1;go++)
        {
            //ark[ol]=go;
            numbers.add(go);
        }
        int[] gta=new int[numbers.size()];
        //int[] gta2=new int[cipher.length*8];
        Random randomGenerator = new Random(iseed);
        Collections.shuffle(numbers, randomGenerator); 
        //      System.out.println(";;"+numbers.get(0)+".."+numbers.get(1)+" "+numbers.size());
        for(int i=0;i<numbers.size();i++)
        {
            gta[i]=numbers.get(i);
        }
        System.out.println("welcome>"+gta.length+" "+gta[0]+" "+numbers.size());
        return gta;
    }
     
    public ProjectRGB(){}
    
    public static void main(String[] args) throws IOException, Exception 
    {
        ProjectRGB newrgb=new ProjectRGB(args[0]);
        newrgb.Strtoimage_ = null;
        newrgb.watermarkmsg_1= null;
        newrgb.Strtoimage_ = newrgb.String_to_image(newrgb.String_alpha_red_green_blue);
        System.out.println("enter the image file path in the format eg.,(DRIVE:\\FOLDER\\FILENAME.FORMAT) as command line arguments");
        System.out.println("calling imagetostring function to create watermark msg ");
        if(newrgb.Strtoimage_==null)
        {
            newrgb.watermarkmsg_1=imagetostring(args[0]);
            System.out.println("hy"+newrgb.watermarkmsg_1.length());
        }
        else if(newrgb.Strtoimage_!=null)
        {
            System.out.println("hooray");
            newrgb.watermarkmsg_1=imagetostring(newrgb.Strtoimage_.getAbsolutePath());
        }
        System.out.println(newrgb.watermarkmsg_1.length());
        System.out.println("Generating 128 bit secret key for AES");
        newrgb.secKey_1 =genKey("universityhastra");
        System.out.println(newrgb.secKey_1.toString());
        System.out.println("calling aes encrypt function to encrypt watermark message");    
        newrgb.cipher_1=aesencrypt(newrgb.watermarkmsg_1,newrgb.secKey_1);;
        System.out.println("ioio...>>"+newrgb.cipher_1.length*8);
        newrgb.ausize_1=(int) new File(args[1]).getAbsoluteFile().length();
        newrgb.noff_1=197;
        newrgb.rseed_1=12;  
        System.out.println("  ;';';';"+newrgb.ausize_1+"creating random array with offest"+newrgb.noff_1+" and seed "+newrgb.rseed_1+"provided");
        newrgb.asom_1=randomarray(newrgb.noff_1,newrgb.rseed_1,newrgb.ausize_1);
        System.out.println("calling lsb embedding in audio function");
        newrgb.embeddedaudio_1= lsbembed(newrgb.cipher_1,args[1],args[2],newrgb.asom_1,newrgb.noff_1);
        System.out.println("calling lsb recovery in audio function to retrieve ciphered watermarkmsg");
        newrgb.retrieved_ciphermsg_1=lsbrecovery(newrgb.embeddedaudio_1.getAbsolutePath(),newrgb.cipher_1.length,newrgb.asom_1,newrgb.noff_1);
        String pt1=args[1];
        MessageDigest md=MessageDigest.getInstance("MD5");
        md.update(Files.readAllBytes(Paths.get(pt1)));
        byte[] dg1=md.digest();
        System.out.println(Arrays.toString(dg1)+" "+md.digest()+dg1);
        String pt2=newrgb.embeddedaudio_1.getAbsolutePath();
        MessageDigest md1=MessageDigest.getInstance("MD5");
        md1.update(Files.readAllBytes(Paths.get(pt2)));
        byte[] dg2=md1.digest();
        System.out.println(Arrays.toString(dg2)+" "+md1.digest()+dg2);
        if(Arrays.equals(dg1, dg2))
        {  
            System.out.println("yes it is similar");
        }
        else
        {
            System.out.println("not matching");
        }
        System.out.println("calling aes decrypt function to decrypt watermark message");
        newrgb.decryptedwatermarkmsg_1 = aesdecrypt(newrgb.retrieved_ciphermsg_1,newrgb.secKey_1);
        System.out.println("calling stringtoimage function to recover secret image ");
        newrgb.stringtoimage_1 =null;
        newrgb.stringtoimage_1 = stringtoimage(newrgb.decryptedwatermarkmsg_1,args[3]);
        ProjectRGB rgb=new ProjectRGB();
        newrgb.ISO_1 = rgb.ImageRetrieval_string_Original(newrgb.stringtoimage_1 .getAbsolutePath());
        System.out.println("Final image after all process is available at"+newrgb.ISO_1.getAbsolutePath());
    
    }
}
