package com.example.diabedible.model;

public class Medication {
    private final String id;        // UUID
    private final String name;      // Nome farmaco
    private final String dose;      // Quantità/dose
    private boolean taken;          // ✅ se il paziente ha confermato

    public Medication(String id, String name, String dose) {
        this.id = id;
        this.name = name;
        this.dose = dose;
        this.taken = false;
    }

    public String getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }

    public String getDose() {
         return dose; 
        }

    public boolean isTaken() { 
        return taken; 
    }

    public void setTaken(boolean taken) { 
        this.taken = taken; 
    }

    public String toString() {
        return name + " - " + dose + (taken ? " ✅" : " ❌");
    }
}
