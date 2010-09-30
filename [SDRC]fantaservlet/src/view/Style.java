package view;

public class Style {
	static public String successMessage(String msg){
		return 	"<p class=\"success\">"+msg+"</p>";
	}
	
	static public String alertMessage(String msg){
		return 	"<p class=\"alert\">"+msg+"</p>";
	}
	
	static public String option(String val, String msg){
		return "<option value=\""+val+"\">"+msg+"</option>";
	}

	static public String option(String val, String msg, Boolean selected){
		return "<option value=\""+val+"\""+(selected?" SELECTED ":"")+">"+msg+"</option>";
	}	
	
	static public String optionGroup(String label){
		return "<optgroup label=\""+label+"\">\n";
	}		
	
	static public String hidden(String name, String val){
		return "<input type=\"hidden\" name=\""+name+"\" value=\""+val+"\">";
	}	
}
