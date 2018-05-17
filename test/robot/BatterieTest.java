package robot;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BatterieTest {
    private Batterie bat;
    @Before
    public void setUp(){
        bat = new Batterie();
    }

    @Test
    public void getReserveTest(){
        bat.consommation_virage();
        assertEquals(98, bat.getCapaciteActuelle(),100);
        bat.consommation_normale();
        assertEquals(97, bat.getCapaciteActuelle(),98);
        bat.consommation_virage_sol();
        assertEquals(94.5, bat.getCapaciteActuelle(),97);
        bat.consommation_obstacle();
        assertEquals(93, bat.getCapaciteActuelle(),94.5);

        bat.rechargerBatterie();
        assertEquals(100, bat.getCapaciteActuelle(),93);

        bat.viderBatterie(1);
        bat.consommation_virage();
        assertEquals(0, bat.getCapaciteActuelle(),100);

        bat.viderBatterie(0.5);
        bat.consommation_normale();
        assertEquals(0, bat.getCapaciteActuelle(),100);

        bat.viderBatterie(1);
        bat.consommation_virage_sol();
        assertEquals(0, bat.getCapaciteActuelle(),100);

        bat.viderBatterie(1);
        bat.consommation_obstacle();
        assertEquals(0, bat.getCapaciteActuelle(),100);


    }
}