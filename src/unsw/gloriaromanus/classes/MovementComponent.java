package unsw.gloriaromanus.classes;

public interface MovementComponent {
    
    /**
     * Attempts to move a group of units from a selected province to a destination province
     * @param destinationProvince
     * @return returns a boolean signalling true if the attempt to move was successful and false if the attempt was unsuccessful
     */
    public boolean moveAttempt(Province destinationProvince, int movementPointsRequired);

}
