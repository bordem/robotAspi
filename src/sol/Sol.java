package sol;

public class Sol {

    private typeSol sol;
    private int epaisseurPoussiere;

    public Sol(typeSol type,int epaisseur){
        this.sol=type;
        this.epaisseurPoussiere=epaisseur;
    }

     public void toSol(String zone){
         String  nombre = zone.substring(1,2);
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
             }
         }
     }

    public typeSol getSol() {
        return sol;
    }

    public int getEpaisseurPoussiere() {
        return epaisseurPoussiere;
    }
}
