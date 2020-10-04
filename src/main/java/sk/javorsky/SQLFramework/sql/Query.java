package sk.javorsky.SQLFramework.sql;

import java.util.List;

public class Query
{
    private StringBuilder query;

    /**
     *
     * @param table
     * @return
     */
    public Query delete(String table)
    {
        query = new StringBuilder();
        query.append("DELETE FROM ");
        query.append(table);
        return this;
    }

    /**
     * Přidá podmínku do sql dotazu
     * @param requirement
     * @return
     */
    public Query where(String requirement)
    {
        query.append(" WHERE ");
        query.append(requirement);
        return this;
    }

    /**
     *
     * @param table
     * @return
     */
    public Query from(String table)
    {
        query.append(" FROM ");
        query.append(table);
        return this;
    }

    //proměnné třídy
    //metody třídy
    /**
     *
     * @param table
     * @return
     */
    public Query update(String table){
        query = new StringBuilder();
        query.append("UPDATE ");
        query.append(table);
        query.append(" SET ");
        return this;
    }

    /**
     * Doplní sloupce
     * @param columns
     */
    public Query set(String[] columns){
        int count = columns.length;
        if(count == 0)
            throw new IllegalArgumentException("Neplatný počet parametrov");

        for(String column : columns){
            query.append(column);
            query.append(" = ");
            query.append("?");
            query.append(",");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        return this;
    }

    //proměnné třídy
    //metody třídy
    /**
     *
     * @param table
     * @return
     */
    public Query insert(String table)
    {
        query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(table);
        return this;
    }

    public Query create(String table)
    {
        query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS ");
        query.append(table);
        //query.append(table);
        return this;
    }

    /**
     *
     * @param params
     * @return
     */
    public Query values(List<Object> params)
    {
        query.append(" VALUES(");

        int count = params.size();

        if(count == 0)
            throw new IllegalArgumentException("Neplatný počet parametrov");

        for (int i = 0; i<count; i++) {
            query.append("?,");
        }
        //odstaníme poslední čárku
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(");");
        return this;
    }


    public Query fields(List<String[]> fields)
    {
        query.append(" (");
        String typ = "varchar";
        int count = fields.size();

        if(count == 0)
            throw new IllegalArgumentException("Neplatný počet parametrov");

        for(String[] field : fields)
        {
            if(field[1].equals(String.class.getName()))
                typ = "VARCHAR";
            if(field[1].equals(int.class.getName()) || field[1].equals(Integer.class.getName()) || field[1].equals(Long.class.getName()))
                typ = "INT";
            if(field[1].equals(float.class.getName()) || field[1].equals(double.class.getName()))
                typ = "DECIMAL";

            query.append(field[0] + " " + typ +"("+ field[2] + ") DEFAULT NULL,");
        }
        //odstraníme poslední čárku
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(");");
        return this;
    }
    //proměnné třídy
    //metody třídy
    /**
     * Vrátí sestavený sql dotaz
     * @return query
     */
    public String getQuery(){
        return query.toString();
    }
}
