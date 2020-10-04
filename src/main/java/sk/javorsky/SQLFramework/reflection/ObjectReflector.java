package sk.javorsky.SQLFramework.reflection;

import sk.javorsky.SQLFramework.anotacie.Id;
import sk.javorsky.SQLFramework.anotacie.Size;
import sk.javorsky.SQLFramework.anotacie.Stlpec;
import sk.javorsky.SQLFramework.anotacie.Tabulka;
import sk.javorsky.SQLFramework.vynimky.MissingIdException;
import sk.javorsky.SQLFramework.vynimky.MissingStlpecAnnotationException;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectReflector {

    public static <T> boolean isTable(Class<T> clazz) {
        return clazz.isAnnotationPresent(Tabulka.class);
    }

    public static <T> String getTableName(Class<T> clazz) {
        return clazz.getAnnotation(Tabulka.class).value();
    }

    public static <T> List<String> getColumnNames(Class<T> clazz) {
        List<String> stlpce = new ArrayList<>();
        for(Field f : clazz.getDeclaredFields()){
            if(f.isAnnotationPresent(Stlpec.class)){
                Stlpec stlpec = f.getAnnotation(Stlpec.class);
                stlpce.add(stlpec.value());
                //System.out.println("Stlpec: "+stlpec.value());
            }
        }

        return stlpce;
    }

    public static <T> String getIdColumnName(Class<T> clazz) {
        String idColumnName = null;
        for(Field f : clazz.getDeclaredFields()){
            if(f.isAnnotationPresent(Id.class)){
                if(f.isAnnotationPresent(Stlpec.class)){
                    idColumnName = f.getAnnotation(Stlpec.class).value();
                }else {
                    throw new MissingStlpecAnnotationException("Pri hladani ID sa nenasiel stlpec oanotovany ako Stlpec.");
                }
            }
        }
        if(idColumnName==null)
            throw new MissingIdException("Chyba anotacia Id v entite "+clazz.getName());

        //System.out.println("Stlpec s ID: "+idColumnName);
        return idColumnName;
    }

    public static <T> T getFilledData(ResultSet result, Class<T> clazz) throws Exception
    {
        T object = null;
        object = clazz.newInstance();
        for(Field f : object.getClass().getDeclaredFields()){
            f.setAccessible(true);
            if(f.isAnnotationPresent(Stlpec.class)){
                String typElementu = f.getType().getName();
                String nazovStlpca = f.getAnnotation(Stlpec.class).value();
                if(typElementu.equals(String.class.getName())){
                    f.set(object, result.getString(nazovStlpca));
                }else if(typElementu.equals(Long.class.getName())){
                    f.set(object,result.getLong(nazovStlpca));
                }else if(typElementu.equals(Integer.class.getName())){
                    f.set(object,result.getInt(nazovStlpca));
                }
            }
        }
        return object;
    }

    public static List<String[]> getFields(Object object) throws Exception
    {   Class<?> clazz = object.getClass();
        //Map<String, Object> data = new HashMap<>();
        List<String[]> listFields= new ArrayList<>();

        String size = "10";
        for(Field f : clazz.getDeclaredFields())
        {
            String[] fields = new String[3];
            f.setAccessible(true);
            if(f.isAnnotationPresent(Stlpec.class))
            {
                if(f.isAnnotationPresent(Size.class)) {
                    size = f.getAnnotation(Size.class).value();
                }
                String typElementu = f.getType().getName();
                String nazovStlpca = f.getAnnotation(Stlpec.class).value();
                fields[0] = nazovStlpca;
                fields[1] = typElementu;
                fields[2] = size;
                //Object hodnota = f.get(object);
                listFields.add(fields);

            }
        }
        return listFields;
    }

    public static List<Object> getObjectData(Object object,List<Object> data) throws Exception
    {   Class<?> clazz = object.getClass();
        //Map<String, Object> data = new HashMap<>();;
        //int i = 0;
        for(Field f : clazz.getDeclaredFields())
        {
            f.setAccessible(true);
            if(f.isAnnotationPresent(Stlpec.class))
            {
                //String typElementu = f.getType().getName();
                //String nazovStlpca = f.getAnnotation(Stlpec.class).value();
                data.add(f.get(object));

            }
        }
        return data;
    }


}
