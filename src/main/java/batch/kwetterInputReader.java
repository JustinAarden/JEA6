/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batch;

import javax.batch.api.chunk.ItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

@Dependent//@Dependent //http://docs.oracle.com/javaee/6/api/javax/enterprise/context/Dependent.html
@Named//("kwetterInputReader")
public class kwetterInputReader implements ItemReader {

    private BufferedReader reader;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        reader = new BufferedReader(
                new InputStreamReader(
                        this
                                .getClass()
                                .getClassLoader()
                                .getResourceAsStream("META-INF/input.json"))); //Get the JSON file out of Resources/META-INF
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public String readItem() {
        try {
            return reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
