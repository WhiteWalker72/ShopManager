package net.whitewalker.shopmanager.persistence;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public abstract class DAOMongoImpl<T> implements DAO<T> {

    private final MongoCollection<Document> collection;

    DAOMongoImpl(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        collection.find().forEach((Block<? super Document>) doc -> list.add(docToDto(doc)));
        return list;
    }

    @Override
    public T findById(String id) {
        FindIterable<Document> findIterable = collection.find(new Document(getDocIdentifier(), id));
        Document result = findIterable.first();
        if (result == null) {
            return null;
        }

        return docToDto(result);
    }

    @Override
    public boolean insert(T dto) {
        if (findById(getIdentifier(dto)) == null) {
            collection.insertOne(dtoToDoc(dto));
            return true;
        }
        return false;
    }

    @Override
    public boolean update(T dto) {
        collection.updateOne(new Document(getDocIdentifier(), getIdentifier(dto)),
                new Document("$set", dtoToDoc(dto)));
        return false;
    }

    @Override
    public boolean delete(T dto) {
        return collection.findOneAndDelete(new Document(getDocIdentifier(), getIdentifier(dto))) != null;
    }

    public abstract String getDocIdentifier();

    public abstract String getIdentifier(T dto);

    public abstract T docToDto(Document doc);

    public abstract Document dtoToDoc(T dto);

}
