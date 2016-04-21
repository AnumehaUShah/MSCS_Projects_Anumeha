package backend;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import frontend.Parser;
import intermediate.Node;

/**
 * This class executes the scheme expression 
 * @author Anumeha
 *
 */

public class Execute {
	
	//public static Node lambda_root = new Node(null);
	public static TreeMap<String, TreeMap<String, Object>> lambda_sym_tbl = new TreeMap<String, TreeMap<String, Object>>();
	public static TreeMap<String, Object> current_lambda_sym_tbl = new TreeMap<String, Object>();
	public static HashMap<String, Object> current_param = new HashMap<String, Object>();
	public static TreeMap<String, Object> global_sym_tbl = new TreeMap<String,Object>();
	public static TreeMap<String, Object> level1_sym_tbl = new TreeMap<String,Object>();
	public static TreeMap<String, Object> stackOfStackTables = new TreeMap<String,Object>();
	public static Stack<HashMap<String, Object>> runtime_stack = new Stack<HashMap<String, Object>>();
	/**
	 * This function executes the scheme expression 
	 *
	 */
	public static void execute(String procedure_name, ArrayList<String> tokenList , TreeMap<String, Object> paramStackOfStackTables, ArrayList<String> tokenListWithParamSpaces)
	 {
		stackOfStackTables = paramStackOfStackTables;
		
		//get the global sym table
		global_sym_tbl = getGlobalSymTbl(stackOfStackTables);
		//System.out.println("global_sym_tbl " + global_sym_tbl);
		
		//get the level1_sym_table
		level1_sym_tbl = getLevel1SymTbl(stackOfStackTables);
		//System.out.println("level1_sym_tbl " + level1_sym_tbl);
		
		//get the lambda root reference
		//Node proc_root = getProcRoot(procedure_name, stackOfStackTables);
		//PrintBinaryTree.printBTree(proc_root, true);
		
		//get the lambda symbol table refrence
		//lambda_sym_tbl = proc_root.symTablerefrence;
		//System.out.println("lambda_sym_tbl " + lambda_sym_tbl);
		//fetch the parameters valu		
		ArrayList<String> parameter_values = getParametersVal(procedure_name,tokenListWithParamSpaces);
		//System.out.println("parameter_values " + parameter_values);
		
		Object output = main(procedure_name, parameter_values);
		if (output instanceof Boolean && output.toString() == "true")
		{
			output = "#t";
		}
		else if (output instanceof Boolean && output.toString() == "false")
		{
			output = "#f";
		}
		
		System.out.println("\nPROGRAM RESULT >>> " + output);
	 }
	
	public static Object main(String procedure_name, ArrayList<String> parameter_values)
	{
		//get the lambda root reference
		Node proc_root = getProcRoot(procedure_name, stackOfStackTables);
		//PrintBinaryTree.printBTree(proc_root, true);
		
		//get the lambda symbol table refrence
		
		 TreeMap<String, Object> proc = lambda_sym_tbl.get(procedure_name);
		if(proc == null)
		{
			proc = new TreeMap<String, Object>();
			proc.put("symTablerefrence", proc_root.symTablerefrence);
			lambda_sym_tbl.put(procedure_name, proc);
		}
		current_lambda_sym_tbl = (TreeMap<String, Object>) proc.get("symTablerefrence");
		
		if(current_lambda_sym_tbl.size() != (parameter_values.size() + 1))
		{
			String error = "Incorrect number of argument passed. Expected: " + (current_lambda_sym_tbl.size()-1) + ". Passed: " + parameter_values.size();
			return error;
		}
		
		//lambda_sym_tbl = proc_root.symTablerefrence;
		//System.out.println("lambda_sym_tbl " + lambda_sym_tbl);
		

		//set the parameters value
		HashMap<String, Object> paramMap2 = new HashMap<String, Object>();
		HashMap<String, Object> paramMap =  setParametersVal(proc_root.right.left, procedure_name, parameter_values, paramMap2, 0);
		//System.out.println("paramMap " + paramMap);
		//System.out.println("lambda_sym_tbl " + lambda_sym_tbl);
		//create activation record and add to runtime stack
		HashMap<String, Object> activationRecord = new HashMap<String, Object>();
		activationRecord.put("0", current_param);
		runtime_stack.push(activationRecord);
		
		current_param = paramMap;
		//activationRecord.put("proc_name", procedure_name);
		
	/*	if(proc_root.left.data != null){
			activationRecord.put("scope", proc_root.left.data);
		}*/
		
		
		//System.out.println(activationRecord);
		//main execution of binary tree starts
		
		Object output = processNode(proc_root.right.right.left, null);
		current_param = runtime_stack.pop();
		current_param = (HashMap<String, Object>) current_param.get("0"); 
		
		return output;
	}
	
