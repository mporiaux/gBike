package googlebike;

import java.awt.*;
import java.awt.event.*;

public class CommandeGStreet2 extends Thread {

protected int valeur=0;
protected boolean pause=false;
protected int coeff=2000;
protected boolean gauche=false;
protected boolean droite=false;

public void setValeur(int valeur){
        this.valeur=(int)(valeur);
        System.out.println("cgs vitesse recue = "+valeur);
}

public void setPause(boolean pause){
    this.pause=pause;
}

public void setGauche(boolean gauche){
    System.out.println("cgs gauche reçue ="+gauche);
    this.gauche=gauche;
}

public void setDroite(boolean droite){
        System.out.println("cgs droite reçue ="+droite);
    this.droite=droite;
}

public void setCoeff(int coeff){
    this.coeff=coeff;
}


  public void run(){
     try
        {
           Robot robot = new Robot();
           do{
           
            if(!pause){
             
             System.out.println("cgs 2  "+gauche+" "+droite+" "+valeur);
             int keyupgd=0;  
             int keyupav=0;
             if(gauche || droite){
                 System.out.println("gauche ou droite");
                 keyupgd=(gauche)?KeyEvent.VK_LEFT:KeyEvent.VK_RIGHT;
                 robot.keyPress(keyupgd);
                 Thread.sleep(100);
                 robot.keyRelease(keyupgd);
                 Thread.sleep(150);
              }
                 
              if(valeur>0)  {  
                 System.out.println("on avance");
              keyupav=KeyEvent.VK_UP;
              // keyupav=(valeur>=20)?KeyEvent.VK_PAGE_UP:KeyEvent.VK_UP;
              // int maxi=(valeur>=20)?200:100;
              // keyupav=KeyEvent.VK_PAGE_UP;
               //int maxi=200;
               int timeSleep=valeur*150;
               robot.keyPress(keyupav);
               Thread.sleep(timeSleep);
               robot.keyRelease(keyupav);
               int ts=(timeSleep);
               if(ts<0)ts=0;
               Thread.sleep(coeff);
             }
              
            
           }
          
         }while(true);
     }
      catch(Exception e){
          System.out.println("erreur de commande GStreet"+e);
       //   e.printStackTrace();
      }
 }

}
