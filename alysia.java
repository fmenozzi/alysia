public class alysia 
{
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

        public XP(Specifier specifier, Xbar xbar) 
        {
            this.specifier = specifier;
            this.xbar      = xbar;
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
            String xbarSentence      = xbar != null      ? xbar.sentence()      : "";

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
            String headSentence       = head != null       ? head.sentence()       : "";
            String complementSentence = complement != null ? complement.sentence() : "";

            return headSentence + " " + complementSentence;
        }
    }
    abstract static class X extends Node
    {
        public String word;

        public X(String word) 
        {
            this.word = word;
        }

        public void print(String prefix)
        {
            System.out.println(prefix + this + " - " + word);
        }

        public String sentence()
        {
            return word;
        }
    }

    /*
     * Noun Phrase (NP)
     * Verb Phrase (VP) 
     * Adjective Phrase (AP)
     * Prepositional Phrase (PP)
     */
    static class NP extends XP {public NP(Det det, Nbar nbar) {super(det, nbar);}}
    static class VP extends XP {public VP(Adv adv, Vbar vbar) {super(adv, vbar);}}
    static class AP extends XP {public AP(Deg deg, Abar abar) {super(deg, abar);}}
    static class PP extends XP {public PP(Deg deg, Pbar pbar) {super(deg, pbar);}}

    /*
     * Nbar (N')
     * Vbar (V')
     * Abar (A')
     * Pbar (P')
     */
    static class Nbar extends Xbar {public Nbar(N noun, XP complement) {super(noun, complement);}}
    static class Vbar extends Xbar {public Vbar(V verb, XP complement) {super(verb, complement);}}
    static class Abar extends Xbar {public Abar(A adj,  XP complement) {super(adj,  complement);}}
    static class Pbar extends Xbar {public Pbar(P prep, XP complement) {super(prep, complement);}}

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

        public Specifier(String word) 
        {
            this.word = word;
        }

        public void print(String prefix)
        {
           System.out.println(prefix + this + " - " + word); 
        }

        public String sentence()
        {
            return word;
        }
    }
    static class Det extends Specifier {public Det(String word) {super(word);}}
    static class Adv extends Specifier {public Adv(String word) {super(word);}}
    static class Deg extends Specifier {public Deg(String word) {super(word);}}

    /*
     * Utility functions for printing tree representation
     */
    public static String indent(String prefix)
    {
        return prefix + "....";
    }

    public static void main(String[] args)
    {
        XP np = new NP(new Det("The"), 
                       new Nbar(new N("house"), 
                                null));

        System.out.println(np.sentence() + "\n");

        np.print("");
    }
}