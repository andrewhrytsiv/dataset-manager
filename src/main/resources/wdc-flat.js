function json2flat(json){
	var object = JSON.parse(json);
	plane = [];
	p = function(object,path){
		for(prop in object){
			path.push(prop);
			if(Array.isArray(object[prop]) || !isObject(object[prop])){
				key = path.join(".");
				key += (Array.isArray(object[prop]))? "[]" : '';
				plane.push({
					path:key, 
					value:(Array.isArray(object[prop]))? object[prop].join(","):object[prop]
				});
			path.splice(-1,1);	
			}else{p(object[prop],path)}
		}
		path.splice(-1,1);
	}
	p(object,[]);
	var metadata = new java.util.LinkedHashMap();
	for(var index in plane){
		metadata.put(plane[index].path, plane[index].value);
	}
	return metadata;
}

function isObject(val) {
    if (val === null) { return false;}
    return ( (typeof val === 'function') || (typeof val === 'object') );
}
