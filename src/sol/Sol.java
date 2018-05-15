package sol;

import robot.Robot;

public class Sol {
    private String zone;
    private typeSol sol;
    private int epaisseurPoussiere;
    public Sol(String chaine){
        zone=chaine;
        toSol();
    }

     public void toSol(){
         String  nombre = zone.substring(1);
         int aspire;
         if(zone != null){
             switch (zone.charAt(0)){
                 case 'V': sol=typeSol.VIDE;
                     break;
                 case 'O' : sol = typeSol.OBSTACLE;
                    break;
                 case 'T': sol=typeSol.TAPIS;
                       epaisseurPoussiere=Integer.parseInt(nombre);
                     break;
                 case '0': sol=typeSol.NORMAL;
                       epaisseurPoussiere=Integer.parseInt(nombre);
                     break;
                 case 'B': sol=typeSol.BASE;
                     break;
             }
         }
     }

    public void afficherSol(){
        switch (sol){
            case VIDE:
                System.out.print("vide ");
                break;
            case OBSTACLE:
                System.out.print("obstacle ");
                break;
            case NORMAL:
                System.out.print("normal"+epaisseurPoussiere+ " ");
                break;
            case TAPIS:
                System.out.print("tapis"+epaisseurPoussiere +" ");
                break;
            case BASE:
                System.out.print("base ");
                break;
        }
    }

    public void setEpaisseurPoussiere(int epaisseurPoussiere) {
            this.epaisseurPoussiere -= epaisseurPoussiere;
    }

    public typeSol getSol() {
        return sol;
    }

    public int getEpaisseurPoussiere() {
        return epaisseurPoussiere;
    }
}
