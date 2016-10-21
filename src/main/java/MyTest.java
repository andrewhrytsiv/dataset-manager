import java.util.LinkedHashMap;
import java.util.Map;

import com.datasets.json.JSObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MyTest {
	public static void main(String[] args){
		Map<String,String> dimMap = new LinkedHashMap<String,String>();
		dimMap.put("ky1", "val1");
		dimMap.put("ky2", "val2");
		dimMap.put("ky1.nested", "nested");
		JSObject jo = new JSObject();
		jo.addPath("andrew2", "val2");
		jo.addPath("andrew1.nested1.role", "val3");
		jo.addPath("andrew1.nested1.label", "label");
		System.out.println(jo.toString());
		System.out.println(jo.getAttributeValueByPath("andrew1.nested1.role"));
	}
}