	/**
	 * This function  gets the lambda root reference 
	 *
	 */
	public static Node getProcRoot(String procedure_name, TreeMap<String, Object> stackOfStackTables){
		Node proc_root = new Node(null);
		for(Map.Entry<String,Object> entry : stackOfStackTables.entrySet()){
			String key = entry.getKey();
			TreeMap<String, Object> value = (TreeMap<String, Object>) entry.getValue();
			
			if(key.equals("level1Symboltable")){
				for(Map.Entry<String,Object> value_entry : value.entrySet()){
					String value_entry_key = value_entry.getKey();
					if(value_entry_key.equals(procedure_name)){
						TreeMap<String, Object>value_entry_value = (TreeMap<String, Object>) value_entry.getValue();
						for(Map.Entry<String,Object> value_entry_entry : value_entry_value.entrySet()){
							
							if(value_entry_entry.getKey().equals("main_root_ref")){
								continue;
							}
							else{
								proc_root = (Node) value_entry_entry.getValue();
							}
						}
					}
					
				}
				
			}
		}//end of the outermost for loop
		
		return proc_root;
	}//end of function getLambdaRoot
	
	
	/**
	 * This function  returns the parameter values from the tokenList 
	 *
	 */	
	public static ArrayList getParametersVal(String name, ArrayList<String> tokenList){
		ArrayList valueList = new ArrayList();
		ArrayList paramTokens = new ArrayList();
		//ArrayList param;
		//boolean found_list = false;
		for(int i=2; i< tokenList.size(); i++ ){
			String token = tokenList.get(i); 
			if(token.equals(" "))
			{
				int j = i+1;
				while(j < tokenList.size() && !tokenList.get(j).equals(" "))
				{
					paramTokens.add(tokenList.get(j));
					j++;
				}
				ArrayList tmpList = createParam(paramTokens);
				valueList.add(tmpList.get(0));
				paramTokens.clear();
				i = j - 1;
			}
		}
		return valueList;
	}
	
	
	public static ArrayList createParam(ArrayList<Object> tokenList)
	{
		ArrayList valueList = new ArrayList();
		int last_brace_found = -1;
		ArrayList subList = new ArrayList();
		boolean is_first_brace = false;
		int first_brace_pos = -1;

		outerLoop: for(int i=0; i < tokenList.size(); i++)
		{
			if(tokenList.get(i).equals("'"))
			{
				continue;
			}
			else if(tokenList.get(i).equals("("))
			{
				if(is_first_brace == false)
				{
					first_brace_pos = i;
					is_first_brace = true;
				}
				last_brace_found = i;
			}
			else if(tokenList.get(i).equals(")") && last_brace_found != -1)
			{
				ArrayList tmpList = new ArrayList();
				int no_of_element = 0;
				for(int j=last_brace_found+1;j<i;j++)
				{
					tmpList.add(tokenList.get(j));
					no_of_element++;
				}
				tokenList.add(last_brace_found, tmpList);
				for(int j=1;j<=no_of_element+2;j++)
				{
					tokenList.remove(last_brace_found+1);
				}
				i = first_brace_pos-1;
				last_brace_found = -1;
			}
			else if(tokenList.get(i).equals(")") && last_brace_found == -1)
			{
				tokenList.remove(i);
			}
		}
		
		if(tokenList.get(0).equals("'"))
		{
			tokenList.remove(0);
		}
		
		return tokenList;
	}
	
