package etu1896.framework;

import javax.servlet.http.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.sql.Date;
import etu1896.annotation.*;
import etu1896.framework.ModelView;

public class Utilitaire{
        
    public Utilitaire() {
    }

    public static String getUrl(String url) {
        String[] decompose = url.split("/");
        String reponse = "";
        int itterator = 0;
        for(int i = decompose.length - 1; i > 3; i --) {
          if(itterator != 0) {
            reponse = "/" + reponse;
          }
          decompose[i] = decompose[i].replace('?', '=');
          String[] tableau = decompose[i].split("=");
          reponse = tableau[0] + reponse;
          itterator += 1;
        }
        
        return "/" + reponse;
    }

    public String getPath(){
        String path = "";
        File file = new File(new Utilitaire().getClass().getSimpleName()+".java");
        String[] chemins = file.getAbsolutePath().replace("\\", "/").split("/");
        for(int i = 0; i<chemins.length - 2; i++) {
            path += chemins[i] + "/";
        }
        path += chemins[chemins.length - 2];
        return path;
    }

    public static ArrayList<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file,packageName + "." + file.getName()));
            } 
            else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName+ '.'+ file.getName().substring(0,file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static HashMap<String,Mapping> getHashMap(String path) throws ClassNotFoundException{
        HashMap<String,Mapping> mappingUrls = new HashMap<String,Mapping>();
        ArrayList<Class<?>> classes = Utilitaire.findClasses(new File(path), "models");
        ArrayList<Method> methods = new ArrayList<>();
        Anno_Url anno = null;
        for(int i=0; i<classes.size(); i++) {
            Method[] methodes = classes.get(i).getDeclaredMethods();
            for (Method m: methodes) {
                methods.add(m);
            }
            for(int j=0; j<methods.size(); j++) {
                if(methods.get(j).isAnnotationPresent(Anno_Url.class)) {
                    anno = methods.get(j).getAnnotation(Anno_Url.class);
                    mappingUrls.put(anno.url(),new Mapping(classes.get(i).getName(),methods.get(j).getName()));
                }
            }
        }
        return mappingUrls;
    }

    public static void afficher_MappingUrls(HashMap<String,Mapping> mappingUrls,PrintWriter out){
        ArrayList<String> keys =  new ArrayList<String>(mappingUrls.keySet());
        out.println("MappingUrls:");
        for(int i=0;i<keys.size();i++) {
            out.println("key:"+keys.get(i)+" , "+"(classname = "+mappingUrls.get(keys.get(i)).getClassName()+" "+"; method = "+mappingUrls.get(keys.get(i)).getMethod()+")");
        }
    }

    public String getSetters(String setters) {
        String rep = "set";
        rep += setters.toUpperCase().substring(0, 1) + setters.substring(1, setters.length());
        return rep;
    }

    public String getGetters(String getters) {
        String rep = "get";
        rep += getters.toUpperCase().substring(0, 1) + getters.substring(1, getters.length());
        return rep;
    }

    public String[] getAttribute(Object objet) {
        String[] attributes;
        Class<?> classe = objet.getClass();
        Field[] fields = classe.getDeclaredFields();
        attributes = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            attributes[i] = fields[i].getName();
        }

        return attributes;
    }

    public String setFields(Object ob, String setters, String values, String fieldName) {
        Method result = null;
        System.out.println(fieldName);
        try {            
            Field field = ob.getClass().getDeclaredField(fieldName);
            System.out.println(field.getType().getSimpleName());
            if (field.getType().getSimpleName().equalsIgnoreCase("integer")) {
                System.out.println("ok");
                result = ob.getClass().getDeclaredMethod(setters, Integer.class);
                result.invoke(ob, Integer.valueOf(values));
            } else if (field.getType().getSimpleName().equalsIgnoreCase("double")) {
                System.out.println("ok");
                result = ob.getClass().getDeclaredMethod(setters, Double.class);
                result.invoke(ob, Double.valueOf(values));
            } else if (field.getType().getSimpleName().equalsIgnoreCase("date")) {
                result = ob.getClass().getDeclaredMethod(setters, Date.class);
                result.invoke(ob, Date.valueOf(values));
            } else if (field.getType().getSimpleName().equalsIgnoreCase("float")) {
                result = ob.getClass().getDeclaredMethod(setters, Float.class);
                result.invoke(ob, Float.valueOf(values));
            } else {
                result = ob.getClass().getDeclaredMethod(setters, String.class);
                result.invoke(ob, values);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.getName();
    }

    public Object getFields(Object ob, String setters) {
        Method result = null;
        Object rep = null;
        try {
            result = ob.getClass().getDeclaredMethod(setters);
            rep = result.invoke(ob);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rep;
    }

    public Method getMethod(Object ob, String methodName) {
        Class<?> classe = ob.getClass();
        Method method = null;
        Method[] methods = classe.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if(methods[i].getName().equals(methodName)) {
                method = methods[i];
            }
        }

        return method;
    }

    public Class<?>[] getParameters(Method method) {
        Class<?>[] parameters = method.getParameterTypes();
    
        return parameters;
    }
}