package etu1896.framework;

import java.lang.reflect.*;
import java.util.*;

public class ModelView {
  String view;
  HashMap<String,Object> data = new HashMap<String, Object>();

  public HashMap<String, Object> getData() {
    return data;
  }

  public void setData(HashMap<String, Object> data) {
    this.data = data;
  }

  public String getView() {
    return view;
  }

  public void setView(String view) {
    this.view = view;
  }

  
  public ModelView(String view) {
    this.view = view;
  }

  public ModelView(){

  }

  public static ModelView loadView(String url, HashMap<String, Mapping> mappingUrls, String id) throws Exception {
    Set<String> set = mappingUrls.keySet();
    if (!set.contains(url)) {
      throw new Exception("404 not found!");
    }

    String className = mappingUrls.get(url).getClassName();
    String methodName = mappingUrls.get(url).getMethod();
    System.out.println(className);
    System.out.println(methodName);
    Class<?> classe = Class.forName(className);
    Constructor<?> constructor = classe.getDeclaredConstructor();
    Object object = constructor.newInstance();
    Method method = null;
    ModelView view = null;
    if(id != null) {
      // System.out.println("mety?");
      method = classe.getDeclaredMethod(methodName, Integer.class);
      // System.out.println(method);
      view = (ModelView) method.invoke(object, Integer.valueOf(id));
      // System.out.println(view);
      // System.out.println(view.getView());
    } else { 
      method = classe.getDeclaredMethod(methodName);
      view = (ModelView) method.invoke(object);
    }
    
    return view;
  }

  public void addItem(String key, Object value) {
    this.getData().put(key,value);
  }
 
}