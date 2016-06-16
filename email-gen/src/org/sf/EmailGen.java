package org.sf;
 
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
 
/*
* Very quick and dirty email generator.
*/
public class EmailGen {
 
        public static void main(String[] args) throws ScriptException, NoSuchMethodException, IOException {
               
                // Eventually include distribution type in args?
                if(args.length < 3) {
                        System.out.println("Please enter how many people, connectedness, and the config file containing keywords.");
                        System.exit(0);
                }
               
                int numPeople = Integer.parseInt(args[0]);
                Chance chance = buildScriptChance();
               
                // Master list of people
                List<PersonNode> people = new ArrayList<PersonNode>();
               
                Random rand = new Random();
               
                PrintWriter f0 = new PrintWriter(new FileWriter("people.csv"));
               f0.println( "\"" + "Name" + "\",\"" + "Email" +  "\"");
                // Generate People
                for(int i = 0; i < numPeople; i++) {
                        PersonNode person = new PersonNode();
                        person.setName(chance.first() + " " + chance.last());
                        person.setEmail(chance.email());
                        // gen title / seniority ? - ie software dev re2... - then use that as part of the base expert rating? - TODO
                       
                        // for each keyword, gen how much of an expert each person is, distribution tbd (options?)
                        // possibility - base expert rating determined by function, gen random number +/- of that expert rating for each keyword
                        double normalDistNum = Math.abs(rand.nextGaussian() * (numPeople * Double.parseDouble(args[1])));
                        System.out.println(normalDistNum);
                        // close enuf
                        int expertLevel = (int) Math.round(normalDistNum + 1);
                        person.setBaseExpert(expertLevel);
                        f0.println(person.toString());
                        people.add(person);
                }
               
                f0.close();
               
                // Master list of keywords
                List<String> keywords = new ArrayList<String>();
               
                // Get Keywords
                try(BufferedReader br = new BufferedReader(new FileReader(args[2]))) {
                        System.out.println("Adding keywords:");
                    for(String line; (line = br.readLine()) != null; ) {
                         System.out.println("\t" + line);
                         keywords.add(line);
                    }
                }
               
                if(keywords.size() <= 0) {
                        System.out.println("No keywords entered! Exiting...");
                        System.exit(0);
                }
               
               
                f0 = new PrintWriter(new FileWriter("emailgen.csv"));
               
                f0.println( "\"" + "SenderName" +  "\",\"" + "SenderEmail" +  "\",\"" + "ReceiverName" +  "\",\"" + "ReceiverEmail" + "\",\"" + "Subject" + "\"");
                // for each keyword, for each person, from pool of people (minus person) generate 'email's from/to to add up to (close) to expert rating
       
                // Generate emails
                for(int i = 0; i < keywords.size(); i++) {
                       
                        String keyword = keywords.get(i);
                        for(int j = 0; j < people.size(); j++) {
                                // Have keyword, person, and their expert level
                                PersonNode person = people.get(j);
                                // Generate emails for that person, for the keyword
                                generateEmails(person, keyword, chance, people, f0);
                        }
                       
                }
                System.out.println("Done generating emails :D");
                f0.close();
               
        }
       
        private static void generateEmails(PersonNode person, String keyword, Chance chance, List<PersonNode> people, PrintWriter f0) {
                Random rand = new Random();
                // Essentially bias their expert level a little for each keyword.
                // Might change it so that the expert level is generated per keyword, rather than per person? TODO
                int expertLevel = person.getBaseExpert() + (rand.nextInt(person.getBaseExpert()*2) - person.getBaseExpert());
                System.out.println("Expert Level: " + expertLevel);
                double actualLevel = 0;
               
                int i = 0;
                while(actualLevel <= expertLevel && i < people.size() - 1) {
                        rand.nextInt(people.size());
                        // They might end up emailing themselves, but whatever...
                        PersonNode emailer = people.get(rand.nextInt(people.size()));
                        if(emailer.getBaseExpert() <= expertLevel) {
                                actualLevel += emailer.getBaseExpert();
                                //String csvLine = emailer.getName() + ", " + emailer.getEmail() + ", " + person.getName() + ", " + person.getEmail() + ", " + chance.sentence() + " " + keyword + " " + chance.sentence();
                                String csvLine = "\"" + emailer.getName() + "\"" + "," + "\"" + emailer.getEmail() +  "\"" + "," +  "\"" + person.getName() + "\"" +  "," +  "\"" + person.getEmail() + "\"" + "," +  "\"" + keyword +  "\"";
                                System.out.println("\t Adding email with expert level " + emailer.getBaseExpert());
                                f0.println(csvLine);
                        }
                        i++;
                }
               
                System.out.println("Actual Level: " + actualLevel);
                System.out.println("----------------------------------------------");
        }
 
        // build a chance builder
        private static Chance buildScriptChance() throws ScriptException {
                // Based upon a Github Gist post.
               
                ScriptEngineManager engineManager = new ScriptEngineManager();
                ScriptEngine engine = engineManager.getEngineByName("nashorn");
                // Create a source for random numbers, as the built-in mersene twister
                // suffers some problems
                final Random random = new Random();
                engine.put("generator", (Supplier<Object>) random::nextDouble);
 
                // Add a global object, so we can get a reference to chance
                final Object exports = engine.eval("exports = {}");
 
                String file = "chance.js";
 
                // Read chance.js from the classpath
                final InputStreamReader reader = new InputStreamReader(
                                EmailGen.class.getClassLoader().getResourceAsStream(file));
                // Execute chance.js
                engine.eval(reader);
 
                // Get a usable interface to chance.js
                final Object chance = engine.eval("new exports.Chance(function(){ return generator.get(); })");
                Invocable invocable = (Invocable) engine;
                return invocable.getInterface(chance, Chance.class);
        }
}
