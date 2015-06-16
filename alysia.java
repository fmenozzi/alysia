import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class alysia 
{
    /*
     * The words we'll use
     */
    static List<String[]> dets  = listOfRows("words/det.txt");
    static List<String[]> adjs  = listOfRows("words/adjectives.txt");
    static List<String[]> nouns = listOfRows("words/nouns.txt");
    static List<String[]> preps = listOfRows("words/prepositions.txt");
    static List<String[]> verbs = listOfRows("words/present-verbs.txt");

    /*
     * Ultimately, everything is really just a node
     */
    abstract static class Node
    {
        @Override
        public String toString()
        {
            String fullClassName = this.getClass().getName();
            return fullClassName.substring(fullClassName.lastIndexOf('$') + 1);
        }

        abstract public void print(String prefix);
        abstract public String sentence();
    }

    /*
     * Basic structure of X' schema
     */
    abstract static class XP extends Node
    {
        public Specifier specifier; 
        public Xbar      xbar;
        public boolean   singular;

        public XP(Specifier specifier, Xbar xbar, boolean singular) 
        {
            this.specifier = specifier;
            this.xbar      = xbar;
            this.singular  = singular;
        }

        public void print(String prefix)
        {
            System.out.println(prefix + this);

            if (specifier != null)
                specifier.print(indent(prefix));
            if (xbar != null)
                xbar.print(indent(prefix));
        }

        public String sentence()
        {
            String specifierSentence = specifier != null ? specifier.sentence() : "";
            String xbarSentence      = xbar      != null ? xbar.sentence()      : "";

            if (specifierSentence.equals("") || xbarSentence.equals(""))
                return specifierSentence + xbarSentence;
            else
                return specifierSentence + " " + xbarSentence;
        }
    }
    abstract static class Xbar extends Node
    {
        public X  head;
        public XP complement;

        public Xbar(X head, XP complement)
        {
            this.head       = head;
            this.complement = complement;
        }

        public void print(String prefix)
        {
            System.out.println(prefix + this); 

            if (head != null)
                head.print(indent(prefix));
            if (complement != null)
                complement.print(indent(prefix));
        }

        public String sentence()
        {
            String headSentence       = head       != null ? head.sentence()       : "";
            String complementSentence = complement != null ? complement.sentence() : "";

            if (headSentence.equals("") || complementSentence.equals(""))
                return headSentence + complementSentence;
            else
                return headSentence + " " + complementSentence;
        }
    }
    abstract static class X extends Node
    {
        public String word;

        public X(String word)            {this.word = word;}
        public void print(String prefix) {System.out.println(prefix + this + " - " + word);}
        public String sentence()         {return word;}
    }

    /*
     * Noun Phrase (NP)
     * Verb Phrase (VP) 
     * Adjective Phrase (AP)
     * Prepositional Phrase (PP)
     */
    static class NP extends XP {public NP(Det det, Nbar nbar, boolean sing) {super(det, nbar, sing);}}
    static class VP extends XP {public VP(Adv adv, Vbar vbar, boolean sing) {super(adv, vbar, sing);}}
    static class AP extends XP {public AP(Deg deg, Abar abar, boolean sing) {super(deg, abar, sing);}}
    static class PP extends XP {public PP(Deg deg, Pbar pbar, boolean sing) {super(deg, pbar, sing);}}

    /*
     * Nbar (N')
     * Vbar (V')
     * Abar (A')
     * Pbar (P')
     */
    static class Nbar extends Xbar {public Nbar(N n, XP comp) {super(n, comp);}}
    static class Vbar extends Xbar {public Vbar(V v, XP comp) {super(v, comp);}}
    static class Abar extends Xbar {public Abar(A a, XP comp) {super(a, comp);}}
    static class Pbar extends Xbar {public Pbar(P a, XP comp) {super(a, comp);}}

    /*
     * Nouns (N)
     * Verbs (V)
     * Adjectives (A)
     * Prepositions (P)
     */
    static class N extends X {public N(String word) {super(word);}}
    static class V extends X {public V(String word) {super(word);}}
    static class A extends X {public A(String word) {super(word);}}
    static class P extends X {public P(String word) {super(word);}}

    /*
     * Determiners (Det)
     * Adverbs (Adv)
     * Degree words (Deg) 
     */
    abstract static class Specifier extends Node
    {
        public String word;

        public Specifier(String word)    {this.word = word;}
        public void print(String prefix) {System.out.println(prefix + this + " - " + word);}
        public String sentence()         {return word;}
    }
    static class Det extends Specifier {public Det(String word) {super(word);}}
    static class Adv extends Specifier {public Adv(String word) {super(word);}}
    static class Deg extends Specifier {public Deg(String word) {super(word);}}

    /*
     * Words
     */
    abstract static class Word 
    {

    }
    static class Noun extends Word
    {
        public String sing, plur;

        public Noun(String sing, String plur) {this.sing = sing; this.plur = plur;}
    }
    static class Verb extends Word
    {
        public String[] sing, plur;

        public Verb(String[] sing, String[] plur) {this.sing = sing.clone(); this.plur = plur.clone();}
    }
    static class Adj extends Word
    {
        public String text;

        public Adj(String text) {this.text = text;}
    }
    static class Prep extends Word
    {
        public String text;

        public Prep(String text) {this.text = text;}
    }

    /*
     * Utility functions for printing tree representation
     */
    public static String indent(String prefix)
    {
        return prefix + "....";
    }

    /* 
     * Get all words from file on disk
     */
    public static List<String[]> listOfRows(String filepath)
    {
        List<String[]> rows = new ArrayList<String[]>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.equals(""))
                    continue;

                String[] words = line.split("\\s*,\\s*");

                rows.add(words);
            }

            br.close();

            return rows;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        } catch (IOException e) {
            System.out.println("Error reading file");
            return null;
        }
    }
  
    /*
     * Construct a Noun Phrase
     */
    public static NP buildNP(boolean singularDeterminer)
    {
        // The NP we'll eventually return
        NP np = new NP(null, null, singularDeterminer);

        // Select a determiner at random
        if (!singularDeterminer)
            singularDeterminer = new Random().nextFloat() < 0.5;
        np.specifier = new Det(singularDeterminer ? dets.get(0)[0] : dets.get(1)[0]);

        np.singular = singularDeterminer;

        // Create the N' with a random, exponentially-decaying depth
        np.xbar = new Nbar(null, null);

        int nounIndex = new Random().nextInt(nouns.size());
        np.xbar.head = new N(singularDeterminer ? nouns.get(nounIndex)[0] : nouns.get(nounIndex)[1]);

        np.xbar.complement = new Random().nextFloat() < 0.3 ? buildPP(singularDeterminer) : 
                             new Random().nextFloat() < 0.1 ? buildVP(singularDeterminer) :
                             null;

        return np;
    }

    /*
     * Construct a Prepositional Phrase
     */
    public static PP buildPP(boolean singularDeterminer)
    {
        // The PP we'll eventually return
        PP pp = new PP(null, null, singularDeterminer);

        // For now, we skip the degree
        pp.specifier = null;

        pp.xbar = new Pbar(null, null);
        pp.xbar.head       = new P(preps.get(new Random().nextInt(preps.size()))[0]);
        pp.xbar.complement = buildNP(singularDeterminer);

        return pp;
    }

    /* 
     * Construct a Verb Phrase
     */
    public static VP buildVP(boolean singularDeterminer)
    {
        // The VP we'll eventually build
        VP vp = new VP(null, null, singularDeterminer);

        // For now, we skip the specifier
        vp.specifier = null;

        // For now, everything is 3rd person
        int verbIndex = singularDeterminer ? 2 : 5;

        vp.xbar = new Vbar(null, null);
        vp.xbar.head       = new V(verbs.get(new Random().nextInt(verbs.size()))[verbIndex]);
        vp.xbar.complement = new Random().nextFloat() < 0.5 ? buildPP(singularDeterminer) : 
                             new Random().nextFloat() < 0.3 ? buildNP(singularDeterminer) :
                             null;

        return vp;
    }

    public static void main(String[] args)
    {
        NP np = buildNP(false);
        VP vp = buildVP(np.singular);

        System.out.print(np.sentence() + " ");
        System.out.println(vp.sentence());
    }
}