package com.LPSWorkflow.main;

import com.LPSWorkflow.model.message.MessageType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iora on 21/07/14.
 */
public class ActionReader {
    // Look up the AEC class : Action-Effect-Conditions
    // This list is esentially our database
    private List<AEC> aecs = new ArrayList<>();

    // opens and reads the lines
    public List<AEC> openFile(String filePath) {
        List<String> lines = new ArrayList<>();
        String line;
        BufferedReader br;

        try {
            if (filePath != null) {
                br = new BufferedReader(new FileReader(filePath));
            } else {
                return null;
            }

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Cannot open file!");
        }
        return parseFileContent(lines);
    }

    // parses the list of string lines
    private List<AEC> parseFileContent(List<String> lines) {

        List<AEC> aecs = new ArrayList<>();

        for (String line : lines) {
            // usually a line contains something like this:
            // action() & cond1() & cond2() -> effect()

            // spilts into split[0] = actions & conds
            //             split[1] = effect
            String[] split = line.split(" -> ");

            String[] actionAndCond = split[0].split(" & ");

            String effect = split[1];

            String action = null;
            List<String> conditions = new ArrayList<>();
            for (String ac : actionAndCond) {
                // gets the actionsAndCond : atm only do() and orders() are actions
                // can be modified to support more actions
                if (ac.startsWith("kill") || ac.startsWith("do") || ac.startsWith("orders")) action = ac;
                else conditions.add(ac);
            }

            aecs.add(new AEC(action, effect, conditions));

        }
        return aecs;
    }

    public ActionReader(String filepath) {
        this.aecs = openFile(filepath);
    }

    public void printAEC() {
        for (AEC aec : aecs) {
            System.out.print("Action: " + aec.getAction() + " ");
            System.out.print("Effect: " + aec.getEffect() + " ");
            System.out.print("Conditions: ");
            for (String condition : aec.getConditions()) {
                System.out.print(condition + " ");
            }
            System.out.println();
        }
    }

    public List<AEC> getAEC() {
        return aecs;
    }
}
