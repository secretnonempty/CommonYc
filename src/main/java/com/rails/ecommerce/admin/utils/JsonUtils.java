package com.rails.ecommerce.admin.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/** 
 * Json序列化工具 
 *  
 * @author http://blog.csdn.net/xxd851116 
 * @date 2014年3月26日 下午1:21:59 
 */  
public class JsonUtils {  
    private static final Logger logger = (Logger) LoggerFactory.getLogger(JsonUtils.class);  
  
    private static ObjectMapper objectMapper = new ObjectMapper();  
  
    /** 
     * 将对象序列化为JSON字符串 
     *  
     * @param object 
     * @return JSON字符串 
     */  
    public static String serialize(Object object) {  
        Writer write = new StringWriter();  
        try {  
            objectMapper.writeValue(write, object);  
        } catch (JsonGenerationException e) {  
            logger.error("JsonGenerationException when serialize object to json", e);  
        } catch (JsonMappingException e) {  
            logger.error("JsonMappingException when serialize object to json", e);  
        } catch (IOException e) {  
            logger.error("IOException when serialize object to json", e);  
        }  
        return write.toString();  
    }  
  
    /** 
     * 将JSON字符串反序列化为对象 
     *  
     * @param object 
     * @return JSON字符串 
     */  
    public static <T> T deserialize(String json, Class<T> clazz) {  
        Object object = null;  
        try {  
            object = objectMapper.readValue(json, TypeFactory.rawClass(clazz));  
        } catch (JsonParseException e) {  
            logger.error("JsonParseException when serialize object to json", e);  
        } catch (JsonMappingException e) {  
            logger.error("JsonMappingException when serialize object to json", e);  
        } catch (IOException e) {  
            logger.error("IOException when serialize object to json", e);  
        }  
        return (T) object;  
    }  
  
    /** 
     * 将JSON字符串反序列化为对象 
     *  
     * @param object 
     * @return JSON字符串 
     */  
    public static <T> T deserialize(String json, TypeReference<T> typeRef) {  
        try {  
            return (T) objectMapper.readValue(json, typeRef);  
        } catch (JsonParseException e) {  
            logger.error("JsonParseException when deserialize json", e);  
        } catch (JsonMappingException e) {  
            logger.error("JsonMappingException when deserialize json", e);  
        } catch (IOException e) {  
            logger.error("IOException when deserialize json", e);  
        }  
        return null;  
    }  
}