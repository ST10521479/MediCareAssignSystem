
package com.mycompany.medicareassignsystem;

// this is thrown when attempting to register a patient with a patient ID that already exists 
public class DuplicatePatientIdException extends Exception {// end
    public DuplicatePatientIdException(String message) {
        super(message);
    }
    
}// end
