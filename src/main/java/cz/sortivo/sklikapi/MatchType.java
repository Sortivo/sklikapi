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
    
    BROAD ("broad"),
    PHRASE ("phrase"),
    EXACT ("exact"),
    NEGATIVE_BROAD("negativeBroad"),
    NEGATIVE_PHRASE("negativePhrase"),
    NEGATIVE_EXACT("negativeExact");
    
    private String matchTypeText;

    private MatchType(String matchTypeText) {
        this.matchTypeText = matchTypeText;
    }

    public String getMatchTypeText() {
        return matchTypeText;
    }
    
    public static MatchType getMatchType(String matchTypeText){
        if (matchTypeText.equalsIgnoreCase("broad")){
            return MatchType.BROAD;
        }
        if (matchTypeText.equalsIgnoreCase("phrase")){
            return MatchType.PHRASE;
        } 
        if (matchTypeText.equalsIgnoreCase("exact")){
            return MatchType.EXACT;
        } 
        if (matchTypeText.equalsIgnoreCase("negativeBroad")){
            return MatchType.NEGATIVE_BROAD;
        } 
        if (matchTypeText.equalsIgnoreCase("negativePhrase")){
            return MatchType.NEGATIVE_PHRASE;
        } 
        if (matchTypeText.equalsIgnoreCase("negativeExact")){
            return MatchType.NEGATIVE_EXACT;
        }         
        
        return null;
    }
    
}
