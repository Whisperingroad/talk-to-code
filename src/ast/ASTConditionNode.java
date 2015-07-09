package ast;

import java.util.ArrayList;

public class ASTConditionNode extends ASTNode{

	String value1;
	String operand;
	String value2;
	ArrayList<String> conditionStatement;
	
	ASTConditionNode(ASTNode p)
	{
		super(p);
		value1="";
		operand="";
		value2="";
		conditionStatement = new ArrayList<String>();
		
	}
	
	ASTConditionNode(String v1, String o, String v2)
	{
		value1 = v1;
		operand = o;
		value2 = v2;
	}
	
	//public String print()
	//{
		//return value1+operand+value2;
	//}
	
	public String print()
	{
		String statement = "";
		for (String partOfStatement : conditionStatement)
		{
			statement = statement + " " + partOfStatement;
		}
		return statement;
	}
}
