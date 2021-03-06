package com.example.lucas.haushaltsmanager.Database.Repositories.Tags;

import com.example.lucas.haushaltsmanager.Database.DefaultDatabase;
import com.example.lucas.haushaltsmanager.Database.QueryResultInterface;
import com.example.lucas.haushaltsmanager.Database.Repositories.Tags.Queries.DeleteQuery;
import com.example.lucas.haushaltsmanager.Database.Repositories.Tags.Queries.GetAllQuery;
import com.example.lucas.haushaltsmanager.Database.Repositories.Tags.Queries.GetQuery;
import com.example.lucas.haushaltsmanager.Database.Repositories.Tags.Queries.InsertQuery;
import com.example.lucas.haushaltsmanager.Database.Repositories.Tags.Queries.UpdateQuery;
import com.example.lucas.haushaltsmanager.Entities.Tag;

import java.util.List;

public class Repository implements TagRepositoryInterface {
    private DefaultDatabase mDatabase;

    Repository(DefaultDatabase database) {
        mDatabase = database;
    }

    @Override
    public Tag save(Tag tag) {
        QueryResultInterface<Tag> result = mDatabase.query(new InsertQuery(tag));

        return result.getSingleResult();
    }

    public Tag find(long id) {
        QueryResultInterface<Tag> result = mDatabase.query(new GetQuery(id));

        return result.getSingleResult();
    }

    @Override
    public List<Tag> getAll() {
        QueryResultInterface<Tag> result = mDatabase.query(new GetAllQuery());

        return result.getAll();
    }

    @Override
    public void update(Tag tag) {
        QueryResultInterface<Tag> result = mDatabase.query(new UpdateQuery(tag));

        result.close();
    }

    @Override
    public void delete(Tag tag) {
        QueryResultInterface<Tag> result = mDatabase.query(new DeleteQuery(tag));

        result.close();
    }

    @Override
    public boolean exists(Tag tag) {
        return find(tag.getIndex()) == null;
    }
}
