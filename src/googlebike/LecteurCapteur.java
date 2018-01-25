package googlebike;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Michel
 */
import java.io.*;
import java.util.*;
public class LecteurCapteur {
  float v=0;
  int dist=0;
  String g,d;

  public LecteurCapteur(){

  }
  public void getData(){
      File fin=new File("vitesse.dta");

      String ph="0";
      try{
      FileReader in = new FileReader(fin);
      Scanner sc=new Scanner(in);
      while(sc.hasNext()) {
       ph=sc.nextLine();
      }
      in.close();
      }
      catch(Exception e){
          System.out.println("erreur de lecture capteur"+e);
          e.printStackTrace();
      }
      try{
     // System.out.println(ph);
      String res[]=ph.split(":");
      v=Float.parseFloat(res[0]);
      dist=(int)Float.parseFloat(res[1]);
      g=res[2];
      d=res[3];

      }
     catch (Exception e){
          System.out.println("erreur d'échantillonnage "+e+"="+ph);
          e.printStackTrace();
     }
       }

    public float getVit(){
        return v;
    }

 
    public long getTact(){
        return 0;
    }
    public int getDist(){
        return dist;
    }

    public boolean isGauche(){
        return g.equals("go");
    }

    public boolean isDroite(){
        return d.equals("do");
    }

}
