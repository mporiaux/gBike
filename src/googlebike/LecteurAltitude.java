/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebike;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import javax.swing.ImageIcon;

/**
 *
 * @author Michel
 */
public class LecteurAltitude extends Thread {
    int[][][]tabChiffres= new int[10][9][6];
    Robot robot=null;
    int lim=500;
    BufferedImage image=null;
    int depv=0,deph=0;
    int rectX,rectY;
    int alt=0;
    public LecteurAltitude(){
    try{        
        robot=new Robot();
    }
    catch(Exception e){}
    File chiffres=new File("d:/chiffres2.txt");
      try{
       
       Scanner sc= new Scanner(chiffres);
       String ligne;
       int chf;
       for(chf=0;chf<=9;chf++){
           for(int lig=0;lig<9;lig++){
               ligne=sc.nextLine();
               System.out.println(ligne);
               for(int col=0;col<ligne.length();col++){
                  char cr=ligne.charAt(col);
                  if(cr=='O')tabChiffres[chf][lig][col]=1;
                  else tabChiffres[chf][lig][col]=0;
               }
               for(int col=ligne.length();col<5;col++){
                   tabChiffres[chf][lig][col]=0;
               }
           }
           
       }
       sc.close();
        }
      catch(Exception e){
          System.out.println("Erreur "+e);
      }
    
    } 
    
    public void run(){
        do{
            lecture();
            try{
                Thread.sleep(1000);
            }
            catch(Exception e){}
        }while(true);
    }
    
    public int getAltitude(){
        return alt;
    }
    
    public void lecture(){
     int  debx=0;
     int lx=5;
     int ly=9;
     int nch=0;
     String valeur="";
         
     image = robot.createScreenCapture(new Rectangle(rectX,rectY,100,20));
    
    int rgb,alpha,red,green,blue;
    int vide=0;
    do{
     boolean trouve=false;
       for(int y=0;y<ly;y++){
              rgb=image.getRGB(debx,y);
              alpha = (rgb >>24 ) & 0xFF;
              red = (rgb >>16 ) & 0xFF;
              green = (rgb >> 8 ) & 0xFF;
              blue = rgb & 0xFF;
             if(red+green+blue>=lim){
                 trouve=true;
                 break;
              }
         }
            if(trouve){
                trouve=false;
                vide=0;
                int chf=getChiffre(debx,0);
                if(chf!=-1){
                    valeur+=""+chf;
                    nch++;
                    debx+=5;
                    if(chf==4||chf==7)debx+=1;
                }
                else debx++;
         }
            else {
                debx++;
                vide++;
                if(vide>=3 && !valeur.equals(""))break;
            }
    
       }while(debx<80);
 
       try{
           alt=Integer.parseInt(valeur);
        //   System.out.println(alt);
       }
       catch (Exception e){}
       
    }    
            
   
    public int getChiffre(int debx,int deby){
        int lx=5;
        int ly=9;
        int[][]tabLue=new int[ly][lx];
        for(int y=0;y<ly;y++){
            for(int x=0;x<lx;x++){
             int rgb=image.getRGB(debx+x, deby+y);
             int alpha = (rgb >>24 ) & 0xFF;
             int red = (rgb >>16 ) & 0xFF;
             int green = (rgb >> 8 ) & 0xFF;
             int blue = rgb & 0xFF;
             if(red+green+blue>=lim)tabLue[y][x]=1;                   
             else tabLue[y][x]=0;
                
            }
         
         }
          
          int scoreMax=-1;
          int posmax=-1;
          
          for(int chf=0;chf<=9;chf++){
              int score=0;
              for(int y=0;y<ly;y++){
                 for(int x=0;x<lx;x++){
                     if (tabLue[y][x]==tabChiffres[chf][y][x])score++;
                 }
              }
              if(score>scoreMax){
                  scoreMax=score;
                  posmax=chf;
              }
          }
          if(scoreMax<40)posmax=-1;
            
       return posmax;
    }
 
}
