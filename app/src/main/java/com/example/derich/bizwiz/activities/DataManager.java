package com.example.derich.bizwiz.activities;

import com.example.derich.bizwiz.clients.Person;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DataManager ourInstance = null;

    private List<Person> mPersonList = new ArrayList<>();


    public static DataManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new DataManager();
//            ourInstance.initializeCourses();
//            ourInstance.initializeExampleNotes();
        }
        return ourInstance;
    }

    public List<Person> getPerson() {return mPersonList;}
}
