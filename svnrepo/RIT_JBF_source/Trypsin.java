/*
 * Trypsin cuts a protein sequence at Arginine(R) or Lysine(K), except when either is
 * followed by a Proline(P).
 *
 * version 2
 */

/**
 *
 * @author Amanda Fisher
 */
import java.util.ArrayList;

public class Trypsin extends Protease {

    ArrayList<Character> buildingIons = new ArrayList<Character>();
    ArrayList<String> cutSequence = new ArrayList<String>();

    /**
     * The cut method takes an input sequence and cuts it in to different Strings
     * at points dependent on the type of Protease using the method. It uses
     * the makeIon method to turn the ArrayList of collectd characters in to
     * a String.
     *
     * @param sequence String sequence representing an amino acid chain.
     * @return ArrayList of Strings, the cut sequence.
     * @throws ProteaseException When given inappropriate input.
     */
    public ArrayList<String> cut(String sequence) throws ProteaseException {
        if (sequence.contains(" ")) {
            throw new ProteaseException("Sequence to be cut must not contain spaces.");
        } else if (sequence.matches(".*\\d.*")) {
            throw new ProteaseException("Sequence to be cut must not contain numbers.");
        } else if (sequence.matches(".*[a-z].*")) {
            throw new ProteaseException("Sequence to be cut must contain all upper case letters.");
        }

        char[] charSequence = sequence.toCharArray();
        for(int i = 0; i < charSequence.length; i++) {
            if(charSequence[i] == 'R' || charSequence[i] == 'K') {
                buildingIons.add(charSequence[i]);
                if(i < charSequence.length - 1 && charSequence[i+1] != 'P') {
                    makeIon();
                }
            } else {
                buildingIons.add(charSequence[i]);
            }
        }
        makeIon();
        return cutSequence;
    }

    /**
     * makeIon takes the characters collected by cut and turns them in to a String
     * representing an Ion's sequence.
     */
    private void makeIon() {
        Character[] characterIon = new Character[buildingIons.size()];
        characterIon = buildingIons.toArray(characterIon);
        char[] charIon = new char[characterIon.length];
        for(int j = 0; j < characterIon.length; j++) {
            charIon[j] = characterIon[j].charValue();
        }
        String ion = new String(charIon);
        cutSequence.add(ion);
        buildingIons.clear();
    }
}
