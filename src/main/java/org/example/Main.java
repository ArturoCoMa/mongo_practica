package org.example;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Filters.*;

public class Main {
    public static void main(String[] args) {
        int id;
        String titulo, director;
        double precio;
        Scanner tec = new Scanner(System.in);

        try(MongoClient mongoClient = new MongoClient("localhost")){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("Juegoteca2");
            MongoCollection mc = mongoDatabase.getCollection("Juegos2");

            //insert one
            Juego juego = new Juego(1,"InputAdventure", "Artur", 59.99);
            Document doc = new Document("_id",juego.get_id());
            doc.append("tituo",juego.getTitulo())
                    .append("director",juego.getDirector())
                    .append("precio",juego.getPrecio());
            mc.insertOne(doc);

            //insert many
            List<Document> listaJuegos = new ArrayList<>();
            for(int i=2 ; i<=4; i++){
                id = i;
                tec.nextLine();
                System.out.print("\ntitulo: ");
                titulo = tec.nextLine();
                System.out.print("\ndirector: ");
                director = tec.nextLine();
                System.out.print("\nprecio: ");
                precio = tec.nextDouble();

                Juego j = new Juego(id, titulo, director, precio);
                Document document = new Document();
                document.append("_id", j.get_id())
                        .append("titulo",j.getTitulo())
                        .append("director", j.getDirector())
                        .append("precio",j.getPrecio());
                listaJuegos.add(document);
            }
            mc.insertMany(listaJuegos);

            //consulta de uno (el juego con id 2)
            Document doc2 = (Document) mc.find(eq("_id",2)).first();
            System.out.println(doc2.toJson());

            //consulta todos
            muestrTodos(mc);


            //cosulta de varios (juegos con un precio entre 20 y 50)
            FindIterable<Document> iterable = mc.find(and(gte("precio",20),lte("precio",50)));
            MongoCursor cursor = iterable.iterator();
            while (cursor.hasNext()){
                System.out.println(cursor.next().toString());
            }

            //consulta 2 de varios (el nombre y precio de los tres juegos más caros)
            FindIterable<Document> iterable2 = mc.find(new Document())
                    .sort(descending("precio"))
                    .projection(fields(exclude("_id"),include("nombre", "precio")))
                    .limit(3);
            MongoCursor cursor2 = iterable2.iterator();
            while (cursor2.hasNext()){
                System.out.println(cursor2.next().toString());
            }


            //updates uno (pon el precio del juego con id 1 en 100)
            Bson filtro = eq("_id",1);
            Bson modificacion = set("precio",100);
            mc.updateOne(filtro,modificacion);

            System.out.println("Update uno primer ejemplo");
            muestrTodos(mc);

            //Update one 2 (incrementa el precio del segundo juego mas barato en 10)
            Document d = (Document) mc.find()
                    .sort(ascending("precio"))
                    .skip(1)
                    .limit(1);
            modificacion = inc("precio",10);
            mc.updateOne(d,modificacion);

            System.out.println("Update uno segundo ejemplo");
            muestrTodos(mc);


            //Update many (incrementa el precio de los tres mas caros en 100)
            FindIterable<Document> filtroVarios = mc.find().sort(descending("precio")).limit(3);
            Bson modificaVarios = inc("precio",100);
            MongoCursor cursorModifica = filtroVarios.iterator();
            while(cursorModifica.hasNext()){
                mc.updateOne((Bson) cursorModifica.next(),modificaVarios);
            }
            System.out.println("Update many");
            muestrTodos(mc);

            //Delete
            Bson deleteUno = eq("_id",3);
            mc.deleteOne(deleteUno);
            System.out.println("Delete");
            muestrTodos(mc);


        }catch (MongoException me){}
    }
    public static void muestrTodos(MongoCollection mc){
        //consulta todos
        FindIterable<Document> todos = mc.find();
        MongoCursor cursorTodos = todos.iterator();
        while(cursorTodos.hasNext()){
            System.out.println(cursorTodos.next().toString());
        }
    }
}