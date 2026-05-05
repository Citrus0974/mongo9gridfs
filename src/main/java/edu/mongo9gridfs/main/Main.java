package edu.mongo9gridfs.main;

import edu.mongo9gridfs.gui.SWTFoxGUI;
import edu.mongo9gridfs.mongo.MongoGridFsService;

public class Main {
    public static void main(String[] args) {
        String connection = "mongodb://localhost:27017";
        String db = "sstu_db_deb_files";
        String title = "Database file manager: MongoDB GridFS";

        if(args.length>=1){
            connection = args[0];
        }
        if(args.length>=2){
            db=args[1];
        }
        if(args.length>=3){
            title = args[2];
        }
        if(args.length>3){
            System.out.println("too many args");
        }

        FileService service = new MongoGridFsService(connection, db);
        GUI gui = new SWTFoxGUI(service, title);
        gui.start();
    }
}
