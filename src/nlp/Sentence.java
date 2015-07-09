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
	
	// Word2 of the first Grammar relation with "nsubj" as an identifier
	// is the first noun/number in the sentence
	// This is usually the first value in the condition statement
	// index keeps track of the Grammar relations that are accessed
	public String findFirstValue(int index)
	{
		for(int i = 0; i < relations.size(); i++ )
		{
			GrammarRelation relation = relations.get(i);
			if(relation.findMainSubject("nsubj"))
			{
				index = i;
				return relation.word2.word;
			}
		}
		return null;
	}
	
	
	// to determine the number of Coordinating
	// conjunctions in a given if-else statement
	// @return the number of coordinating conjunctions
	// in a given sentence.
	// E.g of coordination conjunctions: "and" and "or"
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
	
	public String findNextVariableInCondition(String value , int index)
	{
		String nextVariable = null;
		for (int i = index; i < relations.size(); i++)
		{
			GrammarRelation relation = relations.get(i);
			if (relation.hasWord2(value,"nsubj"))
			{
				if (relation.word1.gramClass.equals("CD") || relation.word1.gramClass.equals("NN")|| relation.word1.gramClass.equals("NNP") || relation.word1.gramClass.equals("NNPS"))
				{
					return relation.word1.word;
				}
				// Word1 in the Grammar relation pair is either an adjective or a comparative adjective
				// this implies that word1 is not the second value in the if statement
				// word1 is most likely an "equal"
				else if (relation.word1.gramClass.equals("JJ") || relation.word1.gramClass.equals("JJR"))
				{
					String temp = relation.word1.word;
				    nextVariable = findVariableInIfCondition(temp, null, "nmod");
					return nextVariable;				
				}
			}
		}
		
		return nextVariable;
		
	}
	
	// @param: a word, either word1 or word2, and a relation tag.
	// if word1 is provided, word2 should be null and vice versa
	// @result: if word1 is provided, the corresponding word2 which shares
	// the same relation tag will be provided.
	// The function will only return word1 or word2 if their Parts of Speech tag corresponds to
	// what is shown below.
	// As these POS tags corresponds to numbers, singular and plural nouns,
	// it ensures that the words that are returned are variables/values in the condition statement
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
	
	
	// @param the first variable/value in the condition statement
	// @result the second variable/value that is compared with the first variable
	// in the condition statement
	public String findNextVariableInIfCondition(String w)
	{
		// first, check the number of ands/ors in the sentence
		int numberOfCCs = determineCoordinatingConjunctionsInIfCondition();
		String nextVariable = null;
		for(GrammarRelation r:relations)
		{
			if (numberOfCCs != 0)
			{
				// two conditions in a phrase,
				// e.g greater than or equal to
				if(r.relation.contains("conj"))
				{
					// store the latter condition
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
				// this implies that the word1 found is not the second value but a condition
				// word1 is most likely an "equal"
				else if (r.word1.gramClass.equals("JJ") || r.word1.gramClass.equals("JJR"))
				{
					// store the condition
					String temp = r.word1.word;
				    nextVariable = findVariableInIfCondition(temp, null, "nmod");
					return nextVariable;				
				}
				
			}
		}
		return null;
	}
	
	public String findConditionInSentence(int indexOfFirstValue, int indexOfSecondValue)
	{
		String s = null;
		
		return s;
		
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
