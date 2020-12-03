package com.lambdaschool.countries.controllers;

import com.lambdaschool.countries.models.Country;
import com.lambdaschool.countries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CountryController {
    @Autowired
    CountryRepository countryrepos;

    private List<Country> findCountries(List<Country> myList, CheckCountry tester)
    {
        List<Country> tempList = new ArrayList<>();

        for (Country c : myList)
        {
            if (tester.test(c))
            {
                tempList.add(c);
            }
        }
        return tempList;
    }

    // http://localhost:2019/names/all
    @GetMapping(value = "/names/all", produces = "application/json")
    public ResponseEntity<?> listAllCountries()
    {
        List<Country> myList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    // http://localhost:2019/names/start/U
    @GetMapping(value = "/names/start/{letter}", produces = "application/json")
    public ResponseEntity<?> listAllCountriesByName(@PathVariable char letter)
    {
        List<Country> myList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        List<Country> rtnList = findCountries(myList, c -> c.getName().charAt(0) == letter);

        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    // http://localhost:2019/population/total
    @GetMapping(value = "population/total", produces = "application/json")
    public ResponseEntity<?> totalPopulation()
    {
        List<Country> myList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);

        double total = 0.0;
        for (Country c : myList) {
            total = total + c.getPopulation();
        }

        System.out.println("Total Population " + total);

        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    // http://localhost:2019/population/min
    @GetMapping(value = "population/min", produces = "application/json")
    public ResponseEntity<?> minPopulation()
    {
        List<Country> myList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((c1, c2) -> (int) (c1.getPopulation() - c2.getPopulation()));
        Country rtnMin = myList.get(0);
        return new ResponseEntity<>(rtnMin, HttpStatus.OK);
    }

    // http://localhost:2019/population/max
    @GetMapping(value = "population/max", produces = "application/json")
    public ResponseEntity<?> maxPopulation()
    {
        List<Country> myList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((c1, c2) -> (int) (c2.getPopulation() - c1.getPopulation()));
        Country rtnMax = myList.get(0);
        return new ResponseEntity<>(rtnMax, HttpStatus.OK);
    }
}