	/**
	 * This function  sets the parameter values to the  lambda ref table parameters 
	 *
	 */
	public static HashMap<String, Object> setParametersVal(Node proc_root_right, String procedure_name,ArrayList<String> params, HashMap<String,Object>param2, int start){
		
		Node root = proc_root_right;
		
		TreeMap<String, Object> tmp_lambda_symbol_table = lambda_sym_tbl.get(procedure_name);
		TreeMap<String, Object> current_symbol_tb_reference = (TreeMap<String, Object>) tmp_lambda_symbol_table.get("symTablerefrence"); 
		
		if(root == null || root.left == null || root.right == null){
			return param2;
		}
		
		else if(root.left.data != null && current_symbol_tb_reference.containsKey(root.left.data) ){
			current_symbol_tb_reference.put(root.left.data, params.get(start));
			param2.put(root.left.data, params.get(start));
			start++;
			
			if(root.right !=null){
				root = root.right;
				tmp_lambda_symbol_table.put("symTablerefrence", current_symbol_tb_reference);
				lambda_sym_tbl.put(procedure_name, tmp_lambda_symbol_table);
				setParametersVal(root, procedure_name, params,param2, start);
			}
			else{
				return param2;
			}
		}
		else{
			return param2;
		}
		
		return param2;
		
	}
	
