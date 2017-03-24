package nl.avans.android.favourites;

import org.junit.Test;

import nl.avans.android.favourites.domain.BSN;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;

/**
 * Created by Robin Schellius on 24-3-2017.
 *
 * These are tests that run on your machine's local Java Virtual Machine (JVM).
 * Use these tests to minimize execution time when your tests have no Android
 * framework dependencies or when you can mock the Android framework dependencies.
 */
public class BsnTest {

    private BSN bsn;

    /**
     * Test of het lukt om een lege BSN te maken - zou een Exception moeten geven.
     * Dit is testvariant 1: de verwachte waarde staat in de @Test-annotatie.
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void creation_error_empty_bsn_1() throws Exception {

        bsn = new BSN("");
        // Als we hier komen is het aanmaken van een lege BSN gelukt -
        // en dat zou niet moeten kunnen.
    }

    /**
     * Test of het lukt om een lege BSN te maken - zou een Exception moeten geven.
     * Dit is testvariant 2: de verwachte waarde wordt getest met jUnit Asserts en fail().
     * Het voordeel van deze methode is dat je heel precies kunt testen op wat fout gaat.
     *
     * @throws Exception
     */
    @Test
    public void creation_error_empty_bsn_2() throws Exception {

        try {
            bsn = new BSN("");
            // Als we hier komen is het aanmaken van een lege BSN gelukt -
            // en dat zou niet moeten kunnen.
            fail();
        } catch (Exception e) {
            // Als we hier komen is de test gelukt, want bij een lege BSN krijgen we een Exception.
            // We checken hier of de message van die exception is zoals we verwachten.
            assertEquals(BSN.ERR_INVALID_BSN, e.getMessage());
        }
    }

    @Test
    public void creation_error_too_short() throws Exception {

        try {
            // Een BSN bestaat uit 8 of 9 getallen - dit zijn er 7.
            bsn = new BSN("1234567");
            // Als we hier komen is het aanmaken gelukt - en dat zou niet moeten kunnen.
            fail();
        } catch (Exception e) {
            // Als we hier komen is de test gelukt, want bij een lege BSN krijgen we een Exception.
            // We checken hier of de message van die exception is zoals we verwachten.
            assertEquals(BSN.ERR_INVALID_BSN, e.getMessage());
        }
    }

    @Test
    public void creation_error_too_long() throws Exception {

        try {
            // 10 cijfers - zou een fout moeten geven.
            bsn = new BSN("1234567891");
            // Als we hier komen is het aanmaken gelukt - en dat zou niet moeten kunnen.
            fail();
        } catch (Exception e) {
            // Als we hier komen is de test gelukt, want bij een lege BSN krijgen we een Exception.
            // We checken hier of de message van die exception is zoals we verwachten.
            assertEquals(BSN.ERR_INVALID_BSN, e.getMessage());
        }
    }

    @Test
    public void creation_error_no_digits() throws Exception {

        try {
            // letters in de BSN - zou een fout moeten geven.
            bsn = new BSN("test...!");
            // Als we hier komen is het aanmaken gelukt - en dat zou niet moeten kunnen.
            fail();
        } catch (Exception e) {
            // Als we hier komen is de test gelukt, want bij een lege BSN krijgen we een Exception.
            // We checken hier of de message van die exception is zoals we verwachten.
            assertEquals(BSN.ERR_INVALID_BSN, e.getMessage());
        }
    }

    @Test
    public void creation_success() throws Exception {

        String validNr = "101681288";

        try {
            // valide BSN - zie http://www.testnummers.nl/.
            bsn = new BSN(validNr);
            assertEquals(validNr, bsn.getBsn());
        } catch (Exception e) {
            // Als we hier komen is de test mislukt.
            fail();
        }
    }

}
