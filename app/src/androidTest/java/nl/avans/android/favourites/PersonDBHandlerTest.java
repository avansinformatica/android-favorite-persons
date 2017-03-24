package nl.avans.android.favourites;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import nl.avans.android.favourites.data.PersonDBHandler;
import nl.avans.android.favourites.domain.Person;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * These are tests that run on a hardware device or emulator. These tests have access to
 * Instrumentation APIs, give you access to information such as the Context of the app you
 * are testing, and let you control the app under test from your test code. Use these
 * tests when writing integration and functional UI tests to automate user interaction,
 * or when your tests have Android dependencies that mock objects cannot satisfy.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PersonDBHandlerTest {

    // De database waarmee we gaan werken.
    private PersonDBHandler personDBHandler;

    // Person die we in de tests gaan gebruiken.
    private Person testPerson;

    // Een aantal voorgedefiniëerde Strings. De inhoud is eigenlijk nier relevant.
    private final String PERSON_FIRSTNAME = "FirstName";
    private final String PERSON_LASTNAME = "LastName";
    private final String PERSON_EMAIL = "test@test.com";

    @Before
    public void setUp() throws Exception {
        // Initialiseer de database.
        personDBHandler = new PersonDBHandler(InstrumentationRegistry.getTargetContext());

        // Initialiseer de testpersoon.
        testPerson = new Person();
        testPerson.setFirstName(PERSON_FIRSTNAME);
        testPerson.setLastName(PERSON_LASTNAME);
        testPerson.setEmailAddress(PERSON_EMAIL);
        testPerson.setFavorite(true);
    }

    @After
    public void tearDown() throws Exception {
        // Verwijder de testpersoon.
        if(personDBHandler != null) {
            personDBHandler.deletePerson(testPerson);
        }
    }

    @Test
    public void creation_success() throws Exception {

        // We krijgen de nieuwe ID van de person in de dtb als returnwaarde.
        // -1 geeft aan dat er een fout was.
        Long errorResult = -1L;
        Long actualResult = personDBHandler.addPerson(testPerson);

        assertNotEquals(errorResult, actualResult);
        assertThat(actualResult, greaterThan(errorResult));
    }

    @Test
    public void deletion_success() throws Exception {

        // Voeg een persoon toe, zodat we die hierna kunnen verwijderen.
        personDBHandler.addPerson(testPerson);

        int errorResult = 0, expectedResult = 1;
        int actualResult = personDBHandler.deletePerson(testPerson);

        assertNotEquals(errorResult, actualResult);
        assertEquals(actualResult, expectedResult);
        //
        // Je zou hier nog kunnen controleren dat we de persoon niet
        // meer kunnen vinden in de database, met bv. findPerson(testPerson).
        //
    }

    @Test
    public void find_and_check_attributes() throws Exception {

        // Voeg een persoon toe, zodat we die hierna kunnen verwijderen.
        Long actualResult = personDBHandler.addPerson(testPerson);
        Long errorResult = -1L;

        // Het toevoegen moet gelukt zijn
        assertNotEquals(errorResult, actualResult);

        // Vervolgens persoon opzoeken in database
        Person result = personDBHandler.getPersonByFirstName(PERSON_FIRSTNAME);
        Log.i("Test", "naam = " + result.getFirstName());
//        assertEquals(PERSON_FIRSTNAME, result.getFirstName());

    }

}
