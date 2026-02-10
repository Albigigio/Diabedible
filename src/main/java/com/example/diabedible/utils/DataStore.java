package com.example.diabedible.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static <T> void saveListToFile(List<T> list, String filePath) {
        try {
            File file = new File(filePath);

            // crea cartella "data/" se manca
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            System.out.println("💾 Salvataggio dati in: " + file.getAbsolutePath());
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, list);

        } catch (IOException e) {
            System.err.println("❌ Errore salvataggio in " + filePath + ": " + e.getMessage());
            e.printStackTrace(); // IMPORTANTISSIMO per vedere l’errore vero
        }
    }

    public static <T> List<T> loadListFromFile(String filePath, TypeReference<List<T>> typeRef) {
        try {
            File file = new File(filePath);
            System.out.println("📂 Caricamento dati da: " + file.getAbsolutePath());

            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, typeRef);

        } catch (IOException e) {
            System.err.println("⚠️ Errore caricamento da " + filePath + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}