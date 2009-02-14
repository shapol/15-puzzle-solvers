package Puzzle;

public class PuzzleState {

private int[] stateImage;
private int spaceIndex;

public PuzzleState(int puzzleType,int[] stateImage,int spaceIndex){
    
   this.stateImage = stateImage; 
   this.spaceIndex = spaceIndex;
    
}

    public int[] getStateImage() {
        return stateImage;
    }

    public void setStateImage(int[] stateImage) {
        this.stateImage = stateImage;
    }

    public int getSpaceIndex() {
        return spaceIndex;
    }

    public void setSpaceIndex(int spaceIndex) {
        this.spaceIndex = spaceIndex;
    }

    
}
