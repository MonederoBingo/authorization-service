package com.monederobingo.authorization.authorization;

public class SelectQuery
{
    private final String query;

    public SelectQuery(String query)
    {
        this.query = query;
    }

    public String getQuery()
    {
        return query;
    }
}