	public static Object processNode(Node root, Node root_parent){
		
		//System.out.println("Printong binary tree in  java");
		//PrintBinaryTree.printBTree(root, true);
	
		//String data = root.left.data;
		if(root.data == null){
			 Object return_data = processNode(root.left, root);
			 return return_data; 
			
		}
		else if(current_param.containsKey(root.data)){
			Object value = current_param.get(root.data);
			return value;
		}
		else if(level1_sym_tbl.containsKey(root.data)){
			return evalUserFunc(root.data, root_parent.right);
			//String param_list=  evalRecursion(root.data, root_parent.right);
			//return param_list;
			//recursion call to execute
		}
		else if(global_sym_tbl.containsKey(root.data)){
			if(root.data.equals("cond")){
				return evalCond(root_parent.right);
			}
			else if(root.data.equals("let*")){
				Object returnVal = evalLetStar(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}
			else if(root.data.equals("let")){
				Object returnVal = evalLetStar(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}			
			else if(root.data.equals(">")){
				Boolean returnVal = evalGreaterThan(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}
			else if(root.data.equals("if")){
				Object returnVal = evalIf(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}			
			else if(root.data.equals("null?")){
				Boolean returnVal = evalNull(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}
			else if(root.data.equals("symbol?")){
				Boolean returnVal = evalSymbol(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}
			else if(root.data.equals("integer?")){
				Boolean returnVal = evalInteger(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}
			else if(root.data.equals("real?")){
				Boolean returnVal = evalReal(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}			
			else if(root.data.equals("boolean?")){
				Boolean returnVal = evalBoolean(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}
			else if(root.data.equals("string?")){
				Boolean returnVal = evalString(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}
			else if(root.data.equals("char?")){
				Boolean returnVal = evalChar(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}			
			else if(root.data.equals("pair?")){
				Boolean returnVal = evalPair(root_parent.right);
				//System.out.println("value of function null" + returnVal);
				return returnVal;
			}
			
			else if(root.data.equals("equal?")){
				Boolean returnVal = evalEqual(root_parent.right);
				return returnVal;
			}
			else if(root.data.equals("#f")){
				return "#f";
			}
			else if(root.data.equals("#t")){
				return "#t";
			}
			else if(root.data.equals("car")){
				Object car =  evalCar(root_parent.right);
				//System.out.println("returned car value = " + car );
				return car;
			}
			else if(root.data.equals("else")){
				return "else";
			}
			else if(root.data.equals("cdr")){
				Object cdr = evalCdr(root_parent.right);
				return cdr;
			}
			else if(root.data.equals("cadr")){
				Object cadr = evalCadr(root_parent.right);
				return cadr;
			}
			else if(root.data.equals("and")){
				Object and = evalAnd(root_parent.right);
				return and;	
			}
			else if(root.data.equals("not")){
				Object not = evalNot(root_parent.right, root_parent);
				return not;	
			}
			else if(root.data.equals("cons")){
				Object cons = evalCons(root_parent.right, root_parent);
				return cons;	
			}
			else if(root.data.equals("'")){
				Object list = evalList(root_parent.right, root_parent);
				return list;	
			}
			else if(root.data.equals("or")){
				Object list = evalOr(root_parent.right);
				return list;	
			}
		    else if(root.data.equals("cadr")){
		    	Object cadr = evalCadr(root_parent.right);
	            return cadr;
		    }
	        else if(root.data.equals("cddr")){
	        	Object cadr = evalCddr(root_parent.right);
	            return cadr;
	        }
	        else if(root.data.equals("list")){
	        	Object cadr = evalList(root_parent.right);
	            return cadr;
	        }
	        else if(root.data.equals("append")){
	        	Object cadr = evalAppend(root_parent.right);
	            return cadr;
	        }			
		}
		return root.data; 
	  }
	
	public static Object evalLetStar(Node root){
		ArrayList letVariables = setLetStarVariables(root.left);
		
		Object letResult = processNode(root.right, root);
		
		for(Object s : letVariables)
		{
			current_param.remove(s);
		}
		
		return letResult;
	}
	
	public static ArrayList setLetStarVariables(Node root)
	{
		ArrayList letVariables = new ArrayList();
		
		if(root.left != null)
		{
			String variable_name = processNode(root.left.left, root.left).toString();
			Object variable_value = processNode(root.left.right, root.left);
			letVariables.add(variable_name);
			current_param.put(variable_name, variable_value);
			if(root.right.left != null)
			{			
				letVariables.addAll(setLetStarVariables(root.right));
			}
		}
		
		return letVariables;
	}
	
	public static Object evalLet(Node root){
		HashMap<String, Object> letVariables = setLetVariables(root.left);
		
		current_param.putAll(letVariables);
		
		Object letResult = processNode(root.right, root);
		
		for (Map.Entry<String, Object> entry : letVariables.entrySet())
		{
			current_param.remove(entry.getKey());
		}

		return letResult;
	}
	
	public static HashMap<String, Object> setLetVariables(Node root)
	{
		HashMap<String, Object> letVariables = new HashMap<String, Object>();
		
		if(root.left != null)
		{
			String variable_name = processNode(root.left.left, root.left).toString();
			Object variable_value = processNode(root.left.right, root.left);
			letVariables.put(variable_name, variable_value);
			//current_param.put(variable_name, variable_value);
			if(root.right.left != null)
			{			
				letVariables.putAll(setLetVariables(root.right));
			}
		}
		
		return letVariables;
	}
	
	public static Boolean evalGreaterThan(Node root){
        Boolean returnValue = false;
       
        try{
            int left  = Integer.parseInt(processNode(root.left, root).toString());
            int right = Integer.parseInt(processNode(root.right, root).toString());
           
            if(left > right){
                returnValue = true;
            }
        }
        catch (NumberFormatException ex)
        {
            returnValue = false;
        }
       
        return returnValue;
   
    }
	
	public static Object evalIf(Node root){
        Boolean condition = (Boolean) processNode(root.left, root);
       
        if(condition == true){
        	Object first = processNode(root.right, root);
            return first;
        }
        else{
        	Object second;
            if(root.right.right.left.data == null && root.right.right.left.left == null && root.right.right.left.right == null )
            {
            	second = processNode(root.right.right.right, root);
            }
            else
            {
            	second = processNode(root.right.right, root);
            }        	
            return second;
        }
       
       
    }
	
	public static ArrayList evalList(Node root, Node root_parent){
		ArrayList list = new ArrayList();
		while(root.left.data != null)
		{
			list.add(root.left.data);
			root = root.left;
		}
		return list;
	}
	
	public static ArrayList evalCons(Node root, Node root_parent){
		Object value_left =  (Object) processNode(root.left, root_parent);
		ArrayList<String> value_right = (ArrayList) processNode(root.right, root_parent);
		
		ArrayList finalArrayList = new ArrayList();
		//for (String s : value_left)
		    finalArrayList.add(value_left);
		for (Object s : value_right)
		    finalArrayList.add(s);		
		return finalArrayList;
	}	
	
	public static Boolean evalNot(Node root, Node root_parent){
		Object notResult = processNode(root_parent.right, root_parent);
		if(notResult.equals("false") || notResult.equals("#f") || notResult.equals(false))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
		
	
	
	public static Object evalCond(Node root){
		
		/*if(root.right == null){
			returnValue =  false;
			return returnValue;
		}*/
		
		Node current = root.left;
		String left = processNode(current.left, current).toString();
		
		//System.out.println("evalCond left" + left);
		//System.out.println("evalCond right" + right);
		if(left.equals("else"))
		{
			Object right = processNode(current.right, current);
			return right;
		}
		else if(left.equals("false")){
			return evalCond(root.right);
		}
		else{
			Object right = processNode(current.right, current);
			return right;
		}
	}
	
	public static Object evalAnd(Node root){
		
		String left = processNode(root.left, root).toString();
		
		
		//System.out.println("evalCond left" + left);
		//System.out.println("evalCond right" + right);
		if((left.equals("true") || left.equals("#t")))
		{
			String right = processNode(root.right, root).toString();
			if((right.equals("true") || right.equals("#t")))
				return true;
			else
				return false;
		}
		else{
			return false;
		}
	}
	
	public static Object evalOr(Node root){
		
		if(root.left == null)
			return false;
		
		String orResult;
		orResult = processNode(root.left, root).toString();
		if((orResult.equals("true") || orResult.equals("#t")))
		{
			return true;
		}
		
		return evalOr(root.right);
		
	}	
	
	public static Boolean evalEqual(Node root){
		Boolean returnValue = true;
		
		Object left = processNode(root.left, root);
		Object right = processNode(root.right, root);
		
		if(left.equals(right)){
			returnValue = true;
		}
		else{
			returnValue = false;
		}
		//System.out.println("evalEqual left" + left);
		//System.out.println("evalEqual right" + right);
		
		return returnValue;
	}
	
	public static Object evalUserFunc(String Procedure_name, Node root){
		
		
		Node proc_root = getProcRoot(Procedure_name, stackOfStackTables);
		TreeMap<String, Object> proc = lambda_sym_tbl.get(Procedure_name);
		if(proc == null)
		{
			proc = new TreeMap<String, Object>();
			proc.put("symTablerefrence", proc_root.symTablerefrence);
			lambda_sym_tbl.put(Procedure_name, proc);
		}
		current_lambda_sym_tbl = (TreeMap<String, Object>) proc.get("symTablerefrence");
		
		System.out.print(Procedure_name);
		
		ArrayList parameter_values = new ArrayList();
		if(current_lambda_sym_tbl.size() > 1)
		{
			Object param_1 = processNode(root.left, root);
			System.out.print("( " + param_1);
			parameter_values.add(param_1);
		}
		if(current_lambda_sym_tbl.size() > 2)
		{
			Object param_2 = processNode(root.right, root);
			System.out.print(", " + param_2);
			parameter_values.add(param_2);
		}
		if(current_lambda_sym_tbl.size() > 3)
		{
			Object param_3 = processNode(root.right.right, root);
			System.out.print(", " + param_3);
			parameter_values.add(param_3);
		}		
		System.out.println(")");
		
		//Node proc_root = getProcRoot(Procedure_name, stackOfStackTables);
		Object return_val = main(Procedure_name, parameter_values);
		
		return return_val;
	}
	
	public static Boolean evalNull(Node root){
		
		Boolean return_val = true;
		String data = processNode(root.left, root).toString();
		//System.out.println("evalnull data" + data);
		if(data.isEmpty() || data.equals("()") || data.equals("( )") || data.equals("[]")){
			return_val = true;
		}
		else{
			return_val = false;
		}
		//System.out.println("evalNull output" + return_val);
		return 	return_val;
		
		
	}
	
public static Boolean evalSymbol(Node root){
		
		Boolean return_val = true;
		String data = processNode(root.left, root).toString();
		
		if (data.charAt(0)  == '\''){
			return_val = true;
		}
		else{
			return_val = false;
		}
	
		return 	return_val;
		
		
	}
	
	public static Boolean evalInteger(Node root){
		
		Boolean return_val = true;
		String data = processNode(root.left, root).toString();
		
		if(data.contains("."))
		{
			return false;
		}
		
		try
	    {
	        Integer.parseInt(data);
	        return_val= true;
	    } catch (NumberFormatException ex)
	    {
	    	return_val = false;
	    }
	
		return 	return_val;	
	}
	
	public static Boolean evalReal(Node root){
		
		Boolean return_val = true;
		String data = processNode(root.left, root).toString();
		
		try
	    {
			Float val = Float.parseFloat(data);
			if(val != Math.ceil(val))
				return_val= true;
			else
				return_val= false;
	    } 
		catch (NumberFormatException ex)
	    {
	    	return_val = false;
	    }
	
		return 	return_val;	
	}

	public static Boolean evalChar(Node root){
		
		Boolean return_val = true;
		String data = processNode(root.left, root).toString();
		
		if(data.length()==1 && Character.isLetter(data.charAt(0)))
			return true;
		else
			return false;
	}		

	public static Boolean evalBoolean(Node root){
	
		Boolean return_val = true;
		String data = processNode(root.left, root).toString();
		if(data.equals("#f") || data.equals("#t")){
			return_val = true;
		}
		else{
			return_val = false;
		}
	 
		return 	return_val;	
	
	
	}
	
	public static Boolean evalString(Node root){
		
		Boolean return_val = true;
		String data = processNode(root.left, root).toString();
		if(data.charAt(0) == '"' && data.charAt(data.length()-1) == '"'){
			return_val = true;
		}
		else{
			return_val = false;
		}
	 
		return 	return_val;	
	
	
	}
	
	public static Boolean evalPair(Node root){
		
		Boolean return_val = true;
		String data = processNode(root.left, root).toString();
		//System.out.println("evalPair" + data);
		if(data.charAt(0) == '[' && data.charAt(data.length()-1) == ']'){
			return_val = true;
		}
		else{
			return_val = false;
		}
	 
		return 	return_val;	
	
	
	}
	
	/*public static Boolean evalChar(Node root){
		
		Boolean return_val = true;
		String data = processNode(root.left,root).toString();
		if(data.charAt(0) == '#' && data.charAt(1) == '\\' ){
			return_val = true;
		}
		else{
			return_val = false;
		}
	 
		return 	return_val;	
	}*/
	
	
	public static Object evalCar(Node root){
		
		ArrayList lst = (ArrayList) processNode(root.left, root);
		//System.out.println("Car lst" + lst);
		Object car = (Object) lst.get(0);
		
		//System.out.println("car "+ car);
		return car;
		
	}
	
public static ArrayList evalCdr(Node root){
		
		ArrayList lst = (ArrayList) processNode(root.left, root);
		ArrayList tmpLst = new ArrayList();
		for(Object lst_item : lst ){
			tmpLst.add(lst_item);
		}
		tmpLst.remove(0);
		return tmpLst;
		//lst.remove(0);
		//return lst;
		
	}

/*public static ArrayList evalCadr(Node root){
	
	ArrayList lst = (ArrayList) processNode(root.left, root);
	ArrayList tmpLst = new ArrayList();
	for(Object lst_item : lst ){
		tmpLst.add(lst_item);
	}
	tmpLst.remove(0);
	return tmpLst;
	//lst.remove(0);
	//return lst;
	
}*/

public static Object evalCadr(Node root){
	   
    ArrayList lst = (ArrayList) processNode(root.left, root);
    ArrayList tmpLst = new ArrayList();
    for(Object lst_item : lst ){
        tmpLst.add(lst_item);
    }
    tmpLst.remove(0);
    Object carTmpList = tmpLst.get(0);
    return carTmpList;
    //lst.remove(0);
    //return lst;
   
}

public static Object evalCddr(Node root){
   
    ArrayList lst = (ArrayList) processNode(root.left, root);
    ArrayList tmpLst = new ArrayList();
    for(Object lst_item : lst ){
        tmpLst.add(lst_item);
    }
    tmpLst.remove(0);
    ArrayList cdrTmpList = tmpLst;
    cdrTmpList.remove(0);
    return cdrTmpList;
    //lst.remove(0);
    //return lst;
   
}

    public static Object evalList(Node root){
   
        ArrayList listing  = new ArrayList();
        String data = new String ();
        if(root.right != null){
            data =  processNode(root.left, root).toString();
            listing.add(data);
            if(root.right.right != null)
            	listing.addAll((Collection) evalList(root.right));
                   
            return listing;
        }
        return false;
    }
   
   
    public static Object evalAppend(Node root){
       
        ArrayList listRight  = (ArrayList) processNode(root.left, root);
        ArrayList listLeft = (ArrayList) processNode(root.right, root);
       
        listRight.addAll(listLeft);
        return listRight;
       

    }
	
	public static TreeMap<String, Object> getLevel1SymTbl(TreeMap<String, Object> stackOfStackTables){
		
		for(Map.Entry<String,Object> entry : Parser.stackOfStackTables.entrySet()){
			String key = entry.getKey();
			TreeMap<String, Object> value = (TreeMap<String, Object>) entry.getValue();
			
			if(key.equals("level1Symboltable")){
				return value;
			}
		}
		return new TreeMap<String, Object>();
	}
	
public static TreeMap<String, Object> getGlobalSymTbl(TreeMap<String, Object> stackOfStackTables){
		
		for(Map.Entry<String,Object> entry : Parser.stackOfStackTables.entrySet()){
			String key = entry.getKey();
			TreeMap<String, Object> value = (TreeMap<String, Object>) entry.getValue();
			
			if(key.equals("globalSymbolTable")){
				return value;
			}
		}
		return new TreeMap<String, Object>();
	}


	
	
	
	
	

}
