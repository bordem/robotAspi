package robot.capteur;

import robot.Direction;
import robot.Robot;
import robot.capteur.Capteur;
import sol.Sol;
import sol.typeSol;

public class CapteurBase extends Capteur{

        public CapteurBase(Robot robot){
                super(robot);
        }

        @Override
        protected void setDetect(int x, int y, Sol[][] piece){
                if(piece[x][y].getSol()== typeSol.BASE){
                        boolean last = detect;
                        detect = true;
                        nPcs.firePropertyChange("Base", last, detect);
                }
        }
}
