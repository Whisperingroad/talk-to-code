package nlp;

import java.util.ArrayList;

public class Sentence {
	public ArrayList<Word> words;
	public ArrayList<GrammarRelation> relations;
	public ArrayList<Clause> clauses;
	public String fullSentence;
	
	Sentence(ArrayList<Word> w, ArrayList<GrammarRelation> r, ArrayList<Clause> c)
	{
		words = w;
		relations = r;
		clauses = c;
		String x = "";
		for(Word w1:words)
			x = x.concat(w1.word+" ");
		x.trim();
		x = x.substring(0, x.length()-3);
		x = x.concat(".");
		fullSentence = x;
	}
	
	Sentence(ArrayList<Word> w)
	{
		words = w;
		relations = new ArrayList<GrammarRelation>();
		clauses = new ArrayList<Clause>();
	}
	
	public ArrayList<Word> getPosTagListFromClause(String POSTag, int indx)
	{
		ArrayList<Word> retList = new ArrayList<Word>();
		for(Word w:clauses.get(indx).words)
			if(w.gramClass.equals(POSTag))
				retList.add(w);
		return retList;
	}
	
	public ArrayList<Word> getPosTagList(String POStag)
	{
		ArrayList<Word> retList = new ArrayList<Word>();
		for(Word w: words)
		{
			if(w.gramClass.equals(POStag))
				retList.add(w);
		}
		return retList;
	}
	
	public String findRelation(Word w, String name)
	{
		for(GrammarRelation r:relations)
		{
			if(r.hasWord1(w.word, name))
			{
				return r.word2.word;
			}		
		}
		return null;
	}
	
	public String findWord2InRelation(String w, String name)
	{
		for(GrammarRelation r: relations)
		{
			if(r.hasWord1(w, name))
			{
				return r.word2.word;
			}
		}
		return null;
	}
	
	public String findVariableInIfCondition(String word1, String word2,String relationTag)
	{
		for(GrammarRelation r: relations)
		{
			String check = r.word2.word;
			String check1 = r.word1.word;
			if (word2 == null)
			{
				if(r.hasWord1(word1, relationTag) && (r.word2.gramClass.equals("CD") || r.word2.gramClass.equals("NN")|| r.word2.gramClass.equals("NNP") || r.word2.gramClass.equals("NNPS")))
				{
					return r.word2.word;
				}
			}
			if (word1 == null)
			{
				if(r.hasWord2(word2, relationTag) && (r.word1.gramClass.equals("CD") || r.word1.gramClass.equals("NN")|| r.word1.gramClass.equals("NNP") || r.word1.gramClass.equals("NNPS")))
				{
					return r.word1.word;
				}
			}
		}
		return null;
	}
	
	// @param w: the first variable in an if statement
	// @param name: the POS tag that we are interested in
	// @return the string that corresponds to the provided
	// POS tag. In this case, it should be the second variable
	public String findWord1InRelation(String w, String name)
	{
		//looping through every grammar relation from a single sentence
		for(GrammarRelation r:relations)
		{
			// if a particular grammar relation contains the word w,
			// and owns the desired relation tag
			// the other word of this grammar relation should be the one
			// that we are looking for.
			if(r.hasWord2(w, name))
			{
				return r.word1.word;
				
			}
		}
		return null;
	}
	
	// to determine the number of Coordination
	// conjunctions in a given if-else statement
	public int determineCoordinatingConjunctionsInIfCondition()
	{
		int numberOfCCs = 0;
		for(GrammarRelation r: relations)
		{
			String relationTag = "conj";
			if(r.relation.contains(relationTag))
			{
				numberOfCCs++;
			}
		}
		return numberOfCCs;
	}
	
	public String findNextVariableInIfCondition(String w)
	{
		int numberOfCCs = determineCoordinatingConjunctionsInIfCondition();
		String nextVariable = null;
		for(GrammarRelation r:relations)
		{
			if (numberOfCCs != 0)
			{
				if(r.relation.contains("conj"))
				{
					String temp = r.word2.word;
					nextVariable = findVariableInIfCondition(temp, null, "nmod");
					numberOfCCs--;
					return nextVariable;
				}
			}
			// for straightforward if-else conditions
			else if(r.hasWord2(w, "nsubj") && numberOfCCs == 0)
			{
				// for debugging purposes
				String check = r.word2.word;
				String checkgram = r.word2.gramClass;
				String check1 = r.word1.word;
				String checkgram1 = r.word1.gramClass;		
				// Word1 in the Grammar relation pair is either a Cardinal Number, a singular proper noun, or a plural proper noun
				if (r.word1.gramClass.equals("CD") || r.word1.gramClass.equals("NN")|| r.word1.gramClass.equals("NNP") || r.word1.gramClass.equals("NNPS"))
				{
					return r.word1.word;
				}
				// Word1 in the Grammar relation pair is either an adjective or a comparative adjective
				// this implies that word1 is not the second value in the if statement
				// word1 is most likely an "equal"
				else if (r.word1.gramClass.equals("JJ") || r.word1.gramClass.equals("JJR"))
				{
					String temp = r.word1.word;
				    nextVariable = findVariableInIfCondition(temp, null, "nmod");
					return nextVariable;				
				}
				
			}
		}
		return null;
	}
	
	
	
	
	public String print()
	{
		String s = "";
		for(Word w:words)
		{
			s = s.concat(w.word+" ");
		}
		return s;
	}
	
	public String returnWordElement(String w, int displacement)
	{
		int i = 0;
		for(Word w1:words)
		{
			if(w1.word.equals(w))
				if(i+displacement>=0)
					return words.get(i+displacement).word;
				else
					return null;
			i++;			
		}
		
		return null;	
	}
	
	public boolean find(String w)
	{
		for(Word w1:words)
		{
			if(w1.word.toLowerCase().equals(w.toLowerCase()))
				return true;
		}
		return false;
	}
	
	public boolean findPhrase(String p)
	{
		if(fullSentence.contains(p))
			return true;
		return false;
	}
	
	
}
