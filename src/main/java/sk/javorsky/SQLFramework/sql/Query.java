package sk.javorsky.SQLFramework.sql;

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
            throw new IllegalArgumentException("Neplatný počet parametrů");

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

    /**
     *
     * @param params
     * @return
     */
    public Query values(Object[] params)
    {
        query.append(" VALUES(");

        int count = params.length;

        if(count == 0)
            throw new IllegalArgumentException("Neplatný počet parametrů");

        for (int i = 0; i<count; i++) {
            query.append("?,");
        }
        //odstaníme poslední čárku
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
