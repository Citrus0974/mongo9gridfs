package edu.mongo9gridfs.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MongoGridFsService {
    MongoClient client;
    MongoDatabase database;
    GridFSBucket bucket;

    public MongoGridFsService(String connection, String database){
        this.client = MongoClients.create(connection);
        this.database = client.getDatabase(database);
        this.bucket = GridFSBuckets.create(this.database);
    }

    public Map<Object, List<String>> getFilesInfo(){
        Map<Object, List<String>> allFilesInfo = new LinkedHashMap<>();
        for(GridFSFile gridFSFile: bucket.find()){
            List<String> fileInfo = new ArrayList<>();
            fileInfo.add(gridFSFile.getFilename());
            fileInfo.add(formatFileSize(gridFSFile.getLength()));
            fileInfo.add(gridFSFile.getUploadDate().toString());
            allFilesInfo.put(gridFSFile.getId(), fileInfo);
        }
        return allFilesInfo;
    }

    private String formatFileSize(long byteSize){
        char[] sizeChars = {'k', 'm', 'g'};
        long orderSize = 1024L;
        if(byteSize < orderSize) return byteSize+" " +"B";
        for (char sizeChar : sizeChars) {
            byteSize /= 1024;
            if (byteSize < 1024) return byteSize + " " + sizeChar + "B";
        }
        return byteSize + " " + sizeChars[sizeChars.length-1] + "B";
    }

    public Object uploadFile(String filePath){
        File file = new File(filePath);
        Object uploaded = null;
        try(InputStream stream = Files.newInputStream(file.toPath())){
            uploaded = bucket.uploadFromStream(file.getName(), stream);
        } catch (IOException e){
            e.printStackTrace();
        }
        return uploaded;
    }

    public boolean downloadFile(Object objectId, String filePath){
        if(objectId==null){
            System.out.println("ObjectID is null. Cannot download");
            return false;
        }
        ObjectId fileId = (ObjectId) objectId;
        File file = new File(filePath);
        try(FileOutputStream stream = new FileOutputStream(file)){
            bucket.downloadToStream(fileId, stream);
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFile(Object objectId){
        if(objectId==null){
            System.out.println("ObjectID is null. Cannot delete");
            return false;
        }
        ObjectId fileId = (ObjectId) objectId;
        try{
            bucket.delete(fileId);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }





}
