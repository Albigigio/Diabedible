package com.example.diabedible.utils; 
import com.fasterxml.jackson.core.type.TypeReference; 
import com.fasterxml.jackson.databind.ObjectMapper; 
import java.io.File; import java.io.IOException; 
import java.util.List; 

public class DataStore { 
    private static final ObjectMapper mapper = new ObjectMapper(); 
   
    public static <T> void saveListToFile(List<T> list, String filePath) { 
        try { mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), list); 

        } 
        catch (IOException e) { 
            System.err.println(" Errore durante il salvataggio dei dati in " + filePath + ": " + e.getMessage()); 
        } } 
        
        public static <T> List<T> loadListFromFile(String filePath, TypeReference<List<T>> typeRef) { 
            try { File file = new File(filePath); 
                if (!file.exists()) return new java.util.ArrayList<>(); 
                return mapper.readValue(file, typeRef); 
            } 
            catch (IOException e) { 
                System.err.println("⚠️ Errore durante il caricamento dei dati da " + filePath + ": " + e.getMessage()); 
                return new java.util.ArrayList<>(); 
            } 
        } 
    }