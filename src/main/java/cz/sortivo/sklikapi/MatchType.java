/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.sortivo.sklikapi;

/**
 *
 * @author Jan Dufek
 */
public enum MatchType {
    
    PHRASE ("phrase"),
    EXACT ("exact"),
    BROAD ("broad");
    
    private String matchTypeText;

    private MatchType(String matchTypeText) {
        this.matchTypeText = matchTypeText;
    }

    public String getMatchTypeText() {
        return matchTypeText;
    }
    
    
    
    public static MatchType getMatchType(String matchTypeText){
        if (matchTypeText.equalsIgnoreCase("phrase")){
            return MatchType.PHRASE;
        } 
        if (matchTypeText.equalsIgnoreCase("exact")){
            return MatchType.EXACT;
        } 
        if (matchTypeText.equalsIgnoreCase("broad")){
            return MatchType.BROAD;
        } 
        return null;
    }
    
}
