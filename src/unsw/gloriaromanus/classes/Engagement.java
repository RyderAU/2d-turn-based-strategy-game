package unsw.gloriaromanus.classes;

import unsw.gloriaromanus.classes.units.Unit;

public interface Engagement {
    /**
     * Conducts an engagement between two units
     * @param playerUnit
     * @param enemyUnit
     */
    public void unitEngagement(Unit playerUnit, Unit enemyUnit);
}
