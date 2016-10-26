package test.readers;
import java.util.LinkedHashMap;
import java.util.Map;

import com.datasets.json.JSNode;
import com.datasets.json.JSObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MyTest {
	public static void main(String[] args){
		Map<String,String> pathMap = new LinkedHashMap<String,String>();
		pathMap.put("andrew2", "val2");
		pathMap.put("andrew1.nested1.role", "val3");
		pathMap.put("andrew1.nested1.label", "label");
		pathMap.put("andrew1.nested3.label", "label2");
		JSObject jo = new JSObject(pathMap);
		JSNode node = jo.getNodeByPath("andrew1");
		System.out.println(node.childNodes().size());
		node.childNodes().forEach( n -> System.out.println(n.key()));
	}
}
