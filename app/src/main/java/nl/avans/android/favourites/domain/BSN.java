package nl.avans.android.favourites.domain;

/**
 * Class om ee Burgerservicenummer (BSN) mee te modelleren.
 * Vooral bedoeld om te laten zien hoe je kunt testen in Android.
 * Zie de bijbehorende testcases.
 *
 * @see "https://nl.wikipedia.org/wiki/Burgerservicenummer"
 *
 */
public class BSN {

    // het BSN opgeslagen als String
    private String bsn;
    public static final String ERR_INVALID_BSN = "Invalid BSN!";

    /**
     * Constructor. De BSN moet een valide waarde hebben, anders volgt een exception.
     *
     * @param bsn
     * @throws Exception
     */
    public BSN(String bsn) throws Exception {
        if(isValidBSN(bsn))
            this.bsn = bsn;
        else throw new Exception(ERR_INVALID_BSN);
    }

    public String getBsn() {
        return bsn;
    }

    /**
     * Checkt of het gegeven BSN valide is. Rrn BSN moet voldoen aan de zgn. elf-proef.
     * Based on: https://mxforum.mendix.com/questions/2162/
     *
     * @param bsn BSN to check.
     * @return true if it is structurally sound.
     */
    public boolean isValidBSN(String bsn) {
        try {
            Double.parseDouble(bsn);
        } catch (Exception e) {
            return false;
        }
        if (bsn.length() != 9) {
            return false;
        } else {
            int checksum = 0;
            for (int i = 0; i < 8; i++) {
                checksum += (Integer.parseInt(Character.toString(bsn.charAt(i))) * (9 - i));
            }
            checksum -= Integer.parseInt(Character.toString(bsn.charAt(8)));
            if (checksum % 11 != 0) {
                return false;
            }
        }
        return true;

    }
}
