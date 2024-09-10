/*
 * package RC.TestData;
 * 
 * import java.io.File; import java.io.IOException; import
 * java.nio.charset.StandardCharsets; import java.util.HashMap; import
 * java.util.List;
 * 
 * import org.apache.commons.io.FileUtils;
 * 
 * import com.fasterxml.jackson.core.type.TypeReference; import
 * com.fasterxml.jackson.databind.ObjectMapper;
 * 
 * 
 * public class DataReader {
 * 
 * public List<HashMap<Object,Object>> getJsonDataToMap(String filePath) throws IOException{
 * 
 * 
 * File file=new File(filePath); 
 * //converting json file content to string String
 * jsonString=FileUtils.readFileToString(file,StandardCharsets.UTF_8);
 * //converting string to list of HashMaps using jackson databind ObjectMapper
 * mapper=new ObjectMapper(); //readValue takes TypeReference object i.e which
 * type to return, here it is List<HashMap<Oject,Object>>
 * List<HashMap<Object,Object>> data = mapper.readValue(jsonString, new
 * TypeReference<List<HashMap<Object,Object>>>(){}); return data;
 * 
 * }
 * 
 * }
 */