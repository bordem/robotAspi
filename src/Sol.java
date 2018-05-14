public class Sol {

    private typeSol sol;
    private short epaisseurPoussiere;
    private boolean estPropre;

    public Sol(typeSol type,short epaisseur){
        this.sol=type;
        this.epaisseurPoussiere=epaisseur;
        if(epaisseur==0)
            this.estPropre=true;
        else
            this.estPropre=false;
    }
    
}
