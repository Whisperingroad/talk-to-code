package nlp;

/**
 * Every pair of words share a grammar relation
 *
 */

public class GrammarRelation {
	public Word word1;
	public Word word2;
	public String relation;
	
	GrammarRelation(Word w1, Word w2, String r)
	{
		word1 = w1;
		word2 = w2;
		relation = r;
	}
	
	public boolean hasWord1(String w1, String name)
	{
		if(w1.toLowerCase().equals(word1.word.toLowerCase()))
		{
			String temp = name;
			String temprelation = relation;
			//change to contains
			//to include both nmod:to and nmod:than relations
			if(relation.contains(name))
			{
				return true;
			}
			
		}
		return false;
	}
	// @param the first variable in an if statement
	// @param the POS tag that we are interested in
	public boolean hasWord2(String w2, String name)
	{
		if(w2.toLowerCase().equals(word2.word.toLowerCase()))
		{
			String temp = name;
			String temprelation = relation;
			//change to contains
			//to include both nmod:to and nmod:than relations
			if(relation.contains(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return word1 + " with " + word2 + " has relation " + relation;
	}
	
}
