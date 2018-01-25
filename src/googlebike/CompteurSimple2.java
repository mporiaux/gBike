package googlebike;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Michel
 */
import java.util.*;
import java.io.*;
public class CompteurSimple2 {
  ICLibrary cl =ICLibrary.instance;
  float dimRoue;
  float vit=0;
  float dist=0;
  int tTot=0;
  int rebond;
  long delai;
  long dtot=0;
  private static CompteurSimple2 cs=null;
private CompteurSimple2(float dimRoue,long delai,long rebond){

   this.dimRoue=dimRoue;
   this.delai=delai;

   cl.CloseDevice();
   long v = cl.OpenDevice(0);
System.out.println("valeur carte="+v);
   if(v==-1){
       System.out.println("erreur de connexion");
       System.exit(0);
   }
 System.out.println("compteur simple créé");
   rebond =50;

 }

public static CompteurSimple2 getInstance(){
    if (cs==null) return cs=new CompteurSimple2((float)2.1,2000,50);
    else return cs;
}


 public void tourne(){
     
     File f=new File("c:/bike/vitesse.dta");
    cl.ResetCounter(1);
    cl.SetCounterDebounceTime(1,rebond);
int n1,n2;
     System.out.println("compteur simple démarre");
    n1 =(int)cl.ReadCounter(1);
 do{
    FileWriter fout=null;
     n2=(int)cl.ReadCounter(1);
     float dist=(n2-n1)*dimRoue;
     dtot+=(int)dist;
     if(dtot<0)dtot=0;
     vit= dist/delai*3600 ;
   try{
      fout=new FileWriter(f);
      fout.write(vit+":"+dtot+":"+isGauche()+":"+isDroite()+"\n");
       System.out.println(vit+":"+dtot+":"+isGauche()+":"+isDroite()+"\n");
     fout.close();
     }
   catch(Exception e){System.out.println("erreur d'écriture"+e);}

   try{
      Thread.sleep(delai);
     }catch(Exception e2){}
     n1=n2;

  }while(true);

 }


  public float getV(){
        return vit;
    }
    public long getTact(){
        return (int) tTot;
    }
    public int getDist(){
        return (int)dist;
    }

public boolean isGauche(){
   return cl.ReadDigitalChannel(2);
}

public boolean isDroite(){
    return cl.ReadDigitalChannel(3);
}

public boolean isFrein(){
   return cl.ReadDigitalChannel(4);
}
 


 public static void main(String args[]){
     CompteurSimple2 cs=new CompteurSimple2((float)2.1,500,50);
     cs.tourne();
 }

}
