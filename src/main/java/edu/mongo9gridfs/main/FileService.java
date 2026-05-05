package edu.mongo9gridfs.main;

import java.util.List;
import java.util.Map;

public interface FileService {
    Map<Object, List<String>> getFilesInfo();
    Object uploadFile(String filePath);
    boolean downloadFile(Object objectId, String filePath);
    boolean deleteFile(Object objectId);

}
