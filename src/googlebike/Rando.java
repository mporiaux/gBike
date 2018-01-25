package googlebike;



public class Rando {
 public static void main(String args[]){
    LecteurCapteur lc= new LecteurCapteur();
    CommandeGStreet cgs =new CommandeGStreet();
    cgs.start();
    cgs.setPause(false);
     System.out.println("demarrage rando");
    do{
        lc.getData();
    /*    System.out.println("vitesse= "+lc.getVit()+"  distance = "+lc.getDist());
         if(lc.isDroite())System.out.print("droite  ");
          if(lc.isGauche())System.out.print("gauche  ");*/
          
          if(lc.isGauche()&& lc.isDroite()){
              System.out.println("Fin du pgm");
              System.exit(0);
          }
         // sc.lecture();
        // System.out.println();

         cgs.setValeur((int)lc.getVit());
         //cgs.setValeur(20);
   
         cgs.setDroite(lc.isDroite());
         cgs.setGauche(lc.isGauche());
         try{
             Thread.sleep(500);
         }
         catch(Exception e){

         }
     }
     while(true);
 }
}
