
package com.mycompany.medicareassignsystem;


public class Bed {// start
    
    
    private int bedNumber;
    private boolean occupied;
    private String patientId;
    
    public Bed(int bedNumber) {
        this.bedNumber = bedNumber;
        this.occupied = false;
        this.patientId = null;
    }
    public int getBedNumber() { return bedNumber; }
    public boolean isOccupied() { return occupied; }
    public String getPatientId() { return patientId; }

    public void occupy(String patientId) {
        this.occupied = true;
        this.patientId = patientId;
    }
    public void free() {
        this.occupied = false;
        this.patientId = null;
    }

    @Override
    public String toString() {
        return occupied ? ("B" + bedNumber + "[Occupied:" + patientId + "]") : ("B" + bedNumber + "[Empty]");
    }
}// end 
            
    

