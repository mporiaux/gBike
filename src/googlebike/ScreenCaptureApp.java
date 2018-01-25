package googlebike;


import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class ScreenCaptureApp extends JFrame implements ActionListener
{
    BufferedImage image=null;
    int lim=500;

    int[][][]tabChiffres= new int[10][9][6];
    Robot robot=null;
    JLabel lbValeur=new JLabel("--------------");
    JLabel zone=new JLabel();
    JButton bt=new JButton("lecture");
    JButton vplus=new JButton("^");
    JButton vmoins=new JButton("v");
    JButton hplus=new JButton("->");
    JButton hmoins=new JButton("<-");
    JTextArea vue=new JTextArea();
    JFrame f,cible;
    int depv=0,deph=0;
    public static void main(String[] arg)
    {
     ScreenCaptureApp scapp=new ScreenCaptureApp();

    }
       
    public ScreenCaptureApp(){
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
           System.out.println("***********************");
       }
       sc.close();
        }
      catch(Exception e){
          System.out.println("Erreur "+e);
      }
       
      try{
        robot = new Robot();
        }
      catch(Exception e){}
         
       f=new JFrame();
       f.setLayout(new GridLayout(3,2));
       f.add(lbValeur);
       f.add(zone);
       f.add(bt);
       f.add(vue);
       f.add(vmoins);
       f.add(vplus);
        f.add(hmoins);
       f.add(hplus);
       bt.addActionListener(this);
       vmoins.addActionListener(this);
       vplus.addActionListener(this);
       hmoins.addActionListener(this);
       hplus.addActionListener(this);
       lbValeur.setFont(new Font("TimesRoman",Font.BOLD+Font.ITALIC,20));
       
       f.pack();
       f.setVisible(true);
       cible=new JFrame();
       cible.pack();
       cible.setVisible(true);
           
    }

    public void lecture(){
     int  debx=0;
     int lx=5;
     int ly=9;
     int nch=0;
     String valeur="";
     Rectangle  posRect= cible.getBounds();
     int rectX=posRect.x+deph;
     int rectY=posRect.y+depv;
     
    // image = robot.createScreenCapture(new Rectangle(863,712,100,20));
     image = robot.createScreenCapture(new Rectangle(rectX,rectY,100,20));
     ImageIcon img = new ImageIcon(image);
     zone.setIcon(img);
     
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
 
       lbValeur.setText(valeur);
       System.out.println("===>"+valeur);
       this.toFront();
    }    
            
   
    public int getChiffre(int debx,int deby){
        int lx=5;
        int ly=9;
        int[][]tabLue=new int[ly][lx];
      //  System.out.println("_____________________________________");
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
             System.out.println(chf+"="+score+"  ");
              if(score>scoreMax){
                 
                  scoreMax=score;
                  posmax=chf;
              }
          }
          if(scoreMax<40)posmax=-1;
            
       return posmax;
    }

    public void actionPerformed(ActionEvent e) {
    
         if(e.getSource()==vmoins)depv--;
         if(e.getSource()==vplus)depv++;
         if(e.getSource()==hmoins)deph--;
         if(e.getSource()==hplus)deph++;
         this.lecture();
       
    }
}